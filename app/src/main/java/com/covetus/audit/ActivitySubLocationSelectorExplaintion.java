package com.covetus.audit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ABS_ADAPTER.SubLocationSelectionExplanationAdapter;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.ExplanationListImage;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static Modal.SubLocationLayer.funDeleteSubLocationLayer;
import static Modal.SubLocationLayer.funGetAllSubLocationLayer;
import static Modal.SubLocationLayer.funInsertImageSubLocationLayer;
import static Modal.SubLocationLayer.funInsertSubLocationLayer;
import static Modal.SubLocationModal.funUpdateSelectedSubLocation;

/**
 * Created by admin1 on 12/12/18.
 */

public class ActivitySubLocationSelectorExplaintion extends Activity {


    public static ArrayList<SubLocationExplation> mListItems = new ArrayList<>();
    public static ListView mListView;
    public static TextViewSemiBold mTxtFolderTitle;
    public static SubLocationSelectionExplanationAdapter subLocationSelectionExplanationAdapter;
    public static DatabaseHelper databaseHelper;
    public static Activity activity;
    @BindView(R.id.mLayoutAddMore)
    RelativeLayout mLayoutAddMore;
    public static String mLayerId;
    public static String mSubLocationId;
    public static String mSubLocationServerId;
    public static String mLayerTitle;
    public static String mLayerDesc;
    public static String mSubLocationTitle;
    public static String mAuditId;
    public static String mUserId;
    public static String mStrMainLocationServer;
    public static String mStrMainLocationLocal;


    @BindView(R.id.mImageMainBack)
    ImageView mImageMainBack;
    @BindView(R.id.mImageStarBack)
    ImageView mImageStarBack;

    @OnClick(R.id.mImageStarBack)
    public void mSingleBack() {
        finish();
    }

    @OnClick(R.id.mImageMainBack)
    public void mMainBack() {
        finish();
        SelectSubFolderLocationActivity.activity.finish();

    }

