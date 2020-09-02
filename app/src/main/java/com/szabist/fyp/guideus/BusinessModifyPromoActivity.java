package com.szabist.fyp.guideus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;

public class BusinessModifyPromoActivity extends AppCompatActivity {

    EditText txtID, txtName, txtDesc, txtContact, txtEmail;
    Spinner area;
    Button btnAdd, btnSub;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostReference;
    private DatabaseReference mPostReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_modify_promo);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init();
        getValueFromFirebase();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                final FirebaseAuth Auth = FirebaseAuth.getInstance();
                mPostReference = FirebaseDatabase.getInstance().getReference()
                        .child("business").child(user.getUid().toString()).child("Store");
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("ID").setValue(txtID.getText().toString());
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("PromoName").setValue(txtName.getText().toString());
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Description").setValue(txtDesc.getText().toString());
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Contact").setValue(txtContact.getText().toString());
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Email").setValue(txtEmail.getText().toString());
                        mDatabase.child("business").child(user.getUid()).child("Store").child("Promo").child("Area").setValue(area.getSelectedItem().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("ID").setValue(txtID.getText().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("PromoName").setValue(txtName.getText().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("Description").setValue(txtDesc.getText().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("Email").setValue(txtEmail.getText().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("Area").setValue(area.getSelectedItem().toString());
                        mDatabase.child("store").child(users.get("Type")).child(users.get("ID")).child("Promo").child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("ProfileImg").setValue("DefaultImage/UserDefaulImage.png");


                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("ID").setValue(txtID.getText().toString());
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("PromoName").setValue(txtName.getText().toString());
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("Description").setValue(txtDesc.getText().toString());
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("Contact").setValue(txtContact.getText().toString());
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("Email").setValue(txtEmail.getText().toString());
                        mDatabase.child("store").child("All").child(users.get("ID")).child("Promo").child("Area").setValue(area.getSelectedItem().toString().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        // [START_EXCLUDE]
                        Toast.makeText(getApplicationContext(), "Failed to load post.",
                                Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                mPostReference.addValueEventListener(postListener);
            }
        });
    }

    private void init() {
        txtID = (EditText) findViewById(R.id.busStoreModID);
        txtName = (EditText) findViewById(R.id.busStoreModName);
        txtDesc = (EditText) findViewById(R.id.busStoreModDescription);
        txtContact = (EditText) findViewById(R.id.busStoreModContact);
        txtEmail = (EditText) findViewById(R.id.busStoreModEmail);
        area = (Spinner) findViewById(R.id.busStoreModType);
        btnAdd = (Button) findViewById(R.id.btnbusStoreModAddPromo);
        btnSub = (Button) findViewById(R.id.btnbusStoreModSubPromo);
    }

//    private void btnWork() {
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//    }

    private void getValueFromFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("business").child(user.getUid().toString()).child("Store").child("Promo");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                // [START_EXCLUDE]
                if(users.get("ID").equals("")){
                    Random rand = new Random();
                    txtID.setText(String.valueOf(rand.nextInt(1000) + 1));
                    txtName.setText(users.get("PromoName"));
                    txtEmail.setText(users.get("Email"));
                    txtContact.setText(users.get("Contact"));
                    txtDesc.setText(users.get("Description"));
                } else {
                    txtID.setText(users.get("ID"));
                    txtName.setText(users.get("PromoName"));
                    txtEmail.setText(users.get("Email"));
                    txtContact.setText(users.get("Contact"));
                    txtDesc.setText(users.get("Description"));
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(getApplicationContext(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

}
