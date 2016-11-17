package com.zhouyijin.zyj.fakeshanbay.tools;


import com.zhouyijin.zyj.fakeshanbay.MyApplication;

/**
 * Created by zhouyijin on 2016/10/23.
 *
 */
public class DisplayUtil {

    public static int px2dp(float px) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dp2px(float dp) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2sp(float px) {
        final float fontScale = MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    public static int sp2px(float sp) {
        final float fontScale = MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}
