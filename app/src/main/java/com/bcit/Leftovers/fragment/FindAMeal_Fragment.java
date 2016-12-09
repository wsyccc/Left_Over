package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.activity.Find_Meal_Activity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;
import com.ldoublem.thumbUplib.ThumbUpView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FindAMeal_Fragment extends Fragment {

    private BootstrapButton breakfast;
    private BootstrapButton lunch;
    private BootstrapButton dinner;
    private static View view;
    public static Map<String, List> choices;
    private ArrayList<String> mealType;
    private DiscreteSlider spice_Level;
    private DiscreteSlider difficulty;
    private ThumbUpView vegan;
    private TextView vegan_text;
    private RelativeLayout tickMarkLabelsRelativeLayout;
    private RelativeLayout difficulty_labels;
    private int spice_level_int = 1;
    private int difficulty_int = 1;
    private boolean vegan_bool = false;

    public FindAMeal_Fragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choices = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.fragment_find_a_meal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setMealType();
        setSpiceLevel();
        setDifficulty();
        setVegan();
        BootstrapButton next = (BootstrapButton) view.findViewById(R.id.give_recipe);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> spice = new ArrayList<>();
                ArrayList<Integer> difficultly = new ArrayList<>();
                ArrayList<Boolean> vegan = new ArrayList<>();
                spice.add(spice_level_int);
                difficultly.add(difficulty_int);
                vegan.add(vegan_bool);
                choices.put("mealType", mealType);
                choices.put("hotness", spice);
                choices.put("difficulty", difficultly);
                choices.put("vegan", vegan);
                Intent intent = new Intent(getActivity(), Find_Meal_Activity.class);
                intent.putExtra("choices", (Serializable) choices);
                for (String s : mealType){
                    Log.d("mealType", s);
                }
                Log.d("hotness", spice.get(0)+"");
                Log.d("difficulty", difficultly.get(0)+"");
                Log.d("vegan", vegan.get(0)+"");
                startActivity(intent);
            }
        });


    }
    public void setMealType(){
        mealType = new ArrayList<>();
        breakfast = (BootstrapButton) view.findViewById(R.id.breakfast);
        lunch = (BootstrapButton) view.findViewById(R.id.lunch);
        dinner = (BootstrapButton) view.findViewById(R.id.dinner);
        breakfast.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                if (isChecked) {
//                    //remove later
//                    lunch.setSelected(false);
//                    dinner.setSelected(false);

                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("breakfast")){
                            mealType.remove(i);
                        }
                    }
                    mealType.add("breakfast");
                } else {
                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("breakfast")){
                            mealType.remove(i);
                        }
                    }
                }
            }
        });
        lunch.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                if (isChecked) {
//                    //remove later
//                    breakfast.setSelected(false);
//                    dinner.setSelected(false);

                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("lunch")){
                            mealType.remove(i);
                        }
                    }
                    mealType.add("lunch");
                } else {
                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("lunch")){
                            mealType.remove(i);
                        }
                    }
                }
            }
        });
        dinner.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                if (isChecked) {
//                    //remove later
//                    lunch.setSelected(false);
//                    breakfast.setSelected(false);

                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("dinner")){
                            mealType.remove(i);
                        }
                    }
                    mealType.add("dinner");
                }else {
                    for (int i = 0; i < mealType.size(); i++){
                        if (mealType.get(i).equals("dinner")){
                            mealType.remove(i);
                        }
                    }
                }
            }
        });
    }
    public void setSpiceLevel(){
        tickMarkLabelsRelativeLayout = (RelativeLayout) view.findViewById(R.id.spice_Level_labels);
        spice_Level = (DiscreteSlider) view.findViewById(R.id.spice_Level);
        spice_Level.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                int childCount = tickMarkLabelsRelativeLayout.getChildCount();
                spice_level_int = position;
                for(int i= 0; i<childCount; i++){
                    TextView tv = (TextView) tickMarkLabelsRelativeLayout.getChildAt(i);
                    if(i == position)
                        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                    else
                        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey_400));
                }
            }

        });
        tickMarkLabelsRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tickMarkLabelsRelativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                addTickMarkTextLabels(spice_Level,tickMarkLabelsRelativeLayout);
            }
        });
    }

    public void setDifficulty(){
        difficulty_labels = (RelativeLayout) view.findViewById(R.id.difficulty_labels);
        difficulty = (DiscreteSlider) view.findViewById(R.id.difficulty);
        difficulty.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                int childCount = difficulty_labels.getChildCount();
                difficulty_int = position;
                for(int i= 0; i<childCount; i++){
                    TextView tv = (TextView) difficulty_labels.getChildAt(i);
                    if(i == position)
                        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                    else
                        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey_400));
                }
            }

        });
        difficulty_labels.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                difficulty_labels.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                addTickMarkTextLabels(difficulty,difficulty_labels);
            }
        });
    }
    public void setVegan(){
        vegan = (ThumbUpView) view.findViewById(R.id.vegan);
        vegan_text = (TextView) view.findViewById(R.id.vegan_text);
        vegan.setUnLikeType(ThumbUpView.LikeType.broken);
        vegan.setOnThumbUp(new ThumbUpView.OnThumbUp(){
            @Override
            public void like(boolean like){
                if (like) {
                    vegan_text.setText("I'm Vegan!");
                    vegan_bool = true;
                } else {
                    vegan_text.setText("I like meat!");
                    vegan_bool = false;
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public void onResume() {
        super.onResume();
        breakfast.setSelected(false);
        lunch.setSelected(false);
        dinner.setSelected(false);
        spice_Level.setPosition(1);
        difficulty.setPosition(1);
        vegan_text.setText("Vegetarian");
        vegan.UnLike();
        vegan.stopAnim();
    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }

    private void addTickMarkTextLabels(DiscreteSlider slider, RelativeLayout layout){
        int tickMarkCount = slider.getTickMarkCount();
        float tickMarkRadius = slider.getTickMarkRadius();
        int width = layout.getMeasuredWidth();

        int discreteSliderBackdropLeftMargin = DisplayUtility.dp2px(getContext(), 32);
        int discreteSliderBackdropRightMargin = DisplayUtility.dp2px(getContext(), 32);
        float firstTickMarkRadius = tickMarkRadius;
        float lastTickMarkRadius = tickMarkRadius;
        int interval = (width - (discreteSliderBackdropLeftMargin+discreteSliderBackdropRightMargin) - ((int)(firstTickMarkRadius+lastTickMarkRadius)) )
                / (tickMarkCount-1);

        String[] tickMarkLabels = {"0", "1", "2", "3", "4"};
        int tickMarkLabelWidth = DisplayUtility.dp2px(getContext(), 40);

        for(int i=0; i<tickMarkCount; i++) {
            TextView tv = new TextView(getContext());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    tickMarkLabelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tv.setText(tickMarkLabels[i]);
            tv.setGravity(Gravity.CENTER);
            if(i==slider.getPosition())
                tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            else
                tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey_400));

            int left = discreteSliderBackdropLeftMargin + (int)firstTickMarkRadius + (i * interval) - (tickMarkLabelWidth/2);

            layoutParams.setMargins(left,
                    0,
                    0,
                    0);
            tv.setLayoutParams(layoutParams);

            layout.addView(tv);
        }
    }
}
