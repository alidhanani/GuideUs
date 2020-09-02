package com.szabist.fyp.guideus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

/**
 * Created by alidhanani on 19/11/2017.
 */

public class BusinessSideBar extends Fragment {
    TextView navUserName, navUserEmail;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mPostReference;
    private DatabaseReference mDatabase;
    ImageView navImg;
    FirebaseAuth Auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Auth = FirebaseAuth.getInstance();
        user = Auth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final View view=inflater.inflate(R.layout.nav_header_business_home_page,container,false);
        navImg = (ImageView) view.findViewById(R.id.navBarBusinessImg);
        navUserName = (TextView) view.findViewById(R.id.navBarBusinessName);
        navUserEmail = (TextView) view.findViewById(R.id.navBarBusinessEmail);
        navUserName.setText(user.getDisplayName());
        navUserEmail.setText(user.getEmail());
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("business").child(user.getUid().toString());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<String, String> users = (Map<String, String>) dataSnapshot.getValue();
                // [START_EXCLUDE]
                storageReference = storage.getReference();
                StorageReference pathReference = storageReference.child(users.get("ProfileImg"));
                Glide.with(view.getContext())
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(navImg);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // [START_EXCLUDE]
                Toast.makeText(view.getContext(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);

        return view;
    }

}
