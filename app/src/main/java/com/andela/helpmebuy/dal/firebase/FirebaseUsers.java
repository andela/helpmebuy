package com.andela.helpmebuy.dal.firebase;


import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.Users;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUsers implements Users {

    Firebase firebase;

    public FirebaseUsers() {
        firebase = new Firebase(Constants.FIREBASE_URL);
    }

    @Override
    public void save(User user, DataCallback<User> callback) {
        firebase.child(Constants.USERS)
                .child(user.getId())
                .setValue(user);

        if (callback != null) {
            callback.onSuccess(user);
        }
    }

    @Override
    public void get(String userId, final DataCallback<User> callback) {
        firebase.child(Constants.USERS)
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        callback.onSuccess(user);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void getAll(final DataCallback<List<User>> callback) {
        firebase.child(Constants.USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> users = new ArrayList<>();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            users.add(snapshot.getValue(User.class));
                        }

                        callback.onSuccess(users);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void query(String[] selection, String[] selectionArgs, final DataCallback<List<User>> callback) {
        callback.onSuccess(new ArrayList<User>());
    }
}
