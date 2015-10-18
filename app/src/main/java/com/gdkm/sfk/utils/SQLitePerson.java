package com.gdkm.sfk.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/24.
 */
public class SQLitePerson extends SQLiteOpenHelper {
    public SQLitePerson(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE person (personId Integer PRIMARY KEY AUTOINCREMENT,ctelnum VARCHAR(20),cemail VARCHAR(20),openId VARCHAR(100),nickname varchar(20),password VARCHAR(50),age INT,sex VARCHAR(20),province VARCHAR(20),city VARCHAR(20),figureurl_2 VARCHAR(200))";
                db.execSQL(sql);
        System.out.println("-----sqlOnCreate----");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
