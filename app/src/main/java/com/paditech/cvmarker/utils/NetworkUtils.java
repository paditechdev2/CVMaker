package com.paditech.cvmarker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.paditech.cvmarker.R;

/**
 * Created by USER on 17/6/2016.
 */
public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        DialogUtil.showErrorDialog(context, context.getResources().getString(R.string.no_network));
        return false;
    }
}
