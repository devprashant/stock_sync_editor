package com.example.probook.stocksynceditor.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.handler.DBHandler;
import com.example.probook.stocksynceditor.helper.CustomListAdapter;
import com.example.probook.stocksynceditor.model.Stock;
import java.util.List;

/**
 * Created by probook on 1/24/2016.
 */
public class OfflineListFragment extends Fragment {

    public DBHandler dataSouce;
    private CustomListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataSouce = new DBHandler(activity, "offline.db");
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_objects, container, false);
        Bundle args = getArguments();
        Log.e("Offline Fragment", new Object(){}.getClass().getEnclosingMethod().getName());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Stock> allStocks = dataSouce.getAllStocks();

        ListView lv = (ListView) getView().findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), allStocks);
        lv.setAdapter(adapter);
        Log.e("Offline Fragment", new Object() {}.getClass().getEnclosingMethod().getName());
    }
}
