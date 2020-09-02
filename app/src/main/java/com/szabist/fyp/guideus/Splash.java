package com.szabist.fyp.guideus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    static boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(getApplicationContext());
        final FirebaseAuth Auth = FirebaseAuth.getInstance();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    final FirebaseUser user = Auth.getInstance().getCurrentUser();
                    if (user == null) {
                        // User is signed in
                        Intent intent = new Intent(Splash.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        // No user is signed in
                        mPostReference = FirebaseDatabase.getInstance().getReference()
                                .child("user").child(user.getUid());
                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get Post object and use the values to update the UI
                                try {
                                    User users = dataSnapshot.getValue(User.class);
                                    status = users.Type.isEmpty();

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                // [START_EXCLUDE]
                                if(status != true) {
                                    Intent i = new Intent(Splash.this, UserHomePageActivity.class);
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(Splash.this, BusinessHomePageActivity.class);
                                    startActivity(i);
                                }
                                // [END_EXCLUDE]
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                // [START_EXCLUDE]
                                Toast.makeText(Splash.this, "Failed to load post.",
                                        Toast.LENGTH_SHORT).show();
                                // [END_EXCLUDE]
                            }
                        };
                        mPostReference.addValueEventListener(postListener);
                    }
                }
            }
        };
        timerThread.start();

    }

    private void getUserType() {
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
                if(users.Type != "Normal") {
                    Intent i = new Intent(Splash.this, UserHomePageActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Splash.this, BusinessHomePageActivity.class);
                    startActivity(i);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(Splash.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

}
