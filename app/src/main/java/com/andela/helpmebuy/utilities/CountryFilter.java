package com.andela.helpmebuy.utilities;

import android.widget.Filter;

import com.andela.helpmebuy.adapters.LocationAdapter;
import com.andela.helpmebuy.models.Country;

import java.util.ArrayList;
import java.util.List;

public class CountryFilter extends ModelFilter<Country> {
    private final LocationAdapter adapter;

    public CountryFilter(LocationAdapter adapter, List<Country> originalValue) {
        super();
        this.adapter = adapter;
        this.initialValue = originalValue;
        this.filteredResult = new ArrayList<>();
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        filteredResult.clear();
        final Filter.FilterResults filterResults = new Filter.FilterResults();

        if (constraint.length() == 0) {
            filteredResult.addAll(initialValue);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final Country country : initialValue) {
                if (country.getName().contains(filterPattern)) {
                    filteredResult.add(country);
                }
            }
        }
        filterResults.values = filteredResult;
        filterResults.count = filteredResult.size();

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults filterResults) {
        filteredResult.clear();
        filteredResult.addAll((ArrayList<Country>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
