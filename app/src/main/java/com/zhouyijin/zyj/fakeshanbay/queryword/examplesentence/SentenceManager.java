package com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence;

import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;

/**
 * Created by zhouyijin on 2016/11/9.
 * <p>
 * 专门获取例句的接口
 */

public interface SentenceManager {

    //这个常量用来表示获取的sentence是系统的例句还是用户自己编写的例句
    int SYSTEM_SENTENCE = -1;
    int USER_SENTENCE = -2;

    /**
     * 获取例句
     * @param word      哪个单词的例句
     * @param callback  获取例句成功后的回调
     * @param sentenceType  如果传入SYSTEM_SENTENCE,则返回系统例句
     *                      如果传入USER_SENTENCE ,则返回用户自定义编写的例句
     */
    void getExampleSentence(String word, OnGetResult callback, int sentenceType);

    void putExampleSentence(ExampleSentenceBean bean);

    interface OnGetResult {
        void OnGetResult(ExampleSentenceBean bean);
    }

}
