package com.zhouyijin.zyj.fakeshanbay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouyijin on 2016/11/8.
 */

public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

    public BaseSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    protected int connectCount = 0;

    @Override
    public SQLiteDatabase getReadableDatabase() {
        connectCount += 1;
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        connectCount += 1;
        return super.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        connectCount -= 1;
        if (connectCount == 0) {
            super.close();
        }
    }


}
