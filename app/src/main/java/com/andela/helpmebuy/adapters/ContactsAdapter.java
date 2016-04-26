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
import com.andela.helpmebuy.models.Contact;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.transforms.CircleTransformation;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.CustomViewHolder> {

    private Context context;

    private List<Contact> contacts;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        new FirebaseCollection<>(Constants.USERS, User.class)
                .get(contact.getId(), new DataCallback<User>() {
                    @Override
                    public void onSuccess(User data) {
                        bindUser(data, holder);
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    private void bindUser(User user, CustomViewHolder holder) {
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
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;

        public CustomViewHolder(View view) {
            super(view);
            profilePicture = (ImageView) view.findViewById(R.id.profile_pic);
            username = (TextView) view.findViewById(R.id.username);
        }
    }
}
