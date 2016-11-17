package com.zhouyijin.zyj.fakeshanbay.StudyRecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.BaseSQLiteOpenHelper;


/**
 * Created by zhouyijin on 2016/10/24.
 */

public class StudyRecorderSQLiteOpenHelper extends BaseSQLiteOpenHelper {

    /**
     * 这个数据空用来存储学习进度的,word表示所有要学习的单词,completed表示这个单词有没有完成学习(0表示未学,1表示已学)
     * 等于这个表只用来记录单词有没有完成学习,word这个是有唯一约束的不能随便插
     */
    public static final String TABLE_NAME = "study_recorder";

    public static final String CREATE_STUDY_RECORDER_TABLE = "create table " + TABLE_NAME + "("
            + "_id" + " integer primary key autoincrement" + ","
            + "word" + " text" + " unique" + ","
            + "completed" + " integer" + ")";

    public StudyRecorderSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDY_RECORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
