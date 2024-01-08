package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mszgajewski.javaandroidcrudapp.databinding.ActivityMainBinding;
import com.mszgajewski.javaandroidcrudapp.databinding.BottomSheetDialogBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    ActivityMainBinding binding;
    BottomSheetDialogBinding bottomSheetDialogBinding;

    private DatabaseReference databaseReference;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        courseRVModalArrayList = new ArrayList<>();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList,this, this);
        binding.RVCourses.setLayoutManager(new LinearLayoutManager(this));
        binding.RVCourses.setAdapter(courseRVAdapter);
        binding.FAB.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,AddActivity.class)));

        getAllItems();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllItems(){
        courseRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                binding.progressBar.setVisibility(View.GONE);
                courseRVModalArrayList.add(snapshot.getValue(CourseRVModal.class));
                courseRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                binding.progressBar.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                binding.progressBar.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                binding.progressBar.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCourseClick(int position) {
        displayBottomSheet(courseRVModalArrayList.get(position));
    }

    @SuppressLint("SetTextI18n")
    private void displayBottomSheet(CourseRVModal courseRVModal){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialogBinding = BottomSheetDialogBinding.inflate(getLayoutInflater());

        bottomSheetDialog.setContentView(bottomSheetDialogBinding.getRoot());
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        bottomSheetDialogBinding.bottomTextView.setText(courseRVModal.getItemName());
        bottomSheetDialogBinding.bottomDescTextView.setText(courseRVModal.getItemDesc());
        bottomSheetDialogBinding.bottomSuitedForTextView.setText(courseRVModal.getItemSuited());
        bottomSheetDialogBinding.bottomPriceTextView.setText(courseRVModal.getItemPrice()+"zł");
        Picasso.get().load(courseRVModal.getItemImg()).into(bottomSheetDialogBinding.bottomImageView);

        bottomSheetDialogBinding.editBottomButton.setOnClickListener(view ->
            startActivity(new Intent(MainActivity.this,EditActivity.class)
                    .putExtra("course", courseRVModal)));

        bottomSheetDialogBinding.detailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(courseRVModal.getItemLink()));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.LogOut) {
            Toast.makeText(this, "Wylogowano", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}