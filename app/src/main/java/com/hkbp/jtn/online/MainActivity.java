package com.hkbp.jtn.online;

//SEBELUMNYA, UNTUK SEMUA YANG BACA CODING INI, SEMUA CODING YANG ADA DI SINI ADALAH HASIL COPPAS DARI WEBSITE.
//SAYA, JORDAN NAPITUPULU, BUKAN PROGRAMMER, SEHINGGA TIDAK TERLALU PAHAM MENGENAI CODING DI SINI.
//JIKA TEMAN-TEMAN KONTRIBUTOR INGIN MEMBUANG CODE YANG USELESS, MONGGO SILAHKAN.
//TAPI JANGAN LUPA KASIH TAU APA YANG KALIAN HAPUS. MAULIATE.

//ANYWAY, TADINYA INI MAINACTIVITY, NAMUN SUDAH DICONVERT MENJADI FRAGMENT KARENA MAU GAMAU WEBVIEW MENJADI
//FRAGMENT (BUKAN ACTIVITY UTAMA) SETELAH APLIKASI INI DITAMBAHKAN BOTTOM NAVIGATION BAR.

//APA AJA YANG PERLU DIIMPORT DI JAVA INI

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.view.View.GONE;

//CODINGNYA MULAI DARI SINI
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //NGASIH PERIJINAN DI AWAL UNTUK CEK PERMISSION
    @RequiresApi(api = Build.VERSION_CODES.P)
    private static final int MY_PERMISSION_REQUEST_CODE = 123;

    //MENENTUKAN IDENTITAS
    WebView webviewku;
    WebSettings websettingku;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRootLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FrameLayout mFrameSettings;


    private BottomNavigationView mMainNav;

    //CODING DI DALAM
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        mFrameSettings = (FrameLayout) findViewById(R.id.frame_settings);


        //NGASIH TAU YANG MANA YANG ADA DI LAYOUT
        webviewku = (WebView)findViewById(R.id.WebView1);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        //NGATUR WEBSETTING DAN WEBVIEW
        websettingku = webviewku.getSettings();
        websettingku.setJavaScriptEnabled(true);
        websettingku.setAllowFileAccess(true);
        websettingku.setAppCacheEnabled(true);
        websettingku.setLoadsImagesAutomatically(true);
        websettingku.setDomStorageEnabled(true);
        websettingku.setLoadWithOverviewMode(true);
        websettingku.setUseWideViewPort(true);

        //NAVBAR FRAME
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        webviewku.setVisibility(View.VISIBLE);
                        mFrameSettings.setVisibility(GONE);
                        String url = webviewku.getUrl();
                        if (url.contains("https://hkbpjtn.web.id/mobile_app/")) {
                        return true;
                    }
                    else{
                        webviewku.setVisibility(View.VISIBLE);
                        mFrameSettings.setVisibility(GONE);
                        webviewku.loadUrl("https://hkbpjtn.web.id/mobile_app/");
                        return true;
                    }

                    case R.id.nav_regist:
                        webviewku.setVisibility(View.VISIBLE);
                        mFrameSettings.setVisibility(GONE);
                        String url2 = webviewku.getUrl();
                        if (url2.contains("https://hkbpjtn.web.id/mobile_app/#registrasi")) {
                        return true;
                    }
                    else{
                        webviewku.setVisibility(View.VISIBLE);
                        mFrameSettings.setVisibility(GONE);
                        webviewku.loadUrl("https://hkbpjtn.web.id/mobile_app/#registrasi");
                        return true;
                    }

                    case R.id.nav_settings:
                        mFrameSettings.setVisibility(View.VISIBLE);
                        webviewku.setVisibility(View.GONE);
                        return true;



                    case R.id.nav_exit:
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
                        alertDialog.show();


                    default:
                        return false;

                }

            }


        });

        //WEB VIEW CLIENT
        webviewku.setWebViewClient(new WebViewClient() {
            //UNTUK TANDA APAKAH HPNYA ADA KONEKSI INTERNET ATAU ENGGAK
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                Log.d("error code", " " + String.valueOf(errorCode));

                if (errorCode == -2)                                                                //errorCode 2 -> Server or proxy hostname lookup failed (no connection)
                    // show to the user whatever you want to show, for example, a local html page:
                    view.loadUrl("file:///android_asset/index.html");
            }

            //INI JUGA UNTUK KONEKSI MATI ATAU ENGGA, TAPI KURANG COCOK
            /*public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }

                if (webView.canGoBack()) {
                    webView.goBack();
                }

                webView.loadUrl("about:blank");

                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Anda tidak memiliki koneksi internet, atau halaman gagal dimuat. Pilih 'Refresh' untuk kembali ke Halaman Utama.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Refresh", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });
                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }*/

            //BIKIN YOUTUBE APP KEBUKA
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                if(url.startsWith("vnd.youtube:") || url.startsWith("mailto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }


        });

        //UNTUK NGASIH TAU DI WEBVIEWNYA BOLEH/GAK BOLEH NGAPAIN AJA
        webviewku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webviewku.setLongClickable(false);
        webviewku.setHapticFeedbackEnabled(false);

        //INI KUNCINYA! NGASIH TAU WEBVIEWNYA HARUS BUKA APA. KALAU KELAK WEBNYA HKBPJTN GANTI DOMAIN, INI JUGA HARUS DIGANTI
        webviewku.loadUrl("https://hkbpjtn.web.id/mobile_app/");

        //KEMAMPUAN UNTUK MENDOWNLOAD FILE; SEMENTARA WAKTU PDF WARTA KEUANGAN/WARTA JEMAAT MUSTI DIDOWNLOAD
        webviewku.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String s, final String s1, final String s2, final String s3, long I) {
                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                                request.setMimeType(s3);
                                String cookies = CookieManager.getInstance().getCookie(s1);
                                request.addRequestHeader("cookie",cookies);
                                request.addRequestHeader("User-Agent",s1);
                                request.setDescription("Sedang mengunduh file");
                                request.setTitle(URLUtil.guessFileName(s,s2,s3));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(s,s2,s3));
                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                    Toast.makeText(MainActivity.this, "Sedang mengunduh file", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });



        //NGASIH IDENTITAS UNTUK CODING YOUTUBE BISA FULLSCREEN + TAMPILIN CIRCLE LOADING. CODING FULLSCREENNYA ADA DI BAWAH
        webviewku.setWebChromeClient(new myChrome() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress < 100){
                    progressBar.setVisibility(View.VISIBLE);
                }
                if(newProgress==100){
                    progressBar.setVisibility(GONE);
                }

            }
        });

        //SWIPE REFRESH
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    //INI UNTUK NGASIH PESAN KALAU KITA MAU KELUAR DARI APP
    @Override
    public void onBackPressed() {
        String isiannya = webviewku.getUrl();
            if (isiannya.matches("https://hkbpjtn.web.id/mobile_app/#")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
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
                alertDialog.show();
            }
            else {
        if(webviewku.canGoBack()) {
            webviewku.goBack();
        }



            else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
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
                alertDialog.show();

            }
        }
    }

    //INI UNTUK CHECK PERMISSION, APAKAH READ/WRITE STORAGENYA DIPERBOLEHKAN
    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Write external storage permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.P)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            mActivity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            } else {
                // Permission already granted
            }
        }
    }

    //INI CODING UNTUK YOUTUBE BISA FULLSCREEN
    private class myChrome extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        //protected FrameLayout mFullscreenContainer;
        //private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        myChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

    //SWIPE REFRESH
    @Override
    public void onRefresh() {
        webviewku.reload();
        swipeRefreshLayout.setRefreshing(false);
    }










}
