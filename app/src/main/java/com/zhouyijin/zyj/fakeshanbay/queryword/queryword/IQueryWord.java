package com.zhouyijin.zyj.fakeshanbay.queryword.queryword;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;

/**
 * Created by zhouyijin on 2016/10/22.
 */

public interface IQueryWord {

    /**
     *
     * @param word  the specific word that you want to query
     * @param listner   the listener's method will be invoke when query has been finished;
     */
    void queryWord(String word, OnQuerySucceedListener listner);

    interface OnQuerySucceedListener {

        /**
         * this method may be invoked in the sub_thread;
         * be careful,if you update your UI in this method may cause a exception,please use handler to update UI
         * @param wordBean
         */
        void onQuerySucceed(WordBean wordBean);
    }

}
