package com.zhouyijin.zyj.fakeshanbay.tools;

import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager.SharedPreferencesManager;

import org.json.JSONObject;

/**
 * Created by zhouyijin on 2016/11/9.
 * ---------------------------------------------------------------
 * 正确的url示例
 * http://api.shanbay.com/api/v1/account/?access_token=Hp2Vd2LLLmzvDVDH0HEQ2xf4zY1X4X
 * 下面是获取正常的情况
 * {
 * username: "zyjzyj2",
 * nickname: "勃学家",
 * id: 18534712,
 * avatar: "https://static.baydn.com/avatar/media_store/8b98a9d06651557a73bb1146433b06df?imageView/1/w/128/h/128/"
 * }
 * //-----------------------------------------------------------
 * 如果token不正常则返回这个
 * {
 * msg: "Invalid token"
 * }
 */

public class CheckTokenExpired {

    public static final String API_URL = "http://api.shanbay.com/api/v1/account/?access_token=";

    /**
     * 检查是否是正确可用的token,
     * 如果可用在回调方法中返回true
     * 如果token不可用在回调方法中返回false
     */
    public static void checkTokenExpired(final OnResult callback) {
        String url = getCheckTokenUrl();
        NetworkConnection.getInstance().getString(url, new NetworkConnection.OnStringResponded() {
            @Override
            public void onStringResponded(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //如果有正确的token肯定返回了username,这时就可以确定啦!
                    callback.onResult(jsonObject.has("username"));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onResult(false);
                }
            }
        });
    }


    private static String getCheckTokenUrl() {
        String currentToken = SharedPreferencesManager.getInstance().getToken();
        String url = API_URL + currentToken;
        return url;
    }

    public interface OnResult {
        /*
            传入值:
                   true:表示token是可用的
                   false:表示token是不可用的
         */
        void onResult(boolean isTokenUseful);
    }
}
