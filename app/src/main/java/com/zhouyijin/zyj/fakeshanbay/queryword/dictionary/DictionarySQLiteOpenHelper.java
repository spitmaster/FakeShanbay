package com.zhouyijin.zyj.fakeshanbay.queryword.dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.BaseSQLiteOpenHelper;

/**
 * Created by zhouyijin on 2016/11/7.
 * <p>
 * 这个数据库用来保存一个单词的内容信息,与wordBean相对应
 * 保存的内容有:  1.单词
 * 2.音标
 * 3.单词的类型(短语还是单词)
 * 4.单词的编号(扇贝网标记的编号)
 * 5.单词的名词性英文解释
 * 6.单词的形容词性英文解释
 * 7.单词的动词的英文解释
 * 8.单词的副词的英文解释
 * 9.单词的连词的英文解释
 * 10.单词的介词的英文解释
 * 11.音频的url地址
 * 12.单词的中文释义
 */

public class DictionarySQLiteOpenHelper extends BaseSQLiteOpenHelper {

    public static final String TABLE_NAME = "local_dictionary";
    public static final String WORD = "word";   //这个应该是唯一的 且非空
    public static final String PRONUNCIATION = "pronunciation";
    public static final String CONTENT_TYPE = "content_type";
    public static final String ID = "ID";       //这个应该是唯一的
    public static final String N_EN = "noun_en";
    public static final String ADJ_EN = "adjective_en";
    public static final String V_EN = "verb_en";
    public static final String ADV_EN = "adverb_en";
    public static final String CONJ_EN = "conjunction_en";
    public static final String PREP_EN = "preposition_en";
    public static final String AUDIO_URL = "audio_url";
    public static final String DEFINITION_CN = "definition_cn";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            "_id" + " integer primary key autoincrement," +
            WORD + " text unique not null," +
            PRONUNCIATION + " text," +
            CONTENT_TYPE + " text," +
            ID + " integer," +
            N_EN + " text," +
            ADJ_EN + " text," +
            V_EN + " text," +
            ADV_EN + " text," +
            CONJ_EN + " text," +
            PREP_EN + " text," +
            AUDIO_URL + " text," +
            DEFINITION_CN + " text" + ")";

    public DictionarySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
