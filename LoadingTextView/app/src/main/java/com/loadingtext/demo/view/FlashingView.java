package com.loadingtext.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.loadingtext.demo.R;
import com.loadingtext.demo.utils.DensityUtil;

/**
 * Created by：shaobo
 * on 2016/12/8 10:52
 */
public class FlashingView extends View {

    private Context mContext;
    private int color;
    private CharSequence text;
    private float mTextSize;
    private Paint mPaint;
    private int dx;

    public FlashingView(Context context) {
        this(context, null, 0);
    }

    public FlashingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JumpTextView);
        color = typedArray.getColor(R.styleable.JumpTextView_textColor, Color.BLACK);
        text = typedArray.getString(R.styleable.JumpTextView_text);
        if (text == null) {
            text = "Hello word";
        }
        mTextSize = typedArray.getFloat(R.styleable.JumpTextView_textSize, 100);
        mTextSize = DensityUtil.px2dip(mContext, mTextSize);
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < text.length(); i++) {
            if (i == dx) {
                mPaint.setTextSize(mTextSize * 1.5f);
                canvas.drawText(text.charAt(i) + "", mTextSize * i + getPaddingLeft(), mTextSize + getPaddingTop(), mPaint);
            } else {
                mPaint.setTextSize(mTextSize);
                canvas.drawText(text.charAt(i) + "", mTextSize * i + getPaddingLeft(), mTextSize + getPaddingTop(), mPaint);
            }
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ValueAnimator animator = ValueAnimator.ofInt(0, text.length());
        animator.setDuration(3000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthmode == MeasureSpec.AT_MOST && heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), (int) (mTextSize) + getPaddingTop() + getPaddingBottom() + DensityUtil.px2dip(mContext, 36));
        } else if (widthmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), heightsize);
        } else if (heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthsize, (int) (mTextSize) + getPaddingTop() + getPaddingBottom() + DensityUtil.px2dip(mContext, 36));
        }
    }
}
