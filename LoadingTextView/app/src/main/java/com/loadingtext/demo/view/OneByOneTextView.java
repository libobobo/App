package com.loadingtext.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created by：shaobo
 * on 2016/12/5 11:24
 */
public class OneByOneTextView extends LoadingTextView {

    private int width;
    private int dy;
    private float textSize;

    public OneByOneTextView(Context context) {
        super(context);
        init();
    }

    public OneByOneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OneByOneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textSize = getTextSize();
        CharSequence text = getText();
        width = (int) textSize * text.length();
    }

    @Override
    public void MyDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(dy, 0, width, (int) textSize + 8, mPaint);
    }

    @Override
    public void MyAnimator() {

        ValueAnimator animator = ValueAnimator.ofInt(0, width);
        animator.setDuration(5000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dy = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }


}
