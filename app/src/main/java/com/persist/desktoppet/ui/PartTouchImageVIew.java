package com.persist.desktoppet.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by taozhiheng on 16-5-24.
 */
public class PartTouchImageVIew extends ImageView {

    private Rect mTouchRect;

    public PartTouchImageVIew(Context context) {
        super(context);
    }

    public PartTouchImageVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PartTouchImageVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != oldw || h != oldh)
        {
            if(mTouchRect == null)
                mTouchRect = new Rect();
            mTouchRect.set(w*3/4, h*2/3, w, h);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchRect.contains((int)event.getX(), (int)event.getY()) && super.onTouchEvent(event);
    }
}
