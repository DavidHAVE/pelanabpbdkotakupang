package com.kupang.androidbpbd.bpbdkupang.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.Report;
import com.kupang.androidbpbd.bpbdkupang.model.Warning;
import com.kupang.androidbpbd.bpbdkupang.ui.atas.AboutActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.atas.AccountActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.atas.GuideActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.autentikasi.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {
    public static final int MULTIPLE_PERMISSIONS = 10;
    //    protected double latitude, longtitude;
    public static LatLng latLng;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;

    LocationManager mLocationManager;

    GoogleMap mGoogleMap;

    boolean mapReady = false;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE};


    private LinearLayout mGuideLinearLayout, mAboutLinearLayout, mMyAccountLinearLayout, mReportLinearLayout;
    private CardView mWarningCardView, mWeatherCardView, mBookCardView, mNewsCardView, mTvCardView, mHistoryCardView, mChatCardView;
    private TextView mDateTextView, mClockTextView, mDisasterTypeTextView, mInformationTextView,
            mAddressTextView;
    private ImageButton mDisasterPositionImageButton, mInitialPositionImageButton;

    private LinearLayout mNewsLinearLayout;
    private ScrollView mScrollView;
    private ImageView mTransparentImageView;
    //////////
    private MapFragment mapFragment;
    private String latitude, longitude;
    private CameraPosition LOCATION;
    private MarkerOptions markerOptionsCurrentPosition;
    private String address;
    /////

    private String date, disasterType, information;
    //Volley Request Queue
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGuideLinearLayout = (LinearLayout) findViewById(R.id.guide_linear_layout);
        mAboutLinearLayout = (LinearLayout) findViewById(R.id.about_linear_layout);
        mMyAccountLinearLayout = (LinearLayout) findViewById(R.id.my_account_linear_layout);
        mReportLinearLayout = (LinearLayout) findViewById(R.id.report_linear_layout);
        mWarningCardView = (CardView) findViewById(R.id.warning_card_view);
        mWeatherCardView = (CardView) findViewById(R.id.weather_card_view);
        mBookCardView = (CardView) findViewById(R.id.book_card_view);
        mNewsCardView = (CardView) findViewById(R.id.news_card_view);
        mTvCardView = (CardView) findViewById(R.id.tv_card_view);
        mHistoryCardView = (CardView) findViewById(R.id.history_card_view);
        mChatCardView = (CardView) findViewById(R.id.chat_card_view);
        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mClockTextView = (TextView) findViewById(R.id.clock_text_view);
        mDisasterTypeTextView = (TextView) findViewById(R.id.disaster_type_text_view);
        mInformationTextView = (TextView) findViewById(R.id.information_text_view);
        mAddressTextView = (TextView) findViewById(R.id.address_text_view);
        mDisasterPositionImageButton = (ImageButton) findViewById(R.id.disaster_position_image_button);
        mInitialPositionImageButton = (ImageButton) findViewById(R.id.initial_position_image_button);
        mNewsLinearLayout = (LinearLayout) findViewById(R.id.news_linear_layout);

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

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapReady = true;
                mGoogleMap = googleMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Log.d("MapActivity", "onMapReady");

