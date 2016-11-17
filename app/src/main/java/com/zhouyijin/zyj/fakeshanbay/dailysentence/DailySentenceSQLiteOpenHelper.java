package com.zhouyijin.zyj.fakeshanbay.dailysentence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2016/10/24.
 *
 */
public class DailySentenceSQLiteOpenHelper extends SQLiteOpenHelper {

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

    public static final String CREATE_DAILY_SENTENCE_TABLE = "create table " + TABLE_NAME + "("
            + _ID + " integer primary key autoincrement" + ","
            + SID + " integer" + ","
            + TTS + " text" + ","
            + CONTENT + " text" + ","
            + NOTE + " text" + ","
            + TRANSLATION + " text" + ","
            + PICTURE + " text" + ","
            + PICTURE2 + " text" + ","
            + DATELINE + " text" + ","
            + FENXIANG_IMG + " text" + ")";


    public DailySentenceSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILY_SENTENCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (oldVersion) {
//            case 1:
//                db.execSQL(ADD_COLUMN_BIG_PIC_FILENAME);
//        }
    }
}
