package com.persist.desktoppet.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by taozhiheng on 16-5-17.
 *
 */
public class ReverseLayout extends RelativeLayout {

    private final static String TAG = "ReverseLayout";

    private Animation mOpenAnimation;
    private Animation mCloseAnimation;
    private Animation mOpenAppendAnimation;
    private Animation mCloseAppendAnimation;
    private float mCenterX;
    private float mCenterY;

    private int mDuration = 250;
    private float mDepthZ = 300;

    private View mFront;
    private View mBack;
    private boolean mIsShowBack;

    private boolean mIsReversing;

    public ReverseLayout(Context context)
    {
        this(context, null);
    }

    public ReverseLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mIsShowBack = false;
        mIsReversing = false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() != 2)
            throw new IllegalArgumentException("Reverse Layout must have 2 children");
        mBack = getChildAt(0);
        mFront = getChildAt(1);
        mBack.setVisibility(INVISIBLE);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != oldw || h != oldh)
        {
            mCenterX = getWidth()/2;
            mCenterY = getHeight()/2;
            initAnimations();
        }
    }

    private void initAnimations()
    {
        initOpenAnim();
        initCloseAnim();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean res = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onIntercept:"+res);
        return res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = super.onTouchEvent(event);
        Log.d(TAG, "onTouch:"+res);
        return res;
    }

    /**
     * 卡牌文本介绍打开效果：注意旋转角度
     */
    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        mOpenAnimation = new Rotate3dAnimation(0, 90, mCenterX, mCenterY, mDepthZ, true);
        mOpenAnimation.setDuration(mDuration);
        mOpenAnimation.setFillAfter(true);
        mOpenAnimation.setInterpolator(new AccelerateInterpolator());

        //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
        mOpenAppendAnimation = new Rotate3dAnimation(270, 360, mCenterX, mCenterY, mDepthZ, false);
        mOpenAppendAnimation.setDuration(mDuration);
        mOpenAppendAnimation.setFillAfter(true);
        mOpenAppendAnimation.setInterpolator(new DecelerateInterpolator());

        mOpenAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mIsReversing = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFront.setVisibility(View.INVISIBLE);
                mBack.setVisibility(View.VISIBLE);
                startAnimation(mOpenAppendAnimation);
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
//                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, mCenterX, mCenterY, mDepthZ, false);
//                rotateAnimation.setDuration(mDuration);
//                rotateAnimation.setFillAfter(true);
//                rotateAnimation.setInterpolator(new DecelerateInterpolator());
//                startAnimation(rotateAnimation);
            }
        });

        mOpenAppendAnimation.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsReversing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private void initCloseAnim() {
        mCloseAnimation = new Rotate3dAnimation(360, 270, mCenterX, mCenterY, mDepthZ, true);
        mCloseAnimation.setDuration(mDuration);
        mCloseAnimation.setFillAfter(true);
        mCloseAnimation.setInterpolator(new AccelerateInterpolator());

        mCloseAppendAnimation = new Rotate3dAnimation(90, 0, mCenterX, mCenterY, mDepthZ, false);
        mCloseAppendAnimation.setDuration(mDuration);
        mCloseAppendAnimation.setFillAfter(true);
        mCloseAppendAnimation.setInterpolator(new DecelerateInterpolator());

        mCloseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mIsReversing = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFront.setVisibility(View.VISIBLE);
                mBack.setVisibility(View.INVISIBLE);
                startAnimation(mCloseAppendAnimation);

//                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(90, 0, mCenterX, mCenterY, mDepthZ, false);
//                rotateAnimation.setDuration(mDuration);
//                rotateAnimation.setFillAfter(true);
//                rotateAnimation.setInterpolator(new DecelerateInterpolator());
//                startAnimation(rotateAnimation);
            }
        });

        mCloseAppendAnimation.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsReversing = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setDuration(int duration)
    {
        if(duration > 0 && mDuration != duration) {
            mDuration = duration;
            clearAnimation();
            initAnimations();
        }
    }

    public void setDepthZ(float depthZ)
    {
        if(mDepthZ != depthZ)
        {
            mDepthZ = depthZ;
            clearAnimation();
            initAnimations();
        }
    }

    public boolean isShowBack()
    {
        return mIsShowBack;
    }

    public void toggle()
    {
        if(mIsReversing)
            return;
        //判断动画执行
        if (mIsShowBack) {
            Log.d(TAG, "closeAnimation");
            startAnimation(mCloseAnimation);
        }else {
            Log.d(TAG, "openAnimation");
            startAnimation(mOpenAnimation);
        }
        mIsShowBack = !mIsShowBack;
    }
}