    @OnClick(R.id.mLayoutAddMore)
    public void mLayoutAddMore() {
        SubLocationExplation subLocationExplation = new SubLocationExplation();
        subLocationExplation.setmStrLayerId(mLayerId);
        subLocationExplation.setmStrLayerTitle(mLayerTitle);
        subLocationExplation.setmStrLayerDesc(mLayerDesc);
        subLocationExplation.setmStrSubLocationTitle(mSubLocationTitle);
        subLocationExplation.setmStrSubLocationServer(mSubLocationServerId);
        subLocationExplation.setmStrExplanationTitle(mSubLocationTitle);
        subLocationExplation.setmStrExplanationDesc(mSubLocationTitle + " Name");
        subLocationExplation.setmStrExplanationStatus("0");
        subLocationExplation.setmStrExplanationWorkPercentage("0");
        subLocationExplation.setmStrAuditId(mAuditId);
        subLocationExplation.setmStrUserId(mUserId);
        subLocationExplation.setmStrMainLocationServer(mStrMainLocationServer);
        subLocationExplation.setmStrMainLocationLocal(mStrMainLocationLocal);
        subLocationExplation.setmStrExplanationArchive("0");
        int currentId = funInsertSubLocationLayer(subLocationExplation,databaseHelper);
        //int currentId = databaseHelper.insert_tb_sub_location_explation_list(subLocationExplation);
        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrSubLocationCount(mListItems.size() + 1 + "");
        selectedSubLocation.setmStrSubLocationServer(mSubLocationServerId);
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrUserId(mUserId);
        funUpdateSelectedSubLocation(selectedSubLocation,"2",databaseHelper);
        //databaseHelper.update_tb_selected_sub_location(selectedSubLocation);
        mListItems.clear();

        SubLocationExplation subLocationExplation1 = new SubLocationExplation();
        subLocationExplation1.setmStrAuditId(mAuditId);
        subLocationExplation1.setmStrUserId(mUserId);
        subLocationExplation1.setmStrLayerId(mLayerId);
        subLocationExplation1.setmStrSubLocationServer(mSubLocationServerId);
        subLocationExplation1.setmStrMainLocationServer(mStrMainLocationServer);
        mListItems = funGetAllSubLocationLayer(subLocationExplation1,databaseHelper);

        //mListItems = databaseHelper.get_all_tb_sub_location_explation_list(mLayerId, mSubLocationId);
        subLocationSelectionExplanationAdapter = new SubLocationSelectionExplanationAdapter(ActivitySubLocationSelectorExplaintion.this, mListItems);
        mListView.setAdapter(subLocationSelectionExplanationAdapter);
        mTxtFolderTitle.setText(mSubLocationTitle + " " + mListItems.size());


        for (int i = 0; i < 8; i++) {
            ExplanationListImage explanationListImage = new ExplanationListImage();
            explanationListImage.setmStrImgDefault("1");
            explanationListImage.setmStrAuditId(mAuditId);
            explanationListImage.setmStrUserId(mUserId);
            explanationListImage.setmStrExplationListId(currentId + "");
            explanationListImage.setmStrLayerId(mLayerId);
            explanationListImage.setmStrMainLocationServer(mStrMainLocationServer);
            explanationListImage.setmStrSubLocationServer(mSubLocationServerId);
            explanationListImage.setmStrImgStatus("1");
            funInsertImageSubLocationLayer(explanationListImage,databaseHelper);
            //databaseHelper.insert_tb_image_sub_location_explation_list(explanationListImage);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublocation_selector_explanatioln);
        ButterKnife.bind(this);
        mUserId = PreferenceManager.getFormiiId(ActivitySubLocationSelectorExplaintion.this);
        mListView = findViewById(R.id.mListView);
        mTxtFolderTitle = findViewById(R.id.mTxtFolderTitle);


    }


    public static void RemoveView(String mStrId) {
        if (mListItems.size() > 1) {

            System.out.println("<><><><>call22");
            SubLocationExplation subLocationExplation = new SubLocationExplation();
            subLocationExplation.setmStrId(mStrId);
            funDeleteSubLocationLayer(subLocationExplation,"1",databaseHelper);


            //databaseHelper.delete_tb_sub_location_explation_list_single_row(mStrId);
            databaseHelper.delete_main_question_by_opration(mAuditId,mUserId,"1",mStrId);
            mListItems.clear();
            subLocationExplation = new SubLocationExplation();
            subLocationExplation.setmStrLayerId(mLayerId);
            subLocationExplation.setmStrSubLocationServer(mSubLocationServerId);
            subLocationExplation.setmStrMainLocationServer(mStrMainLocationServer);
            subLocationExplation.setmStrAuditId(mAuditId);
            subLocationExplation.setmStrUserId(mUserId);
            mListItems = funGetAllSubLocationLayer(subLocationExplation,databaseHelper);

            //mListItems = databaseHelper.get_all_tb_sub_location_explation_list(mLayerId, mSubLocationId);
            mTxtFolderTitle.setText(mSubLocationTitle + " " + mListItems.size());
            subLocationSelectionExplanationAdapter = new SubLocationSelectionExplanationAdapter(activity, mListItems);
            mListView.setAdapter(subLocationSelectionExplanationAdapter);
            SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
            selectedSubLocation.setmStrSubLocationCount(mListItems.size() + "");
            selectedSubLocation.setmStrSubLocationServer(mSubLocationServerId);
            selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
            selectedSubLocation.setmStrAuditId(mAuditId);
            selectedSubLocation.setmStrUserId(mUserId);
            funUpdateSelectedSubLocation(selectedSubLocation,"2",databaseHelper);
            //databaseHelper.update_tb_selected_sub_location(selectedSubLocation);
        } else {
            //alert min 1 sub view hona jaruri he
            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_alert_not_remove), activity);
        }
    }


    public static void updateView(int pos) {
        mListItems.clear();
        SubLocationExplation subLocationExplation1 = new SubLocationExplation();
        subLocationExplation1.setmStrAuditId(mAuditId);
        subLocationExplation1.setmStrUserId(mUserId);
        subLocationExplation1.setmStrLayerId(mLayerId);
        subLocationExplation1.setmStrSubLocationServer(mSubLocationServerId);
        subLocationExplation1.setmStrMainLocationServer(mStrMainLocationServer);
        mListItems = funGetAllSubLocationLayer(subLocationExplation1,databaseHelper);


        //mListItems = databaseHelper.get_all_tb_sub_location_explation_list(mLayerId, mSubLocationId);
        mTxtFolderTitle.setText(mSubLocationTitle + " " + mListItems.size());
        subLocationSelectionExplanationAdapter = new SubLocationSelectionExplanationAdapter(activity, mListItems);
        mListView.setAdapter(subLocationSelectionExplanationAdapter);
        mListView.setSelection(pos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = ActivitySubLocationSelectorExplaintion.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLayerId = bundle.getString("mLayerId");
            mSubLocationId = bundle.getString("mSubLocationId");
            mSubLocationServerId = bundle.getString("mSubLocationServerId");
            mLayerTitle = bundle.getString("mLayerTitle");
            mLayerDesc = bundle.getString("mLayerDesc");
            mSubLocationTitle = bundle.getString("mSubLocationTitle");
            mAuditId = bundle.getString("mAuditId");
            mStrMainLocationServer = bundle.getString("mStrMainLocationServer");
            mStrMainLocationLocal = bundle.getString("mStrMainLocationLocal");
        }
        databaseHelper = new DatabaseHelper(ActivitySubLocationSelectorExplaintion.this);
        mListItems.clear();
        SubLocationExplation subLocationExplation1 = new SubLocationExplation();
        subLocationExplation1.setmStrAuditId(mAuditId);
        subLocationExplation1.setmStrUserId(mUserId);
        subLocationExplation1.setmStrLayerId(mLayerId);
        subLocationExplation1.setmStrSubLocationServer(mSubLocationServerId);
        subLocationExplation1.setmStrMainLocationServer(mStrMainLocationServer);
        mListItems = funGetAllSubLocationLayer(subLocationExplation1,databaseHelper);
        mTxtFolderTitle.setText(mSubLocationTitle + " " + mListItems.size());
        subLocationSelectionExplanationAdapter = new SubLocationSelectionExplanationAdapter(ActivitySubLocationSelectorExplaintion.this, mListItems);
        mListView.setAdapter(subLocationSelectionExplanationAdapter);
    }
}