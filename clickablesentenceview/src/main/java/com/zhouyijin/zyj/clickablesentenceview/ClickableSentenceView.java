package com.zhouyijin.zyj.clickablesentenceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类用显示文字,并使得点击的文字会回调一个函数,回调的函数会传入点击的文字的String
 * 文字以空格的形式分开
 * <p>
 * Created by zhouyijin on 2016/10/25.
 * <p>
 * 以下是attrs中的属性
 * <declare-styleable name="ClickableSentenceView">
 * <attr name="WordsSize" format="dimension"/>
 * <attr name="WordsInterval" format="dimension"/>
 * <attr name="WordsColor" format="color"/>
 * <attr name="WordsBackgroundColor" format="color"/>
 * <attr name="Sentence" format="string"/>
 * <attr name="RowInterval" format="dimension"/>
 * </declare-styleable>
 * 可以自定义的属性:
 * 1.文字的大小
 * 2.各单词之间的距离
 * 3.单词的颜色
 * 4.单词的背景的颜色
 * 6.文字的字体
 */
public class ClickableSentenceView extends ViewGroup {

    public static final String TAG = ClickableSentenceView.class.getSimpleName();

    //这个变量只是在measure的时候记录行数,方便计算的,不用管
    private int mRows = 0;

    private String mKeyword;

    public ClickableSentenceView(Context context) {
        super(context);
    }

