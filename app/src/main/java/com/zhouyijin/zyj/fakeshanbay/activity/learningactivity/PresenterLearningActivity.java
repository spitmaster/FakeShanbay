package com.zhouyijin.zyj.fakeshanbay.activity.learningactivity;


import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zhouyijin.zyj.fakeshanbay.BasePresenter;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_explore_mode.ExploreModeFragment;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_self_evaluate_mode.SelfEvaluateModeFragment;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_spelling_mode.SpellingModeFragment;
import com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence.SentenceManager;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;

/**
 * Created by zhouyijin on 2016/11/11.
 */

public class PresenterLearningActivity extends BasePresenter<LearningActivity> implements SelfEvaluateModeFragment.OnCompleteListener, SpellingModeFragment.OnWordCorrectListener, ExploreModeFragment.OnWordCompleteListener {

    public static final String TAG = "word_test";

    private String mWord;
    private WordBean mWordBean;
    private ExampleSentenceBean systemSentenceBean;
    private boolean isMastered;
    private InputMethodManager imm;

    protected PresenterLearningActivity(LearningActivity view) {
        super(view);
        imm = (InputMethodManager) view.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void onCreate() {
        getNewWord();
    }

    /**
     * 当自评模式完成它的工作之后会回调这个方法,一般在这个回调里去切换到拼写模式
     *
     * @param word           完成的是哪个单词
     * @param isWordMastered 这个单词是否已经掌握
     */
    @Override
    public void onCompleteListener(String word, boolean isWordMastered) {
        Log.d(TAG, "onCompleteListener: ");
        isMastered = isWordMastered;
        if (isWordMastered) {
            getView().switch2SpellingMode(mWordBean, systemSentenceBean);
        } else {
            getView().switch2ExploreMode(mWordBean, systemSentenceBean, isWordMastered);
        }
    }

    /**
     * 当拼写模式完成工作之后会回调这个方法
     * |    用来切换到探索模式,并播放声音
     * @param word      完成的单词
     * @param isCorrect 是否拼写正确
     */
    @Override
    public void onWordCorrectListener(String word, boolean isCorrect) {
        getView().switch2ExploreMode(mWordBean, systemSentenceBean, isCorrect);
        MyApplication.getPronunciation().playAudio(word);
    }

    /**
     * 当探索模式结束之后会回调这个方法
     *
     * @param word
     * @param isMastered
     */
    @Override
    public void OnWordComplete(String word, boolean isMastered) {
        if (isMastered) {
            MyApplication.getTodayTask().setFinishedWord(mWord);
        } else {
            MyApplication.getTodayTask().setFailedWord(mWord);
        }
        if (!getNewWord()) {
            Toast.makeText(getView(), "今天的任务结束了!", Toast.LENGTH_LONG).show();
            getView().finish();
        }
    }


    /**
     * 获取最新的任务单词
     *
     * @return 获取成功则返回true, 获取失败说明任务已经完成了返回false
     */
    public boolean getNewWord() {
        mWord = MyApplication.getTodayTask().getFirstUnfinishedOrFailedWord();
        if (mWord == null) {
            return false;
        }
        MyApplication.getQueryWord().queryWord(mWord, new IQueryWord.OnQuerySucceedListener() {
            @Override
            public void onQuerySucceed(WordBean wordBean) {
                mWordBean = wordBean;
                if (wordBean != null) {
                    MyApplication.getSentenceManager().getExampleSentence(mWord, new SentenceManager.OnGetResult() {
                        @Override
                        public void OnGetResult(ExampleSentenceBean bean) {
                            systemSentenceBean = bean;
                            if (systemSentenceBean != null) {
                                getView().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getView().switch2EvaluatingMode(mWordBean, systemSentenceBean);
                                    }
                                });
                            }
                        }
                    }, SentenceManager.SYSTEM_SENTENCE);
                } else {
                    MyApplication.getTodayTask().setFinishedWord(mWord);
                    if (!getNewWord()) {
                        // TODO: 2016/11/13 返回false则直接进入打卡界面
                    }
                }
            }
        });
        return true;
    }
}
