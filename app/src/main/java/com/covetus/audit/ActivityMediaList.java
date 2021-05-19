package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.MainLocationSubFolder;
import ABS_GET_SET.SelectedLocation;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.FlowLayout;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static Modal.MainLocationModal.funGetAllSelectedMainLocation;
import static Modal.SubFolderModal.funGetAllSubFolderLayer;
import static Modal.SubFolderModal.funGetAllSubFolders;
import static Modal.SubLocationLayer.funGetAllSubLocationLayer;
import static Modal.SubLocationLayer.getAllExplanationImage;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;


public class ActivityMediaList extends Activity {


    DatabaseHelper db;
    @BindView(R.id.mMediaFlowView)
    FlowLayout mMediaFlowView;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;

    int mStrStep = 0;

    String mAuditId;
    String mMainLocation;
    String mSubLocation;
    String mUserId;
    String mSubFolderId;
    String mSubFolderLayerId;
    String mSubLocationLayerId;

    @OnClick(R.id.mImageBack)
    void mImageBack() {
        if (mStrStep == 5) {
            mStrStep = 4;
            createviewsByStep();
        } else if (mStrStep == 4) {
            mStrStep = 3;

            createviewsByStep();
        } else if (mStrStep == 3) {
            mStrStep = 2;

            createviewsByStep();
        } else if (mStrStep == 2) {
            mStrStep = 1;

            createviewsByStep();
        } else if (mStrStep == 1) {
            mStrStep = 0;
            createviewsByStep();
        } else if (mStrStep == 0) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_media_list);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        mUserId = PreferenceManager.getFormiiId(ActivityMediaList.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuditId = bundle.getString("mAuditId");
        }
        createviewsByStep();
    }

    public void createviewsByStep() {
        if (mStrStep == 0) {
            mMediaFlowView.removeAllViews();
            SelectedLocation selectedLocation = new SelectedLocation();
            selectedLocation.setmStrAuditId(mAuditId);
            selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(ActivityMediaList.this));
            ArrayList<SelectedLocation> mListItems = funGetAllSelectedMainLocation(selectedLocation, db);
            for (int i = 0; i < mListItems.size(); i++) {
                customeViews("", mListItems.get(i).getmStrMainLocationTitle(), mListItems.get(i).getmStrMainLocationServerId(), "", "", "", "");
            }
        } else if (mStrStep == 1) {
            mMediaFlowView.removeAllViews();
            MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
            mainLocationSubFolder.setmStrAuditId(mAuditId);
            mainLocationSubFolder.setmStrUserId(mUserId);
            mainLocationSubFolder.setmStrMainLocationServerId(mMainLocation);
            ArrayList<MainLocationSubFolder> mAuditList = funGetAllSubFolders(mainLocationSubFolder, db);
            for (int i = 0; i < mAuditList.size(); i++) {
                customeViews("", mAuditList.get(i).getmStrSubFolderName(), "", mAuditList.get(i).getmStrId(), "", "", "");
            }
        } else if (mStrStep == 2) {
            mMediaFlowView.removeAllViews();
            ArrayList<LayerList> mAuditList = funGetAllSubFolderLayer(mSubFolderId, mAuditId, mUserId, "1", db);
            for (int i = 0; i < mAuditList.size(); i++) {
                customeViews(mAuditList.get(i).getmStrLayerImg(), mAuditList.get(i).getmStrLayerTitle(), "", "", mAuditList.get(i).getmStrId(), "", "");
            }
        } else if (mStrStep == 3) {
            mMediaFlowView.removeAllViews();
            ArrayList<SelectedSubLocation> mAuditList = new ArrayList<SelectedSubLocation>();
            SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
            selectedSubLocation.setmStrUserId(mUserId);
            selectedSubLocation.setmStrLayerId(mSubFolderLayerId);
            selectedSubLocation.setmStrAuditId(mAuditId);
            selectedSubLocation.setmStrMainLocationServer(mMainLocation);
            mAuditList = funGetAllSelectedSubLocation(selectedSubLocation, "1", db);
            for (int i = 0; i < mAuditList.size(); i++) {
                customeViews("", mAuditList.get(i).getmStrSubLocationTitle(), "", "", "", mAuditList.get(i).getmStrSubLocationServer(), "");
            }

        } else if (mStrStep == 4) {
            mMediaFlowView.removeAllViews();
            ArrayList<SubLocationExplation> mAuditList = new ArrayList<SubLocationExplation>();
            SubLocationExplation subLocationExplation1 = new SubLocationExplation();
            subLocationExplation1.setmStrAuditId(mAuditId);
            subLocationExplation1.setmStrUserId(mUserId);
            subLocationExplation1.setmStrLayerId(mSubFolderLayerId);
            subLocationExplation1.setmStrSubLocationServer(mSubLocation);
            subLocationExplation1.setmStrMainLocationServer(mMainLocation);
            mAuditList = funGetAllSubLocationLayer(subLocationExplation1, db);
            for (int i = 0; i < mAuditList.size(); i++) {
                customeViews(mAuditList.get(i).getmStrExplanationImage(), mAuditList.get(i).getmStrSubLocationTitle(), "", "", "", mAuditList.get(i).getmStrSubLocationServer(), mAuditList.get(i).getmStrId());
            }
        } else if (mStrStep == 5) {
            mMediaFlowView.removeAllViews();
            ArrayList<String> mAuditList = new ArrayList<String>();
            mAuditList = getAllExplanationImage(mAuditId, mUserId, mSubLocationLayerId, db);
            for (int i = 0; i < mAuditList.size(); i++) {
                customeViews(mAuditList.get(i), "", "", "", "", "", "");
            }
        }
    }


    public void customeViews(final String mStrImage, String mStrTitle, final String mStrMainLocation, final String mStrSubFolderId, final String mStrSubFolderLayerId, final String mStrSubLocation, final String mStrSubLocationLayerId) {
        final View Imghidden = getLayoutInflater().inflate(R.layout.item_media_grid, mMediaFlowView, false);
        ImageView mMediaImage = Imghidden.findViewById(R.id.mMediaImage);
        TextViewSemiBold mTxtFolderName = Imghidden.findViewById(R.id.mTxtFolderName);

        if (mStrStep == 0)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_folder).into(mMediaImage);
        else if (mStrStep == 1)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_folder).into(mMediaImage);
        else if (mStrStep == 2)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_placeholder_audit).error(R.drawable.ic_placeholder_audit).into(mMediaImage);
        else if (mStrStep == 3)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_folder).into(mMediaImage);
        else if (mStrStep == 4)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_placeholder_audit).error(R.drawable.ic_placeholder_audit).into(mMediaImage);
        else if (mStrStep == 5)
            Glide.with(this).load(mStrImage).placeholder(R.drawable.ic_placeholder_audit).error(R.drawable.ic_placeholder_audit).into(mMediaImage);


        mTxtFolderName.setText(mStrTitle);
        mMediaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStrStep == 0) {
                    mStrStep = 1;
                    mMainLocation = mStrMainLocation;
                    createviewsByStep();
                } else if (mStrStep == 1) {
                    mStrStep = 2;
                    mSubFolderId = mStrSubFolderId;
                    createviewsByStep();
                } else if (mStrStep == 2) {
                    mStrStep = 3;
                    mSubFolderLayerId = mStrSubFolderLayerId;
                    createviewsByStep();
                } else if (mStrStep == 3) {
                    mStrStep = 4;
                    mSubLocation = mStrSubLocation;
                    createviewsByStep();
                } else if (mStrStep == 4) {
                    mStrStep = 5;
                    mSubLocationLayerId = mStrSubLocationLayerId;
                    createviewsByStep();
                }else if (mStrStep == 5) {
                    Intent intent = new Intent(ActivityMediaList.this, ActivityPreviewImage.class);
                    intent.putExtra("mMediaFolder", mStrImage);
                    startActivity(intent);
                }
            }
        });

        mMediaFlowView.addView(Imghidden);
    }


    @Override
    public void onBackPressed() {

        if (mStrStep == 5) {
            mStrStep = 4;
            createviewsByStep();
        } else if (mStrStep == 4) {
            mStrStep = 3;
            createviewsByStep();
        } else if (mStrStep == 3) {
            mStrStep = 2;
            createviewsByStep();
        } else if (mStrStep == 2) {
            mStrStep = 1;
            createviewsByStep();
        } else if (mStrStep == 1) {
            mStrStep = 0;
            createviewsByStep();
        } else if (mStrStep == 0) {
            finish();
        }

    }
}