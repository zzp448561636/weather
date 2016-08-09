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
        curCityIndex = 1;
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

    public void appendCityInfoList(CityInfo cityInfo){
        if (cityInfoList == null) cityInfoList = new ArrayList<>();
        cityInfoList.add(cityInfo);
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
}
