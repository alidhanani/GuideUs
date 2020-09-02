package com.szabist.fyp.guideus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BusinessProfilePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostReference;
    EditText txtName, txtEmail, txtContact;
    ImageView imgProfile;
    Spinner txtArea;
    Button btnSave;
    private StorageReference mStorage;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_page);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init();
        getValueFromFirebase();
        btnWork();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBusProfile);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_profile_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        BusinessSideBar side = new BusinessSideBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_business_profile_view, side).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_business_profile_view);
        navigationView.setNavigationItemSelectedListener(this);

//        mStorage.child("DefaultImage/UserDefaulImage.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                Picasso.with(getApplicationContext()).load(uri.getPath().toString()).into(imgProfile);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BusinessImageActivity.class);
                startActivity(i);
            }
        });

    }

    private void getValueFromFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("business").child(user.getUid().toString());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Business users = dataSnapshot.getValue(Business.class);
                // [START_EXCLUDE]
                    txtName.setText(users.Name);
                    txtEmail.setText(users.Email);
                    txtContact.setText(users.Contact);
                mStorage = storage.getReference();
                StorageReference pathReference = mStorage.child(users.ProfileImg);
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(imgProfile);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(BusinessProfilePageActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

    private void init() {
        imgProfile = (ImageView) findViewById(R.id.businessProfileImg);
        txtName = (EditText) findViewById(R.id.businessProfileName);
        txtEmail = (EditText) findViewById(R.id.businessProfileEmail);
        txtContact = (EditText) findViewById(R.id.businessProfileContact);
        txtArea = (Spinner) findViewById(R.id.spinnerBusinessProfileArea);
        btnSave = (Button) findViewById(R.id.businessProfileSave);
    }

    private String getName() {
        return txtName.getText().toString();
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

    private void btnWork() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabase.child("business").child(user.getUid()).child("Email").setValue(getEmail());
                mDatabase.child("business").child(user.getUid()).child("Name").setValue(getName());
                mDatabase.child("business").child(user.getUid()).child("Contact").setValue(getContact());
                mDatabase.child("business").child(user.getUid()).child("Area").setValue(getArea());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_profile_layout);
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
            Intent i = new Intent(BusinessProfilePageActivity.this, BusinessHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_business_profile) {
            Intent i = new Intent(BusinessProfilePageActivity.this, BusinessProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_mystore) {
            Intent i = new Intent(BusinessProfilePageActivity.this, BusinessMyStorePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_business_setting) {
            Intent i = new Intent(BusinessProfilePageActivity.this, BusinessSettingPage.class);
            startActivity(i);

        } else if (id == R.id.nav_business_help) {
            Intent i = new Intent(BusinessProfilePageActivity.this, BusinessHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_business_profile_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
