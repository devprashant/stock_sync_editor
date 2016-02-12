package com.example.probook.stocksynceditor.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.handler.DBHandler;
import com.example.probook.stocksynceditor.model.Stock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by probook on 2/5/2016.
 */
public class NetworkCommunicator {

    private Context context;
    private onDBUpdateListener mCallback;
    private onOfflineDBUpdateListener mfCallback;
    private DBHandler dataSouce;

    public NetworkCommunicator(Context context) {
        this.context = context;
    }

    public void deleteData(String objectId){

        // Checking for mainactivity for implementing callback interface
        try {
            mCallback = (onDBUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onDBchanged listsner");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://stock-devprashant.rhcloud.com/stocks/dstock/" + objectId;
        //String url = "http://10.0.2.2:3000/stocks/dstock/" + objectId;
        Log.e("Id of object to delete", objectId);

        // Get data from Network

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    Log.e("D JSON Response", response.getClass().getSimpleName());

                    // Async Update data in cloud.db
                    Toast.makeText(context,"Delete Successfully", Toast.LENGTH_SHORT).show();
                    new NetworkCommunicator(context).getData();
                } else {
                    Log.e("JSON Response", "EMPTY");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        requestQueue.add(req);
    }
    public void setData(){

        try {
            mfCallback = (onOfflineDBUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onOfflineDBchanged listsner");
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://stock-devprashant.rhcloud.com/stocks/cstock";
        //String url = "http://10.0.2.2:3000/stocks/cstock";

        dataSouce = new DBHandler(context,"offline.db");
        final List<Stock> allStocks = dataSouce.getAllStocks();
        final int[] counter = {0};

        JsonObjectRequest req;
        for (final Stock stock : allStocks) {
            HashMap<String, String> stockToSet = new HashMap<>();
            stockToSet.put("itemname", stock.getItemName());
            stockToSet.put("quantity", stock.getItemQuantity());
            stockToSet.put("price", stock.getItemPrice());
            stockToSet.put("createdon", stock.getCreatedOn());
            stockToSet.put("createdby", stock.getCreatedBy());
            stockToSet.put("modifiedon", stock.getModifiedOn());
            stockToSet.put("modifiedby", stock.getModifiedBy());

            JSONObject jsonReqBody = new JSONObject(stockToSet);

            Log.e("Json", jsonReqBody.toString());

            req = new JsonObjectRequest(Request.Method.POST, url, jsonReqBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Data: ", "Added successfully" + stock.getItemName());
                    // Delete stock if inserted in cloud successfully
                    new DBHandler(context, "offline.db").deleteStock(stock);

                    // counter to display Toast when all offline stcok is uploaded.
                    counter[0]++;
                    if(counter[0] == allStocks.size()){
                        Toast.makeText(context, "All Stocks Uploaded", Toast.LENGTH_SHORT).show();
                    }
                    // Notify adapter for the change
                    // This callback should synchronous with the upper delete stock
                    // Here this works good as above operation is very quick
                    // !may create problem if local data is huge
                    mfCallback.onOfflineDBChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(context, context.getString(R.string.error_network_timeout), Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        //TODO
                    } else if (error instanceof ServerError) {
                        //TODO
                    } else if (error instanceof NetworkError) {
                        //TODO
                    } else if (error instanceof ParseError) {
                        //TODO
                    }
                }
            });

            requestQueue.add(req);
        }
    }

    public void getData() {

        // Checking for mainactivity for implementing callback interface
        try {
            mCallback = (onDBUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onDBchanged listsner");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://stock-devprashant.rhcloud.com/stocks/lstock";
        //String url = "http://10.0.2.2:3000/stocks/lstock";

        // Get data from Network
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    Log.e("JSONRespone", response.getClass().getSimpleName());

                    // Async Update data in cloud.db
                    new dbClearAndUpdate().execute(response);
                } else {
                    Log.e("JSON Response", "EMPTY");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        requestQueue.add(req);
    }


    public class dbClearAndUpdate extends AsyncTask<JSONArray, Void, Void> {

        DBHandler dataSource = new DBHandler(context, "cloud.db");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataSource.deleteAllStocks();
        }

        @Override
        protected Void doInBackground(JSONArray... params) {

            JSONArray res = params[0];
            Stock stock = new Stock();

            for (int i = 0; i < res.length(); i++) {
                try {
                    JSONObject jobj = res.getJSONObject(i);
                    Log.e("R from thread ", jobj.getString("itemname"));

                    stock.setItemName(jobj.getString("itemname"));
                    stock.setItemQuantity(jobj.getString("quantity"));
                    stock.setItemPrice(jobj.getString("price"));
                    stock.setCreatedOn(jobj.getString("createdon"));
                    stock.setCreatedBy(jobj.getString("createdby"));
                    stock.setModifiedOn(jobj.getString("modifiedon"));
                    stock.setModifiedBy(jobj.getString("modifiedby"));
                    stock.setObjectId(jobj.getString("_id"));

                    dataSource.createStock(stock);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCallback.onDBChanged();
        }
    }

    public interface onDBUpdateListener {
        public void onDBChanged();
    }

    public interface onOfflineDBUpdateListener{
        public void onOfflineDBChanged();
    }
}