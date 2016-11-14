package com.bcit.Leftovers.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bcit.Leftovers.R;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                if (mWifi == null){
                    Toast.makeText(Splash.this, "This application requires internet connect. Please connect to the internet and try again.",Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}