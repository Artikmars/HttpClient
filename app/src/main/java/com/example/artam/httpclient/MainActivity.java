package com.example.artam.httpclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myTag";
    Button button;
    EditText editText;
    private WebView wv1;
    private String concatUrl;
    boolean connectionIsValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkConnection(connectionIsValid);
                if (connectionIsValid){
                if (editText.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your website!", Toast.LENGTH_SHORT).show();
            }
            else {
                String url = editText.getText().toString().trim();
                boolean checkUrl = isValidUrl(url);
                if (checkUrl == true) {                                        //source?
                    final String testhttp = url.substring(0, 4);
                    if (!testhttp.equals("http")) {
                        String http = "http://";
                        concatUrl = http.concat(url);
                    }
                    wv1.getSettings().setLoadsImagesAutomatically(true);
                    wv1.getSettings().setJavaScriptEnabled(true);
                    wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    wv1.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            // Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), concatUrl + " doesn't exist", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (concatUrl.isEmpty()) {
                        wv1.loadUrl(url);
                    } else
                        wv1.loadUrl(concatUrl);

                } else {
                    Toast.makeText(getApplicationContext(), "Please give a valid URL",
                            Toast.LENGTH_LONG).show();
                    wv1.clearView();
                }

            }
            }}
        });
    }
    private class MyBrowser extends WebViewClient {  //http://stackoverflow.com/questions/7772409/set-loadurltimeoutvalue-on-webview
        boolean timeout;
        public MyBrowser(){
            timeout = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(timeout){
                        Toast.makeText(getApplicationContext(), "Timeout error",
                                Toast.LENGTH_SHORT).show();
                    }
               }
           }).start();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public boolean isValidUrl(String url) { //source???
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkConnection(boolean a){ //https://dzone.com/articles/checking-an-internet-connection-in-android-2
        if(isOnline()){
           return connectionIsValid = true;
        }else{
            Toast.makeText(getApplicationContext(), "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
            return connectionIsValid = false;

        }
    }
}

