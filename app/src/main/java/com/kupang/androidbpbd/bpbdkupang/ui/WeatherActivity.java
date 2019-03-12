package com.kupang.androidbpbd.bpbdkupang.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.TouchImageView;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.Warning;
import com.kupang.androidbpbd.bpbdkupang.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView mDateTextView, mInformationTextView;
    private TouchImageView mWeatherTouchImageView;
    private LinearLayout mEmptyView;

    private ProgressDialog loadingDialog;

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String date, photo, information;

    //Volley Request Queue
    private RequestQueue requestQueue;

    private int requestCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mWeatherTouchImageView = (TouchImageView) findViewById(R.id.weather_touch_image_view);
        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);
        mInformationTextView = (TextView) findViewById(R.id.information_text_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
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

        mProgressBar.setVisibility(View.VISIBLE);
        weatherRead();
    }

    private void weatherRead() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_READ_PRAKIRAAN_CUACA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OrderDetailActivity", "responseWeather : " + response);

                        if (response.equals("[]") || response.equals("")){
                            mProgressBar.setVisibility(View.GONE);
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
                                JSONObject jsonWarning = obj.getJSONObject("prakiraan_cuaca");

                                Weather weather = new Weather(
                                        jsonWarning.getString(Constants.TAG_WEATHER_DATE),
                                        jsonWarning.getString(Constants.TAG_WEATHER_INFORMATION),
                                        jsonWarning.getString(Constants.TAG_WEATHER_IMAGE)
                                );

                                view(weather);

                            } else {
                                mProgressBar.setVisibility(View.GONE);
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
                        mProgressBar.setVisibility(View.GONE);
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void view(Weather weather) {
        date = weather.getTanggal();
        information = weather.getInformasi();
        photo = weather.getFoto();

        mDateTextView.setText(date);
        mInformationTextView.setText(information);


        Glide.with(this).load(Constants.URL_IMAGE + photo)
                .asBitmap()
                .override(400, 200) // Can be 2000, 2000
                .into(new BitmapImageViewTarget(mWeatherTouchImageView) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        mProgressBar.setVisibility(View.GONE);
                        loadingDialog.dismiss();
                        mWeatherTouchImageView.setImageBitmap(drawable);
                        mWeatherTouchImageView.setVisibility(View.VISIBLE);
                    }


                });




    }

    @Override
    public void onRefresh() {
        mDateTextView.setText("");
        mWeatherTouchImageView.setImageResource(0);
        mInformationTextView.setText("");
        weatherRead();
    }
}
