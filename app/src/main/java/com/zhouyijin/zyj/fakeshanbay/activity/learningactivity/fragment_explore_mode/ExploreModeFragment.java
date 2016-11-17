package com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_explore_mode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.swipe.SwipeLayout;
import com.zhouyijin.zyj.clickablesentenceview.ClickableSentenceView;
import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;

import java.util.List;

/**
 * Created by zhouyijin on 2016/11/12.
 */

public class ExploreModeFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ClickableSentenceView.OnWordClickListener {

    private String exploreWord;
    private boolean isMastered = false;
    private WordBean mWordBean;
    private ExampleSentenceBean mSentenceBean;


    TextView tv_withdraw,   //撤销"会背"的按钮
            tv_explore_word,    //点击播放声音的文本,显示word和word的音标
            tv_explore_chinese_annotation,  //单词的中文注解
            tv_english_annotation,      //单词的英文注解,默认不显示
            tv_note,                //点击进入一个activity查看所有用户的笔记
            tv_next;    //点击完成这个单词的背诵

    private void setExploreWord(WordBean wordBean) {
        if (wordBean == null) {
            return;
        }
        exploreWord = wordBean.getContent();
        SpannableString s1 = new SpannableString(exploreWord);
        s1.setSpan(new TextAppearanceSpan(getActivity(), R.style.LearningWordBig), 0, exploreWord.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_explore_word.setText("");
        tv_explore_word.append(s1);
        String pronunciation = wordBean.getPronunciation();
        if (pronunciation != null && !pronunciation.equals("")) {
            pronunciation = "  /" + pronunciation + "/";
            SpannableString s2 = new SpannableString(pronunciation);
            s2.setSpan(new TextAppearanceSpan(getActivity(), R.style.LearningWordSmall), 0, pronunciation.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_explore_word.append(s2);
        }
        tv_explore_chinese_annotation.setText(wordBean.getChineseDefinition());
        String englishAnnotation = wordBean.getWordAllExplanationEn();
        if (englishAnnotation == null || englishAnnotation.equals("")) {
            tb_english_annotation.setVisibility(View.GONE);
        } else {
            tv_english_annotation.setText(englishAnnotation);
            tb_english_annotation.setVisibility(View.VISIBLE);
        }
        tv_english_annotation.setVisibility(View.GONE);
    }

    ToggleButton tb_english_annotation; //点击显示或关闭英文注释

    RecyclerView rv_notes;  //预览一部分用户的笔记

    /**
     * 以下是例句专用的控件-----------------------------------------------------------------------------------
     */
    SwipeLayout sl_example_sentence_1, sl_example_sentence_2;    //放置例句的容器

    ImageView iv_delete_icon_1, iv_delete_icon_2,    //删除例句的按钮
            iv_sentence_draw_1, iv_sentence_draw_2;      //点击可以拉开,显示删除按钮

    ClickableSentenceView csv_example_sentence_1, csv_example_sentence_2;    //例句的内容,可以点击

    TextView tv_example_sentence_chinese_annotation_1, tv_example_sentence_chinese_annotation_2;
    /*
     以上是例句专用的控件-------------------------------------------------------------------------------------
     */

    private void setExampleSentence(ExampleSentenceBean sentenceBean) {
        sl_example_sentence_1.setVisibility(View.GONE);
        sl_example_sentence_2.setVisibility(View.GONE);

        if (sentenceBean == null) return;
        List<ExampleSentenceBean.DataBean> list = sentenceBean.getData();
        if (list == null || list.size() == 0) return;
        ExampleSentenceBean.DataBean bean1 = list.get(0);
        if (bean1 != null) {
            String annotation = bean1.ridTag();
            String keyword = bean1.getKeywordInSentence();
            csv_example_sentence_1.setSentence(annotation, keyword);
            csv_example_sentence_1.setTextViewStyle(Color.BLACK, 16, Color.TRANSPARENT, 4, null);
            tv_example_sentence_chinese_annotation_1.setText(bean1.getTranslation());
            sl_example_sentence_1.setVisibility(View.VISIBLE);
            Log.d("persistent", "setExampleSentence: " + annotation);
        }
        if (list.size() < 2) return;
        ExampleSentenceBean.DataBean bean2 = list.get(1);
        if (bean2 != null) {
            String annotation = bean2.ridTag();
            String keyword = bean2.getKeywordInSentence();
            csv_example_sentence_2.setSentence(annotation, keyword);
            csv_example_sentence_2.setTextViewStyle(Color.BLACK, 16, Color.TRANSPARENT, 4, null);
            tv_example_sentence_chinese_annotation_2.setText(bean1.getTranslation());
            sl_example_sentence_2.setVisibility(View.VISIBLE);
            Log.d("persistent", "setExampleSentence: " + annotation);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshExploreView();
    }

    //当切换到这个fragment的时候需要调用这个方法,不然不能正确显示
    public void refreshExploreView() {
        setWithDrawView(isMastered);
        setExploreWord(mWordBean);
        setExampleSentence(mSentenceBean);
        // TODO: 2016/11/12  添加其他用户的例句
        setNotes(); //以后再写吧
    }

    public void setContent(@NonNull WordBean wordBean, ExampleSentenceBean sentenceBean, boolean isMastered) {
        this.isMastered = isMastered;
        mWordBean = wordBean;
        mSentenceBean = sentenceBean;
    }

    private void setNotes() {
    }


    private void setWithDrawView(boolean isMastered) {
        if (isMastered) {
            tv_withdraw.setTextSize(14);
            tv_withdraw.setText("今天不再安排学习");
            String text = "  撤销";
            SpannableString withdraw = new SpannableString(text);
            withdraw.setSpan(new TextAppearanceSpan(getActivity(), R.style.KeyWord), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_withdraw.append(text);
            tv_withdraw.setVisibility(View.VISIBLE);
        } else {
            tv_withdraw.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        tv_withdraw = (TextView) view.findViewById(R.id.tv_withdraw);
        tv_withdraw.setOnClickListener(this);
        tv_explore_word = (TextView) view.findViewById(R.id.tv_explore_word);
        tv_explore_word.setOnClickListener(this);
        tv_explore_chinese_annotation = (TextView) view.findViewById(R.id.tv_explore_chinese_annotation);
        tv_english_annotation = (TextView) view.findViewById(R.id.tv_english_annotation);
        tv_note = (TextView) view.findViewById(R.id.tv_note);
        tv_note.setOnClickListener(this);
        tb_english_annotation = (ToggleButton) view.findViewById(R.id.tb_english_annotation);
        tb_english_annotation.setOnCheckedChangeListener(this);
        rv_notes = (RecyclerView) view.findViewById(R.id.rv_notes);
        tv_next = (TextView) view.findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);

        iv_delete_icon_1 = (ImageView) view.findViewById(R.id.iv_delete_icon_1);
        iv_delete_icon_1.setOnClickListener(this);
        iv_delete_icon_2 = (ImageView) view.findViewById(R.id.iv_delete_icon_2);
        iv_delete_icon_2.setOnClickListener(this);
        iv_sentence_draw_1 = (ImageView) view.findViewById(R.id.iv_sentence_draw_1);
        iv_sentence_draw_1.setOnClickListener(this);
        iv_sentence_draw_2 = (ImageView) view.findViewById(R.id.iv_sentence_draw_2);
        iv_sentence_draw_2.setOnClickListener(this);
        csv_example_sentence_1 = (ClickableSentenceView) view.findViewById(R.id.csv_example_sentence_1);
        csv_example_sentence_1.setOnWordClickListener(this);
        csv_example_sentence_2 = (ClickableSentenceView) view.findViewById(R.id.csv_example_sentence_2);
        csv_example_sentence_2.setOnWordClickListener(this);
        tv_example_sentence_chinese_annotation_1 = (TextView) view.findViewById(R.id.tv_example_sentence_chinese_annotation_1);
        tv_example_sentence_chinese_annotation_2 = (TextView) view.findViewById(R.id.tv_example_sentence_chinese_annotation_2);
        sl_example_sentence_1 = (SwipeLayout) view.findViewById(R.id.sl_example_sentence_1);
        sl_example_sentence_1.addDrag(SwipeLayout.DragEdge.Right, iv_delete_icon_1);
        sl_example_sentence_2 = (SwipeLayout) view.findViewById(R.id.sl_example_sentence_2);
        sl_example_sentence_2.addDrag(SwipeLayout.DragEdge.Right, iv_delete_icon_2);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_withdraw:
                tv_withdraw.setTextSize(16);
                tv_withdraw.setText("稍后将继续安排这个单词的学习");
                isMastered = false;
                break;
            case R.id.tv_explore_word:
                MyApplication.getPronunciation().playAudio(exploreWord);
                break;
            case R.id.tv_note:
                // TODO: 2016/11/12 打开一个activity专门显示其他用户的笔记
                break;
            case R.id.iv_delete_icon_1:
                sl_example_sentence_1.setVisibility(View.GONE);
                break;
            case R.id.iv_delete_icon_2:
                sl_example_sentence_2.setVisibility(View.GONE);
                break;
            case R.id.iv_sentence_draw_1:
                sl_example_sentence_1.toggle();
                break;
            case R.id.iv_sentence_draw_2:
                sl_example_sentence_2.toggle();
                break;
            case R.id.tv_next:
                if (listener != null) {
                    listener.OnWordComplete(exploreWord, isMastered);
                }
                break;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode_explore_fragment, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_english_annotation:    //点击显示英文解释
                if (isChecked) {
                    tv_english_annotation.setVisibility(View.VISIBLE);
                    tb_english_annotation.setTextColor(Color.WHITE);
                } else {
                    tv_english_annotation.setVisibility(View.GONE);
                    tb_english_annotation.setTextColor(getResources().getColor(R.color.shanbayStandard));
                }
                break;
        }
    }

    private OnWordCompleteListener listener;

    public void setOnWordCompleteListener(OnWordCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWordClick(View view, String word) {
        Toast.makeText(getActivity(), word + " 被点击", Toast.LENGTH_SHORT).show();
    }


    public interface OnWordCompleteListener {
        void OnWordComplete(String word, boolean isMastered);   //当点击下一个按钮的时候调用这个方法,让activity做其他的逻辑
    }
}
