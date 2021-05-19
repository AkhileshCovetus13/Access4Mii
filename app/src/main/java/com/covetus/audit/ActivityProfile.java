package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.mStrBaseUrlImage;

public class ActivityProfile extends Activity {

    @BindView(R.id.mTxtUserName)
    TextViewBold mTxtUserName;
    @BindView(R.id.mTxtUserEmail)
    TextViewBold mTxtUserEmail;
    @BindView(R.id.mLayoutUpdateDetails)
    LinearLayout mLayoutUpdateDetails;
    @BindView(R.id.mLayoutChatWithAdmin)
    LinearLayout mLayoutChatWithAdmin;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    @BindView(R.id.mImgUserProfile)
    ImageView mImgUserProfile;

    @BindView(R.id.mTxtUser)
    TextViewSemiBold mTxtUser;
    @BindView(R.id.mTxtShareAudit)
    TextViewBold mTxtShareAudit;
    @BindView(R.id.mTxtHowToUse)
    TextViewBold mTxtHowToUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        ButterKnife.bind(this);
        if (PreferenceManager.getFormiiUserRole(ActivityProfile.this).equals("0")) {
            mTxtUser.setText("Auditor");
        } else {
            mTxtUser.setText("Inspector");
        }
    }

    /*update profile image when image is updated*/
    @Override
    protected void onResume() {
        mTxtUserName.setText(PreferenceManager.getFormiiFullName(ActivityProfile.this));
        mTxtUserEmail.setText(PreferenceManager.getFormiiEmail(ActivityProfile.this));
        System.out.println("<><><mImg" + mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(ActivityProfile.this));
        Glide.with(ActivityProfile.this).load(mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(ActivityProfile.this)).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImgUserProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ActivityProfile.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImgUserProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        super.onResume();
    }

    /* click for going to update profile screen */
    @OnClick(R.id.mLayoutUpdateDetails)
    public void mLayoutGoToUpdateProfile() {
        //CommonUtils.OnClick(ActivityProfile.this, mLayoutUpdateDetails);
        Intent intent = new Intent(ActivityProfile.this, ActivityUpdateProfile.class);
        startActivity(intent);
        //finish();
    }

    /* click for going to update profile screen */
    @OnClick(R.id.mLayoutChatWithAdmin)
    public void mLayoutChatWithAdmin() {
        //CommonUtils.OnClick(ActivityProfile.this, mLayoutUpdateDetails);
        Intent intent = new Intent(ActivityProfile.this, ActivityChatWithAdmin.class);
        startActivity(intent);

    }

    /* click for going back */
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        finish();
    }/* click for going back */

    @OnClick(R.id.mTxtShareAudit)
    public void mGoToShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "Let me recommend you this application for managing your audit task. You can download the "+getString(R.string.app_name)+" app for Android version:"+"");
        startActivity(Intent.createChooser(intent, "Choose one"));

    }/* click for going back */

    @OnClick(R.id.mTxtHowToUse)
    public void mGoHowToUse() {

    }
}
