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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.bcit.Leftovers.fragment.Home_Fragment.convertStandardJSONString;

public class Find_Meal_Activity extends AppCompatActivity {

    private PredicateLayout predicateLayout;
    private Map<String, List> result = null;
    private List<String> mealType;
    private int hotness;
    private int difficulty;
    private List<Recipe> recipes;
    private ItemTouchHelper itemTouchHelper;
    private GridLayoutManager mLayoutManager;
    private HomeImageAdapter mAdapter;
    private RecyclerView recyclerview;
    private TextView nothing;
    private String vegan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__meal_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Meal");
        predicateLayout = (PredicateLayout) findViewById(R.id.tags);
        recyclerview = (RecyclerView) findViewById(R.id.results);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        nothing = (TextView) findViewById(R.id.no_result);
        nothing.setVisibility(View.INVISIBLE);
        result = (HashMap<String, List>) getIntent().getSerializableExtra("choices");
        mealType = result.get("mealType");
        setTags();
        setListener();
        Log.d("a!!!!!",generateQuery());
        new GetData().execute(generateQuery());
    }

    public String generateQuery(){
        String meal = "";
        for (String type: mealType){
            meal += type+",";
        }
        return "collection=recipe"+"&hotness="+hotness+"&difficulty="+difficulty+"&mealType="+meal+
                "&vegan="+ vegan + "&action=findMeal";
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

    public void setTags() {
        for (Map.Entry<String, List> entry : result.entrySet()) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.shrink_to_original);
            for (int i = 0; i < entry.getValue().size(); i++) {
                BootstrapLabel label = new BootstrapLabel(this);
                label.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
                label.setBootstrapHeading(DefaultBootstrapHeading.H6);
                label.setAnimation(anim);
                label.startAnimation(anim);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 20, 0);
                label.setLayoutParams(lp);
                if (entry.getKey().equals("mealType")) {
                    label.setText(entry.getValue().get(i).toString());
                } else if (entry.getKey().equals("hotness")) {
                    hotness = (Integer) entry.getValue().get(i);
                    switch (hotness) {
                        case 0:
                            label.setText("Non Spicy");
                            break;
                        case 1:
                            label.setText("Slightly Spicy");
                            break;
                        case 2:
                            label.setText("Small Chili");
                            break;
                        case 3:
                            label.setText("Medium Hot");
                            break;
                        case 4:
                            label.setText("Super spicy");
                            break;
                        default:
                            break;
                    }
                } else if (entry.getKey().equals("difficulty")) {
                    difficulty = (Integer) entry.getValue().get(i);
                    switch (difficulty) {
                        case 0:
                            label.setText("Easy");
                            break;
                        case 1:
                            label.setText("Slightly Easy");
                            break;
                        case 2:
                            label.setText("Not Hard");
                            break;
                        case 3:
                            label.setText("Hard");
                            break;
                        case 4:
                            label.setText("Extremely Hard");
                            break;
                        default:
                            break;
                    }
                } else if (entry.getKey().equals("vegan")) {
                    if ((Boolean) entry.getValue().get(i)) {
                        label.setText("Vegan");
                        vegan = "vegetarian";
                    } else {
                        label.setText("Meat Lover");
                        vegan = "normal";
                    }
                }
                predicateLayout.addView(label,new PredicateLayout.LayoutParams(2, 0));
            }
        }
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
                Log.d("comment", params[0]);
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
                    recyclerview.setAdapter(mAdapter = new HomeImageAdapter(Find_Meal_Activity.this, recipes));
                    mAdapter.setOnItemClickListener(new HomeImageAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
                            Intent intent = new Intent(Find_Meal_Activity.this, RecipeActivity.class);
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
            FindAMeal_Fragment.choices.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
