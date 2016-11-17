package com.zhouyijin.zyj.fakeshanbay.TodayTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.BaseSQLiteOpenHelper;

/**
 * Created by zhouyijin on 2016/10/24.
 * 这个表是保存今天的任务单词的,而且是只保存今天的任务,日期一改就直接换内容了!
 */

public class TodayTaskSQLiteOpenHelper extends BaseSQLiteOpenHelper {

    public static final String TABLE_NAME = "today_task";
    public static final String WORD_COLUMN = "word";
    public static final String COMPLETED_COLUMN = "completed";
    public static final String DATE_LINE = "date";

    public static final String CREATE_TODAY_TASK_TABLE = "create table " + TABLE_NAME + "("
            + "_id" + " integer primary key autoincrement" + ","
            + WORD_COLUMN + " text" + " unique" + ","
            + COMPLETED_COLUMN + " integer,"
            + DATE_LINE + " text"
            + ")";


//    public static final String CREATE_TODAY_TASK_TABLE = "create table today_task(_id integer primary key autoincrement,word text unique,completed integer,date text)";

    public TodayTaskSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODAY_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
