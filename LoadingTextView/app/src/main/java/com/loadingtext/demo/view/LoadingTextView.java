package com.loadingtext.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by：shaobo
 * on 2016/12/5 11:26
 */
public abstract class LoadingTextView extends TextView {
    public Paint mPaint;
    public LoadingTextView(Context context) {
        super(context);
        init();
    }

    public LoadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mPaint = getPaint();
    }

    public abstract void MyDraw(Canvas canvas);
    public abstract void MyAnimator();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        MyDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        MyAnimator();
    }
}
