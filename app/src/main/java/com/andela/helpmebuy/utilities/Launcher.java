package com.andela.helpmebuy.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Launcher {
    public static final String TRANSPORT = "transport";

    public static void launchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void launchActivity(Context context, Bundle bundle,  Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(TRANSPORT, bundle);
        context.startActivity(intent);
    }
}
