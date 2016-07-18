package com.mecury.circlelibrary;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by 海飞 on 2016/7/12.
 */
public class CircleBackground extends RelativeLayout {

    //默认属性
    private static final int DEFAULT_BALL_RUNNING_DURATION = 6000;

    private final float default_out_circle_radius;
    private final float default_out_circle_strokeWidth;
    private final float default_in_circle_radius;
    private final float default_in_circle_width;
    private final float default_ball_radius;

    //
    private int outCircleColor;
    private float outCircleRadius;
    private float outCircleStrokeWidth;    //外部圆的宽度
    private int inCircleColor;
    private float inCircleRadius;
    private float inCircleWidth;      //内部圆环宽度
    private int ball_color;
    private float ballRadius;
    private int ballRunningDuration;

    private Paint outPaint;
    private Paint inPaint;
    private Paint innerPaint;
    private Paint ballPaint;

    private LayoutParams mLayoutParams;

    private AnimatorSet animatorSet = new AnimatorSet();
    private boolean ballRunning = false;


    public CircleBackground(Context context) {
        this(context, null);
    }

    public CircleBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context,attrs);
    }

    public CircleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_in_circle_radius = Utils.dp2px(getResources(), 40);
        default_in_circle_width = Utils.dp2px(getResources(), 6);
        default_out_circle_radius = Utils.dp2px(getResources(), 50);
        default_out_circle_strokeWidth = Utils.dp2px(getResources(), 2);
        default_ball_radius = Utils.dp2px(getResources(), 5);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleBackground);
        outCircleColor = typedArray.getColor(R.styleable.CircleBackground_outCircleColor
                ,getResources().getColor(R.color.out_circle_color));
        outCircleRadius = typedArray.getDimension(R.styleable.CircleBackground_outCircleRadius
                ,default_out_circle_radius);
        outCircleStrokeWidth = typedArray.getDimension(R.styleable.CircleBackground_outCircleStrokeWidth
                ,default_out_circle_strokeWidth);
        inCircleColor = typedArray.getColor(R.styleable.CircleBackground_inCircleColor
                ,getResources().getColor(R.color.in_circle_color));
        inCircleRadius = typedArray.getDimension(R.styleable.CircleBackground_inCircleRadius
                ,default_in_circle_radius);
        inCircleWidth = typedArray.getDimension(R.styleable.CircleBackground_inCircleWidth
                , default_in_circle_width);
        ball_color = typedArray.getColor(R.styleable.CircleBackground_ballColor
                ,getResources().getColor(R.color.ball_color));
        ballRadius = typedArray.getDimension(R.styleable.CircleBackground_ballRadius
                ,default_ball_radius);
        ballRunningDuration = typedArray.getInt(R.styleable.CircleBackground_ballRunningDuration
                , DEFAULT_BALL_RUNNING_DURATION);
        typedArray.recycle();

        outPaint = new Paint();
        inPaint = new Paint();
        innerPaint = new Paint();
        ballPaint = new Paint();

        mLayoutParams = new LayoutParams((int)(2.5*(outCircleRadius + outCircleStrokeWidth))
                , (int)(2.5*(outCircleRadius + outCircleStrokeWidth)));
        mLayoutParams.addRule(CENTER_IN_PARENT);


        //外圆
        outPaint.setColor(outCircleColor);
        outPaint.setAntiAlias(true);
        outPaint.setStrokeWidth(outCircleStrokeWidth);
        outPaint.setStyle(Paint.Style.STROKE);
        CircleView outInCircle = new CircleView(getContext(), outPaint, 0, outCircleRadius);
        addView(outInCircle, mLayoutParams);

        //内环
        inPaint.setColor(inCircleColor);
        inPaint.setAntiAlias(true);
        inPaint.setStrokeWidth(inCircleWidth);
        inPaint.setStyle(Paint.Style.STROKE);
        CircleView innerInCircle = new CircleView(getContext(), inPaint, 0, inCircleRadius);
        addView(innerInCircle,mLayoutParams);

        innerPaint.setAntiAlias(true);
        innerPaint.setColor(inCircleColor);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setStrokeWidth(inCircleWidth);
        CircleView innerCircle = new CircleView(getContext(), innerPaint, 0, inCircleRadius + inCircleWidth /2 + 1);
        addView(innerCircle, mLayoutParams);

        ballPaint.setAntiAlias(true);
        ballPaint.setColor(ball_color);
        ballPaint.setStyle(Paint.Style.FILL);
        BallCircleView ball = new BallCircleView(getContext(),ballPaint);
        addView(ball,mLayoutParams);

        //旋转动画
        ball.setPivotX(mLayoutParams.width/2);
        ball.setPivotY(mLayoutParams.height/2);
        final ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(ball,"rotation", 0, 360);
        rotationAnimator.setDuration(ballRunningDuration);
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        animatorSet.play(rotationAnimator);
    }

    private class CircleView extends View {

        Paint paint;
        float strokeWidth;
        float radius;

        public CircleView(Context context, Paint paint, float strokeWidth, float radius) {
            super(context);
            this.strokeWidth = strokeWidth;
            this.paint = paint;
            this.radius = radius;
            this.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int center=(Math.min(getWidth(),getHeight()))/2;
            canvas.drawCircle(center,center,radius,paint);
        }
    }

    private class BallCircleView extends View {

        Paint paint;

        public BallCircleView(Context context, Paint paint){
            super(context);
            this.paint = paint;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int center=(Math.min(getWidth(),getHeight()))/2;
            canvas.drawCircle(center + outCircleRadius,center, ballRadius,paint);
        }
    }

    public void startAnimation(){
        if (!isBallRunning()){
            animatorSet.start();
            ballRunning = true;
        }
    }

    public void endAnimation(){
        if (isBallRunning()){
            animatorSet.end();
            ballRunning = false;
        }
    }

    public boolean isBallRunning(){
        return ballRunning;
    }
}























