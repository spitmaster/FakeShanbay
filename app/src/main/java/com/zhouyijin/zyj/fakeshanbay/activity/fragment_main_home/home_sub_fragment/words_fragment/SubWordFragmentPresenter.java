package com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.words_fragment;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.zhouyijin.zyj.fakeshanbay.BasePresenter;
import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.LearningActivity;
import com.zhouyijin.zyj.fakeshanbay.dailysentence.DailySentence;
import com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence.SentenceManager;

/**
 * Created by zhouyijin on 2016/10/24.
 */

public class SubWordFragmentPresenter extends BasePresenter<HomeSubFragmentWords>  {

    protected SubWordFragmentPresenter(HomeSubFragmentWords view) {
        super(view);
    }

    public void onResume() {
        getView().onRefreshStudyBrief();
        getView().onRefreshTodayTask();
    }

    public void onCreateView() {
        if (MyApplication.getTodayTask().checkIsNewDay()) MyApplication.getTodayTask().refreshWords();
        //更新dailysentence,只在创建的时候更新一次
        DailySentence.getInstance().getDailySentence(new DailySentence.OnDailySentenceUpdate() {
            @Override
            public void onDailySentenceUpdate(DailySentenceBean bean) {
                getView().onRefreshDailySentence(bean);
            }
        });
    }

    public void startLearning() {
        String word = MyApplication.getTodayTask().getFirstUnfinishedOrFailedWord();
        Log.d("sentence", "startLearning: word = " + word);
        MyApplication.getSentenceManager().getExampleSentence(word, new SentenceManager.OnGetResult() {
            @Override
            public void OnGetResult(ExampleSentenceBean bean) {
                if (bean == null) {
                    getView().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getView().getActivity(), "学习内容还没准备好", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getView().startActivity(new Intent(getView().getActivity(), LearningActivity.class));
                }
            }
        }, SentenceManager.SYSTEM_SENTENCE);
    }



}
