package com.example.zeng.weather;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.zeng.weather.data.CityInfo;
import com.example.zeng.weather.database.DBOperation;
import com.example.zeng.weather.util.LruBitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeng on 2016/7/24.
 */
public class AppController extends Application{
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private List<CityInfo> cityInfoList;
    private boolean isCityListChanged;
    private int curCityIndex;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        isCityListChanged = false;
        curCityIndex = 0;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public List<CityInfo> getCityInfoList(){
        if(cityInfoList != null) return cityInfoList;
        DBOperation dbOperation = new DBOperation(AppController.this);
        cityInfoList = dbOperation.queryCitySelected();
        dbOperation.closeRes();
        dbOperation = null;
        return cityInfoList;
    }

    /**
     *
     * @param name
     * @return -1失败，1成功
     */
    public int addCityInfoList(String name){
        DBOperation db  = new DBOperation(AppController.this);
        int order = getLatestOrder() + 1;
        CityInfo cf = null;
        cf = db.insertCitySelected(name,order);
        if (cf == null){
            db.closeRes();
            db = null;
            return -1;
        }
        else{
            if (cityInfoList == null) cityInfoList = new ArrayList<>();
            cityInfoList.add(cf);
            isCityListChanged = true;
            curCityIndex = order - 1;
            db.closeRes();
            db = null;
            return 1;
        }
    }

    public List<String> getCityNameList(){
        List<String> list = null;
        DBOperation dbOperation = new DBOperation(AppController.this);
        list = dbOperation.queryCitySelectedName();
        dbOperation.closeRes();
        dbOperation = null;
        return list;
    }

    public boolean isCityListChanged() {
        return isCityListChanged;
    }

    public void setCityListChanged(boolean cityListChanged) {
        isCityListChanged = cityListChanged;
    }

    public int getCurCityIndex() {
        return curCityIndex;
    }

    public void setCurCityIndex(int curCityIndex) {
        this.curCityIndex = curCityIndex;
    }

    /**
     * 返回当前cityInfoList中order的最大值
     * 因为每次删除移动都会修改order，
     * 所以数据库中cityorder是连续不重复的，从1到size
     * @return
     */
    public int getLatestOrder(){
        return cityInfoList.size();
    }

    public List<CityInfo> delete(int position){
        DBOperation db  = new DBOperation(AppController.this);
        db.delete(cityInfoList.get(position));
        cityInfoList.clear();
        cityInfoList.addAll(db.queryCitySelected());
        db.closeRes();
        db = null;
        return  cityInfoList;
    }

    public List<CityInfo> swap(int from,int to){
        CityInfo cf = cityInfoList.get(from);
        CityInfo ct = cityInfoList.get(to);
        DBOperation db  = new DBOperation(AppController.this);
        db.swap(cf,ct);
        cityInfoList.clear();
        cityInfoList.addAll(db.queryCitySelected());
        db.closeRes();
        db=null;
        return cityInfoList;
    }
}
