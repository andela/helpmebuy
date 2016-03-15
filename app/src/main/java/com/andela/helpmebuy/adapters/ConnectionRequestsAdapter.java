package com.andela.helpmebuy.adapters;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.ConnectionStatus;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;

import java.util.List;

public class ConnectionRequestsAdapter extends RecyclerView.Adapter<ConnectionRequestsAdapter.CustomViewHolder> {

    private List<Connection> connections;
    private ConnectionRequestListener callback;

    public ConnectionRequestsAdapter(List<Connection> connections) {
        this.connections = connections;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final Connection connection = this.connections.get(position);
        User user = connection.getUser();

        holder.profilePicture.setImageURI(Uri.parse(user.getProfilePictureUrl()));
        holder.username.setText(user.getFullName());
        holder.message.setText(connection.getMessage());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.accept:
                        connection.setConnectionStatus(ConnectionStatus.ACCEPTED.getStatus());
                        break;
                    case R.id.reject:
                        connection.setConnectionStatus(ConnectionStatus.REJECTED.getStatus());
                        break;
                }

                if(callback != null) {
                    onItemDismiss(position);
                    callback.onConnectionUpdate(connection);
                }
            }
        };

        holder.accept.setOnClickListener(clickListener);
        holder.reject.setOnClickListener(clickListener);
    }

    public void setConnectionRequestListener(ConnectionRequestListener callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void onItemDismiss(int position){
        connections.remove(position);
        notifyDataSetChanged();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicture;

        TextView username;

        TextView message;

        Button accept;

        Button reject;

        public CustomViewHolder(View view) {
            super(view);
            profilePicture = (ImageView)view.findViewById(R.id.profile_pic);
            username = (TextView)view.findViewById(R.id.username);
            message = (TextView)view.findViewById(R.id.message);
            accept = (Button)view.findViewById(R.id.accept);
            reject = (Button)view.findViewById(R.id.reject);

        }
    }
}
