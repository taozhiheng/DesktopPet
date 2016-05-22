package com.persist.desktoppet.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.persist.desktoppet.R;


/**
 * Created by taozhiheng on 16-1-24.
 *
 * SlidingLayout must have only one LinearLayout child view,the LinearLayout can have less than(<=) 3 children:
 * one child is used as the main content view,
 * the other child(children) is(are) used as the drawer view(s).
 * the view(s) on the left of leftDrawerView or on the right of rightDrawerView will be removed
 *
 * SlidingLayout extends HorizontalScrollView, implements sliding effect by using the method smoothScrollTo().
 * The child view's scale will be changed when sling by overriding the method onScrollChanged()
 *
 * There a strange problem:
 * when the layout only have contentView and rightDrawerView,
 * overScrolling from left to right may cause the contentView's less than 1f.
 * I don't know why,so I just setScale to 1f.See {@link #closeDrawer()}
 *
 */
public class SlidingLayout extends HorizontalScrollView{

    private final static String TAG = "SlidingLayout";

    public final static int STATE_IDLE_LEFT = -2;
    public final static int STATE_SETTLING_LEFT = -1;
    public final static int STATE_IDLE = 0;
    public final static int STATE_SETTLING_RIGHT = 1;
    public final static int STATE_IDLE_RIGHT = 2;

    private int mLeftWidth;
    private int mScreenWidth;
    private int mRightWidth;
    private boolean once;
    private View mLeft;
    private View mContent;
    private View mRight;

    private int mHeight;

    private int mContentIndex = 0;
    private boolean mAutoScale = true;

    private int mState;

    private Rect mContentRect;

    public SlidingLayout(Context context)
    {
        this(context, null, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        once = false;
        mScreenWidth = this.getScreenWidth(context);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.SlidingLayout, defStyle, 0);

        mContentIndex = a.getInteger(R.styleable.SlidingLayout_contentIndex, 0);
        mAutoScale = a.getBoolean(R.styleable.SlidingLayout_autoScale, true);
        a.recycle();
        mContentRect = new Rect();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!(getChildAt(0) instanceof LinearLayout))
            throw new IllegalArgumentException("SlidingLayout must have only one LinearLayout child view!"+getChildAt(0));
        LinearLayout wrapper = (LinearLayout) getChildAt(0);
        int childCount = wrapper.getChildCount();
        if(mContentIndex >= childCount)
            throw new IllegalArgumentException("The LinearLayout in SlidingLayout contentIndex is out of bounds:count="
                    +childCount+", contentIndex="+mContentIndex);
        //set up contentView,leftDrawerView and rightDrawerView
        mContent =  wrapper.getChildAt(mContentIndex);

