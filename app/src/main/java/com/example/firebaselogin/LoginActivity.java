package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    TextView tv_l_user,tv_l_forgot_pswd;
    Button login;
    EditText et_l_email,et_l_paswd;
    FirebaseAuth auth;
    //sign in from google account no1
    SignInButton signInButton;
    GoogleSignInClient mgoogleSignIn;
    public static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_l_user = findViewById(R.id.l_already_user);
        tv_l_forgot_pswd = findViewById(R.id.l_forget_pswd);
        login = findViewById(R.id.login_btn);
        et_l_email = findViewById(R.id.l_email);
        et_l_paswd = findViewById(R.id.l_pswd);
        signInButton = findViewById(R.id.l_sign_in);
        //no1
        requestGoogleSignIn();
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        tv_l_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        //forget password
        tv_l_forgot_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });
        // no1
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    //no1
    private void signIn() {
        Intent signInIntent = mgoogleSignIn.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    //no1
    private void requestGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignIn = GoogleSignIn.getClient(this,gso);

    }
    //store user into firebase no1
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Toast.makeText(LoginActivity.this,"failed" + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //no1
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this,"not done varification",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loginUser() {
        String email1 = et_l_email.getText().toString();
        String paswd1 = et_l_paswd.getText().toString();
        if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(paswd1)){
            Toast.makeText(LoginActivity.this,"enter details",Toast.LENGTH_SHORT).show();
        }
        else {
            auth.signInWithEmailAndPassword(email1,paswd1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //varification mail that no one can make fake email
                        if (auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"not done varification",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"oops!failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}