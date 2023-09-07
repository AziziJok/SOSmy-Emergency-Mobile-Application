package com.psm.sosmyv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VictimMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {



    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseAuth mAuth;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private Button aRequest , pRequest, fRequest, telRequest;

    private LatLng pickupLocation;

    private Boolean requestBol = false;
    private Marker pickupMarker;

    private LinearLayout mAmbulanceInfo;

    private TextView mAmbulanceName, mAmbulanceTelNo, mAmbulancePlateNo;

    private LinearLayout mPoliceInfo;

    private TextView mPoliceName, mPoliceTelNo, mPolicePlateNo;

    private LinearLayout mFiremanInfo;

    private TextView mFiremanName, mFiremanTelNo, mFiremanPlateNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mAuth = FirebaseAuth.getInstance();


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Victim Map");

        drawerLayout = findViewById(R.id.drawable_layout_victimMap);
        actionBarDrawerToggle = new ActionBarDrawerToggle(VictimMapActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
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

        mAmbulanceInfo = (LinearLayout) findViewById(R.id.ambulanceInfo);
        mAmbulanceName = (TextView) findViewById(R.id.ambulanceName);
        mAmbulanceTelNo = (TextView) findViewById(R.id.ambulanceTelNo);
        mAmbulancePlateNo = (TextView) findViewById(R.id.ambulancePlateNo);

        mPoliceInfo = (LinearLayout) findViewById(R.id.policeInfo);
        mPoliceName = (TextView) findViewById(R.id.policeName);
        mPoliceTelNo = (TextView) findViewById(R.id.policeTelNo);
        mPolicePlateNo = (TextView) findViewById(R.id.policePlateNo);

        mFiremanInfo = (LinearLayout) findViewById(R.id.firemanInfo);
        mFiremanName = (TextView) findViewById(R.id.firemanName);
        mFiremanTelNo = (TextView) findViewById(R.id.firemanTelNo);
        mFiremanPlateNo = (TextView) findViewById(R.id.firemanPlateNo);

        aRequest = (Button) findViewById(R.id.hospital_nearby);
        pRequest = (Button) findViewById(R.id.police_nearby);
        fRequest = (Button) findViewById(R.id.fireman_nearby);
        telRequest = (Button) findViewById(R.id.trustTEL);


        telRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToAddedContact();
            }
        });

        aRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (requestBol){
                     requestBol = false;
                     geoQuery.removeAllListeners();
                     ambulanceLocationRef.removeEventListener(ambulanceLocationRefListener);

                     if (ambulanceFoundID != null){
                         DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(ambulanceFoundID).child("victimRideId");
                         dispatchRef.removeValue();
                         ambulanceFoundID = null;


                     }
                     ambulanceFound = false;
                     radius = 1;
                     String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");
                     GeoFire geoFire = new GeoFire(ref);
                     geoFire.removeLocation(userId,new
                             GeoFire.CompletionListener(){
                                 @Override
                                 public void onComplete(String key, DatabaseError error) {
                                     Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();                    }
                             });

                     if (pickupMarker != null){
                         pickupMarker.remove();
                     }

                     aRequest.setText("call Ambulance");

                     mAmbulanceInfo.setVisibility(View.GONE);
                     mAmbulanceName.setText("");
                     mAmbulanceTelNo.setText("");
                     mAmbulancePlateNo.setText("");


                 }
                 else {
                     requestBol = true;
                     String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");

                     GeoFire geoFire = new GeoFire(ref);
                     geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new
                             GeoFire.CompletionListener() {
                                 @Override
                                 public void onComplete(String key, DatabaseError error) {
                                     //Do some stuff if you want to
                                 }
                             });


                     pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                     pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                     aRequest.setText("Getting your Ambulance...");

                     getClosestDispatch();

                 }
            }
         });

        pRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestBol){
                    requestBol = false;
                    geoQuery.removeAllListeners();
                    policeLocationRef.removeEventListener(policeLocationRefListener);

                    if (policeFoundID != null){
                        DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(policeFoundID).child("victimRideId");
                        dispatchRef.removeValue();
                        policeFoundID = null;


                    }
                    policeFound = false;
                    radius = 1;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.removeLocation(userId,new
                            GeoFire.CompletionListener(){
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();                    }
                            });

                    if (pickupMarker != null){
                        pickupMarker.remove();
                    }

                    pRequest.setText("call Police");

                    mPoliceInfo.setVisibility(View.GONE);
                    mPoliceName.setText("");
                    mPoliceTelNo.setText("");
                    mPolicePlateNo.setText("");


                }
                else {
                    requestBol = true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new
                            GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    //Do some stuff if you want to
                                }
                            });


                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    pRequest.setText("Getting your Police...");

                    getClosestPolice();

                }
            }
        });

        fRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestBol){
                    requestBol = false;
                    geoQuery.removeAllListeners();
                    firemanLocationRef.removeEventListener(firemanLocationRefListener);

                    if (firemanFoundID != null){
                        DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(firemanFoundID).child("victimRideId");
                        dispatchRef.removeValue();
                        firemanFoundID = null;


                    }
                    firemanFound = false;
                    radius = 1;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.removeLocation(userId,new
                            GeoFire.CompletionListener(){
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();                    }
                            });

                    if (pickupMarker != null){
                        pickupMarker.remove();
                    }

                    fRequest.setText("call Fireman");

                    mFiremanInfo.setVisibility(View.GONE);
                    mFiremanName.setText("");
                    mFiremanTelNo.setText("");
                    mFiremanPlateNo.setText("");


                }
                else {
                    requestBol = true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("victimRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new
                            GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    //Do some stuff if you want to
                                }
                            });


                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    fRequest.setText("Getting your Fireman...");

                    getClosestFireman();

                }
            }
        });

    }

    private int radius = 1;
    private Boolean ambulanceFound = false;
    private String ambulanceFoundID;

    private Boolean policeFound = false;
    private String policeFoundID;

    private Boolean firemanFound = false;
    private String firemanFoundID;

    GeoQuery geoQuery;

    private void getClosestDispatch() {

        DatabaseReference dispatchLocation = FirebaseDatabase.getInstance().getReference().child("ambulanceAvailable");

        GeoFire geoFire = new GeoFire(dispatchLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!ambulanceFound && requestBol){
                    ambulanceFound = true;
                    ambulanceFoundID = key;

                    DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(ambulanceFoundID);
                    String icNo = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("victimRideId", icNo);
                    dispatchRef.updateChildren(map);

                    getDispatchLocation();
                    getDispatchInfo();
                    aRequest.setText("Looking for Ambulance Location...");

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!ambulanceFound)
                {
                    radius++;
                    getClosestDispatch();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getClosestPolice() {

        DatabaseReference dispatchLocation = FirebaseDatabase.getInstance().getReference().child("policeAvailable");

        GeoFire geoFire = new GeoFire(dispatchLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!policeFound && requestBol){
                    policeFound = true;
                    policeFoundID = key;

                    DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(policeFoundID);
                    String icNo = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("victimRideId", icNo);
                    dispatchRef.updateChildren(map);

                    getPoliceLocation();
                    getPoliceInfo();
                    pRequest.setText("Looking for Police Location...");

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!policeFound)
                {
                    radius++;
                    getClosestPolice();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getClosestFireman() {

        DatabaseReference dispatchLocation = FirebaseDatabase.getInstance().getReference().child("firemanAvailable");

        GeoFire geoFire = new GeoFire(dispatchLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!firemanFound && requestBol){
                    firemanFound = true;
                    firemanFoundID = key;

                    DatabaseReference dispatchRef = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(firemanFoundID);
                    String icNo = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("victimRideId", icNo);
                    dispatchRef.updateChildren(map);

                    getFiremanLocation();
                    getFiremanInfo();
                    fRequest.setText("Looking for Fireman Location...");

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!firemanFound)
                {
                    radius++;
                    getClosestFireman();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Marker mAmbulanceMarker;
    private DatabaseReference ambulanceLocationRef;
    private ValueEventListener ambulanceLocationRefListener;

    private Marker mPoliceMarker;
    private DatabaseReference policeLocationRef;
    private ValueEventListener policeLocationRefListener;

    private Marker mFiremanMarker;
    private DatabaseReference firemanLocationRef;
    private ValueEventListener firemanLocationRefListener;

    private void getDispatchLocation()
    {
        ambulanceLocationRef = FirebaseDatabase.getInstance().getReference().child("ambulanceWorking").child(ambulanceFoundID).child("l");
        ambulanceLocationRefListener = ambulanceLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    aRequest.setText("Ambulance Found");
                    if(map.get(0) != null){

                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){

                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng dispatchLatLng = new LatLng(locationLat,locationLng);
                    if (mAmbulanceMarker != null){
                        mAmbulanceMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(dispatchLatLng.latitude);
                    loc2.setLongitude(dispatchLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance<100){

                        aRequest.setText("Ambulance's Here");
                    }else
                    {
                        aRequest.setText("Ambulance found" + String.valueOf(distance));
                    }

                    mAmbulanceMarker = mMap.addMarker(new MarkerOptions().position(dispatchLatLng).title("your Ambulance"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getPoliceLocation()
    {
        policeLocationRef = FirebaseDatabase.getInstance().getReference().child("policeWorking").child(policeFoundID).child("l");
        policeLocationRefListener = policeLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    pRequest.setText("Police Found");
                    if(map.get(0) != null){

                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){

                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng dispatchLatLng = new LatLng(locationLat,locationLng);
                    if (mPoliceMarker != null){
                        mPoliceMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(dispatchLatLng.latitude);
                    loc2.setLongitude(dispatchLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance<100){

                        pRequest.setText("Police's Here");
                    }else
                    {
                        pRequest.setText("Police found" + String.valueOf(distance));
                    }

                    mPoliceMarker = mMap.addMarker(new MarkerOptions().position(dispatchLatLng).title("your Police"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getFiremanLocation()
    {
        firemanLocationRef = FirebaseDatabase.getInstance().getReference().child("firemanWorking").child(firemanFoundID).child("l");
        firemanLocationRefListener = firemanLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    fRequest.setText("Fireman Found");
                    if(map.get(0) != null){

                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){

                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng dispatchLatLng = new LatLng(locationLat,locationLng);
                    if (mFiremanMarker != null){
                        mFiremanMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(dispatchLatLng.latitude);
                    loc2.setLongitude(dispatchLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance<100){

                        pRequest.setText("Fireman's Here");
                    }else
                    {
                        pRequest.setText("Fireman found" + String.valueOf(distance));
                    }

                    mFiremanMarker = mMap.addMarker(new MarkerOptions().position(dispatchLatLng).title("your Fireman"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getDispatchInfo(){
        mAmbulanceInfo.setVisibility(View.VISIBLE);
        DatabaseReference mVictimDatabase = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(ambulanceFoundID);
        mVictimDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() >0 ){
                    Map<String, Object> map = (Map<String,Object >) snapshot.getValue();
                    if (map.get("name") != null){
                        mAmbulanceName.setText(map.get("name").toString());
                    }
                    if (map.get("telNo") != null){
                        mAmbulanceTelNo.setText(map.get("telNo").toString());

                    }
                    if (map.get("plateNo") != null){
                        mAmbulancePlateNo.setText(map.get("plateNo").toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void getPoliceInfo(){
        mPoliceInfo.setVisibility(View.VISIBLE);
        DatabaseReference mVictimDatabase = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(policeFoundID);
        mVictimDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() >0 ){
                    Map<String, Object> map = (Map<String,Object >) snapshot.getValue();
                    if (map.get("name") != null){
                        mPoliceName.setText(map.get("name").toString());
                    }
                    if (map.get("telNo") != null){
                        mPoliceTelNo.setText(map.get("telNo").toString());

                    }
                    if (map.get("plateNo") != null){
                        mPolicePlateNo.setText(map.get("plateNo").toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void getFiremanInfo(){
        mFiremanInfo.setVisibility(View.VISIBLE);
        DatabaseReference mVictimDatabase = FirebaseDatabase.getInstance().getReference().child("Dispatch").child(firemanFoundID);
        mVictimDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() >0 ){
                    Map<String, Object> map = (Map<String,Object >) snapshot.getValue();
                    if (map.get("name") != null){
                        mFiremanName.setText(map.get("name").toString());
                    }
                    if (map.get("telNo") != null){
                        mFiremanTelNo.setText(map.get("telNo").toString());

                    }
                    if (map.get("plateNo") != null){
                        mFiremanPlateNo.setText(map.get("plateNo").toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


    }

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(location.getLatitude(),location.getLongitude()));
        circleOptions.radius(2000);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(6);
        mMap.addCircle(circleOptions);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions.title(String.valueOf(location)));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    private void SendUserToAddedContact() {

        Intent loginIntent = new Intent(VictimMapActivity.this, ContactsActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(VictimMapActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToHome(){

        Intent homeIntent = new Intent(VictimMapActivity.this, VictimMapActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    private void SendUserToEmergency(){

        Intent emergencyIntent = new Intent(VictimMapActivity.this, MapsActivity.class);
        emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emergencyIntent);
    }

    private void SendUserToProfile(){

        Intent profileIntent = new Intent(VictimMapActivity.this, ProfileActivity2.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
    }

    private void SendUserToAddContact(){

        Intent profileIntent = new Intent(VictimMapActivity.this, AddContactActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
    }
    private void SendUserToContact(){

        Intent profileIntent = new Intent(VictimMapActivity.this, ContactsActivity.class);
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
                Toast.makeText(this, "Search Nearby Facility", Toast.LENGTH_SHORT).show();
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
