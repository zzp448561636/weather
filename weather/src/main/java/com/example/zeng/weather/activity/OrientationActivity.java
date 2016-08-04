package com.example.zeng.weather.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zeng.weather.AppController;
import com.example.zeng.weather.R;
import com.example.zeng.weather.data.CityInfo;
import com.example.zeng.weather.data.Constant;
import com.example.zeng.weather.data.GridAdapter;
import com.example.zeng.weather.database.DBOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrientationActivity extends AppCompatActivity {
    private boolean isOrientation;
    private Button btnSearch;
    private CoordinatorLayout coordinatorLayout;
    private GridView hotCity,hotScene;
    private List<String> cityList,sceneList;
    private GridAdapter cityAdpter,sceneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        isOrientation = getIntent().getBooleanExtra("isOrientation",true);

        getDataFromFile();
        initView();

    }

    private void initView(){
        btnSearch = (Button)findViewById(R.id.btn_search);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.orientation_content);
        hotCity = (GridView)findViewById(R.id.hot_city_grid);
        hotScene = (GridView)findViewById(R.id.hot_scene_grid);
        cityAdpter = new GridAdapter(OrientationActivity.this,cityList);
        sceneAdapter = new GridAdapter(OrientationActivity.this,sceneList);

        hotCity.setAdapter(cityAdpter);
        hotScene.setAdapter(sceneAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showSnackBarMessage("搜索功能待完善");
            }
        });

        hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addNewCity("city",i);
            }
        });

        hotScene.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addNewCity("scene",i);
            }
        });
    }

    public void showSnackBarMessage(String msg){
        Snackbar snackbar = Snackbar.make(coordinatorLayout,msg,Snackbar.LENGTH_LONG);
        setSnackbarMessageTextColor(snackbar, Color.WHITE);
        snackbar.show();
    }

    public void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    /**
     * 热门城市，人们景点的数据基本不会变化，所以考虑写在文件中，从文件中读取，如果有更新更新文件即可
     * 文件大小一般只有几KB
     * 且可以展示一下文件读取方面的基本用法
     */
    public void getDataFromFile(){
        File file = new File(getApplicationContext().getFilesDir(), Constant.HOT_CITY_SCENE_FILE_NAME);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            cityList = Arrays.asList(reader.readLine().split("&"));
            sceneList = Arrays.asList(reader.readLine().split("&"));
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewCity(String listName,int i){
        DBOperation db  = new DBOperation(OrientationActivity.this);
        int order = AppController.getInstance().getLatestOrder() + 1;
        String name = null;
        CityInfo cityInfo = null;
        switch (listName){
            case "city":
                name = cityList.get(i);
                cityInfo = db.insertCitySelected(name,order);
                break;
            case "scene":
                name = sceneList.get(i);
                cityInfo = db.insertCitySelected(name,order);
                break;
        }
        if (cityInfo == null){
            showSnackBarMessage("请重试");
        }
        else{
            AppController.getInstance().appendCityInfoList(cityInfo);
            AppController.getInstance().setCityListChanged(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO:判断是否立即进行定位
    }
}
