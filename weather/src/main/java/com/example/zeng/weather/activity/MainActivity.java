package com.example.zeng.weather.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.zeng.weather.Fragment.SceneFragment;
import com.example.zeng.weather.Fragment.UserFragment;
import com.example.zeng.weather.Fragment.WeatherFragment;
import com.example.zeng.weather.R;
import com.example.zeng.weather.data.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private WeatherFragment weatherFragment;
    private SceneFragment sceneFragment;
    private final String WEATHER_TAG = "weather";
    private final String SCENE_TAG = "scene";
    private final String USER_TAG = "user";
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        fragmentManager = getSupportFragmentManager();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateFragment();
            }
        });
//        radioGroup.check(R.id.radio_weather)方法会执行三次onCheckedChangeListener方法
//        具体解释可以看http://stackoverflow.com/questions/10263778/radiogroup-calls-oncheckchanged-three-times
//        执行多次，后几次fragment.isAdd也返回false，FragmentHostCallback为空，重新add一次weatherFragment，报错
//        为什么FragmentHostCallback mHost为空，暂时没找到答案，如果大神知道请指教
        ((RadioButton)findViewById(R.id.radio_weather)).setChecked(true);
    }

    private void updateFragment(){
        FragmentTransaction tran = fragmentManager.beginTransaction();
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radio_weather:
                if(weatherFragment == null) weatherFragment = new WeatherFragment();
                if(!weatherFragment.isAdded())tran.add(R.id.frame_layout,weatherFragment,WEATHER_TAG);
                else tran.show(weatherFragment);
                if(sceneFragment !=null && sceneFragment.isAdded() && !sceneFragment.isHidden())tran.hide(sceneFragment);
                if(userFragment !=null && userFragment.isAdded() && !userFragment.isHidden())tran.hide(userFragment);
                break;
            case R.id.radio_scene:
                if(sceneFragment == null)   sceneFragment = new SceneFragment();
                if(!sceneFragment.isAdded()) tran.add(R.id.frame_layout,sceneFragment,SCENE_TAG);
                else tran.show(sceneFragment);
                if(weatherFragment !=null && weatherFragment.isAdded() && !weatherFragment.isHidden())tran.hide(weatherFragment);
                if(userFragment !=null && userFragment.isAdded() && !userFragment.isHidden())tran.hide(userFragment);
                break;
            case R.id.radio_user:
                if(userFragment == null)  userFragment = new UserFragment();
                if(!userFragment.isAdded()) tran.add(R.id.frame_layout,userFragment,USER_TAG);
                else tran.show(userFragment);
                if(weatherFragment !=null && weatherFragment.isAdded() && !weatherFragment.isHidden())tran.hide(weatherFragment);
                if(sceneFragment !=null && sceneFragment.isAdded() && !sceneFragment.isHidden())tran.hide(sceneFragment);
                break;
        }
        tran.setCustomAnimations(R.anim.alpha_in,R.anim.alpha_out);
        tran.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO:必做：根据currentIndex判断是否无选中城市，即size==0，是否小于cityInfoList.size，
    }

    public void onFragmentInteraction(Uri uri){

    }
}
