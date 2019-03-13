package com.rakhmat.androidgithubuserssearch.Activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rakhmat.androidgithubuserssearch.Fragment.FragmentResult;
import com.rakhmat.androidgithubuserssearch.R;

public class MainActivity extends AppCompatActivity {
    private final String clientId = "37de96e20fe5c60832c8";
    private final String clientSecret = "7b9c286fd096c8bfd347c758a08c679dcc771514";
    private final String redirectUri = "usergithubfinder://callback";
    private WebView web;
    private Dialog auth_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth_dialog = new Dialog(MainActivity.this);
        auth_dialog.setContentView(R.layout.auth_dialog);
        web = (WebView)auth_dialog.findViewById(R.id.web_view);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&scope=repo&redirect_uri=" + redirectUri);
        web.setWebViewClient(new WebViewClient() {

            boolean authComplete = false;
            Bundle bundle = new Bundle();
            FragmentResult fragmentResult = new FragmentResult();

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    String authCode = uri.getQueryParameter("code");
                    //Log.i("", "CODE : " + authCode);
                    authComplete = true;
                    bundle.putString("code", authCode);
                    bundle.putString("client_id", clientId);
                    bundle.putString("client_secret", clientSecret);
                    fragmentResult.setArguments(bundle);

                    loadFragment(fragmentResult);

                    auth_dialog.dismiss();
                    //Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
                }else if(url.contains("error=access_denied")){
                    Log.i("", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    //Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                    auth_dialog.dismiss();
                }
            }
        });
        auth_dialog.show();
        auth_dialog.setTitle("Authorize");
        auth_dialog.setCancelable(true);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }
}
