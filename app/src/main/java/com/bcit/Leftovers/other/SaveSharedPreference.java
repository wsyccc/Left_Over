package com.bcit.Leftovers.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Siyuan on 2016/11/13.
 */

public class SaveSharedPreference {
    static final String PREF_USER = "user";
    static Set<String> user = new HashSet();
    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void addUser(String email, String userName){
        user.add(email);
        user.add(userName);
    }
    public static void setUser(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putStringSet(PREF_USER, user);
        editor.commit();
    }
    public static Set<String> getUser(Context ctx){
        return getSharedPreferences(ctx).getStringSet(PREF_USER, new HashSet<String>());
    }
}
