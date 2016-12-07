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
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.loadingtext.demo.R;
import com.loadingtext.demo.utils.DensityUtil;

import java.util.HashMap;

/**
 * 抛物线 同样利用二阶贝塞尔曲线完成 起点为（0,0） 终点为每个字的横坐标纵坐标 控制点选为横坐标的一半
 * 纵坐标和起点相同
 * 利用动画和路径测量中得到运动轨迹
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
    private int color;
    private float mTextSize;
    private Context mContext;

    public ParabolicTextView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        init();
    }

    public ParabolicTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context.getApplicationContext();
        init();
    }

    public ParabolicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JumpTextView);
        color = typedArray.getColor(R.styleable.JumpTextView_textColor, Color.BLACK);
        text = typedArray.getString(R.styleable.JumpTextView_text);
        if (text == null) {
            text = "Hello word";
        }
        mTextSize = typedArray.getFloat(R.styleable.JumpTextView_textSize, 40);
        mTextSize = DensityUtil.px2dip(mContext, mTextSize);
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPath = new Path();
        endloc = new HashMap<>();
    }

    private void PlayAnimator() {
        if (i < text.length()) {
            mPath.moveTo(0, 0);//起点
            mPath.quadTo((mTextSize * i) / 2, 0, mTextSize * i, mTextSize + DensityUtil.px2dip(mContext, 300));
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
        if (i < text.length()) {
            canvas.drawText(text.charAt(i) + "", mCurrentPosition[0] + paddingLeft, mCurrentPosition[1] + paddingTop, mPaint);//画曲线中间点坐标
            if (endloc.size() > 0) {
                for (int j = 0; j < endloc.size(); j++) {
                    canvas.drawText(text.charAt(j) + "", endloc.get(j).x + paddingLeft, endloc.get(j).y + paddingTop, mPaint);
                }
            }
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
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), (int) mTextSize + getPaddingTop() + getPaddingBottom() +  DensityUtil.px2dip(mContext, 300));
        } else if (widthmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mTextSize * text.length() + getPaddingLeft() + getPaddingRight(), heightsize);
        } else if (heightmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthsize, (int) mTextSize + getPaddingTop() + getPaddingBottom() +  DensityUtil.px2dip(mContext, 300));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PlayAnimator();
    }
}