    public ClickableSentenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickableSentenceView);
        mTextSize = a.getDimensionPixelSize(R.styleable.ClickableSentenceView_WordsSize, sp2px(16));
        mWordsInterval = a.getDimensionPixelSize(R.styleable.ClickableSentenceView_WordsInterval, sp2px(8));
        mWordsColor = a.getColor(R.styleable.ClickableSentenceView_WordsColor, Color.BLACK);
        mWordsBackgroundColor = a.getColor(R.styleable.ClickableSentenceView_WordsBackgroundColor, Color.TRANSPARENT);
        mSentence = a.getString(R.styleable.ClickableSentenceView_Sentence);
        mRowsInterval = a.getDimensionPixelSize(R.styleable.ClickableSentenceView_RowInterval, 4);
        a.recycle();
        setSentence(mSentence);
    }

    public ClickableSentenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private int mTextSize = 16;

    public void setTextSizeSP(int size) {
        mTextSize = size;
        for (TextView tv : mWordsTextView) {
            tv.setTextSize(size);
        }
        requestLayout();
        invalidate();
    }

    private int mWordsColor = Color.BLACK;

    public void setWordsColor(int color) {
        mWordsColor = color;
        for (TextView tv : mWordsTextView) {
            tv.setTextColor(color);
        }
        invalidate();
    }


    private int mWordsBackgroundColor = Color.TRANSPARENT;

    public void setWordsBackgroundColor(int color) {
        mWordsBackgroundColor = color;
        for (TextView tv : mWordsTextView) {
            tv.setBackgroundColor(color);
        }
        invalidate();
    }


    private Typeface mTypeface;

    /**
     * 单词间间距
     */
    private int mWordsInterval = sp2px(8);

    public void setWordsIntervalPixel(int interval) {
        mWordsInterval = interval;
        requestLayout();
        invalidate();
    }

    /**
     * 行间间距
     */
    private int mRowsInterval = 8;

    public void setRowsIntervalPixel(int interval) {
        mRowsInterval = interval;
        requestLayout();
        invalidate();
    }

    private String mSentence;
    /**
     * 把每个单独的单词保存在这个list中
     */
    private List<String> mSplitWords;

    /**
     * 这个list保存的是所有textViews that represent the spilt words;
     */
    private List<TextView> mWordsTextView;

    private OnWordClickListener onWordClickListener;

    public void setOnWordClickListener(OnWordClickListener onWordClickListener) {
        this.setClickable(true);
        this.onWordClickListener = onWordClickListener;
    }

    public String getSentence() {
        return mSentence;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 以下是初始化数据过程
     */
    public void setSentence(String sentence) {
        setSentence(sentence, null);
    }

    public void setSentence(String sentence, String keyword) {
        if (sentence == null || sentence.equals("")) {
            return;
        }
        mSentence = sentence;
        this.mKeyword = keyword;
        mSplitWords = splitSentence(sentence);
        if (mWordsTextView == null) {
            mWordsTextView = new ArrayList<>(mSplitWords.size());
        } else {
            mWordsTextView.clear();
            this.removeAllViews();
        }
        for (final String word : mSplitWords) {
            TextView tv = new TextView(getContext());
            if (keyword != null && !keyword.equals("")) {
                if (word.equals(keyword)) {
                    setKeywordStyle(tv);
                }
            } else {
                setTextViewStyle(tv);
            }
            tv.setText(word);
            /**
             * 给textView设置点击事件
             */

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWordClickListener != null) {
                        onWordClickListener.onWordClick(v, word);
                    }
                    //// TODO: 2016/10/25 同时父控件也接收到点击事件,可以对textView进行操作
                    onWordClicked(v);
                }
            });

            this.addView(tv);
            mWordsTextView.add(tv);
        }
        /**
         * 准备好数据后就可以对UI重绘了!
         */
        requestLayout();
        invalidate();
    }

    private void setKeywordStyle(TextView tv) {
        tv.setTag(new Integer(1));  //这个是给keyword做个记号
        tv.setTextColor(Color.GREEN);
        tv.setTextSize(mTextSize);
        tv.setBackgroundColor(mWordsBackgroundColor);
        if (mTypeface != null) {
            tv.setTypeface(mTypeface);
        }
    }

    /**
     * 这个方法用来给每个textView设置具体的风格
     */
    private void setTextViewStyle(TextView tv) {
        tv.setTextColor(mWordsColor);
        tv.setTextSize(mTextSize);
        tv.setBackgroundColor(mWordsBackgroundColor);
        if (mTypeface != null) {
            tv.setTypeface(mTypeface);
        }
    }

    /**
     * 统一给所有TextView设置具体风格
     *
     * @param wordsColor          a integer for colour
     * @param textSize            the textSize in sp
     * @param textBackgroundColor a integer for colour
     * @param typeface            you can customize your own typeface by send a new typeface
     */
    public void setTextViewStyle(int wordsColor, int textSize, int textBackgroundColor, int rowInterval, Typeface typeface) {
        if (wordsColor != -1) {
            this.mWordsColor = wordsColor;
        }
        if (textSize != -1) {
            this.mTextSize = textSize;
        }
        if (textBackgroundColor != -1) {
            this.mWordsBackgroundColor = textBackgroundColor;
        }
        if (typeface != null) {
            this.mTypeface = typeface;
        }
        if (rowInterval != -1) {
            mRowsInterval = rowInterval;
        }
        for (TextView tv : mWordsTextView) {
            tv.setTextColor(mWordsColor);
            tv.setTextSize(mTextSize);
            tv.setBackgroundColor(mWordsBackgroundColor);
            if (mTypeface != null) {
                tv.setTypeface(mTypeface);
            }
        }
        requestLayout();
        invalidate();
    }


    /**
     * this method is invoked when the word has been clicked
     *
     * @param v the view that your clicked
     */
    private void onWordClicked(View v) {

    }


    /**
     * 这个方法使输入的句子能够按单词分开
     *
     * @param sentence 输入一个整句
     * @return 把句子里的单词按空格分开, 放入一个ArrayList中返回
     */
    private List<String> splitSentence(String sentence) {
        if (sentence == null || sentence.equals("")) {
            return null;
        }
        String[] words = sentence.trim().split("\\s+");
        List<String> result = new ArrayList<>(words.length + 2);
        for (String word : words) {
            result.add(word);
        }
        return result;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 以下是measure过程
     * 先计算宽度,计算行的时候就已经算出列数了
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //遍历子view,先要获取子TextView的大小
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        //然后根据子view的大小来确定 整体大小
        /**
         * 先获取宽度再获取高度
         */
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 获取宽度
     * <p>
     * 并且会给一个成员变量复制mRows这个变量代表计算出的多少行
     *
     * @param widthMeasureSpec 父View传入的widthMeasureSpec
     * @return 计算出的宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        //计算的时候要把padding去掉,计算完再加回去,排除padding对计算的影响
        int specWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int result = 0;
        /**
         * 先讨论当没有子View的时候
         */
        if (mWordsTextView == null || mWordsTextView.size() == 0) {  //当没有单词的时候
            switch (specMode) {
                case MeasureSpec.EXACTLY:
                    result = MeasureSpec.getSize(widthMeasureSpec);
                    mRows = 0;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    result = 0;
                    mRows = 0;
                    break;
                case MeasureSpec.AT_MOST:
                    result = 0;
                    mRows = 0;
                    break;
            }
            return result;
        }

        /**
         * 现在讨论当单词数>0的时候
         */
        mRows = 1;
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                /**
                 * 这里同样要计算下,不然不知道有多少行,到时候没法计算高度
                 */
                result = specWidth;
                /**
                 * 下面计算在Exactly模式下有多少行;
                 */
                int rowWidth1 = 0;  //当前测量行的宽度
                int count1 = mWordsTextView.size(); //子View的个数
                for (int i = 0; i < count1; i++) {
                    int viewWidth = mWordsTextView.get(i).getMeasuredWidth();   //子View的宽度
                    /**
                     * 一个特殊情况view比限定宽度还宽,那么要新开一行去放置
                     */
                    if (viewWidth > specWidth) {    //如果子view比限定的行都宽
                        //如果本行有单词了,那么换行显示
                        if (rowWidth1 > 0) {    //如果本行有单词显示了,则换行去显示这个超长的单词
                            mRows++;    //直接换行,行数+1
                        }
                        rowWidth1 = viewWidth;
                        //并进行下一次循环
                        continue;
                    }

                    /**
                     * 这里判断view没有超过限定宽度那么宽的时候的情况
                     */
                    //行首的情况
                    if (rowWidth1 == 0) {   //这种情况一般是全句第一个单词
                        //如果是首行的话,直接不用判断了,直接加上就行了,肯定不会超过限制的,不换行
                        rowWidth1 = viewWidth;
                    } else {
                        //如果加上子View的宽和空格,不会超过限制,那么宽度就是直接加上的宽度
                        if (rowWidth1 + mWordsInterval + viewWidth <= specWidth) {
                            rowWidth1 = rowWidth1 + mWordsInterval + viewWidth;
                        } else {
                            //如果加上子view和空格,超过限制,那么另起一行算宽度
                            rowWidth1 = viewWidth;
                            mRows++;    //行数+1
                        }
                    }
                }
                break;
            /**
             * 这种情况就有多长给多长就行
             * 遍历所有view把宽度加起来,在把每个word的中间间隔添加上,就行了
             */
            case MeasureSpec.UNSPECIFIED:
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    result += child.getMeasuredWidth();
                    if (i != count - 1) {
                        result += mWordsInterval;
                    }
                }
                //这种方式总共只有一行
                mRows = 1;
                break;
            /**
             * 关键是这个,这个才是重点
             */
            case MeasureSpec.AT_MOST:
                /**
                 * 因为这个句子是有顺序的,所以我们要用给定的顺序来计算,用mWordsTextView,而不用getChildAt
                 */
                int viewsCount = mWordsTextView.size();
                int maxWidth = 0;   //最大的宽度
                int rowWidth = 0;   //当前测量行的宽度
                for (int i = 0; i < viewsCount; i++) {
                    View textView = mWordsTextView.get(i);
                    int viewWidth = textView.getMeasuredWidth();


                    /**
                     * 这是个特例,如果一个子view比限制大,那么直接就用上限就行,但要计算行数
                     */
                    if (viewWidth >= specWidth) {
                        maxWidth = specWidth;   //直接使用最大的宽度
                        if (rowWidth > 0) {    //如果这个是在行首,则不用加行数,直接占用这行就行
                            mRows++;    //行数+1
                        }
                        rowWidth = viewWidth;   //正在测量的行宽则是这个view的宽度

                        continue;    //进行下次循环
                    }

                    /**
                     * 先判断加上这个view会不会超过限制,不会则直接加上,
                     * 会超过限制则另起一行
                     */
                    if (rowWidth == 0) {
                        //如果是首行的话,直接不用判断了,直接加上就行了,肯定不会超过限制的
                        rowWidth = viewWidth;
                    } else {
                        //如果加上子View的宽和空格,不会超过限制,那么宽度就是直接加上的宽度
                        if (rowWidth + mWordsInterval + viewWidth <= specWidth) {
                            rowWidth = rowWidth + mWordsInterval + viewWidth;
                        } else {
                            //如果加上子view和空格,超过限制,那么另起一行算宽度
                            rowWidth = viewWidth;
                            mRows++;    //行数+1
                        }
                    }
                    //如果计算出新的宽度比记录大的,那么覆盖原先记录的
                    if (rowWidth > maxWidth) {
                        maxWidth = rowWidth;
                    }
                }
                if (maxWidth < specWidth) {
                    result = maxWidth;
                } else {
                    result = specWidth;
                }
                break;
        }
        //把padding加回去
        result = result + getPaddingLeft() + getPaddingRight();
        return result;
    }

    /**
     * 获取高度,高度是要参照测量好的宽度来获取的,不然得不到高度
     * 计算高度需要一个成员变量mRows
     * 计算宽度的时候就已经算出mRows了,通过加上每行的高度,每行的间隔,上下padding得出高度
     *
     * @param heightMeasureSpec 父View的heightMeasureSpec
     * @return 计算出的高度
     */
    private int measureHeight(int heightMeasureSpec) {
        if (mWordsTextView == null) {
            return heightMeasureSpec;
        }
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specHeight;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            if (mRows > 1) {
                result = getPaddingTop() + getPaddingBottom() + mRows * mWordsTextView.get(0).getMeasuredHeight() + (mRows - 1) * mRowsInterval;
            } else {
                //如果只有一行则不计算间距了
                result = getPaddingTop() + getPaddingBottom() + mRows * mWordsTextView.get(0).getMeasuredHeight();
            }
        } else {
            //如果是AT_MOST
            if (mRows > 1) {
                result = getPaddingTop() + getPaddingBottom() + mRows * mWordsTextView.get(0).getMeasuredHeight() + (mRows - 1) * mRowsInterval;
            } else {
                //如果只有一行则不计算间距了
                result = getPaddingTop() + getPaddingBottom() + mRows * mWordsTextView.get(0).getMeasuredHeight();
            }
            //如果超过限制,则用限制的高度
            if (result > specHeight) {
                result = specHeight;
            }
        }
        return result;
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------------
     * 以下是layout过程
     */


    @Override
    protected synchronized void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mWordsTextView == null) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int parentLeft = getPaddingLeft();
        int parentTop = getPaddingTop();
        //如果只有一行的情况下!
        if (mRows == 1) {
            int childLeft = 0;
            int childTop = 0;
            int count = mWordsTextView.size();
            for (int i = 0; i < count; i++) {
                View child = mWordsTextView.get(i);

                //给child设置位置
                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
                childLeft = childLeft + mWordsInterval + child.getMeasuredWidth();
            }
        } else {
            //如果有多行的情况下!!!!!!!!!!!!!!!!!!!
            int childLeft = 0;
            int childTop = 0;
            int availableHeight = height - getPaddingTop() - getPaddingBottom();
            int availableWidth = width - getPaddingLeft() - getPaddingRight();
            int count = mWordsTextView.size();
            //上一个view分配后最后一行已经占用的宽度,用来判断需不需要换行
            int usedWidth = 0;
            for (int i = 0; i < count; i++) {
                View child = mWordsTextView.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (usedWidth + childWidth + mWordsInterval > availableWidth) {
                    //如果view加到行尾会超出范围,则换行
                    childLeft = 0;
                    childTop = childTop + childHeight + mRowsInterval;
                    usedWidth = childWidth;
                } else {
                    //如果view加到行尾不会超出范围,则直接加到行尾
                    if (usedWidth == 0) {
                        //第一行第一个view会走到这行代码
                        childLeft = 0;
                        usedWidth = usedWidth + childWidth;
                    } else {
                        childLeft = usedWidth + mWordsInterval;
                        //更新已占用的宽度
                        usedWidth = usedWidth + childWidth + mWordsInterval;
                    }
                }
                child.layout(
                        childLeft + parentLeft,
                        childTop + parentTop,
                        childLeft + parentLeft + childWidth,
                        childTop + parentTop + childHeight);
            }
        }
    }


    /**
     * 这个接口的方法会在,指定单词被点击后回调,传入单词的word
     */
    public interface OnWordClickListener {
        void onWordClick(View view, String word);
    }


    public int px2sp(float px) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    public int sp2px(float sp) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

}
