package com.andela.helpmebuy.utilities;


import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Contact;

import java.util.List;

public class ContactsHelper {

    private ContactsListener listener;

    ListCallback<Contact> listCallback;

    public ContactsHelper(ContactsListener listener) {
        this.listener = listener;
    }

    public void createContact(Contact contact, String targetId) {
        getCollection(targetId).save(contact, getSaveCallback());
    }

    public void getAllContacts(String userId, ListCallback<Contact> listCallback) {
        this.listCallback = listCallback;
        getCollection(userId).getAll(getFetchCallback());
    }

    private DataCallback<List<Contact>> getFetchCallback() {
        return new DataCallback<List<Contact>>() {
            @Override
            public void onSuccess(List<Contact> data) {
                listCallback.onGetList(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        };
    }

    private FirebaseCollection<Contact> getCollection(String targetId) {
        return new FirebaseCollection<>(getUrl(targetId), Contact.class);
    }

    private DataCallback<Contact> getSaveCallback() {
        return new DataCallback<Contact>() {
            @Override
            public void onSuccess(Contact data) {
                listener.onContactCreated(data);
            }

            @Override
            public void onError(String errorMessage) {
                listener.onError(errorMessage);
            }
        };
    }

    private String getUrl(String targetId) {
        return Constants.CONTACTS + "/" + targetId;
    }
}
