package com.zhouyijin.zyj.fakeshanbay.activity.learningactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhouyijin.zyj.fakeshanbay.BaseActivity;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_explore_mode.ExploreModeFragment;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_self_evaluate_mode.SelfEvaluateModeFragment;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_spelling_mode.SpellingModeFragment;
import com.zhouyijin.zyj.myprogressbar.MyProgressBar;

/**
 * Created by zhouyijin on 2016/11/2.
 * 这个activity用来显示背单词的界面
 * 一共有三种模式:
 * |            1.拼写模式
 * |            2.探索模式
 * |            3.自评模式
 */

public class LearningActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "process";
    private PresenterLearningActivity presenter;

    private FrameLayout queryWordContainer, modeContainer;
    private int modeContainerID, queryWordContainerID;

    private ImageButton homeIcon, searchIcon;
    private TextView modeText;

    private SelfEvaluateModeFragment mEvaluatingFragment;  //自评模式
    private SpellingModeFragment mSpellingFragment;      //拼写模式
    private ExploreModeFragment mExploreFragment;       //探索模式

    private MyProgressBar bar;

    //初始化控件
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        presenter = new PresenterLearningActivity(this);
        initView();
        presenter.onCreate();
    }

    private void initView() {
        queryWordContainerID = R.id.query_word_container;
        queryWordContainer = (FrameLayout) findViewById(queryWordContainerID);
        queryWordContainer.setVisibility(View.GONE);
        modeContainerID = R.id.mode_container;
        modeContainer = (FrameLayout) findViewById(modeContainerID);
        homeIcon = (ImageButton) findViewById(R.id.ib_topbar_icon_home);
        homeIcon.setOnClickListener(this);
        searchIcon = (ImageButton) findViewById(R.id.ib_topbar_icon_search);
        searchIcon.setOnClickListener(this);
        modeText = (TextView) findViewById(R.id.tv_mode);
        bar = (MyProgressBar) findViewById(R.id.pb_myprogress);
        bar.setMax(MyApplication.getTodayTask().getTodayTaskWordsNumber());
    }


    /**
     * 调用这个方法显示自评模式
     *
     * @param wordBean     显示的单词信息
     * @param sentenceBean 显示一个例句
     */
    public void switch2EvaluatingMode(@NonNull WordBean wordBean, @NonNull ExampleSentenceBean sentenceBean) {
        Log.d("big_test", "switch2EvaluatingMode: ");
        if (wordBean == null) {
            return;
        }
        modeText.setText("自评模式");
        queryWordContainer.setVisibility(View.GONE);
        if (mEvaluatingFragment == null) {
            mEvaluatingFragment = new SelfEvaluateModeFragment();
            mEvaluatingFragment.setOnCompleteListener(presenter);
        }
        mEvaluatingFragment.setContent(wordBean, sentenceBean);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(modeContainerID, mEvaluatingFragment);
        transaction.commit();
        bar.setProgress(MyApplication.getTodayTask().getTodayFinishedWordsNumber());
        bar.setSecondaryProgress(MyApplication.getTodayTask().getTodayFailedWordsNumber());
    }


    /**
     * 调用这个方法显示拼写模式
     *
     * @param wordBean     传入的单词信息
     * @param sentenceBean 传入的例句信息,最好使用系统例句
     */
    public void switch2SpellingMode(@NonNull WordBean wordBean, ExampleSentenceBean sentenceBean) {
        Log.d(TAG, "switch2SpellingMode: ");
        if (wordBean == null) {
            return;
        }
        modeText.setText("拼写模式");
        if (mSpellingFragment == null) {
            mSpellingFragment = new SpellingModeFragment();
            mSpellingFragment.setOnWordCorrectListener(presenter);
        }
        mSpellingFragment.setContent(wordBean, sentenceBean);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(modeContainerID, mSpellingFragment);
        transaction.commit();
    }


    /**
     * 调用这个方法显示探索模式
     *
     * @param wordBean     封装了单词的信息
     * @param sentenceBean 封装了例句的信息
     * @param isMastered   这个单词是否掌握了
     */
    public void switch2ExploreMode(WordBean wordBean, ExampleSentenceBean sentenceBean, boolean isMastered) {
        if (wordBean == null) {
            return;
        }
        modeText.setText("探索模式");
        if (mExploreFragment == null) {
            mExploreFragment = new ExploreModeFragment();
            mExploreFragment.setOnWordCompleteListener(presenter);
        }
        mExploreFragment.setContent(wordBean, sentenceBean, isMastered);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(modeContainerID, mExploreFragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_topbar_icon_home:
                finish();
                break;
            case R.id.ib_topbar_icon_search:
                queryWordContainer.setVisibility(View.VISIBLE);
                // TODO: 2016/11/13 打开搜索界面
                break;
        }
    }
}
