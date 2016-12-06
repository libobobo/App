package com.loadingtext.demo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.HashMap;

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
    private int i;
    //记录最终坐标点
    private HashMap<Integer, Point> endloc;


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
        text = "正在加载...";
        endloc = new HashMap<>();
        PlayAnimator();
    }

    private void PlayAnimator() {
        if (i < text.length()) {
            mPath.moveTo(0, 0);//起点
            mPath.quadTo((50 * i) / 2, 0, 50 * i, 300);
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
            animator.setDuration(1500);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //每一个动画执行完成后记录最后一个坐标点
                    Point point = new Point();
                    point.x = (int) mCurrentPosition[0];
                    point.y = (int) mCurrentPosition[1];
                    endloc.put(i, point);
                    i++;
                    if (i >= text.length()) {//当i大于7的时候置为零方便下次循环
                        i = 0;
                        endloc.clear();//清空集合，从新来过
                    }
                    mPath.reset();
                    mPath.moveTo(0, 0);
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
        if (i < text.length()) {
            canvas.drawText(text.charAt(i) + "", mCurrentPosition[0], mCurrentPosition[1], mPaint);//画曲线中间点坐标
            if (endloc.size() > 0) {
                for (int j = 0; j < endloc.size(); j++) {
                    canvas.drawText(text.charAt(j) + "", endloc.get(j).x, endloc.get(j).y, mPaint);
                }
            }
        }
    }

}
