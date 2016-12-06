package com.loadingtext.demo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by：shaobo
 * on 2016/12/6 14:10
 */
public class JumpTextView extends View {

    private Paint mPaint;
    private Path mPath;

    private CharSequence text;
    private int i;
    private PathMeasure mPathMeasure;
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];

    public JumpTextView(Context context) {
        super(context);
        init();
    }

    public JumpTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JumpTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(50);
        mPaint.setColor(Color.BLUE);
        mPath = new Path();
        text = "正在加载...";
        PlayAnimator();

    }

    private void PlayAnimator() {
        if (i < text.length()) {
            mPath.moveTo(50 * i, 50);//起点
            mPath.quadTo(50 * i, 25, 50 * i, 50);//25 表示向上跳动字体的一半大小
            mPathMeasure = new PathMeasure(mPath, false);
            ValueAnimator animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    mPathMeasure.getPosTan(value, mCurrentPosition, null);
                    postInvalidate();
                }
            });
            mPath.close();
            animator.setDuration(500);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    i++;
                    if (i >= text.length()) {//当i大于7的时候置为零方便下次循环
                        i = 0;
                    }
                    mPath.reset();
                    PlayAnimator();

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int j = 0; j < text.length(); j++) {
            if (j == i) continue;
            canvas.drawText(text.charAt(j) + "", 50 * j, 50, mPaint);
        }
        if (i < text.length()) {
            canvas.drawText(text.charAt(i) + "", mCurrentPosition[0], mCurrentPosition[1], mPaint);//画曲线中间点坐标
        }
    }

}
