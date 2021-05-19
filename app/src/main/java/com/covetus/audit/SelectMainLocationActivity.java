package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditMainLocation;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static Modal.AuditListModal.funUpdateAudit;
import static Modal.MainLocationModal.funGetAllMainLocation;
import static Modal.MainLocationModal.funGetAllSelectedMainLocation;
import static Modal.MainLocationModal.funGetSelectedMainLocationCount;
import static Modal.MainLocationModal.funInsertSelectedMainLocation;
import static Modal.MainLocationModal.funIsMainLocationSelected;
import static Modal.MainLocationModal.funUpdateSelectedMainLocation;
import static com.covetus.audit.ActivityTabHostMain.mStrCurrentTab;

public class SelectMainLocationActivity extends Activity implements Listener {

    public static RecyclerView rvTop;
    public static RecyclerView rvBottom;
    public static String mStrDelete = "0";
    public static HashMap<String, String> meMap = new HashMap<String, String>();
    public static HashMap<String, String> meMapDesc = new HashMap<String, String>();
    public static HashMap<String, String> meMapServerId = new HashMap<String, String>();
    public static HashMap<String, String> meMapLocalId = new HashMap<String, String>();

    @BindView(R.id.tvEmptyListTop)
    TextView tvEmptyListTop;

    @BindView(R.id.tvEmptyListBottom)
    TextView tvEmptyListBottom;

    @BindView(R.id.mLayoutDelete)
    RelativeLayout mLayoutDelete;

    @BindView(R.id.mLayoutNext)
    RelativeLayout mLayoutNext;

    @BindView(R.id.mImageHome)
    ImageView mImageHome;


    @BindView(R.id.mEditSearchLocation)
    EditTextRegular mEditSearchLocation;

    public static Activity activity;
    public static String mUserId;
    public static TextViewRegular mTxtLocationDesc;
    public static TextViewSemiBold mTextNormal;
    public static TextViewSemiBold mInfoText;
    ArrayList<AuditMainLocation> mListArry = new ArrayList<>();
    public static DatabaseHelper db;
    public static String mAuditId;



