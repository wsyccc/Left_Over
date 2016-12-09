package com.bcit.Leftovers.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.fragment.FindAMeal_Fragment;
import com.bcit.Leftovers.other.HomeImageAdapter;
import com.bcit.Leftovers.other.PredicateLayout;
import com.bcit.Leftovers.other.Recipe;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapHeading;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.bcit.Leftovers.fragment.Home_Fragment.convertStandardJSONString;

public class Ingredients_Activity extends AppCompatActivity {

    private List<Integer> options;
    private List<String> choices;
    private PredicateLayout predicateLayout;
    private List<Recipe> recipes;
    private ItemTouchHelper itemTouchHelper;
    private GridLayoutManager mLayoutManager;
    private HomeImageAdapter mAdapter;
    private RecyclerView recyclerview;
    private TextView nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        predicateLayout = (PredicateLayout) findViewById(R.id.ingredient_tags);
        recyclerview = (RecyclerView) findViewById(R.id.ingredient_results);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        nothing = (TextView) findViewById(R.id.none_result);
        nothing.setVisibility(View.INVISIBLE);
        options = new ArrayList<>();
        options = (List<Integer>) getIntent().getSerializableExtra("options");
        choices = new ArrayList<>();
        getActualIngredients();
        setTag();
        setListener();
        new GetData().execute(getQuery());
    }
    public void getActualIngredients(){
        for (int result : options){
            switch (result){
                case R.id.beef_image:
                    choices.add("beef");
                    break;
                case R.id.cheese_image:
                    choices.add("cheese");
                    break;
                case R.id.shrimp_image:
                    choices.add("shrimp");
                    break;
                case R.id.fish_image:
                    choices.add("fish");
                    break;
                case R.id.pork_image:
                    choices.add("pork");
                    break;
                case R.id.turkey_image:
                    choices.add("turkey");
                    break;
                case R.id.asparagus_image:
                    choices.add("asparagus");
                    break;
                case R.id.bellpeppers_image:
                    choices.add("bell peppers");
                    break;
                case R.id.bokchoy_image:
                    choices.add("bokchoy");
                    break;
                case R.id.broccoli_image:
                    choices.add("broccoli");
                    break;
                case R.id.cabbage_image:
                    choices.add("cabbage");
                    break;
                case R.id.carrots_image:
                    choices.add("carrots");
                    break;
                case R.id.cauliflower_image:
                    choices.add("flower");
                    break;
                case R.id.chillipepper_image:
                    choices.add("chili");
                    break;
                case R.id.corn_image:
                    choices.add("corn");
                    break;
                case R.id.cucumbers_image:
                    choices.add("cucumber");
                    break;
                case R.id.jalapeno_image:
                    choices.add("jalapeno");
                    break;
                case R.id.lettuce_image:
                    choices.add("lettuce");
                    break;
                case R.id.lime_image:
                    choices.add("lime");
                    break;
                case R.id.mushrooms_image:
                    choices.add("mushroom");
                    break;
                case R.id.peas_image:
                    choices.add("peas");
                    break;
                case R.id.potato_image:
                    choices.add("potato");
                    break;
                case R.id.spinach_image:
                    choices.add("spinach");
                    break;
                case R.id.tofu_image:
                    choices.add("tofu");
                    break;
                case R.id.tomato_image:
                    choices.add("tomato");
                    break;
                case R.id.zucchini_image:
                    choices.add("zucchini");
                    break;
                case R.id.almond_image:
                    choices.add("almond");
                    break;
                case R.id.bread_image:
                    choices.add("bread");
                    break;
                case R.id.noodle_image:
                    choices.add("noodle");
                    break;
                case R.id.pasta_image:
                    choices.add("pasta");
                    break;
                case R.id.peanuts_image:
                    choices.add("peanut");
                    break;
                case R.id.rice_image:
                    choices.add("rice");
                    break;
                case R.id.butter_image:
                    choices.add("butter");
                    break;
                case R.id.egg_image:
                    choices.add("egg");
                    break;
                case R.id.milk_image:
                    choices.add("milk");
                    break;
                case R.id.yogurt_image:
                    choices.add("yogurt");
                    break;
                case R.id.apple_image:
                    choices.add("apple");
                    break;
                case R.id.avocado_image:
                    choices.add("avocado");
                    break;
                case R.id.banana_image:
                    choices.add("banana");
                    break;
                case R.id.blueberry_image:
                    choices.add("blueberry");
                    break;
                case R.id.orange_image:
                    choices.add("orange");
                    break;
                case R.id.mango_image:
                    choices.add("mango");
                    break;
                case R.id.peach_image:
                    choices.add("peach");
                    break;
                case R.id.grape_image:
                    choices.add("grape");
                    break;
                case R.id.pineapple_image:
                    choices.add("pineapple");
                    break;
                case R.id.strawberry_image:
                    choices.add("strawberry");
                    break;
                case R.id.watermelon_image:
                    choices.add("watermelon");
                    break;
                case R.id.chicken_image:
                    choices.add("chicken");
                    break;
                default:
                    break;
            }
        }
    }

    public void setTag(){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.shrink_to_original);
        if (choices != null){
            for (int i =0; i< choices.size(); i++){
                BootstrapLabel label = new BootstrapLabel(this);
                label.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
                label.setBootstrapHeading(DefaultBootstrapHeading.H6);
                label.setAnimation(anim);
                label.startAnimation(anim);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 20, 0);
                label.setLayoutParams(lp);
                label.setText(choices.get(i));
                predicateLayout.addView(label,new PredicateLayout.LayoutParams(2, 0));
            }
        }
    }

    public String getQuery(){
        String selection = "";
        for (String item: choices){
            selection += item+",";
        }
        return "collection=recipe"+"&ingredients="+selection + "&action=findByIngredient";
    }

    public void setListener() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(recipes, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        private HttpsURLConnection connection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "null";
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                URL url = new URL("https://wayneking.me/mongoDB/leftover_mongodb.php");
                this.connection = (HttpsURLConnection) url.openConnection();
                this.connection.setRequestProperty("connection", "Keep-Alive");
                this.connection.setConnectTimeout(5 * 1000);
                this.connection.setReadTimeout(5 * 1000);
                this.connection.setRequestMethod("POST");
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.connect();
                out = new PrintWriter(this.connection.getOutputStream());
                out.print(params[0]);
                out.flush();
                if (this.connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                    result = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                Gson gson = new Gson();
                String jsonData = null;
                Log.d("why!!!",result);
                try {
                    JSONArray jsonArray = null;
                    if (!result.equalsIgnoreCase("<br />")) {
                        jsonArray = new JSONArray(result);
                        jsonData = convertStandardJSONString(jsonArray.toString());
                    }if (result.equals("") || result.equals("[]")){
                        nothing.setVisibility(View.VISIBLE);
                    }
                    recipes = gson.fromJson(jsonData, new TypeToken<List<Recipe>>() {
                    }.getType());
                    Recipe recipe = new Recipe();
                    recipes.add(recipe);
                    if (jsonArray.length() != recipes.size()) {
                        recipes.remove(recipes.size() - 1);
                    }
                    recyclerview.setAdapter(mAdapter = new HomeImageAdapter(Ingredients_Activity.this, recipes));
                    mAdapter.setOnItemClickListener(new HomeImageAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
                            Intent intent = new Intent(Ingredients_Activity.this, RecipeActivity.class);
                            intent.putExtra("recipe", recipes.get(position));
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view) {
                            itemTouchHelper.startDrag(recyclerview.getChildViewHolder(view));
                        }
                    });
                    itemTouchHelper.attachToRecyclerView(recyclerview);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
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
            choices.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
