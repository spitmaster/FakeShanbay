package com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;
import com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyijin on 2016/11/9.
 * ExampleSentence接口的实现类,
 * 暂时只考虑获取系统例句
 * ---------------------------------------
 * 实现了从本地数据库获取,如果获取失败则从网络获取
 * 实现了把数据保存到本地数据库
 */

/*
        API：获取单词的例句
URL： https://api.shanbay.com/bdc/example/?vocabulary_id={id}&type={type}

请求方式: GET

参数： {id} ，必须，单词id ; {type} 可选， 如果指定type＝sys，则返回系统例句

返回示例：
{
    "msg": "SUCCESS",
    "status_code": 0,
    "data": [
        {
            id: 443808,
            "user": {
                "username": "username",
                "nickname": "nickanme",
                "id": 72196,
                "avatar": "http://qstatic.shanbay.com/team/media_store/d99aa28621d300c251c67d4c2ede08e8_1.jpg"
            },
            "unlikes": 0,
            "likes": 3,
            "translation": "跟大家打个招呼",
            "annotation": "say <vocab>hello</vocab> to everybody",
            "version": 0,
        },
         {
            id: 443808,
            "user": {
                "username": "username",
                "nickname": "nickanme",
                "id": 72198,
                "avatar": "http://qstatic.shanbay.com/team/media_store/d99aa28621d300c251c67d4c2ede08e8_1.jpg"
            },
            "unlikes": 0,
            "likes": 3,
            "translation": "跟大家打个招呼",
            "annotation": "say <vocab>hello</vocab> to everybody",
            "version": 1
        }
    }
}
    返回字段说明:

    id           int           例句的id
    user         object        用户的json对象
    likes        int           喜欢该例句的用户数量
    unlikes      int           不喜欢该例句的用户数量
    translation  int           例句的翻译
    annotation   string        例句原文，vocab括起来的内容是匹配的单词，用来高亮
 */


public class SentenceManagerImp implements SentenceManager {

    public static final String DB_NAME = "example_sentence.db";
    public static final String TABLE_NAME = "example_sentence_system";

    public static final String ID = "id";   //例句的id,这个不是主键,这个id由扇贝网提供 值是int 唯一非空
    public static final String WORD = "word"; //单词
    public static final String ANNOTATION = "annotation";   //例句原文
    public static final String TRANSLATION = "translation"; //例句的中文翻译
    public static final String USER_NAME = "username";  //标记例句的贡献用户,如果是系统例句则此列应为"Shanbay"

    private String token;
    private ExampleSentenceSQLiteOpenHelper sqLiteOpenHelper;

    private SentenceManagerImp() {
        sqLiteOpenHelper =
                new ExampleSentenceSQLiteOpenHelper(
                        MyApplication.getContext(),
                        DB_NAME, null, 1);
        token = SharedPreferencesManager.getInstance().getToken();
    }

    private static class InstanceHolder {
        private static SentenceManagerImp instance = new SentenceManagerImp();
    }

    public static SentenceManagerImp getInstance() {
        return InstanceHolder.instance;
    }

    //查询例句信息,获取的数据以bean的形式传入回调函数中
    //先从数据库获取,如果获取不到则从网络获取
    @Override
    public void getExampleSentence(String word, final OnGetResult callback, final int sentenceType) {
        IQueryWord queryWord = MyApplication.getQueryWord();
        queryWord.queryWord(word, new IQueryWord.OnQuerySucceedListener() {
            @Override
            public void onQuerySucceed(WordBean wordBean) {

                if (wordBean == null) {
                    callback.OnGetResult(null);
                    return;
                }
                String word = wordBean.getContent();
                ExampleSentenceBean bean;
                if (sentenceType == SentenceManager.SYSTEM_SENTENCE) {
                    bean = getSystemBeanFromDatabase(word);
                } else {
                    bean = getUserBeanFromDatabase(word);
                }
                if (bean != null) {
                    callback.OnGetResult(bean);
                    return;
                }
                //如果本地查询不到,则从网络查询......这嵌套的内部匿名类太多了...有空看能不能减少些嵌套
                int wordID = wordBean.getId();
                String url;
                if (sentenceType == SentenceManager.SYSTEM_SENTENCE) {
                    url = getSysSentenceUrl(wordID);
                } else {
                    url = getUserSentenceUrl(wordID);
                }
                NetworkConnection.getInstance().
                        getExampleSentenceBean(url, new NetworkConnection.OnExampleSentenceBeanResponded() {
                            @Override
                            public void onExampleSentenceBeanResponded(ExampleSentenceBean bean) {
                                callback.OnGetResult(bean);
                                //查询到的同时,保存到数据库中
                                putExampleSentence(bean);
                            }
                        });
            }
        });
    }


