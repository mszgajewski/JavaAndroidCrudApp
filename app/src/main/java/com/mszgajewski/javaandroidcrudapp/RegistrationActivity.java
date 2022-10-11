package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText userNameEdit, passwordEdit, confirmPasswordEdit;
    private Button regButton;
    private ProgressBar progressBar;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userNameEdit = findViewById(R.id.regEditUserName);
        passwordEdit = findViewById(R.id.regEditPassword);
        confirmPasswordEdit = findViewById(R.id.regCnfPassword);
        regButton = findViewById(R.id.regBtn);
        progressBar = findViewById(R.id.regProgressBar);
        loginTextView = findViewById(R.id.regQuestion);
        mAuth = FirebaseAuth.getInstance();

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String userName = userNameEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String confirmPassword = confirmPasswordEdit.getText().toString().trim();

                if (!password.equals(confirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Proszę sprawdić hasło", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Proszę sprawdić dane", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Zarejestrowano użytkownika", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}