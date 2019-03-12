package com.kupang.androidbpbd.bpbdkupang.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kupang.androidbpbd.bpbdkupang.R;

public class BookActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    WebSettings webSettings;

    String URL = "https://docs.google.com/viewer?url=" + "https://siaga.bnpb.go.id/hkb/po-content/uploads/documents/Buku_Saku-10Jan18_FA.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        webView = (WebView) findViewById(R.id.web_view);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Untuk mengaktifkan javascript
        webSettings.getUseWideViewPort();

        webView.loadUrl(URL);

    }

    @Override
    public void onBackPressed() {
        // Jika Webview bisa di back maka backward page sebelumnya
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
            System.exit(0);
        }
    }

}
