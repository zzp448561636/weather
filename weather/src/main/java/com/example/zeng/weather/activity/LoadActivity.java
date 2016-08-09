package com.example.zeng.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.zeng.weather.AppController;
import com.example.zeng.weather.R;
import com.example.zeng.weather.data.CityInfo;
import com.example.zeng.weather.data.Constant;
import com.example.zeng.weather.data.OnAnimationListener;
import com.example.zeng.weather.widget.RoundProgressBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LoadActivity extends AppCompatActivity implements OnAnimationListener {
    private ImageView iv;
    private int animationTime = 3000;
    private RoundProgressBar rpb;
    private List<CityInfo> cityInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        iv = (ImageView) findViewById(R.id.iv);
        rpb= (RoundProgressBar)findViewById(R.id.rpb);
        rpb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rpb.stopAnimation();
            }
        });

        //先显示一下首界面，不然跳的太快了
//        此处其实也可以做一些版本对比，下载一些相关数据，例如writeToFile模拟的下载热门城市信息的文件
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //数据库中无存储的城市信息，则先进行定位
                if (AppController.getInstance().getCityInfoList().size() == 0){
                    writeToFile();
                    goToOrientationActivity();
                }
                else showAdv();
            }
        },1000);
    }

    /**
     * 设想中应该先get方法获取广告图片下载url，再进行下载
     */
    private void showAdv(){
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(Constant.ADV_IMAGE_URL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                iv.setImageBitmap(response.getBitmap());
                rpb.setVisibility(View.VISIBLE);
                rpb.startAnimation(animationTime,LoadActivity.this);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                goToMainActivity();
            }
        });
    }

    public void OnAnimationFinished(){
        goToMainActivity();
    }

    public void goToMainActivity(){
        Intent intent = new Intent(LoadActivity.this,MainActivity.class);
        startActivity(intent);
        LoadActivity.this.finish();
    }

    public void goToOrientationActivity(){
        Intent intent = new Intent(LoadActivity.this,OrientationActivity.class);
        //是否自动进行定位并添加位置
        intent.putExtra("isOrientation",true);
        startActivity(intent);
        LoadActivity.this.finish();
    }

    /**
     * 模拟下载热门城市，热门景点的文件
     * /data/data/com.example.zeng.weather/files/hotfile.txt;
     */
    private void writeToFile(){
        File file = new File(getApplicationContext().getFilesDir(),Constant.HOT_CITY_SCENE_FILE_NAME);
        if(file.exists())  return;
        String hotCity= "定位&北京市&天津市&上海市&重庆市&沈阳市&大连市&长春市&哈尔滨市&郑州市&武汉市&长沙市&广州市&深圳市&南京市";
        String hotScene="故宫博物院&东方明珠塔&黄果树瀑布&黄山风景区&庐山风景区&清明上河园&布达拉宫&秦始皇陵&云冈石窟&镜泊湖&桃花源&黄鹤楼&丽江古城&乐山大佛&南京夫子庙";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(hotCity);
            bufferedWriter.newLine();
            bufferedWriter.write(hotScene);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rpb = null;
    }
}
