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

    private TextInputEditText userName, password, confirmPassword;
    private Button regButton;
    private ProgressBar progressBar;
    private TextView regQuestion;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userName = findViewById(R.id.regEditUserName);
        password = findViewById(R.id.regEditPassword);
        confirmPassword = findViewById(R.id.regEditCnfPassword);
        regButton = findViewById(R.id.regBtn);
        progressBar = findViewById(R.id.regProgressBar);
        regQuestion = findViewById(R.id.regQuestion);
        mAuth = FirebaseAuth.getInstance();

        regQuestion.setOnClickListener(new View.OnClickListener() {
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
                String name = userName.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                String cnfPassword = confirmPassword.getText().toString().trim();

                if (!pwd.equals(cnfPassword)){
                    Toast.makeText(RegistrationActivity.this, "Proszę sprawdić hasło", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPassword)){
                    Toast.makeText(RegistrationActivity.this, "Proszę sprawdić dane", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mAuth.createUserWithEmailAndPassword(name,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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