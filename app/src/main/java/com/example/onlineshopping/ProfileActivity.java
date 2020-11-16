package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profilePic;
    private TextView profileName,profileEmail,profilePhone;
    private Button profileEdit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic=findViewById(R.id.imProfilePic);
        profileName=findViewById(R.id.tvProfilename);
        profileEmail=findViewById(R.id.tvProfileEmail);
        profilePhone=findViewById(R.id.tvProfilePhone);
        profileEdit=findViewById(R.id.btnProfileEdit);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile= snapshot.getValue(UserProfile.class);
                profileName.setText("Email: " + userProfile.getUserName());
                profileEmail.setText("Name: " + userProfile.getUserEmail());
                profilePhone.setText("Phone: " + userProfile.getUserPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });



    }
}