package com.covetus.audit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin18 on 19/12/18.
 */

public class ActivityPreviewImage extends Activity {
    @BindView(R.id.mImgPreview)
    ImageView mImgPreview;
    String mQuesImgPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mQuesImgPath = bundle.getString("mMediaFolder");
            System.out.println("<><><mQuesImgPath" + mQuesImgPath);
        }
        Glide.with(ActivityPreviewImage.this).load(mQuesImgPath).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mImgPreview);
    }
}
