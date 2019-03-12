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

public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    WebSettings webSettings;

    String URL = "http://bpbd.kupangkota.go.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        webView = (WebView) findViewById(R.id.web_view);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Untuk mengaktifkan javascript
        webSettings.getUseWideViewPort();

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Menampilkan loading ketika webview proses load halaman
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            // Ketika webview error atau selesai load page loading akan dismiss

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        webView.loadUrl(URL);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        // Untuk refresh webview dengan swipe
        webView.reload();
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
