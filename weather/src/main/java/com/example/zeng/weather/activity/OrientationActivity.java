package com.example.zeng.weather.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.zeng.weather.AppController;
import com.example.zeng.weather.R;
import com.example.zeng.weather.data.Constant;
import com.example.zeng.weather.util.GridAdapter;
import com.example.zeng.weather.util.LocationService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class OrientationActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private boolean isOrientation;
    private Button btnSearch;
    private ImageButton btnBack;
    private CoordinatorLayout coordinatorLayout;
    private GridView hotCity,hotScene;
    private List<String> cityList,sceneList,citySelected;
    private GridAdapter cityAdpter,sceneAdapter;

    private AlertDialog dialog;

    private LocationService locationService;

    private String[] perms = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        isOrientation = getIntent().getBooleanExtra("isOrientation",true);

        getDataFromFile();
        initView();
        //初始化定位
        locationService = new LocationService(OrientationActivity.this);
        locationService.registerListener(mListener);
        //定位选项可以使用默认，也可以自定义，自定义在LocationService中
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    private void initView(){
        btnSearch = (Button)findViewById(R.id.btn_search);
        btnBack = (ImageButton)findViewById(R.id.ib_back);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.orientation_content);
        hotCity = (GridView)findViewById(R.id.hot_city_grid);
        hotScene = (GridView)findViewById(R.id.hot_scene_grid);
        cityAdpter = new GridAdapter(OrientationActivity.this,cityList,citySelected);
        sceneAdapter = new GridAdapter(OrientationActivity.this,sceneList,citySelected);

        hotCity.setAdapter(cityAdpter);
        hotScene.setAdapter(sceneAdapter);

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showSnackBarMessage("搜索功能待完善");
            }
        });

        hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i ==0){
                    startLocation();
                }
                else{
                    addNewCity(cityList.get(i));
                }
            }
        });

        hotScene.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addNewCity(sceneList.get(i));
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
        citySelected = AppController.getInstance().getCityNameList();
    }

    public void addNewCity(String name){
        if (citySelected.contains(name)){
            AppController.getInstance().setCurCityIndex(citySelected.indexOf(name));
            goToMainActivity();
        }
        else{
            int result = AppController.getInstance().addCityInfoList(name);
            if(result == -1){
                showSnackBarMessage("请重试");
            }
            else if(result == 1){
                goToMainActivity();
            }
        }
    }

    private void showLocationDialog(final String location){
        if(dialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            addNewCity(location);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            dialog = builder.create();
        }
        dialog.setMessage("添加定位："+location);
        dialog.show();
    }

    public void goToMainActivity(){
        Intent intent = new Intent(OrientationActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in_animation, R.anim.left_out_animation);
        OrientationActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOrientation && (dialog ==null || !dialog.isShowing())){
            startLocation();
        }
    }

    @Override
    protected void onPause() {
        locationService.stop();
        super.onPause();
    }

    /**
     * android6.0之后，权限需要用户同意，而不再是manifest中声明即可
     */
    private void startLocation(){
        if (EasyPermissions.hasPermissions(this, perms)) {
            locationService.start();
        } else {
            EasyPermissions.requestPermissions(this, "请求以下权限",
                    1, perms);
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                switch (location.getLocType()){
                    case BDLocation.TypeGpsLocation:
                    case BDLocation.TypeNetWorkLocation:
                    case BDLocation.TypeOffLineLocation:
                        sb.append(location.getCity());
                        sb.append(location.getDistrict());
                        showLocationDialog(sb.toString());
                        break;
                    case BDLocation.TypeServerError:
                    case BDLocation.TypeNetWorkException:
                    case BDLocation.TypeCriteriaException:
                        sb.append("请在权限管理中同意定位相关权限申请");
                        showSnackBarMessage(sb.toString());
                        break;
                }
                locationService.stop();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        locationService.start();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
    }
}
