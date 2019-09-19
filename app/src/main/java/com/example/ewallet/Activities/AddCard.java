package com.example.ewallet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ewallet.R;

public class AddCard extends AppCompatActivity {

    //UI
    Button btnCredit, btnDebit, btnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        btnOther = findViewById(R.id.btnOther);
        btnDebit = findViewById(R.id.btnDebit);
        btnCredit = findViewById(R.id.btnCredit);

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherActivity();
            }
        });

        btnDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debitActivity();
            }
        });

        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditActivity();
            }
        });
    }

    private void otherActivity() {
        Intent intent = new Intent(this, OtherCardActivity.class);
        startActivity(intent);
    }

    private void debitActivity() {
        Intent intent = new Intent(this, DebitCardActivity.class);
        startActivity(intent);
    }

    private void CreditActivity() {
        Intent intent = new Intent(this, CreditCardActivity.class);
        startActivity(intent);
    }
}
