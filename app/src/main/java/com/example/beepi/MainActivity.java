package com.example.beepi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.beepi.model.Car;
import com.example.beepi.model.CarsResponse;
import com.example.beepi.network.CarsRequest;
import com.example.beepi.util.ViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, CarsAdapter.CarListener {

    private static final String QUERY = "query";

    private CarsAdapter mAdapter;
    private Toolbar mToolbar;
    @Bind(R.id.loading) ContentLoadingProgressBar mLoading;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private String mQuery;
    private SearchView mSearchView;
    private Request mQueryRequest;

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mSearchView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppHandles.initInstance(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        mLoading.hide();

        mQuery = (savedInstanceState != null) ? savedInstanceState.getString(QUERY) : null;
        mAdapter = new CarsAdapter();
        mAdapter.setListener(this);

        initRecyclerView();

        searchCars("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(QUERY, mQuery);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        mSearchView.setOnQueryTextListener(this);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        mSearchView.setQuery(mQuery, false);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRecyclerView.removeOnScrollListener(mScrollListener);
        if (mQueryRequest != null) {
            mQueryRequest.cancel();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // no-op
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.equals(query, mQuery)) {
            searchCars(query);
        }
        return true;
    }

    private void searchCars(String query) {
        if (mQueryRequest != null) {
            mQueryRequest.cancel();
        }

        mLoading.show();

        mQuery = query;
        mQueryRequest = new CarsRequest(mQuery,
                new Listener<CarsResponse>() {
                    @Override
                    public void onResponse(CarsResponse response) {
                        mQueryRequest = null;
                        mLoading.hide();

                        mAdapter.setCars(response.carsOnSale);
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mQueryRequest = null;
                        mLoading.hide();

                        String msg = error.getLocalizedMessage();
                        if (TextUtils.isEmpty(msg)) {
                            if (error instanceof TimeoutError) {
                                msg = getString(R.string.error_timeout);
                            } else {
                                msg = getString(R.string.error_network);
                            }
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage(msg)
                                .setPositiveButton(android.R.string.ok, null)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                }
        );

        AppHandles.getInstance().getRequestQueue().add(mQueryRequest);
    }

    @Override
    public void onCarClick(Car car) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://www.beepi.com" + car.carPageUrl));
        startActivity(intent);
    }

    private void initToolbar() {
        mToolbar = ViewUtil.findViewById(this, R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initRecyclerView() {
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

}
