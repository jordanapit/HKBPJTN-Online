package com.hkbp.jtn.online;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private WebView webviewku;

    RelativeLayout errorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webviewku = (WebView)findViewById(R.id.WebView1);
        errorLayout = (RelativeLayout)findViewById(R.id.errorLayout);

        WebSettings websettingku = webviewku.getSettings();

        webviewku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webviewku.setLongClickable(false);

        webviewku.setHapticFeedbackEnabled(false);

        webviewku.getSettings().setJavaScriptEnabled(true);
        webviewku.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webviewku.loadUrl("https://www.hkbpjtn.online/");
        webviewku.setWebViewClient(new myWebClient());






    }

    @Override
    public void onBackPressed() {
        if(webviewku.canGoBack()) {
            webviewku.goBack();
        } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Apakah anda yakin ingin keluar?");
                builder.setCancelable(true);
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();}


    }

    public class myWebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            errorLayout.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        }
    }
}
