package com.bcit.Leftovers.activity;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bcit.Leftovers.R;
import com.bcit.Leftovers.fragment.CommentDialog;
import com.bcit.Leftovers.other.CommentListAdapter;
import com.bcit.Leftovers.other.Login;
import com.bcit.Leftovers.other.Recipe;
import com.bcit.Leftovers.other.SnackbarUtil;
import com.bumptech.glide.Glide;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private Recipe recipe;
    private CollapsingToolbarLayoutState state;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static ListView listView;
    private static CommentListAdapter adapter;
    private TextView titleText;
    private ImageView titleImage;
    private AppBarLayout app_bar;
    private String stepsStr;
    private String dietTypeStr;
    private String mealTypeStr;
    private String ingredientsStr;
    private String ingredientDescriptionStr;
    private List<Recipe.CommentBean> comment;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Login.loginStatus == 1 && adapter != null && listView != null) {
                    CommentDialog commentDialog = new CommentDialog(recipe.getRecipeID());
                    commentDialog.show(getFragmentManager(), "Comment");
                    adapter.notifyDataSetChanged();
//                    listView.invalidate();
                    listView.setAdapter(adapter);

                } else {
                    SnackbarUtil.ShortSnackbar(collapsingToolbarLayout, "Please Sign up!", SnackbarUtil.Alert).show();
                }
            }
        });
        init();

    }

    public void init() {
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        comment = recipe.getComment();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        titleImage = (ImageView) findViewById(R.id.title_image);
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(recipe.getRecipeName());
        Glide.with(this)
                .load(recipe.getMainImage())
                .into(titleImage);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        titleText.setVisibility(View.GONE);
                        collapsingToolbarLayout.setTitle(recipe.getRecipeName());//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbarLayout.setTitle("");//设置title不显示
                        titleText.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            titleText.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        collapsingToolbarLayout.setTitle(recipe.getRecipeName());//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        expandRecipe();
        TextView description = (TextView) findViewById(R.id.description);
        TextView dietType = (TextView) findViewById(R.id.dietType);
        TextView mealType = (TextView) findViewById(R.id.mealType);
        TextView ingredients = (TextView) findViewById(R.id.ingredients);
        TextView ingredientsDescription = (TextView) findViewById(R.id.ingredients_description);
        TextView steps = (TextView) findViewById(R.id.steps);
        ImageView hotness = (ImageView) findViewById(R.id.hotness);
        ImageView difficulty = (ImageView) findViewById(R.id.difficulty_image);
        int difficultyImage;
        int hotnessImage;
        description.setText(recipe.getDescription());
        switch (recipe.getHotness()){
            case 0:
                hotnessImage = R.drawable.btn_spiciness_lvl_1;
                break;
            case 1:
                hotnessImage = R.drawable.btn_spiciness_lvl_2;
                break;
            case 2:
                hotnessImage = R.drawable.btn_spiciness_lvl_3;
                break;
            case 3:
                hotnessImage = R.drawable.btn_spiciness_lvl_4;
                break;
            case 4:
                hotnessImage = R.drawable.btn_spiciness_lvl_5;
                break;
            default:
                hotnessImage = R.drawable.btn_spiciness_lvl_1;
                break;
        }
        switch (recipe.getDifficulty()){
            case 0:
                difficultyImage = R.drawable.btn_difficulty_lvl_1;
                break;
            case 1:
                difficultyImage = R.drawable.btn_difficulty_lvl_2;
                break;
            case 2:
                difficultyImage = R.drawable.btn_difficulty_lvl_3;
                break;
            case 3:
                difficultyImage = R.drawable.btn_difficulty_lvl_4;
                break;
            case 4:
                difficultyImage = R.drawable.btn_difficulty_lvl_5;
                break;
            default:
                difficultyImage = R.drawable.btn_difficulty_lvl_1;
                break;
        }
        Glide.with(this)
                .load(difficultyImage)
                .into(difficulty);
        Glide.with(this)
                .load(hotnessImage)
                .into(hotness);
        dietType.setText(Html.fromHtml(dietTypeStr.replaceAll("null", "")));
        mealType.setText(Html.fromHtml(mealTypeStr.replaceAll("null", "")));
        ingredients.setText(Html.fromHtml(ingredientsStr.replaceAll("null", "")));
        ingredientsDescription.setText(Html.fromHtml(ingredientDescriptionStr.replaceAll("null", "")));
        steps.setText(Html.fromHtml(stepsStr.replaceAll("null", "")));
        //For comment
        listView = (ListView) findViewById(R.id.comment_list);
        adapter = new CommentListAdapter(this, comment);
        listView.setAdapter(adapter);
    }

    public void expandRecipe() {
        List<Recipe.StepsBean> steps = recipe.getSteps();
        List<Recipe.DietTypeBean> dietType = recipe.getDietType();
        List<Recipe.MealTypeBean> mealType = recipe.getMealType();
        List<Recipe.IngredientsBean> ingredients = recipe.getIngredients();
        List<Recipe.IngredientDescriptionBean> ingredientDescription = recipe.getIngredientDescription();
        for (Recipe.StepsBean s : steps) {
            stepsStr += "&#9679; " + s.getStepInstruction() + "<br/><br/>";
        }
        for (Recipe.DietTypeBean s : dietType) {
            dietTypeStr += "&#9679; " + s.getDiet() + "<br/>";
        }
        for (Recipe.MealTypeBean s : mealType) {
            mealTypeStr += "&#9679; " + s.getType() + "<br/>";
        }
        for (Recipe.IngredientsBean s : ingredients) {
            ingredientsStr += "&#9679; " + s.getIngredient() + "<br/>";
        }
        for (Recipe.IngredientDescriptionBean s : ingredientDescription) {
            ingredientDescriptionStr += "&#9679; " + s.getDetails() + "<br/>";
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
