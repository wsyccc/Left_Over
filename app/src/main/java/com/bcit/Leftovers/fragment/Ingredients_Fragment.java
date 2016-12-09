package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcit.Leftovers.R;

import com.bcit.Leftovers.activity.Ingredients_Activity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand.REGULAR;
import static com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand.WARNING;


public class Ingredients_Fragment extends Fragment {


    private View view;
    private static final int[] meatId = {R.id.beef_image, R.id.chicken_image, R.id.fish_image, R.id.pork_image,
            R.id.shrimp_image, R.id.turkey_image, R.id.tofu_image, R.id.egg_image};
    private static final int[] vegeId = {R.id.asparagus_image, R.id.bellpeppers_image, R.id.bokchoy_image, R.id.broccoli_image,
            R.id.cabbage_image, R.id.carrots_image, R.id.cauliflower_image, R.id.chillipepper_image,
            R.id.cucumbers_image, R.id.jalapeno_image,  R.id.spinach_image,  R.id.zucchini_image,
            R.id.lettuce_image, R.id.tomato_image, R.id.mushrooms_image, R.id.peas_image};
    private static final int[] grainId = {R.id.almond_image, R.id.bread_image, R.id.noodle_image, R.id.pasta_image,
            R.id.peanuts_image, R.id.rice_image, R.id.corn_image, R.id.potato_image};
    private static final int[] dairyId = {R.id.butter_image, R.id.cheese_image, R.id.egg_image, R.id.milk_image, R.id.yogurt_image};
    private static final int[] fruitId = {R.id.apple_image, R.id.avocado_image, R.id.banana_image, R.id.blueberry_image,
            R.id.orange_image, R.id.mango_image, R.id.peach_image, R.id.grape_image,
            R.id.pineapple_image, R.id.strawberry_image, R.id.watermelon_image, R.id.lime_image};

    private static final int[] meatImage = {R.drawable.beef, R.drawable.chicken, R.drawable.fish, R.drawable.pork,
            R.drawable.shrimp, R.drawable.turkey, R.drawable.tofu, R.drawable.egg};
    private static final int[] vegeImage = {R.drawable.asparagus, R.drawable.bellpeppers, R.drawable.bokchoy, R.drawable.broccoli,
            R.drawable.cabbage, R.drawable.carrots, R.drawable.cauliflower, R.drawable.chillipepper,
            R.drawable.cucumbers, R.drawable.jalapeno, R.drawable.spinach, R.drawable.zucchini,
            R.drawable.lettuce, R.drawable.tomato, R.drawable.mushrooms, R.drawable.peas};
    private static final int[] grainImage = {R.drawable.almond, R.drawable.bread, R.drawable.noodle, R.drawable.pasta,
            R.drawable.peanuts, R.drawable.rice, R.drawable.corn, R.drawable.potato};
    private static final int[] dairyImage = {R.drawable.butter, R.drawable.cheese, R.drawable.egg, R.drawable.milk, R.drawable.yogurt};
    private static final int[] fruitImage = {R.drawable.apple, R.drawable.avocado, R.drawable.banana, R.drawable.blueberry,
            R.drawable.orange, R.drawable.mango, R.drawable.peach, R.drawable.grape,
            R.drawable.pineapple, R.drawable.strawberry, R.drawable.watermelon, R.drawable.lime};

    private List<Integer> choices;
    private List<BootstrapCircleThumbnail> buttons;

    public Ingredients_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        choices = new ArrayList<>();
        buttons = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        init();
        setOnClickLisetener();
        BootstrapButton next = (BootstrapButton) view.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Ingredients_Activity.class);
                intent.putExtra("options", (Serializable) choices);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        setOnClickLisetener();
    }

    public void onPause() {
        super.onPause();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void init() {
        for (int i = 0; i < meatId.length; i++) {
            BootstrapCircleThumbnail thumbnail = (BootstrapCircleThumbnail) view.findViewById(meatId[i]);
            Glide.with(getActivity())
                    .load(meatImage[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(thumbnail);
            buttons.add(thumbnail);
        }

        for (int i = 0; i < vegeId.length; i++) {
            BootstrapCircleThumbnail thumbnail = (BootstrapCircleThumbnail) view.findViewById(vegeId[i]);
            Glide.with(getActivity())
                    .load(vegeImage[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(thumbnail);
            buttons.add(thumbnail);
        }
        for (int i = 0; i < grainId.length; i++) {
            BootstrapCircleThumbnail thumbnail = (BootstrapCircleThumbnail) view.findViewById(grainId[i]);
            Glide.with(getActivity())
                    .load(grainImage[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(thumbnail);
            buttons.add(thumbnail);
        }
        for (int i = 0; i < dairyId.length; i++) {
            BootstrapCircleThumbnail thumbnail = (BootstrapCircleThumbnail) view.findViewById(dairyId[i]);
            Glide.with(getActivity())
                    .load(dairyImage[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(thumbnail);
            buttons.add(thumbnail);
        }
        for (int i = 0; i < fruitId.length; i++) {
            BootstrapCircleThumbnail thumbnail = (BootstrapCircleThumbnail) view.findViewById(fruitId[i]);
            Glide.with(getActivity())
                    .load(fruitImage[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(thumbnail);
            buttons.add(thumbnail);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnClickLisetener() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BootstrapCircleThumbnail b = (BootstrapCircleThumbnail) v;
                    if (b.getBootstrapBrand().equals(DefaultBootstrapBrand.WARNING)) {
                        b.setBootstrapBrand(REGULAR);
                        if (choices != null) {
                            for (int k = 0; k < choices.size(); k++) {
                                if (choices.get(k) == v.getId()) {
                                    choices.remove(k);
                                    break;
                                }
                            }
                        }
                    } else {
                        b.setBootstrapBrand(WARNING);
                        choices.add(v.getId());
                    }
                }
            });
        }
    }
}
