package com.persist.desktoppet.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by taozhiheng on 16-1-24.
 *
 * SlidingLayout must have only one LinearLayout child view,the LinearLayout can have 2 or 3 children:
 * the first child is used as the left drawer view,
 * the second child is used as the main content view,
 * the third child is used as the right drawer view.
 *
 * SlidingLayout extends HorizontalScrollView, implements sliding effect by using the method smoothScrollTo().
 * The child view's scale will be changed when sling by overriding the method onScrollChanged()
 */
public class SwipeLayout extends HorizontalScrollView{

    private int mLeftWidth = 0;
    private int mScreenWidth;
    private int mRightWidth;
    private boolean once;
//    private View mLeft;
    private View mContent;
    private View mRight;

    public SwipeLayout(Context context)
    {
        this(context, null, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
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

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        once = false;
        mScreenWidth = getScreenWidth(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!once)
        {
            if(!(getChildAt(0) instanceof LinearLayout))
                throw new IllegalArgumentException("SlidingLayout must have only one LinearLayout child view!");
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            int childCount = wrapper.getChildCount();
            if(childCount != 2)
                throw new IllegalArgumentException("The LinearLayout in SlidingLayout should have 2 children!");
//            mLeft =  wrapper.getChildAt(0);
            mContent =  wrapper.getChildAt(0);
//            if(childCount == 3)
            mRight = wrapper.getChildAt(1);

            mContent.getLayoutParams().width = mScreenWidth;


        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mLeftWidth = mLeft.getMeasuredWidth();
//        if(mRight != null)
            mRightWidth = mRight.getMeasuredWidth();
//        else
//            mRightWidth = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            // 将菜单隐藏
            this.scrollTo(mLeftWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
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
                return true;

        }
        return super.onTouchEvent(ev);
    }



    /**
     * open the left drawer, display the left view
     * */
    private void openLeftDrawer()
    {
        this.smoothScrollTo(0, 0);
    }

    /**
     * close the left or right drawer, display the content view
     * */
    private void closeDrawer()
    {
        this.smoothScrollTo(mLeftWidth, 0);
    }

    /**
     * open the right drawer, display the right view
     * */
    private void openRightDrawer()
    {
        this.smoothScrollTo(mLeftWidth+mRightWidth, 0);
    }


    public final static int LEFT_OPEN = 0;
    public final static int CLOSE = 1;
    public final static int RIGHT_OPEN = 2;

    /**
     *change SlidingLayout's drawer state
     *
     * @param state The state value to adjust SlidingLayout.
     */
    public void changeDrawerState(int state)
    {
        switch (state)
        {
            case LEFT_OPEN:
                openLeftDrawer();
                break;
            case CLOSE:
                closeDrawer();
                break;
            case RIGHT_OPEN:
                if(mRight != null)
                    openRightDrawer();
                break;
            default:
                throw new IllegalArgumentException("The state value must be one of LEFT_OPEN, CLOSE, RIGHT_OPEN!");
        }
    }
}
