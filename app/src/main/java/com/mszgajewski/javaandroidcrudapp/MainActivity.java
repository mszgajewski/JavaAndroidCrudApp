package com.mszgajewski.javaandroidcrudapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView itemRV;
    private ProgressBar progressBar;
    private FloatingActionButton addFAB;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private RelativeLayout sheetRelativeLayout;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemRV = findViewById(R.id.RVCourses);
        progressBar = findViewById(R.id.progressBar);
        addFAB = findViewById(R.id.FAB);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        sheetRelativeLayout = findViewById(R.id.bottomSheet);
        courseRVModalArrayList = new ArrayList<>();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList,this, this);
        itemRV.setLayoutManager(new LinearLayoutManager(this));
        itemRV.setAdapter(courseRVAdapter);
        addFAB.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,AddActivity.class)));

        getAllItems();

    }

    private void getAllItems(){
        courseRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                courseRVModalArrayList.add(snapshot.getValue(CourseRVModal.class));
                courseRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
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

    private void displayBottomSheet(CourseRVModal courseRVModal){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,sheetRelativeLayout);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView itemNameTV = layout.findViewById(R.id.bottomTextView);
        TextView itemDescTV = layout.findViewById(R.id.bottomDescTextView);
        TextView itemSuitedTV = layout.findViewById(R.id.bottomSuitedForTextView);
        TextView itemPriceTV = layout.findViewById(R.id.bottomPriceTextView);
        ImageView itemIV = layout.findViewById(R.id.bottomImageView);
        Button editButton = layout.findViewById(R.id.editBottomButton);
        Button detailsButton = layout.findViewById(R.id.detailsButton);


        itemNameTV.setText(courseRVModal.getItemName());
        itemDescTV.setText(courseRVModal.getItemDesc());
        itemSuitedTV.setText(courseRVModal.getItemSuited());
        itemPriceTV.setText(courseRVModal.getItemPrice()+"zł");
        Picasso.get().load(courseRVModal.getItemImg()).into(itemIV);

        editButton.setOnClickListener(view ->
            startActivity(new Intent(MainActivity.this,EditActivity.class)
                    .putExtra("course", courseRVModal)));

        detailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(courseRVModal.getItemLink()));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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