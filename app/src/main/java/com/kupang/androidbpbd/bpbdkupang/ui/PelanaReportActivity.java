package com.kupang.androidbpbd.bpbdkupang.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.kupang.androidbpbd.bpbdkupang.R;

public class PelanaReportActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView mCallCardView, mReportCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_pelana);

        mCallCardView = (CardView) findViewById(R.id.call_card_view);
        mReportCardView = (CardView) findViewById(R.id.report_card_view);

        mCallCardView.setOnClickListener(this);
        mReportCardView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mCallCardView){
            call();
        }else if(v == mReportCardView){
            startActivity(new Intent(this, MapReportActivity.class));
        }
    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0830820382"));

        if (ActivityCompat.checkSelfPermission(PelanaReportActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}
