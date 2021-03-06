package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bcit.Leftovers.R;
import com.bcit.Leftovers.activity.RecipeActivity;
import com.bcit.Leftovers.other.HomeImageAdapter;
import com.bcit.Leftovers.other.Recipe;
import com.bcit.Leftovers.other.ImageHolderView;
import com.bcit.Leftovers.other.SnackbarUtil;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Home_Fragment extends Fragment implements AdapterView.OnItemClickListener {


    private ConvenientBanner convenientBanner;
    private List<String> networkImages;
    private String[] images = {
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image1.jpg",
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image2.jpg",
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image3.jpg",
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image4.jpg",
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image5.jpg",
            "https://wayneking.me/mongoDB/leftover_images/adBanner/image6.jpg"
    };
    private ArrayList<String> transformerList = new ArrayList<>(Arrays.asList(
            DefaultTransformer.class.getSimpleName(), AccordionTransformer.class.getSimpleName(),
            BackgroundToForegroundTransformer.class.getSimpleName(), CubeInTransformer.class.getSimpleName(),
            CubeOutTransformer.class.getSimpleName(), DepthPageTransformer.class.getSimpleName(),
            FlipHorizontalTransformer.class.getSimpleName(), FlipVerticalTransformer.class.getSimpleName(),
            ForegroundToBackgroundTransformer.class.getSimpleName(), RotateDownTransformer.class.getSimpleName(),
            RotateUpTransformer.class.getSimpleName(), StackTransformer.class.getSimpleName(),
            ZoomInTransformer.class.getSimpleName(), ZoomOutTranformer.class.getSimpleName()));

    private RecyclerView recyclerview;
    private LinearLayout coordinatorLayout;
    private HomeImageAdapter mAdapter;
    private List<Recipe> recipes;
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private static int ID = 0;
    private ItemTouchHelper itemTouchHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;


    public Home_Fragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            view = inflater.inflate(R.layout.fragment_home, container, false);
            coordinatorLayout = (LinearLayout) view.findViewById(R.id.line_coordinatorLayout);
            recyclerview = (RecyclerView) view.findViewById(R.id.grid_recycler);
            mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(mLayoutManager);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.grid_swipe_refresh);
            swipeRefreshLayout.setProgressViewOffset(false, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                            getResources().getDisplayMetrics()));
            return view;
        }catch (Exception e){
            e.printStackTrace();
            return view;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ID = 0;
        super.onActivityCreated(savedInstanceState);
//        convenientBanner = (ConvenientBanner) LayoutInflater.from(getContext()).inflate(R.layout.adapter_header_cb,null);
//        convenientBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,600));
        convenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        networkImages = Arrays.asList(images);
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, networkImages);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Collections.swap(transformerList, 0, new Random().nextInt(13 - 1) + 1);
                String transformerName = transformerList.get(0);
                try {
                    Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transformerName);
                    ABaseTransformer transformer = (ABaseTransformer) cls.newInstance();
                    convenientBanner.getViewPager().setPageTransformer(true, transformer);

                    if (transformerName.equals("StackTransformer")) {
                        convenientBanner.setScrollDuration(1200);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
        });
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("PageScrolled", position+"");
            }

            @Override
            public void onPageSelected(int position) {
                //Log.d("onPageSelected", position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d("PageScrollStateChanged", state+"");
            }
        });
        setListener();
        new GetData().execute(ID);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ID = 0;
        convenientBanner.startTurning(5000);
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
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }



    private void setListener() {
        try{
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ID = 0;
                    recipes.clear();
                    new GetData().execute(ID);
                    ID = 10;
                }
            });

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

            //recyclerview滚动监听
            recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                    // 滑动状态停止并且剩余少于三个item时，自动加载下一页
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 3 >= mLayoutManager.getItemCount()) {
                        if (recipes != null){
                            new GetData().execute(ID+=10);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                获取加载的最后一个可见视图在适配器的位置。
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GetData extends AsyncTask<Integer, Void, String> {

        private HttpsURLConnection connection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置swipeRefreshLayout为刷新状态
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(Integer... params) {
            String query = "collection=recipe" + "&id=" + params[0] + "&action=home";
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
                out.print(query);
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
                try {
                    JSONArray jsonArray = null;
                    if (!result.equalsIgnoreCase("<br />")){
                        jsonArray = new JSONArray(result);
                        jsonData = convertStandardJSONString(jsonArray.toString());
                    }
                    //防止不停的加载
                    if (jsonArray == null || jsonArray.length() <= 0){
                        SnackbarUtil.ShortSnackbar(coordinatorLayout, "This is the last one", SnackbarUtil.Info).show();
                    }else {
                        //ID += 10;
                        if (recipes == null || recipes.size() == 0) {
                            recipes = gson.fromJson(jsonData, new TypeToken<List<Recipe>>() {
                            }.getType());
                            Recipe recipe = new Recipe();
                            recipes.add(recipe);
                            if (jsonArray.length() != recipes.size()){
                                recipes.remove(recipes.size()-1);
                            }

                        } else {
                            List<Recipe> more = gson.fromJson(jsonData, new TypeToken<List<Recipe>>() {
                            }.getType());
                            recipes.addAll(more);
                            Recipe recipe = new Recipe();
                            recipes.add(recipe);
                            if (jsonArray.length() != recipes.size()){
                                recipes.remove(recipes.size()-1);
                            }
                        }
                        if (mAdapter == null || ID == 10) {
                            recyclerview.setAdapter(mAdapter = new HomeImageAdapter(getActivity(), recipes));

                            mAdapter.setOnItemClickListener(new HomeImageAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view) {
                                    int position = recyclerview.getChildAdapterPosition(view);
                                    SnackbarUtil.ShortSnackbar(coordinatorLayout, recipes.get(position).getIngredients().get(0).getIngredient(), SnackbarUtil.Info).show();
                                    Intent intent = new Intent(getActivity(), RecipeActivity.class);
                                    intent.putExtra("recipe",recipes.get(position));
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(View view) {
                                    itemTouchHelper.startDrag(recyclerview.getChildViewHolder(view));
                                }
                            });
                            itemTouchHelper.attachToRecyclerView(recyclerview);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
            //停止swipeRefreshLayout加载动画
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\", "");
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        return data_json;
    }


}
