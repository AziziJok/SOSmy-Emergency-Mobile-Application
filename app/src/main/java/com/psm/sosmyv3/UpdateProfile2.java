package com.psm.sosmyv3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateProfile2 extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText name, icNo , telNo;
    private Button UpdateAccBtn;

    private DatabaseReference SettinguserRef;
    private FirebaseAuth mAuth;

    private  String currentUserId;



    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile2);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        SettinguserRef = FirebaseDatabase.getInstance().getReference().child("Victim").child(currentUserId);

        mToolbar = (Toolbar) findViewById(R.id.update_profile);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = (EditText) findViewById(R.id.update_name);
        icNo = (EditText) findViewById(R.id.update_icNo);
        telNo = (EditText) findViewById(R.id.update_telNo);
        UpdateAccBtn = (Button) findViewById(R.id.update_profile_button);

        SettinguserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String myName = snapshot.child("name").getValue().toString();
                    String myIcNo = snapshot.child("icNo").getValue().toString();
                    String myTelNo = snapshot.child("telNo").getValue().toString();

                    name.setText(myName );
                    icNo.setText(myIcNo );
                    telNo.setText(myTelNo );


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UpdateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateAccountInfo();

            }
        });


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Profile");

        drawerLayout = findViewById(R.id.drawable_layout_update_profile2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(UpdateProfile2.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
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

    private void ValidateAccountInfo() {

        String vName = name.getText().toString();
        String vIcNo = icNo.getText().toString();
        String vTelNo = telNo.getText().toString();



        if (TextUtils.isEmpty(vName)){

            Toast.makeText(this,"Please write your name..",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(vIcNo)){

            Toast.makeText(this,"Please write your IC Number..",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(vTelNo)){

            Toast.makeText(this,"Please write your Tel Number..",Toast.LENGTH_SHORT).show();
        }
        else{

            UpdateAccountInfo(vName, vIcNo, vTelNo);
        }


    }

    private void UpdateAccountInfo(String vName, String vIcNo, String vTelNo) {

        HashMap userMap = new HashMap();

        userMap.put("name", vName);
        userMap.put("icNo", vIcNo);
        userMap.put("telNo", vTelNo);

        SettinguserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){

                    SendUserToProfile();

                    Toast.makeText(UpdateProfile2.this, "Profile Updated Successfully..",Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(UpdateProfile2.this, "Error occurred while updating..",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    private void SendUserToProfile(){

        Intent settingIntent = new Intent(UpdateProfile2.this, ProfileActivity2.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(UpdateProfile2.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToHome(){

        Intent homeIntent = new Intent(UpdateProfile2.this, VictimMapActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    private void SendUserToEmergency(){

        Intent emergencyIntent = new Intent(UpdateProfile2.this, MapsActivity.class);
        emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emergencyIntent);
    }
    private void SendUserToAddContact(){

        Intent profileIntent = new Intent(UpdateProfile2.this, AddContactActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
    }
    private void SendUserToContact(){

        Intent profileIntent = new Intent(UpdateProfile2.this, ContactsActivity.class);
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
                SendUserToProfile();
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


}