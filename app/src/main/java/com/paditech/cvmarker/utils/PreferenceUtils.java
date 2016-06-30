package com.paditech.cvmarker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.model.OtherInfo;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.model.WorkExperience;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

/**
 * Created by USER on 12/6/2016.
 */
public class PreferenceUtils {

    private final static String PREF_NAME = "Resume";
    private final static String RESUME_LIST = "RESUME_LIST";
    private final static String AVATAR = "AVATAR";
    private final static String PESONAL_IS_DEFAULT = "PESONAL_IS_DEFAULT";
    private final static String DEFAULT_PERSONAL = "DEFAULT_PERSONAL";
    private final static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";

    public static void saveListResume(Context context, List<Resume> resumeList)
    {
        Gson gson = new Gson();
        String rs = gson.toJson(resumeList);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(RESUME_LIST, rs);
        Log.e("RESUME", rs ) ;
        editor.apply();
    }

    public static void saveStyle(Context context, int index, int style) {
        List<Resume> resumeList = PreferenceUtils.getListResume(context);
        resumeList.get(index).setStyle(style);
        PreferenceUtils.saveListResume(context, resumeList);
    }

    public static void saveLanguage(Context context, int index, String language) {
        List<Resume> resumeList = PreferenceUtils.getListResume(context);
        resumeList.get(index).setLanguage(language);
        PreferenceUtils.saveListResume(context, resumeList);
    }

    public static List<Resume> getListResume(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonResumes = preferences.getString(RESUME_LIST, "");
        return getListResume(jsonResumes);
    }

    public static Resume getResume(Context context, int index) {
        Resume resume ;
        resume = getListResume(context).get(index);
        return resume;
    }

    public static List<Resume> getListResume(String gson)
    {
        List<Resume> resumeList;
        Type type = new TypeToken<List<Resume>>() {}.getType();
        resumeList = new Gson().fromJson(gson, type);
        return resumeList;
    }

    public static Personal getPersonal(Context context, int index) {
        List<Resume> list = getListResume(context);
        Resume resume = list.get(index);
        return resume.getPersonal();
    }

    public static void setAvatar(Context context, String avatarPath)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(AVATAR, avatarPath);
        Log.e("AVATAR", avatarPath ) ;
        editor.apply();
    }

    public static String getAvatar(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String rs = preferences.getString(AVATAR, "");
        return rs;
    }

    public static void setPersonalIsDefault(Context context, boolean isDefault)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PESONAL_IS_DEFAULT, isDefault);
        editor.apply();
    }

    public static boolean getPesonalIsDefault(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean rs = preferences.getBoolean(PESONAL_IS_DEFAULT, false);
        return rs;
    }

    public static void setDefaultPersonal(Context context, Personal personal)
    {
        Gson gson = new Gson();
        String rs = gson.toJson(personal);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(DEFAULT_PERSONAL, rs);
        editor.apply();
    }

    public static Personal getDefaultPesonal(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String  rs = preferences.getString(DEFAULT_PERSONAL, "");
        Personal personal;
        Type type = new TypeToken<Personal>() {}.getType();
        personal = new Gson().fromJson(rs, type);
        return personal;
    }

    public static void saveDefaultLanguage(Context context, String lang)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(DEFAULT_LANGUAGE, lang);
        editor.apply();
    }

    public static String getDefaultLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String rs = preferences.getString(DEFAULT_LANGUAGE, Locale.getDefault().getLanguage());
        return rs;
    }

}
