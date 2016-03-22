package com.andela.helpmebuy.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.ConnectionStatus;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.transforms.CircleTransformation;
import com.andela.helpmebuy.utilities.CurrentTravelListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class TravellersAdapter extends RecyclerView.Adapter<TravellersAdapter.ViewHolder> {

    private Context context;

    private List<Travel> travels;

    private FirebaseCollection<User> users;

    private FirebaseCollection<Connection> connection;

    private Location location;

    private CurrentTravelListener currentTravelListener;

    public TravellersAdapter(Context context) {
        this.context = context;
        this.travels = new ArrayList<>();
        this.users = new FirebaseCollection<>(Constants.USERS, User.class);
    }

    public TravellersAdapter(Context context, List<Travel> travels) {
        this(context);
        this.travels = travels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.traveller_item_view, parent, false);

        return new ViewHolder(view, new TravelClickListener());
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Travel travel = travels.get(position);
        final String travelUserId = travel.getUserId();

        viewHolder.connectButton.setVisibility(View.VISIBLE);
        viewHolder.connectButton.setEnabled(true);
        viewHolder.connectButton.setText("CONNECT");

        viewHolder.travelClickListener.updatePosition(position);
        viewHolder.travelClickListener.bindData(travelUserId);

        users.get(travelUserId, new DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                String profilePictureUrl = user.getProfilePictureUrl();

                Picasso.with(context)
                        .load(profilePictureUrl.isEmpty() ? "http://example.com" : profilePictureUrl)
                        .placeholder(R.drawable.ic_account_circle_black_48dp)
                        .error(R.drawable.ic_account_circle_black_48dp)
                        .transform(new CircleTransformation())
                        .into(viewHolder.profilePicture);

                if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
                    viewHolder.profilePicture.setAlpha(0.38f);
                }

                viewHolder.name.setText(user.getFullName());
            }

            @Override
            public void onError(String errorMessage) {
            }
        });

        String address = travel.getDepartureAddress().getLocation().getCity().getName() + ", " + travel.getDepartureAddress().getLocation().getCountry().getName();
        viewHolder.departureLocation.setText(address);

        if (travel.getArrivalDate() != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM e, y h:m a");

            viewHolder.departureDate.setText(travel.getArrivalDate().withZone(DateTimeZone.getDefault()).toString(formatter));
        }
    }


    @Override
    public int getItemCount() {
        return travels.size();
    }

    public Travel getTravel(int thePosition) {
        return travels.get(thePosition);
    }

    public List<Travel> getTravels() {
        return travels;
    }

    public void setTravels(List<Travel> travels) {
        this.travels = travels;
    }

    public void setCurrentTravelListener(CurrentTravelListener currentTravelListener) {
        this.currentTravelListener = currentTravelListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicture;
        TextView name;
        TextView departureLocation;
        TextView departureDate;
        Button connectButton;
        TravelClickListener travelClickListener;

        public ViewHolder(View view, TravelClickListener travelClickListener) {
            super(view);
            profilePicture = (ImageView) view.findViewById(R.id.traveller_profile_picture);
            name = (TextView) view.findViewById(R.id.traveller_name);
            departureLocation = (TextView) view.findViewById(R.id.traveller_departure_location);
            departureDate = (TextView) view.findViewById(R.id.traveller_arrival_date);
            connectButton = (Button) view.findViewById(R.id.connect_button);
            this.travelClickListener = travelClickListener;
            this.travelClickListener.setButton(connectButton);
            connectButton.setOnClickListener(this.travelClickListener);
        }
    }

    private class TravelClickListener implements View.OnClickListener {
        private int position;
        private Button button;
        private String userId = CurrentUserManager.get(context).getId();

        public void updatePosition(int position) {
            this.position = position;
        }

        public void setButton(Button button) {
            this.button = button;
        }

        private void requestSent() {
            button.setVisibility(View.VISIBLE);
            button.setText(R.string.connection_pending);
            button.setEnabled(false);
        }

        @Override
        public void onClick(View view) {
            requestSent();
            currentTravelListener.getCurrentTravel(getTravel(this.position));
        }

        public void bindData(final String travelUserId) {
            new FirebaseCollection<>(Constants.CONNECTIONS + "/" + userId, Connection.class)
                    .get(travelUserId, new DataCallback<Connection>() {
                        @Override
                        public void onSuccess(Connection data) {
                            if (data != null) {
                                if (data.getConnectionStatus() == ConnectionStatus.PENDING.getStatus()) {
                                    requestSent();
                                } else if (data.getConnectionStatus() == ConnectionStatus.ACCEPTED.getStatus()) {
                                    button.setVisibility(View.GONE);
                                } else {
                                    button.setVisibility(View.VISIBLE);
                                    button.setEnabled(true);
                                    button.setText("CONNECT");
                                }
                            }

                            if (userId.equals(travelUserId)) {
                                button.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
        }
    }
}
