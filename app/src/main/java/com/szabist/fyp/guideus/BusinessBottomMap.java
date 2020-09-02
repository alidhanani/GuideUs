package com.szabist.fyp.guideus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by NerdMacAdmin on 03/01/2018.
 */

public class BusinessBottomMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    View mView;
    private GoogleMap mMap;
    static LatLng destination;
    //GoogleMap map;
    static BusinessValues val = new BusinessValues();
    static String latValue;
    static String longValue;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        mView = inflater.inflate(R.layout.activity_business_map, null);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.Busmap);
        mapFragment.getMapAsync(this);
        Toast.makeText(getContext(), latValue + " " + longValue, Toast.LENGTH_SHORT).show();
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap = googleMap;
        updateMap();
    }

    private void updateMap() {

        Double lat = 0.0, lon = 0.0;
        Double olat = 0.0, olon = 0.0;
//        if(latValue == "" && longValue == "") {
        lat = Double.valueOf(-33.852);
        lon = Double.valueOf(151.211);
//        } else {
//            lat = Double.parseDouble(latValue);
//            lon = Double.parseDouble(longValue);
//        }
        if(val.getLat().equals("") && val.getLog().equals("")) {
            lat = Double.parseDouble("0.0");
            lon = Double.parseDouble("0.0");
        } else {
            lat = Double.parseDouble(val.getLat());
            lon = Double.parseDouble(val.getLog());
        }
        if(val.getOlat().equals("") && val.getOlog().equals("")) {
            olat = Double.parseDouble("0.0");
            olon = Double.parseDouble("0.0");
        } else {
            olat = Double.parseDouble(val.getOlat());
            olon = Double.parseDouble(val.getOlog());
        }
        final LatLng origin = new LatLng(lat, lon);
        destination = new LatLng(olat, olon);
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);

        LatLngBounds bounds = builder.build();

        int padding = 100;

        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        final MarkerOptions markerOptions = new MarkerOptions().position(destination).title(val.getRes());
        final MarkerOptions markerOptionsMe = new MarkerOptions().position(origin).title("Moi");
        //mMap.moveCamera(cu);
        //mMap.animateCamera(cu);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera.
                mMap.moveCamera(cu);
                mMap.animateCamera(cu);
                // Remove listener to prevent position reset on camera move.
                mMap.setOnCameraChangeListener(null);
            }
        });
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Marker mMarker = mMap.addMarker(markerOptions);
        mMap.addMarker(markerOptionsMe);
        mMarker.showInfoWindow();


        GoogleDirection.withServerKey("AIzaSyDWaA1MUq19Gp4faCpM2EbL28e8MQXttoE")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {

                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            mMap.addCircle(new CircleOptions()
                                    .center(origin)
                                    .radius(15)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.BLUE)
                            );
//                            final Marker a = mMap.addMarker(markerOptions);
//                            a.showInfoWindow();
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);
                            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);
                            } else {
                                // Show rationale and request permission.
                            }

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("MAP", t.getMessage());


//                        final Marker a = mMap.addMarker(markerOptions);
//                        a.showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        } else {
                            // Show rationale and request permission.
                        }

                    }
                });
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

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(getContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
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
        longValue = String.valueOf(location.getLongitude());
        latValue = String.valueOf(location.getLatitude());
        //Toast.makeText(getContext(), latValue + " " + longValue, Toast.LENGTH_SHORT).show();
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

}
