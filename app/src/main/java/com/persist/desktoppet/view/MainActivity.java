package com.persist.desktoppet.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.presenter.IMainPresenter;
import com.persist.desktoppet.presenter.MainPresenterImpl;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main view implementation
 */
public class MainActivity extends AppCompatActivity implements IMainView{

    private final static String TAG = "MainActivity";
    private IMainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pet");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setSelected(PetApplication.isServiceRunning(getApplicationContext(), Const.DISPLAY_SERVICE_ACTION));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PetApplication.isServiceRunning(getApplicationContext(), Const.DISPLAY_SERVICE_ACTION))
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
        mMainPresenter = new MainPresenterImpl(PetApplication.getPetModel(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.loadConfig();

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
        Intent intent = new Intent(Const.DISPLAY_SERVICE_ACTION);
        intent.setPackage(getPackageName());
        startService(intent);
    }

    @Override
    public void destroyPet() {
        Intent intent = new Intent(Const.DISPLAY_SERVICE_ACTION);
        intent.setPackage(getPackageName());
        stopService(intent);
    }

    @Override
    public void loadConfig() {

    }

    @Override
    public void editConfig() {
        startActivity(new Intent(this, ConfigActivity.class));
    }
}
