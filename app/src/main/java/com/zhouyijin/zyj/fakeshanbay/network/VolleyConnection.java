package com.zhouyijin.zyj.fakeshanbay.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.tools.JavaBeanGenerator;

/**
 * Created by zhouyijin on 2016/11/16.
 */

public class VolleyConnection implements NetworkConnection.HttpConnection {

    public static final String TAG = "VolleyConnection";


    @Override
    public void requestUrlAsyncTaskForString(String url, final NetworkConnection.OnStringResponded callback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onStringResponded(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getString ------->> onErrorResponse: ");
            }
        });
        request.setTag("String");
        requestQueue.add(request);
    }

    @Override
    public void requestUrlAsyncTaskForWordBean(String url, final NetworkConnection.OnWordBeanResponded callback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                WordBean bean = JavaBeanGenerator.getInstance().getWordBean(response);
                callback.onWordBeanResponded(bean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getWordBean ------->> onErrorResponse: ");
            }
        });
        request.setTag("WordBean");
        requestQueue.add(request);
    }

    @Override
    public void requestUrlAsyncTaskForByteArray(String url, final NetworkConnection.OnByteArrayResponded callback) {
        ByteArrayRequest request = new ByteArrayRequest(url, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                callback.onByteArrayResponded(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getByteArray ------->> onErrorResponse: ");
            }
        });
        request.setTag("ByteArray");
        requestQueue.add(request);
    }


    @Override
    public void requestUrlAsyncTaskForDailySentenceBean(String url, final NetworkConnection.OnDailySentenceBeanResponded callback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DailySentenceBean bean = JavaBeanGenerator.getInstance().getDailySentenceBean(response);
                callback.onDailySentenceBeanResponded(bean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getDailySentenceBean ------->> onErrorResponse: ");
            }
        });
        request.setTag("DailySentenceBean");
        requestQueue.add(request);
    }

    @Override
    public void requestUrlAsyncTaskForExampleSentenceBean(String url, final NetworkConnection.OnExampleSentenceBeanResponded callback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("123 : ", response);
                ExampleSentenceBean bean = JavaBeanGenerator.getInstance().getExampleSentenceBean(response);
                callback.onExampleSentenceBeanResponded(bean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getExampleSentenceBean ------->> onErrorResponse: ");
            }
        });
        request.setTag("ExampleSentenceBean");
        requestQueue.add(request);
    }


    private RequestQueue requestQueue;

    private VolleyConnection() {
        requestQueue = Volley.newRequestQueue(MyApplication.getContext());
    }

    public static VolleyConnection getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static VolleyConnection instance = new VolleyConnection();
    }


    public static class ByteArrayRequest extends Request<byte[]> {
        private Response.Listener<byte[]> listener;

        public ByteArrayRequest(int method, String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.listener = listener;
        }

        public ByteArrayRequest(String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(byte[] response) {
            listener.onResponse(response);
        }
    }
}
