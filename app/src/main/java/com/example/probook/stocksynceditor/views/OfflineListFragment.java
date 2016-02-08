package com.example.probook.stocksynceditor.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.controller.NetworkCommunicator;
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
    private ListView lv;

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

        lv = (ListView) getView().findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), allStocks);
        lv.setAdapter(adapter);
        Button btnUpdate = (Button) getView().findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkCommunicator(getActivity()).setData();
            }
        });
        Log.e("Offline Fragment", new Object() {}.getClass().getEnclosingMethod().getName());
    }

    public void dbUpdated(){

        List<Stock> allStocks = dataSouce.getAllStocks();
        lv = (ListView) getView().findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), allStocks);
        lv.setAdapter(adapter);
    }

}
