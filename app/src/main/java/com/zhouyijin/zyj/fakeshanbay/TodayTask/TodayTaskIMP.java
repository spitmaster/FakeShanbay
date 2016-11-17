package com.zhouyijin.zyj.fakeshanbay.TodayTask;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UFormat;
import android.util.Log;

import com.zhouyijin.zyj.fakeshanbay.Beans.ExampleSentenceBean;
import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.StudyRecorder.StudyRecorder;
import com.zhouyijin.zyj.fakeshanbay.StudyRecorder.StudyRecorderIMP;
import com.zhouyijin.zyj.fakeshanbay.queryword.examplesentence.SentenceManager;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;
import com.zhouyijin.zyj.fakeshanbay.sharedpreferencesmanager.SharedPreferencesManager;
import com.zhouyijin.zyj.fakeshanbay.tools.DateLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyijin on 2016/10/24.
 * <p>
 * 这个类专门用来存储今日任务!
 * 设置成单例模式因为今日只有一天
 * <p>
 * 这个类所有的功能:
 * 1.获取/设置今天的任务量(要背的单词个数)
 * 2.根据任务量获取今天的任务(所有的单词)(通过StudyRecorder获取今天的单词)
 * 3.获取今天已经完成的任务(已经背过的单词)
 * 4.获取今天未完成的任务(未背过的单词)
 * 5.获取今天已经完成和未完成的单词的个数
 * 6.设置某个单词已经完成,或者未完成
 */

public class TodayTaskIMP implements TodayTask {

    public static final String TABLE_NAME = "today_task";
    public static final String WORD_COLUMN = "word";
    public static final String COMPLETED_COLUMN = "completed";
    public static final String DATE_LINE = "date";
    public static final int FINISHED = 1;
    public static final int UNFINISHED = 0;
    public static final int FAILED = -1;
    public static final String DB_NAME = "today_task.db";


    private StudyRecorder studyRecorder;
    private TodayTaskSQLiteOpenHelper sqliteOpenHelper;

    //准备任务的时候需要用的变量
    private int progress;
    private int maxProgress;

    private TodayTaskIMP() {
        studyRecorder = StudyRecorderIMP.getStudyRecorder();
        sqliteOpenHelper =
                new TodayTaskSQLiteOpenHelper(
                        MyApplication.getContext(),
                        DB_NAME, null, 1);
    }


    public static TodayTaskIMP getInstance() {
        return InstanceHolder.instance;
    }


    private static class InstanceHolder {
        private static final TodayTaskIMP instance = new TodayTaskIMP();
    }

    //设置完今天的单词数后直接刷新今天的单词,如果设置了一个数比今天已经背过的单词还要少,那么就不生效
    @Override
    public void setTaskWordsNumber(int number) {
        if (number < 1 || number > 500) {
            return;
        }
        SharedPreferencesManager.getInstance().setWordsScheduleNumber(number);
        refreshWords();
    }

    //只会刷新今天没背的单词
    @Override
    public void refreshWords() {
        //如果今天的计划没有问题就不刷新了
        if (getTodayUnfinishedWordsNumber() + getTodayFinishedWordsNumber() == SharedPreferencesManager.getInstance().getWordsScheduleNumber()) {
            return;
        }
        //获取还有多少词没有背
        int wordNumber = SharedPreferencesManager.getInstance().getWordsScheduleNumber()
                - getTodayFinishedWordsNumber();
        if (wordNumber < 1 || wordNumber > 500) {
            return;
        }
        maxProgress = wordNumber;
        List<String> words = studyRecorder.getTaskWords(wordNumber);
        setTodayTaskWords(words);
    }

    private void setTodayTaskWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        //先把今天没背的单词全删了
        String dateLine = DateLine.getTodayDateLine();
        db.delete(TABLE_NAME, COMPLETED_COLUMN + " <= ? and " + DATE_LINE + " = ?", new String[]{String.valueOf(UNFINISHED), dateLine});
        //再把新分配过来的单词加入数据库
        db.beginTransaction();
        for (String word : words) {
            ContentValues values = new ContentValues();
            values.put(WORD_COLUMN, word);
            values.put(COMPLETED_COLUMN, UNFINISHED);
            values.put(DATE_LINE, dateLine);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        sqliteOpenHelper.close();
    }

