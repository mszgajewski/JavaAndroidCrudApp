package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mszgajewski.javaandroidcrudapp.databinding.ActivityEditBinding;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseId;
    private CourseRVModal courseRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        courseRVModal = getIntent().getParcelableExtra("course");
        if(courseRVModal != null){
            binding.editItemName.setText(courseRVModal.getItemName());
            binding.editItemPrice.setText(courseRVModal.getItemPrice());
            binding.editItemSuited.setText(courseRVModal.getItemSuited());
            binding.editItemImage.setText(courseRVModal.getItemImg());
            binding.editItemLink.setText(courseRVModal.getItemLink());
            binding.editItemDesc.setText(courseRVModal.getItemDesc());
            courseId = courseRVModal.getCourseId();
        }

        databaseReference = firebaseDatabase.getReference("Courses").child(courseId);

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editProgressBar.setVisibility(View.VISIBLE);
                String name  = binding.editItemName.getText().toString().trim();
                String price = binding.editItemPrice.getText().toString().trim();
                String suited= binding.editItemSuited.getText().toString().trim();
                String image = binding.editItemImage.getText().toString().trim();
                String link =  binding.editItemLink.getText().toString().trim();
                String description = binding.editItemDesc.getText().toString().trim();
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
                        binding.editProgressBar.setVisibility(View.GONE);
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

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
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