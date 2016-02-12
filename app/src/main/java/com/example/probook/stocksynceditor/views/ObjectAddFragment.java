package com.example.probook.stocksynceditor.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.handler.DBHandler;
import com.example.probook.stocksynceditor.model.Stock;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by probook on 1/24/2016.
 */
public class ObjectAddFragment extends Fragment implements View.OnClickListener {

    private DBHandler dataSource;

    private Calendar calender = Calendar.getInstance();
    private TextView txtCreatedOn;
    private static final String CREATED_ON = "Created On: ";
    private EditText etItemName;
    private EditText etItemQuantity;
    private EditText etItemPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_objects, container, false);
        Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set initial Creation Date
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(calender.getTime());
        txtCreatedOn = (TextView) getView().findViewById(R.id.txt_created_on);
        txtCreatedOn.setText(sDate);

        // Set button to select date from datepicker fragment
        Button btnDatePick = (Button) getView().findViewById(R.id.btn_date_pick);
        btnDatePick.setText(CREATED_ON);
        btnDatePick.setOnClickListener(this);
        // get data from edit text
        // save data on btn click
        Button btnSave = (Button) getActivity().findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_date_pick:
                setDate(); break;
            case R.id.btn_save:
                saveStock(); break;
        }
    }

    private void saveStock() {
        String sDate = new SimpleDateFormat("dd-MM-yyyy").format(calender.getTime());

        etItemName = (EditText) getView().findViewById(R.id.et_item_name);
        etItemQuantity = (EditText) getView().findViewById(R.id.et_item_quantity);
        etItemPrice = (EditText) getView().findViewById(R.id.et_item_price);

        String itemName = etItemName.getText().toString();
        String itemQuantity = etItemQuantity.getText().toString();
        String itemPrice = etItemPrice.getText().toString();

        // Create stock object
        Stock stock = new Stock();
        stock.setItemName(itemName);
        stock.setItemQuantity(itemQuantity);
        stock.setItemPrice(itemPrice);
        stock.setCreatedOn(sDate);
        stock.setCreatedBy("Santi User");
        stock.setModifiedOn(sDate);
        stock.setModifiedBy("Santi User");
        stock.setObjectId("offline");

        saveData(stock);
    }

    private void saveData(Stock stock) {
        dataSource = new DBHandler(getActivity(),"offline.db");
        dataSource.createStock(stock);

        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

        etItemName.setText("");
        etItemQuantity.setText("");
        etItemPrice.setText("");

        calender = Calendar.getInstance();
        txtCreatedOn.setText(new SimpleDateFormat("dd-MM-yyyy").format(calender.getTime()));
    }

    private void setDate() {

        DatePickerFragment fDate = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        fDate.setArguments(args);
        fDate.setCallback(onDate);
        fDate.show(getFragmentManager(),"Dialog");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(year,monthOfYear,dayOfMonth);

            String sDate = String.valueOf(year)
                    + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(dayOfMonth);

            txtCreatedOn.setText(sDate);
        }
    };
}
