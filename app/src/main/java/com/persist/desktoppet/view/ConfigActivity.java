package com.persist.desktoppet.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.ConfigBean;
import com.persist.desktoppet.presenter.ConfigPresenterImpl;
import com.persist.desktoppet.presenter.IConfigPresenter;


/**
 * Created by taozhiheng on 16-4-7.
 *
 * config view implementation
 */
public class ConfigActivity extends BaseActivity implements IConfigView{

    private TextView mTheme;
    private SwitchCompat mRing;
    private SwitchCompat mVibrate;
    private AlertDialog mDialog;

    private IConfigPresenter mConfigPresenter;
    private int mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        mTheme = (TextView) findViewById(R.id.config_theme);
        mRing = (SwitchCompat) findViewById(R.id.config_ring);
        mVibrate = (SwitchCompat) findViewById(R.id.config_vibrate);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseTheme();
            }
        });

        mRing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfigPresenter.resetRingConfig(isChecked);
            }
        });

        mVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfigPresenter.resetVibrateConfig(isChecked);
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
    public void loadConfig(ConfigBean config) {
        mId = config.getThemeConfig();
        mTheme.setText(getResources().getStringArray(R.array.theme_array)[mId]);
        mRing.setChecked(config.getRingConfig());
        mVibrate.setChecked(config.getVibrateConfig());
    }

    @Override
    public void resetTheme(int theme) {
        mId = theme;
        mTheme.setText(getResources().getStringArray(R.array.theme_array)[theme]);
    }

    @Override
    public void resetRing(boolean ring) {
        //change service config
    }

    @Override
    public void resetVibrate(boolean vibrate) {
        //change service config
    }
}
