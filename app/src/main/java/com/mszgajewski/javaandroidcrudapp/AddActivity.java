package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mszgajewski.javaandroidcrudapp.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;

    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add);

        progressBar = findViewById(R.id.addProgressBar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name  = binding.addItemName.getText().toString().trim();
                String price = binding.addItemPrice.getText().toString().trim();
                String suited = binding.addItemSuited.getText().toString().trim();
                String image = binding.addItemImageLink.getText().toString().trim();
                String link = binding.addItemLink.getText().toString().trim();
                String description = binding.addItemDesc.getText().toString().trim();
                courseId = name;

                CourseRVModal courseRVModal = new CourseRVModal(name, price, suited, image, link, description, courseId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        databaseReference.child(courseId).setValue(courseRVModal);
                        Toast.makeText(AddActivity.this,"Dodano", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddActivity.this, "Błąd " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}