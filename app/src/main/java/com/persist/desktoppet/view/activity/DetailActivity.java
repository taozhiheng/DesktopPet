package com.persist.desktoppet.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.ui.HorizontalProgressBarWithNumber;
import com.persist.desktoppet.util.Const;

import java.util.Locale;

public class DetailActivity extends BaseActivity {

    private TextView mName;
    private TextView mAge;
    private ImageView mSex;
    private TextView mPhrase;
    private TextView mId;

    private PetBean mPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName = (TextView) findViewById(R.id.pet_name);
        mAge = (TextView) findViewById(R.id.pet_age);
        mSex = (ImageView) findViewById(R.id.pet_sex);
        mPhrase = (TextView) findViewById(R.id.pet_phrase);
        mId = (TextView) findViewById(R.id.pet_id);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("设置");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPet = getIntent().getParcelableExtra(Const.KEY_PET);
        initViewConfig();
    }

    private void initViewConfig()
    {
        mName.setText(mPet.getName());
        mAge.setText(String.format(Locale.CHINA, "%d 天", mPet.getAge()));
        int index = 0;
        if(mPet.getSex())
            index = 1;
        mSex.setImageResource(Const.PET_ICONS[index]);
        mPhrase.setText(mPet.getPhrase());
        mId.setText(mPet.getId());
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

}
