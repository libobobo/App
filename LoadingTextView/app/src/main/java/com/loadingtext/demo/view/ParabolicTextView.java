package com.loadingtext.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 抛物线
 * Created by：shaobo
 * on 2016/12/5 19:11
 */
public class ParabolicTextView extends View {

    private Paint mPaint;
    private Path mPath;
    private CharSequence text;
    private PathMeasure mPathMeasure;
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    private ValueAnimator animator;

    public ParabolicTextView(Context context) {
        super(context);
        init();
    }

    public ParabolicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParabolicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(50);
        mPath = new Path();
        text = "正";
        mPath.moveTo(0, 0);//起点
        mPath.quadTo((50 * 0) / 2, 0, 50 * 0, 300);
        mPathMeasure = new PathMeasure(mPath, false);
        animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                postInvalidate();
            }
        });

        mPath.close();
        animator.setDuration(3000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text.charAt(0) + "", mCurrentPosition[0], mCurrentPosition[1], mPaint);

    }
}