    /**
     * @return 获取今天任务中Failed的单词
     */
    public List<String> getTodayFailedWords() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dataLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{WORD_COLUMN},
                COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?",
                new String[]{String.valueOf(FAILED), dataLine},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        List<String> words = new ArrayList<>(cursor.getCount() + 5);
        do {
            String word = cursor.getString(cursor.getColumnIndex(WORD_COLUMN));
            words.add(word);
        } while (cursor.moveToNext());
        sqliteOpenHelper.close();
        cursor.close();
        return words;
    }

    /**
     * @return 返回一个list 里面放的是今天没有完成的单词
     */
    @Override
    public List<String> getTodayUnfinishedWords() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dataLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{WORD_COLUMN},
                COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?",
                new String[]{String.valueOf(UNFINISHED), dataLine},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        List<String> words = new ArrayList<>(cursor.getCount() + 5);
        do {
            String word = cursor.getString(cursor.getColumnIndex(WORD_COLUMN));
            words.add(word);
        } while (cursor.moveToNext());
        sqliteOpenHelper.close();
        cursor.close();
        return words;
    }

    /**
     * @return 返回一个list 里面放的是今天已完成的单词
     */
    @Override
    public List<String> getTodayFinishedWords() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{TABLE_NAME + "." + WORD_COLUMN},
                COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?",
                new String[]{String.valueOf(FINISHED), dateLine},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        List<String> words = new ArrayList<>(cursor.getCount() + 5);
        do {
            String word = cursor.getString(cursor.getColumnIndex(WORD_COLUMN));
            words.add(word);
        } while (cursor.moveToNext());
        sqliteOpenHelper.close();
        cursor.close();
        return words;
    }

    //设置指定单词为学习过的,如果状态是failed则提升到unfinished,如果是unfinished则提升到finished
    @Override
    public void setFinishedWord(String word) {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, WORD_COLUMN + " = ?", new String[]{word}, null, null, null);
        int completed;
        if (cursor.moveToFirst()) {
            completed = cursor.getInt(cursor.getColumnIndex(COMPLETED_COLUMN));
            cursor.close();
        } else {
            cursor.close();
            return;
        }
        if (completed < FINISHED) {
            completed++;
        }
        ContentValues values = new ContentValues();
        values.put(COMPLETED_COLUMN, completed);
        if (completed == UNFINISHED) {  //如果是failed变过来的,需要删掉在插入,使它的排序在最后面
            db.delete(TABLE_NAME, WORD_COLUMN + " = ?", new String[]{word});
            db.insert(TABLE_NAME, null, values);
        } else {
            db.update(TABLE_NAME, values, WORD_COLUMN + " = ?", new String[]{word});
        }
        sqliteOpenHelper.close();
        if (completed == FINISHED) {
            MyApplication.getStudyRecorder().recordWord(word);
        }
    }

    //设置指定单词为未学习的,这个单词必须是今天的任务中的
    @Override
    public void setFailedWord(String word) {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String dateline = DateLine.getTodayDateLine();
        values.put(COMPLETED_COLUMN, FAILED);
        values.put(WORD_COLUMN, word);
        values.put(DATE_LINE, dateline);
        //使用删除再插入的方法,可以把这个单词放入表的最后一行,这样getFirstUnfinishedWord()时就不会重复了
        db.delete(TABLE_NAME, WORD_COLUMN + " = ?", new String[]{word});
        db.insert(TABLE_NAME, null, values);
        sqliteOpenHelper.close();
        MyApplication.getStudyRecorder().unRecordWord(word);
    }


    /**
     * @return 获取一个未完成的单词
     */
    @Override
    public String getFirstUnfinishedOrFailedWord() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dataLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{WORD_COLUMN},
                COMPLETED_COLUMN + " <= ? and " + DATE_LINE + " = ?",
                new String[]{String.valueOf(UNFINISHED), dataLine},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        String word = cursor.getString(cursor.getColumnIndex(WORD_COLUMN));
        sqliteOpenHelper.close();
        cursor.close();
        return word;
    }

    /**
     * 这个方法用来准备今天的任务
     *
     * @param listener 此listener会在下载任务有更新的情况下被回调,刷新进度
     */
    @Override
    public void prepareTodayTask(final OnPrepareProgress listener) {
        progress = 0;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<String> words = new ArrayList<>();
                List<String> list1 = getTodayUnfinishedWords();
                List<String> list2 = getTodayFailedWords();
                if (list1 != null) words.addAll(list1);
                if (list2 != null) words.addAll(list2);
                if (words == null) {
                    return;
                }
                for (final String word : words) {
                    MyApplication.getQueryWord().queryWord(word, new IQueryWord.OnQuerySucceedListener() {
                        @Override
                        public void onQuerySucceed(WordBean wordBean) {
                            if (wordBean == null) {
                                return;
                            }
                            progress++;
                            if (listener != null) {
                                listener.onPrepareProgress(progress, maxProgress);
                            }
                        }
                    });
                    MyApplication.getSentenceManager().getExampleSentence(word, new SentenceManager.OnGetResult() {
                        @Override
                        public void OnGetResult(ExampleSentenceBean bean) {

                        }
                    }, SentenceManager.SYSTEM_SENTENCE);
                }
            }
        };
        MyApplication.getExecutor().execute(r);
    }

    /**
     * 判断这个计划是不是新的一天了,数据库里的计划是不是今天的
     *
     * @return 如果就是今天的计划返回false, 如果今天是新的一天则返回true
     */
    @Override
    public boolean checkIsNewDay() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME, null, DATE_LINE + " = ?", new String[]{dateLine}, null, null, null);
        boolean result = !cursor.moveToFirst();
        cursor.close();
        sqliteOpenHelper.close();
        return result;
    }


    /**
     * @return 获取数据库中今天没有完成的单词数
     */
    @Override
    public int getTodayUnfinishedWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME, null, COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?", new String[]{String.valueOf(UNFINISHED), dateLine}, null, null, null);
        int result = cursor.getCount();
        sqliteOpenHelper.close();
        cursor.close();
        return result;
    }


    /**
     * @return 获取数据库中今天背失败的单词数
     */
    public int getTodayFailedWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME, null, COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?", new String[]{String.valueOf(FAILED), dateLine}, null, null, null);
        int result = cursor.getCount();
        sqliteOpenHelper.close();
        cursor.close();
        return result;
    }

    /**
     * @return 获取数据库中今天完成的单词数
     */
    @Override
    public int getTodayFinishedWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME, null, COMPLETED_COLUMN + " = ? and " + DATE_LINE + " = ?", new String[]{String.valueOf(FINISHED), dateLine}, null, null, null);
        int result = cursor.getCount();
        sqliteOpenHelper.close();
        cursor.close();
        return result;
    }

    /**
     * @return 获取数据库中今天任务的所有单词数
     */
    @Override
    public int getTodayTaskWordsNumber() {
        SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
        String dateLine = DateLine.getTodayDateLine();
        Cursor cursor = db.query(TABLE_NAME, null, DATE_LINE + " = ?", new String[]{dateLine}, null, null, null);
        int result = cursor.getCount();
        sqliteOpenHelper.close();
        cursor.close();
        return result;
    }

}
