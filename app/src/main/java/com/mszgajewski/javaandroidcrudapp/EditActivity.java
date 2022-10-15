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

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private TextInputEditText itemName, itemPrice, itemSuited, itemImg, itemLink, itemDesc;
    private Button editButton, deleteButton;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseId;
    private CourseRVModal courseRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        itemName = findViewById(R.id.editItemName);
        itemPrice = findViewById(R.id.editItemPrice);
        itemSuited = findViewById(R.id.editItemSuited);
        itemImg = findViewById(R.id.editItemImage);
        itemLink = findViewById(R.id.editItemLink);
        itemDesc = findViewById(R.id.editItemDesc);
        editButton = findViewById(R.id.editBtn);
        deleteButton = findViewById(R.id.deleteBtn);
        progressBar = findViewById(R.id.editProgressBar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        courseRVModal = getIntent().getParcelableExtra("course");
        if(courseRVModal != null){
            itemName.setText(courseRVModal.getItemName());
            itemPrice.setText(courseRVModal.getItemPrice());
            itemSuited.setText(courseRVModal.getItemSuited());
            itemImg.setText(courseRVModal.getItemImg());
            itemLink.setText(courseRVModal.getItemLink());
            itemDesc.setText(courseRVModal.getItemDesc());
            courseId = courseRVModal.getCourseId();
        }

        databaseReference = firebaseDatabase.getReference("Courses").child(courseId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name  = itemName.getText().toString().trim();
                String price = itemPrice.getText().toString().trim();
                String suited= itemSuited.getText().toString().trim();
                String image = itemImg.getText().toString().trim();
                String link =  itemLink.getText().toString().trim();
                String description = itemDesc.getText().toString().trim();
                courseId = name;

                Map <String,Object> map = new HashMap<>();
                map.put("itemName", name);
                map.put("itemPrice",price);
                map.put("itemSuited",suited);
                map.put("itemImg",image);
                map.put("itemLink",link);
                map.put("itemDesc",description);
                map.put("courseId",courseId);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditActivity.this, "Edytowano", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditActivity.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }
        });
    }

    private void deleteCourse(){
        databaseReference.removeValue();
        Toast.makeText(this, "Usunięto", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditActivity.this,MainActivity.class));
    }

}