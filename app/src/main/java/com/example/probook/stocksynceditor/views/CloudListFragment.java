package com.example.probook.stocksynceditor.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.controller.NetworkCommunicator;
import com.example.probook.stocksynceditor.handler.DBHandler;
import com.example.probook.stocksynceditor.helper.CustomListAdapter;
import com.example.probook.stocksynceditor.helper.SQLiteHelper;
import com.example.probook.stocksynceditor.model.Stock;
import java.util.List;

/**
 * Created by probook on 1/24/2016.
 */
public class CloudListFragment extends Fragment {

    public DBHandler dataSouce;
    private CustomListAdapter adapter;
    private ListView lv;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataSouce = new DBHandler(activity, "cloud.db");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_objects, container, false);
        Bundle args = getArguments();
        Log.e("Cloud Fragment", new Object(){}.getClass().getEnclosingMethod().getName());
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
                new NetworkCommunicator(getActivity()).getData();
            }
        });
        Log.e("Cloud Fragment", new Object() {
        }.getClass().getEnclosingMethod().getName());

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = (Stock) adapter.getItem(position);
                ObjectEditDialogFragment editDialogFragment = new ObjectEditDialogFragment();
                Bundle args = new Bundle();
                args.putString(SQLiteHelper.COL_ITEM_NAME, stock.getItemName());
                args.putString(SQLiteHelper.COL_ITEM_QUANTITY, stock.getItemQuantity());
                args.putString(SQLiteHelper.COL_ITEM_PRICE, stock.getItemPrice());
                args.putString(SQLiteHelper.COL_CREATED_ON, stock.getCreatedOn());
                args.putString(SQLiteHelper.COL_CREATED_BY, stock.getCreatedBy());
                args.putString(SQLiteHelper.COL_OBJECT_ID, stock.getObjectId());

                Log.e("On click objid: ", stock.getObjectId().getClass().getSimpleName());
                editDialogFragment.setArguments(args);
                editDialogFragment.show(getFragmentManager(), "editDialog");

                return true;
            }
        });

        EditText etSearch = (EditText) getView().findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void dbUpdated(){

        List<Stock> allStocks = dataSouce.getAllStocks();
        lv = (ListView) getView().findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), allStocks);
        lv.setAdapter(adapter);
    }
}
