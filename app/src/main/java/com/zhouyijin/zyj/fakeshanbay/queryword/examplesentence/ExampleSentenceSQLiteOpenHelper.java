package com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.BaseSQLiteOpenHelper;

/**
 * Created by zhouyijin on 2016/11/9.
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



-----------------------------------------------------------------------------------------------------------
系统例句的示例

{
    "msg": "SUCCESS",
    "status_code": 0,
    "data": [
        {
            "username": "Shanbay",
            "likes": 0,
            "last": null,
            "userid": 1,
            "mid": null,
            "audio_name": null,
            "annotation": "He sent her a <vocab>single<\/vocab> red rose.",
            "audio_addresses": {},
            "version": 0,
            "unlikes": 0,
            "user": {
                "username": "Shanbay",
                "nickname": "Shanbay",
                "id": 1,
                "avatar": "https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/"
            },
            "word": "single",
            "translation": "他送给她一支红玫瑰。",
            "nickname": "Shanbay",
            "id": 3835459,
            "first": null
        },
        {
            "username": "Shanbay",
            "likes": 0,
            "last": null,
            "userid": 1,
            "mid": null,
            "audio_name": null,
            "annotation": "a <vocab>single<\/vocab>-sex school (= for boys only or for girls only)",
            "audio_addresses": {},
            "version": 0,
            "unlikes": 0,
            "user": {
                "username": "Shanbay",
                "nickname": "Shanbay",
                "id": 1,
                "avatar": "https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/"
            },
            "word": "single",
            "translation": "男子（或女子）学校",
            "nickname": "Shanbay",
            "id": 3835462,
            "first": null
        }
    ]
}
    返回字段说明:

    id           int           例句的id
    user         object        用户的json对象
    likes        int           喜欢该例句的用户数量
    unlikes      int           不喜欢该例句的用户数量
    translation  int           例句的翻译
    annotation   string        例句原文，vocab括起来的内容是匹配的单词，用来高亮
 */

public class ExampleSentenceSQLiteOpenHelper extends BaseSQLiteOpenHelper {

    public static final String TABLE_NAME = "example_sentence_system";

    public static final String ID = "id";   //例句的id,这个不是主键  值是int 唯一非空
    public static final String WORD = "word"; //单词
    public static final String ANNOTATION = "annotation";   //例句原文
    public static final String TRANSLATION = "translation"; //例句的中文翻译
    public static final String USER_NAME = "username";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            "_id integer primary key autoincrement," +
            ID + " integer not null unique," +
            WORD + " text not null," +
            ANNOTATION + " text," +
            TRANSLATION + " text," +
            USER_NAME + " text" + ")";


    public ExampleSentenceSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
