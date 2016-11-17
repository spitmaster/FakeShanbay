package com.zhouyijin.zyj.fakeshanbay.queryword.queryword;

import android.util.Log;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.queryword.dictionary.LocalDictionary;
import com.zhouyijin.zyj.fakeshanbay.queryword.dictionary.LocalDictionaryImp;
import com.zhouyijin.zyj.fakeshanbay.queryword.querywordonline.IQueryWordOnline;
import com.zhouyijin.zyj.fakeshanbay.queryword.querywordonline.QueryWordOnlineIMP;
import com.zhouyijin.zyj.fakeshanbay.queryword.wordCache.IWordCache;
import com.zhouyijin.zyj.fakeshanbay.queryword.wordCache.MyWordCache;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>
 * this is singleton class
 */

public class QueryWordIMP implements IQueryWord {
    private QueryWordIMP() {
        mWordCache = MyWordCache.getInstance();
        mQueryWordOnline = QueryWordOnlineIMP.getInstance();
        localDictionary = LocalDictionaryImp.getInstance();
    }

    public static QueryWordIMP getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * a public method to set the other IWordCache's implement
     *
     * @param wordCache you could set a new IWordCache's implement instead of the default IWordCache's implement.
     */
    public void setWordCache(IWordCache wordCache) {
        mWordCache = wordCache;
    }

    /**
     * same as setWordCache
     *
     * @param imp
     */
    public void setQueryWordOnline(IQueryWordOnline imp) {
        mQueryWordOnline = imp;
    }

    public void setLocalDictionary(LocalDictionary localDictionary) {
        this.localDictionary = localDictionary;
    }

    /**
     * you can replace a new implement as you wish
     */
    private IWordCache mWordCache;
    private IQueryWordOnline mQueryWordOnline;
    private LocalDictionary localDictionary;


    @Override
    public void queryWord(String word, OnQuerySucceedListener listener) {
        WordBean bean = mWordCache.getWord(word);
        //如果内存没有获取到单词,则从本地数据库获取
        if (bean == null) {
            bean = localDictionary.queryWord(word);
        }

        //如果本地数据库没有,则从网络获取
        if (bean == null) {
            mQueryWordOnline.queryWordOnline(word, listener);
        } else {
            listener.onQuerySucceed(bean);
        }
    }


    private static class InstanceHolder {
        private static final QueryWordIMP instance = new QueryWordIMP();
    }
}
