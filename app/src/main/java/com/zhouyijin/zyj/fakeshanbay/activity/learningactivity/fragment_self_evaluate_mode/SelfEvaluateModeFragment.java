package com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_self_evaluate_mode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;

/**
 * Created by zhouyijin on 2016/11/11.
 * The fragment is used for self_evaluating_mode
 */

public class SelfEvaluateModeFragment extends Fragment implements View.OnClickListener {

    private TextView tv_self_evaluating_word,
            tv_too_simple,
            tv_english_annotation,
            tv_chinese_annotation,
            tv_known,
            tv_unknown,
            tv_check_details;

    private LinearLayout ll_known;
    private FrameLayout fl_details;

    private String evaluatingWord;

    //记录点击了多少次不认识按钮, it record how many times the unknown Button have been clicked
    private int unknownCount;

    //用来存储这个单词相关的信息 ,this WordBean store many important information of the word
    private WordBean thisWordBean;

    private ExampleSentenceBean thisSentenceBean;

    //this listener's method will be invoked when self_evaluating is completed no matter user through the test
    private OnCompleteListener listener;


    /**

     */
    private void refreshSelfEvaluateFragment() {
        Log.d("big_test", "refreshSelfEvaluateFragment: ");
        evaluatingWord = thisWordBean.getContent();
        setEvaluatingWordText(thisWordBean);
        setEnglishSentence(thisSentenceBean);
        setChineseAnnotation(thisWordBean);
        ll_known.setVisibility(View.VISIBLE);
        fl_details.setVisibility(View.GONE);
        tv_known.setText("认识");
        tv_unknown.setText("不认识");
        unknownCount = 0;
        MyApplication.getPronunciation().playAudio(evaluatingWord);
    }


    public void setContent(@NonNull WordBean bean, ExampleSentenceBean sentenceBean) {
        thisWordBean = bean;
        thisSentenceBean = sentenceBean;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSelfEvaluateFragment();
    }

    private void setChineseAnnotation(WordBean wordBean) {
        tv_chinese_annotation.setText(wordBean.getChineseDefinition());
        tv_chinese_annotation.setBackgroundColor(getResources().getColor(R.color.theme_blue_lite));
        tv_chinese_annotation.setVisibility(View.GONE);
    }


    private void setEnglishSentence(ExampleSentenceBean sentenceBean) {
        if (sentenceBean == null || sentenceBean.getData().isEmpty()) {
            tv_english_annotation.setVisibility(View.GONE);
            return;
        }
        SpannableString englishAnnotation = sentenceBean.getData().get(0).getColoredEnglishSentence();
        tv_english_annotation.setText(englishAnnotation);
        tv_english_annotation.setBackgroundColor(getResources().getColor(R.color.theme_blue_lite));
        tv_english_annotation.setVisibility(View.GONE);
    }

    private void setEvaluatingWordText(WordBean bean) {
        tv_self_evaluating_word.setText("");
        String word = bean.getContent();
        String pronunciation = bean.getPronunciation();
        SpannableString s1 = new SpannableString(word);
        s1.setSpan(new TextAppearanceSpan(getActivity(), R.style.LearningWordBig), 0, s1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_self_evaluating_word.append(s1);
        if (pronunciation != null && !pronunciation.equals("")) {
            SpannableString s2 = new SpannableString("   /" + pronunciation + "/");
            s2.setSpan(new TextAppearanceSpan(getActivity(), R.style.LearningWordSmall), 0, s2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_self_evaluating_word.append(s2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode_self_evaluate_fragment, container, false);
        initView(view);
        Log.d("big_test", "onCreateView: ");
        return view;
    }

    private void initView(View view) {
        ll_known = (LinearLayout) view.findViewById(R.id.ll_known);
        fl_details = (FrameLayout) view.findViewById(R.id.fl_details);

        tv_self_evaluating_word = (TextView) view.findViewById(R.id.tv_self_evaluating_word);
        tv_self_evaluating_word.setOnClickListener(this);
        tv_too_simple = (TextView) view.findViewById(R.id.tv_too_simple);
        tv_too_simple.setOnClickListener(this);
        tv_english_annotation = (TextView) view.findViewById(R.id.tv_english_annotation);
        tv_chinese_annotation = (TextView) view.findViewById(R.id.tv_chinese_annotation);
        tv_known = (TextView) view.findViewById(R.id.tv_known);
        tv_known.setOnClickListener(this);
        tv_unknown = (TextView) view.findViewById(R.id.tv_unknown);
        tv_unknown.setOnClickListener(this);
        tv_check_details = (TextView) view.findViewById(R.id.tv_check_details);
        tv_check_details.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_self_evaluating_word:  //点击单词播放声音
                MyApplication.getPronunciation().playAudio(evaluatingWord);
                break;
            case R.id.tv_too_simple:
                if (listener == null) {
                    return;
                }
                listener.onCompleteListener(evaluatingWord, true);
                break;
            case R.id.tv_known://   认识,直接跳到拼写模式,只要点击了一次不认识则跳转到探索模式,由activity判断
                if (listener == null) {
                    return;
                }
                listener.onCompleteListener(evaluatingWord, unknownCount < 1);
                break;
            case R.id.tv_unknown:
                unknownCount++;
                unknownOperation();
                break;
            case R.id.tv_check_details:
                // TODO: 2016/11/11 跳转到探索模式
                listener.onCompleteListener(evaluatingWord, unknownCount < 1);
                break;
        }
    }

    private void unknownOperation() {
        switch (unknownCount) {
            case 1:     //第一次点击
                firstClickUnknown();
                break;
            case 2:      //第二次点击
                secondClickUnknown();
                break;
        }
    }


    /**
     * 第二次点击unknown有变换的UI
     * 1.英文释义的背景变成有边框白色的背景
     * 2.出现中文释义
     * 3.下方的两个按钮消失
     * 4.出现查看详细的按钮
     */
    private void secondClickUnknown() {
        tv_english_annotation.setBackground(getResources().getDrawable(R.drawable.shape_border_white_bg, null));
        tv_chinese_annotation.setVisibility(View.VISIBLE);
        ll_known.setVisibility(View.GONE);
        fl_details.setVisibility(View.VISIBLE);
    }

    /**
     * 第一次点击unknown有变换的UI
     * 1.too_simple消失
     * 2.出现英文释义
     * 3.最下方两个按钮内容改变成"想起来了"和"没想起来"
     * <p>
     * 有个特例,有些词语是没有英文注解的,直接显示中文注解,然后显示查看详细的按钮
     */
    private void firstClickUnknown() {
        tv_too_simple.setVisibility(View.GONE);
        tv_english_annotation.setVisibility(View.VISIBLE);
        tv_known.setText("想起来了");
        tv_unknown.setText("没想起来");
    }


    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }


    public interface OnCompleteListener {
        //在activity中实现这个方法,这个方法会在完成自评之后调用
        void onCompleteListener(String word, boolean isWordMastered);
    }
}
