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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userNameEdit, passwordEdit;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView registerTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEdit = findViewById(R.id.loginEditUserName);
        passwordEdit = findViewById(R.id.loginEditPassword);
        loginButton = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.loginProgressBar);
        registerTextView = findViewById(R.id.loginQuestion);
        mAuth = FirebaseAuth.getInstance();

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String userName = userNameEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Proszę sprawdzić dane", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Zarejestrowano użytkownika", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}