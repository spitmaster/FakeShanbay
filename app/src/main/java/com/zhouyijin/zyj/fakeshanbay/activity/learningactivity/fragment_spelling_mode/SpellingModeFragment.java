package com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.fragment_spelling_mode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.activity.learningactivity.PresenterLearningActivity;
import com.zhouyijin.zyj.recitewords.ReciteWords;


/**
 * Created by zhouyijin on 2016/11/2.
 */

public class SpellingModeFragment extends Fragment implements ReciteWords.OnTextChangedListener, ReciteWords.OnCheckListener {

    public static final String TAG = "process";

    //显示单词的解释
    private TextView tv_word_explanation_cn;

    //显示输入单词的界面
    private ReciteWords rw_contentwords;

    //显示整个句子的解释
    private TextView tv_sentence_explanation;

    //显示提示信息
    private TextView tv_spelling_hint;

    //显示提示按钮
    private TextView tv_spelling_mode_hint_button;

    private OnWordCorrectListener listener;

    private String displayingInput;

    //这个由activity调用设置,好让activity来控制流程
    public void setOnWordCorrectListener(OnWordCorrectListener listener) {
        this.listener = listener;
    }

    private String recitingWord;
    private int hintCount = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode_spelling_fragment, container, false);
        initView(view);
        Log.d(TAG, "onCreateView: ");
        return view;
    }

    private void initView(View view) {
        tv_word_explanation_cn = (TextView) view.findViewById(R.id.tv_word_explanation_cn);
        rw_contentwords = (ReciteWords) view.findViewById(R.id.rw_contentwords);
        rw_contentwords.setWordChangeListener(this);
        rw_contentwords.setOnCheckListener(this);
        tv_sentence_explanation = (TextView) view.findViewById(R.id.tv_sentence_explanation);
        tv_spelling_hint = (TextView) view.findViewById(R.id.tv_spelling_hint);
        tv_spelling_mode_hint_button = (TextView) view.findViewById(R.id.tv_spelling_mode_hint_button);
        tv_spelling_mode_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    1.提示一次以内填写正确后点击,算这个单词成功背过了,传入true
                    2.如果提示两次以上,就不能算这个单词成功背会,第二个参数传入false
                 */
                if (rw_contentwords.isInputCorrect()) {
                    if (listener != null) {
                        listener.onWordCorrectListener(recitingWord, hintCount < 2);
                        return;
                    }
                }
                //如果填写不正确点击
                switch (hintCount) {
                    case 0: //第一次提示应该要播放声音来提示
                        playAudio(recitingWord);
                        break;
                    case 1: //第二次提示,要改变下方的文字提示信息,显示这个单词的一部分做提示
                        tipSome(recitingWord);
                        break;
                    default: //第三次以上,在下方直接显示整个单词内容,同时显示输入的单词做对照
                        tipAll(recitingWord);
                        break;
                }
                hintCount++;
            }
        });
    }

    //当输错太多次调用
    private void tipAll(String recitingWord) {
        String hint = "正确答案: " + recitingWord;
        if (displayingInput != null && !displayingInput.equals("")) {
            hint = hint + "\n" + "你的答案: " + displayingInput;
        }
        tv_spelling_hint.setText(hint);
        tv_spelling_mode_hint_button.setVisibility(View.GONE);
    }


    private int[] indexOfHintWord;

    //当输错第二次的时候调用,显示单词的一部分,随机抽掉几个字母(1/2)
    private void tipSome(String recitingWord) {
        if (indexOfHintWord == null) {
            indexOfHintWord = generateEmptyLetterIndex(recitingWord.length());
        }
        char[] letters = recitingWord.toCharArray();
        for (int i : indexOfHintWord) {
            letters[i] = '_';
        }
        String hintWord = new String(letters);
        String hint = "单词的部分字母是: " + hintWord;
        tv_spelling_hint.setText(hint);
    }

    //生成一个不重复的数组,存储的数字不大于传入的参数-1,不小于0;用做随机抽取单词内的字母的下标
    private int[] generateEmptyLetterIndex(int StringLength) {
        int[] a = new int[StringLength / 2];
        int count = 0;
        while (count < a.length) {
            boolean flag = true;
            int rn = (int) (Math.random() * StringLength);
            for (int i : a) {
                if (rn == i) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                a[count] = rn;
                count++;
            }
        }
        return a;
    }


    //播放单词的声音,当输错第一次时调用
    private void playAudio(String recitingWord) {
        MyApplication.getPronunciation().playAudio(recitingWord);
    }

    /**
     * 这个方法会把传入的bean中随机抽取一个例句显示到控件上
     *
     * @param bean 传入的bean
     */
    public void setReciteWordsContent(ExampleSentenceBean bean) {
        Log.d(TAG, "setReciteWordsContent: ");
        if (bean == null || bean.getData() == null || bean.getData().size() == 0) {
            rw_contentwords.setReciteWords(mWordBean.getContent(), mWordBean.getContent());
            tv_sentence_explanation.setText("");
            tv_sentence_explanation.setVisibility(View.GONE);
            return;
        }
        int number = bean.getData().size();
        int index = (int) (Math.random() * number);//随机取其中一个例句显示
        String keyword = bean.getData().get(index).getWord();
        if (keyword == null || keyword.equals("")) keyword = mWordBean.getContent();
        String sentence = bean.getData().get(index).ridTag();
        String keywordInSentence = bean.getData().get(index).getKeywordInSentence();
        String explanation = bean.getData().get(index).getTranslation();
        rw_contentwords.setReciteWords(sentence, keyword, keywordInSentence);
        tv_sentence_explanation.setText(explanation);
        rw_contentwords.postDelayed(new Runnable() {
            @Override
            public void run() {
                rw_contentwords.requestTextFocus();
            }
        }, 500);
    }

    /**
     * 当从别的界面切换过来的时候需要调用这个方法刷新显示的内容并重置各种flag,
     * 使得这个fragment像是新的一样
     * ----------------------------------------------------------------
     * 根据传入的单词的WordBean和ExampleSentenceBean来设置显示的内容
     */
    private void refreshContent() {
        Log.d(TAG, "refreshContent: ");
        recitingWord = mWordBean.getContent();
        hintCount = 0;
        indexOfHintWord = null;
        tv_word_explanation_cn.setText(mWordBean.getChineseDefinition());
        setReciteWordsContent(mSentence);
    }

    private WordBean mWordBean;
    private ExampleSentenceBean mSentence;

    /**
     * 这个方法要在resume之前调用
     *
     * @param wordBean 单词的信息
     * @param sentence 句子的信息
     */
    public void setContent(@NonNull WordBean wordBean, ExampleSentenceBean sentence) {
        mWordBean = wordBean;
        mSentence = sentence;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContent();
    }


    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        displayingInput = text.toString();
        tv_spelling_hint.setText("按确认键提交");
        tv_spelling_mode_hint_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheck(boolean isInputCorrect) {
        if (isInputCorrect) {
            if (listener != null) {
                listener.onWordCorrectListener(recitingWord, hintCount < 2);
                return;
            }
        }
        //如果填写不正确点击
        switch (hintCount) {
            case 0: //第一次提示应该要播放声音来提示
                playAudio(recitingWord);
                break;
            case 1: //第二次提示,要改变下方的文字提示信息,显示这个单词的一部分做提示
                tipSome(recitingWord);
                break;
            default: //第三次以上,在下方直接显示整个单词内容,同时显示输入的单词做对照
                tipAll(recitingWord);
                break;
        }
        hintCount++;
    }


    public interface OnWordCorrectListener {
        //用来翻页回调
        void onWordCorrectListener(String word, boolean isCorrect);
    }


}
