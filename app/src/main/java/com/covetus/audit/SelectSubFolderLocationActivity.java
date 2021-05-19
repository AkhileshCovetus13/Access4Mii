package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.AuditSubLocation;
import ABS_GET_SET.ExplanationListImage;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;
import ly.img.android.ui.utilities.PermissionRequest;

import static ABS_HELPER.CommonUtils.FOLDER;
import static Modal.SubFolderModal.funUpdateSubFolderLayer;
import static Modal.SubLocationLayer.funDeleteAllExplanationImage;
import static Modal.SubLocationLayer.funDeleteSubLocationLayer;
import static Modal.SubLocationLayer.funInsertImageSubLocationLayer;
import static Modal.SubLocationLayer.funInsertSubLocationLayer;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;
import static Modal.SubLocationModal.funGetAllSubLocation;
import static Modal.SubLocationModal.funGetExistingSubLocationCount;
import static Modal.SubLocationModal.funInsertSelectedSubLocation;
import static Modal.SubLocationModal.funIsSubLocationSelected;
import static Modal.SubLocationModal.funUpdateSelectedSubLocation;


public class SelectSubFolderLocationActivity extends Activity implements Listener, PermissionRequest.Response {

    public static int CAMERA_PREVIEW_RESULT = 1;
    public static RecyclerView rvTop;
    public static RecyclerView rvBottom;
    public static String mStrDelete = "0";
    public static HashMap<String, String> meMap = new HashMap<String, String>();
    public static HashMap<String, String> meMapDesc = new HashMap<String, String>();
    public static HashMap<String, String> meMapLocalId = new HashMap<String, String>();
    public static HashMap<String, String> meMapServerId = new HashMap<String, String>();

    @BindView(R.id.tvEmptyListTop)
    TextView tvEmptyListTop;
    @BindView(R.id.tvEmptyListBottom)
    TextView tvEmptyListBottom;
    @BindView(R.id.mLayoutDelete)
    RelativeLayout mLayoutDelete;
    @BindView(R.id.mLayoutNext)
    RelativeLayout mLayoutNext;
    @BindView(R.id.mImgAddPhoto)
    LinearLayout mImgAddPhoto;
    @BindView(R.id.mImgEditPhoto)
    LinearLayout mImgEditPhoto;

    @BindView(R.id.mImgSubGroup)
    ImageView mImgSubGroup;
    @BindView(R.id.mTxtLayerTitle)
    TextViewBold mTxtLayerTitle;
    @BindView(R.id.mImageMainBack)
    ImageView mImageMainBack;


    @BindView(R.id.mEditSearchLocation)
    EditTextRegular mEditSearchLocation;

    public static Activity activity;



    public static TextViewRegular mTxtLocationDesc;
    public static TextViewSemiBold mTextNormal;
    public static TextViewSemiBold mInfoText;
    ArrayList<AuditSubLocation> mListArry = new ArrayList<>();
    public static DatabaseHelper db;
    public static String mAuditId;
    public static String mStrMainLocationServer;
    public static String mStrMainLocationLocal;
    public static String mLayerId;
    public static String mUserId;
    public static String mLayerTitle, mLayerImg, mLayerDesc, mOpration;


    @OnClick(R.id.mImageMainBack)
    public void goBack() {
        finish();
    }

    /* click to add image*/
    @OnClick(R.id.mImgAddPhoto)
    public void mImgAddPhoto() {
        new CameraPreviewIntent(this)
                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                .setExportPrefix(getString(R.string.mTextFile_imagename))
                .setEditorIntent(
                        new PhotoEditorIntent(this)
                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                .destroySourceAfterSave(true)
                )
                .startActivityForResult(CAMERA_PREVIEW_RESULT);
    }


    /* click to add image*/
    @OnClick(R.id.mImgEditPhoto)
    public void mImgEditPhoto() {
        if (!TextUtils.isEmpty(mLayerImg)) {
            PhotoEditorIntent mPhotoEdit = new PhotoEditorIntent(SelectSubFolderLocationActivity.this);
            mPhotoEdit.setSourceImagePath(mLayerImg);
            startActivityForResult(mPhotoEdit, CAMERA_PREVIEW_RESULT);
        } else {

        }

    }

