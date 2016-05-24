package com.persist.desktoppet.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
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


    private boolean mIsShow = true;
    private int mEmotion;
    private int mResource;
    private Paint mPaint;

    private String mName;
    private String mMessage;

    private float startX;
    private float startY;
    private float x;
    private float y;
    private int TOOL_BAR_HIGH = 0;
    private float mTouchSlop;
    private int mScreenWidth = 0;
    private GestureDetector mDetector;


//    private Drawable mCloseDrawable;
    private Rect mCloseRect;
    private Rect mPetRect;

    private OnCloseListener mCloseListener;
    private OnMoveListener mOnMoveListener;
    private OnClickListener mClickListener;
    private OnDoubleClickListener mDoubleClickListener;
    private OnLongClickListener mLongClickListener;


    private final static String TAG = "PetView";

    public interface OnCloseListener
    {
        void onClose();
    }

    public interface OnMoveListener
    {
        void onMove(int newX, int newY);
    }

    public interface OnDoubleClickListener
    {
        void onDoubleClick();
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
        initPetView();
    }

    private void initPetView()
    {
        mPaint = new Paint();
        mPaint.setTextSize(25);
        mDetector = new GestureDetector(getContext(), new MyGestureListener());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TOOL_BAR_HIGH = ScreenUtil.getStatusBarHeight(getContext());
        DisplayMetrics display = ScreenUtil.getDisplay(getContext());
        mScreenWidth = display.widthPixels;
        mCloseRect = new Rect();
        mPetRect = new Rect();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onDown(MotionEvent e) {
            startX = e.getX();
            startY = e.getY();
            LogUtil.d(TAG, "onDown");
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            doMove();
//            if(mOnMoveListener != null && Math.abs(distanceX) > mTouchSlop && Math.abs(distanceY) > mTouchSlop)
//                mOnMoveListener.onMove((int)-distanceX, (int)-distanceY);
            LogUtil.d(TAG, "onScroll,dx="+distanceX+", dy="+distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            doUp();
            LogUtil.d(TAG, "onSingleTapUp");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int x = (int)e.getX();
            int y = (int)e.getY();
            if(mCloseRect.contains(x, y))
            {
                if(mCloseListener!= null)
                    mCloseListener.onClose();
            }
            else if(mPetRect.contains(x, y)) {
                if (mClickListener != null)
                    mClickListener.onClick(PetView.this);
            }
            LogUtil.d(TAG, "onSingleTapConfirmed");
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(mDoubleClickListener != null)
                mDoubleClickListener.onDoubleClick();
            LogUtil.d(TAG, "onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            LogUtil.d(TAG, "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getMeasuredWidth()/2;
//        int height = getMeasuredHeight()/2;
//        setMeasuredDimension(width, height*3/2);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCloseRect.set(w-h/6, 0, w, h/6);
        mPetRect.set(0, h/6, w, h*5/6);
//        if(mCloseDrawable != null)
//            mCloseDrawable.setBounds(mCloseRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if(mCloseDrawable != null)
//            mCloseDrawable.draw(canvas);
//
//        Paint.FontMetrics metrics = mPaint.getFontMetrics();
//        int baseOffset = (int)(metrics.top+metrics.bottom)/2;
//        if(mName != null)
//            canvas.drawText(mName, 0, getHeight()/12-baseOffset, mPaint);
//        if(mMessage != null)
//            canvas.drawText(mMessage, 0, getHeight()*11/12-baseOffset, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸点相对于屏幕左上角坐标
        x = event.getRawX();
        y = event.getRawY() - TOOL_BAR_HIGH;
//        LogUtil.d(TAG, "------X: "+ x +"------Y:" + y);
        mDetector.onTouchEvent(event);
        switch(event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //触摸点相对与view左上角坐标
//                startX = event.getX();
//                startY = event.getY();
//                LogUtil.e("position","startRawX:"+x+" startRawY:"+y);
//                LogUtil.e("position","startX:"+startX+" startY:"+startY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                doMove();
//                break;
            case MotionEvent.ACTION_UP:
                doUp();
                startX = startY = 0;
                break;
        }
        return true;
    }


    private void doMove(){
        if(mOnMoveListener != null)
            mOnMoveListener.onMove((int)(x - startX), (int)(y - startY));
    }

    private void doUp()
    {
        if(mOnMoveListener != null) {
            int newX = (int) (x - startX);
            if (newX + getWidth() / 2 <= mScreenWidth / 2)
                newX = 0;
            else
                newX = mScreenWidth - getWidth();
            mOnMoveListener.onMove(newX, (int)(y - startY));
        }
    }


    public void setOnMoveListener(OnMoveListener l) {
//        super.setOnDragListener(l);
        mOnMoveListener = l;
    }

    public void setOnCloseListener(OnCloseListener l) {
        this.mCloseListener = l;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(l);
        mClickListener = l;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener l)
    {
        mDoubleClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
//        super.setOnLongClickListener(l);
        mLongClickListener = l;
    }

    public void setEmotion(int emotion)
    {
        if(mEmotion != emotion) {

            invalidate();
        }
    }

    public void setEmotionResource(int resId)
    {
        if(mResource != resId ) {
            this.mResource = resId;

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

    }


    public void setIsShow(boolean isShow)
    {
        if(mIsShow != isShow)
        {
            mIsShow = isShow;
            invalidate();
        }
    }

    public void setName(String name)
    {
        this.mName = name;
        invalidate();
    }

    public void showMessage(String msg, long duration)
    {
        this.mMessage = msg;
    }

    public boolean getIsShow()
    {
        return mIsShow;
    }

    public int getEmotion()
    {
        return mEmotion;
    }

    public String getName()
    {
        return mName;
    }
}
