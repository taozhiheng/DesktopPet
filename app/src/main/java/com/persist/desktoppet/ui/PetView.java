package com.persist.desktoppet.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.cunoraz.gifview.library.GifView;
import com.persist.desktoppet.R;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.util.ScreenUtil;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * pet view
 * display pet
 */
public class PetView extends GifView {


    private Context mContext;
    private boolean mIsShow = true;
    private int mEmotion;
    private int mResource;
    private Drawable mDrawable;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mMaxWidth;
    private int mMaxHeight;
    private boolean mAdjustViewBounds;
    private boolean mAdjustViewBoundsCompat;

//    private WindowManager wm;
//    private static WindowManager.LayoutParams params;
    private float startX;
    private float startY;
    private float x;
    private float y;
    private int TOOL_BAR_HIGH = 0;
    private float mTouchSlop;
    private int mScreenWidth = 0;

    private OnPositionChangeListener mPosListener;

    private final static String TAG = "PetView";



    public interface OnPositionChangeListener
    {
        void onPositionChange(int newX, int newY);
    }

    public PetView(Context context)
    {
        this(context, null);
    }

    public PetView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PetView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPetView();
    }

    private void initPetView()
    {
        mPaddingLeft = 0;
        mPaddingRight = 0;
        mPaddingTop = 0;
        mPaddingBottom = 0;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TOOL_BAR_HIGH = ScreenUtil.getStatusBarHeight(getContext());
        DisplayMetrics display = ScreenUtil.getDisplay(getContext());
        mScreenWidth = display.widthPixels;
        mMaxWidth = display.widthPixels/2;
        mMaxHeight = display.widthPixels/2;
        mAdjustViewBounds = true;
        mDrawable = getResources().getDrawable(R.drawable.sticker);
        if(mDrawable != null) {
            mDrawableWidth = mDrawable.getIntrinsicWidth();
            mDrawableHeight = mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        mAdjustViewBoundsCompat = mContext.getApplicationInfo().targetSdkVersion <=
                Build.VERSION_CODES.JELLY_BEAN_MR1;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//
//        int w;
//        int h;
//
//        // Desired aspect ratio of the view's contents (not including padding)
//        float desiredAspect = 0.0f;
//
//        // We are allowed to change the view's width
//        boolean resizeWidth = false;
//
//        // We are allowed to change the view's height
//        boolean resizeHeight = false;
//
//        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        if (mDrawable == null) {
//            // If no drawable, its intrinsic size is 0.
//            mDrawableWidth = -1;
//            mDrawableHeight = -1;
//            w = h = 0;
//        } else {
//            w = mDrawableWidth;
//            h = mDrawableHeight;
//            if (w <= 0) w = 1;
//            if (h <= 0) h = 1;
//
//            // We are supposed to adjust view bounds to match the aspect
//            // ratio of our drawable. See if that is possible.
//            if (mAdjustViewBounds) {
//                resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
//                resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;
//
//                desiredAspect = (float) w / (float) h;
//            }
//        }
//
//        int pleft = mPaddingLeft;
//        int pright = mPaddingRight;
//        int ptop = mPaddingTop;
//        int pbottom = mPaddingBottom;
//
//        int widthSize;
//        int heightSize;
//
//        if (resizeWidth || resizeHeight) {
//            /* If we get here, it means we want to resize to match the
//                drawables aspect ratio, and we have the freedom to change at
//                least one dimension.
//            */
//
//            // Get the max possible width given our constraints
//            widthSize = resolveAdjustedSize(w + pleft + pright, mMaxWidth, widthMeasureSpec);
//
//            // Get the max possible height given our constraints
//            heightSize = resolveAdjustedSize(h + ptop + pbottom, mMaxHeight, heightMeasureSpec);
//
//            if (desiredAspect != 0.0f) {
//                // See what our actual aspect ratio is
//                float actualAspect = (float)(widthSize - pleft - pright) /
//                        (heightSize - ptop - pbottom);
//
//                if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {
//
//                    boolean done = false;
//
//                    // Try adjusting width to be proportional to height
//                    if (resizeWidth) {
//                        int newWidth = (int)(desiredAspect * (heightSize - ptop - pbottom)) +
//                                pleft + pright;
//
//                        // Allow the width to outgrow its original estimate if height is fixed.
//                        if (!resizeHeight && !mAdjustViewBoundsCompat) {
//                            widthSize = resolveAdjustedSize(newWidth, mMaxWidth, widthMeasureSpec);
//                        }
//
//                        if (newWidth <= widthSize) {
//                            widthSize = newWidth;
//                            done = true;
//                        }
//                    }
//
//                    // Try adjusting height to be proportional to width
//                    if (!done && resizeHeight) {
//                        int newHeight = (int)((widthSize - pleft - pright) / desiredAspect) +
//                                ptop + pbottom;
//
//                        // Allow the height to outgrow its original estimate if width is fixed.
//                        if (!resizeWidth && !mAdjustViewBoundsCompat) {
//                            heightSize = resolveAdjustedSize(newHeight, mMaxHeight,
//                                    heightMeasureSpec);
//                        }
//
//                        if (newHeight <= heightSize) {
//                            heightSize = newHeight;
//                        }
//                    }
//                }
//            }
//        } else {
//            /* We are either don't want to preserve the drawables aspect ratio,
//               or we are not allowed to change view dimensions. Just measure in
//               the normal way.
//            */
//            w += pleft + pright;
//            h += ptop + pbottom;
//
//            w = Math.max(w, getSuggestedMinimumWidth());
//            h = Math.max(h, getSuggestedMinimumHeight());
//
//            widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
//            heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
//        }
//
//        setMeasuredDimension(widthSize, heightSize);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec)
    {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mDrawable != null)
            mDrawable.setBounds(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if(mIsShow) {
//            mDrawable.draw(canvas);
//            LogUtil.d(TAG, "draw drawable" + mDrawable + ",width=" + mDrawable.getIntrinsicWidth() + ", height=" + mDrawable.getIntrinsicHeight());
//            LogUtil.d(TAG, "view,width="+getWidth()+", height="+getHeight());
//        }
        //draw something else
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸点相对于屏幕左上角坐标
        x = event.getRawX();
        y = event.getRawY() - TOOL_BAR_HIGH;
        LogUtil.d(TAG, "------X: "+ x +"------Y:" + y);

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触摸点相对与view左上角坐标
                startX = event.getX();
                startY = event.getY();
                LogUtil.e("position","startRawX:"+x+" startRawY:"+y);
                LogUtil.e("position","startX:"+startX+" startY:"+startY);
                break;
            case MotionEvent.ACTION_MOVE:

                doMove();
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                startX = startY = 0;
                break;
        }
        return true;
    }


    private void doMove(){
        if(mPosListener != null)
            mPosListener.onPositionChange((int)(x - startX), (int)(y - startY));
    }

    private void doUp()
    {
        if(mPosListener != null) {
            int newX = (int) (x - startX);
            if (newX + getWidth() / 2 <= mScreenWidth / 2)
                newX = 0;
            else
                newX = mScreenWidth - getWidth();
            mPosListener.onPositionChange(newX, (int)(y-startY));
        }
    }

    public void setOnPositionChangeListener(OnPositionChangeListener listener)
    {
        this.mPosListener = listener;
    }


    public void setEmotion(int emotion)
    {
        if(mEmotion != emotion) {
            mDrawable.setLevel(emotion);
            invalidate();
        }
    }

    public void setEmotionResource(int resId)
    {
        if(mResource != resId ) {
            this.mResource = resId;
            mDrawable = resolveResource();
            invalidate();
        }
    }

    private Drawable resolveResource()
    {
        Resources resources = getResources();
        if(resources == null)
            return null;
        Drawable d = null;

        if (mResource != 0) {
            try {
                //??? how to get drawable from resource id
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    d = resources.getDrawable(mResource, getContext().getTheme());
                else
                    d = resources.getDrawable(mResource);
            } catch (Exception e) {
                // Don't try again.
                mResource = 0;
            }
        }
        return d;
    }

    public void setEmotionDrawable(Drawable drawable)
    {
        if(mDrawable != drawable)
        {
            mDrawable = drawable;
            //...
            invalidate();
        }
    }


    public void setIsShow(boolean isShow)
    {
        if(mIsShow != isShow)
        {
            mIsShow = isShow;
            invalidate();
        }
    }

    public void showMessage(String msg, long duration)
    {

    }

    public boolean getIsShow()
    {
        return mIsShow;
    }

    public int getEmotion()
    {
        return mEmotion;
    }
}
