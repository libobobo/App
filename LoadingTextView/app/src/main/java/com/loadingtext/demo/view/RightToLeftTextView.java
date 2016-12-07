package com.loadingtext.demo.view;

import android.animation.IntEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.loadingtext.demo.R;

import java.util.HashMap;

/**
 * 每个字由屏幕右侧做位移  每个字间的间隔设为250 * 字的个数 在缩小至字体大小 * 个数
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
    private int color;
    private float mTextSize;


    public RightToLeftTextView(Context context) {
        super(context);
        init();
    }

    public RightToLeftTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JumpTextView);
        color = typedArray.getColor(R.styleable.JumpTextView_textColor, Color.BLACK);
        text = typedArray.getString(R.styleable.JumpTextView_text);
        if (text == null) {
            text = "Hello word";
        }
        mTextSize = typedArray.getFloat(R.styleable.JumpTextView_textSize, 20);
        typedArray.recycle();
        init();
    }

    public RightToLeftTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    private void init() {
        map = new HashMap<>();
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setTextSize(mTextSize);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        animator = ValueAnimator.ofInt(0, width);
        animator.setDuration(5000);
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
            animator1 = ValueAnimator.ofInt(250 * i, (int) mTextSize * i);
            final int y = i;
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//不开始执行动画不会走此方法
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int dx = (int) animation.getAnimatedValue();
                    map.put(y, dx);
                    postInvalidate();
                }
            });
            animator1.setDuration(5000);
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
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < text.length(); i++) {
            canvas.drawText(text.charAt(i) + "", width - dy + map.get(i) + paddingLeft, mTextSize + paddingTop, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthmode == MeasureSpec.AT_MOST && heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), (int) mTextSize + 8 + getPaddingTop() + getPaddingBottom());
        } else if (heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthsize, (int) mTextSize + 8 + getPaddingTop() + getPaddingBottom());
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
