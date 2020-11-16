package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PostingActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button logOut;
    private Button openItemActivitybtn,openUploadActivitybtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        firebaseAuth = FirebaseAuth.getInstance();
        logOut=findViewById(R.id.btn_posting);
        openItemActivitybtn=findViewById(R.id.btn_view);
        openUploadActivitybtn=findViewById(R.id.btn_post);

        openItemActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(PostingActivity.this,ItemActivity.class);
                startActivity(i);
            }
        });
        openUploadActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PostingActivity.this,UploadActivity.class);
                startActivity(i);
            }
        });








        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();

            }
        });
    }
    private void  Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(PostingActivity.this,LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:{
                Logout();

            }
            case R.id.profileMenu:
                startActivity(new Intent(PostingActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}