package com.bcit.Leftovers.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bcit.Leftovers.R;

public class SpiceLevel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_level);
    }

    public void chooseSpice(View view) {
        Intent i = new Intent(this, SpiceLevel.class);

        Button b = (Button) view;
        String diet = b.getText().toString();

        i.putExtra("diet", diet);
        startActivity(i);
    }
}