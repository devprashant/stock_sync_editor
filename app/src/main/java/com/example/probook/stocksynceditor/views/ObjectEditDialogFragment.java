package com.example.probook.stocksynceditor.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.controller.NetworkCommunicator;
import com.example.probook.stocksynceditor.handler.DBHandler;
import com.example.probook.stocksynceditor.helper.SQLiteHelper;
import com.example.probook.stocksynceditor.model.Stock;

/**
 * Created by probook on 2/8/2016.
 */
public class ObjectEditDialogFragment extends DialogFragment {

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private DBHandler dataSouce;
    private View dialogView;

    private NetworkCommunicator.onOfflineDBUpdateListener mfCallback;

    // Editext in layout
    private EditText etItemName;
    private EditText etQuantity;
    private EditText etPrice;

    // Arguments passed as bundle
    private long itemId;
    private String itemName;
    private String quantity;
    private String price;
    private String createdOn;
    private String createdBy;
    private String modifiedOn;
    private String modifiedBy;
    private String objectId;
    private Activity activityContext;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityContext = activity;
        dataSouce = new DBHandler(activity, "offline.db");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mfCallback = (NetworkCommunicator.onOfflineDBUpdateListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString() + " must implement onOfflineDBchanged listsner");
        }
        mfCallback.onOfflineDBChanged();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.fragment_edit_object, null);

        etItemName = ((EditText) dialogView.findViewById(R.id.et_item_name));
        etQuantity = ((EditText) dialogView.findViewById(R.id.et_item_quantity));
        etPrice = ((EditText) dialogView.findViewById(R.id.et_item_price));

        itemName = getArguments().getString(SQLiteHelper.COL_ITEM_NAME);
        quantity = getArguments().getString(SQLiteHelper.COL_ITEM_QUANTITY);
        price = getArguments().getString(SQLiteHelper.COL_ITEM_PRICE);
        createdOn = getArguments().getString(SQLiteHelper.COL_CREATED_ON);
        createdBy = getArguments().getString(SQLiteHelper.COL_CREATED_BY);
        objectId = getArguments().getString(SQLiteHelper.COL_OBJECT_ID);

        Log.e("Object id in edit", objectId);

        etItemName.setText(itemName);
        etQuantity.setText(quantity);
        etPrice.setText(price);

        builder.setTitle("Edit Stock")
                .setView(dialogView)
                .setNegativeButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Stock stock = new Stock();

                        itemName = etItemName.getText().toString();
                        quantity = etQuantity.getText().toString();
                        price = etPrice.getText().toString();

                        stock.setItemName(itemName);
                        stock.setItemQuantity(quantity);
                        stock.setItemPrice(price);
                        stock.setCreatedOn(createdOn);
                        stock.setCreatedBy(createdBy);
                        stock.setModifiedOn("8 feb 2016");
                        stock.setModifiedBy("SANTI USer");
                        stock.setObjectId(objectId);

                        dataSouce.createStock(stock);

                    }
                })
        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Edit Canceled", Toast.LENGTH_SHORT).show();
            }
        })
        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check internet connectivity (pre requisite )
                new NetworkCommunicator(getActivity()).deleteData(objectId);
            }
        });
        return  builder.create();
    }
}
