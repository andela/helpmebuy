package com.andela.helpmebuy.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.andela.helpmebuy.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CurrentUser {

    private final static String CURRENT_USER_KEY = "currentUserKey";

    public static void save(User user, Context context) {
        String value = toJson(user);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(CURRENT_USER_KEY, value);

        editor.commit();
    }

    public static User get(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return toUser(preferences.getString(CURRENT_USER_KEY, ""));
    }

    private static String toJson(User user){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(user);
        } catch(JsonProcessingException e){
            return "";
        }
    }

    private static User toUser(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, User.class);
        } catch(IOException e) {
            return null;
        }
    }
}