    //将bean中的例句信息遍历到数据库中,一个bean中可能含有多个例句
    @Override
    public void putExampleSentence(ExampleSentenceBean bean) {
        if (bean == null) {
            return;
        }
        List<ExampleSentenceBean.DataBean> dataBeans = bean.getData();
        if (dataBeans == null) {
            return;
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        for (ExampleSentenceBean.DataBean dataBean : dataBeans) {
            int id = dataBean.getId();
            String word = dataBean.getWord();
            String annotation = dataBean.getAnnotation();
            String translation = dataBean.getTranslation();
            String username = dataBean.getUsername();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(WORD, word);
            contentValues.put(ANNOTATION, annotation);
            contentValues.put(TRANSLATION, translation);
            contentValues.put(USER_NAME, username);
            db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        sqLiteOpenHelper.close();
    }


    //从数据库获取bean,有可能获取多个例句
    private ExampleSentenceBean getUserBeanFromDatabase(String word) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor =
                db.query(
                        TABLE_NAME,
                        null,
                        WORD + " = ? and " + USER_NAME + " != ?",
                        new String[]{word + "",},
                        null, null, null);
        //如果cursor为空,或者cursor is empty,则返回null
        ExampleSentenceBean bean = packBeanFromCursor(cursor);
        sqLiteOpenHelper.close();
        return bean;
    }

    private ExampleSentenceBean getSystemBeanFromDatabase(String word) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor =
                db.query(
                        TABLE_NAME,
                        null,
                        WORD + " = ? and " + USER_NAME + " = ?",
                        new String[]{word + "", ExampleSentenceBean.SYSTEN_USER_NAME},
                        null, null, null);
        //如果cursor为空,或者cursor is empty,则返回null
        ExampleSentenceBean bean = packBeanFromCursor(cursor);
        sqLiteOpenHelper.close();
        return bean;
    }

    //将数据库中获取的内容cursor,打包成一个ExampleSentenceBean,可能含有多个例句
    private ExampleSentenceBean packBeanFromCursor(Cursor cursor) {
        if (cursor == null | !cursor.moveToFirst()) {
            return null;
        }
        ExampleSentenceBean bean = new ExampleSentenceBean();
        List<ExampleSentenceBean.DataBean> dataBeans = new ArrayList<>();
        bean.setData(dataBeans);
        do {
            ExampleSentenceBean.DataBean dataBean = new ExampleSentenceBean.DataBean();
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            dataBean.setId(id);
            String word = cursor.getString(cursor.getColumnIndex(WORD));
            dataBean.setWord(word);
            String annotation = cursor.getString(cursor.getColumnIndex(ANNOTATION));
            dataBean.setAnnotation(annotation);
            String translation = cursor.getString(cursor.getColumnIndex(TRANSLATION));
            dataBean.setTranslation(translation);
            dataBeans.add(dataBean);
        } while (cursor.moveToNext());
        cursor.close();
        return bean;
    }


    private String getSysSentenceUrl(int wordID) {
        String url = "https://api.shanbay.com/bdc/example/?access_token=" + token + "&vocabulary_id=" + wordID + "&type=sys";
        return url;
    }

    private String getUserSentenceUrl(int wordID) {
        String url = "https://api.shanbay.com/bdc/example/?access_token=" + token + "vocabulary_id=" + wordID;
        return url;
    }

}
