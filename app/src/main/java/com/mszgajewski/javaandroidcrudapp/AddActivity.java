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

public class AddActivity extends AppCompatActivity {

    private TextInputEditText itemName, itemPrice, itemSuited, itemImg, itemLink, itemDesc;
    private Button addButton;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        itemName = findViewById(R.id.addItemName);
        itemPrice = findViewById(R.id.addItemPrice);
        itemSuited = findViewById(R.id.addItemSuited);
        itemImg = findViewById(R.id.addItemImageLink);
        itemLink = findViewById(R.id.addItemLink);
        itemDesc = findViewById(R.id.addItemDesc);
        addButton = findViewById(R.id.addBtn);
        progressBar = findViewById(R.id.addProgressBar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name  = itemName.getText().toString().trim();
                String price = itemPrice.getText().toString().trim();
                String suited = itemSuited.getText().toString().trim();
                String image = itemImg.getText().toString().trim();
                String link = itemLink.getText().toString().trim();
                String description = itemDesc.getText().toString().trim();
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