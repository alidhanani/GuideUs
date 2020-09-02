package com.szabist.fyp.guideus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;

public class BusinessMyStorePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener    {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostReference;
    private DatabaseReference mPostReference1;
    private DatabaseReference mPostReference2;
    private DatabaseReference mPostReference3;
    EditText txtID, txtName, txtLongitude, txtLatitude, txtDescription;
    Spinner txtType;
    Button btnSave;
    static int totalAll;
    static int totalAllName;
    static int totalType;
    static int totalTypeName;

    private static final String TAG = "EmailPassword";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    GoogleMap map;
    static String latValue;
    static String longValue;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_my_store_page);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBusStore);
        setSupportActionBar(toolbar);

        init();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation();
        getValueFromFirebase();
        btnWork();

        BusinessSideBar side = new BusinessSideBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_business_store_view, side).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_store_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_business_store_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {
        txtID = (EditText) findViewById(R.id.businessStoreID);
        txtName = (EditText) findViewById(R.id.businessStoreName);
        txtLatitude = (EditText) findViewById(R.id.businessStoreLatitude);
        txtLongitude = (EditText) findViewById(R.id.businessStoreLongitude);
        txtDescription = (EditText) findViewById(R.id.businessStoreDescription);
        txtType = (Spinner) findViewById(R.id.spinnerBusinessStoreType);
        btnSave = (Button) findViewById(R.id.businessStoreBtnCreate);
    }

    private void getValueFromFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("business").child(user.getUid().toString()).child("Store");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Business users = dataSnapshot.getValue(Business.class);
                // [START_EXCLUDE]
                if(users.ID.equals("")){
                    Random rand = new Random();
                    txtID.setText(String.valueOf(rand.nextInt(1000) + 1));
                    txtName.setText(users.StoreName);
                    txtDescription.setText(users.StoreDecription);
                    txtLongitude.setText(users.Longitude);
                    txtLatitude.setText(users.Latitude);
                } else {
                    txtID.setText(users.ID);
                    txtName.setText(users.StoreName);
                    txtDescription.setText(users.StoreDecription);
                    txtLongitude.setText(users.Longitude);
                    txtLatitude.setText(users.Latitude);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(BusinessMyStorePageActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

    }


    private void getTotalType(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("store").child(getType());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Store users = dataSnapshot.getValue(Store.class);
                // [START_EXCLUDE]
                //totalType = Integer.parseInt(users.totalType);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(BusinessMyStorePageActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    private void btnWork() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                final FirebaseAuth Auth = FirebaseAuth.getInstance();
                mPostReference = FirebaseDatabase.getInstance().getReference()
                        .child("store").child(getType());
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        //Store users = dataSnapshot.getValue(Store.class);
                        Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                        // [START_EXCLUDE]
                        //totalTypeName = Integer.parseInt(users.get("total"));
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String clubkey = childSnapshot.getKey();
                            //Toast.makeText(getApplicationContext(), clubkey, Toast.LENGTH_SHORT).show();
                        }
                        //mDatabase.child("store").child(getType()).child("Name").child("title"+users.size()).setValue(getID());
                        //mDatabase.child("store").child(getType()).child("Name").child("total").setValue(totalTypeName+1);
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        // [START_EXCLUDE]
                        Toast.makeText(BusinessMyStorePageActivity.this, "Failed to load post.",
                                Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                mPostReference.addValueEventListener(postListener);
//                mPostReference2 = FirebaseDatabase.getInstance().getReference()
//                        .child("store").child("All").child("Name");
//                ValueEventListener postListener2 = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get Post object and use the values to update the UI
//                        //Store users = dataSnapshot.getValue(Store.class);
//                        Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
//                        // [START_EXCLUDE]
//                        //totalAllName = Integer.parseInt(users.get("total"));
//                        // [END_EXCLUDE]
//                        mDatabase.child("store").child("All").child("Name").child("title"+users.size()).setValue(getID());
//                        //mDatabase.child("store").child("All").child("Name").child("total").setValue(totalTypeName+1);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//                        // [START_EXCLUDE]
//                        Toast.makeText(BusinessMyStorePageActivity.this, "Failed to load post.",
//                                Toast.LENGTH_SHORT).show();
//                        // [END_EXCLUDE]
//                    }
//                };
//                mPostReference2.addValueEventListener(postListener2);

                mDatabase.child("business").child(user.getUid()).child("Store").child("ID").setValue(getID());
                mDatabase.child("business").child(user.getUid()).child("Store").child("StoreName").setValue(getName());
                mDatabase.child("business").child(user.getUid()).child("Store").child("Type").setValue(getType());
                mDatabase.child("business").child(user.getUid()).child("Store").child("Longitude").setValue(getLongitude());
                mDatabase.child("business").child(user.getUid()).child("Store").child("Latitude").setValue(getLatitude());
                mDatabase.child("business").child(user.getUid()).child("Store").child("StoreDecription").setValue(getDecription());
                mDatabase.child("store").child(getType()).child(getID()).child("ID").setValue(user.getUid());
                mDatabase.child("store").child(getType()).child(getID()).child("StoreID").setValue(getID());
                mDatabase.child("store").child(getType()).child(getID()).child("Name").setValue(getName());
                mDatabase.child("store").child(getType()).child(getID()).child("Longitude").setValue(Double.parseDouble(getLongitude()));
                mDatabase.child("store").child(getType()).child(getID()).child("Latitude").setValue(Double.parseDouble(getLatitude()));
                mDatabase.child("store").child(getType()).child(getID()).child("Description").setValue(getDecription());
                mDatabase.child("store").child(getType()).child(getID()).child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");
                mDatabase.child("store").child("All").child(getID()).child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");


                mDatabase.child("store").child("All").child(getID()).child("ID").setValue(user.getUid());
                mDatabase.child("store").child("All").child(getID()).child("StoreID").setValue(getID());
                mDatabase.child("store").child("All").child(getID()).child("Name").setValue(getName());
                mDatabase.child("store").child("All").child(getID()).child("Longitude").setValue(Double.parseDouble(getLongitude()));
                mDatabase.child("store").child("All").child(getID()).child("Latitude").setValue(Double.parseDouble(getLatitude()));
                mDatabase.child("store").child("All").child(getID()).child("Description").setValue(getDecription());


            }
        });
    }

    private String getID() {
        return txtID.getText().toString();
    }

    private String getName() {
        return txtName.getText().toString();
    }

    private String getLatitude() {
        return txtLatitude.getText().toString();
    }

    private String getLongitude() {
        return txtLongitude.getText().toString();
    }

    private String getDecription() {
        return txtDescription.getText().toString();
    }

    private String getType() {
        return txtType.getSelectedItem().toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_store_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.business_promo_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.busPromoAdd) {
            Intent i = new Intent(this, BusinessModifyPromoActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business_home) {
            // Handle the camera action
            Intent i = new Intent(BusinessMyStorePageActivity.this, BusinessHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_business_profile) {
            Intent i = new Intent(BusinessMyStorePageActivity.this, BusinessProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_mystore) {
            Intent i = new Intent(BusinessMyStorePageActivity.this, BusinessMyStorePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_setting) {
            Intent i = new Intent(BusinessMyStorePageActivity.this, BusinessSettingPage.class);
            startActivity(i);

        } else if (id == R.id.nav_business_help) {
            Intent i = new Intent(BusinessMyStorePageActivity.this, BusinessHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_store_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        longValue = String.valueOf(location.getLongitude());
        latValue = String.valueOf(location.getLatitude());
        txtLatitude.setText(String.valueOf(location.getLatitude()));
        txtLongitude.setText(String.valueOf(location.getLongitude() ));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
