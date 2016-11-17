package com.zhouyijin.zyj.fakeshanbay.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.tools.JavaBeanGenerator;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>
 * this class encapsulate the OkHttp's function
 */

public class OkHttpConnection implements NetworkConnection.HttpConnection {

    private OkHttpClient okHttpClient;
    private Handler networkHandler;


    private OkHttpConnection() {
        okHttpClient = new OkHttpClient();
        networkHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 向指定的url请求字符串,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void requestUrlAsyncTaskForString(String url, final NetworkConnection.OnStringResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessStringResult(response.body().string(), callback);
                    response.close();
                } else if (response != null) {
                    response.close();
                }
            }
        });
    }

    /**
     * 向指定的url请求JSON,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void requestUrlAsyncTaskForWordBean(final String url, final NetworkConnection.OnWordBeanResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    WordBean wordBean = JavaBeanGenerator.getInstance().getWordBean(response.body().string());
                    if (!wordBean.isQuerySuccessful()) {
                        wordBean = null;
                    }
                    //wordBean返回null表示查询失败
                    onSuccessWordBeanResult(wordBean, callback);
                    response.close();
                    return;
                } else if (response != null) {
                    response.close();
                }
                onSuccessWordBeanResult(null, callback);
            }
        });
    }


    /**
     * 向指定的url请求其他数据,以byte[]形式返回,通过回调callback的方法返回结果
     *
     * @param url
     * @param callback
     */
    public void requestUrlAsyncTaskForByteArray(String url, final NetworkConnection.OnByteArrayResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteArrayResult(response.body().bytes(), callback);
                    response.close();
                } else if (response != null) {
                    response.close();
                }

            }
        });
    }


    /**
     * 这个方法会给回调方法传一个InputStream,但是回调方法不会在主线程执行,仍然会在子线程,所以如果要更新UI的话一定要注意了!!
     *
     * @param url      请求任务的url
     * @param callback 获取InputStream的回调方法
     */
    public void requestUrlAsyncTaskForInputStream(String url, final NetworkConnection.OnInputStreamResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessInputStreamResult(response.body().byteStream(), callback);
                }
            }
        });
    }


    public void requestUrlAsyncTaskForDailySentenceBean(String url, final NetworkConnection.OnDailySentenceBeanResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    DailySentenceBean bean = JavaBeanGenerator.getInstance().getDailySentenceBean(response.body().string());
                    onSuccessDailySentenceBeanResult(bean, callback);
                    response.close();
                } else if (response != null) {
                    response.close();
                }
            }
        });
    }

    public void requestUrlAsyncTaskForExampleSentenceBean(String url, final NetworkConnection.OnExampleSentenceBeanResponded callback) {
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    ExampleSentenceBean bean = JavaBeanGenerator.getInstance().getExampleSentenceBean(response.body().string());
                    onSuccessExampleSentenceBeanResult(bean, callback);
                    response.close();
                    return;
                } else if (response != null) {
                    response.close();
                }
                onSuccessExampleSentenceBeanResult(null, callback);
            }
        });

    }

    private void onSuccessStringResult(final String result, final NetworkConnection.OnStringResponded callback) {
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onStringResponded(result);
            }
        });
    }

    private void onSuccessWordBeanResult(final WordBean result, final NetworkConnection.OnWordBeanResponded callback) {
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onWordBeanResponded(result);
            }
        });
    }

    private void onSuccessByteArrayResult(final byte[] result, final NetworkConnection.OnByteArrayResponded callback) {
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onByteArrayResponded(result);
            }
        });
    }

    /**
     * 这个方法不能放到主线程去执行,InputStream如果很大那么会造成应用ANR
     *
     * @param is
     * @param callback
     */
    private void onSuccessInputStreamResult(final InputStream is, final NetworkConnection.OnInputStreamResponded callback) {
        callback.onInputStreamResponded(is);
    }


    private void onSuccessDailySentenceBeanResult(final DailySentenceBean bean, final NetworkConnection.OnDailySentenceBeanResponded callback) {
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onDailySentenceBeanResponded(bean);
            }
        });
    }


    private void onSuccessExampleSentenceBeanResult(ExampleSentenceBean bean, NetworkConnection.OnExampleSentenceBeanResponded callback) {
        callback.onExampleSentenceBeanResponded(bean);
    }


    public static OkHttpConnection getInstance() {
        return InstanceHolder.instance;
    }


    private static class InstanceHolder {
        private static final OkHttpConnection instance = new OkHttpConnection();

    }


}
