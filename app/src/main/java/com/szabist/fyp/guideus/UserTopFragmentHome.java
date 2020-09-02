package com.szabist.fyp.guideus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by alidhanani on 04/11/2017.
 */

public class UserTopFragmentHome extends Fragment  implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    ArrayList<ModelUserRecycler> RecyclerArrayListValue;
    static ArrayList<ModelUserRecycler> RecyclerArrayList = new ArrayList<>();
    View mView;

    static String userName;
    static String userAddress;

    FirebaseAuth Auth;
    FirebaseUser user;
    //TextView txtTitle, txtDistance, txtMore;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostReference;
    private DatabaseReference mPostReference1;
    private DatabaseReference mPostReference2;

    private static final String TAG = "EmailPassword";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    GoogleMap map;
    static Double latValue = 0.0;
    static Double longValue = 0.0;
    static ArrayList<String> storeName = new ArrayList<>();

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    Button btnWork;

    public interface OnButtonPressListener {
        public void onButtonPressed(String msg, String msg2);
    }

    OnButtonPressListener buttonListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (OnButtonPressListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_user_home_top_frag, null );
        btnWork = (Button) mView.findViewById(R.id.txtUserHomeShowMore);
        FirebaseApp.initializeApp(mView.getContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        checkLocation();
        //getStoreInfo();
        //Toast.makeText(mView.getContext(), "" + RecyclerArrayList.size(), Toast.LENGTH_SHORT).show();
        //RecyclerArrayListValue = getStoreInfo();


        final ArrayList<ModelUserRecycler> RecyclerArrayListForFire = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("store").child("All");
        ValueEventListener postListener1 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                // [START_EXCLUDE]
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    final String clubkey = childSnapshot.getKey();
                    mPostReference1 = FirebaseDatabase.getInstance().getReference()
                            .child("store").child("All").child(clubkey);
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            // Get Post object and use the values to update the UI
                            final RecyclerView rv = (RecyclerView) mView.findViewById(R.id.busrecyclerview);
                            // if (dataSnapshot.exists()) {
                            //    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Map<String, String> users = (Map<String, String>) dataSnapshot1.getValue();
                            // [START_EXCLUDE]
                            //txtTitle.setText(users.get("Name"));
                            //txtMore.setText(users.get("Description"));
                            Location loc1 = new Location("");
                            loc1.setLatitude(latValue);
                            loc1.setLongitude(longValue);

                            final ModelUserRecycler modelRecycler = new ModelUserRecycler();
                            modelRecycler.setResturant(users.get("Name"));
                            modelRecycler.setHere(users.get("Description"));
                            modelRecycler.setContact("");
                            modelRecycler.setEmail("");
                            modelRecycler.setType(clubkey);
                            modelRecycler.setID(users.get("StoreID"));
                            modelRecycler.setUrl("");
                            modelRecycler.setLongitude(String.valueOf(users.get("Longitude")));
                            modelRecycler.setLatitude(String.valueOf(users.get("Latitude")));
                            modelRecycler.setoLatitude(latValue.toString());
                            modelRecycler.setoLongitude(longValue.toString());


                            Location loc2 = new Location("");
                            loc2.setLatitude(Double.parseDouble(String.valueOf(users.get("Latitude"))));
                            loc2.setLongitude(Double.parseDouble(String.valueOf(users.get("Longitude"))));
                            float distanceInMeters = loc1.distanceTo(loc2);

//                    LatLng here = new LatLng(latValue, longValue);
//                    LatLng there = new LatLng(Double.parseDouble(users.Latitude), Double.parseDouble(users.Longitude));
//                    Double result = SphericalUtil.computeDistanceBetween(here, there);
                            //result = result / 1000000;
                            distanceInMeters = distanceInMeters / 1000;
                            //String show = String.format("%.3f", result);
                            String show1 = String.format("%.2f", distanceInMeters);
                            modelRecycler.setDistance(show1);
                            //events.add(modelRecycler);
                            addingValue(modelRecycler);
                            RecyclerArrayListForFire.add(modelRecycler);
                            //userName = users.get("Name");
                            //userAddress = users.get("Address");
                            //Toast.makeText(getContext(), String.valueOf(RecyclerArrayListForFire.size()),Toast.LENGTH_LONG).show();
                            //    }
                            //}

                            mPostReference2 = FirebaseDatabase.getInstance().getReference()
                                    .child("store").child("All").child(clubkey).child("Promo");
                            ValueEventListener postListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                    Map<String, String> users2 = (Map<String, String>) dataSnapshot2.getValue();
                                    modelRecycler.setPromoTitle(users2.get("PromoName"));
                                    modelRecycler.setPromoDesc(users2.get("Description"));

                                    LinearLayoutManager mManager = new LinearLayoutManager(getContext());
                                    rv.setLayoutManager(mManager);

                                    RecyclerAdapterUser recyclerAdapter = new RecyclerAdapterUser(getContext(),RecyclerArrayListForFire);
                                    rv.setAdapter(recyclerAdapter);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message
                                    // [START_EXCLUDE]
                                    Toast.makeText(mView.getContext(), "Failed to load post.",
                                            Toast.LENGTH_SHORT).show();
                                    // [END_EXCLUDE]
                                }
                            };
                            mPostReference2.addValueEventListener(postListener);

                            //myVariable.FirebaseArrayList.add(myVariable.modelRecycler);
                            //txtDistance.setText(show1);
                            // [END_EXCLUDE]
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // [START_EXCLUDE]
                            Toast.makeText(mView.getContext(), "Failed to load post.",
                                    Toast.LENGTH_SHORT).show();
                            // [END_EXCLUDE]
                        }
                    };
                    mPostReference1.addValueEventListener(postListener);
                }


                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(mView.getContext(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

//        btnWork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buttonListener.onButtonPressed(userName, userAddress);
//            }
//        });

        mPostReference.addValueEventListener(postListener1);

        return mView;

    }

    public ArrayList<ModelBusinessRecycler> getValue() {
        ArrayList<ModelBusinessRecycler> RecyclerArrayList = new ArrayList<>();

        ModelBusinessRecycler modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant1");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant1");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant2");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant3");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant4");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        modelRecycler = new ModelBusinessRecycler();
        modelRecycler.setResturant("resturant5");
        modelRecycler.setDistance("10.12");
        modelRecycler.setHere("here");
        RecyclerArrayList.add(modelRecycler);

        return  RecyclerArrayList;

    }

    public void addingValue(ModelUserRecycler value) {
        //Toast.makeText(getContext(), String.valueOf(value.getResturant()),Toast.LENGTH_LONG).show();
        RecyclerArrayList.add(value);
    }

    private ArrayList<ModelUserRecycler> getStoreInfo() {

        final ArrayList<ModelUserRecycler> RecyclerArrayListForFire = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("Store").child("Name");
        ValueEventListener postListener1 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                // [START_EXCLUDE]
                //Toast.makeText(getContext(), users.get("store0"),Toast.LENGTH_LONG).show();
                int total = Integer.parseInt(String.valueOf(users.get("total")));
                //Toast.makeText(getContext(), total + "",Toast.LENGTH_LONG).show();
                for(int i = 0; i < total; i++) {
                    //Toast.makeText(getContext(), users.get("title"+i),Toast.LENGTH_LONG).show();
                    //storeName.add(users.get("title"+i).toString());
                    mPostReference = FirebaseDatabase.getInstance().getReference()
                            .child("Store").child(users.get("store"+i).toString());
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get Post object and use the values to update the UI
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                                    // [START_EXCLUDE]
                                    //txtTitle.setText(users.get("Name"));
                                    //txtMore.setText(users.get("Description"));
                                    Location loc1 = new Location("");
                                    loc1.setLatitude(latValue);
                                    loc1.setLongitude(longValue);

                                    ModelUserRecycler modelRecycler = new ModelUserRecycler();
                                    modelRecycler.setResturant(users.get("Name"));
                                    modelRecycler.setHere(users.get("Address"));
                                    modelRecycler.setLatitude(String.valueOf(users.get("Latitude")));
                                    modelRecycler.setLongitude(String.valueOf(users.get("Longitude")));
                                    Location loc2 = new Location("");
                                    loc2.setLatitude(Double.parseDouble(String.valueOf(users.get("Latitude"))));
                                    loc2.setLongitude(Double.parseDouble(String.valueOf(users.get("Longitude"))));
                                    float distanceInMeters = loc1.distanceTo(loc2);
//                    LatLng here = new LatLng(latValue, longValue);
//                    LatLng there = new LatLng(Double.parseDouble(users.Latitude), Double.parseDouble(users.Longitude));
//                    Double result = SphericalUtil.computeDistanceBetween(here, there);
                                    //result = result / 1000000;
                                    distanceInMeters = distanceInMeters / 1000;
                                    //String show = String.format("%.3f", result);
                                    String show1 = String.format("%.2f", distanceInMeters);
                                    modelRecycler.setDistance(show1);
                                    //events.add(modelRecycler);
                                    addingValue(modelRecycler);
                                    RecyclerArrayListForFire.add(modelRecycler);
                                    Toast.makeText(getContext(), String.valueOf(RecyclerArrayListForFire.size()),Toast.LENGTH_LONG).show();
                                }
                            }

                            //myVariable.FirebaseArrayList.add(myVariable.modelRecycler);
                            //txtDistance.setText(show1);
                            // [END_EXCLUDE]
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // [START_EXCLUDE]
                            Toast.makeText(mView.getContext(), "Failed to load post.",
                                    Toast.LENGTH_SHORT).show();
                            // [END_EXCLUDE]
                        }
                    };
                    mPostReference.addValueEventListener(postListener);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(mView.getContext(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mPostReference.addValueEventListener(postListener1);
        //Toast.makeText(getContext(), String.valueOf(RecyclerArrayListForFire.size()),Toast.LENGTH_LONG).show();
        return RecyclerArrayListForFire;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            //longValue =  mLocation.getLongitude();
            //latValue = mLocation.getLatitude();
            latValue = mLocation.getLatitude();
            longValue = mLocation.getLongitude();
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(mView.getContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
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
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
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
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        //longValue = String.valueOf(location.getLongitude());
        longValue =  location.getLongitude();
        latValue = location.getLatitude();
        //latValue = String.valueOf(location.getLatitude());
        //txtLatitude.setText(String.valueOf(location.getLatitude()));
        //txtLongitude.setText(String.valueOf(location.getLongitude() ));
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.userFilterAll) {
            Toast.makeText(mView.getContext(), "All is selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterCloths) {
            Toast.makeText(mView.getContext(), "All is Clothes", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterHardware) {
            Toast.makeText(mView.getContext(), "All is Hardware", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterResturant) {
            Toast.makeText(mView.getContext(), "All is Resturant", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterShoes) {
            Toast.makeText(mView.getContext(), "All is Shoes", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}

