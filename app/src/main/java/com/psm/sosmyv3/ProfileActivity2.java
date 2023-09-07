package com.psm.sosmyv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity2 extends AppCompatActivity {

    private TextView name, icNo , telNo;
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
        setContentView(R.layout.activity_profile2);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Victim").child(currentUserId);



        name = (TextView) findViewById(R.id.my_profile_full_name);
        icNo = (TextView) findViewById(R.id.my_icNo);
        telNo = (TextView) findViewById(R.id.my_telNo);
        updateBtn = (Button) findViewById(R.id.pupdate_button);


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String myName = snapshot.child("name").getValue().toString();
                    String myIcNo = snapshot.child("icNo").getValue().toString();
                    String myTelNo = snapshot.child("telNo").getValue().toString();


                    name.setText("Name : " + myName );
                    icNo.setText("IC Number : " + myIcNo );
                    telNo.setText("Tel Number : " + myTelNo );


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

        drawerLayout = findViewById(R.id.drawable_layout_profile2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ProfileActivity2.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
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

        Intent loginIntent = new Intent(ProfileActivity2.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToHome(){

        Intent homeIntent = new Intent(ProfileActivity2.this, VictimMapActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    private void SendUserToEmergency(){

        Intent emergencyIntent = new Intent(ProfileActivity2.this, MapsActivity.class);
        emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emergencyIntent);
    }

    private void SendUserToAddContact(){

        Intent profileIntent = new Intent(ProfileActivity2.this, AddContactActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
    }
    private void SendUserToContact(){

        Intent profileIntent = new Intent(ProfileActivity2.this, ContactsActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
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

            case R.id.add_contact:
                SendUserToAddContact();
                Toast.makeText(this, "Add Trusted Contact", Toast.LENGTH_SHORT).show();
                break;

            case R.id.my_contacts:
                SendUserToContact();
                Toast.makeText(this, "My Trusted Contact", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }



    private void SendUserToProfileSetting(){

        Intent settingIntent = new Intent(ProfileActivity2.this, UpdateProfile2.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
    }



}