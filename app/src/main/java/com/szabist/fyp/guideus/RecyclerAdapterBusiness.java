package com.szabist.fyp.guideus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NerdMacAdmin on 03/01/2018.
 */

public class RecyclerAdapterBusiness extends RecyclerView.Adapter<RecyclerAdapterBusiness.TestViewHolder>  {

    Context mcontext;

    public interface OnButtonPressListener {
        public void onButtonPressed(String msg, String msg2);
    }

    BusinessTopFragmentHome.OnButtonPressListener buttonListener;

    public void onAttach(Activity activity) {
        try {
            buttonListener = (BusinessTopFragmentHome.OnButtonPressListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    public RecyclerAdapterBusiness(Context mcontext, ArrayList<ModelBusinessRecycler> recyclerList) {
        this.mcontext = mcontext;
        RecyclerList = recyclerList;
    }

    ArrayList<ModelBusinessRecycler> RecyclerList;

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mcontext)
                .inflate(R.layout.recycler_view_business,parent,false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.resturant.setText(RecyclerList.get(position).getResturant());
        holder.distance.setText(RecyclerList.get(position).getDistance());
        holder.more.setText(RecyclerList.get(position).getHere());
        final int value = position;
        //    holder.more.setOnClickListener(new View.OnClickListener() {
        //       public void onClick(View v) {
        //         buttonListener.onButtonPressed(RecyclerList.get(value).getResturant(), RecyclerList.get(value).getHere());

        //   }
        //  });

    }

    @Override
    public int getItemCount() {
        return RecyclerList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        TextView resturant , distance, more;
        public TestViewHolder(View itemView) {
            super(itemView);
            resturant = (TextView) itemView.findViewById(R.id.txtBusHomeShowName);
            distance = (TextView) itemView.findViewById(R.id.txtBusHomeShowDistance);
            more = (TextView) itemView.findViewById(R.id.txtBusHomeShowMore);






            // more.setOnClickListener(new );

            // more.setOnClickListener(new itemView.setOnClickListener(););
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // Toast.makeText(v.getContext(), RecyclerList.get(getAdapterPosition()).getResturant(), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(v.getContext(), RecyclerList.get(getAdapterPosition()).getHere(), Toast.LENGTH_SHORT).show();

                    BusinessValues val = new BusinessValues();
                    val.setRes(RecyclerList.get(getAdapterPosition()).getResturant());
                    val.setAdd(RecyclerList.get(getAdapterPosition()).getHere());
                    val.setId(RecyclerList.get(getAdapterPosition()).getID());
                    val.setCont(RecyclerList.get(getAdapterPosition()).getContact());
                    val.setEmail(RecyclerList.get(getAdapterPosition()).getEmail());
                    val.setType(RecyclerList.get(getAdapterPosition()).getType());
                    val.setUrl(RecyclerList.get(getAdapterPosition()).getUrl());
                    val.setLat(RecyclerList.get(getAdapterPosition()).getLatitude());
                    val.setLog(RecyclerList.get(getAdapterPosition()).getLongitude());
                    val.setOlat(RecyclerList.get(getAdapterPosition()).getoLatitude());
                    val.setOlog(RecyclerList.get(getAdapterPosition()).getoLongitude());
                    val.setpTitle(RecyclerList.get(getAdapterPosition()).getPromoTitle());
                    val.setpDesc(RecyclerList.get(getAdapterPosition()).getPromoDesc());


                    Bundle bundle = new Bundle();

//                    BusinessBottomMap userBottomMap = new BusinessBottomMap();
//                    userBottomMap.setArguments(bundle);
//                    ((BusinessHomePageActivity) v.getContext()).AddFragment(userBottomMap);


                    BusinessBottomFragmentHome userBottomFragmentHome = new BusinessBottomFragmentHome();
                    userBottomFragmentHome.setArguments(bundle);


                    ((BusinessHomePageActivity) v.getContext()).AddFragment(userBottomFragmentHome);
//                   // UserHomePageActivity userHomePageActivity = new UserHomePageActivity(

                }
            });


        }
    }
}
