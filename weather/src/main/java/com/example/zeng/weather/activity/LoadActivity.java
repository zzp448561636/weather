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
import com.example.zeng.weather.data.Constant;
import com.example.zeng.weather.data.OnAnimationListener;
import com.example.zeng.weather.widget.RoundProgressBar;

public class LoadActivity extends AppCompatActivity implements OnAnimationListener {
    private ImageView iv;
    private int animationTime = 3000;
    private RoundProgressBar rpb;

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

         //设想中应该先get方法获取广告图片下载url，再进行下载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAdv();
            }
        },2000);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rpb = null;
    }
}
