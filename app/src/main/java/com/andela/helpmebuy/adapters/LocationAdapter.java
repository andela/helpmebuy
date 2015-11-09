package com.andela.helpmebuy.adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andela.helpmebuy.R;

import java.util.ArrayList;
import java.util.List;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{

    private Context context;
    private List<String> locations;

    public LocationAdapter(Context context) {
        this.context = context;
        this.locations = new ArrayList<>();
    }

    public LocationAdapter(Context context, List<String>locations) {
        this(context);
        this.locations = locations;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String>locations) {
        this.locations = locations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.locationView.setText(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView locationView;

        public ViewHolder(View view) {
            super(view);
            locationView = (TextView) view;
        }
    }


}
