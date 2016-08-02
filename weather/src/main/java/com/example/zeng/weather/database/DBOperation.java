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
        List<CityInfo> cityList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TBL_CITY_SELECTED,
                new String[]{Constant.TBL_CITY_SELECTED_ID,Constant.TBL_CITY_SELECTED_NAME,Constant.TBL_CITY_SELECTED_ORDER},
                null,null,null,null,Constant.TBL_CITY_SELECTED_ORDER + " desc");
        while (cursor.moveToNext()){
            if(cityList == null)cityList = new ArrayList<CityInfo>();
            CityInfo cityInfo = new CityInfo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
            cityList.add(cityInfo);
        }
        db.close();
        return cityList;
    }

}
