package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin1 on 16/11/18.
 */

public class ActivityThankYou extends Activity {


    @BindView(R.id.mLayoutUpdate)
    RelativeLayout mLayoutUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        ButterKnife.bind(this);
    }

    /* click to go to home screen */
    @OnClick(R.id.mLayoutUpdate)
    public void mGoHome() {
        Intent intent = new Intent(ActivityThankYou.this,ActivityTabHostMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("mStrCurrentTab","0");
        startActivity(intent);
        finish();
    }
}
