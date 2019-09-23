package com.example.ewallet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {
    //UI
    TextView txtCardName, txtCardLocation, txtCardCashBack, txtCardPoints, txtNumber;
    Button btnUpdate, btnAddCard;
    SeekBar barDistance;

    //Shared Prefs
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
    private int PROXIMITY_RADIUS = 50;

    //Card info
    private Integer numCards;
    private Integer numCreditCards;
    private int lengthOfDetails;
    private String[] placeDetails;
    private String sameName;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //scheduleAlarm();

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

        //Initialize Places with API Key
        Places.initialize(getApplicationContext(), "AIzaSyDEiGk3Heoodq_mArpQQU4PZQtIllgwtlU");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Get Current Location
        getLastKnownLocation();

        //Get amount of cards
        numCards = mPrefs.getInt("numCards", 0);
        numCreditCards = mPrefs.getInt("numCreditCards", 0);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareCredit();
                compareGift();
            }
        });

        //Send user to activity to create a new card
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardActivity();
            }
        });

        //Change proximity (take out in final version) (only used for testing)
        //TODO
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

    //Method to create toast msgs
    private void toastMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    //Get users location
    private void getLastKnownLocation() {
        //check if permission was granted
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //If permission is granted, get lat / long coordinates
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace, boolean isName)
    {
        StringBuilder googlePlaceUrl;
        //Create a different url depending on parameter
        if (isName) {
            googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            googlePlaceUrl.append("input="+nearbyPlace);
            googlePlaceUrl.append("&location=" + latitude + "," + longitude);
            googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
            googlePlaceUrl.append("&strictbounds");
            googlePlaceUrl.append("&key=" + "AIzaSyCrWq_uhXQwi7zFdNjqq60auYdo6IMiGRU");
        } else {
            googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlaceUrl.append("location=" + latitude + "," + longitude);
            googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlaceUrl.append("&type=" + nearbyPlace);
            googlePlaceUrl.append("&sensor=true");
            googlePlaceUrl.append("&key=" + "AIzaSyCrWq_uhXQwi7zFdNjqq60auYdo6IMiGRU");
        }
        Log.d("HomeActivity", "url = "+googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }

    private void addCardActivity() {
        Intent intent = new Intent(this, AddCard.class);
        startActivity(intent);
    }

    // bool isName represents whether the program should create a URL to find a
    // place with a specific name (ex. Target) or to find a general place t
    // type (ex. grocery store)
    private void getPlace(String str, boolean isName) {
        Object[] dataUrl = new Object[1];
        GetNearbyLocations getNearbyLocations = new GetNearbyLocations();

        String url;

        url = getUrl(latitude, longitude, str, isName);

        dataUrl[0] = url;

        //Store place details
        try {
            details = getNearbyLocations.execute(dataUrl).get();
            placeName = details.get("place_name");
            lengthOfDetails = Integer.parseInt(details.get("length"));
            placeDetails = new String[lengthOfDetails];
            for (int i = 0; i < lengthOfDetails; i++) {
                placeDetails[i] = details.get("" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compareGift() {
        getLastKnownLocation();
        if (isLocation) {
            for (Integer i = 0; i < numCards; i++) {
                String key = i.toString();
                Gson gson = new Gson();
                String json = mPrefs.getString(key, null);
                OtherCards card = gson.fromJson(json, OtherCards.class);

                getPlace(card.getLocation(), true);

                if (placeName.contains(card.getLocation())) {
                    setUIText(card.getName(), card.getLocation(), "", "" + card.getCashBack());
                } else {
                    toastMsg("No matches were found");
                }
            }
        }
    }

    private void compareCredit() {
        getLastKnownLocation();
        if (isLocation) {
            for (Integer i = 0; i < numCreditCards; i++) {
                String key = "CC" + i.toString();
                Gson gson = new Gson();
                String json = mPrefs.getString(key, null);
                CreditCard card = gson.fromJson(json, CreditCard.class);
                List<Object[]> temp = card.getRewards();

                for (int j = 0; j < temp.size(); j++) {
                    Object[] obj = temp.get(j);
                    String place = obj[2].toString();
                    double start = (double) (obj[0]);
                    double end = (double) (obj[1]);
                    if (isDate((int)start, (int)end)) {
                        if (card.getIsNameGiven()) {
                            getPlace(place, true);
                            if (placeName.contains(place)) {
                                setUIText(card.getName(), place, obj[3].toString(), "" + card.getCashBack());
                            } else {
                                toastMsg("No matches were found");
                            }
                        } else {
                            getPlace(place, false);
                            for (int k = 0; k < lengthOfDetails; k++) {
                                if (place.equals(placeDetails[k])) {
                                    setUIText(card.getName(), place, obj[3].toString(), "" + card.getCashBack());
                                }
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

    /*public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }*/

    public void setUIText(String cardName, String cardLocation, String cardPoints, String cardCashBack) {
        txtCardName.setText(cardName);
        txtCardLocation.setText(cardLocation);
        txtCardPoints.setText(cardPoints);
        txtCardCashBack.setText(cardCashBack);
        displayNotification(cardName);
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
