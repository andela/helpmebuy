package com.andela.helpmebuy.utilities;

import android.widget.Filter;

import com.andela.helpmebuy.adapters.LocationAdapter;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationFilter<T extends Location> extends Filter {
    private final LocationAdapter<T> adapter;

    public LocationFilter(LocationAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        List<T> result = new ArrayList<>();

        final Filter.FilterResults filterResults = new Filter.FilterResults();

        List<T> locations = adapter.getInitialLocations();

        if (constraint.length() == 0) {
            result.addAll(locations);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final T location: locations) {
                if (location.getName().toLowerCase().startsWith(filterPattern)) {
                    result.add(location);
                }
            }
        }

        filterResults.values = result;
        filterResults.count = result.size();

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults filterResults) {
        adapter.setLocations((ArrayList<T>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
