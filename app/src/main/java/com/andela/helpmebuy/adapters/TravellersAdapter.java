package com.andela.helpmebuy.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.transforms.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TravellersAdapter extends RecyclerView.Adapter<TravellersAdapter.ViewHolder> {

    private Context context;

    private List<Travel> travels;

    public TravellersAdapter(Context context) {
        this.context = context;
        this.travels = new ArrayList<>();
    }

    public TravellersAdapter(Context context, List<Travel> travels) {
        this.context = context;
        this.travels = travels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.traveller_item_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        FrameLayout itemView = viewHolder.itemView;

        ImageView profilePicture = (ImageView) itemView.findViewById(R.id.traveller_profile_picture);
        TextView name = (TextView) itemView.findViewById(R.id.traveller_name);
        TextView departureLocation = (TextView) itemView.findViewById(R.id.traveller_departure_location);
        TextView departureDate = (TextView) itemView.findViewById(R.id.traveller_departure_date);

        Travel travel = travels.get(position);

        String profilePictureUrl = travel.getUser().getProfilePictureUrl();
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Picasso.with(context)
                    .load(profilePictureUrl)
                    .transform(new CircleTransformation())
                    .into(profilePicture);
        }

        name.setText(travel.getUser().getFullName());
        departureLocation.setText(travel.getDepartureAddress().getCity() + ", " + travel.getDepartureAddress().getCountry());
        departureDate.setText(travel.getDepartureDate().toString());
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    public List<Travel> getTravels() {
        return travels;
    }

    public void setTravels(List<Travel> travels) {
        this.travels = travels;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout itemView;

        public ViewHolder(View view) {
            super(view);

            itemView = (FrameLayout) view;
        }

    }
}
