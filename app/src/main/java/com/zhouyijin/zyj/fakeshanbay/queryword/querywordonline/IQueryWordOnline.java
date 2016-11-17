package com.zhouyijin.zyj.fakeshanbay.queryword.querywordonline;


import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>
 * <p>
 * this interface
 */

public interface IQueryWordOnline {

    /**
     * 等号后面加要查询的单词
     */
    String SHANBAY_QUERY_WORD_API = "https://api.shanbay.com/bdc/search/?word=";

    /**
     * send the task to the network connection queue
     *
     * @param word     the word that your want to query for.
     * @param listener when the result has been represented,listener's method will be invoke;
     */
    void queryWordOnline(String word, IQueryWord.OnQuerySucceedListener listener);

}
