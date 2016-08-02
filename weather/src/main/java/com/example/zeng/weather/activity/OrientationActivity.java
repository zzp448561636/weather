package com.example.zeng.weather.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zeng.weather.R;

public class OrientationActivity extends AppCompatActivity {
    private boolean isOrientation;
    private Button btnSearch;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        isOrientation = getIntent().getBooleanExtra("isOrientation",true);

        btnSearch = (Button)findViewById(R.id.btn_search);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.orientation_content);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout,"搜索功能待完善",Snackbar.LENGTH_LONG);
                setSnackbarMessageTextColor(snackbar, Color.WHITE);
                snackbar.show();
            }
        });
    }

    public void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
