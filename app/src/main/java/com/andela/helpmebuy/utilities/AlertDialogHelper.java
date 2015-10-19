package com.andela.helpmebuy.utilities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.andela.helpmebuy.activities.ChangePasswordActivity;
import com.andela.helpmebuy.R;

public class AlertDialogHelper {

    public static Dialog createDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final Intent intent = new Intent(context, ChangePasswordActivity.class);

        builder.setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(intent);
            }
        });

        builder.setTitle(R.string.alert_dialog_title);
        builder.setMessage(R.string.alert_dialog_message);

        return builder.create();
    }
}
