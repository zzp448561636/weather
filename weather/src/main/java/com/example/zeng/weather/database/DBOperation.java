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
        Cursor cursor = null;
        try{
            cursor= db.query(Constant.TBL_CITY_SELECTED,
                    new String[]{Constant.COL_CITY_SELECTED_ID,Constant.COL_CITY_SELECTED_NAME,Constant.COL_CITY_SELECTED_ORDER},
                    null,null,null,null,Constant.COL_CITY_SELECTED_ORDER + " asc");
            while (cursor.moveToNext()){
                CityInfo cityInfo = new CityInfo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
                cityList.add(cityInfo);
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null)cursor.close();
            db.close();
            return cityList;
        }
    }

    public List<String> queryCitySelectedName(){
        List<String> cityList = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try{
            String sql = "select " +  Constant.COL_CITY_SELECTED_NAME + " from " + Constant.TBL_CITY_SELECTED + " order by " + Constant.COL_CITY_SELECTED_ORDER + " asc";
            cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                cityList.add(cursor.getString(0));
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if(cursor !=null) cursor.close();
            db.close();
            return cityList;
        }
    }

    public CityInfo insertCitySelected(String name,int order){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            String sql = "insert into " + Constant.TBL_CITY_SELECTED + "("
                    + Constant.COL_CITY_SELECTED_NAME +","
                    + Constant.COL_CITY_SELECTED_ORDER + ") values(?,?)";
            db.execSQL(sql,new Object[]{name,order});
        }catch (Exception e){e.printStackTrace();}
        finally {
            db.close();
            return queryCitySelectedByOrder(order);
        }
    }

    public CityInfo queryCitySelectedByOrder(int order){
        CityInfo cityInfo = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try{
            String sql = "select * from " + Constant.TBL_CITY_SELECTED + " where " + Constant.COL_CITY_SELECTED_ORDER + " = ?";
            cursor = db.rawQuery(sql,new String[]{String.valueOf(order)});
            if (cursor.moveToNext()){
                cityInfo = new CityInfo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if(cursor !=null)cursor.close();
            db.close();
            return cityInfo;
        }
    }

    public void delete(CityInfo city){
        int order = city.getCityOrder();
        int id = city.getCityId();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql1 = "update " + Constant.TBL_CITY_SELECTED + " set "
                    + Constant.COL_CITY_SELECTED_ORDER + " = "
                    + Constant.COL_CITY_SELECTED_ORDER + " - 1 where "
                    + Constant.COL_CITY_SELECTED_ORDER + " > " + order;
            String sql2 = "delete from " + Constant.TBL_CITY_SELECTED +" where "+Constant.COL_CITY_SELECTED_ID +" = "+id;
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.setTransactionSuccessful();
        }catch (Exception e){e.printStackTrace();}
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void swap(CityInfo from,CityInfo to){
        int fromId = from.getCityId();
        int fromOrder = from.getCityOrder();
        int toId = to.getCityId();
        int toOrder = to.getCityOrder();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql1 = "update " + Constant.TBL_CITY_SELECTED + " set "
                    + Constant.COL_CITY_SELECTED_ORDER + " = "
                    + toOrder + " where " + Constant.COL_CITY_SELECTED_ID + " =  " + fromId;
            String sql2 = "update " + Constant.TBL_CITY_SELECTED + " set "
                    + Constant.COL_CITY_SELECTED_ORDER + " = "
                    + fromOrder + " where " + Constant.COL_CITY_SELECTED_ID + " =  " + toId;
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }
}
