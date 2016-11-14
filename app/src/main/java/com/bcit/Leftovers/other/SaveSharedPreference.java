package com.bcit.Leftovers.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


/**
 * Created by Siyuan on 2016/11/13.
 */

public class SaveSharedPreference {
    private static final String PREF_USER = "user";
    private static SharedPreferences.Editor editor;
    private static Set<String> set = new HashSet<>();
    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void addUser(String email, String username){
        set.add(email);
        set.add(username);
    }
    public static void setUser(Context ctx){
        editor = getSharedPreferences(ctx).edit();
        editor.putStringSet(PREF_USER, set);
        editor.commit();
    }
    public static Set getUser(Context ctx){
        return getSharedPreferences(ctx).getStringSet(PREF_USER, new HashSet<String>());
    }
    public static String getEmail(Context ctx){
        Set result = getUser(ctx);
        String email = null;
        for (Object s : result){
            email =  s.toString();
            break;
        }
        return email;
    }
    public static String getUserName(Context ctx){
        Set result = getUser(ctx);
        String username = null;
        for (Object o : result){
            username = o.toString();
        }
        //Log.d(SaveSharedPreference.class.getName(), username);
        return username;
    }
    public static void clear(){
        editor.clear();
        editor.commit();
    }
}
