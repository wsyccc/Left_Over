package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.other.BannerAdapter;
import com.bcit.Leftovers.other.Home_Fragment_Info;

import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ViewPager mViewPager;
    private List<ImageView> mlist;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private int[] bannerImages = { R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4 };
    private String[] bannerTexts = { "COLOMBIAN VEGETABLE SOUP", "PRESSURE COOKER CORN SOUP",
            "QUICK AND EASY PRESSURE COOKER CHICKEN, LENTIL, AND BACON STEW WITH CARROTS",
            "QUICK AND EASY PRESSURE COOKER CHICKEN AND BLACK BEAN STEW" };
    private BannerAdapter mAdapter;
    private BannerListener bannerListener;
    private int pointIndex = 0;
    private boolean isStop = false;
    private List<String> imageUrl;
    private RecyclerView mRecyclerView;
    private Home_Fragment_Info recycleAdapter;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        initUrl();
        recycleAdapter = new Home_Fragment_Info(getActivity(), imageUrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(recycleAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initAction();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * Banner Listener
     */
    class BannerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % bannerImages.length;
            mTextView.setText(bannerTexts[newPosition]);
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            mLinearLayout.getChildAt(pointIndex).setEnabled(false);
            pointIndex = newPosition;
        }

    }

    /**
     * init Banner View
     */
    private void initView() {
        mViewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        mTextView = (TextView) getActivity().findViewById(R.id.tv_bannertext);
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.points);
    }

    /**
     * init Banner Action
     */
    private void initAction() {
        bannerListener = new BannerListener();
        mViewPager.addOnPageChangeListener(bannerListener);
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mlist.size());
        mViewPager.setCurrentItem(index);
        mLinearLayout.getChildAt(pointIndex).setEnabled(true);
    }

    /**
     * init Banner Data
     */
    private void initData() {
        mlist = new ArrayList<>();
        View view;
        LayoutParams params;
        for (int i = 0; i < bannerImages.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(bannerImages[i]);
            mlist.add(imageView);
            view = new View(getActivity());
            params = new LayoutParams(5, 5);
            params.leftMargin = 10;
            view.setBackgroundResource(R.drawable.point_background);
            view.setLayoutParams(params);
            view.setEnabled(false);

            mLinearLayout.addView(view);
        }
        mAdapter = new BannerAdapter(mlist);
        mViewPager.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();
    }
    public void initUrl(){
        imageUrl = new ArrayList<>();
        imageUrl.add("https://wayneking.me/mongoDB/leftover_images/recipes/images/1.jpg");
        imageUrl.add("https://wayneking.me/mongoDB/leftover_images/recipes/images/2.jpg");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
