package com.persist.desktoppet.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.presenter.impl.MainPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IMainPresenter;
import com.persist.desktoppet.ui.HorizontalProgressBarWithNumber;
import com.persist.desktoppet.ui.SlidingLayout;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.iview.IMainView;

import java.util.Locale;

/**
 * Created by taozhiheng on 16-5-13.
 *
 * new launcher activity
 */
public class MainActivity extends BaseActivity implements IMainView{


    private final static String TAG = "DisplayActivity";
    private IMainPresenter mMainPresenter;

    private SlidingLayout mSliding;
    private int mLastSlidingState = SlidingLayout.STATE_IDLE;

    private ImageView mIcon;
    private TextView mName;
    private TextView mAge;

    private HorizontalProgressBarWithNumber mPower;
    private TextView mPhrase;


    private NavigationView mDrawer;

    private View mFeed;
    private View mSwitch;
    private View mFind;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(Const.KEY_RECEIVER_MAIN_ACTION, Const.RECEIVER_ACTION_DESTROY);
            switch (action)
            {
                case Const.RECEIVER_ACTION_DESTROY:
                    mSwitch.setSelected(false);
                    break;
                case Const.RECEIVER_ACTION_UPDATE:
                    mPower.setProgress(intent.getIntExtra(Const.KEY_RECEIVER_MAIN_VALUE, 0));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences(Const.PREF_PET, Context.MODE_PRIVATE);
        if(pref.getBoolean(Const.KEY_IS_FIRST, true)) {
            startActivity(new Intent(this, CreateActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mSliding = (SlidingLayout) findViewById(R.id.display_sliding);
        mDrawer = (NavigationView) findViewById(R.id.display_drawer);

        mFeed = findViewById(R.id.display_feed);
        mSwitch = findViewById(R.id.display_switch);
        mFind = findViewById(R.id.display_find);

        LinearLayout header = (LinearLayout) mDrawer.getHeaderView(0);
        mIcon = (ImageView) header.findViewById(R.id.drawer_image);
        mName = (TextView) header.findViewById(R.id.drawer_name);
        mAge = (TextView) header.findViewById(R.id.drawer_age);
        mPower = (HorizontalProgressBarWithNumber) header.findViewById(R.id.drawer_power);
        mPhrase = (TextView) header.findViewById(R.id.drawer_phrase);

        initViewConfig();

        mMainPresenter = new MainPresenterImpl(PetApplication.getPetModel(this), this);

        registerReceiver(mReceiver, new IntentFilter(Const.KEY_RECEIVER_MAIN));
    }

    private void initViewConfig()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        mPhrase.setSelected(true);

        mDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId())
                {

                    case R.id.drawer_alarm:
                        startActivity(new Intent(MainActivity.this, AlarmActivity.class));
                        break;
                    case R.id.drawer_settings:
                        editConfig();
                        break;
                    case R.id.drawer_jump:
//                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.drawer_help:
                        startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        break;
                }
                return true;
            }
        });

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPet();
            }
        });

        mSwitch.setSelected(PetApplication.isServiceRunning(getApplicationContext(), Const.ACTION_DISPLAY_SERVICE));
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PetApplication.isServiceRunning(getApplicationContext(), Const.ACTION_DISPLAY_SERVICE))
                {
                    LogUtil.d(TAG, "service is running");
                    mMainPresenter.destroyPet();
                    view.setSelected(false);
                }
                else
                {
                    LogUtil.d(TAG, "service is stopped");
                    mMainPresenter.createPet();
                    view.setSelected(true);
                }
            }
        });

        mPower.setMax(PetBean.MAX_POWER);
        mPower.setReachedColorGenerator(new HorizontalProgressBarWithNumber.ColorGenerator() {
            @Override
            public int generateColor(float radio) {
                if(radio <= 0.2f)
                    return Const.COLOR_WEAK;
                else if(radio >= 0.8f)
                    return Const.COLOR_HEALTHY;
                else
                    return Const.COLOR_NORMAL;
            }
        });
        mPower.setTextColorGenerator(new HorizontalProgressBarWithNumber.ColorGenerator() {
            @Override
            public int generateColor(float radio) {
                if(radio <= 0.2f)
                    return Const.COLOR_WEAK;
                else if(radio >= 0.8f)
                    return Const.COLOR_HEALTHY;
                else
                    return Const.COLOR_NORMAL;
            }
        });


        mFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedPet();
            }
        });

        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPet();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume, lastState="+mLastSlidingState+", state="+mSliding.getDrawerState());
        mMainPresenter.loadPet();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mLastSlidingState = mSliding.getDrawerState();
        Log.d(TAG, "onStop, lastState="+mLastSlidingState);
    }

    @Override
    public void onBackPressed() {
        if(mSliding.getDrawerState() != SlidingLayout.STATE_IDLE)
            mSliding.changeDrawerState(SlidingLayout.STATE_IDLE);
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            mSliding.changeDrawerState(SlidingLayout.STATE_IDLE_LEFT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
                Toast.makeText(getBaseContext(), "授权失败,将无法显示桌面宠物", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void createPet() {
        Intent intent = new Intent(Const.ACTION_DISPLAY_SERVICE);
        intent.setPackage(getPackageName());
        intent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_START);
        startService(intent);
    }

    @Override
    public void destroyPet() {
        Intent intent = new Intent(Const.ACTION_DISPLAY_SERVICE);
        intent.setPackage(getPackageName());
        stopService(intent);
    }

    @Override
    public void loadPet(PetBean pet) {
        if(pet.getName() == null)
        {
            startActivity(new Intent(this, CreateActivity.class));
        }
        int index = 0;
        if(pet.getSex())
            index = 1;
        mIcon.setImageResource(Const.PET_ICONS[index]);
        mName.setText(pet.getName());
        mAge.setText(String.format(Locale.CHINA, "%d 天", pet.getAge()));

        mPower.setProgress(pet.getPower());
        mPhrase.setText(pet.getPhrase());
    }


    private void editPet() {
        startActivity(new Intent(this, EditActivity.class));
    }

    private void editConfig() {
        startActivity(new Intent(this, ConfigActivity.class));
    }

    private void feedPet(){
        startActivity(new Intent(this, FeedActivity.class));
    }

    private void findPet() {
        startActivity(new Intent(this, ConnectActivity.class));
    }

}
