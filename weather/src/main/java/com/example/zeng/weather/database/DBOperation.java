package com.example.zeng.weather.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zeng.weather.data.CityInfo;
import com.example.zeng.weather.data.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeng on 2016/7/31.
 * 对数据库的简易操作，就用原始的操作方式，其他还可以使用例如ORM框架等等
 * 数据库操作可以用execSQL()和rawQuery()，执行sql语句
 * 也可以用insert，update，select方法进行，operation中会使用两种方法演示
 */
public class DBOperation {
    private DBHelper dbHelper;
    private Context context;

    public DBOperation(Context context){
        this.context = context;
        this.dbHelper = new DBHelper(context,Constant.DATABASE_NAME,null,Constant.DATABASE_VERSION);
    }

    public void closeRes(){
        if (dbHelper !=null)dbHelper.close();
    }

    public List<CityInfo> queryCitySelected(){
        List<CityInfo> cityList = new ArrayList<CityInfo>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TBL_CITY_SELECTED,
                new String[]{Constant.COL_CITY_SELECTED_ID,Constant.COL_CITY_SELECTED_NAME,Constant.COL_CITY_SELECTED_ORDER},
                null,null,null,null,Constant.COL_CITY_SELECTED_ORDER + " asc");
        while (cursor.moveToNext()){
            CityInfo cityInfo = new CityInfo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
            cityList.add(cityInfo);
        }
        cursor.close();
        db.close();
        return cityList;
    }

    public List<String> queryCitySelectedName(){
        List<String> cityList = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select " +  Constant.COL_CITY_SELECTED_NAME + " from " + Constant.TBL_CITY_SELECTED + " order by " + Constant.COL_CITY_SELECTED_ORDER + " asc";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            cityList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return cityList;
    }

    public CityInfo insertCitySelected(String name,int order){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into " + Constant.TBL_CITY_SELECTED + "("
                + Constant.COL_CITY_SELECTED_NAME +","
        + Constant.COL_CITY_SELECTED_ORDER + ") values(?,?)";
        db.execSQL(sql,new Object[]{name,order});
        db.close();
        return queryCitySelectedByOrder(order);
    }

    public CityInfo queryCitySelectedByOrder(int order){
        CityInfo cityInfo = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + Constant.TBL_CITY_SELECTED + " where " + Constant.COL_CITY_SELECTED_ORDER + " = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(order)});
        if (cursor.moveToNext()){
            cityInfo = new CityInfo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
        }
        cursor.close();
        db.close();
        return cityInfo;
    }
}
