package com.example.zeng.weather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.zeng.weather.data.OnAnimationListener;

/**
 * Created by Zeng on 2016/7/25.
 */
public class RoundProgressBar extends View {
    private Context context;
    private BarAnimation anim;
    private Paint defaultCirclePaint;//进度条外围圆圈
    private Paint circlePaint;//进度条内部圆圈
    private Paint textPaint;
    private int[] colors;//渐变色
    private float circleStrokeWidth;
    private float pressExtraStrokeWidth;
    private RectF mColorWheelRectangle;
    private float endAngle;
    private OnAnimationListener animListener;

    public RoundProgressBar(Context context){
        super(context);
        this.context = context;
        init(null,0);
    }

    public RoundProgressBar(Context context,AttributeSet attrs){
        super(context,attrs );
        this.context = context;
        init(attrs,0);
    }

    public RoundProgressBar(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        this.context = context;
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs,int defStyleAttr){
        colors = new int[]{0xFF6BB7ED, 0xFF47D9CE, 0xFF56CADC};
        Shader s = new SweepGradient(0, 0, colors, null);
        defaultCirclePaint = new Paint();
        defaultCirclePaint.setShader(s);
        defaultCirclePaint.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
        defaultCirclePaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
        defaultCirclePaint.setAntiAlias(true);// 去锯齿

        circlePaint = new Paint();
        circlePaint.setColor(Color.rgb(255,140,0));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        anim = new BarAnimation();
        mColorWheelRectangle = new RectF();
        endAngle = -90f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mColorWheelRectangle, 0, 360, false, circlePaint);
        canvas.drawText("跳过",circleStrokeWidth * 2,
                mColorWheelRectangle.bottom - (circleStrokeWidth * 3),
                textPaint);
        canvas.drawArc(mColorWheelRectangle, -90, endAngle, false, defaultCirclePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        circleStrokeWidth = Textscale(50, min);// 圆弧的宽度
        pressExtraStrokeWidth = Textscale(2, min);// 圆弧离矩形的距离

        mColorWheelRectangle.set(circleStrokeWidth, circleStrokeWidth,
                min-circleStrokeWidth, min-circleStrokeWidth);// 设置矩形


//        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth,
//                circleStrokeWidth + pressExtraStrokeWidth, min
//                        - circleStrokeWidth - pressExtraStrokeWidth, min
//                        - circleStrokeWidth - pressExtraStrokeWidth);// 设置矩形
        circlePaint.setStrokeWidth(circleStrokeWidth );
        defaultCirclePaint.setStrokeWidth(circleStrokeWidth);
//        textPaint.setTextSize(circleStrokeWidth * 2);
        textPaint.setTextSize(circleStrokeWidth * 3);
    }

    /**
     * 根据控件的大小改变绝对位置的比例
     *
     * @param n
     * @param m
     * @return
     */
    private float Textscale(float n, float m) {
        return n / 500 * m;
    }

    public void startAnimation(int time, final OnAnimationListener ainmListener){
        anim.setDuration(time);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ainmListener.OnAnimationFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(anim);
        this.animListener = ainmListener;
    }

    public void stopAnimation(){
        this.clearAnimation();
        animListener.OnAnimationFinished();
    }

    class BarAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            endAngle =  interpolatedTime * 360;
            postInvalidate();
        }
    }
}
