package com.zhouyijin.zyj.atable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhouyijin on 2016/11/14.
 * 一个图表控件
 */

public class ATable extends View {

    public static final int GRIDLINE_SYTLE_DASH = 1;
    public static final int GRIDLINE_SYTLE_FULL = 2;
    public static final int GRIDLINE_SYTLE_NONE = 0;


    public ATable(Context context) {
        this(context, null);
    }

    public ATable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ATable);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ATable_XaxisNameColor) {
                mXaxisNameColor = a.getColor(R.styleable.ATable_XaxisNameColor, Color.GRAY);
            } else if (attr == R.styleable.ATable_XaxisNameSize) {
                mXaxisNameSize = a.getDimension(R.styleable.ATable_XaxisNameSize, sp2px(16));
            } else if (attr == R.styleable.ATable_YaxisTextSize) {
                mYaxisTextSize = a.getDimension(R.styleable.ATable_YaxisTextSize, sp2px(16));
            } else if (attr == R.styleable.ATable_YaxisTextColor) {
                mYaxisTextColor = a.getColor(R.styleable.ATable_YaxisTextColor, Color.GRAY);
            } else if (attr == R.styleable.ATable_YaxisTextCount) {
                mYaxisTextCount = a.getInt(R.styleable.ATable_YaxisTextCount, 5);
                if (2 > mYaxisTextCount) mYaxisTextCount = 2;
                else if (mYaxisTextCount > 20) mYaxisTextCount = 20;
            } else if (attr == R.styleable.ATable_CurveColor) {
                mCurveColor = a.getColor(R.styleable.ATable_CurveColor, Color.GREEN);
            } else if (attr == R.styleable.ATable_ShadowColor) {
                mShadowColor = a.getColor(R.styleable.ATable_ShadowColor, 0x3300FF00);
            } else if (attr == R.styleable.ATable_PointColor) {
                mPointColor = a.getColor(R.styleable.ATable_PointColor, Color.GREEN);
            } else if (attr == R.styleable.ATable_PointBorderColor) {
                mPointBorderColor = a.getColor(R.styleable.ATable_PointBorderColor, Color.WHITE);
            } else if (attr == R.styleable.ATable_GridLineColor) {
                mGridLineColor = a.getColor(R.styleable.ATable_GridLineColor, Color.GRAY);
            } else if (attr == R.styleable.ATable_GridLineStyle) {
                mGridLineStyle = a.getInt(R.styleable.ATable_GridLineStyle, GRIDLINE_SYTLE_DASH);
            } else if (attr == R.styleable.ATable_YaxisTableInterval) {
                mYaxisTableInterval = a.getDimension(R.styleable.ATable_YaxisTableInterval, sp2px(4));
            } else if (attr == R.styleable.ATable_YaxisTextInterval) {
                mYaxisTextInterval = a.getDimension(R.styleable.ATable_YaxisTextInterval, sp2px(8));
            } else if (attr == R.styleable.ATable_TableNameInterval) {
                mTableNameInterval = a.getDimension(R.styleable.ATable_TableNameInterval, sp2px(4));
            } else if (attr == R.styleable.ATable_PointRadius) {
                pointRadius = a.getDimension(R.styleable.ATable_PointRadius, 8);
            } else if (attr == R.styleable.ATable_PointBorderWidth) {
                pointBorderWidth = a.getDimension(R.styleable.ATable_PointBorderWidth, 4);
            }
        }
        a.recycle();
        mPaint = new Paint();
        initPaint();
    }

    private Paint mPaint;

    private String[] mXaxisNames = {"test1", "test2", "test3", "test4", "test5"};    //横坐标每个刻度的名字
    private int[] mData = {10, 20, 30, 40, 50};        //每个刻度对应的值
    private float mXaxisNameSize = sp2px(16);        //横坐标刻度文字的大小
    private int mXaxisNameColor = Color.GRAY;   //横坐标刻度文字的颜色
    private float mYaxisTextSize = sp2px(16);        //纵坐标刻度文字的大小
    private int mYaxisTextColor = Color.GRAY;   //纵坐标刻度文字的颜色
    private int mYaxisTextCount = 5;        //纵坐标刻度的数量
    private int mCurveColor = Color.GREEN;  //点的连接线的颜色
    private int mShadowColor = 0x3300FF00;  //阴影的颜色,一般设置成透明比较好
    private int mPointColor = Color.GREEN;  //点的颜色
    private int mPointBorderColor = Color.WHITE;    //点的边缘的颜色
    private int mGridLineColor = Color.GRAY;  //网格线的颜色
    private int mGridLineStyle = GRIDLINE_SYTLE_DASH;   //网格线的式样
    private float pointRadius = sp2px(8);
    private float pointBorderWidth = 4;

    private float mYaxisTextInterval = sp2px(8); //纵坐标刻度文字的间隔;按sp来算吧
    private float mYaxisTableInterval = sp2px(4);//纵坐标刻度文字与表格之间的间隔
    private float mTableNameInterval = sp2px(4); //横坐标的名字和表格之间的空隙

    public void setCurveColor(int curveColor) {
        this.mCurveColor = curveColor;
        invalidate();
    }


    public void setGridLineColor(int gridLineColor) {
        this.mGridLineColor = gridLineColor;
        invalidate();
    }

    public void setGridLineStyle(int gridLineStyle) {
        if (gridLineStyle < 0 || gridLineStyle > 2) return;
        this.mGridLineStyle = gridLineStyle;
        invalidate();
    }

    public void setPointBorderColor(int pointBorderColor) {
        this.mPointBorderColor = pointBorderColor;
        invalidate();
    }

    public void setPointColor(int pointColor) {
        this.mPointColor = pointColor;
        invalidate();
    }

    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
        invalidate();
    }

    public void setTableNameInterval(float tableNameInterval) {
        if (mTableNameInterval < 0) tableNameInterval = 0;
        this.mTableNameInterval = tableNameInterval;
        invalidate();
    }

    public void setXaxisNameColor(int XaxisNameColor) {
        this.mXaxisNameColor = XaxisNameColor;
        invalidate();
    }

    public void setXaxisNames(String[] XaxisNames) {
        this.mXaxisNames = XaxisNames;
        invalidate();
    }

    public void setXaxisNameSize(float XaxisNameSize) {
        if (mXaxisNameSize < 0) XaxisNameSize = 0;
        this.mXaxisNameSize = XaxisNameSize;
        invalidate();
    }

    public void setYaxisTableInterval(float YaxisTableInterval) {
        if (YaxisTableInterval < 0) YaxisTableInterval = 0;
        this.mYaxisTableInterval = YaxisTableInterval;
        invalidate();
    }

    public void setYaxisTextColor(int YaxisTextColor) {
        this.mYaxisTextColor = YaxisTextColor;
        invalidate();
    }

    public void setYaxisTextCount(int YaxisTextCount) {
        if (YaxisTextCount < 2) YaxisTextCount = 2;
        else if (YaxisTextCount > 20) YaxisTextCount = 20;
        this.mYaxisTextCount = YaxisTextCount;
        invalidate();
    }

    public void setYaxisTextInterval(float YaxisTextInterval) {
        if (YaxisTextInterval < 0) YaxisTextInterval = 0;
        this.mYaxisTextInterval = YaxisTextInterval;
        invalidate();
    }

    public void setYaxisTextSize(float YaxisTextSize) {
        if (YaxisTextSize < 0) YaxisTextSize = 0;
        this.mYaxisTextSize = YaxisTextSize;
        invalidate();
    }

    public void setPointBorderWidth(float pointBorderWidth) {
        if (pointBorderWidth < 0) pointBorderWidth = 0;
        this.pointBorderWidth = pointBorderWidth;
    }

    public void setPointRadius(float pointRadius) {
        if (pointRadius < 0) pointRadius = 0;
        this.pointRadius = pointRadius;
    }

    public void setData(int[] data) {
        if (data == null) data = new int[]{0, 0};
        this.mData = data;
        invalidate();
    }

    public int[] getData() {
        return mData;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getWidthMeasuredSpc(widthMeasureSpec),
                getHeightMeasuredSpec(heightMeasureSpec));
    }

    private int getHeightMeasuredSpec(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                result = (int) ((getFontHeight(mYaxisTextSize) + mYaxisTextInterval) * (mYaxisTextCount + 1)
                        + mTableNameInterval + getFontHeight(mXaxisNameSize));
                break;
        }
        return result;
    }

    private float getFontHeight(float px) {
        mPaint.setTextSize(px);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    private int getWidthMeasuredSpc(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                float wordWidth = getBigestXaxisNameWidth();
                float numberWidth = getBigestNumberWidth() + mYaxisTableInterval;
                float width = wordWidth * mXaxisNames.length
                        + numberWidth + getPaddingLeft() + getPaddingRight();
                if (size != 0) result = (int) Math.min(width, size);
                else result = (int) width;
                break;
        }
        return result;
    }

    private float getBigestNumberWidth() {
        if (mData == null || mData.length == 0) {
            return 0;
        }
        float x = 0;
        for (int i : mData) {
            String w = String.valueOf(i);
            float wordWidth = getWordWidth(w, mYaxisTextSize);
            if (wordWidth > x) x = wordWidth;
        }
        return x;
    }

    private float getBigestXaxisNameWidth() {     //获取横坐标最大的一个刻度名字的长度
        float x = 0;
        if (mXaxisNames == null || mXaxisNames.length == 0) {
            return 0;
        }
        for (String s : mXaxisNames) {
            float a = getWordWidth(s, mXaxisNameSize);
            if (a > x) x = a;
        }
        return x;
    }

    /**
     * 计算一段字符串在指定sp下的宽度
     *
     * @param word 指定的字符串
     * @param px   指定的px值
     * @return
     */
    private float getWordWidth(String word, float px) {
        if (word == null || word.length() == 0) {
            return 0;
        }
        mPaint.setTextSize(px);
        float width = mPaint.measureText(word);
        return width;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        drawX(canvas);
        drawY(canvas);  //这个方法必须放在drawShadow和drawLine和drawPoint之前,计算刻度关系后面的绘制坐标
        drawTable(canvas);
        drawShadow(canvas);
        drawLine(canvas);
        drawPoint(canvas);
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(null);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void drawPoint(Canvas canvas) {
        canvas.save();
        float left = getPaddingLeft() + getBigestNumberWidth() + mYaxisTableInterval + blockWidth / 2;
        canvas.translate(left, getPaddingTop());
        mPaint.setPathEffect(null);
        mPaint.setStrokeWidth(pointBorderWidth);
        for (PointF p : dataPoints) {
            mPaint.setColor(mPointColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(p.x, p.y, pointRadius, mPaint);
            mPaint.setColor(mPointBorderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(p.x, p.y, pointRadius, mPaint);
        }
        canvas.restore();
    }

    private PointF[] dataPoints;

    private void drawShadow(Canvas canvas) {
        mPaint.setColor(mShadowColor);
        canvas.save();
        Path shadowPath = new Path();
        float left = getPaddingLeft() + getBigestNumberWidth() + mYaxisTableInterval + blockWidth / 2;
        float bottom = blockHeight * (scales.length + 1);
        canvas.translate(left, getPaddingTop());
        dataPoints = new PointF[mData.length];
        for (int i = 0; i < mData.length; i++) {
            float x = i * blockWidth;
            float y = getPointY(mData[i]);
            dataPoints[i] = new PointF(x, y);

        }
        shadowPath.reset();
        shadowPath.moveTo(0, bottom);
        for (PointF p : dataPoints) {
            shadowPath.lineTo(p.x, p.y);
        }
        shadowPath.lineTo(dataPoints[dataPoints.length - 1].x, bottom);
        shadowPath.close();
        canvas.drawPath(shadowPath, mPaint);
        canvas.restore();
    }

    private float getPointY(int data) {
        if (data == 0) return blockHeight * scales.length;
        for (int i = 0; i < scales.length; i++) {
            if (i == scales.length - 1) break;
            if (data >= scales[i] && data <= scales[i + 1]) {
                int exceedData = data - scales[i];
                float otherY = ((exceedData * 1.0f / (scales[1] - scales[0])) * blockHeight + blockHeight * (i + 1));   //这个获取的是从下往上走的坐标
                return blockHeight * (scales.length + 1) - otherY;  //转换为从上往下的坐标
            }
        }
        return -1;
    }

    private void drawLine(Canvas canvas) {
        mPaint.setColor(mCurveColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        canvas.save();
        Path curvePath = new Path();
        float left = getPaddingLeft() + getBigestNumberWidth() + mYaxisTableInterval + blockWidth / 2;
        canvas.translate(left, getPaddingTop());
        curvePath.reset();
        curvePath.moveTo(dataPoints[0].x, dataPoints[0].y);
        for (int i = 1; i < dataPoints.length; i++) {
            curvePath.lineTo(dataPoints[i].x, dataPoints[i].y);
        }
        canvas.drawPath(curvePath, mPaint);
        canvas.restore();

    }

    private int[] scales;

    private float blockHeight;

    private void drawY(Canvas canvas) {     //绘制Y轴上的刻度
        if (mData == null || mData.length == 0) return;
        scales = getScales();
        canvas.save();
        float left = getPaddingLeft();
        canvas.translate(left, getPaddingTop());
        float height = getMeasuredHeight() - getPaddingTop() - mTableNameInterval - getFontHeight(mXaxisNameSize) - getPaddingBottom();
        int blockCount = scales.length + 1;
        blockHeight = height / blockCount;
        mPaint.setTextSize(mYaxisTextSize);
        mPaint.setColor(mYaxisTextColor);
        float firstScaleY = height - blockHeight;
        float firstScaleTextY = getFontBaselineY(firstScaleY, mPaint);
        float centerX = getBigestNumberWidth() / 2;
        for (int i = 0; i < scales.length; i++) {
            String scaleText = String.valueOf(scales[i]);
            float y = firstScaleTextY - blockHeight * i;
            canvas.drawText(scaleText, centerX, y, mPaint);
        }
        canvas.restore();
    }


    private int[] getScales() {
        int min = getMinInt(mData);
        int max = getMaxInt(mData);
        int interval = max - min;
        int count = mYaxisTextCount;
        int first = min - interval / (count - 1);
        if (first < 0) {
            first = 0;
        }
        int end = max + interval / (count - 1);
        int scaleInterval = (end - first) / (count - 1);
        int[] scales = new int[count];
        for (int i = 0; i < count; i++) {
            scales[i] = first + scaleInterval * i;
        }
        return scales;
    }

    private int getMaxInt(int[] a) {
        int x = Integer.MIN_VALUE;
        for (int i : a) {
            if (i > x) x = i;
        }
        return x;
    }

    private int getMinInt(int[] a) {
        int x = Integer.MAX_VALUE;
        for (int i : a) {
            if (i < x) x = i;
        }
        return x;
    }

    private void drawX(Canvas canvas) { //绘制X轴上的刻度名字
        if (mXaxisNames == null || mXaxisNames.length == 0) return;
        canvas.save();
        float leftEdge = getPaddingLeft() + getBigestNumberWidth() + mYaxisTableInterval;
        float width = getMeasuredWidth() - getPaddingRight() - leftEdge;
        float height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        canvas.translate(leftEdge, getPaddingTop());
        float perWidth = width / mXaxisNames.length;
        mPaint.setTextSize(mXaxisNameSize);
        mPaint.setColor(mXaxisNameColor);

        float centerY = height - getFontHeight(mXaxisNameSize) / 2;
        float textY = getFontBaselineY(centerY, mPaint);

        for (int i = 0; i < mXaxisNames.length; i++) {
            float wordWidth = mPaint.measureText(mXaxisNames[i]);
            float wordCenterX = perWidth * i + perWidth / 2;
            if (wordWidth < perWidth) {
                canvas.drawText(mXaxisNames[i], wordCenterX, textY, mPaint);
            } else {
                canvas.drawText("...", wordCenterX, textY, mPaint);
            }
        }
        canvas.restore();
    }

    private void drawTable(Canvas canvas) {
        mPaint.setColor(mGridLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        switch (mGridLineStyle) {
            case GRIDLINE_SYTLE_DASH:
                DashPathEffect dpe = new DashPathEffect(new float[]{4, 3}, 0);  //构造方法第一个参数是个数组,表示实线和空白之间的占比, 第二个参数是偏移量
                mPaint.setPathEffect(dpe);
                break;
            case GRIDLINE_SYTLE_FULL:
                mPaint.setPathEffect(null);
                break;
            case GRIDLINE_SYTLE_NONE:
                mPaint.setColor(Color.TRANSPARENT);
                break;
        }
        canvas.save();
        float left = getPaddingLeft() + getBigestNumberWidth() + mYaxisTableInterval;
        canvas.translate(left, getPaddingTop());
        float right = getMeasuredWidth() - left - getPaddingRight();
        float height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTableNameInterval - getFontHeight(mXaxisNameSize);
        RectF rect = new RectF(0, 0, right, height);
        drawHorizontalLine(canvas, rect);
        drawVerticalLine(canvas, rect);
        canvas.restore();
        mPaint.setStyle(Paint.Style.FILL);
    }

    private float blockWidth;

    private void drawVerticalLine(Canvas canvas, RectF rect) {
        Path mPath = new Path();
        blockWidth = rect.right / mData.length;
        float firstX = blockWidth / 2;
        for (int i = 0; i < mData.length; i++) {
            float x = firstX + blockWidth * i;
            mPath.reset();
            mPath.moveTo(x, 0);
            mPath.lineTo(x, rect.bottom);
            canvas.drawPath(mPath, mPaint);
        }
    }

    private void drawHorizontalLine(Canvas canvas, RectF rect) {
        Path mPath = new Path();
        Paint p = new Paint(mPaint);
        p.setPathEffect(null);
        //第一条线
        mPath.reset();
        mPath.moveTo(0, rect.bottom);
        mPath.lineTo(rect.right, rect.bottom);
        canvas.drawPath(mPath, p);
        for (int i = 1; i <= mYaxisTextCount; i++) {
            mPath.reset();
            float height = rect.bottom - i * blockHeight;
            mPath.moveTo(0, height);
            mPath.lineTo(rect.right, height);
            canvas.drawPath(mPath, mPaint);
        }
    }


    public float getYaxisTextSize() {
        return mYaxisTextSize;
    }

    public int getPointBorderColor() {
        return mPointBorderColor;
    }

    public int getPointColor() {
        return mPointColor;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public float getTableNameInterval() {
        return mTableNameInterval;
    }

    public int getXaxisNameColor() {
        return mXaxisNameColor;
    }

    public String[] getXaxisNames() {
        return mXaxisNames;
    }

    public float getXaxisNameSize() {
        return mXaxisNameSize;
    }

    public float getYaxisTableInterval() {
        return mYaxisTableInterval;
    }

    public int getYaxisTextColor() {
        return mYaxisTextColor;
    }

    public int getYaxisTextCount() {
        return mYaxisTextCount;
    }

    public float getYaxisTextInterval() {
        return mYaxisTextInterval;
    }

    public int getGridLineStyle() {
        return mGridLineStyle;
    }

    public int getGridLineColor() {
        return mGridLineColor;
    }

    public int getCurveColor() {
        return mCurveColor;
    }

    private float px2sp(float px) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    private float sp2px(float sp) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale;
    }

    private float getFontBaselineY(float centerY, Paint mPaint) {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return centerY + (fm.descent - fm.ascent) / 2 - fm.descent;
    }
}
