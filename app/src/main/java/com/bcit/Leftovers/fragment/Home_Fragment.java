package com.bcit.Leftovers.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.bcit.Leftovers.other.ImageHolderView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Home_Fragment extends Fragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener, OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int change = 0;
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
    private ListView listView;
    private ArrayAdapter transformerArrayAdapter;
    private ArrayList<String> transformerList = new ArrayList<>(Arrays.asList(
            DefaultTransformer.class.getSimpleName(), AccordionTransformer.class.getSimpleName(),
            BackgroundToForegroundTransformer.class.getSimpleName(), CubeInTransformer.class.getSimpleName(),
            CubeOutTransformer.class.getSimpleName(), DepthPageTransformer.class.getSimpleName(),
            FlipHorizontalTransformer.class.getSimpleName(), FlipVerticalTransformer.class.getSimpleName(),
            ForegroundToBackgroundTransformer.class.getSimpleName(), RotateDownTransformer.class.getSimpleName(),
            RotateUpTransformer.class.getSimpleName(), StackTransformer.class.getSimpleName(),
            ZoomInTransformer.class.getSimpleName(), ZoomOutTranformer.class.getSimpleName()));

    public Home_Fragment() {}

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
        return rootView;
    }

    @Override
    public void onStart(){
        convenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        listView = (ListView) getActivity().findViewById(R.id.listView);
        transformerArrayAdapter = new ArrayAdapter(getActivity().getBaseContext(), R.layout.adapter_transformer, transformerList);
        listView.setAdapter(transformerArrayAdapter);
        listView.setOnItemClickListener(this);
        networkImages= Arrays.asList(images);
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
            @Override
            public ImageHolderView createHolder() {
                return new ImageHolderView();
            }
        },networkImages);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Collections.swap(transformerList, 0, new Random().nextInt(13-1)+1);
                String transforemerName = transformerList.get(0);
                try {
                    Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
                    ABaseTransformer transforemer= (ABaseTransformer)cls.newInstance();
                    convenientBanner.getViewPager().setPageTransformer(true,transforemer);

                    //部分3D特效需要调整滑动速度
                    if(transforemerName.equals("StackTransformer")){
                        convenientBanner.setScrollDuration(1200);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
        });
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
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
    public void onPause(){
        super.onPause();
        convenientBanner.stopTurning();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//        点击后加入两个内容
//        localImages.clear();
//        localImages.add(R.drawable.ic_test_2);
//        localImages.add(R.drawable.ic_test_4);
//        convenientBanner.notifyDataSetChanged();

        //控制是否循环
//        convenientBanner.setCanLoop(!convenientBanner.isCanLoop());


//        String transforemerName = transformerList.get(position);
//        try {
//            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
//            ABaseTransformer transforemer= (ABaseTransformer)cls.newInstance();
//            convenientBanner.getViewPager().setPageTransformer(true,transforemer);
//
//            //部分3D特效需要调整滑动速度
//            if(transforemerName.equals("StackTransformer")){
//                convenientBanner.setScrollDuration(1200);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(getClass().getName(), e.getMessage());
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("PageScrolled", "123456789");
    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(getContext(),"监听到翻到第"+position+"了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("PageScrolled", "123456789");
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(getActivity().getBaseContext(),"点击了第"+position+"个",Toast.LENGTH_SHORT).show();
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
