package com.kupang.androidbpbd.bpbdkupang.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.TouchImageView;
import com.kupang.androidbpbd.bpbdkupang.model.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetilHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mDateTextView;
    private TouchImageView mPhotoTouchImageView;
    private EditText mLatitudeEditText, mLongitudeEditText, mAddressEditText, mDisasterTypeEditText, mInformationEditText;
    private Button mGotoLocationButton;
    private ImageView mTransparentImageView;
    private ScrollView mScrollView;
    private String date, latitude, longitude, address, disasterType, photo, information;
    private ProgressBar mProgressBar;
    private MapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private boolean isMapReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_history);


        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mPhotoTouchImageView = (TouchImageView) findViewById(R.id.photo_touch_image_view);
        mLatitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
        mLongitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
        mAddressEditText = (EditText) findViewById(R.id.address_edit_text);
        mDisasterTypeEditText = (EditText) findViewById(R.id.disaster_type_edit_text);
        mInformationEditText = (EditText) findViewById(R.id.information_edit_text);
        mGotoLocationButton = (Button) findViewById(R.id.goto_location_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mTransparentImageView = (ImageView) findViewById(R.id.transparent_image_view);
        mTransparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        mPhotoTouchImageView.setVisibility(View.GONE);

        if (this.getIntent() != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            date = getIntent().getExtras().getString(Constants.TAG_REPORT_DATE);
            latitude = getIntent().getExtras().getString(Constants.TAG_REPORT_LATITUDE);
            longitude = getIntent().getExtras().getString(Constants.TAG_REPORT_LONGITUDE);
            address = getIntent().getExtras().getString(Constants.TAG_REPORT_ADDRESS);
            disasterType = getIntent().getExtras().getString(Constants.TAG_REPORT_DISASTER_TYPE);
            information = getIntent().getExtras().getString(Constants.TAG_REPORT_INFORMATION);
            photo = getIntent().getExtras().getString(Constants.TAG_REPORT_PHOTO);

            mDateTextView.setText(date);

            Glide.with(this)
                    .load(Constants.URL_IMAGE_LAPORAN + photo)
                    .asBitmap()
                    //.placeholder(R.drawable.default_placeholder)
                    .override(400, 200) // Can be 2000, 2000
                    .into(new BitmapImageViewTarget(mPhotoTouchImageView) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            mPhotoTouchImageView.setImageBitmap(drawable);
                            mPhotoTouchImageView.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });


            mLatitudeEditText.setText(latitude);
            mLongitudeEditText.setText(longitude);
            mAddressEditText.setText(address);
            mDisasterTypeEditText.setText(disasterType);
            mInformationEditText.setText(information);


            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    isMapReady = true;
                    flyTo();
                    Log.e("DetailHistActivity", "isMapReady2 :" + isMapReady);
                }
            });

            Log.e("DetilHistoryActivity", "date : " + date + ", latitude :" + latitude + ", lobgitude :" + longitude + ", address :" +
                    address + ", disasterType :" + disasterType + ", photo :" + photo);
        }

        mGotoLocationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mGotoLocationButton) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(latitude), Double.parseDouble(longitude));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }

    private void flyTo() {

        double latitudeDouble = Double.parseDouble(latitude);
        double longtitudeDouble = Double.parseDouble(longitude);

        LatLng latLng = new LatLng(latitudeDouble, longtitudeDouble);

        Log.e("OrderHistoryDetActivity", "latLng :" + latLng);
        CameraPosition LOCATION = CameraPosition.builder()
                .target(latLng)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posisition");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mGoogleMap.addMarker(markerOptions);

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(LOCATION), 3000, null);
    }
}
