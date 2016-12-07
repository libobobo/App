package com.loadingtext.demo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.loadingtext.demo.R;

/**
 * Created by：shaobo
 * on 2016/12/6 14:10
 */
public class JumpTextView extends View {

    private Paint mPaint;
    private Path mPath;
    private int i;
    private PathMeasure mPathMeasure;
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    private int color;
    private CharSequence text;
    private float mTextSize;
    private Context mContext;

    public JumpTextView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        init();
    }

    public JumpTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context.getApplicationContext();
        init();
    }

    public JumpTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
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

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setTextSize(mTextSize);
        mPath = new Path();
    }

    private void PlayAnimator() {
        if (i < text.length()) {
            mPath.moveTo(mTextSize * i, mTextSize);//起点
            mPath.quadTo(mTextSize * i, mTextSize / 2, mTextSize * i, mTextSize);//25 表示向上跳动字体的一半大小
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
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int j = 0; j < text.length(); j++) {
            if (j == i) continue;
            canvas.drawText(text.charAt(j) + "", mTextSize * j + paddingLeft, mTextSize + paddingTop, mPaint);
        }
        if (i < text.length()) {
            canvas.drawText(text.charAt(i) + "", mCurrentPosition[0] + paddingLeft, mCurrentPosition[1] + paddingTop, mPaint);//画曲线中间点坐标
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        PlayAnimator();
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
        } else if (widthmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), heightsize);
        } else if (heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthsize, (int) mTextSize + 8 + getPaddingTop() + getPaddingBottom());
        }
    }
}
