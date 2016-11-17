package com.zhouyijin.zyj.fakeshanbay.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;

import java.io.InputStream;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>
 * this class encapsulate the OkHttp's function
 */

public class NetworkConnection {

    private HttpConnection httpConnection;

    private NetworkConnection() {
        httpConnection = VolleyConnection.getInstance();
    }

    /**
     * 向指定的url请求字符串,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void getString(String url, final OnStringResponded callback) {
        httpConnection.requestUrlAsyncTaskForString(url, callback);
    }

    /**
     * 向指定的url请求JSON,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void getWordBean(final String url, final OnWordBeanResponded callback) {
        httpConnection.requestUrlAsyncTaskForWordBean(url, callback);
    }


    /**
     * 向指定的url请求其他数据,以byte[]形式返回,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void getByteArray(String url, final OnByteArrayResponded callback) {
        httpConnection.requestUrlAsyncTaskForByteArray(url, callback);
    }

    public void getDailySentenceBean(String url, final OnDailySentenceBeanResponded callback) {
        httpConnection.requestUrlAsyncTaskForDailySentenceBean(url, callback);
    }

    public void getExampleSentenceBean(String url, final OnExampleSentenceBeanResponded callback) {
        httpConnection.requestUrlAsyncTaskForExampleSentenceBean(url, callback);
    }


    public interface OnStringResponded {
        void onStringResponded(String result);
    }

    public interface OnByteArrayResponded {
        void onByteArrayResponded(byte[] result);
    }

    public interface OnWordBeanResponded {
        void onWordBeanResponded(WordBean result);
    }

    public interface OnInputStreamResponded {
        void onInputStreamResponded(InputStream is);
    }

    public interface OnDailySentenceBeanResponded {
        void onDailySentenceBeanResponded(DailySentenceBean bean);
    }

    public interface OnExampleSentenceBeanResponded {
        void onExampleSentenceBeanResponded(ExampleSentenceBean bean);
    }


    public static NetworkConnection getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static final NetworkConnection instance = new NetworkConnection();

    }

    //如果联网了,返回true,没联网返回false
    public static boolean isNetworkConnected() {
        //先判断有没有联网权限
        if (ContextCompat.checkSelfPermission(
                MyApplication.getContext(),
                Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_DENIED
                ) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }


    public interface HttpConnection {
        void requestUrlAsyncTaskForString(String url, final NetworkConnection.OnStringResponded callback);

        void requestUrlAsyncTaskForWordBean(final String url, final NetworkConnection.OnWordBeanResponded callback);

        void requestUrlAsyncTaskForByteArray(String url, final NetworkConnection.OnByteArrayResponded callback);

        void requestUrlAsyncTaskForDailySentenceBean(String url, final NetworkConnection.OnDailySentenceBeanResponded callback);

        void requestUrlAsyncTaskForExampleSentenceBean(String url, final NetworkConnection.OnExampleSentenceBeanResponded callback);
    }

}
