package com.zhouyijin.zyj.fakeshanbay;

import android.app.Application;
import android.content.Context;

import com.zhouyijin.zyj.fakeshanbay.StudyRecorder.StudyRecorder;
import com.zhouyijin.zyj.fakeshanbay.StudyRecorder.StudyRecorderIMP;
import com.zhouyijin.zyj.fakeshanbay.TodayTask.TodayTask;
import com.zhouyijin.zyj.fakeshanbay.TodayTask.TodayTaskIMP;
import com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence.SentenceManager;
import com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence.SentenceManagerImp;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.QueryWordIMP;
import com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager.SharedPreferencesManager;
import com.zhouyijin.zyj.fakeshanbay.pronunciation.Pronunciation;
import com.zhouyijin.zyj.fakeshanbay.pronunciation.WordPronunciation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhouyijin on 2016/10/22.
 */

public class MyApplication extends Application {

    private static Executor executor;

    private static Context context;

    private static TodayTask todayTask;

    private static StudyRecorder studyRecorder;

    private static IQueryWord queryWord;

    private static SentenceManager sentenceManager;

    private static Pronunciation pronunciation;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newFixedThreadPool(3);
        context = MyApplication.this;
        todayTask = TodayTaskIMP.getInstance();
        studyRecorder = StudyRecorderIMP.getStudyRecorder();
        queryWord = QueryWordIMP.getInstance();
        sentenceManager = SentenceManagerImp.getInstance();
        pronunciation = WordPronunciation.getInstance();

        //如果是第一次启动,那么获取一下新的单词计划,不然没词可学啊
        if (SharedPreferencesManager.getInstance().isFirstUseAPP()) {
            getWordsFromAsset();
            SharedPreferencesManager.getInstance().setAPPUsedFlag();
        } else {
            getNewTask();
        }
    }

    private void getWordsFromAsset() {
        InputStream is;
//            StudyRecordGetter.getInstance().insertWords(TempWords.TMEPWORDS);
        try {
            is = getResources().getAssets().open("words/words.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            List<String> words = new ArrayList<>(150);
            String[] a = sb.toString().trim().split("\\s+");
            for (String word : a) {
                words.add(word);
            }
// TODO: 2016/11/3
            //向studyRecorder添加学习计划的单词
            studyRecorder.addNewWords(words);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //第一次使用默认设置100个单词
        getTodayTask().setTaskWordsNumber(100);
    }

    private void getNewTask() {
        getTodayTask().refreshWords();
    }


    public static IQueryWord getQueryWord() {
        return queryWord;
    }

    public static StudyRecorder getStudyRecorder() {
        return studyRecorder;
    }

    public static TodayTask getTodayTask() {
        return todayTask;
    }

    public static Context getContext() {
        return context;
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static SentenceManager getSentenceManager() {
        return sentenceManager;
    }

    public static Pronunciation getPronunciation() {
        return pronunciation;
    }


}
