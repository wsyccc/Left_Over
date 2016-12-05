package com.bcit.Leftovers.other;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Siyuan on 2016/11/22.
 */

public class Recipe implements Serializable {

    private int recipeID;
    private String recipeName;
    private String mainImage;
    private List<StepsBean> steps = new ArrayList<>();
    private String description;
    private List<CommentBean> comment = new ArrayList<>();
    private int hotness;
    private List<DietTypeBean> dietType = new ArrayList<>();
    private int difficulty;
    private List<MealTypeBean> mealType = new ArrayList<>();
    private List<IngredientsBean> ingredients = new ArrayList<>();
    private List<IngredientDescriptionBean> ingredientDescription = new ArrayList<>();

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
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public void setComment(List<CommentBean> comment){
        this.comment = comment;
    }
    public List<CommentBean> getComment(){
        return comment;
    }
    public void setHotness(int hotness){
        this.hotness = hotness;
    }
    public int getHotness(){
        return hotness;
    }
    public void setDietType(List<DietTypeBean> dietType){
        this.dietType = dietType;
    }
    public List<DietTypeBean> getDietType(){
        return dietType;
    }
    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public void setMealType(List<MealTypeBean> mealType){
        this.mealType = mealType;
    }
    public List<MealTypeBean> getMealType(){
        return mealType;
    }
    public void setIngredients(List<IngredientsBean> ingredients){
        this.ingredients = ingredients;
    }
    public List<IngredientsBean> getIngredients(){
        return ingredients;
    }
    public void setIngredientDescription(List<IngredientDescriptionBean> ingredientDescription){
        this.ingredientDescription = ingredientDescription;
    }
    public List<IngredientDescriptionBean> getIngredientDescription(){
        return ingredientDescription;
    }



    public static class StepsBean implements Serializable{
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
    public static class CommentBean implements Serializable{
        @Expose
        private String email;
        @Expose
        private String content;

        public void setEmail(String email){
            this.email = email;
        }

        public String getEmail(){
            return email;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return content;
        }
    }
    public static class DietTypeBean implements Serializable{
        @Expose
        private String diet;

        public void setDiet(String diet){
            this.diet = diet;
        }
        public String getDiet(){
            return diet;
        }
    }
    public static class MealTypeBean implements Serializable{
        @Expose
        private String type;

        public void setType(String type){
            this.type = type;
        }

        public String getType(){
            return type;
        }
    }
    public static class IngredientsBean implements Serializable{
        @Expose
        private String ingredient;

        public void setIngredient(String ingredient){
            this.ingredient = ingredient;
        }

        public String getIngredient(){
            return ingredient;
        }
    }
    public static class IngredientDescriptionBean implements Serializable{
        @Expose
        private String ingredientDescription;

        public void setIngredientDescription(String ingredientDescription){
            this.ingredientDescription = ingredientDescription;
        }

        public String getIngredientDescription(){
            return ingredientDescription;
        }
    }
}
