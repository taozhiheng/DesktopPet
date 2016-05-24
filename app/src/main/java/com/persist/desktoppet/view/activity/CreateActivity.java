package com.persist.desktoppet.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.adapter.TypeAdapter;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.bean.TypeBean;
import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.ui.ReverseLayout;
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

    private ReverseLayout mSex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setTitle(getResources().getString(R.string.title_create));
        mName = (EditText) findViewById(R.id.create_name);
        mPhrase = (EditText) findViewById(R.id.create_phrase);
        mSex = (ReverseLayout) findViewById(R.id.create_sex);

        mSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSex.toggle();
                Toast.makeText(getBaseContext(), "sex:"+mSex.isShowBack(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createOK()
    {
        String name = mName.getText().toString();
        if(name.equals(""))
        {
            Snackbar.make(mPhrase, "名字不可为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        PetBean pet = new PetBean();
        pet.setName(name);
        pet.setPhrase(mPhrase.getText().toString());
        pet.setSex(mSex.isShowBack());
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            pet.setId(bluetoothAdapter.getAddress());
        }
        IPetModel petModel = PetApplication.getPetModel(this);
        petModel.createPet(pet);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.create_ok)
        {
            createOK();
        }
        return super.onOptionsItemSelected(item);
    }
}
