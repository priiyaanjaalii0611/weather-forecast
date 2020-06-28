package com.example.android.weatherapp2.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragementList=new ArrayList<>();
    private final List<String> fragmentTitle= new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        return fragementList.get(i);
    }

    @Override
    public int getCount() {
        return fragementList.size();
    }

    public void addFragment(Fragment fragment,String title){
        fragementList.add(fragment);
        fragmentTitle.add(title);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  fragmentTitle.get(position);
    }

}
