package com.kupang.androidbpbd.bpbdkupang.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.adapter.ReportAdapter;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.model.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout mEmptyView;
    private ProgressBar mProgressBar;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    //Creating a List
    private List<Report> listReport;
    //Creating Views
    private RecyclerView mOrderHistoryRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReportAdapter reportAdapter;
    //Volley Request Queue
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initializing Views
        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mOrderHistoryRecyclerView = (RecyclerView) findViewById(R.id.report_history_recycler_view);
        mOrderHistoryRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mOrderHistoryRecyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listReport = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Log.e("OrderHistoryFragment", "OrderHistoryFragment");

        //initializing our adapter
        reportAdapter = new ReportAdapter(listReport, this);

        //Adding adapter to recyclerview
        mOrderHistoryRecyclerView.setAdapter(reportAdapter);

        if (listReport.isEmpty()) {
            mOrderHistoryRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mOrderHistoryRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        reportAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                mOrderHistoryRecyclerView.setVisibility(reportAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
                mEmptyView.setVisibility(reportAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        listReport.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private JsonArrayRequest getDataFromServer() {

        final int userId = SharedPrefManager.getInstance(this).getUser().getUserId();

        Log.e("HistoryActivity", "userId ="+userId);
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constants.URL_READ_HISTORY_LAPORAN + userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        Log.e("HistoryActivity", "response :" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
//                        Toast.makeText(HistoryActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData() {
        getDataFromServer().setRetryPolicy(new DefaultRetryPolicy(6000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getDataFromServer());

        Log.e("HistoryActivity", "getData");
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        Log.e("HistoryActivity", "array length =" + array.length());
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            Report report = new Report();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                Log.e("HistoryActivity", "json =" + json);


                //Adding data to the superhero object
                report.setReportId(json.getInt(Constants.TAG_REPORT_ID));
                report.setDate(json.getString(Constants.TAG_REPORT_DATE));
                report.setLatitude(json.getString(Constants.TAG_REPORT_LATITUDE));
                report.setLongitude(json.getString(Constants.TAG_REPORT_LONGITUDE));

                report.setAddress(json.getString(Constants.TAG_REPORT_ADDRESS));
                report.setDisasterType(json.getString(Constants.TAG_REPORT_DISASTER_TYPE));

                report.setPhoto(json.getString(Constants.TAG_REPORT_PHOTO));
                report.setInformation(json.getString(Constants.TAG_REPORT_INFORMATION));

                report.setUserId(json.getString(Constants.TAG_REPORT_USER_ID));

                Log.e("HistoryActivity", "json report id =" + json.getInt(Constants.TAG_REPORT_ID) + ", date :" + json.getString(Constants.TAG_REPORT_DATE) +
                        ", latitude :" + json.getString(Constants.TAG_REPORT_LATITUDE) + ", longtitude :" + json.getString(Constants.TAG_REPORT_LONGITUDE) +
                        ", address :" + json.getString(Constants.TAG_REPORT_ADDRESS) + ", disaster Type :" + json.getString(Constants.TAG_REPORT_DISASTER_TYPE) +
                        ", photo :" + json.getString(Constants.TAG_REPORT_PHOTO) +", information :" + json.getString(Constants.TAG_REPORT_INFORMATION) +
                        ", userId :" + json.getString(Constants.TAG_REPORT_USER_ID));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            listReport.add(report);
        }

        //Notifying the adapter that data has been added or changed
        reportAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        listReport.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        getData();
    }
}
