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
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.transforms.CircleTransformation;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConnectionRequestsAdapter extends RecyclerView.Adapter<ConnectionRequestsAdapter.CustomViewHolder> {

    private List<Connection> connections;
    private ConnectionRequestListener callback;
    private Context context;
    private View view;

    public ConnectionRequestsAdapter(List<Connection> connections, Context context, View view) {
        this.connections = connections;
        this.context = context;
        this.view = view;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connection_request_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        getItemCount();
        final Connection connection = this.connections.get(position);

        new FirebaseCollection<>(Constants.USERS, User.class)
                .get(connection.getId(), new DataCallback<User>() {
                    @Override
                    public void onSuccess(User data) {
                        bindUser(data, holder, connection);
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.accept:
                        connection.setConnectionStatus(ConnectionStatus.ACCEPTED.getStatus());
                        break;
                    case R.id.reject:
                        connection.setConnectionStatus(ConnectionStatus.REJECTED.getStatus());
                        break;
                }

                if (callback != null) {
                    callback.onConnectionUpdate(connection);
                    onItemDismiss(position);
                }
            }
        };

        holder.accept.setOnClickListener(clickListener);
        holder.reject.setOnClickListener(clickListener);
    }

    private void bindUser(User user, CustomViewHolder holder, Connection connection) {
        String profilePictureUrl = user.getProfilePictureUrl();
        if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
            holder.profilePicture.setAlpha(0.38f);
        } else {
            Picasso.with(context)
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.ic_account_circle_black_48dp)
                    .error(R.drawable.ic_account_circle_black_48dp)
                    .transform(new CircleTransformation())
                    .into(holder.profilePicture);
        }

        holder.username.setText(user.getFullName());
        holder.message.setText(connection.getMessage());
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void onItemDismiss(int position) {
        connections.remove(position);
        swapList(connections);
        resizeLayout();
        notifyDataSetChanged();
    }

    public void resizeLayout() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_conn_req);
        TextView noContactRequest = (TextView) view.findViewById(R.id.no_contact_request);
        noContactRequest.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        int itemCount = connections.size();

        if(itemCount > 0){
            recyclerView.getLayoutParams().height = 300;
            recyclerView.setVisibility(View.VISIBLE);
        }
        if(itemCount == 0){
            recyclerView.getLayoutParams().height = 100;
            noContactRequest.setVisibility(View.VISIBLE);
        }
    }

    public void swapList(List<Connection> newList){
        connections = newList;
        notifyDataSetChanged();
        resizeLayout();
    }

    public void setConnectionRequestListener(ConnectionRequestListener callback) {
        this.callback = callback;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;
        TextView message;
        Button accept;
        Button reject;

        public CustomViewHolder(View view) {
            super(view);
            profilePicture = (ImageView) view.findViewById(R.id.profile_pic);
            username = (TextView) view.findViewById(R.id.username);
            message = (TextView) view.findViewById(R.id.message);
            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);
        }
    }
}
