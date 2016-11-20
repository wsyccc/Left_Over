package com.bcit.Leftovers.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Siyuan on 2016/11/13.
 */

public class SaveSharedPreference {
    private static SharedPreferences.Editor editor;
    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void setUser(String email, String userName, String avatarUrl, Context ctx){
        editor = getSharedPreferences(ctx).edit();
        clear();
        editor.putString("email", email);
        editor.putString("userName", userName);
        editor.putString("avatarUrl", avatarUrl);
        editor.commit();
    }
    public static String getUser(Context ctx, String what){
        try{
            return getSharedPreferences(ctx).getString(what, null);
        }catch (Exception e){
            return null;
        }
    }

    public static void clear(){
        editor.clear();
        editor.commit();
    }
}
