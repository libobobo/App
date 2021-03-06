package com.loadingtext.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.loadingtext.demo.utils.DensityUtil;

/**
 * 在textview 上画上一成和文字长宽相同且与背景颜色相同的矩形，不断改变矩形宽度已达到目的
 * Created by：shaobo
 * on 2016/12/5 11:24
 */
public class OneByOneTextView extends LoadingTextView {

    private int width;
    private int dy;
    private float textSize;
    private Context mContext;
    public OneByOneTextView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        init();
    }

    public OneByOneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context.getApplicationContext();
        init();
    }

    public OneByOneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
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
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        canvas.drawRect(dy + paddingLeft, 0 + paddingTop, width+ paddingLeft, (int) textSize + DensityUtil.px2dip(mContext,36), mPaint);
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
