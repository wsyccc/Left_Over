package com.bcit.Leftovers.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.bcit.Leftovers.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

/**
 * Created by Siyuan on 2016/11/13.
 */

public class Login {

    public static int loginStatus = 0;
    private Context context = null;
    private String userName = null;
    public static String email = null;
    private String imageUrl = null;

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
                imageUrl = jsonObject.getString("avatar");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login() {
        if (userName != null) {
            String CuserName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
            SaveSharedPreference.setUser(this.email, CuserName, imageUrl, context);
            MainActivity.userName = SaveSharedPreference.getUser(context, "userName");
            MainActivity.email = SaveSharedPreference.getUser(context, "email");
            if (imageUrl.length() >= 64){
                MainActivity.urlProfileImg = SaveSharedPreference.getUser(context, "avatarUrl");
            }
            Log.d(getClass().getName() + "email!", SaveSharedPreference.getUser(context, "email"));
            Log.d(getClass().getName() + "username!!!!!!!!!", SaveSharedPreference.getUser(context, "userName"));
            MainActivity.txtName.setText(MainActivity.userName);
            MainActivity.txtWebsite.setText(MainActivity.email);
            Glide.with(context).load(MainActivity.urlProfileImg)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(MainActivity.imgProfile);
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
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return true;
            }
            //for the future
        } else {
            return false;
        }
    }
}
