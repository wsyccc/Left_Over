package com.bcit.Leftovers.other;

import android.content.Context;
import android.util.Log;

import com.bcit.Leftovers.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import static com.bcit.Leftovers.other.Login.loginStatus;

/**
 * Created by Siyuan on 2016/11/13.
 */

public class Logout {
    private Context context;
    private String email;

    public Logout(String email, Context ctx){
        this.context = ctx;
        this.email = email;
        String json = "email=" + email + "&collection=usersInfo"
                + "&action=find";
        MongoDB mongoDB = new MongoDB(ctx);
        String result;
        try {
            result = mongoDB.execute(json).get();
            if (result.equalsIgnoreCase("null")) {
                Log.e(Logout.class.getName(), "Cannot find the user");
            }else{
                JSONObject jsonObject = new JSONObject(result);
                loginStatus = jsonObject.getInt("login");
                Log.d("fsf",loginStatus+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean logout() {
        if (SaveSharedPreference.getUser(context,"email").length() != 0 && Login.email != null) {
            SaveSharedPreference.clear();
            MainActivity.userName = "King";
            MainActivity.txtName.setText(MainActivity.userName);
            MainActivity.email = "leftover@bcit.ca";
            MainActivity.txtWebsite.setText(MainActivity.email);
            MainActivity.urlProfileImg = "https://static.mengniang.org/common/thumb/a/a2/59205988_p0.jpg/250px-59205988_p0.jpg";
            Glide.with(context).load(MainActivity.urlProfileImg)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(MainActivity.imgProfile);
            String json = "email=" + email + "&collection=usersInfo"
                    + "&action=updateOne" + "&which=login" + "&to=0";
            MongoDB mongoDB = new MongoDB(context);
            String result;
            try {
                result = mongoDB.execute(json).get();
                if (result.equalsIgnoreCase("null")) {
                    Log.e(Login.class.getName(), "Cannot log out");
                    return false;
                }else{
                    loginStatus = 0;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            //for the future
        } else {
            return false;
        }

    }
}
