package com.szabist.fyp.guideus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusinessActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener   {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText txtName, txtEmail, txtContact, txtPassConfirm, txtPass;
    Spinner txtArea;
    Button btnBusinessEnter;
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
        setContentView(R.layout.activity_business);
        mAuth = FirebaseAuth.getInstance();
        init();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        //checkLocation();
        btnWork();
    }

    private void btnWork() {
        btnBusinessEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = getName();
                String email = getEmail();
                String contact = getContact();
                String passConfirm = getConfirmPass();
                String area = getArea();
                String pass = getPass();

                if(name.equals("")) {
                    txtName.setError("Enter Name");
                } else if(email.equals("")) {
                    txtEmail.setError("Enter Email");
                } else if(contact.equals("")) {
                    txtContact.setError("Enter Contact");
                } else if(passConfirm.equals("")) {
                    txtPassConfirm.setError("Enter Pass Confirm");
                } else if(pass.equals("")) {
                    txtPass.setError("Enter Password");
                } else {

                    if(pass.equals(passConfirm)) {
                         FirebaseSignUp(getEmail(), getPass());
                    } else {
                        Toast.makeText(getApplicationContext(), "The Password Doesn't Match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void FirebaseSignUp(String email, String pass) {
        final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(getName())
                .setPhotoUri(Uri.parse("DefaultImage/UserDefaulImage.png"))
                .build();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("ID").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("PromoName").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Description").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Contact").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Email").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Area").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("ID").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("StoreName").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Type").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Longitude").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("Latitude").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Store").child("StoreDecription").setValue("");
                            mDatabase.child("business").child(user.getUid()).child("Email").setValue(getEmail());
                            mDatabase.child("business").child(user.getUid()).child("ID").setValue(user.getUid());
                            mDatabase.child("business").child(user.getUid()).child("Name").setValue(getName());
                            mDatabase.child("business").child(user.getUid()).child("Contact").setValue(getContact());
                            mDatabase.child("business").child(user.getUid()).child("Area").setValue(getArea());
                            mDatabase.child("business").child(user.getUid()).child("Type").setValue("Business");
                            mDatabase.child("business").child(user.getUid()).child("Password").setValue(getPass());
                            mDatabase.child("business").child(user.getUid()).child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");
                            Intent i = new Intent(BusinessActivity.this, BusinessHomePageActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void init() {
        txtName = (EditText) findViewById(R.id.txtBusinessName);
        txtPass = (EditText) findViewById(R.id.txtBusinessPass);
        txtPassConfirm = (EditText) findViewById(R.id.txtBusinessPassConfirm);
        txtEmail = (EditText) findViewById(R.id.txtLBusinessEmail);
        txtContact = (EditText) findViewById(R.id.txtBusinessContact);
        txtArea = (Spinner) findViewById(R.id.spinnerBusinessArea);
        btnBusinessEnter = (Button) findViewById(R.id.btnBusinessEnter);
    }

    private String getPass() {
        return txtPass.getText().toString();
    }

    private String getName() {
        return txtName.getText().toString();
    }

    private String getConfirmPass() {
        return txtPassConfirm.getText().toString();
    }

    private String getEmail() {
        return txtEmail.getText().toString();
    }

    private String getContact() {
        return txtContact.getText().toString();
    }

    private String getArea() {
        return txtArea.getSelectedItem().toString();
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
        //txtLat.setText(String.valueOf(location.getLatitude()));
        //txtLong.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
