package com.psm.sosmyv3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private Button btnLogIn2;
    private Button btnLogOut;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogIn2 = (Button) findViewById(R.id.btn_login);
        btnLogOut = (Button) findViewById(R.id.btn_sign_out);
        mAuth = FirebaseAuth.getInstance();

        btnLogIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<AuthResult> resultTask = mAuth.signInAnonymously();
                resultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SendUserToHome();
                    }
                });
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SendUserToLoginActivity();
            }

        });


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // Toast.makeText(ActivityAnonymous.this, "SignedIn", Toast.LENGTH_SHORT).show();
                    btnLogIn2.setVisibility(View.GONE);
                    btnLogOut.setVisibility(View.VISIBLE);
                    //Toast.makeText(ActivityAnonymous.this, mAuth.getCurrentUser().getProviderId(), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(ActivityAnonymous.this, "SignedOut", Toast.LENGTH_SHORT).show();
                    btnLogOut.setVisibility(View.GONE);
                    btnLogIn2.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    private void SendUserToHome(){

        Intent homeIntent = new Intent(LoginActivity2.this, MainActivity2.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(LoginActivity2.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }



}