//
//                if (mGoogleMap != null && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                }
//                else
                if (mGoogleMap != null) {
                    setup();
                }
            }
        });
        buildGoogleApiClient();

        requestQueue = Volley.newRequestQueue(this);

        mGuideLinearLayout.setOnClickListener(this);
        mAboutLinearLayout.setOnClickListener(this);
        mMyAccountLinearLayout.setOnClickListener(this);
        mReportLinearLayout.setOnClickListener(this);
        mWarningCardView.setOnClickListener(this);
        mWeatherCardView.setOnClickListener(this);
        mBookCardView.setOnClickListener(this);
        mNewsCardView.setOnClickListener(this);
        mTvCardView.setOnClickListener(this);
        mHistoryCardView.setOnClickListener(this);
        mChatCardView.setOnClickListener(this);
        mDisasterPositionImageButton.setOnClickListener(this);
        mInitialPositionImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mGuideLinearLayout) {
            startActivity(new Intent(this, GuideActivity.class));
        } else if (v == mAboutLinearLayout) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (v == mMyAccountLinearLayout) {
            startActivity(new Intent(this, AccountActivity.class));
        } else if (v == mReportLinearLayout) {
            startActivity(new Intent(this, PelanaReportActivity.class));
        } else if (v == mWarningCardView) {
            startActivity(new Intent(this, WarningActivity.class));
        } else if (v == mWeatherCardView) {
            startActivity(new Intent(this, WeatherActivity.class));
        } else if (v == mBookCardView) {
            startActivity(new Intent(this, BookActivity.class));
        } else if (v == mNewsCardView) {
            startActivity(new Intent(this, NewsActivity.class));
        } else if (v == mTvCardView) {
            startActivity(new Intent(this, TVActivity.class));
        } else if (v == mHistoryCardView) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (v == mChatCardView) {
            openWhatsappContact("+6281339137853");
        } else if (v == mDisasterPositionImageButton) {
            flyTo(1);
        } else if (v == mInitialPositionImageButton) {
            flyTo(2);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(
                    this, LoginActivity.class));
        }

        getLatestReportData();
        checkPermissions();
        isGPSTurnOn();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                            Toast.makeText(this, "Akses di tolak", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    isGPSTurnOn();
                }
                return;
            }
        }
    }

    private void isGPSTurnOn() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }


        if (!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setMessage("GPS belum di aktifkan.");
            dialog.setPositiveButton("Buka Pengaturan Lokasi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                    return;
                }
            });
            dialog.show();
        }
    }


    private void getLatestReportData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_READ_LATEST_LAPORAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OrderDetailActivity", "response : " + response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject jsonReport = obj.getJSONObject("latest_laporan");


                                date = jsonReport.getString(Constants.TAG_REPORT_DATE);
                                latitude = jsonReport.getString(Constants.TAG_REPORT_LATITUDE);
                                longitude = jsonReport.getString(Constants.TAG_REPORT_LONGITUDE);
                                address = jsonReport.getString(Constants.TAG_REPORT_ADDRESS);
                                disasterType = jsonReport.getString(Constants.TAG_REPORT_DISASTER_TYPE);
                                information = jsonReport.getString(Constants.TAG_REPORT_INFORMATION);


                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("MainActivity1", "date :" + date + ", latitude :" + latitude + ", longitude :" + longitude);


                        StringTokenizer tk = new StringTokenizer(date);
                        String date = tk.nextToken();
                        String time = tk.nextToken();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
                        sdf.setTimeZone(tz);
                        sdf2.setTimeZone(tz);
                        Date dt1, dt2;
                        try {
                            dt1 = sdf.parse(date);
                            dt2 = sdf2.parse(time);
                            date = sdf.format(dt1);
                            time = sdf2.format(dt2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mDateTextView.setText(date);
                        mClockTextView.setText(time);
                        mAddressTextView.setText(address);
                        mDisasterTypeTextView.setText(disasterType);
                        mInformationTextView.setText(information);


                        flyTo(1);

                        mNewsLinearLayout.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
                mGoogleApiClient.disconnect();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setup() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;

            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Log.d("MapActivity", "lastLocation");
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            mGoogleMap.setMyLocationEnabled(false);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d("MapActivity", "buidGoogleApiClient");
    }

    private void flyTo(int locationType) {

        if (locationType == 1) {
            double latitudeDouble = Double.parseDouble(latitude);
            double longtitudeDouble = Double.parseDouble(longitude);

            LatLng latLng = new LatLng(latitudeDouble, longtitudeDouble);

            Log.e("OrderHistoryDetActivity", "latLng :" + latLng);
            LOCATION = CameraPosition.builder()
                    .target(latLng)
                    .zoom(16)
                    .bearing(0)
                    .tilt(0)
                    .build();

            MarkerOptions markerOptionsDisaster = new MarkerOptions();
            markerOptionsDisaster.position(latLng);
            markerOptionsDisaster.title("Disaster Posisition");
            markerOptionsDisaster.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mGoogleMap.addMarker(markerOptionsDisaster);

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(LOCATION), 3000, null);
        } else if (locationType == 2) {
            LOCATION = CameraPosition.builder()
                    .target(latLng)
                    .zoom(16)
                    .bearing(0)
                    .tilt(0)
                    .build();


            markerOptionsCurrentPosition = new MarkerOptions();
            markerOptionsCurrentPosition.position(latLng);
            markerOptionsCurrentPosition.title("Current Posisition");
            markerOptionsCurrentPosition.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mGoogleMap.addMarker(markerOptionsCurrentPosition);

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(LOCATION), 3000, null);

        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(800);
        mLocationRequest.setSmallestDisplacement(0.45F);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onReq uestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MapActivity", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("MapActivity", "Connection failed: ConnectionRewsult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude()); //you already have this

        Log.e("MapReportActivity", "Location Changed, latlng :" + latLng);

        if (markerOptionsCurrentPosition != null) {
            markerOptionsCurrentPosition = null;
            mGoogleMap.clear();
        }

        LOCATION = CameraPosition.builder()
                .target(latLng)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();

        markerOptionsCurrentPosition = new MarkerOptions();
        markerOptionsCurrentPosition.position(latLng);
        markerOptionsCurrentPosition.title("Current Posisition");
        markerOptionsCurrentPosition.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mGoogleMap.addMarker(markerOptionsCurrentPosition);

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    void openWhatsappContact(String number) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
