package com.szabist.fyp.guideus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordUserActivity extends AppCompatActivity {

    EditText txtOldPass, txtNewPass;
    private DatabaseReference mDatabase;
    Button btnChange;
    private DatabaseReference mPostReference;
    private static final String TAG = "PostDetailActivity";
    protected static String firebasePass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_user);
        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init();
        btnWork();
    }

    private void init() {
        txtOldPass = (EditText) findViewById(R.id.userOldPassword);
        txtNewPass = (EditText) findViewById(R.id.userNewPassword);
        btnChange = (Button) findViewById(R.id.userPasswordChange);
    }


    private void btnWork() {
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserPass();
                final String oldPass = txtOldPass.getText().toString();
                final String newPass = txtNewPass.getText().toString();

                if(oldPass.equals("")) {
                    txtOldPass.setError("Enter Old Pass");
                } else if(newPass.equals("")) {
                    txtNewPass.setError("Enter New Pass");
                } else {
                    if(oldPass != firebasePass) {
                        Toast.makeText(getApplicationContext(), "Your Password Doesn't Matches The Old Password", Toast.LENGTH_SHORT).show();
                    } else {
                        user.updatePassword(newPass)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                            mDatabase.child("user").child(user.getUid()).child("Password").setValue(newPass);
                                            Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),"There was an error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

    }

    private void getUserPass() {
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
                firebasePass = users.Password;
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PasswordUserActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

}
