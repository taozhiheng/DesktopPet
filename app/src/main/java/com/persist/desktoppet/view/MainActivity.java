package com.persist.desktoppet.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.presenter.IMainPresenter;
import com.persist.desktoppet.presenter.MainPresenterImpl;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;

import java.util.Locale;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main view implementation
 */
public class MainActivity extends BaseActivity implements IMainView{

    private final static String TAG = "MainActivity";
    private IMainPresenter mMainPresenter;

    private FloatingActionButton fab;
    private TextView mAge;
    private TextView mType;
    private TextView mSex;
    private TextView mLevel;
    private TextView mPhrase;

    private AlertDialog mDialog;
    private EditText mText;
    private boolean mEditName;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fab.setSelected(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mEditName = true;
                ActionBar actionBar = getSupportActionBar();
                if(actionBar != null)
                    showDialog(actionBar.getTitle());
                else
                    showDialog(null);
                return false;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
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

        mAge = (TextView) findViewById(R.id.pet_age);
        mType = (TextView) findViewById(R.id.pet_type);
        mSex = (TextView) findViewById(R.id.pet_sex);
        mLevel = (TextView) findViewById(R.id.pet_level);
        mPhrase = (TextView) findViewById(R.id.pet_phrase);
        mPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditName = false;
                showDialog(mPhrase.getText());
            }
        });

        mMainPresenter = new MainPresenterImpl(PetApplication.getPetModel(this), this);

        registerReceiver(mReceiver, new IntentFilter(Const.KEY_RECEIVER_MAIN));
    }

    private void showDialog(CharSequence content)
    {
        if(mDialog == null)
        {
            mText = new EditText(this);
            mDialog = new AlertDialog.Builder(this)
                    .setView(mText)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mText.getText().toString();
                            if(mEditName)
                                mMainPresenter.editPetName(content);
                            else
                                mMainPresenter.editPetPhrase(content);
                        }
                    })
                    .create();
        }
        if(mEditName)
            mDialog.setTitle("修改宠物名");
        else
            mDialog.setTitle("修改口头禅");
        mText.setText(content);
        mDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.loadPet();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mMainPresenter.savePet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_settings:
                mMainPresenter.editConfig();
                break;
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
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(pet.getName());
        }
        mAge.setText(String.format(Locale.CHINA, "%d 天", pet.getAge()));
        mType.setText(getResources().getStringArray(R.array.type_array)[pet.getType()]);
        if(pet.getSex())
            mSex.setText("雌");
        else
            mSex.setText("雄");
        mLevel.setText(String.format(Locale.CHINA, "%d", pet.getLevel()));
        mPhrase.setText(pet.getPhrase());
    }

    @Override
    public void editPetName(String name) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle(name);
        Intent intent = new Intent(Const.ACTION_DISPLAY_SERVICE);
        intent.setPackage(getPackageName());
        intent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_RENAME);
        intent.putExtra(Const.KEY_NAME, name);
        startService(intent);
    }

    @Override
    public void editPetPhrase(String phrase) {
        mPhrase.setText(phrase);
    }

    @Override
    public void editConfig() {
        startActivity(new Intent(this, ConfigActivity.class));
    }
}
