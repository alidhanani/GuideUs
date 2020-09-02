package com.szabist.fyp.guideus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

/**
 * Created by alidhanani on 04/11/2017.
 */

public class BusinessBottomFragmentHome extends Fragment {
    View mView;
    static BusinessValues val = new BusinessValues();
    StorageReference storageRef;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_business_home_below_frag, null);

        //Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),val.getRes(), Toast.LENGTH_SHORT).show();
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mapGoogle);
//        mapFragment.getMapAsync(this);

        TextView txtName=(TextView)mView.findViewById(R.id.busHomeName);
        txtName.setText(val.getRes());
        TextView txtAddress = (TextView)mView.findViewById(R.id.busHomeDesc);
        txtAddress.setText(val.getAdd());
        TextView txtType = (TextView) mView.findViewById(R.id.busHomeType);
        txtType.setText(val.getType());
        TextView txtEmail = (TextView) mView.findViewById(R.id.busHomeEmail);
        txtEmail.setText(val.getEmail());
        TextView txtContact = (TextView) mView.findViewById(R.id.busHomeContact);
        txtContact.setText(val.getCont());
        TextView txtID = (TextView) mView.findViewById(R.id.busHomeID);
        txtID.setText(val.getId());
        TextView txtPromoTitle = (TextView) mView.findViewById(R.id.busHomePromoShow);
        txtPromoTitle.setText(val.getpDesc());
        TextView txtPromoDesc = (TextView) mView.findViewById(R.id.busHomePromoDesc);
        txtPromoDesc.setText(val.getpTitle());
        Button btnMap = (Button) mView.findViewById(R.id.btnBusinessMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("mapLong", val.getLog());
                bundle.putString("mapLat", val.getLat());
                Intent i = new Intent(getContext(), BusinessMappingActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        //final ImageView img = (ImageView) mView.findViewById(R.id.userHomeImg);
//        Glide
//                .with(getContext())
//                .load(val.getUrl()) // the uri you got from Firebase
//                .centerCrop()
//                .into(img);
        //Picasso.with(getContext()).load(val.getUrl()).into(img);
//        storageRef = FirebaseStorage.getInstance().getReference();
//        String id = val.getUrl();
//        if(id.isEmpty()) {
//            id = "gs://androidproject-4bc34.appspot.com/storePic/KFC.png";
//        }
//        storageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                //Toast.makeText(getApplicationContext(), "GET IMAGE SUCCESSFUL",Toast.LENGTH_LONG).show();
//                if(uri==null){
//                    Toast.makeText(getContext(), "URI IS NULL",Toast.LENGTH_LONG).show();
//                }
//                try {
//                    ImageView img = (ImageView) mView.findViewById(R.id.userHomeImg);
//                    Picasso.with(getContext()).load(val.getUrl()).into(img);
//                    //img.setImageURI(uri);
//
//                }
//                catch (Exception e){
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(getContext(), "GET IMAGE FAILED",Toast.LENGTH_LONG).show();
//                // Handle any errors
//            }
//        });

        return mView;
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    void setMessage(String msg, String msg2){
        TextView txtName=(TextView)mView.findViewById(R.id.busHomeName);
        txtName.setText(val.getRes());
        TextView txtAddress = (TextView)mView.findViewById(R.id.busHomeDesc);
        txtAddress.setText(val.getAdd());
    }
}
