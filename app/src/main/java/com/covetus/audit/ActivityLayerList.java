package com.covetus.audit;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ABS_ADAPTER.LayerListAdapter;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.MainLocationSubFolder;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static Modal.MainLocationModal.funUpdateSelectedMainLocation;
import static Modal.SubFolderModal.funDeleteSubFolderLayer;
import static Modal.SubFolderModal.funGetAllSubFolderLayer;
import static Modal.SubFolderModal.funInsertSubFolderLayer;
import static Modal.SubFolderModal.funUpdateSubFolderCount;

public class ActivityLayerList extends Activity {

    public static ArrayList<LayerList> mListItems = new ArrayList<LayerList>();
    public static LayerListAdapter layerListAdapter;
    public static String mSubFolderId;
    public static String mStrAuditId;
    public static Activity activity;
    public static ListView mListView;
    public static TextViewBold mTxtMainLocationTitle;
    public static TextViewSemiBold mTxtFolderTitle;
    private static final boolean DEVELOPER_MODE = true;


    @BindView(R.id.mLayoutAddMore)
    RelativeLayout mLayoutAddMore;
    @BindView(R.id.mImageStarBack)
    ImageView mImageStarBack;
    DatabaseHelper databaseHelper;


    @OnClick(R.id.mLayoutAddMore)
    public void mAddQuantity() {


        LayerList layerList = new LayerList();

        layerList.setmStrUserId(PreferenceManager.getFormiiId(ActivityLayerList.this));
        layerList.setmStrAuditId(mListItems.get(0).getmStrAuditId());
        layerList.setmStrLayerDesc(mListItems.get(0).getmStrMainLocationTitle() + getString(R.string.mTextFile_concat_name));
        layerList.setmStrLayerTitle(mListItems.get(0).getmStrMainLocationTitle());
        layerList.setmStrMainLocationId(mListItems.get(0).getmStrMainLocationId());
        layerList.setmStrMainLocationServerId(mListItems.get(0).getmStrMainLocationServerId());

        System.out.println("#####layermStrMainLocationServerId " + mListItems.get(0).getmStrMainLocationServerId());

        layerList.setmStrMainLocationTitle(mListItems.get(0).getmStrMainLocationTitle());
        layerList.setmStrSubFolderTitle(mListItems.get(0).getmStrSubFolderTitle());
        layerList.setmStrSubFolderId(mListItems.get(0).getmStrSubFolderId());
        layerList.setmStrLayerArchive("0");
        layerList.setmStrLayerImg(mListItems.get(0).getmStrLayerImg());

        funInsertSubFolderLayer(layerList,databaseHelper);
        //databaseHelper.insert_tb_sub_folder_explation_list(layerList);
        MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
        funUpdateSubFolderCount(mSubFolderId,"1",mainLocationSubFolder,databaseHelper);
        //databaseHelper.update_tb_location_sub_folder_count(mSubFolderId, "add");
        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrAuditId(mListItems.get(0).getmStrAuditId());
        selectedLocation.setmStrUserId(mListItems.get(0).getmStrUserId());
        selectedLocation.setmStrMainLocationServerId(mListItems.get(0).getmStrMainLocationServerId());
        funUpdateSelectedMainLocation(selectedLocation,"1",databaseHelper);

        //databaseHelper.update_tb_selected_main_location_count(mListItems.get(0).getmStrMainLocationId(), "add");
        mListItems.clear();
        //mListItems = databaseHelper.get_all_tb_sub_folder_explation_list(mSubFolderId);
        mListItems = funGetAllSubFolderLayer(mSubFolderId,mStrAuditId,PreferenceManager.getFormiiId(activity),"1",databaseHelper);
        System.out.println("<><><>" + mListItems.size());
        layerListAdapter = new LayerListAdapter(ActivityLayerList.this, mListItems);
        mListView.setAdapter(layerListAdapter);
        mTxtMainLocationTitle.setText(mListItems.get(0).getmStrMainLocationTitle());
        mTxtFolderTitle.setText(mListItems.get(0).getmStrSubFolderTitle() + "(" + mListItems.size() + " " + mListItems.get(0).getmStrMainLocationTitle() + ")");
        mListView.setSelection(mListItems.size() - 1);
    }


