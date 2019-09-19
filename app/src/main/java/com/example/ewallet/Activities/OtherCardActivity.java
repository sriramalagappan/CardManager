package com.example.ewallet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.Cards.OtherCards;
import com.example.ewallet.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

public class OtherCardActivity extends AppCompatActivity {

    //UI
    Button btnAddOtherCard;
    TextView txtNameO, txtLocationO, intValue, intCashBackO;

    SharedPreferences mPrefs;

    private Integer numCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_card);

        mPrefs = this.getSharedPreferences("com.example.ewallet", Context.MODE_PRIVATE);
        numCards = mPrefs.getInt("numCards", 0);

        btnAddOtherCard = findViewById(R.id.btnAddOtherCard);
        txtNameO = findViewById(R.id.txtName);
        txtLocationO = findViewById(R.id.txtLocationO);
        intValue = findViewById(R.id.intValue);
        intCashBackO = findViewById(R.id.intCashBackO);

        btnAddOtherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = txtLocationO.getText().toString();
                if (loc.equals("") || txtNameO.getText().toString().equals("") || intValue.getText().toString().equals("") || intCashBackO.getText().toString().equals("")) {
                    toastMsg("Please fill in all the fields");
                } else {
                    OtherCards otherCards = new OtherCards(txtNameO.getText().toString());
                    otherCards.setLocation(loc);
                    otherCards.setValue(Integer.parseInt(intValue.getText().toString()));
                    otherCards.setCashBack(Integer.parseInt(intCashBackO.getText().toString()));

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(otherCards);
                    prefsEditor.putString(numCards.toString(), json);
                    prefsEditor.commit();
                    mPrefs.edit().putInt("numCards", (numCards + 1)).commit();
                    sendHome();
                }
            }
        });
    }

    private void sendHome() {
        Intent intent = new Intent (this, HomeActivity.class);
        startActivity(intent);
    }

    private void toastMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
