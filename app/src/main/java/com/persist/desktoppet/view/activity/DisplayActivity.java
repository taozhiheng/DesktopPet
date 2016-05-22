package com.persist.desktoppet.view.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class DisplayActivity extends BaseActivity implements IMainView{


    private final static String TAG = "DisplayActivity";
    private IMainPresenter mMainPresenter;

    private SlidingLayout mSliding;
    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton fab;

    private ImageView mIcon;
    private TextView mName;
    private TextView mAge;

//    private TextView mLevel;
    private HorizontalProgressBarWithNumber mPower;
    private TextView mPhrase;

    private FloatingActionButton mFeed;
    private FloatingActionButton mFind;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fab.setSelected(false);
        }
    };
    private NavigationView mDrawer;


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
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mPower = (HorizontalProgressBarWithNumber) findViewById(R.id.display_power);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mFeed = (FloatingActionButton) findViewById(R.id.fab_feed);
        mFind = (FloatingActionButton) findViewById(R.id.fab_find);

        LinearLayout header = (LinearLayout) mDrawer.getHeaderView(0);
        mIcon = (ImageView) header.findViewById(R.id.drawer_image);
        mName = (TextView) header.findViewById(R.id.drawer_name);
        mAge = (TextView) header.findViewById(R.id.drawer_age);
//        mLevel = (TextView) findViewById(R.id.pet_level);
        mPhrase = (TextView) header.findViewById(R.id.drawer_phrase);

        initViewConfig();

        mMainPresenter = new MainPresenterImpl(PetApplication.getPetModel(this), this);

        registerReceiver(mReceiver, new IntentFilter(Const.KEY_RECEIVER_MAIN));
    }

    private void initViewConfig()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.logo);

        mPhrase.setSelected(true);

        mDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId())
                {

                    case R.id.drawer_alarm:
                        break;
                    case R.id.drawer_settings:
                        editConfig();
                        break;
                    case R.id.drawer_jump:
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");

                        try {
                            PackageInfo info = getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        //com.tencent.mm.ui.LauncherUI
                        if(intent != null)
                            startActivity(intent);
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

        fab.setSelected(PetApplication.isServiceRunning(getApplicationContext(), Const.ACTION_DISPLAY_SERVICE));
        fab.setOnClickListener(new View.OnClickListener() {
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
        mPower.setTextGenerator(new HorizontalProgressBarWithNumber.TextGenerator() {
            @Override
            public String generateText(int progress, int max) {
                return progress+"/"+max;
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
        mMainPresenter.loadPet();
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

//        mType.setText(getResources().getStringArray(R.array.type_array)[pet.getType()]);
//        if(pet.getSex())
//            mSex.setText("雌");
//        else
//            mSex.setText("雄");

//        mLevel.setText(String.format(Locale.CHINA, "%d", pet.getLevel()));
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
