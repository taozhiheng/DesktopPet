package com.persist.desktoppet.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.presenter.impl.EditPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IEditPresenter;
import com.persist.desktoppet.ui.HorizontalProgressBarWithNumber;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.view.iview.IEditView;

import java.util.Locale;

/**
 * Created by taozhiheng on 16-5-22.
 *
 */
public class EditActivity extends BaseActivity implements IEditView{

    private final static String TAG = "EditActivity";

    private EditText mName;
    private TextView mAge;
    private TextView mSex;
    private HorizontalProgressBarWithNumber mPower;
    private EditText mPhrase;

    private PetBean mPet;

    private boolean mEnable = false;
    private boolean mChanged = false;

    private IEditPresenter mPresenter;



    private TextView test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mName = (EditText) findViewById(R.id.pet_name);
        mAge = (TextView) findViewById(R.id.pet_age);
        mSex = (TextView) findViewById(R.id.pet_sex);
        mPower = (HorizontalProgressBarWithNumber) findViewById(R.id.pet_power);
        mPhrase = (EditText) findViewById(R.id.pet_phrase);
        test = (TextView) findViewById(R.id.test);

        initViewConfig();

        mPet = new PetBean();

        mPresenter = new EditPresenterImpl(PetApplication.getPetModel(this), this);
    }

    private void initViewConfig()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("资料");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        test.setSelected(true);
        test.requestFocus();
        setEnable();
    }

    private void setEnable()
    {
        mName.setEnabled(mEnable);
        mPhrase.setEnabled(mEnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadPet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit_edit:
                String name = mName.getText().toString();
                String phrase = mPhrase.getText().toString();
                checkData(name, phrase);
                if(mEnable)
                {
                    if(mChanged) {
                        //save data and finish
                        mPet.setName(name);
                        mPet.setPhrase(phrase);
                        mPresenter.savePet(mPet);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "资料无修改", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //enable edit
                    mEnable = true;
                    setEnable();
                    item.setIcon(R.mipmap.ic_ok);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkData(String name, String phrase)
    {
        mChanged = !(name.equals(mPet.getName()) && phrase.equals(mPet.getPhrase()));
    }

    @Override
    public void loadPet(PetBean petBean) {
        mPet.setName(petBean.getName());
        mPet.setAge(petBean.getAge());
        mPet.setSex(petBean.getSex());
        mPet.setLevel(petBean.getLevel());
        mPet.setPower(petBean.getPower());
        mPet.setPhrase(petBean.getPhrase());

        mName.setText(petBean.getName());
        mAge.setText(String.format(Locale.CHINA, "%d", petBean.getAge()));
        if(petBean.getSex())
            mSex.setText("雌");
        else
            mSex.setText("雄");
        mPower.setProgress(petBean.getPower());
        mPhrase.setText(petBean.getPhrase());
        test.setText(petBean.getPhrase()+"..."+petBean.getPhrase());
        Log.d(TAG, "isFocused:"+test.isFocused());
    }

    @Override
    public void savePet(PetBean petBean) {
        Toast.makeText(getBaseContext(), "编辑完成", Toast.LENGTH_SHORT).show();
        finish();
    }
}
