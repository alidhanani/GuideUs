package com.szabist.fyp.guideus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class UserSettingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Button btnLogout, btnPassChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_page);
        FirebaseApp.initializeApp(getApplicationContext());

        btnPassChange = (Button) findViewById(R.id.userPassbtn);
        btnPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PasswordUserActivity.class);
                startActivity(i);
            }
        });
        btnLogout = (Button) findViewById(R.id.userLogBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(UserSettingPageActivity.this, MainActivity.class);
                    startActivity(i);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUserSetting);
        setSupportActionBar(toolbar);

        SideBarFrag side = new SideBarFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_user_setting_view, side).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_setting_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_user_setting_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_setting_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.user_home_page, menu);
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
//        if (id == R.id.action_user_settings) {
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

        if (id == R.id.nav_user_home) {
            // Handle the camera action
            Intent i = new Intent(UserSettingPageActivity.this, UserHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_user_profile) {
            Intent i = new Intent(UserSettingPageActivity.this, UserProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_setting) {
            Intent i = new Intent(UserSettingPageActivity.this, UserSettingPageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_help) {
            Intent i = new Intent(UserSettingPageActivity.this, UserHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_setting_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void getSignOut() {
//        AuthUI.getInstance()
//                .signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent i = new Intent(UserSettingPageActivity.this, MainActivity.class);
//                        startActivity(i);
//                    }
//                });
//    }

}
