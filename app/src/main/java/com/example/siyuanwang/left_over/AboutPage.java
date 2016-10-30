package com.example.siyuanwang.left_over;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }

    public void goBack (View v){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}
