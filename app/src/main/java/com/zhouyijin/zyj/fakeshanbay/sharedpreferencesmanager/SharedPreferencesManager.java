package com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhouyijin.zyj.fakeshanbay.MyApplication;

/**
 * Created by zhouyijin on 2016/10/23.
 */

public class SharedPreferencesManager {

    public static final String SHAREDPREFERENCES_NAME = "fake_shanbay_sharedPreferences";

    public static final String TOKEN_KEY = "token";
    public static final String FIRST_USE = "first_use";
    public static final String WORDS_SCHEDULE_NUMBER = "words_schedule";

    private SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    private SharedPreferences.Editor editor = sharedPreferences.edit();

    private SharedPreferencesManager() {
    }

    public static SharedPreferencesManager getInstance() {
        return holder.manager;
    }

    //获取保存的token
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    //保存token
    public void setToken(String token) {
        editor.putString(TOKEN_KEY, token);
        editor.commit();
    }

    //查看是否第一次使用APP
    public boolean isFirstUseAPP() {
        return sharedPreferences.getBoolean(FIRST_USE, true);
    }

    //设置使用过APP的flag
    public void setAPPUsedFlag() {
        editor.putBoolean(FIRST_USE, false);
        editor.commit();
    }

    /**
     * ----------------------------------------------------------------------------------------------
     */
    public int getWordsScheduleNumber() {
        return sharedPreferences.getInt(WORDS_SCHEDULE_NUMBER, 100);
    }

    public void setWordsScheduleNumber(int wordsScheduleNumber){
        editor.putInt(WORDS_SCHEDULE_NUMBER, wordsScheduleNumber);
        editor.commit();
    }


    public static class holder {
        private static final SharedPreferencesManager manager = new SharedPreferencesManager();
    }

}
