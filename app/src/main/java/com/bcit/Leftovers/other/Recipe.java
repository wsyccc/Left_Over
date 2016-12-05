package com.bcit.Leftovers.other;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Siyuan on 2016/11/22.
 */

public class Recipe {

    private int recipeID;
    private String mainImage;
    private String recipeName;

    private List<StepsBean> steps = new ArrayList<>();

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }
    public String getRecipeName(){
        return recipeName;
    }
    public void setRecipeName(String recipeName){
        this.recipeName = recipeName;
    }
    public List<StepsBean> getSteps(){
        return steps;
    }
    public void setSteps(List<StepsBean> steps){
        this.steps = steps;
    }

    public static class StepsBean{
        @Expose
        private int stepNo;
        @Expose
        private String stepImage;
        @Expose
        private String stepInstruction;

        public void setStepNo(int stepNo){
            this.stepNo = stepNo;
        }
        public int getStepNo(){
            return stepNo;
        }
        public void setStepImage(String stepImage){
            this.stepImage = stepImage;
        }
        public String getStepImage(){
            return stepImage;
        }
        public void setStepInstruction(String stepInstruction){
            this.stepInstruction = stepInstruction;
        }
        public String getStepInstruction(){
            return stepInstruction;
        }

    }


}
