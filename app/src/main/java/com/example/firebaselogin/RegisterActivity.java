package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextView tv_r_user;
    Button register;
    EditText et_r_email,et_r_pswd;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_r_user = findViewById(R.id.r_already_user);
        register = findViewById(R.id.register_btn);
        et_r_email = findViewById(R.id.r_email);
        et_r_pswd = findViewById(R.id.r_pswd);

        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatUser();
            }
        });
        tv_r_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private void creatUser() {
        String email = et_r_email.getText().toString();
        String pswd = et_r_pswd.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pswd)){
            Toast.makeText(RegisterActivity.this,"enter details",Toast.LENGTH_SHORT).show();
        }
        else {
            auth.createUserWithEmailAndPassword(email,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //varification mail that no one can make fake email
                        FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterActivity.this,"varification mail sent",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this,"mail sending fail",Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Toast.makeText(RegisterActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                        }
                    else {
                        Toast.makeText(RegisterActivity.this,"oops!failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}