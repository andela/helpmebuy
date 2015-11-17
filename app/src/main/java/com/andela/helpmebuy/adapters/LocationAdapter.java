package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.views.LocationView;

import java.util.ArrayList;
import java.util.List;


public class LocationAdapter<T extends Location> extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;

    private List<T> initialLocations;

    private List<T> locations;

    private LocationView.OnLocationClickedListener<T> listener;

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
    public void onBindViewHolder(LocationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.locationName.setText(locations.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout location;
        public TextView locationName;

        public ViewHolder(View view) {
            super(view);

            locationName = (TextView) view.findViewById(R.id.current_location);
            location = (LinearLayout) view.findViewById(R.id.item_view);
            location.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            if (LocationAdapter.this.listener != null) {
                                int position = ViewHolder.this.getAdapterPosition();

                                T location = LocationAdapter.this.locations.get(position);

                                LocationAdapter.this.listener.onLocationClicked(location);
                            }
                        }
                    });
        }
    }

    public void setOnLocationClickedListener(LocationView.OnLocationClickedListener listener) {
        this.listener = listener;
    }

}
