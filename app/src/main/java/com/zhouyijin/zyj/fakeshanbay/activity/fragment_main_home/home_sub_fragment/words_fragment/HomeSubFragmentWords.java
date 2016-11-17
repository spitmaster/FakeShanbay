package com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.words_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.activity.sharepictureactivity.SharePicActivity;


/**
 * Created by Administrator on 2016/7/22.
 */
public class HomeSubFragmentWords extends Fragment implements View.OnClickListener, IUpdateSubFragmentWords {
    private TextView tv_check_in_days;      //总打卡天数
    private TextView tv_schedule_words_num;  //计划学习的单词数
    private TextView tv_mastered_words_num; //掌握单词数
    private TextView tv_today_words;           //今日需要学习单词数
    private TextView tv_unfinished_words;          //今日学习的新词
    private TextView tv_complete_words;     //今日已完成的单词

    private TextView tv_daily_sentence_in_english;  //每日一句的英文
    private TextView tv_daily_sentence_in_chinese;  //每日一句的中文
    private TextView tv_daily_sentence_author;      //每日一句的作者

    private ImageButton ib_start_learning_words;      //开始背单词的按钮
    private ImageButton ib_start_shanbay_listen,    //四个打开扇贝自家其他应用的按钮
            ib_start_shanbay_news,
            ib_start_shanbay_reader,
            ib_start_shanbay_sentence;
    private ImageButton ib_share_daily_sentence_to_weibo,   //两个分享每日一句的按钮
            ib_share_daily_sentence_to_weixin;

    private SubWordFragmentPresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_sub_fragment_words, container, false);
        initViews(view);
        presenter = new SubWordFragmentPresenter(this);
        presenter.onCreateView();
        return view;
    }

    @Override
    public void onResume() {
        presenter.onResume();
        super.onResume();
    }

    private void initViews(View view) {
        tv_check_in_days = (TextView) view.findViewById(R.id.tv_check_in_days);
        tv_schedule_words_num = (TextView) view.findViewById(R.id.tv_schedule_words_num);
        tv_mastered_words_num = (TextView) view.findViewById(R.id.tv_mastered_words_num);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/impact_0.ttf");
        tv_check_in_days.setTypeface(tf);
        tv_schedule_words_num.setTypeface(tf);
        tv_mastered_words_num.setTypeface(tf);
        tv_today_words = (TextView) view.findViewById(R.id.tv_today_words);
        tv_unfinished_words = (TextView) view.findViewById(R.id.tv_unfinished_words);
        tv_complete_words = (TextView) view.findViewById(R.id.tv_complete_words);
        tv_daily_sentence_in_english = (TextView) view.findViewById(R.id.tv_daily_sentence_in_english);
        tv_daily_sentence_in_english.setOnClickListener(this);
        tv_daily_sentence_in_chinese = (TextView) view.findViewById(R.id.tv_daily_sentence_in_chinese);
        tv_daily_sentence_in_chinese.setOnClickListener(this);
        tv_daily_sentence_author = (TextView) view.findViewById(R.id.tv_daily_sentence_author);

        ib_start_learning_words = (ImageButton) view.findViewById(R.id.ib_start_learning_words);
        ib_start_learning_words.setOnClickListener(this);
        ib_start_shanbay_listen = (ImageButton) view.findViewById(R.id.ib_start_shanbay_listen);
        ib_start_shanbay_listen.setOnClickListener(this);
        ib_start_shanbay_news = (ImageButton) view.findViewById(R.id.ib_start_shanbay_news);
        ib_start_shanbay_news.setOnClickListener(this);
        ib_start_shanbay_reader = (ImageButton) view.findViewById(R.id.ib_start_shanbay_reader);
        ib_start_shanbay_reader.setOnClickListener(this);
        ib_start_shanbay_sentence = (ImageButton) view.findViewById(R.id.ib_start_shanbay_sentence);
        ib_start_shanbay_sentence.setOnClickListener(this);
        ib_share_daily_sentence_to_weibo = (ImageButton) view.findViewById(R.id.ib_share_daily_sentence_to_weibo);
        ib_share_daily_sentence_to_weibo.setOnClickListener(this);
        ib_share_daily_sentence_to_weixin = (ImageButton) view.findViewById(R.id.ib_share_daily_sentence_to_weixin);
        ib_share_daily_sentence_to_weixin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_start_learning_words:
                presenter.startLearning();
                break;
            case R.id.ib_start_shanbay_listen:

                break;
            case R.id.ib_start_shanbay_news:

                break;
            case R.id.ib_start_shanbay_reader:

                break;
            case R.id.ib_start_shanbay_sentence:

                break;
            case R.id.ib_share_daily_sentence_to_weibo:
                break;
            case R.id.ib_share_daily_sentence_to_weixin:
                break;

            case R.id.tv_daily_sentence_in_chinese:
                startActivity(new Intent(getActivity(), SharePicActivity.class));
                break;
            case R.id.tv_daily_sentence_in_english:
                startActivity(new Intent(getActivity(), SharePicActivity.class));
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefreshDailySentence(DailySentenceBean bean) {
        if (bean == null) {
            return;
        }
        String english = bean.getContent();
        String chinese = bean.getNote();
        String author = bean.getDateline();
        tv_daily_sentence_in_english.setText(english);
        tv_daily_sentence_in_chinese.setText(chinese);
        tv_daily_sentence_author.setText(author);
    }

    @Override
    public void onRefreshStudyBrief() {
        tv_schedule_words_num.setText(MyApplication.getStudyRecorder().getScheduleWordNumber() + "");
        tv_mastered_words_num.setText(MyApplication.getStudyRecorder().getMasteredWordNumber() + "");

    }

    @Override
    public void onRefreshTodayTask() {
        tv_today_words.setText(MyApplication.getTodayTask().getTodayTaskWordsNumber() + "");
        tv_unfinished_words.setText("" +
                (MyApplication.getTodayTask().getTodayUnfinishedWordsNumber()
                        + MyApplication.getTodayTask().getTodayFailedWordsNumber()));
        tv_complete_words.setText(MyApplication.getTodayTask().getTodayFinishedWordsNumber() + "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
