package com.example.probook.stocksynceditor.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.probook.stocksynceditor.R;
import com.example.probook.stocksynceditor.controller.NetworkCommunicator;


public class MainActivity extends ActionBarActivity implements NetworkCommunicator.onDBUpdateListener, NetworkCommunicator.onOfflineDBUpdateListener{

    private CollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        // Setting ViewPager
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e("Pager: ", String.valueOf(position));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDBChanged() {
        CloudListFragment frag = (CloudListFragment) mCollectionPagerAdapter.getRegisteredFragment(1);
        frag.dbUpdated();
    }

    @Override
    public void onOfflineDBChanged() {
        OfflineListFragment frag = (OfflineListFragment) mCollectionPagerAdapter.getRegisteredFragment(0);
        frag.dbUpdated();
    }
}
