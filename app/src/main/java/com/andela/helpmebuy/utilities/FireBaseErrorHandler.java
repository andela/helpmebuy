package com.andela.helpmebuy.utilities;

import android.content.Context;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.firebase.client.FirebaseError;

public class FireBaseErrorHandler {


    public static String getErrorMessage(FirebaseError firebaseError, Context context) {
        String errorMessage = "";
        switch (firebaseError.getCode()) {
            case FirebaseError.USER_DOES_NOT_EXIST:
                errorMessage = context.getResources().getString(R.string.firebase_user_does_not_exist);
                break;
            case FirebaseError.EMAIL_TAKEN:
                errorMessage = context.getResources().getString(R.string.firebase_email_taken);
                break;
            case FirebaseError.INVALID_EMAIL:
                errorMessage = context.getResources().getString(R.string.firebase_email_invalid);
                break;
            case FirebaseError.INVALID_PASSWORD:
                errorMessage = context.getResources().getString(R.string.firebase_password_invalid);
                break;
            case FirebaseError.INVALID_CREDENTIALS:
                errorMessage = context.getResources().getString(R.string.firebase_credentials_invalid);
                break;
            case FirebaseError.NETWORK_ERROR:
                errorMessage = context.getResources().getString(R.string.firebase_network_error);
                break;
            case FirebaseError.DISCONNECTED:
                errorMessage = context.getResources().getString(R.string.firebase_network_disconnected);
                break;
            default:
                errorMessage = context.getResources().getString(R.string.firebase_unknown_error);
        }
        return errorMessage;
    }

}
