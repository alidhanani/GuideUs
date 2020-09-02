package com.szabist.fyp.guideus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserMappingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mapping);
        UserBottomMap topFrag = new UserBottomMap();
        getSupportFragmentManager().beginTransaction().replace(R.id.user_frgrament_map, topFrag).commit();
    }
}
