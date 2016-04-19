package com.andela.helpmebuy.utilities;

import com.andela.helpmebuy.models.Contact;

public interface ContactsListener {
    void onContactCreated(Contact contact);
    void onError(String message);
}
