package com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.words_fragment;

import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;

/**
 * Created by zhouyijin on 2016/10/24.
 */

public interface IUpdateSubFragmentWords {

    void onRefreshDailySentence(DailySentenceBean bean);
    void onRefreshStudyBrief();
    void onRefreshTodayTask();

}
