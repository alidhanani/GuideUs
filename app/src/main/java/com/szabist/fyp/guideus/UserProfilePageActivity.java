package com.szabist.fyp.guideus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UserProfilePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    EditText name, email, contact, address;
    Spinner area;
    ImageView profimage;
    private static final String TAG = "PostDetailActivity";
    Button btnSave;
    private StorageReference mStorage;
    FirebaseStorage storage;
    final FirebaseAuth Auth = FirebaseAuth.getInstance();
    final FirebaseUser user = Auth.getCurrentUser();
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init();
        getValueFromFirebase();
        applySave();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);

        SideBarFrag side = new SideBarFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_user_profile_view, side).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_profile_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_user_profile_view);
        navigationView.setNavigationItemSelectedListener(this);


//        StorageReference storageRef = storage.getReference().child("DefaultImage").child(user.getUid().toString());//reach out to your photo file hierarchically
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("URI", uri.toString()); //check path is correct or not ?
//                Picasso.with(getApplicationContext()).load(uri.toString()).into(profimage);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle errors
//            }
//        });

//        final FirebaseAuth Auth = FirebaseAuth.getInstance();
//        final FirebaseUser user = Auth.getCurrentUser();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        profimage.buildDrawingCache();
//        profimage.setDrawingCacheEnabled(true);
//        Bitmap bitmap = profimage.getDrawingCache();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
//        byte[] data = baos.toByteArray();
//        String path = "/DefaultImage/UserDefaulImage.png";
//        mStorage = storage.getReference(path);
//
//        UploadTask uploadTask = mStorage.putBytes(data);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                downloadUri = taskSnapshot.getDownloadUrl();
//                Picasso.with(getApplicationContext()).load(downloadUri.toString()).into(profimage);
//            }
//        });

        profimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserImageActivity.class);
                startActivity(i);
            }
        });

    }


    private void init() {
        profimage = (ImageView) findViewById(R.id.userImg);
        name = (EditText) findViewById(R.id.userProfileName);
        email = (EditText) findViewById(R.id.userProfileEmail);
        contact = (EditText) findViewById(R.id.userProfileContact);
        address = (EditText) findViewById(R.id.userProfileAddress);
        area = (Spinner) findViewById(R.id.spinnerUserProfileArea);
        btnSave = (Button) findViewById(R.id.userProfileSave);

    }

    private void getValueFromFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user").child(user.getUid().toString());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User users = dataSnapshot.getValue(User.class);
                // [START_EXCLUDE]
                name.setText(users.Name);
                email.setText(users.Email);
                address.setText(users.Address);
                //area.setText(users.Area);
                contact.setText(users.Contact);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(UserProfilePageActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

    private void applySave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateDetail();
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDetail() {
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user").child(user.getUid()).child("Email").setValue(email.getText().toString());
        mDatabase.child("user").child(user.getUid()).child("Area").setValue(area.getSelectedItem().toString());
        mDatabase.child("user").child(user.getUid()).child("Address").setValue(address.getText().toString());
        mDatabase.child("user").child(user.getUid()).child("Contact").setValue(contact.getText().toString());
        mDatabase.child("user").child(user.getUid()).child("Name").setValue(name.getText().toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_profile_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
            Intent i = new Intent(UserProfilePageActivity.this, UserHomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_user_profile) {
            Intent i = new Intent(UserProfilePageActivity.this, UserProfilePageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_setting) {
            Intent i = new Intent(UserProfilePageActivity.this, UserSettingPageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_user_help) {
            Intent i = new Intent(UserProfilePageActivity.this, UserHelpPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_user_profile_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
