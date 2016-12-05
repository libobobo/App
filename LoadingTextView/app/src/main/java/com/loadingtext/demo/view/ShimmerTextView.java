package com.loadingtext.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;


/**闪动文字
 * Created by：shaobo
 * on 2016/12/5 10:54
 */
public class ShimmerTextView extends LoadingTextView{
    private int mDx;
    private LinearGradient mLinearGradient;

    public ShimmerTextView(Context context) {
        super(context);
    }

    public ShimmerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void MyDraw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(mDx, 0);
        mLinearGradient.setLocalMatrix(matrix);
        mPaint.setShader(mLinearGradient);
    }

    @Override
    public void MyAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 2 * getMeasuredWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDx = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.start();

        mLinearGradient = new LinearGradient(-getMeasuredWidth(), 0, 0, 0, new int[]{
                getCurrentTextColor(), 0xff00ff00, getCurrentTextColor()
        },
                new float[]{
                        0,
                        0.5f,
                        1
                },
                Shader.TileMode.CLAMP
        );
    }
}
