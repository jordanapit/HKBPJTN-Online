package com.hkbp.jtn.online;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private WebView webviewku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webviewku = (WebView)findViewById(R.id.WebView1);

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
        webviewku.setWebViewClient(new WebViewClient());
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
}
