package com.example.probook.stocksynceditor.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.model.Stock;
import java.util.List;

/**
 * Created by probook on 1/24/2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private List<Stock> allStocks;
    private List<Stock> filteredStocks;

    private LayoutInflater inflater;

    private Activity activity;

    public CustomListAdapter(Activity activity, List<Stock> allStocks) {
        this.allStocks = allStocks;
        this.filteredStocks = allStocks;
        this.activity = activity;
        System.out.println("Custom Adapter " + "Constructor");
    }

    @Override
    public int getCount() {
        return filteredStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get Layout Inflater
        if ( inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // Inflate layout
        if ( convertView == null ){
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        // Fill layout
        TextView txtItemName = (TextView) convertView.findViewById(R.id.txt_item_name);
        TextView txtItemQuantity = (TextView) convertView.findViewById(R.id.txt_item_quantity);
        TextView txtItemPrice = (TextView) convertView.findViewById(R.id.txt_item_price);
        TextView txtModifiedOn = (TextView) convertView.findViewById(R.id.txt_modified_on);

        txtItemName.setText(filteredStocks.get(position).getItemName());
        txtItemQuantity.setText(filteredStocks.get(position).getItemQuantity());
        txtItemPrice.setText(filteredStocks.get(position).getItemPrice());
        txtModifiedOn.setText(filteredStocks.get(position).getModifiedOn());

        return convertView;
    }
}
