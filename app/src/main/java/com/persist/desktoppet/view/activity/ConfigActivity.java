package com.persist.desktoppet.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.ConfigBean;
import com.persist.desktoppet.presenter.impl.ConfigPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IConfigPresenter;
import com.persist.desktoppet.service.WXListenService;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.view.iview.IConfigView;


/**
 * Created by taozhiheng on 16-4-7.
 *
 * config view implementation
 */
public class ConfigActivity extends BaseActivity implements IConfigView {

//    private TextView mTheme;
    private SwitchCompat mReceive;
    private SwitchCompat mRing;
    private TextView mCare;
    private View mCareContainer;

    private AlertDialog mDialog;


    private IConfigPresenter mConfigPresenter;
    private int mId;

    public final static int REQUEST_CARE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
//        mTheme = (TextView) findViewById(R.id.config_theme);
        mReceive = (SwitchCompat) findViewById(R.id.config_receive);
        mRing = (SwitchCompat) findViewById(R.id.config_ring);
        mCare = (TextView) findViewById(R.id.config_care);
        mCareContainer = findViewById(R.id.config_care_container);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("设置");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        mTheme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choseTheme();
//            }
//        });


        mReceive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfigPresenter.resetReceiveConfig(isChecked);
                if(isChecked)
                    mCareContainer.setVisibility(View.VISIBLE);
                else
                    mCareContainer.setVisibility(View.INVISIBLE);
            }
        });

        mRing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfigPresenter.resetRingConfig(isChecked);
            }
        });

        mCareContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigActivity.this, CareActivity.class);
                intent.putExtra(Const.KEY_CARE, mCare.getText().toString());
                startActivityForResult(intent, REQUEST_CARE);
            }
        });

        mConfigPresenter = new ConfigPresenterImpl(PetApplication.getConfigModel(this), this);
    }

    private void choseTheme()
    {
        if(mDialog == null) {
            mDialog = new AlertDialog.Builder(this)
                    .setTitle("选择主题")
                    .setSingleChoiceItems(getResources().getStringArray(R.array.theme_array), mId,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mConfigPresenter.resetConfigTheme(which);
                                    dialog.dismiss();
                                }
                            })
                    .create();
        }
        mDialog.getListView().setItemChecked(mId, true);
        mDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mConfigPresenter.loadConfig();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CARE)
        {
            mConfigPresenter.resetCareConfig(data.getStringExtra(Const.KEY_CARE));
        }
    }

    @Override
    public void loadConfig(ConfigBean config) {
        mId = config.getThemeConfig();
//        mTheme.setText(getResources().getStringArray(R.array.theme_array)[mId]);
        mReceive.setChecked(config.getReceiveConfig());
        mRing.setChecked(config.getRingConfig());
        mCare.setText(config.getCareConfig());
    }

    @Override
    public void resetTheme(int theme) {
        mId = theme;
//        mTheme.setText(getResources().getStringArray(R.array.theme_array)[theme]);
    }

    @Override
    public void resetReceiveConfig(boolean receive) {
        //change service config
        Intent intent = new Intent(this, WXListenService.class);
        intent.setPackage(getPackageName());
        if(receive) {
            intent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_START);
            startService(intent);
        }
        else
            stopService(intent);
    }

    @Override
    public void resetRingConfig(boolean ring) {
        //change service config
    }

    @Override
    public void resetCareConfig(String care) {
        if(care != null && care.length() > 0) {
            mCare.setText(care);
            Intent intent = new Intent(this, WXListenService.class);
            intent.setPackage(getPackageName());
            intent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_UPDATE);
            intent.putExtra(Const.KEY_CARE, care);
            startService(intent);
        }
    }
}
