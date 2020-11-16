package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText userName,userEmail,userPhone,userPassword;
    private Button regButton;
    private TextView userLogging;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilepic;
    String name,email,phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //update to the database
                    String user_email=userEmail.getText().toString().trim();
                    String user_password=userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendEmailVerification();

                            }else{
                                Toast.makeText(RegistrationActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });
        userLogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userName=(EditText)findViewById(R.id.edt_Name);
        userEmail=(EditText)findViewById(R.id.edt_Email);
        userPhone=(EditText)findViewById(R.id.edt_Phone);
        userPassword=(EditText)findViewById(R.id.edt_Passwordreg);
        regButton=(Button)findViewById(R.id.btn_Submit);
        userLogging=(TextView)findViewById(R.id.tv_logging);
        userProfilepic=(ImageView)findViewById(R.id.iv_Profilepic);
    }
    private Boolean validate(){
        Boolean result = false;
        name= userName.getText().toString();
        email=userEmail.getText().toString();
        phone =userPhone.getText().toString();
        password=userPassword.getText().toString();

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please Fill all the details",Toast.LENGTH_SHORT).show();
        }else {
            result = true;

        }
        return result;
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this,"Successful Registered, Verification email sent!",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this,"Verificationn not sent",Toast.LENGTH_SHORT);
                    }

                }
            });
        }
    }
    private void sendUserData(){

        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile= new UserProfile(name, email, phone);
        myRef.setValue(userProfile);

    }
}