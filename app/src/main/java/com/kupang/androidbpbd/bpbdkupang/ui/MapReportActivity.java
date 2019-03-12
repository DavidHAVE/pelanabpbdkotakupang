package com.kupang.androidbpbd.bpbdkupang.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.model.Report;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapReportActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {


    public static LatLng latLng;
    public static String LATITUDE = "";
    public static String LONGITUDE = "";
    public static String ADDRESS = "";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected CameraPosition PLACE = null;
    LocationManager mLocationManager;
    GoogleMap gMap;
    Marker currLocationMarker;
    GoogleMap receptionGMap;

    boolean mapReady = false;
    Geocoder geocoder;
    List<android.location.Address> addresses;

    private MapFragment mapFragment;
    private ImageView mCurrentPositionImageView;
    private Button mNextButton;
    private MarkerOptions markerOptions;
    private String district, city, country, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_report);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mCurrentPositionImageView = (ImageView) findViewById(R.id.current_position_image_view);
        mNextButton = (Button) findViewById(R.id.next_button);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapReady = true;
                gMap = googleMap;
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Log.d("MapReportActivity", "onMapReady");


                if (gMap != null && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    finish();
                } else if (gMap != null) {
                    setup();
                }
            }
        });
        buildGoogleApiClient();

        mCurrentPositionImageView.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mCurrentPositionImageView) {
            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected()) {
                    flyTo(latLng);
                }
            }
        } else if (view == mNextButton) {
            if (city.equals("Special Region of Yogyakarta") || city.equals("Daerah Istimewa Yogyakarta")) {
                Log.e("MapReportActivity", "cocok");
                Intent intent = new Intent(this, SendReportActivity.class);
                startActivity(intent);

                Log.e("MapReportActivity", "latitude :" + LATITUDE + ", longitude :" + LONGITUDE + ", address :" + ADDRESS);
            } else {
                Log.e("MapReportActivity", "tidak cocok");
                Toast.makeText(this, "Bukan di wilayah Yogyakarta", Toast.LENGTH_LONG).show();
            }
        }
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
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            Log.d("MapReportActivity", "lastLocation");
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            gMap.setMyLocationEnabled(false);
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Hidupkan GPS")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Log.d("MapReportActivity", "buidGoogleApiClient");
    }

    private void flyTo(LatLng latLng) {

        CameraPosition LOCATION = CameraPosition.builder()
                .target(latLng)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();

        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(LOCATION), 3000, null);
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


        if (markerOptions != null) {
            markerOptions = null;
            gMap.clear();
        }

        Log.e("MapReportActivity", "latlng :"+latLng+", latitude :" + location.getLatitude() + ", longtitude :" + location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Posisition");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        currLocationMarker = gMap.addMarker(markerOptions);

        flyTo(latLng);

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }


        ADDRESS = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        district = addresses.get(0).getLocality();
        city = addresses.get(0).getAdminArea();
        country = addresses.get(0).getLocality();
        postalCode = addresses.get(0).getPostalCode();

        Log.e("MapReportActivity1", "Save address :" + ADDRESS + ", district :" + district + ", city :" + city + ", country :" + country + ", postalCode :" + postalCode);


        if (city.equals("Special Region of Yogyakarta") || city.equals("Daerah Istimewa Yogyakarta")) {
            LATITUDE = String.valueOf(latLng.latitude);
            LONGITUDE = String.valueOf(latLng.longitude);

            Report report = new Report(LATITUDE, LONGITUDE, ADDRESS);
            SharedPrefManager.getInstance(this).saveReport(report);
            Log.e("MapReportActivity2", "Save address :" + ADDRESS + ", district :" + district + ", city :" + city + ", country :" + country + ", postalCode :" + postalCode);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

}
