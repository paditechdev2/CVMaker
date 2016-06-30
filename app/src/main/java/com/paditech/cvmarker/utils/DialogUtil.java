package com.paditech.cvmarker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.paditech.cvmarker.R;


/**
 * Created by USER on 15/6/2016.
 */
public class DialogUtil {

    public static void showErrorDialog(Context context, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
