package com.example.zeng.weather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.example.zeng.weather.data.Constant;


/**
 * Created by Zeng on 2016/7/31.
 */
public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context, String name, CursorFactory factory,int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + Constant.TBL_CITY_SELECTED
                +"("
                + Constant.COL_CITY_SELECTED_ID + " integer primary key autoincrement, "
                + Constant.COL_CITY_SELECTED_NAME + " varchar(20), "
                + Constant.COL_CITY_SELECTED_ORDER + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
