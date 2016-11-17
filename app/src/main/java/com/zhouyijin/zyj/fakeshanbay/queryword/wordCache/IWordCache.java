package com.zhouyijin.zyj.fakeshanbay.queryword.wordCache;


import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;

/**
 * Created by zhouyijin on 2016/10/22.
 */

public interface IWordCache {

    /**
     * this method is used for get the specific word as a WordBean
     * @param word the specific word
     * @return
     */
    WordBean getWord(String word);

    /**
     * this method is used for save the specific word to the cache
     * @param wordBean
     * @return
     */
    boolean putWord(WordBean wordBean);

}
