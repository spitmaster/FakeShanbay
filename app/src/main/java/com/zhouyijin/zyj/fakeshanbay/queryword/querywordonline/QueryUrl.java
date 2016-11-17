package com.zhouyijin.zyj.fakeshanbay.queryword.querywordonline;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhouyijin on 2016/10/23.
 */

public class QueryUrl {

    private QueryUrl() {
    }

    public static final String SHANBAY_QUERY_WORD_API = IQueryWordOnline.SHANBAY_QUERY_WORD_API;

    public static String genarateQueryWordUrl(String word) {
        String url = SHANBAY_QUERY_WORD_API + word;
//        try {
//            url = URLEncoder.encode(url, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return url;
    }
}
