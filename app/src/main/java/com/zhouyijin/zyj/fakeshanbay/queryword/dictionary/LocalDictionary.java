package com.zhouyijin.zyj.fakeshanbay.queryword.dictionary;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;

/**
 * Created by zhouyijin on 2016/11/7.
 * <p>
 * 操作本地词典的接口,
 * <p>
 * 一般在网上查过的单词,所有内容都会存放到本地词库中,以节省流量
 */

public interface LocalDictionary {

    WordBean queryWord(String word);

    void putWord(WordBean bean);

}
