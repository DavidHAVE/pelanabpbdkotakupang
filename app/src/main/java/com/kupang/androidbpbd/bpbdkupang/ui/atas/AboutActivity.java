package com.kupang.androidbpbd.bpbdkupang.ui.atas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.kupang.androidbpbd.bpbdkupang.R;

public class AboutActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(false);

        mWebView.loadUrl("file:///android_asset/about.html");
    }
}
