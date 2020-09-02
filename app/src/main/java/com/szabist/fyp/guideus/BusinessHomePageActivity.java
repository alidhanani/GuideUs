package com.szabist.fyp.guideus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BusinessHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BusinessTopFragmentHome.OnButtonPressListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBusHome);
        setSupportActionBar(toolbar);

        BusinessTopFragmentHome topFrag = new BusinessTopFragmentHome();
        BusinessBottomFragmentHome bottomFrag = new BusinessBottomFragmentHome();
        //UserBottomMapFrag bottomMap = new UserBottomMapFrag();
        BusinessSideBar side = new BusinessSideBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_business_home_view, side).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.bus_frgrament_top, topFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bus_frgrament_bottom, bottomFrag).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.map, bottomFrag).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_bottom, bottomMap).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_home_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_business_home_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onButtonPressed(String msg, String msg2) {
        BusinessBottomFragmentHome Obj=(BusinessBottomFragmentHome) getSupportFragmentManager().findFragmentById(R.id.bus_frgrament_bottom);
        Obj.setMessage(msg, msg2);
    }



    public void AddFragment(Fragment frag){

        getSupportFragmentManager().beginTransaction().replace(R.id.bus_frgrament_bottom,frag).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_home_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.business_home_page, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business_home) {
            // Handle the camera action
            Intent i = new Intent(BusinessHomePageActivity.this, BusinessHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_business_profile) {
            Intent i = new Intent(BusinessHomePageActivity.this, BusinessProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_mystore) {
            Intent i = new Intent(BusinessHomePageActivity.this, BusinessMyStorePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_setting) {
            Intent i = new Intent(BusinessHomePageActivity.this, BusinessSettingPage.class);
            startActivity(i);

        } else if (id == R.id.nav_business_help) {
            Intent i = new Intent(BusinessHomePageActivity.this, BusinessHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_home_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
