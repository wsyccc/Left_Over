package com.bcit.Leftovers.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.bcit.Leftovers.activity.MainActivity;

import org.json.JSONObject;

/**
 * Created by Siyuan on 2016/11/13.
 */

public class Login {

    public static int loginStatus = 0;
    private Context context = null;
    private String userName = null;
    public static String email = null;
    public static boolean flag = false;

    public Login(String email, Context ctx) {
        this.context = ctx;
        String json = "email=" + email + "&collection=usersInfo"
                + "&action=find";
        MongoDB mongoDB = new MongoDB(ctx);
        String result;
        try {
            result = mongoDB.execute(json).get();
            if (result.equalsIgnoreCase("null")) {
                Log.e(Login.class.getName(), "Cannot find the user");
            } else {
                JSONObject jsonObject = new JSONObject(result);
                userName = jsonObject.getString("username");
                this.email = jsonObject.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login() {
        if (userName != null) {
            String CuserName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
            SaveSharedPreference.setUser(this.email, CuserName, context);
            MainActivity.userName = SaveSharedPreference.getUser(context, "userName");
            MainActivity.email = SaveSharedPreference.getUser(context, "email");
            Log.d(getClass().getName() + "email!", SaveSharedPreference.getUser(context, "email"));
            Log.d(getClass().getName() + "username!!!!!!!!!", SaveSharedPreference.getUser(context, "userName"));
            MainActivity.txtName.setText(MainActivity.userName);
            MainActivity.txtWebsite.setText(MainActivity.email);
            Log.d(getClass().getName(), MainActivity.userName);
            Log.d(getClass().getName(), MainActivity.email);
            if (loginStatus == 0) {
                String json = "email=" + email + "&collection=usersInfo"
                        + "&action=updateOne" + "&which=login" + "&to=1";
                MongoDB mongoDB = new MongoDB(context);
                String result;
                try {
                    result = mongoDB.execute(json).get();
                    if (result.equalsIgnoreCase("null")) {
                        Log.e(Login.class.getName(), "Cannot update login");
                        return false;
                    } else {
                        loginStatus = 1;
                        flag = true;
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                flag = true;
                return true;
            }
            //for the future
        } else {
            return false;
        }
    }
}
