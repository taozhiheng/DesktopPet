package com.persist.desktoppet.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.persist.desktoppet.R;


/**
 * Created by taozhiheng on 16-4-16.
 *
 * base activity, add slide in and
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
