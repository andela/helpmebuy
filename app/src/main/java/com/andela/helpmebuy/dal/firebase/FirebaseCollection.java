package com.andela.helpmebuy.dal.firebase;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.models.Model;
import com.andela.helpmebuy.config.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCollection<T extends Model> implements DataCollection<T> {

    private Firebase firebase;

    private String childName;

    private Class<T> type;

    public FirebaseCollection(String childName, Class<T> type) {
        this.firebase = new Firebase(Constants.FIREBASE_URL);

        this.childName = childName;

        this.type = type;
    }

    @Override
    public void save(T data, DataCallback<T> callback) {
        firebase.child(childName)
                .child(data.getId())
                .setValue(data);

        if (callback != null) {
            callback.onSuccess(data);
        }
    }

    @Override
    public void get(String id, final DataCallback<T> callback) {
        firebase.child(childName)
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callback.onSuccess(dataSnapshot.getValue(type));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void getAll(final DataCallback<List<T>> callback) {
        firebase.child(childName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> data = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            data.add(snapshot.getValue(type));
                        }

                        callback.onSuccess(data);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void query(String path, String arg, final DataCallback<List<T>> callback) {
        Query query = firebase.child(childName).orderByChild(path).equalTo(arg);
        firebaseQuery(query, callback);
    }

    @Override
    public void query(String path, int arg, DataCallback<List<T>> callback) {
        Query query = firebase.child(childName).orderByChild(path).equalTo(arg);
        firebaseQuery(query, callback);
    }

    private void firebaseQuery(Query query, final DataCallback<List<T>> callback) {
        final List<T> data = new ArrayList<>();

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                data.add(dataSnapshot.getValue(type));
                callback.onSuccess(data);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                callback.onError("Connection was cancelled");
            }

        });
    }
}
