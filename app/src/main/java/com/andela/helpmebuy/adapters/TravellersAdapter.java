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
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.transforms.CircleTransformation;
import com.andela.helpmebuy.util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

        final ImageView profilePicture = (ImageView) itemView.findViewById(R.id.traveller_profile_picture);
        final TextView name = (TextView) itemView.findViewById(R.id.traveller_name);
        final TextView departureLocation = (TextView) itemView.findViewById(R.id.traveller_departure_location);
        final TextView departureDate = (TextView) itemView.findViewById(R.id.traveller_departure_date);

        final Travel travel = travels.get(position);

        Firebase firebase = new Firebase(Constants.FIREBASE_URL + "/" + Constants.USERS + "/" + travel.getUserId());

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                String profilePictureUrl = user.getProfilePictureUrl();

                Picasso.with(context)
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.ic_account_circle_black_48dp)
                        .error(R.drawable.ic_account_circle_black_48dp)
                        .transform(new CircleTransformation())
                        .into(profilePicture);

                if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
                    profilePicture.setAlpha(0.54f);
                }

                name.setText(user.getFullName());
                departureLocation.setText(travel.getDepartureAddress().getCity() + ", " + travel.getDepartureAddress().getCountry());

                if (travel.getDepartureDate() != null) {
                    departureDate.setText(travel.getDepartureDate().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
