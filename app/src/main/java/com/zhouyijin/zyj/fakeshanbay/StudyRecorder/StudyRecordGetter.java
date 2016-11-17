package com.zhouyijin.zyj.fakeshanbay.StudyRecorder;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhouyijin.zyj.fakeshanbay.MyApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouyijin on 2016/10/24.
 * 这个类主要用来操作数据库
 */

public class StudyRecordGetter {
    public static final String TABLE_NAME = "study_recorder";
    public static final String WORD_COLUMN = "word";
    public static final String COMPLETED_COLUMN = "completed";
    public static final int STUDIED = 1;
    public static final int UNSTUDIED = 0;

    /**
     * 单例模式,防止重复操作数据库
     *
     * @return
     */
    public static StudyRecordGetter getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static StudyRecordGetter instance = new StudyRecordGetter();
    }

    public static final String DATABASE_NAME = "StudyRecorder.db";
    public static final int VERSION = 1;

    private StudyRecordGetter() {
        sqliteOpenHelper =
                new StudyRecorderSQLiteOpenHelper(
                        MyApplication.getContext(),
                        DATABASE_NAME, null, VERSION);
    }

    StudyRecorderSQLiteOpenHelper sqliteOpenHelper;

    public void insertWord(String word, int isCompletedStudy) {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD_COLUMN, word);
        values.put(COMPLETED_COLUMN, isCompletedStudy);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqliteOpenHelper.close();
    }

    public void insertWord(String word) {
        insertWord(word, UNSTUDIED);
    }


    public void insertWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = 0;
        db.beginTransaction();
        for (String word : words) {
            values.put(WORD_COLUMN, word);
            values.put(COMPLETED_COLUMN, UNSTUDIED);
            ++i;
            Log.d("", "中文好识别!!!" + i);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            values.clear();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        sqliteOpenHelper.close();
    }


    public boolean isWordStudied(String word) {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                WORD_COLUMN + " = ?",
                new String[]{word}, null, null, null);
        sqliteOpenHelper.close();
        if (cursor.moveToFirst()) {
            int isWordStudied = cursor.getInt(cursor.getColumnIndex(COMPLETED_COLUMN));
            if (isWordStudied == 0) {
                cursor.close();
                return false;
            } else {
                cursor.close();
                return true;
            }
        } else {
            cursor.close();
            return false;
        }
    }


    public void recordWord(String word) {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        //先删除再插入,这样如果表中没有的单词也能记录下来,相当于update了       //这个方法不好.....
//        db.delete(TABLE_NAME, WORD_COLUMN + " = ?", new String[]{word});

        //有其他效率更高的办法
        ContentValues values = new ContentValues();
        values.put(WORD_COLUMN, word);
        values.put(COMPLETED_COLUMN, STUDIED);
        int row = db.updateWithOnConflict(TABLE_NAME, values, WORD_COLUMN + " = ?", new String[]{word}, SQLiteDatabase.CONFLICT_IGNORE);
        if (-1 == row) {
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        sqliteOpenHelper.close();
    }

    public void recordWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (String word : words) {
            values.put(WORD_COLUMN, word);
            values.put(COMPLETED_COLUMN, STUDIED);
            int row = db.updateWithOnConflict(TABLE_NAME, values, WORD_COLUMN + " = ?", new String[]{word}, SQLiteDatabase.CONFLICT_IGNORE);
            if (row == -1) {
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            values.clear();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        sqliteOpenHelper.close();
    }

    public List<String> getUnstudiedWords(int number) {
        if (number < 1 || number > 500) {
            return null;
        }
        List<String> words = new ArrayList<>(number + 5);
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COMPLETED_COLUMN + " = ?",
                new String[]{String.valueOf(UNSTUDIED)}, null, null, null);
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                if (i >= number) break;
                String word = cursor.getString(cursor.getColumnIndex(WORD_COLUMN));
                words.add(word);
                ++i;
            } while (cursor.moveToNext());
        }
        sqliteOpenHelper.close();
        cursor.close();
        return words;
    }

    public int getMasteredWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COMPLETED_COLUMN + " = ?",
                new String[]{String.valueOf(STUDIED)}, null, null, null);
        int result = cursor.getCount();
        sqliteOpenHelper.close();
        cursor.close();
        return result;
    }

    public int getScheduleWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                null,
                null, null, null, null);
        int result = cursor.getCount();
        cursor.close();
        sqliteOpenHelper.close();
        return result;
    }

    public void unRecordWord(String word) {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        //先
        ContentValues values = new ContentValues();
        values.put(WORD_COLUMN, word);
        values.put(COMPLETED_COLUMN, UNSTUDIED);
        int row = db.updateWithOnConflict(TABLE_NAME, values, WORD_COLUMN + " = ?", new String[]{word}, SQLiteDatabase.CONFLICT_IGNORE);

        if (-1 == row) {
            db.insert(TABLE_NAME, null, values);
        }

        sqliteOpenHelper.close();
    }

}
