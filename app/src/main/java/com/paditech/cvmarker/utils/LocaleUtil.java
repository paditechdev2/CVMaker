package com.paditech.cvmarker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

/**
 * Created by USER on 14/6/2016.
 */
public class LocaleUtil {


    public static void setLanguage(Context context, String type) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(type);
        res.updateConfiguration(conf, dm);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void updateDefaultLanguage(Context context)
    {
        String defaultLang = PreferenceUtils.getDefaultLanguage(context);
        setLanguage(context, defaultLang);

    }

    public static float getWidthScreen(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }
}
