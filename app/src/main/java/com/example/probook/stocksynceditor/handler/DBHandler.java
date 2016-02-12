package com.example.probook.stocksynceditor.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.probook.stocksynceditor.helper.SQLiteHelper;
import com.example.probook.stocksynceditor.model.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by probook on 1/24/2016.
 * Class For CRUD Operations in db;
 */

public class DBHandler {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {
            SQLiteHelper.COL_ID
            ,SQLiteHelper.COL_ITEM_NAME
            ,SQLiteHelper.COL_ITEM_QUANTITY
            ,SQLiteHelper.COL_ITEM_PRICE
            ,SQLiteHelper.COL_CREATED_ON
            ,SQLiteHelper.COL_CREATED_BY
            ,SQLiteHelper.COL_MODIFIED_ON
            ,SQLiteHelper.COL_MODIFIED_BY
            ,SQLiteHelper.COL_OBJECT_ID
    };

    public DBHandler(Context context, String dbName) {

        dbHelper = new SQLiteHelper(context, dbName);
    }

    /**
     * Essential Database Connection Operations
     */

    private void open() throws SQLiteException {

        database = dbHelper.getWritableDatabase();
    }

    private void close() {

        dbHelper.close();
    }

    /*
     *CRUD Operations
     */

    // 1. Create Stock
    public Stock createStock(Stock stock){
        this.open();
        ContentValues stockEntry = new ContentValues();
        stockEntry.put(SQLiteHelper.COL_ITEM_NAME, stock.getItemName());
        stockEntry.put(SQLiteHelper.COL_ITEM_QUANTITY, stock.getItemQuantity());
        stockEntry.put(SQLiteHelper.COL_ITEM_PRICE, stock.getItemPrice());
        stockEntry.put(SQLiteHelper.COL_CREATED_ON, stock.getCreatedOn());
        stockEntry.put(SQLiteHelper.COL_CREATED_BY, stock.getCreatedBy());
        stockEntry.put(SQLiteHelper.COL_MODIFIED_ON, stock.getModifiedOn());
        stockEntry.put(SQLiteHelper.COL_MODIFIED_BY, stock.getModifiedBy());
        stockEntry.put(SQLiteHelper.COL_OBJECT_ID, stock.getObjectId());
        Log.e("Stock object Id", stock.getObjectId());

        long insertId = database.insert(SQLiteHelper.TABLE_STOCK,null, stockEntry);

        Cursor cursor =  database.query(SQLiteHelper.TABLE_STOCK, allColumns
                , SQLiteHelper.COL_ID + " =? "
                , new String[]{String.valueOf(insertId)}
                , null, null, null
        );
        
        cursor.moveToFirst();
        Stock newStock = cursorToStock(cursor); 
        cursor.close();
        this.close();
        
        return newStock;
    }

    // 2. Read Stock
    // 2.1 Get All Stocks
    public List<Stock> getAllStocks() {

        this.open();

        Cursor cursor = database.query(SQLiteHelper.TABLE_STOCK, allColumns, null, null, null, null, null);
        List<Stock> allStocks = new ArrayList<>();

        cursor.moveToLast();
        while (!cursor.isBeforeFirst()){
            allStocks.add(cursorToStock(cursor));
            cursor.moveToPrevious();
        }

        cursor.close();
        this.close();
        return allStocks;
    }

    // 2.2 Get Stock with name ( Later )
    public ArrayList<Stock> getStockWithName() {

        return null;
    }

    // 3. Update Stock
    public Stock updateStock(Stock stock) {
        this.open();

        ContentValues stockEntry = new ContentValues();
        stockEntry.put(SQLiteHelper.COL_ID, stock.getId());
        stockEntry.put(SQLiteHelper.COL_ITEM_NAME, stock.getItemName());
        stockEntry.put(SQLiteHelper.COL_ITEM_QUANTITY, stock.getItemQuantity());
        stockEntry.put(SQLiteHelper.COL_ITEM_PRICE, stock.getItemPrice());
        stockEntry.put(SQLiteHelper.COL_CREATED_ON, stock.getCreatedOn());
        stockEntry.put(SQLiteHelper.COL_CREATED_BY, stock.getCreatedBy());
        stockEntry.put(SQLiteHelper.COL_MODIFIED_ON, stock.getModifiedOn());
        stockEntry.put(SQLiteHelper.COL_MODIFIED_BY, stock.getModifiedBy());

        database.update(SQLiteHelper.TABLE_STOCK, stockEntry
                , SQLiteHelper.COL_ID + " =? "
                , new String[]{String.valueOf(stock.getId())}
        );

        Cursor cursor =  database.query(SQLiteHelper.TABLE_STOCK, allColumns
                , SQLiteHelper.COL_ID + " =? "
                , new String[]{String.valueOf(stock.getId())}
                , null, null, null
        );

        cursor.moveToFirst();
        Stock updatedStock = cursorToStock(cursor);
        cursor.close();
        this.close();

        return updatedStock;
    }

    // 4. Delete Stock
    // 4.1 Delete stock object
    public void deleteStock(Stock stock){
        this.open();
        long id = stock.getId();

        database.delete(SQLiteHelper.TABLE_STOCK, SQLiteHelper.COL_ID + " =?", new String[]{String.valueOf(id)});
        this.close();
    }

    // 4.2 Delete all stocks
    public void deleteAllStocks(){
        this.open();
        database.execSQL("DELETE FROM " + SQLiteHelper.TABLE_STOCK);
        this.close();
    }

    // Helper Functions
    private Stock cursorToStock(Cursor cursor) {
        Stock stock = new Stock();
        stock.setId(cursor.getLong(0));
        stock.setItemName(cursor.getString(1));
        stock.setItemQuantity(cursor.getString(2));
        stock.setItemPrice(cursor.getString(3));
        stock.setCreatedOn(cursor.getString(4));
        stock.setCreatedBy(cursor.getString(5));
        stock.setModifiedOn(cursor.getString(6));
        stock.setModifiedBy(cursor.getString(7));
        stock.setObjectId(cursor.getString(8));

        return stock;
    }

}
