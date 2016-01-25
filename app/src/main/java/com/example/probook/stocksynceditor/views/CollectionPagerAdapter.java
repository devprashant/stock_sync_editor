package com.example.probook.stocksynceditor.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by probook on 1/24/2016.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private String[] pageTitle = { "On Cloud", "On Device", " Add Stock"};
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        switch (position){
            case 1:
                fragment = new CloudListFragment();
                args.putString("fName", "List Fragment");
                fragment.setArguments(args);
                break;
            case 0:
                fragment = new OfflineListFragment();
                args.putString("fName", "Offline Fragment");
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new ObjectAddFragment();
                args.putString("fName", "Add Fragment");
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }
}