        if(mContentIndex-1 >= 0)
            mLeft =  wrapper.getChildAt(mContentIndex-1);
        if(mContentIndex+1 < childCount)
            mRight = wrapper.getChildAt(mContentIndex+1);
        //remove other views
        if(mContentIndex+2 < childCount)
            wrapper.removeViews(mContentIndex+2, childCount-mContentIndex-2);
        if(mContentIndex-2 >= 0)
            wrapper.removeViews(mContentIndex-2, mContentIndex-1);
    }

    /**
     *get screen's width to set the content view's width
     *
     * @param context The context to init SlidingLayout.
     * @return Return the screen's width.
     */
    private int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * set the widths of contentView, leftDrawerView and rightDrawerView
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!once)
        {
            mContent.getLayoutParams().width = mScreenWidth;
            Log.d(TAG, "contentW="+mContent.getWidth()+"/"+mScreenWidth);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mLeft != null)
            mLeftWidth = mLeft.getMeasuredWidth();
        else
            mLeftWidth = 0;
        if(mRight != null)
            mRightWidth = mRight.getMeasuredWidth();
        else
            mRightWidth = 0;
        mHeight = mContent.getHeight();
        Log.d(TAG, "leftW="+mLeftWidth+", rightW="+mRightWidth);
    }

    /**
     * close the drawer(s)
     * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            // 将菜单隐藏
            this.scrollTo(mLeftWidth, 0);
            once = true;
            mContentRect.set(0, 0, mContent.getWidth(), mContent.getHeight());
        }
        Log.d(TAG, "onLayout");
    }


    private float startX;
    private float startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent,x="+ev.getX()+", y="+ev.getY()+" rect:"+mContentRect+" state="+mState);
        //intercept touch event from contentView when the drawers are not closed
        switch (ev.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX()-startX);
                float dy = Math.abs(ev.getY()-startY);
                if(mState == STATE_IDLE && dy>dx)
                    return false;
                break;
        }
        return (mState != STATE_IDLE && mContentRect.contains((int)ev.getX(), (int)ev.getY())) || super.onInterceptTouchEvent(ev);
    }

    private boolean fromContent;
    private boolean scroll;

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getActionMasked();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                fromContent = mContentRect.contains((int)ev.getX(), (int)ev.getY());
                scroll = false;
            case MotionEvent.ACTION_MOVE:
                if(getScrollX() < 0 || getScrollX() > mLeftWidth+mRightWidth) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX < mLeftWidth/2)
                {
                    openLeftDrawer();
                }
                else if(scrollX >= mLeftWidth/2 && scrollX <= mLeftWidth+mRightWidth/2)
                {
                    closeDrawer();
                }
                else
                {
                    openRightDrawer();
                }
                if(fromContent && !scroll && mState != STATE_IDLE && mContentRect.contains((int)ev.getX(), (int)ev.getY()))
                    closeDrawer();
                return true;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * reset the child view's scale while scrolling if {@link #mAutoScale} is true
     *
     * */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d(TAG, "onScrollChanged:"+l+" x="+mContent.getScrollX()+",width="+mContent.getWidth());

        scroll = true;
        if(l == 0)
            mState = STATE_IDLE_LEFT;
        else if(l < mLeftWidth)
            mState = STATE_SETTLING_LEFT;
        else if(l == mLeftWidth)
            mState = STATE_IDLE;
        else if(l < mLeftWidth+mRightWidth)
            mState = STATE_SETTLING_RIGHT;
        else if(l == mLeftWidth+mRightWidth)
            mState = STATE_IDLE_RIGHT;

        if(!mAutoScale || l<0 || l>mLeftWidth+mRightWidth) {
            int left = mLeftWidth-l;
            int right = mLeftWidth+mScreenWidth;
            mContentRect.set(left, 0, right, mHeight);
            return;
        }
        if(l < mLeftWidth && mLeft != null) {
            float scale = l * 1.0f / mLeftWidth;
            float leftScale = 1 - 0.3f * scale;
            float rightScale = 0.8f + scale * 0.2f;

            mLeft.setPivotX(mLeftWidth);
            mLeft.setScaleX(leftScale);
            mLeft.setScaleY(leftScale);
            mLeft.setAlpha(1 - 0.4f * scale);
//            mLeft.setTranslationX(mLeftWidth * scale * 0.7f);

            mContent.setPivotX(0);
            mContent.setPivotY(mContent.getHeight() / 2);
            mContent.setScaleX(rightScale);
            mContent.setScaleY(rightScale);

            int left = mLeftWidth-l;
            int right = (int)(left+mScreenWidth*rightScale);
            int top = (int)(mHeight*(1-rightScale)/2);
            int bottom = (int)(mHeight*(1+rightScale)/2);
            mContentRect.set(left, top, right, bottom);

        }
        else if(l > mLeftWidth && mRight != null)
        {
            float scale = (l-mLeftWidth) * 1.0f / mRightWidth;
            float rightScale = 0.7f + 0.3f * scale;
            float leftScale = 1 - scale * 0.2f;

            mRight.setPivotX(0);
            mRight.setScaleX(rightScale);
            mRight.setScaleY(rightScale);
            mRight.setAlpha(0.6f + 0.4f * scale);
//            mRight.setTranslationX(-mRightWidth * (1 - scale) * 0.7f);

            mContent.setPivotX(mContent.getWidth());
            mContent.setPivotY(mContent.getHeight() / 2);
            mContent.setScaleX(leftScale);
            mContent.setScaleY(leftScale);

            int right = mScreenWidth+mLeftWidth-l;
            int left = (int)(right-mScreenWidth*leftScale);
            int top = (int)(mHeight*(1-leftScale)/2);
            int bottom = (int)(mHeight*(1+leftScale)/2);
            mContentRect.set(left, top, right, bottom);
        }


    }

    /**
     * open the left drawer, display the left view
     * */
    private void openLeftDrawer()
    {
        if(mState == STATE_IDLE_LEFT)
            return;
        this.smoothScrollTo(0, 0);
        mState = STATE_IDLE_LEFT;
        Log.d(TAG, "openLeftDrawer:"+mContent.getScaleX());
    }

    /**
     * close the left or right drawer, display the content view
     * */
    private void closeDrawer()
    {
//        if(mState == STATE_IDLE)
//            return;
        this.smoothScrollTo(mLeftWidth, 0);
        if(mContent.getScaleX() < 1) {
            mContent.setScaleX(1);
            mContent.setScaleY(1);
        }
        mState = STATE_IDLE;
        Log.d(TAG, "closeDrawer:"+mContent.getScaleX());
    }

    /**
     * open the right drawer, display the right view
     * */
    private void openRightDrawer()
    {
        if(mState == STATE_IDLE_RIGHT)
            return;
        this.smoothScrollTo(mLeftWidth+mRightWidth, 0);
        mState = STATE_IDLE_RIGHT;
        Log.d(TAG, "openRightDrawer:"+mContent.getScaleX());
    }


    public int getDrawerState()
    {
        return mState;
    }

    /**
     *change SlidingLayout's drawer state
     *
     * @param state The state value to adjust SlidingLayout.
     */
    public void changeDrawerState(int state)
    {
        switch (state)
        {
            case STATE_IDLE_LEFT:
                openLeftDrawer();
                break;
            case STATE_IDLE:
                closeDrawer();
                break;
            case STATE_IDLE_RIGHT:
                if(mRight != null)
                    openRightDrawer();
                break;
            default:
                throw new IllegalArgumentException("The state value must be one of STATE_IDLE_LEFT, STATE_IDLE, STATE_IDLE_RIGHT!");
        }
    }
}
