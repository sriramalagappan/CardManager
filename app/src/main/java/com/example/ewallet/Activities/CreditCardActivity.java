package com.example.ewallet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.Cards.CreditCard;
import com.example.ewallet.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreditCardActivity extends AppCompatActivity {

    //UI
    Button btnAddCardC, btnRewards;
    TextView txtNameC, intCashBackC, txtDate, txtDateEnd, txtPlace, intPoints, intUSD;
    Switch switchPlace;
    Spinner spinner;

    SharedPreferences mPrefs;

    private int startDate;
    private int endDate;

    private Integer numCreditCards;

    private List<Object[]> rewards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);


        mPrefs = this.getSharedPreferences("com.example.ewallet", Context.MODE_PRIVATE);
        numCreditCards = mPrefs.getInt("numCreditCards", 0);

        btnAddCardC = findViewById(R.id.btnAddCardC);
        btnRewards = findViewById(R.id.btnRewards);
        txtNameC = findViewById(R.id.txtNameC);
        intCashBackC = findViewById(R.id.intCashBackC);
        txtDate = findViewById(R.id.txtDate);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        switchPlace = findViewById(R.id.switchPlace);
        txtPlace = findViewById(R.id.txtPlace);
        spinner = findViewById(R.id.spinner);
        intPoints = findViewById(R.id.intPoints);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog monthDatePickerDialog = new DatePickerDialog(CreditCardActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int temp = month + 1;
                        txtDate.setText("" + (temp));
                        startDate = temp;
                    }
                }, year, month, day) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
                        getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);

                    }
                };
                monthDatePickerDialog.setTitle("Select Starting Date");
                monthDatePickerDialog.show();
            }
        });

        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog monthDatePickerDialog = new DatePickerDialog(CreditCardActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int temp = month + 1;
                        txtDateEnd.setText("" + temp);
                        endDate = temp;
                    }
                }, year, month, day) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
                        getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);

                    }
                };
                monthDatePickerDialog.setTitle("Select Ending Date");
                monthDatePickerDialog.show();
            }
        });

        btnRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object[] obj = new Object[4];
                obj[0] = startDate;
                obj[1] = endDate;
                if (!(switchPlace.isChecked())) {
                    obj[2] = txtPlace.getText().toString();
                } else {
                    obj[2] = spinner.getSelectedItem().toString();
                }
                obj[3] = Integer.parseInt(intPoints.getText().toString());
                rewards.add(obj);
            }
        });

        btnAddCardC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNameC.getText().toString().equals("") || intCashBackC.getText().toString().equals("")) {
                    toastMsg("Please fill in at least the first fields");
                }

                CreditCard creditCard = new CreditCard(txtNameC.getText().toString());
                creditCard.setCashBack(Integer.parseInt(intCashBackC.getText().toString()));
                creditCard.setRewards(rewards);
                creditCard.setIsNameGiven(!switchPlace.isChecked());

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(creditCard);
                prefsEditor.putString("CC" + numCreditCards.toString(), json);
                prefsEditor.commit();
                mPrefs.edit().putInt("numCreditCards", (numCreditCards + 1)).commit();
                sendHome();
            }
        });
    }

    private void sendHome() {
        toastMsg("Card Created");
        Intent intent = new Intent (this, HomeActivity.class);
        startActivity(intent);
    }

    private void toastMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
