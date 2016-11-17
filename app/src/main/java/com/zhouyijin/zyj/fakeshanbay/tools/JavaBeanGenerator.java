package com.zhouyijin.zyj.fakeshanbay.tools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;

/**
 * Created by zhouyijin on 2016/11/16.
 * |1.
 * |    通过使用Gson编译String生成javaBean
 */

public class JavaBeanGenerator {

    public WordBean getWordBean(String str) {
        return gson.fromJson(str, WordBean.class);
    }

    public DailySentenceBean getDailySentenceBean(String str) {
        return gson.fromJson(str, DailySentenceBean.class);
    }

    public ExampleSentenceBean getExampleSentenceBean(String str) {

        ExampleSentenceBean bean = null;
        try {
            bean = gson.fromJson(str, ExampleSentenceBean.class);
        } catch (Exception e) {
            bean = null;
        }
        return bean;
    }


    private Gson gson;

    private JavaBeanGenerator() {
        gson = new Gson();
    }

    public static JavaBeanGenerator getInstance() {
        return InstanceHolder.instance;

    }

    private static class InstanceHolder {
        private static JavaBeanGenerator instance = new JavaBeanGenerator();

    }
}
