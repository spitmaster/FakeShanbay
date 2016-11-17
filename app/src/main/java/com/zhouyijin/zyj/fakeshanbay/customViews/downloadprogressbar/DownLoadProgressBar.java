package com.zhouyijin.zyj.fakeshanbay.customViews.downloadprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.zhouyijin.zyj.fakeshanbay.R;


/**
 * Created by zyj on 2016/8/23.
 * 这个进度条显示事件很短,如果下载完成则自动变成透明的,不再绘制,但是还占着位置
 */
public class DownLoadProgressBar extends ProgressBar {

    /**
     * progressbar的宽度
     */
    private float barWidth = 0;

    private float viewWidth = 300;

    private Paint mPaint;
    private int mColor;


    public DownLoadProgressBar(Context context) {
        this(context, null);
    }

    public DownLoadProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownLoadProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DownLoadProgressBar);
        //如果没有设置bar的宽度,默认宽度为3
        barWidth = a.getDimension(R.styleable.DownLoadProgressBar_BarWidth, 10);
        //默认长度为300
        viewWidth = a.getDimension(R.styleable.DownLoadProgressBar_ViewWidth, 300);
        //默认颜色为白色
        mColor = a.getColor(R.styleable.DownLoadProgressBar_BarColor, Color.WHITE);

        a.recycle();

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        int result;

        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        }

        result = (int) barWidth;
        if (specSize > 0) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        int result;

        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        }

        result = (int) viewWidth;

        if (specSize > 0) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {

        double ratio = getProgress() * 1.0 / getMax();
        RectF mRectF = new RectF();
        mRectF.set(0, 0, (float) (getMeasuredWidth() * ratio), getBarWidth());
        canvas.drawRect(mRectF, mPaint);

    }

    public float getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int width) {
        if (width < 0) {
            return;
        }
        barWidth = width;
        invalidate();
    }


    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
    }

    public float getViewWidth() {
        return viewWidth;

    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
        invalidate();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        invalidate();
    }
}
