package com.andela.helpmebuy.dal.firebase;


import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.Travels;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseTravels implements Travels {

    Firebase firebase;

    public FirebaseTravels() {
        firebase = new Firebase(Constants.FIREBASE_URL);
    }

    @Override
    public void save(Travel travel, DataCallback<Travel> callback) {
        firebase.child(Constants.TRAVELS)
                .child(travel.getId())
                .setValue(travel);

        if (callback != null) {
            callback.onSuccess(travel);
        }
    }

    @Override
    public void get(String travelId, final DataCallback<Travel> callback) {
        firebase.child(Constants.TRAVELS)
                .child(travelId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Travel travel = dataSnapshot.getValue(Travel.class);

                        callback.onSuccess(travel);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void getAll(final DataCallback<List<Travel>> callback) {
        firebase.child(Constants.TRAVELS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Travel> travels = new ArrayList<>();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            travels.add(snapshot.getValue(Travel.class));
                        }

                        callback.onSuccess(travels);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void query(String[] selection, String[] selectionArgs, final DataCallback<List<Travel>> callback) {
        callback.onSuccess(new ArrayList<Travel>());
    }
}
