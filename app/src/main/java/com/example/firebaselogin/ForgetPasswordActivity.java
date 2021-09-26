package com.example.firebaselogin;

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

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText et_f_pswd;
    Button btn_submit;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
         et_f_pswd = findViewById(R.id.f_et_pswd);
         btn_submit = findViewById(R.id.f_btn_save);
         auth = FirebaseAuth.getInstance();
         
         btn_submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 resetPassword();
             }
         });
    }

    private void resetPassword() {
        String email = et_f_pswd.getText().toString();
        if (email.isEmpty()){
            et_f_pswd.setError("please send the email");
            et_f_pswd.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetPasswordActivity.this,"check email for the reset password",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                }
                else {
                    Toast.makeText(ForgetPasswordActivity.this,"mail sending failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}