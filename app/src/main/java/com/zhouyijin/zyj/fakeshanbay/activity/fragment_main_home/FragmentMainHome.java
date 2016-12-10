package com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.dictionaries_fragment.HomeSubFragmentDictionaries;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.expansion_fragment.HomeSubFragmentExpansion;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.progress_fragment.HomeSubFragmentProgress;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.words_fragment.HomeSubFragmentWords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyijin on 2016/10/24.
 */

public class FragmentMainHome extends Fragment implements View.OnClickListener {

    public static final String TAG = FragmentMainHome.class.getSimpleName();


    private ViewPager vp_fragment_main_home_viewpager;
    private List<Fragment> fragments;
    private List<String> titles;
    private HomeFragmentViewPagerAdapter fragmentViewPagerAdapter;
    private TabLayout tabLayout;
    private ImageButton searchButton, notificationButton;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareDataForViewPager();
    }

    private void prepareDataForViewPager() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        fragments.add(new HomeSubFragmentWords());
        fragments.add(new HomeSubFragmentProgress());
        fragments.add(new HomeSubFragmentDictionaries());
        fragments.add(new HomeSubFragmentExpansion());
        titles.add(getResources().getString(R.string.remember_words));
        titles.add(getResources().getString(R.string.progress));
        titles.add(getResources().getString(R.string.dictionaries));
        titles.add(getResources().getString(R.string.expansion));
        fragmentViewPagerAdapter = new HomeFragmentViewPagerAdapter(getFragmentManager(), fragments, titles);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container);
        Log.d(TAG, "onCreateView: ");
        return view;
    }

    @NonNull
    private View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        initViewPager(view);
        initTopBar(view);
        return view;
    }

    private void initTopBar(View container) {
        searchButton = (ImageButton) container.findViewById(R.id.iv_topbar_icon_search);
        notificationButton = (ImageButton) container.findViewById(R.id.iv_topbar_icon_notification);
        searchButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
    }

    private void initViewPager(View container){
        tabLayout = (TabLayout) container.findViewById(R.id.tl_fragment_main_home_tablayout);
        vp_fragment_main_home_viewpager = (ViewPager) container.findViewById(R.id.vp_fragment_main_home_viewpager);
        vp_fragment_main_home_viewpager.setAdapter(fragmentViewPagerAdapter);
        tabLayout.setupWithViewPager(vp_fragment_main_home_viewpager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.theme_blue),
                getResources().getColor(R.color.theme_blue));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.theme_blue));
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //表示固定住不能滑动, TabLayout.MODE_SCROLLABLE表示可以滑动


    }


    @Override
    public void onClick(View view) {

    }
}