package com.loadingtext.demo.view;

import android.animation.IntEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * Created by：shaobo
 * on 2016/12/5 13:39
 */
public class RightToLeftTextView extends View {


    private int width;
    private CharSequence text;
    private int dy;
    private Paint mPaint;
    private HashMap<Integer, Integer> map;
    private ValueAnimator animator1;
    private ValueAnimator animator;


    public RightToLeftTextView(Context context) {
        super(context);
        init();
    }

    public RightToLeftTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RightToLeftTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        map = new HashMap<>();
        mPaint = new Paint();
        mPaint.setTextSize(50);
        text = "正在加载...";
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        animator = ValueAnimator.ofInt(0, width);
        animator.setDuration(3000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float v) {
                return v;
            }
        });
        animator.setEvaluator(new IntEvaluator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dy = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();

        for (int i = 0; i < text.length(); i++) {
            animator1 = ValueAnimator.ofInt(250 * i, 50 * i);
            final int y = i;
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//不开始执行动画不会走此方法
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int dx = (int) animation.getAnimatedValue();
                    map.put(y, dx);
                    postInvalidate();
                }
            });
            animator1.setDuration(3000);
            animator1.setRepeatMode(ValueAnimator.RESTART);
            animator1.setRepeatCount(ValueAnimator.INFINITE);
            animator1.setInterpolator(new TimeInterpolator() {
                @Override
                public float getInterpolation(float v) {
                    return v;
                }
            });
            animator1.setEvaluator(new IntEvaluator());
            animator1.start();
        }
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < text.length(); i++) {
            canvas.drawText(text.charAt(i) + "", width - dy + map.get(i), 70, mPaint);
        }
    }

    public void cancle() {
        if (animator1 != null) {
            animator1.cancel();
        }
        if (animator != null) {
            animator.cancel();
        }
        if (map != null) {
            map.clear();
        }
    }
}
