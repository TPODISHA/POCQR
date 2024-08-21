package com.tpcodl.samplehimu;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tpcodl.mylibdemo.MyLibrary;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class WebViewActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_mdm_data;
    WebView web_view;
    String caNumber="";
    String headerTitle="";
    private ProgressDialog progressdialog;
    private Context mContext;
    private String encriptedConsumerNumber="";
    private String url="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        iv_back=findViewById(R.id.iv_back);

        mContext = this;
        progressdialog = new ProgressDialog(mContext);

        tv_mdm_data=findViewById(R.id.tv_mdm_data);
        web_view=findViewById(R.id.webview);

        try {
            Intent intent=getIntent();


            caNumber=intent.getStringExtra("caNumber");
            headerTitle=intent.getStringExtra("headerTitle");

        }catch (Exception ex){
            ex.printStackTrace();
            caNumber="80000002735";
            headerTitle="MDM DATA";
        }

       // caNumber="80000002735";



       new  TestAsync().execute();


        tv_mdm_data.setText(headerTitle);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        web_view.getSettings().setDomStorageEnabled(true);
        web_view.getSettings().setSupportMultipleWindows(false);
        web_view.getSettings().setMediaPlaybackRequiresUserGesture(false);
        web_view.getSettings().setSaveFormData(true);
        web_view.getSettings().setAllowContentAccess(true);
        web_view.getSettings().setAllowFileAccess(false);
        web_view.getSettings().setAllowFileAccessFromFileURLs(false);
        web_view.getSettings().setAllowUniversalAccessFromFileURLs(false);
        web_view.getSettings().setBuiltInZoomControls(true);
        web_view.getSettings().setDisplayZoomControls(false);

        web_view.setWebViewClient(new WebViewClient());

        web_view.setClickable(true);


        web_view.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType(mimetype);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                        mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File",
                        Toast.LENGTH_LONG).show();
                Log.d("dsfgh", "onDownloadRequested: " + url);
            }
        });



        //  url = "https://portal.tpcentralodisha.com:4114/tpcodl-bill-view/getPrepaidInfoHtml?caEnc="+encriptedConsumerNumber;

        // url = "https://javaqas.tpcentralodisha.com/tpcodl-bill-view-qas/getPrepaidInfoHtml?caEnc="+encriptedConsumerNumber;









        web_view.getSettings().setTextZoom(100);



    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume () {
        super.onResume();

        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause () {
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy () {
        // web_view.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        //  web_view.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed () {

        // ...
        super.onBackPressed();
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressdialog.dismiss();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // System.out.println("sdfgdf=="+url);

          /*  if (!isFirstTime){
              Toast.makeText(PaymentConsumptionSmartActivity.this,"Download is in progress..please wait..",Toast.LENGTH_SHORT).show();
            }
            isFirstTime=false;*/

            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                progressdialog.dismiss();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            if (caNumber!=null && (!caNumber.equalsIgnoreCase(""))){
                try {
                    try {
                        encriptedConsumerNumber= URLEncoder.encode(MyLibrary.openWebView(mContext,caNumber), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } catch (Exception  e) {
                    e.printStackTrace();
                }
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);

            url = "https://portal.tpcentralodisha.com:4114/tpcodl-bill-view/getPrepaidInfoHtml?caEnc="+encriptedConsumerNumber;

           // url = "https://www.google.com/";




            try {
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();

            }catch (Exception ex){
                ex.printStackTrace();
            }

            if (isNetworkConnected()){
                web_view.loadUrl(url);
              //  web_view.loadDataWithBaseURL("",url,"text/html", "UTF-8","");
            }
            else {
                Toast.makeText(mContext,"No internet connection,please connect to internet!",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