    @OnClick(R.id.mImageHome)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.mLayoutNext)
    public void mLayoutGoNext() {
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
        List<String> listSource = adapterSource.getList();
        if (listSource.size() > 0) {
            Intent intent = new Intent(SelectMainLocationActivity.this, LocationSubFolder.class);
            intent.putExtra("mAuditId", mAuditId);
            startActivity(intent);
        } else {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_give_count), SelectMainLocationActivity.this);
        }



   /* DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
    List<String> listSource = adapterSource.getList();
    if(listSource.size()>0){
        for(int i = 0;i<listSource.size();i++){
        if(meMap.get(listSource.get(i)).equals("0") || meMap.get(listSource.get(i)).equals("")){
            CommonUtils.mShowAlert(getString(R.string.mTextFile_give_count)+listSource.get(i)+getString(R.string.mTextFile_location),SelectMainLocationActivity.this);
            return;
        }
        }

        for(int i = 0;i<listSource.size();i++){

            SelectedLocation selectedLocation = new SelectedLocation();
            selectedLocation.setmStrAuditId(mAuditId);
            selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(SelectMainLocationActivity.this));
            selectedLocation.setmStrMainLocationLocalId(meMapLocalId.get(listSource.get(i)));
            selectedLocation.setmStrMainLocationServerId(meMapServerId.get(listSource.get(i)));
            selectedLocation.setmStrMainLocationTitle(listSource.get(i));
            selectedLocation.setmStrMainLocationCount(meMap.get(listSource.get(i)));
            selectedLocation.setmStrMainLocationDesc(meMapDesc.get(listSource.get(i)));
            if(!db.isExistNotification(meMapLocalId.get(listSource.get(i)))){
            //insert
            db.insert_tb_selected_main_location(selectedLocation);
            db.update_tb_list_audit(mAuditId,"1");
            }else {
            //updateSelectMainLocationActivity
            db.update_tb_list_audit(mAuditId,"1");
            db.update_tb_selected_main_location(selectedLocation);
            }
        }*/

        //finish();

    }


    @OnClick(R.id.mLayoutDelete)
    public void mLayoutDelete() {
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
        List<String> listSource = adapterSource.getList();
        if (listSource.size() > 0) {
            if (mStrDelete.equals("0")) {
                mTextNormal.setText(R.string.mtextFile_done);
                mStrDelete = "1";
                adapterSource.notifyDataSetChanged();
            } else {
                mTextNormal.setText(R.string.mtextFile_delete);
                mStrDelete = "0";
                adapterSource.notifyDataSetChanged();
            }
        }

    }

    public static int getCount() {
        DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
        List<String> listSource = adapterSource.getList();
        return listSource.size();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main_location);
        ButterKnife.bind(this);
        mStrCurrentTab = "0";
        activity = SelectMainLocationActivity.this;
        mUserId = PreferenceManager.getFormiiId(SelectMainLocationActivity.this);
        db = new DatabaseHelper(SelectMainLocationActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuditId = bundle.getString("mAuditId");
        }
        rvTop = findViewById(R.id.rvTop);
        rvBottom = findViewById(R.id.rvBottom);
        mTextNormal = findViewById(R.id.mTextNormal);
        mInfoText = findViewById(R.id.mInfoText);
        mTxtLocationDesc = findViewById(R.id.mTxtLocationDesc);


        //mTxtLocationDesc.setMovementMethod(new ScrollingMovementMethod());
        initTopRecyclerView("");
        initBottomRecyclerView();
        tvEmptyListTop.setVisibility(View.GONE);
        tvEmptyListBottom.setVisibility(View.GONE);
        mTextNormal.setText(R.string.mtextFile_delete);
        mStrDelete = "0";


        mEditSearchLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(s.length() == 0){
                    initTopRecyclerView2("");
                }else if(s.length() > 2){
                    initTopRecyclerView2(s.toString());
                }

            }
        });


    }

    private void initTopRecyclerView(String mStrText) {
        mListArry.clear();
        meMap.clear();
        meMapDesc.clear();
        meMapServerId.clear();
        meMapLocalId.clear();
        mListArry = funGetAllMainLocation(mAuditId,mUserId,mStrText,db);
        List<String> topList = new ArrayList<>();
        topList.clear();
        for (int i = 0; i < mListArry.size(); i++) {
            if(funIsMainLocationSelected(mAuditId,mUserId,mListArry.get(i).getmStrLocationServerId(),db)<=0){
                topList.add(mListArry.get(i).getmStrLocationTitle());
                meMap.put(mListArry.get(i).getmStrLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationDesc());
                meMapServerId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationServerId());
                meMapLocalId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrId());
            }
            /*


            if (!db.isExistNotification(mListArry.get(i).getmStrId())) {
                topList.add(mListArry.get(i).getmStrLocationTitle());
                meMap.put(mListArry.get(i).getmStrLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationDesc());
                meMapServerId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationServerId());
                meMapLocalId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrId());
            }*/
        }
        rvBottom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DragListAdapter topListAdapter = new DragListAdapter(SelectMainLocationActivity.this, topList, this, "0", mAuditId);
        topListAdapter.notifyDataSetChanged();
        rvBottom.setAdapter(topListAdapter);
        tvEmptyListTop.setOnDragListener(topListAdapter.getDragInstance());
        rvBottom.setOnDragListener(topListAdapter.getDragInstance());
    }

    private void initTopRecyclerView2(String mStrText) {
        mListArry.clear();
        //meMap.clear();
        //meMapDesc.clear();
        //meMapServerId.clear();
        //meMapLocalId.clear();
        mListArry = funGetAllMainLocation(mAuditId,mUserId,mStrText,db);
        List<String> topList = new ArrayList<>();
        topList.clear();
        for (int i = 0; i < mListArry.size(); i++) {
            if(funIsMainLocationSelected(mAuditId,mUserId,mListArry.get(i).getmStrLocationServerId(),db)<=0){
                topList.add(mListArry.get(i).getmStrLocationTitle());
                meMap.put(mListArry.get(i).getmStrLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationDesc());
                meMapServerId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationServerId());
                meMapLocalId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrId());
            }
            /*


            if (!db.isExistNotification(mListArry.get(i).getmStrId())) {
                topList.add(mListArry.get(i).getmStrLocationTitle());
                meMap.put(mListArry.get(i).getmStrLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationDesc());
                meMapServerId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrLocationServerId());
                meMapLocalId.put(mListArry.get(i).getmStrLocationTitle(), mListArry.get(i).getmStrId());
            }*/
        }
        rvBottom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DragListAdapter topListAdapter = new DragListAdapter(SelectMainLocationActivity.this, topList, this, "0", mAuditId);
        topListAdapter.notifyDataSetChanged();
        rvBottom.setAdapter(topListAdapter);
        tvEmptyListTop.setOnDragListener(topListAdapter.getDragInstance());
        rvBottom.setOnDragListener(topListAdapter.getDragInstance());
    }


    private void initBottomRecyclerView() {
        rvTop.setLayoutManager(new GridLayoutManager(this, 2));
        //rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrAuditId(mAuditId);
        selectedLocation.setmStrUserId(mUserId);
        ArrayList<SelectedLocation> mSelectedLocationList = funGetAllSelectedMainLocation(selectedLocation,db);
        List<String> bottomList = new ArrayList<>();
        for (int i = 0; i < mSelectedLocationList.size(); i++) {
            bottomList.add(mSelectedLocationList.get(i).getmStrMainLocationTitle());
            meMap.put(mSelectedLocationList.get(i).getmStrMainLocationTitle(), mSelectedLocationList.get(i).getmStrMainLocationCount());
            meMapDesc.put(mSelectedLocationList.get(i).getmStrMainLocationTitle(), mSelectedLocationList.get(i).getmStrMainLocationDesc());
            meMapServerId.put(mSelectedLocationList.get(i).getmStrMainLocationTitle(), mSelectedLocationList.get(i).getmStrMainLocationServerId());
            meMapLocalId.put(mSelectedLocationList.get(i).getmStrMainLocationTitle(), mSelectedLocationList.get(i).getmStrMainLocationLocalId());
        }
        DragListAdapter bottomListAdapter = new DragListAdapter(SelectMainLocationActivity.this, bottomList, this, "1", mAuditId);
        rvTop.setAdapter(bottomListAdapter);
        tvEmptyListBottom.setOnDragListener(bottomListAdapter.getDragInstance());
        rvTop.setOnDragListener(bottomListAdapter.getDragInstance());
    }


    public static void getRemove(int position,String str) {
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));

        //String title1 = ((TextView) rvTop.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.text)).getText().toString();
        DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
        String list = adapterSource.getList().get(position);
        List<String> listSource = adapterSource.getList();
        SelectMainLocationActivity.meMap.put(listSource.get(position), "0");
        listSource.remove(position);
        adapterSource.updateList(listSource);
        adapterSource.notifyDataSetChanged();


        //add
        //mStrDelete = "0";
        DragListAdapter adapterTarget = (DragListAdapter) rvBottom.getAdapter();
        List<String> customListTarget = adapterTarget.getList();
        customListTarget.add(str);
        //countList.add("0");
        adapterTarget.updateList(customListTarget);
        adapterTarget.notifyDataSetChanged();

        DragListAdapter adapterSo = (DragListAdapter) rvTop.getAdapter();
        List<String> listSo = adapterSo.getList();
        System.out.println("<><><>" + listSo.size());
        if (listSo.size() == 0) {
            mTextNormal.setText(R.string.mtextFile_delete);
            mStrDelete = "0";
        }


    }

    @Override
    public void setEmptyListTop(boolean visibility) {
        //tvEmptyListTop.setVisibility(visibility ? View.VISIBLE : View.GONE);
        //rvTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyListBottom(boolean visibility) {
        //tvEmptyListBottom.setVisibility(visibility ? View.VISIBLE : View.GONE);
        //rvBottom.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        mTextNormal.setText(R.string.mtextFile_delete);
        mStrDelete = "0";
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        initTopRecyclerView("");
        initBottomRecyclerView();
        tvEmptyListTop.setVisibility(View.GONE);
        tvEmptyListBottom.setVisibility(View.GONE);
        mEditSearchLocation.setText("");
        super.onResume();

    }

    public static void dragNew(String mStrName) {
        System.out.println("<><><><>" + mStrName);
        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrAuditId(mAuditId);
        selectedLocation.setmStrUserId(mUserId);
        selectedLocation.setmStrMainLocationLocalId(meMapLocalId.get(mStrName));
        selectedLocation.setmStrMainLocationServerId(meMapServerId.get(mStrName));
        selectedLocation.setmStrMainLocationTitle(mStrName);
        selectedLocation.setmStrMainLocationCount("1");
        selectedLocation.setmStrMainLocationDesc(meMapDesc.get(mStrName));
        if(funIsMainLocationSelected(mAuditId,mUserId,meMapServerId.get(mStrName),db)<=0){
            funInsertSelectedMainLocation(selectedLocation,db);
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
        }else {
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
            funUpdateSelectedMainLocation(selectedLocation,"3",db);
            /*db.update_tb_selected_main_location(selectedLocation);*/
        }

     /*   if (!db.isExistNotification(meMapLocalId.get(mStrName))) {
            //insert
            db.insert_tb_selected_main_location(selectedLocation);
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
        } else {
            //update
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
            db.update_tb_selected_main_location(selectedLocation);
        }*/

    }

    public static void updateCount(int opration, String mStrName) {
        System.out.println("<><><><>" + mStrName);
        /*int existingCount = db.get_existing_count_main_location(mAuditId, mUserId, );*/
        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrUserId(mUserId);
        selectedLocation.setmStrAuditId(mAuditId);
        selectedLocation.setmStrMainLocationServerId(meMapServerId.get(mStrName));
        int existingCount = funGetSelectedMainLocationCount(selectedLocation,db);
        System.out.println("<><><><>" + existingCount);
        SelectedLocation selectedLocation1 = new SelectedLocation();
        selectedLocation1.setmStrAuditId(mAuditId);
        selectedLocation1.setmStrUserId(mUserId);
        selectedLocation1.setmStrMainLocationLocalId(meMapLocalId.get(mStrName));
        selectedLocation1.setmStrMainLocationServerId(meMapServerId.get(mStrName));
        selectedLocation1.setmStrMainLocationTitle(mStrName);
        if (opration == 1) {
            selectedLocation1.setmStrMainLocationCount(existingCount + 1+ "");
        } else {
            selectedLocation1.setmStrMainLocationCount(existingCount - 1 + "");
        }
        selectedLocation1.setmStrMainLocationDesc(meMapDesc.get(mStrName));
        if(funIsMainLocationSelected(mAuditId,mUserId,meMapServerId.get(mStrName),db)<=0){
            funInsertSelectedMainLocation(selectedLocation1,db);
            /*db.insert_tb_selected_main_location(selectedLocation);*/
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
        }else {

            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
            funUpdateSelectedMainLocation(selectedLocation1,"3",db);
            /*db.update_tb_selected_main_location(selectedLocation);*/
        }



       /* if (!db.isExistNotification(meMapLocalId.get(mStrName))) {
            //insert
            db.insert_tb_selected_main_location(selectedLocation);
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
        } else {
            //update
            Audit audit = new Audit();
            audit.setmStatus("1");
            audit.setmAuditId(mAuditId);
            funUpdateAudit(audit,"1",db);
            db.update_tb_selected_main_location(selectedLocation);
        }*/
        DragListAdapter adapterSource = (DragListAdapter) rvTop.getAdapter();
        adapterSource.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
