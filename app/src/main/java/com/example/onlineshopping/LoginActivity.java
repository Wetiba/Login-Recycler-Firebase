package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername,edtPassword;
    private Button btnLogin;
    private TextView tvInfo,tvRegester;
    private int counter= 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgetpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername=(EditText) findViewById(R.id.edt_Username);
        edtPassword=(EditText) findViewById(R.id.edt_Password);
        btnLogin= (Button) findViewById(R.id.btn_Login);
        tvInfo=(TextView) findViewById(R.id.tv_Info);
        tvRegester=(TextView) findViewById(R.id.tv_Register);
        forgetpassword=(TextView)findViewById(R.id.tv_ForgotPassword);

        tvInfo.setText("No of attempts remaining 3");
        firebaseAuth  = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        FirebaseUser user= firebaseAuth.getCurrentUser();

        if (user !=null){
            finish();
            startActivity(new Intent(LoginActivity.this,PostingActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputname =edtUsername.getText().toString();
                String inputpassword= edtPassword.getText().toString();
                if (inputname.isEmpty()||inputpassword.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please fill all the details",Toast.LENGTH_SHORT).show();
                }else {
                    validate(inputname,inputpassword);
                }
            }
        });
        tvRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PaswordActivity.class));
            }
        });
    }
    private void validate(String userName,String userPassword){
        progressDialog.setMessage("Welcome");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    //Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                  checkEmailVerification();
                } else{
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    counter --;
                    tvInfo.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if (counter == 0){
                        btnLogin.setEnabled(false);
                    }
                }

            }
        });

    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser= firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag= firebaseUser.isEmailVerified();
        if (emailFlag){
            finish();
            startActivity(new Intent(LoginActivity.this,PostingActivity.class));
        }else{
            Toast.makeText(this,"Please Verify Your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}