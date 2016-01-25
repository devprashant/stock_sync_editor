package com.example.probook.stocksynceditor.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by probook on 1/24/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_STOCK = "stock_table";

    public static final String COL_ID = "_id";
    public static final String COL_ITEM_NAME = "item_name";
    public static final String COL_ITEM_QUANTITY = "item_quantity";
    public static final String COL_ITEM_PRICE = "item_price";
    public static final String COL_CREATED_ON = "created_on";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_MODIFIED_ON = "modified_on";
    public static final String COL_MODIFIED_BY = "modified_by";

    public static String DATABASE_NAME ;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_CMD = "create table " + TABLE_STOCK + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_ITEM_NAME + " text not null, "
            + COL_ITEM_QUANTITY + " text not null, "
            + COL_ITEM_PRICE + " text not null, "
            + COL_CREATED_ON + " text not null, "
            + COL_CREATED_BY + " text not null, "
            + COL_MODIFIED_ON + " text not null, "
            + COL_MODIFIED_BY + " text not null "
            + ")";

    public SQLiteHelper(Context context, String dbName){
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(db);
    }
}
