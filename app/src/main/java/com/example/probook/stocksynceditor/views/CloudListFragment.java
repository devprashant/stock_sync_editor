package com.example.probook.stocksynceditor.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.probook.stocksynceditor.R;

/**
 * Created by probook on 1/24/2016.
 */
public class CloudListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_objects, container, false);
        Bundle args = getArguments();
        return rootView;
    }

}
