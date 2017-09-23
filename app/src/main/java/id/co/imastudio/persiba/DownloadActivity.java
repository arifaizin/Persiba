package id.co.imastudio.persiba;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static id.co.imastudio.persiba.R.id.webView;

public class DownloadActivity extends AppCompatActivity {

    private WebView web;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        final String linkurl = getIntent().getStringExtra("URL");

        web = (WebView) findViewById(webView);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);


        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDisplayZoomControls(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDomStorageEnabled(true);

        web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (isNetworkConnected()) {
            loadUrl(linkurl);
        } else {

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Internet Connection", Toast.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadUrl(linkurl);
                        }
                    }).show();

        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                web.reload();
            }
        });
    }

    private void loadUrl(String linkurl) {
        web.loadUrl(linkurl);

        web.setWebViewClient(new WebViewClient() {
                                 @Override
                                 public void onPageFinished(WebView view, String url) {
                                     super.onPageFinished(view, url);
                                     getSupportActionBar().setTitle(web.getTitle());
                                 }

                                 @Override
                                 public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                     super.onReceivedSslError(view, handler, error);
                                     handler.proceed();
                                 }

                                 @Override
                                 public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                     view.loadUrl(url);
                                     if (url.endsWith(".pdf")) {
                                         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                         // if want to download pdf manually create AsyncTask here
                                         // and download file
                                         return true;
                                     }
                                     return false;
                                 }

                             }

        );
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
