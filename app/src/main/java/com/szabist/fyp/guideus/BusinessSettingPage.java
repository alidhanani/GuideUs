package com.szabist.fyp.guideus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class BusinessSettingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnLogout, btnPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setting_page);
        FirebaseApp.initializeApp(getApplicationContext());

        btnLogout = (Button) findViewById(R.id.busLogBtn);
        btnPass = (Button) findViewById(R.id.busPassbtn);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PasswordBusActivity.class);
                startActivity(i);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(BusinessSettingPage.this, MainActivity.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBusSetting);
        setSupportActionBar(toolbar);

        BusinessSideBar side = new BusinessSideBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_business_setting_view, side).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_setting_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_business_setting_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_setting_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.business_home_page, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_business_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business_home) {
            Intent i = new Intent(BusinessSettingPage.this, BusinessHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_business_profile) {
            Intent i = new Intent(BusinessSettingPage.this, BusinessProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_mystore) {
            Intent i = new Intent(BusinessSettingPage.this, BusinessMyStorePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_setting) {
            Intent i = new Intent(BusinessSettingPage.this, BusinessSettingPage.class);
            startActivity(i);

        } else if (id == R.id.nav_business_help) {
            Intent i = new Intent(BusinessSettingPage.this, BusinessHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_setting_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
