package com.kupang.androidbpbd.bpbdkupang.ui.atas;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.kupang.androidbpbd.bpbdkupang.R;

public class GuideActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(false);

        mWebView.loadUrl("file:///android_asset/guide.html");
    }
}