    @OnClick(R.id.mImageStarBack)
    public void mImageBack() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_list);
        ButterKnife.bind(this);
        activity = ActivityLayerList.this;
        databaseHelper = new DatabaseHelper(ActivityLayerList.this);
        mListView = findViewById(R.id.mListView);
        mTxtMainLocationTitle = findViewById(R.id.mTxtMainLocationTitle);
        mTxtFolderTitle = findViewById(R.id.mTxtFolderTitle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSubFolderId = bundle.getString("mSubFolderId");
            mStrAuditId = bundle.getString("mStrAuditId");
        }
        mListItems = funGetAllSubFolderLayer(mSubFolderId,mStrAuditId,PreferenceManager.getFormiiId(ActivityLayerList.this),"1",databaseHelper);
        //mListItems = databaseHelper.get_all_tb_sub_folder_explation_list(mSubFolderId);
        if (mListItems.size() > 0) {
            layerListAdapter = new LayerListAdapter(ActivityLayerList.this, mListItems);
            mListView.setAdapter(layerListAdapter);
            mTxtMainLocationTitle.setText(mListItems.get(0).getmStrMainLocationTitle());
            mTxtFolderTitle.setText(mListItems.get(0).getmStrSubFolderTitle() + "(" + mListItems.size() + " " + mListItems.get(0).getmStrMainLocationTitle() + ")");
        }

    }


    public static void RemoveView(String mStrId) {
        if (mListItems.size() > 1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(activity);
            funDeleteSubFolderLayer(mStrId,mListItems.get(0).getmStrAuditId(),"2",databaseHelper);
            //databaseHelper.delete_tb_sub_folder_single_row(mStrId);
            MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
            funUpdateSubFolderCount(mSubFolderId,"2",mainLocationSubFolder,databaseHelper);
            //databaseHelper.update_tb_location_sub_folder_count(mSubFolderId, "min");
            SelectedLocation selectedLocation = new SelectedLocation();
            selectedLocation.setmStrAuditId(mListItems.get(0).getmStrAuditId());
            selectedLocation.setmStrUserId(mListItems.get(0).getmStrUserId());
            selectedLocation.setmStrMainLocationServerId(mListItems.get(0).getmStrMainLocationServerId());
            funUpdateSelectedMainLocation(selectedLocation,"2",databaseHelper);
            //databaseHelper.update_tb_selected_main_location_count(mListItems.get(0).getmStrMainLocationId(), "min");
            databaseHelper.delete_main_question_by_opration(mListItems.get(0).getmStrAuditId(),PreferenceManager.getFormiiId(activity),"3",mStrId);
            mListItems.clear();
            mListItems = funGetAllSubFolderLayer(mSubFolderId,mStrAuditId,PreferenceManager.getFormiiId(activity),"1",databaseHelper);
            //mListItems = databaseHelper.get_all_tb_sub_folder_explation_list(mSubFolderId);
            System.out.println("<><><>" + mListItems.size());
            layerListAdapter = new LayerListAdapter(activity, mListItems);
            mListView.setAdapter(layerListAdapter);
            mTxtMainLocationTitle.setText(mListItems.get(0).getmStrMainLocationTitle());
            mTxtFolderTitle.setText(mListItems.get(0).getmStrSubFolderTitle() + "(" + mListItems.size() + " " + mListItems.get(0).getmStrMainLocationTitle() + ")");
        } else {
            //alert min 1 sub view hona jaruri he
            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_alert_not_remove), activity);
        }
    }


    public static void updateView(int position) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        mListItems.clear();
        mListItems = funGetAllSubFolderLayer(mSubFolderId,mStrAuditId,PreferenceManager.getFormiiId(activity),"1",databaseHelper);
        //mListItems = databaseHelper.get_all_tb_sub_folder_explation_list(mSubFolderId);
        System.out.println("<><><>" + mListItems.size());
        layerListAdapter = new LayerListAdapter(activity, mListItems);
        mListView.setAdapter(layerListAdapter);
        mTxtMainLocationTitle.setText(mListItems.get(0).getmStrMainLocationTitle());
        System.out.println("<><><>title layer-" +mListItems.get(0).getmStrMainLocationTitle());
        mTxtFolderTitle.setText(mListItems.get(0).getmStrSubFolderTitle() + "(" + mListItems.size() + " " + mListItems.get(0).getmStrMainLocationTitle() + ")");
        mListView.setSelection(position-1);

    }

}