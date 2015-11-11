package com.andela.helpmebuy.authentication;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Daniel James on 10/21/2015.
 */
public class ErrorHandler {

    private String message;

    private FirebaseError firebaseError;

    public ErrorHandler() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void custommessage() {
        switch (firebaseError.getCode()){
            case FirebaseError.EMAIL_TAKEN:
                setMessage("This email is already in use");
                break;
            case FirebaseError.NETWORK_ERROR:
                setMessage("There is a problem with the network");
                break;
            case FirebaseError.USER_DOES_NOT_EXIST:
                setMessage("This user does not exist");

        }
    }
}
