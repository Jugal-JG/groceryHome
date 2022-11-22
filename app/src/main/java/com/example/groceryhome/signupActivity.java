package com.example.groceryhome;

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

public class signupActivity extends AppCompatActivity {

    private Button buttonSignup;
    private EditText Name,Email,Password,ConfirmPassword;
    private TextView Login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignup = findViewById(R.id.appCompatButton_signup);
        Name = findViewById(R.id.name_edit);
        Email = findViewById(R.id.email_edit);
        Password = findViewById(R.id.password_edit);
        Login = findViewById(R.id.login_in_signup);
        ConfirmPassword = findViewById(R.id.repassword_edit);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signupActivity.this,onboarding.class));
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

    }

    private void performAuth() {
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String pass = Password.getText().toString();
        String confirmpass = ConfirmPassword.getText().toString();



        if(name.isEmpty()){
            Name.setError("Enter a Name");
        }
        else if(!email.matches(emailPattern)){
            Email.setError("Enter email in a proper format");
        }
        else if (pass.isEmpty()|| pass.length()<8){
            Password.setError("Enter a length minimum of 8 characters");
        }
        else if(!pass.equals(confirmpass)){
            ConfirmPassword.setError("Passwords are not matching");
        }
        else{
            progressDialog.setMessage("Please wait while we Register");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(signupActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(signupActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(signupActivity.this, MainActivity.class);
        startActivity(intent);
    }
}