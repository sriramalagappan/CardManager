package com.example.ewallet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ewallet.R;
import com.example.ewallet.Util.UserErrorHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity {

    //UI References
    private EditText inputEmail, inputPassword, passwordCheck;
    private Button btnNext;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById((R.id.inputPassword));
        passwordCheck = findViewById(R.id.passwordCheck);
        btnNext = findViewById((R.id.btnNext));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String passCheck = passwordCheck.getText().toString();

                if (password.equals(passCheck)) {
                    try {
                        if (password.length() > 5 && !email.equals("")) {
                            auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        UserErrorHandler error = new UserErrorHandler(((FirebaseAuthException) task.getException()).getErrorCode());
                                        toastMsg(error.getErrorResponse());
                                    } else {
                                        toastMsg("Registration Successful. Now Login");
                                        returnToMainActivity();
                                    }
                                }
                            });
                        } else {
                            toastMsg("Please fill all fields. Password must be at least 6 characters");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    toastMsg("Passwords do not match");
                }
            }
        });
    }
    private void toastMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
