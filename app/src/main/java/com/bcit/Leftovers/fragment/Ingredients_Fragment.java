package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bcit.Leftovers.R;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;

import static com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand.REGULAR;
import static com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand.SUCCESS;



public class Ingredients_Fragment extends Fragment {



    private static final String[] ids = 
    public Ingredients_Fragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ingredients, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    public void init(){

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void chooseIngredient(View v) {
        BootstrapCircleThumbnail b = (BootstrapCircleThumbnail) v;
        BootstrapBrand i = b.getBootstrapBrand();
        if (i.equals(DefaultBootstrapBrand.SUCCESS)) {
            b.setBootstrapBrand(REGULAR);
        } else {
            b.setBootstrapBrand(SUCCESS);
        }

    }
    public void nextStep(View view){

    }

}
