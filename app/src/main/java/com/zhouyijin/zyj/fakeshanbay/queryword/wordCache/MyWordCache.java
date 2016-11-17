package com.zhouyijin.zyj.fakeshanbay.queryword.wordCache;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>          the default implement of IWordCache which is inefficient
 *
 * you can make your own implement
 *
 *
 * this class is singleton
 * it is used for save the using word in the RAM to prevent read disk repeatedly
 * <p>
 * <p>
 * it use a HashMap to saving the data
 */
// TODO: 2016/10/22 还要实现从数据库获取数据
public class MyWordCache implements IWordCache {

    private Map<String, WordBean> cacheMap;

    private MyWordCache() {
        cacheMap = new HashMap<>();
    }

    public static MyWordCache getInstance() {
        return CacheHolder.myWordCache;
    }


    /**
     * get the wordBean that represent query word
     * @param word the specific word
     * @return wordBean
     */
    @Override
    public WordBean getWord(String word) {
        return cacheMap.get(word);
    }

    /**
     * this method is used for saving specific word to the hashmap,
     *
     * @param wordBean the specific word's wordBean
     * @return return the result of whether saving is successful
     * <p>
     * if the map is already has this word then return false;
     */
    @Override
    public boolean putWord(WordBean wordBean) {
        if (wordBean == null) {
            return false;
        }
        cacheMap.put(wordBean.getContent(), wordBean);
        return true;
    }




















    private static class CacheHolder {
        private static final MyWordCache myWordCache = new MyWordCache();
    }

}
