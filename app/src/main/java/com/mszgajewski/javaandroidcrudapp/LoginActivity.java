package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mszgajewski.javaandroidcrudapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.loginQuestion.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        binding.loginBtn.setOnClickListener(view -> {
            binding.loginProgressBar.setVisibility(View.VISIBLE);
            String name = binding.loginEditUserName.getText().toString().trim();
            String pwd = binding.loginEditPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pwd)){
                Toast.makeText(LoginActivity.this, "Proszę sprawdzić dane", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mAuth.signInWithEmailAndPassword(name,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            binding.loginProgressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Zarejestrowano użytkownika", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            binding.loginProgressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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