    @OnClick(R.id.mLayoutNext)
    public void mLayoutGoNext() {
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        SubLocationDragListAdapter adapterSource = (SubLocationDragListAdapter) rvTop.getAdapter();
        List<String> listSource = adapterSource.getList();
        if (listSource.size() > 0) {
            Intent intent = new Intent(SelectSubFolderLocationActivity.this, ActivityLocationSelector.class);
            intent.putExtra("mStrLayerId", mLayerId);
            intent.putExtra("mStrAuditId", mAuditId);
            intent.putExtra("mStrMainLocationId", mStrMainLocationServer);
            startActivity(intent);
        } else {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_give_count), SelectSubFolderLocationActivity.this);
        }
     /*   Intent intent = new Intent(SelectSubFolderLocationActivity.this, ActivityLocationSelector.class);
        intent.putExtra("mStrLayerId", mLayerId);
        startActivity(intent);*/
    }


    @OnClick(R.id.mLayoutDelete)
    public void mLayoutDelete() {
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        SubLocationDragListAdapter adapterSource = (SubLocationDragListAdapter) rvTop.getAdapter();
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
        SubLocationDragListAdapter adapterSource = (SubLocationDragListAdapter) rvTop.getAdapter();
        List<String> listSource = adapterSource.getList();
        return listSource.size();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_location);
        ButterKnife.bind(this);
        activity = SelectSubFolderLocationActivity.this;
        mUserId = PreferenceManager.getFormiiId(SelectSubFolderLocationActivity.this);
        db = new DatabaseHelper(SelectSubFolderLocationActivity.this);
        Bundle bundle = getIntent().getExtras();
        mStrDelete = "0";
        if (bundle != null) {
            mOpration = bundle.getString("mOpration");
            if(mOpration.equals("1")){
            String mSubLocationId = bundle.getString("mSubLocationId");
            String mSubLocationServerId = bundle.getString("mSubLocationServerId");
            String mLayerId = bundle.getString("mLayerId");
            String mLayerTitle = bundle.getString("mLayerTitle");
            String mLayerDesc = bundle.getString("mLayerDesc");
            String mSubLocationTitle = bundle.getString("mSubLocationTitle");
            String mAuditId = bundle.getString("mAuditId");
            String mStrMainLocationServer = bundle.getString("mStrMainLocationServer");
            String mStrMainLocationLocal = bundle.getString("mStrMainLocationLocal");
            Intent intent2 = new Intent(SelectSubFolderLocationActivity.this, ActivitySubLocationSelectorExplaintion.class);
            intent2.putExtra("mSubLocationId", mSubLocationId);
            intent2.putExtra("mSubLocationServerId", mSubLocationServerId);
            intent2.putExtra("mLayerId", mLayerId);
            intent2.putExtra("mLayerTitle", mLayerTitle);
            intent2.putExtra("mLayerDesc", mLayerDesc);
            intent2.putExtra("mSubLocationTitle", mSubLocationTitle);
            intent2.putExtra("mAuditId", mAuditId);
            intent2.putExtra("mStrMainLocationServer", mStrMainLocationServer);
            intent2.putExtra("mStrMainLocationLocal", mStrMainLocationLocal);
            startActivity(intent2);
            }
            mAuditId = bundle.getString("mAuditId");
            mStrMainLocationServer = bundle.getString("mStrMainLocationServer");
            mStrMainLocationLocal = bundle.getString("mStrMainLocationLocal");
            mLayerId = bundle.getString("mLayerId");
            mLayerTitle = bundle.getString("mLayerTitle");
            mLayerDesc = bundle.getString("mLayerDesc");
            mLayerImg = bundle.getString("mLayerImg");
            System.out.println("<><><><>mLayerDesc" + mLayerDesc);

        }
        rvTop = findViewById(R.id.rvTop);
        rvBottom = findViewById(R.id.rvBottom);
        mTextNormal = findViewById(R.id.mTextNormal);
        mTxtLocationDesc = findViewById(R.id.mTxtLocationDesc);
        mInfoText = findViewById(R.id.mInfoText);
        mTxtLocationDesc.setMovementMethod(new ScrollingMovementMethod());
        mTxtLayerTitle.setText(mLayerTitle);
        Glide.with(SelectSubFolderLocationActivity.this).load(mLayerImg).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mImgSubGroup);
        initTopRecyclerView("");
        initBottomRecyclerView();
        tvEmptyListTop.setVisibility(View.GONE);
        tvEmptyListBottom.setVisibility(View.GONE);




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
                    initTopRecyclerView("");
                }else if(s.length() > 2){
                    initTopRecyclerView(s.toString());
                }

            }
        });




    }

    private void initTopRecyclerView(String mStrSearch) {

        AuditSubLocation auditSubLocation = new AuditSubLocation();
        auditSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        auditSubLocation.setmStrAuditId(mAuditId);
        auditSubLocation.setmStrUserId(PreferenceManager.getFormiiId(SelectSubFolderLocationActivity.this));
        mListArry = funGetAllSubLocation(auditSubLocation,mStrSearch, db);
        System.out.println("<><><>wer "+mListArry.size());
        //mListArry = db.get_all_tb_audit_sub_location(mStrMainLocationServer, mAuditId);
        List<String> topList = new ArrayList<>();
        for (int i = 0; i < mListArry.size(); i++) {

            SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
            selectedSubLocation.setmStrUserId(mUserId);
            selectedSubLocation.setmStrAuditId(mAuditId);
            selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
            selectedSubLocation.setmStrLayerId(mLayerId);
            selectedSubLocation.setmStrSubLocationServer(mListArry.get(i).getmStrSubLocationServerId());
            if (!funIsSubLocationSelected(selectedSubLocation, db)) {
                topList.add(mListArry.get(i).getmStrSubLocationTitle());
                meMap.put(mListArry.get(i).getmStrSubLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrSubLocationDesc());
                meMapLocalId.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrSubLocationServerId());
                meMapServerId.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrSubLocationServerId());
            }

            /*if (!db.isExistSubLocation(mAuditId,mLayerId,mListArry.get(i).getmStrId())) {
                topList.add(mListArry.get(i).getmStrSubLocationTitle());
                meMap.put(mListArry.get(i).getmStrSubLocationTitle(), "0");
                meMapDesc.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrSubLocationDesc());
                meMapLocalId.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrId());
                meMapServerId.put(mListArry.get(i).getmStrSubLocationTitle(), mListArry.get(i).getmStrSubLocationServerId());
            }*/
        }
        rvBottom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SubLocationDragListAdapter topListAdapter = new SubLocationDragListAdapter(SelectSubFolderLocationActivity.this, topList, this, "0", mAuditId);
        rvBottom.setAdapter(topListAdapter);
        tvEmptyListTop.setOnDragListener(topListAdapter.getDragInstance());
        rvBottom.setOnDragListener(topListAdapter.getDragInstance());
    }

    //
    private void initBottomRecyclerView() {
        //rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTop.setLayoutManager(new GridLayoutManager(this, 2));

        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrUserId(mUserId);
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrLayerId(mLayerId);
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        ArrayList<SelectedSubLocation> mAuditList = funGetAllSelectedSubLocation(selectedSubLocation, "1", db);

//        ArrayList<SelectedSubLocation> mAuditList = db.get_all_tb_selected_sub_location(mLayerId);
        List<String> bottomList = new ArrayList<>();
        for (int i = 0; i < mAuditList.size(); i++) {
            bottomList.add(mAuditList.get(i).getmStrSubLocationTitle());
            meMap.put(mAuditList.get(i).getmStrSubLocationTitle(), mAuditList.get(i).getmStrSubLocationCount());
            meMapDesc.put(mAuditList.get(i).getmStrSubLocationTitle(), mAuditList.get(i).getmStrSubLocationDesc());
            meMapLocalId.put(mAuditList.get(i).getmStrSubLocationTitle(), mAuditList.get(i).getmStrSubLocationServer());
        }
        SubLocationDragListAdapter bottomListAdapter = new SubLocationDragListAdapter(SelectSubFolderLocationActivity.this, bottomList, this, "1", mAuditId);
        rvTop.setAdapter(bottomListAdapter);
        tvEmptyListBottom.setOnDragListener(bottomListAdapter.getDragInstance());
        rvTop.setOnDragListener(bottomListAdapter.getDragInstance());
    }


    public static void getRemove(int position) {
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
        String title1 = ((TextView) rvTop.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.text)).getText().toString();
        SubLocationDragListAdapter adapterSource = (SubLocationDragListAdapter) rvTop.getAdapter();
        String list = adapterSource.getList().get(position);
        List<String> listSource = adapterSource.getList();
        SelectSubFolderLocationActivity.meMap.put(listSource.get(position), "0");
        listSource.remove(position);
        adapterSource.updateList(listSource);
        adapterSource.notifyDataSetChanged();


        //add
        //mStrDelete = "0";
        SubLocationDragListAdapter adapterTarget = (SubLocationDragListAdapter) rvBottom.getAdapter();
        List<String> customListTarget = adapterTarget.getList();
        customListTarget.add(title1);
        //countList.add("0");
        adapterTarget.updateList(customListTarget);
        adapterTarget.notifyDataSetChanged();

        SubLocationDragListAdapter adapterSo = (SubLocationDragListAdapter) rvTop.getAdapter();
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


    /* / getting result for image from gallery or camera /*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
            Toast.makeText(this, "Image saved at: " + path, Toast.LENGTH_LONG).show();
            File mMediaFolder = new File(path);
            mLayerImg = path;
            Glide.with(SelectSubFolderLocationActivity.this).load(mMediaFolder).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mImgSubGroup);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            System.out.println("<><><>bimap" + bitmap);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            System.out.println("<><><>iMagepath" + path);
            System.out.println("<><><>iMageString" + imageString);
            System.out.println("<><><>iMageStringId" + mLayerId);
            LayerList layerUpdateList = new LayerList();
            layerUpdateList.setmStrLayerImg(path);
            layerUpdateList.setmStrId(mLayerId);
            funUpdateSubFolderLayer(layerUpdateList, "2", db);
            //db.update_tb_sub_folder_explation_list_add_img(layerUpdateList);
            db.update_question_answer_table_image(mAuditId, PreferenceManager.getFormiiId(SelectSubFolderLocationActivity.this), mLayerId, "1", path);

        }
        // finish();
    }

    /* permmission request*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* permmission grant*/
    @Override
    public void permissionGranted() {

    }

    /* permmission denied*/
    @Override
    public void permissionDenied() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        activity = SelectSubFolderLocationActivity.this;
        mTextNormal.setText(R.string.mtextFile_delete);
        mStrDelete = "0";
        mTxtLocationDesc.setText(null);
        mInfoText.setText(activity.getString(R.string.mText_information_box));
