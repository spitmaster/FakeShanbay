package com.zhouyijin.zyj.fakeshanbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhouyijin.zyj.fakeshanbay.BaseActivity;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager.SharedPreferencesManager;

/**
 * Created by zhouyijin on 2016/10/23.
 */

public class GetTokenActivity extends BaseActivity {

    public static final String APP_KEY = "15b368d5abf11a3a2464";
    public static final String APP_SECRET = "a172f4142afacef1bfa1d7bdda79c356ef30c58b";


    public static final String AUTHORITATION = "https://api.shanbay.com/oauth2/authorize/?client_id=" + APP_KEY + "&response_type=token";
    public static final String flag = "success/#access_token=";

    //启动这个Activity的时候要把这个标识符发过来,证明目的是获取token
    public static final int GET_TOKEN = 2;
    public static final String GET_TOKEN_KEY = "get_token";


    private WebView wv_getToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_token);

        //如果非获取token的目的调用此Activity,那么直接结束掉这个Activity,不进行任何操作,以免浪费资源
        Intent intent = getIntent();
        int flag = intent.getIntExtra(GET_TOKEN_KEY, 0);
        if (flag != GET_TOKEN) {
            finish();
        }

        intiView();
        wv_getToken.loadUrl(AUTHORITATION);
    }


    private void intiView() {
        wv_getToken = (WebView) findViewById(R.id.wv_get_token);
        wv_getToken.getSettings().setJavaScriptEnabled(true);
        wv_getToken.setWebViewClient(new WebViewClient() {
            /**
             * 经测验,webkit根本不会调用这个方法,真他妈坑!
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                if (url.contains(flag)) {
                    int indexStart = url.lastIndexOf(flag) + flag.length();
                    int indexEnd = url.lastIndexOf("&token_type=");
                    //扇贝的token是30位字符
                    String token = url.substring(indexStart, indexEnd);
                    //将token保存到sharedPreferences中
                    SharedPreferencesManager.getInstance().setToken(token);
                    Intent intent = new Intent();
                    intent.putExtra(GET_TOKEN_KEY, token);
                    setResult(RESULT_OK, intent);
                    view.clearCache(true);
                    view.destroy();
                    finish();
                }
                return false;
            }

            /**
             * 经测试,获取token后只会回调这个方法!妈的居然还过时了额
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if (url.contains(flag)) {
                    int indexStart = url.lastIndexOf(flag) + flag.length();
                    int indexEnd = url.lastIndexOf("&token_type=");
                    //扇贝的token是30位字符
                    String token = url.substring(indexStart, indexEnd);
                    //将token保存到sharedPreferences中
                    SharedPreferencesManager.getInstance().setToken(token);
                    Intent intent = new Intent();
                    intent.putExtra(GET_TOKEN_KEY, token);
                    setResult(RESULT_OK, intent);
                    view.clearCache(true);
                    view.destroy();
                    finish();
                }
                return false;
            }
        });
        wv_getToken.setWebChromeClient(new WebChromeClient() {


        });


    }

}
