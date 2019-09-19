package com.example.ewallet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.Cards.DebitCard;
import com.example.ewallet.Cards.OtherCards;
import com.example.ewallet.R;
import com.google.gson.Gson;

public class DebitCardActivity extends AppCompatActivity {

    //UI
    Button btnAddCardD;
    TextView txtNameD, intCashBackD;

    SharedPreferences mPrefs;

    private Integer numCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_card);

        mPrefs = this.getSharedPreferences("com.example.ewallet", Context.MODE_PRIVATE);
        numCards = mPrefs.getInt("numCards", 0);

        btnAddCardD = findViewById(R.id.btnAddCardD);
        txtNameD = findViewById(R.id.txtNameD);
        intCashBackD = findViewById(R.id.intCashBackD);

        btnAddCardD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNameD.getText().toString().equals("") || intCashBackD.getText().toString().equals("")) {
                    toastMsg("Please fill in all the fields");
                } else {
                    DebitCard debitCard = new DebitCard(txtNameD.getText().toString());
                    debitCard.setCashBack(Integer.parseInt(intCashBackD.getText().toString()));

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(debitCard);
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