//        mStrDelete = "0";
        initTopRecyclerView("");
        initBottomRecyclerView();
        tvEmptyListTop.setVisibility(View.GONE);
        tvEmptyListBottom.setVisibility(View.GONE);
        mEditSearchLocation.setText("");
        super.onResume();
    }


    public static void dragNew(String mStrName) {
        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        selectedSubLocation.setmStrMainLocationLocal(mStrMainLocationLocal);
        selectedSubLocation.setmStrSubLocationTitle(mStrName);
        selectedSubLocation.setmStrSubLocationCount(meMap.get(mStrName));
        selectedSubLocation.setmStrSubLocationDesc(meMapDesc.get(mStrName));
        selectedSubLocation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
        selectedSubLocation.setmStrLayerId(mLayerId);
        selectedSubLocation.setmStrLayerTitle(mLayerTitle);
        selectedSubLocation.setmStrLayerDesc(mLayerDesc);
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrWorkStatus("0");
        selectedSubLocation.setmStrUserId(mUserId);
        funInsertSelectedSubLocation(selectedSubLocation, db);
        //db.insert_tb_selected_sub_location(selectedSubLocation);

        SubLocationExplation subLocationExplation = new SubLocationExplation();
        subLocationExplation.setmStrLayerId(mLayerId);
        subLocationExplation.setmStrLayerTitle(mLayerTitle);
        subLocationExplation.setmStrLayerDesc(mLayerDesc);
        subLocationExplation.setmStrSubLocationTitle(mStrName);
        subLocationExplation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
        subLocationExplation.setmStrExplanationTitle(mStrName);
        subLocationExplation.setmStrExplanationDesc(mStrName + " Name");
        subLocationExplation.setmStrExplanationStatus("0");
        subLocationExplation.setmStrExplanationWorkPercentage("0");
        subLocationExplation.setmStrAuditId(mAuditId);
        subLocationExplation.setmStrUserId(mUserId);
        subLocationExplation.setmStrMainLocationServer(mStrMainLocationServer);
        subLocationExplation.setmStrMainLocationLocal(mStrMainLocationLocal);
        subLocationExplation.setmStrExplanationArchive("0");
        int currentId = funInsertSubLocationLayer(subLocationExplation, db);
        //int currentId = db.insert_tb_sub_location_explation_list(subLocationExplation);

        //insert_tb_image_sub_location_explation_list
        for (int i = 0; i < 8; i++) {
            ExplanationListImage explanationListImage = new ExplanationListImage();
            explanationListImage.setmStrImgDefault("1");
            explanationListImage.setmStrAuditId(mAuditId);
            explanationListImage.setmStrUserId(mUserId);
            explanationListImage.setmStrExplationListId(currentId + "");
            explanationListImage.setmStrLayerId(mLayerId);
            explanationListImage.setmStrSubLocationServer(meMapLocalId.get(mStrName));
            explanationListImage.setmStrMainLocationServer(mStrMainLocationServer);
            explanationListImage.setmStrImgStatus("1");
            funInsertImageSubLocationLayer(explanationListImage, db);
            //db.insert_tb_image_sub_location_explation_list(explanationListImage);
        }


    }

    public static void updateCount(int opration, final String mStrName) {
        System.out.println("<><><><>" + mStrName);
        updateSubLocation(mStrName, opration);
/*
        if (opration == 0) {
            int count = db.getCountAllGivenAnsByMainAndSubLocation(mAuditId, mUserId, meMapLocalId.get(mStrName), mStrMainLocationServer);
            if (count > 0) {

                final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                TextViewRegular mTxtMsg = (TextViewRegular) dialog.findViewById(R.id.mTxtMsg);
                RelativeLayout mConfirm = (RelativeLayout) dialog.findViewById(R.id.mConfirm);
                RelativeLayout mCancel = (RelativeLayout) dialog.findViewById(R.id.mCancel);
                mTxtMsg.setText(activity.getString(R.string.mtextFile_this) + " " + mLayerTitle + activity.getString(R.string.mtextFile_subfolder_msg));

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                mConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateSubLocation(mStrName, 0);
                        dialog.dismiss();
                    }
                });
                dialog.showPDialog();


            } else {
                updateSubLocation(mStrName, 0);
            }


        } else {
            updateSubLocation(mStrName, 1);
        }*/


    }


    public static void updateSubLocation(String mStrName, int opration) {

        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrUserId(mUserId);
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
        selectedSubLocation.setmStrLayerId(mLayerId);
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        int existingCount = funGetExistingSubLocationCount(selectedSubLocation, db);

        //int existingCount = db.get_existing_sub_location_count(mAuditId,mLayerId, meMapLocalId.get(mStrName));
        selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
        selectedSubLocation.setmStrMainLocationLocal(mStrMainLocationLocal);
        selectedSubLocation.setmStrSubLocationTitle(mStrName);
        if (opration == 1) {
            selectedSubLocation.setmStrSubLocationCount(existingCount + 1 + "");
        } else {
            selectedSubLocation.setmStrSubLocationCount(existingCount - 1 + "");
        }
        selectedSubLocation.setmStrSubLocationDesc(meMapDesc.get(mStrName));
        selectedSubLocation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
        selectedSubLocation.setmStrLayerId(mLayerId);
        selectedSubLocation.setmStrLayerTitle(mLayerTitle);
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrWorkStatus("0");
        selectedSubLocation.setmStrUserId(mUserId);
        funUpdateSelectedSubLocation(selectedSubLocation, "2", db);

        if (opration == 1) {
            SubLocationExplation subLocationExplation = new SubLocationExplation();
            subLocationExplation.setmStrLayerId(mLayerId);
            subLocationExplation.setmStrLayerTitle(mLayerTitle);
            subLocationExplation.setmStrLayerDesc(mLayerDesc);
            subLocationExplation.setmStrSubLocationTitle(mStrName);
            subLocationExplation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
            subLocationExplation.setmStrExplanationTitle(mStrName);
            subLocationExplation.setmStrExplanationDesc(mStrName + " Name");
            subLocationExplation.setmStrExplanationStatus("0");
            subLocationExplation.setmStrExplanationWorkPercentage("0");
            subLocationExplation.setmStrAuditId(mAuditId);
            subLocationExplation.setmStrUserId(mUserId);
            subLocationExplation.setmStrMainLocationServer(mStrMainLocationServer);
            subLocationExplation.setmStrMainLocationLocal(mStrMainLocationLocal);
            subLocationExplation.setmStrExplanationArchive("0");
            int currentId = funInsertSubLocationLayer(subLocationExplation, db);
            //int currentId = db.insert_tb_sub_location_explation_list(subLocationExplation);
            for (int i = 0; i < 8; i++) {
                ExplanationListImage explanationListImage = new ExplanationListImage();
                explanationListImage.setmStrImgDefault("1");
                explanationListImage.setmStrAuditId(mAuditId);
                explanationListImage.setmStrUserId(mUserId);
                explanationListImage.setmStrExplationListId(currentId + "");
                explanationListImage.setmStrLayerId(mLayerId);
                explanationListImage.setmStrMainLocationServer(mStrMainLocationServer);
                explanationListImage.setmStrSubLocationServer(meMapLocalId.get(mStrName));
                explanationListImage.setmStrImgStatus("1");
                funInsertImageSubLocationLayer(explanationListImage, db);
                //db.insert_tb_image_sub_location_explation_list(explanationListImage);
            }
        } else {
            SubLocationExplation subLocationExplation = new SubLocationExplation();
            subLocationExplation.setmStrLayerId(mLayerId);
            subLocationExplation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
            funDeleteSubLocationLayer(subLocationExplation, "2", db);
            //db.delete_tb_sub_location_explation_list(mLayerId, meMapLocalId.get(mStrName));
            funDeleteAllExplanationImage(mLayerId, "2", db);
            db.deleteAllGivenAnsByMainAndSubLocation(mAuditId, mUserId, meMapLocalId.get(mStrName), mStrMainLocationServer);



            //db.delete_tb_image_sub_location_explation_list(mLayerId);
            for (int j = 0; j < Integer.parseInt(meMap.get(mStrName)); j++) {
                subLocationExplation = new SubLocationExplation();
                subLocationExplation.setmStrLayerId(mLayerId);
                subLocationExplation.setmStrLayerTitle(mLayerTitle);
                subLocationExplation.setmStrLayerDesc(mLayerDesc);
                subLocationExplation.setmStrSubLocationTitle(mStrName);
                subLocationExplation.setmStrSubLocationServer(meMapLocalId.get(mStrName));
                subLocationExplation.setmStrExplanationTitle(mStrName);
                subLocationExplation.setmStrExplanationDesc(mStrName + " Name");
                subLocationExplation.setmStrExplanationStatus("0");
                subLocationExplation.setmStrExplanationWorkPercentage("0");
                subLocationExplation.setmStrAuditId(mAuditId);
                subLocationExplation.setmStrUserId(mUserId);
                subLocationExplation.setmStrMainLocationServer(mStrMainLocationServer);
                subLocationExplation.setmStrMainLocationLocal(mStrMainLocationLocal);
                subLocationExplation.setmStrExplanationArchive("0");
                int currentId = funInsertSubLocationLayer(subLocationExplation, db);
                for (int i = 0; i < 8; i++) {
                    ExplanationListImage explanationListImage = new ExplanationListImage();
                    explanationListImage.setmStrImgDefault("1");
                    explanationListImage.setmStrAuditId(mAuditId);
                    explanationListImage.setmStrUserId(mUserId);
                    explanationListImage.setmStrExplationListId(currentId + "");
                    explanationListImage.setmStrLayerId(mLayerId);
                    explanationListImage.setmStrMainLocationServer(mStrMainLocationServer);
                    explanationListImage.setmStrSubLocationServer(meMapLocalId.get(mStrName));
                    explanationListImage.setmStrImgStatus("1");
                    funInsertImageSubLocationLayer(explanationListImage, db);

                }
            }
        }


    }
}

