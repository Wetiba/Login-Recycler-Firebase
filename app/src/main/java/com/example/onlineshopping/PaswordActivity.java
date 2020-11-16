package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PaswordActivity extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasword);
        passwordEmail=(EditText)findViewById(R.id.edt_PasswordReset);
        resetPassword=(Button)findViewById(R.id.btn_PasswordReset);
        firebaseAuth=FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  useremail= passwordEmail.getText().toString().trim();

                if (useremail.equals("")){
                    Toast.makeText(PaswordActivity.this,"Please enter the Regestere ID",Toast.LENGTH_SHORT).show();
                } else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PaswordActivity.this,"Password reset email Sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PaswordActivity.this,LoginActivity.class));
                            }

                        }
                    });
                }

            }
        });
    }
}