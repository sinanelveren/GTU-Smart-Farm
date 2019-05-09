package com.example.aurora.akillitarim;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragList = new ArrayList<>();
    private final List<String> fragTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragList.get(i);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        fragList.add(fragment);
        fragTitleList.add(title);
    }
}
