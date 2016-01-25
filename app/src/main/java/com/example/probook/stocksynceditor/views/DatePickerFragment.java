package com.example.probook.stocksynceditor.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by probook on 1/24/2016.
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener onDate;

    public void setCallback(DatePickerDialog.OnDateSetListener onDate){
        this.onDate = onDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        int year, month, day;
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");

        return new DatePickerDialog(getActivity(),onDate,year,month,day);
    }
}
