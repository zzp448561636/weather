package com.example.zeng.weather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.example.zeng.weather.R;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_weather:
                        break;
                    case R.id.radio_scene:
                        break;
                    case R.id.radio_user:
                        break;
                }
            }
        });
        radioGroup.check(R.id.radio_weather);
    }

    private void initGesture(){
        gestureListener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
               return super.onScroll(e1, e2, distanceX, distanceY);
            }
        };
        gestureDetector = new GestureDetector(this,gestureListener);
    }
}
