package com.szabist.fyp.guideus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by alidhanani on 18/11/2017.
 */

public class SideBarFrag extends Fragment {
    TextView navUserName, navUserEmail;
    FirebaseAuth Auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Auth = FirebaseAuth.getInstance();
        user = Auth.getInstance().getCurrentUser();
        View view=inflater.inflate(R.layout.nav_header_user_home_page,container,false);
        navUserName = (TextView) view.findViewById(R.id.navBarUserName);
        navUserEmail = (TextView) view.findViewById(R.id.navBarUserEmail);
        navUserName.setText(user.getDisplayName());
        navUserEmail.setText(user.getEmail());
        return view;
    }
}
