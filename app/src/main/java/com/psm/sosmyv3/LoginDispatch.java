package com.psm.sosmyv3;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginDispatch extends AppCompatActivity {

    private Button ambulanceButton, policeButton, firemanButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dispatch);

        ambulanceButton = (Button) findViewById(R.id.ambulance_login);
        policeButton = (Button) findViewById(R.id.police_login);
        firemanButton = (Button) findViewById(R.id.fireman_login);


        ambulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToAmbulanceLoginActivity();
            }
        });

        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToPoliceLoginActivity();
            }
        });

        firemanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToFiremanLoginActivity();
            }
        });

    }

    private void SendUserToAmbulanceLoginActivity() {

        Intent loginIntent = new Intent(LoginDispatch.this, AmbulanceLogin.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void SendUserToPoliceLoginActivity() {

        Intent loginIntent = new Intent(LoginDispatch.this, PoliceLogin.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void SendUserToFiremanLoginActivity() {

        Intent loginIntent = new Intent(LoginDispatch.this, FiremanLogin.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }


}
