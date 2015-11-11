package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.utilities.LocationFilter;

import java.util.ArrayList;
import java.util.List;


public class LocationAdapter<T extends Location> extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;

    private List<T> initialLocations;

    private List<T> locations;

    public LocationAdapter(Context context) {
        this.context = context;
        this.initialLocations = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    public LocationAdapter(Context context, List<T> locations) {
        this(context);
        this.initialLocations = locations;
        this.locations = locations;
    }

    public List<T> getInitialLocations() {
        return initialLocations;
    }

    public void setInitialLocations(List<T> initialLocations) {
        this.initialLocations = initialLocations;
        this.locations = initialLocations;
    }

    public List<T> getLocations() {
        return locations;
    }

    public void setLocations(List<T> locations) {
        this.locations = locations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.locationView.setText(locations.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView locationView;

        public ViewHolder(View view) {
            super(view);
            locationView = (TextView) view.findViewById(R.id.current_location);
        }
    }

}
