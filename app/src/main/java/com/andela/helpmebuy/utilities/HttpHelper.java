package com.andela.helpmebuy.utilities;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class HttpHelper implements JsonCallback {

    private Context context;

    public HttpHelper(Context context) {
        this.context = context;
    }

    @Override
    public void getJson(String url, final HttpCallback httpCallback) {
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            httpCallback.onError(e.getMessage());
                        } else {
                            String formattedAddress = result
                                    .getAsJsonArray("results")
                                    .get(0)
                                    .getAsJsonObject()
                                    .get("formatted_address")
                                    .getAsString();
                            httpCallback.onSuccess(formattedAddress);
                        }
                    }
                });
    }
}
