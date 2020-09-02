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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.maps.android.SphericalUtil;

import java.util.zip.Inflater;

public class UserHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserTopFragmentHome.OnButtonPressListener {

    FirebaseAuth Auth;
    FirebaseUser user;
    TextView navUserName, navUserEmail;

    private DatabaseReference mPostReference;

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
        setContentView(R.layout.activity_user_home_page);
        FirebaseApp.initializeApp(getApplicationContext());
        Auth = FirebaseAuth.getInstance();
        user = Auth.getInstance().getCurrentUser();
        //init();
        //setUpText();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUserHome);
        setSupportActionBar(toolbar);

        SideBarFrag side = new SideBarFrag();
        UserTopFragmentHome topFrag = new UserTopFragmentHome();
        UserBottomFragmentHome bottomFrag = new UserBottomFragmentHome();
        //UserBottomMapFrag bottomMap = new UserBottomMapFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_user_home_view, side).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_top, topFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_bottom, bottomFrag).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.map, bottomFrag).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_bottom, bottomMap).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_home_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_user_home_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpText() {
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getInstance().getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user").child(user.getProviderId());
        System.out.print("User Id" + user.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User users = dataSnapshot.getValue(User.class);
                navUserName.setText(user.getDisplayName().toString());
                navUserEmail.setText(user.getEmail().toString());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    @Override
    public void onButtonPressed(String msg, String msg2) {
        UserBottomFragmentHome Obj=(UserBottomFragmentHome) getSupportFragmentManager().findFragmentById(R.id.user_frgrament_bottom);
        Obj.setMessage(msg, msg2);
    }



    public void AddFragment(Fragment frag){

        getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_bottom,frag).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }


    private void init() {
        navUserName = (TextView) findViewById(R.id.navBarUserName);
        navUserEmail = (TextView) findViewById(R.id.navBarUserEmail);
//        navUserName.setText(user.getDisplayName());
//        navUserEmail.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_home_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.userFilterAll) {
            Toast.makeText(this, "All is selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterCloths) {
            Toast.makeText(this, "All is Clothes", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterHardware) {
            Toast.makeText(this, "All is Hardware", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterResturant) {
            Toast.makeText(this, "All is Resturant", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.userFilterShoes) {
            Toast.makeText(this, "All is Shoes", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_home) {
            // Handle the camera action
            Intent i = new Intent(UserHomePageActivity.this, UserHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_user_profile) {
            Intent i = new Intent(UserHomePageActivity.this, UserProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_setting) {
            Intent i = new Intent(UserHomePageActivity.this, UserSettingPageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_help) {
            Intent i = new Intent(UserHomePageActivity.this, UserHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_home_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
