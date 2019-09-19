package com.example.ewallet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.Cards.CreditCard;
import com.example.ewallet.Cards.OtherCards;
import com.example.ewallet.R;
import com.example.ewallet.Util.AlarmReciever;
import com.example.ewallet.Util.GetNearbyLocations;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {
    //UI
    TextView txtCardName, txtCardLocation, txtCardCashBack, txtCardPoints, txtNumber;
    Button btnUpdate, btnAddCard;
    SeekBar barDistance;

    SharedPreferences mPrefs;

    //Notification
    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;

    //Location
    private double latitude;
    private double longitude;
    boolean isLocation = true;

    //Place Information
    private HashMap<String,String> details;
    private String placeName = " ";
    int PROXIMITY_RADIUS = 50;

    //Card info
    Integer numCards;
    Integer numCreditCards;

    int lengthOfDeets;
    String[] deets;
    private String sameName;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scheduleAlarm();

        mPrefs = this.getSharedPreferences("com.example.ewallet", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtCardName = findViewById(R.id.txtCardName);
        txtCardLocation = findViewById(R.id.txtCardLocation);
        txtCardCashBack = findViewById(R.id.txtCardCashBack);
        txtCardPoints = findViewById(R.id.txtCardPoints);
        txtNumber = findViewById(R.id.txtNumber);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnAddCard = findViewById(R.id.btnAddCard);

        barDistance = findViewById(R.id.barDistance);

        Places.initialize(getApplicationContext(), "AIzaSyDEiGk3Heoodq_mArpQQU4PZQtIllgwtlU");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastKnownLocation();

        numCards = mPrefs.getInt("numCards", 0);
        numCreditCards = mPrefs.getInt("numCreditCards", 0);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareCredit();
                compare();
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardActivity();
            }
        });

        barDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PROXIMITY_RADIUS = progress;
                txtNumber.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void toastMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void getLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if (latitude == 0 || longitude == 0) {
                        //getLastKnownLocation();
                    }
                }
            }
        });
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyCrWq_uhXQwi7zFdNjqq60auYdo6IMiGRU");

        Log.d("HomeActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private String getUrlName (double latitude , double longitude , String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
        googlePlaceUrl.append("input="+nearbyPlace);
        googlePlaceUrl.append("&location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&strictbounds");
        googlePlaceUrl.append("&key=" + "AIzaSyCrWq_uhXQwi7zFdNjqq60auYdo6IMiGRU");

        Log.d("HomeActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private void addCardActivity() {
        Intent intent = new Intent(this, AddCard.class);
        startActivity(intent);
    }

    private void getPlace(String str, boolean b) {
        Object[] dataUrl = new Object[1];
        GetNearbyLocations getNearbyLocations = new GetNearbyLocations();

        String url;

        if (b) {
            url = getUrl(latitude, longitude, str);
        } else {
            url = getUrlName(latitude,longitude,str);
        }

        dataUrl[0] = url;

        try {
            details = getNearbyLocations.execute(dataUrl).get();
            placeName = details.get("place_name");
            lengthOfDeets = Integer.parseInt(details.get("length"));
            deets = new String[lengthOfDeets];
            for (int i = 0; i < lengthOfDeets; i++) {
                deets[i] = details.get("" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compare() {
        getLastKnownLocation();
        Log.d("HomeActivity", numCards.toString());
        if (isLocation) {
            for (Integer i = 0; i < numCards; i++) {
                String key = i.toString();
                Gson gson = new Gson();
                String json = mPrefs.getString(key, null);
                OtherCards card = gson.fromJson(json, OtherCards.class);

                getPlace(card.getLocation(), false);

                //toastMsg(numCards + " " + placeName + " " + card.getLocation());

                if (placeName.contains(card.getLocation())) {
                    displayNotification(card.getName());
                    txtCardName.setText(card.getName());
                    txtCardLocation.setText(card.getLocation());
                    txtCardPoints.setText("" + card.getCashBack());
                    txtCardCashBack.setText("");
                } else {
                    //btnUpdate.setText(placeName);
                }
                Log.d("asd", numCards.toString());
            }
        }
    }

    private void compareCredit() {
        getLastKnownLocation();
        if (isLocation) {
            for (Integer i = 0; i < numCreditCards; i++) {
                String key = "CC " + i.toString();
                Gson gson = new Gson();
                String json = mPrefs.getString(key, null);
                CreditCard card = gson.fromJson(json, CreditCard.class);
                List<Object[]> temp = card.getRewards();
                if (card.getPlace()) {
                    for (int j = 0; j < temp.size(); j++) {
                        Object[] obj = temp.get(j);
                        getPlace(obj[2].toString(), true);
                        double start = (double) (obj[0]);
                        double end = (double) (obj[1]);
                        if (isDate((int)start, (int)end)) {
                            for (int k = 0; k < lengthOfDeets; k++) {
                                if (obj[2].toString().equals(deets[k])) {
                                    displayNotification(card.getName());
                                    txtCardName.setText(card.getName());
                                    txtCardLocation.setText(obj[2].toString());
                                    txtCardPoints.setText(obj[3].toString());
                                    txtCardCashBack.setText(card.getCashBack());
                                }
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < temp.size(); j++) {
                        Object[] obj = temp.get(j);
                        getPlace(obj[2].toString(), false);
                        double start = (double) (obj[0]);
                        double end = (double) (obj[1]);
                        if (isDate((int)start, (int)end)) {
                            if (placeName.contains(obj[2].toString())) {
                                txtCardName.setText(card.getName());
                                txtCardLocation.setText(obj[2].toString());
                                txtCardPoints.setText(obj[3].toString());
                                txtCardCashBack.setText("" + card.getCashBack());
                                displayNotification(card.getName());
                            } else {
                                //TODO
                            }

                        }
                    }
                }
            }
        }
    }

    public void displayNotification(String str) {
        CharSequence name = "EWallet";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_card_notification)
                .setContentTitle("EWallet")
                .setContentText(str);

        if (!(placeName.equals(sameName))) {
            sameName = placeName;
            NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(NOTIFICATION_ID, notification.build());
        }
    }

    public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private boolean isDate(int start, int end) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String currentDate = df.format(d);

        int month = Integer.parseInt(currentDate);

        if (start <= month && month < end) {
            return true;
        }
        return false;
    }
}
