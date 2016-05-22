package com.persist.desktoppet.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.adapter.TypeAdapter;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.bean.TypeBean;
import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taozhiheng on 16-4-17.
 *
 * create pet activity
 */
public class CreateActivity extends BaseActivity {

    private EditText mName;
    private EditText mPhrase;
    private RecyclerView mTypeRecycler;
    private RadioGroup mSexGroup;
    private Button mOK;
    private TypeAdapter mAdapter;

    private boolean mSex;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setTitle(getResources().getString(R.string.title_create));

        mName = (EditText) findViewById(R.id.create_name);
        mPhrase = (EditText) findViewById(R.id.create_phrase);
        mTypeRecycler = (RecyclerView) findViewById(R.id.create_type_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mTypeRecycler.setLayoutManager(layoutManager);
        mSexGroup = (RadioGroup) findViewById(R.id.create_sex);
        mSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSex = (checkedId != 0);
            }
        });
        mOK = (Button) findViewById(R.id.create_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOK();
            }
        });
        initAdapter();
    }


    private void initAdapter()
    {
        List<TypeBean> list = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.type_array);
        if(names.length < Const.ICONS.length)
            return;
        int i = 0;
        for(String name : names)
        {
            list.add(new TypeBean(name, Const.ICONS[i]));
            i++;
        }
        mAdapter = new TypeAdapter(list);
        mTypeRecycler.setAdapter(mAdapter);
    }

    private void createOK()
    {
        PetBean pet = new PetBean();
        pet.setName(mName.getText().toString());
        if(pet.getName() == null || pet.getName().equals(""))
        {
            Snackbar.make(mOK, "名字不可为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        pet.setPhrase(mPhrase.getText().toString());
        pet.setType(mAdapter.getSelectedId());
        pet.setSex(mSex);
        IPetModel petModel = PetApplication.getPetModel(this);
        petModel.createPet(pet);
        startActivity(new Intent(this, DisplayActivity.class));
    }


}
