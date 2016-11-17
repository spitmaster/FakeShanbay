package com.zhouyijin.zyj.fakeshanbay.dailysentence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.tools.DateLine;

/**
 * Created by zhouyijin on 2016/10/24.
 */

public class DailySentence {

//    private static class InstanceHolder {
//        private static DailySentence instance = new DailySentence();
//    }

    public static final String DAILY_SENTENCE_API = "http://open.iciba.com/dsapi/";

    public static final String DAILY_SENTENCE_DB_NAME = "daily_sentence.db";

    public static final String TABLE_NAME = "daily_sentence";
    public static final String _ID = "_id";

    //金山词霸给的id
    public static final String SID = "sid";
    //句子的音频url
    public static final String TTS = "tts";
    //句子的英文
    public static final String CONTENT = "content";
    //句子的中文
    public static final String NOTE = "note";
    //词霸小编给的评价
    public static final String TRANSLATION = "translation";
    //句子的配图(小)
    public static final String PICTURE = "picture";
    //句子的配图(大)
    public static final String PICTURE2 = "picture2";
    //每日一句的日期
    public static final String DATELINE = "dateline";
    //分享图片的url
    public static final String FENXIANG_IMG = "fenxiang_img";


    private DailySentenceSQLiteOpenHelper sqLiteOpenHelper;

    private static DailySentence instance = null;

    private DailySentence() {
        sqLiteOpenHelper = new DailySentenceSQLiteOpenHelper(
                MyApplication.getContext(),
                DAILY_SENTENCE_DB_NAME,
                null, 1);
    }

    public static DailySentence getInstance() {
        if (instance == null) {
            synchronized (DailySentence.class) {
                if (instance == null) {
                    instance = new DailySentence();
                }
            }
        }
        return instance;
    }


    public void getDailySentence(OnDailySentenceUpdate callBack) {
        if (!getDailySentenceFromLocal(callBack)) {
            getDailySentenceFromInternet(callBack);
        }
    }

    /**
     * @return 如果返回true则获取的就是今天的, 如果返回false则返回的不是今天的需要联网获取
     */
    public boolean getDailySentenceFromLocal(OnDailySentenceUpdate callBack) {
        Boolean result = false;
        //// TODO: 2016/11/4 为什么总是抛null pointer exception呢??!?!? 
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                DATELINE + " = ?",
                new String[]{DateLine.getTodayDatelineInSelectForm()},
                null, null, null);
        DailySentenceBean bean;
        if (cursor != null && cursor.moveToFirst()) {
            //如果就是今天的获取bean传入回调函数中
            bean = getDailySentenceBeanFromCursor(cursor);
            callBack.onDailySentenceUpdate(bean);
            db.close();
            cursor.close();
            result = true;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            //如果不是今天的,那么还是要获取一个最新的放入回调函数中
            //恕我愚钝,只能用一个蠢办法.....
            bean = getLatestDailySentenceFromLocal(db);
            callBack.onDailySentenceUpdate(bean);
            db.close();
        }
        return result;
    }

    private DailySentenceBean getLatestDailySentenceFromLocal(SQLiteDatabase db) {
        //妈的不会用sql语句真吃亏!,还要查询两次...浪费时间
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"max(" + SID + ")"},
                null, null, null, null, null);
        //如果cursor是空,数据库获取失败,则返回false
        if (!cursor.moveToFirst()) {
            return null;
        }
        //得到最后一次存入数据库的数据的id
        //这个sid一定要加上max和括号.....不然娶不到数据
        int tempSid = cursor.getInt(cursor.getColumnIndex("max(" + SID + ")"));
        cursor.close();
        //这次查询获取的才是真正的最新的本地数据
        cursor = db.query(
                TABLE_NAME,
                null,
                SID + " = ?", new String[]{String.valueOf(tempSid)},
                null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                DailySentenceBean bean = getDailySentenceBeanFromCursor(cursor);
                cursor.close();
                return bean;
            } else {
                cursor.close();
                return null;
            }
        } else {
            return null;
        }
    }

    private DailySentenceBean getDailySentenceBeanFromCursor(Cursor cursor) {
        DailySentenceBean bean = new DailySentenceBean();
        int sid = cursor.getInt(cursor.getColumnIndex(SID));
        String tts = cursor.getString(cursor.getColumnIndex(TTS));
        String content = cursor.getString(cursor.getColumnIndex(CONTENT));
        String note = cursor.getString(cursor.getColumnIndex(NOTE));
        String translation = cursor.getString(cursor.getColumnIndex(TRANSLATION));
        String picture = cursor.getString(cursor.getColumnIndex(PICTURE));
        String picture2 = cursor.getString(cursor.getColumnIndex(PICTURE2));
        String dateline = cursor.getString(cursor.getColumnIndex(DATELINE));
        String fenxiang_img = cursor.getString(cursor.getColumnIndex(FENXIANG_IMG));
        bean.setSid(sid);
        bean.setTts(tts);
        bean.setContent(content);
        bean.setNote(note);
        bean.setTranslation(translation);
        bean.setPicture(picture);
        bean.setPicture2(picture2);
        bean.setDateline(dateline);
        bean.setFenxiang_img(fenxiang_img);
        return bean;
    }

    public void getDailySentenceFromInternet(final OnDailySentenceUpdate callBack) {
        NetworkConnection
                .getInstance()
                .getDailySentenceBean(
                        DAILY_SENTENCE_API,
                        new NetworkConnection.OnDailySentenceBeanResponded() {
                            @Override
                            public void onDailySentenceBeanResponded(DailySentenceBean bean) {
                                callBack.onDailySentenceUpdate(bean);
                                //同时还要保存到本地数据库中
                                updateDailySentence(bean);
                            }
                        }
                );
    }

    public void updateDailySentence(DailySentenceBean bean) {
        if (bean == null) {
            return;
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int sid = bean.getSid();
        String tts = bean.getTts();
        String content = bean.getContent();
        String note = bean.getNote();
        String translation = bean.getTranslation();
        String picture = bean.getPicture();
        String picture2 = bean.getPicture2();
        String dateline = bean.getDateline();
        String fenxiang_img = bean.getFenxiang_img();
        ContentValues values = new ContentValues();
        values.put(SID, sid);
        values.put(TTS, tts);
        values.put(CONTENT, content);
        values.put(NOTE, note);
        values.put(TRANSLATION, translation);
        values.put(PICTURE, picture);
        values.put(PICTURE2, picture2);
        values.put(DATELINE, dateline);
        values.put(FENXIANG_IMG, fenxiang_img);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    /**
     * 其他类实现这个接口接收dailysentence
     */
    public interface OnDailySentenceUpdate {
        void onDailySentenceUpdate(DailySentenceBean bean);
    }


}
