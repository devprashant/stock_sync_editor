package com.example.probook.stocksynceditor.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by probook on 1/24/2016.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private String[] pageTitle = {"On Device", "On Cloud", " Add Stock"};
    public SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        switch (position){
            case 0:
                fragment = new OfflineListFragment();
                args.putString("fName", "Offline Fragment");
                Log.e("fName", "Offline Fragment");
                fragment.setArguments(args);
                registeredFragments.put(position, fragment);
                break;
            case 1:
                fragment = new CloudListFragment();
                args.putString("fName", "Cloud Fragment");
                Log.e("fName", "Cloud Fragment");
                fragment.setArguments(args);
                registeredFragments.put(position, fragment);
                break;
            case 2:
                fragment = new ObjectAddFragment();
                args.putString("fName", "Add Fragment");
                Log.e("fName", "Add Fragment");
                fragment.setArguments(args);
                registeredFragments.put(position, fragment);
                break;
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        registeredFragments.remove(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }
}
