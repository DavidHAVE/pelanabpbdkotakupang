package com.kupang.androidbpbd.bpbdkupang.ui;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.Warning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WarningActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressDialog loadingDialog;

    private LinearLayout mEmptyView;

    private TextView mDateTextView, mTitleTextView, mInformationTextView;

    private String date, title, information;

    //Volley Request Queue
    private RequestQueue requestQueue;

    private int requestCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mInformationTextView = (TextView) findViewById(R.id.information_text_view);
        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        requestQueue = Volley.newRequestQueue(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setCancelable(true);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();

        warningRead();
    }


    private void warningRead() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_READ_PERINGATAN_DINI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OrderDetailActivity", "responseWarning : " + response);

                        if (response.equals("[]") || response.equals("")){
                            mSwipeRefreshLayout.setRefreshing(false);
                            loadingDialog.dismiss();
                            mEmptyView.setVisibility(View.VISIBLE);
                            return;
                        }else{
                            mEmptyView.setVisibility(View.GONE);
                        }

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject jsonWarning = obj.getJSONObject("peringatan_dini");

                                Warning warning = new Warning(
                                        jsonWarning.getString(Constants.TAG_WARNING_DATE),
                                        jsonWarning.getString(Constants.TAG_WARNING_TITLE),
                                        jsonWarning.getString(Constants.TAG_WARNING_INFORMATION)
                                );

                                date = warning.getTanggal();
                                title = warning.getJudul();
                                information = warning.getInformasi();

                                mDateTextView.setText(date);
                                mTitleTextView.setText(title);
                                mInformationTextView.setText(information);


                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        loadingDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        mDateTextView.setText("");
        mTitleTextView.setText("");
        mInformationTextView.setText("");
        warningRead();
    }
}
