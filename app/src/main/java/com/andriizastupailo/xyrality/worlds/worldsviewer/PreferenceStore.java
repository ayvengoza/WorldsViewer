package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * class for simply access to preferences
 */

public class PreferenceStore {
    private static final String DEFAULT_STRING = "";
    private static final String LOGIN_KEY = "login";
    private static final String PASSORD_KEY = "password";
    private static final String DEVICE_TYPE_KEY = "device_type";
    private static final String DEVICE_ID_KEY = "device_id";

    public static String getLogin(Context context){
        return getString(context, LOGIN_KEY);
    }

    public static String getPassword(Context context){
        return getString(context, PASSORD_KEY);
    }

    public static String getDeviceType(Context context){
        return getString(context, DEVICE_TYPE_KEY);
    }

    public static String getDeviceId(Context context){
        return getString(context, DEVICE_ID_KEY);
    }

    public static void setLogin(Context context, String login){
        setString(context, LOGIN_KEY, login);
    }

    public static void setPassword(Context context, String password){
        setString(context, PASSORD_KEY, password);
    }

    public static void setDeviceType(Context context, String deviceType){
        setString(context, DEVICE_TYPE_KEY, deviceType);
    }

    public static void setDeviceId(Context context, String deviceId){
        setString(context, DEVICE_ID_KEY, deviceId);
    }

    public static void cleanUser(Context context){
        setString(context, LOGIN_KEY, DEFAULT_STRING);
        setString(context, PASSORD_KEY, DEFAULT_STRING);
    }

    public static boolean isUserSet(Context context){
        if(getLogin(context).equals(DEFAULT_STRING) ||
                getPassword(context).equals(DEFAULT_STRING))
            return false;
        return true;
    }

    private static String getString(Context context, String key){
        SharedPreferences sp =
                context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        return sp.getString(key, DEFAULT_STRING);
    }

    private static void setString(Context context, String key, String value){
        SharedPreferences sp =
                context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
