package com.andela.helpmebuy.utilities;

import android.content.Context;
import android.content.Intent;

public class Launcher {
    public static void launchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
