package com.persist.desktoppet.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.animation.TranslateWithScaleAnimation;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.presenter.impl.FeedPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IFeedPresenter;
import com.persist.desktoppet.ui.CircleProgressBar;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.view.iview.IFeedView;

/**
 * Created by taozhiheng on 16-5-22.
 */
public class FeedActivity extends BaseActivity implements IFeedView{

    private CircleProgressBar mPower;
    private View mApple;
    private View mCake;
    private View mBanana;

    private IFeedPresenter mPresenter;

    private TranslateWithScaleAnimation mAnimation;
    private boolean mIsPlaying;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(Const.KEY_RECEIVER_FEED_ACTION, Const.RECEIVER_ACTION_DESTROY);
            switch (action)
            {
                case Const.RECEIVER_ACTION_UPDATE:
                    mPower.setProgress(intent.getIntExtra(Const.KEY_RECEIVER_FEED_VALUE, 0));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("喂食");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mPower = (CircleProgressBar) findViewById(R.id.feed_power);

        mApple = findViewById(R.id.feed_apple);
        mCake = findViewById(R.id.feed_cake);
        mBanana = findViewById(R.id.feed_banana);

        mApple.setOnClickListener(onClickListener);
        mCake.setOnClickListener(onClickListener);
        mBanana.setOnClickListener(onClickListener);

        mPresenter = new FeedPresenterImpl(PetApplication.getPetModel(this), this);

        registerReceiver(mReceiver, new IntentFilter(Const.KEY_RECEIVER_FEED));

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mIsPlaying)
                return;
            if(mAnimation == null) {
                mAnimation = new TranslateWithScaleAnimation(v, mPower.getX()+mPower.getWidth()/2, mPower.getY()+mPower.getHeight()/2, 1.5f);
                mAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mIsPlaying = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mIsPlaying = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            else
                mAnimation.setView(v);
            mPresenter.increasePower(5);
            v.startAnimation(mAnimation);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadPower();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void loadPower(int power) {
        mPower.setProgress(power);
    }

    @Override
    public void increasePower(int power) {
        int progress = mPower.getProgress()+power;
        if(progress > mPower.getMaxProgress())
            progress = mPower.getMaxProgress();
        mPower.setProgress(progress);
    }
}
