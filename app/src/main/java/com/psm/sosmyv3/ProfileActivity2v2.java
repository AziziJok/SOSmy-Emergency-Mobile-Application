package com.psm.sosmyv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity2v2 extends AppCompatActivity {

    private TextView name, icNo , telNo, trustedTelNo;
    private Button updateBtn;

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;

    private String currentUserId;


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2v2);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);



        name = (TextView) findViewById(R.id.my_profile_full_name);
        icNo = (TextView) findViewById(R.id.my_icNo);
        telNo = (TextView) findViewById(R.id.my_telNo);
        trustedTelNo = (TextView) findViewById(R.id.my_trustedTelNo);
        updateBtn = (Button) findViewById(R.id.pupdate_button);


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String myName = snapshot.child("name").getValue().toString();
                    String myIcNo = snapshot.child("icNo").getValue().toString();
                    String myTelNo = snapshot.child("telNo").getValue().toString();
                    String myTrustedTelNo = snapshot.child("trustedTelNo").getValue().toString();


                    name.setText("Name : " + myName );
                    icNo.setText("IC Number : " + myIcNo );
                    telNo.setText("Tel Number : " + myTelNo );
                    trustedTelNo.setText("Trusted Contact Number : " + myTrustedTelNo );


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToProfileSetting();
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");

        drawerLayout = findViewById(R.id.drawable_layout_profile2v2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ProfileActivity2v2.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });



    }


    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(ProfileActivity2v2.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToHome(){

        Intent homeIntent = new Intent(ProfileActivity2v2.this, MainActivity2.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    private void SendUserToEmergency(){

        Intent emergencyIntent = new Intent(ProfileActivity2v2.this, EmergencyLayout2.class);
        emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emergencyIntent);
    }

    private void SendUserToSetting(){

        Intent settingIntent = new Intent(ProfileActivity2v2.this, SettingActivity2.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                SendUserToHome();
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_emergency:
                SendUserToEmergency();
                Toast.makeText(this, "Emergency", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                SendUserToSetting();
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }



    private void SendUserToProfileSetting(){

        Intent settingIntent = new Intent(ProfileActivity2v2.this, UpdateProfile.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
    }



}