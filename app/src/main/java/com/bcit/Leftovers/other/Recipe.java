package com.bcit.Leftovers.other;

/**
 * Created by Siyuan on 2016/11/22.
 */

public class Recipe {

    private int recipeID;
    private String mainImage;

//    public Recipe(int recipeID, String mainImage){
//        this.recipeID = recipeID;
//        this.mainImage = mainImage;
//    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setID(int recipeID) {
        this.recipeID = recipeID;
    }


}
