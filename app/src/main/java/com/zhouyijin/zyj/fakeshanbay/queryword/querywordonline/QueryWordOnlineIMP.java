package com.zhouyijin.zyj.fakeshanbay.queryword.querywordonline;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.queryword.dictionary.LocalDictionary;
import com.zhouyijin.zyj.fakeshanbay.queryword.dictionary.LocalDictionaryImp;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;
import com.zhouyijin.zyj.fakeshanbay.queryword.wordCache.IWordCache;
import com.zhouyijin.zyj.fakeshanbay.queryword.wordCache.MyWordCache;

/**
 * Created by zhouyijin on 2016/10/22.
 * <p>
 * 单例模式,持有一个
 */

public class QueryWordOnlineIMP implements IQueryWordOnline {

    private IWordCache wordCache;
    private LocalDictionary localDictionary;

    private QueryWordOnlineIMP() {
        wordCache = MyWordCache.getInstance();
        localDictionary = LocalDictionaryImp.getInstance();
    }

    public static QueryWordOnlineIMP getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void queryWordOnline(String word, final IQueryWord.OnQuerySucceedListener listener) {
        String url = QueryUrl.genarateQueryWordUrl(word);
        NetworkConnection.getInstance()
                .getWordBean(url, new NetworkConnection.OnWordBeanResponded() {
                    @Override
                    public void onWordBeanResponded(WordBean result) {
                        listener.onQuerySucceed(result);
                        if (result == null) {
                            return;
                        }
                        wordCache.putWord(result);
                        localDictionary.putWord(result);
                    }
                });
    }

    private static class InstanceHolder {
        private static final QueryWordOnlineIMP instance = new QueryWordOnlineIMP();
    }

}
