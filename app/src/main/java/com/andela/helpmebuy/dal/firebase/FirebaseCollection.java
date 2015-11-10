package com.andela.helpmebuy.dal.firebase;

import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.models.Model;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
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
    public void getMap(final DataCallback<LinkedHashMap<String, LinkedHashMap<String, List<String>>>> callback) {
        firebase.child(childName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LinkedHashMap<String, LinkedHashMap<String, List<String>>> countries = new LinkedHashMap<String, LinkedHashMap<String, List<String>>>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            countries.put("countries", new LinkedHashMap<String, List<String>>());
                        }
                        callback.onSuccess(countries);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        callback.onError(firebaseError.getMessage());
                    }
                });
    }

    @Override
    public void query(String[] selection, String[] selectionArgs, final DataCallback<List<T>> callback) {
        callback.onSuccess(new ArrayList<T>());
    }



}
