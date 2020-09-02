package com.szabist.fyp.guideus;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText fname, lname, email, pass, confirmpass;
    Button btnReg;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        initialise();
        btnSigningMethod();
    }

    private void initialise() {
        fname = (EditText) findViewById(R.id.txtFirstName);
        lname = (EditText) findViewById(R.id.txtLastName);
        email = (EditText) findViewById(R.id.txtEmail);
        pass = (EditText) findViewById(R.id.txtPass);
        confirmpass = (EditText) findViewById(R.id.txtPassConfirm);
        btnReg = (Button) findViewById(R.id.btnSignUp);
    }

    private String getFirstName() {
        return fname.getText().toString();
    }

    private String getLastName() {
        return lname.getText().toString();
    }

    private String getEmail() {
        return email.getText().toString();
    }

    private String getPass() {
        return pass.getText().toString();
    }

    private String getConfirmPass() {
        return confirmpass.getText().toString();
    }

    private void btnSigningMethod() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = getFirstName();
                String last = getLastName();
                String address = getEmail();
                String password = getPass();
                String confirm = getConfirmPass();

                if(first.equals("")) {
                    fname.setError("Enter First Name");
                } else if(last.equals("")) {
                    lname.setError("Enter Last Name");
                } else if(address.equals("")) {
                    email.setError("Enter Email Address");
                } else if(password.equals("")) {
                    pass.setError("Enter Password");
                } else if(confirm.equals("")) {
                    confirmpass.setError("Enter Confirm Pass");
                } else if(!password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Password is not same", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseSignUp(address, password);
                }

            }
        });
    }

    private void FirebaseSignUp(String email, String pass) {
        final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(getFirstName()+" "+getLastName())
                .setPhotoUri(Uri.parse("DefaultImage/UserDefaulImage.png"))
                .build();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("user").child(user.getUid()).child("Email").setValue(user.getEmail());
                            mDatabase.child("user").child(user.getUid()).child("ID").setValue(user.getUid());
                            mDatabase.child("user").child(user.getUid()).child("Area").setValue("");
                            mDatabase.child("user").child(user.getUid()).child("Type").setValue("Normal");
                            mDatabase.child("user").child(user.getUid()).child("Address").setValue("");
                            mDatabase.child("user").child(user.getUid()).child("Contact").setValue("");
                            mDatabase.child("user").child(user.getUid()).child("Name").setValue(getFirstName() +" "+ getLastName());
                            mDatabase.child("user").child(user.getUid()).child("Password").setValue(getPass());
                            mDatabase.child("user").child(user.getUid()).child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");
                            Intent i = new Intent(getApplicationContext(), UserHomePageActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}
