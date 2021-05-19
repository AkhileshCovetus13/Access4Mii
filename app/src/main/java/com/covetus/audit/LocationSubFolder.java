package com.covetus.audit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.EditTextSemiBold;
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

import static Modal.MainLocationModal.funGetAllSelectedMainLocation;
import static Modal.SubFolderModal.funDeleteSubFolderLayer;
import static Modal.SubFolderModal.funGetAllSubFolders;
import static Modal.SubFolderModal.funInsertSubFolderLayer;
import static Modal.SubFolderModal.funInsertSubFolders;
import static Modal.SubFolderModal.funUpdateSubFolderCount;

public class LocationSubFolder extends Activity {

    @BindView(R.id.mLayoutAddLocation)
    LinearLayout mLayoutAddLocation;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    @BindView(R.id.mImageStarBack)
    ImageView mImageStarBack;
    DatabaseHelper db;
    ArrayList<SelectedLocation> mAuditList;
    String mAuditId;
    String mUserId;

    private static final boolean DEVELOPER_MODE = true;

    @OnClick(R.id.mImageStarBack)
    public void mImageBack() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_location_divide_sub_folder);


        ButterKnife.bind(this);
        mUserId = PreferenceManager.getFormiiId(LocationSubFolder.this);
        db = new DatabaseHelper(LocationSubFolder.this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mAuditId = bundle.getString("mAuditId");
        }
        SelectedLocation selectedMainLocation = new SelectedLocation();
        selectedMainLocation.setmStrAuditId(mAuditId);
        selectedMainLocation.setmStrUserId(mUserId);
        mAuditList = funGetAllSelectedMainLocation(selectedMainLocation,db);
        /*mAuditList = db.get_all_tb_selected_main_location(mAuditId);*/
        for (int i = 0; i < mAuditList.size(); i++) {
            final SelectedLocation selectedLocation = mAuditList.get(i);
            LayoutInflater mInflater = LayoutInflater.from(LocationSubFolder.this);
            View convertView = mInflater.inflate(R.layout.item_selected_location, null);
            ImageView mImgAddSubFolder = convertView.findViewById(R.id.mImgAddSubFolder);
            final TextViewSemiBold mTxtMainLocationCount = convertView.findViewById(R.id.mTxtMainLocationCount);
            final TextViewSemiBold mTxtMainLocation = convertView.findViewById(R.id.mTxtMainLocation);
            final ImageView mImgExpand = convertView.findViewById(R.id.mImgExpand);
            mTxtMainLocationCount.setText(selectedLocation.getmStrMainLocationCount());
            mTxtMainLocation.setText(selectedLocation.getmStrMainLocationTitle());
            final LinearLayout mLayoutForSubFolder = convertView.findViewById(R.id.mLayoutForSubFolder);


            mImgExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = mLayoutForSubFolder.getChildCount();
                    if(a>0){
                        if (mLayoutForSubFolder.getVisibility() == View.VISIBLE) {
                            mLayoutForSubFolder.setVisibility(View.GONE);
                            mImgExpand.setImageResource(R.mipmap.ic_arrow_green_down);
                        }else {
                            mLayoutForSubFolder.setVisibility(View.VISIBLE);
                            mImgExpand.setImageResource(R.mipmap.ic_arrow_green_up);
                        }
                    }else {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_no_subfolder),LocationSubFolder.this);
                    }

                   /* if (mLayoutForSubFolder.getVisibility() == View.VISIBLE) {
                        mLayoutForSubFolder.setVisibility(View.GONE);
                        mImgExpand.setImageResource(R.mipmap.ic_arrow_green_down);
                    }else {
                        mLayoutForSubFolder.setVisibility(View.VISIBLE);
                        mImgExpand.setImageResource(R.mipmap.ic_arrow_green_up);
                    }*/


                }
            });


            mImgAddSubFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = 0;
                    for (int index = 0; index < mLayoutForSubFolder.getChildCount(); ++index) {
                        View nextChild = mLayoutForSubFolder.getChildAt(index);
                        TextViewSemiBold mTxtCount = nextChild.findViewById(R.id.mTxtCount);
                        a = a + Integer.parseInt(mTxtCount.getText().toString());
                    }
                    int newCount = Integer.parseInt(mTxtMainLocationCount.getText().toString()) - a;
                    if (newCount > 0) {
                        mAddUpdateSubFolder(mImgExpand,mLayoutForSubFolder,mTxtMainLocation, mTxtMainLocationCount, newCount,selectedLocation.getmStrMainLocationLocalId(),selectedLocation.getmStrMainLocationServerId());
                    }
                }
            });
            //ArrayList<MainLocationSubFolder> mAuditList = db.get_all_tb_location_sub_folder(selectedLocation.getmStrMainLocationLocalId());
            MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
            mainLocationSubFolder.setmStrAuditId(selectedLocation.getmStrAuditId());
            mainLocationSubFolder.setmStrUserId(selectedLocation.getmStrUserId());
            mainLocationSubFolder.setmStrMainLocationServerId(selectedLocation.getmStrMainLocationServerId());
            ArrayList<MainLocationSubFolder> mAuditList = funGetAllSubFolders(mainLocationSubFolder,db);
            if(mAuditList.size()>0){
            for(int j = 0;j<mAuditList.size();j++){
            //mLayoutForSubFolder.setVisibility(View.VISIBLE);
            LayoutInflater msubInflater = LayoutInflater.from(LocationSubFolder.this);
            View subConvertView = msubInflater.inflate(R.layout.item_selected_sub_folder, null);
            final TextViewSemiBold mTxtSubGroupFolder = subConvertView.findViewById(R.id.mTxtSubGroupFolder);
            final TextViewSemiBold mTxtCount = subConvertView.findViewById(R.id.mTxtCount);
            final TextViewSemiBold mTxtFolderId = subConvertView.findViewById(R.id.mTxtFolderId);
            ImageView mImgUpdateSubGroup = subConvertView.findViewById(R.id.mImgUpdateSubGroup);
            mTxtSubGroupFolder.setText(mAuditList.get(j).getmStrSubFolderName());
            mTxtCount.setText(mAuditList.get(j).getmStrSubFolderCont());
            mTxtFolderId.setText(mAuditList.get(j).getmStrId());
                mImgUpdateSubGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //update
                        int a = 0;
                        for (int index = 0; index < mLayoutForSubFolder.getChildCount(); ++index) {
                            View nextChild = mLayoutForSubFolder.getChildAt(index);
                            TextViewSemiBold mTxtCount = nextChild.findViewById(R.id.mTxtCount);
                            a = a + Integer.parseInt(mTxtCount.getText().toString());
                        }
                        int newCount = Integer.parseInt(mTxtMainLocationCount.getText().toString()) - a;
                        updateDialog(selectedLocation.getmStrMainLocationLocalId(),mTxtSubGroupFolder,mTxtMainLocation, mTxtCount, Integer.parseInt(mTxtCount.getText().toString()) + newCount,mTxtFolderId.getText().toString(),selectedLocation.getmStrMainLocationServerId());
                    }
                });


                subConvertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("<><><>status "+db.mCheckSubFolderCount(mAuditId,PreferenceManager.getFormiiId(LocationSubFolder.this)));
                        boolean mGoesStatus = db.mCheckSubFolderCount(mAuditId,PreferenceManager.getFormiiId(LocationSubFolder.this));
                        if(mGoesStatus==true){
                            Intent intent = new Intent(LocationSubFolder.this,ActivityLayerList.class);
                            intent.putExtra("mSubFolderId",mTxtFolderId.getText());
                            intent.putExtra("mStrAuditId",mAuditId);
                            startActivity(intent);
                            finish();
                        }else {
                            CommonUtils.mShowAlert(getString(R.string.mtextFile_order_procedd),LocationSubFolder.this);
                        }
                    }
                });
                mLayoutForSubFolder.addView(subConvertView);
            }
            }

            mLayoutAddLocation.addView(convertView);
        }

    }


    public void mAddUpdateSubFolder(final ImageView imageView,final LinearLayout linearLayout,final TextViewSemiBold mTxtMainLocation, final TextViewSemiBold mTxtMainLocationCount, final int Count,final String mLocationId,final String mLocationServerId) {
        final Dialog dialog = new Dialog(LocationSubFolder.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_update_sub_folder);
        final EditTextSemiBold mDiaEditFolderName = dialog.findViewById(R.id.mEditFolderName);
        final TextViewSemiBold mDiaTxtCount = dialog.findViewById(R.id.mTxtCount);
        ImageView mImgCountDown = dialog.findViewById(R.id.mImgCountDown);
        ImageView mImgCountUp = dialog.findViewById(R.id.mImgCountUp);
        TextViewBold mTxtAddButton = dialog.findViewById(R.id.mTxtAddButton);
        TextViewBold mTxtCancelButton = dialog.findViewById(R.id.mTxtCancelButton);
        mDiaTxtCount.setText("0");
        mImgCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mDiaTxtCount.getText().toString());
                if (a > 0) {
                    mDiaTxtCount.setText(a - 1 + "");
                } else {
                }

            }
        });
        mImgCountUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mDiaTxtCount.getText().toString());
                if (a < Count) {
                    mDiaTxtCount.setText(a + 1 + "");
                }
            }
        });

        mTxtCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mTxtAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDiaTxtCount.getText().equals("0") || mDiaEditFolderName.getText().toString().length()<=0) {
                CommonUtils.mShowAlert(getString(R.string.mTextFile_alert_folder_name),LocationSubFolder.this);
                } else {
                    imageView.setImageResource(R.mipmap.ic_arrow_green_up);
                    linearLayout.setVisibility(View.VISIBLE);
                    LayoutInflater mInflater = LayoutInflater.from(LocationSubFolder.this);
                    View convertView = mInflater.inflate(R.layout.item_selected_sub_folder, null);
                    final TextViewSemiBold mTxtSubGroupFolder = convertView.findViewById(R.id.mTxtSubGroupFolder);
                    final TextViewSemiBold mTxtCount = convertView.findViewById(R.id.mTxtCount);
                    final TextViewSemiBold mTxtFolderId = convertView.findViewById(R.id.mTxtFolderId);
                    ImageView mImgUpdateSubGroup = convertView.findViewById(R.id.mImgUpdateSubGroup);
                    mTxtSubGroupFolder.setText(mDiaEditFolderName.getText());
                    mTxtCount.setText(mDiaTxtCount.getText());
                    mImgUpdateSubGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //update
                            int a = 0;
                            for (int index = 0; index < linearLayout.getChildCount(); ++index) {
                                View nextChild = linearLayout.getChildAt(index);
                                TextViewSemiBold mTxtCount = nextChild.findViewById(R.id.mTxtCount);
                                a = a + Integer.parseInt(mTxtCount.getText().toString());
                            }
                            int newCount = Integer.parseInt(mTxtMainLocationCount.getText().toString()) - a;
                            updateDialog(mLocationId,mTxtSubGroupFolder,mTxtMainLocation,mTxtCount, Integer.parseInt(mTxtCount.getText().toString()) + newCount,mTxtFolderId.getText().toString(),mLocationServerId);
                        }
                    });
                    //insert
                    MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
                    mainLocationSubFolder.setmStrAuditId(mAuditId);
                    mainLocationSubFolder.setmStrUserId(PreferenceManager.getFormiiId(LocationSubFolder.this));
                    mainLocationSubFolder.setmStrMainLocationId(mLocationId);
                    mainLocationSubFolder.setmStrMainLocationServerId(mLocationId);
                    mainLocationSubFolder.setmStrSubFolderName(mDiaEditFolderName.getText().toString());
                    mainLocationSubFolder.setmStrSubFolderCont(mDiaTxtCount.getText().toString());
                    mainLocationSubFolder.setmStrMainLocationServerId(mLocationServerId);
                    int Id = funInsertSubFolders(mainLocationSubFolder,db);
                    //int Id = db.insert_tb_location_sub_folder(mainLocationSubFolder);
                    mTxtFolderId.setText(Id+"");

                    for (int j=0;j<Integer.parseInt(mDiaTxtCount.getText().toString());j++){
                        LayerList layerList = new LayerList();
                        layerList.setmStrUserId(PreferenceManager.getFormiiId(LocationSubFolder.this));
                        layerList.setmStrAuditId(mAuditId);
                        layerList.setmStrLayerDesc(mTxtMainLocation.getText().toString()+" Name");
                        layerList.setmStrLayerTitle(mTxtMainLocation.getText().toString());
                        layerList.setmStrMainLocationId(mLocationId);
                        layerList.setmStrMainLocationServerId(mLocationServerId);
                        System.out.println("#####locsubStrMainLocationServerId " + mLocationServerId);
                        layerList.setmStrMainLocationTitle(mTxtMainLocation.getText().toString());
                        layerList.setmStrSubFolderTitle(mDiaEditFolderName.getText().toString());
                        layerList.setmStrSubFolderId(mTxtFolderId.getText().toString());
                        layerList.setmStrLayerArchive("0");
                        funInsertSubFolderLayer(layerList,db);

                        //db.insert_tb_sub_folder_explation_list(layerList);
                    }

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean mGoesStatus = db.mCheckSubFolderCount(mAuditId,PreferenceManager.getFormiiId(LocationSubFolder.this));
                            if(mGoesStatus==true){
                                Intent intent = new Intent(LocationSubFolder.this,ActivityLayerList.class);
                                intent.putExtra("mSubFolderId",mTxtFolderId.getText());
                                intent.putExtra("mStrAuditId",mAuditId);
                                startActivity(intent);
                                finish();
                            }else {
                                CommonUtils.mShowAlert(getString(R.string.mtextFile_order_procedd),LocationSubFolder.this);
                            }
                        }
                    });
                    linearLayout.addView(convertView);
                    dialog.dismiss();
                }


            }
        });
        dialog.show();
    }

    void updateDialog(final String mLocationId,final TextViewSemiBold mDiaTxtSubGroupFolder,final TextViewSemiBold mTxtMainLocation, final TextViewSemiBold mDiaTxtCount, final int Count,final String mIdForUpdate,final String mMainLocationServerId) {
        final Dialog dialog = new Dialog(LocationSubFolder.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_update_sub_folder);
        final EditTextSemiBold mEditFolderName = dialog.findViewById(R.id.mEditFolderName);
        final TextViewSemiBold mTxtCount = dialog.findViewById(R.id.mTxtCount);
        ImageView mImgCountDown = dialog.findViewById(R.id.mImgCountDown);
        ImageView mImgCountUp = dialog.findViewById(R.id.mImgCountUp);
        TextViewBold mTxtAddButton = dialog.findViewById(R.id.mTxtAddButton);
        TextViewBold mTxtCancelButton = dialog.findViewById(R.id.mTxtCancelButton);
        mTxtCount.setText(mDiaTxtCount.getText());
        mEditFolderName.setText(mDiaTxtSubGroupFolder.getText());

        mImgCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mTxtCount.getText().toString());
                if (a > 0) {
                    mTxtCount.setText(a - 1 + "");
                }

            }
        });

        mImgCountUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mTxtCount.getText().toString());
                if (a < Count) {
                    mTxtCount.setText(a + 1 + "");
                }
            }
        });

        mTxtCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mTxtAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtCount.getText().equals("0") || mEditFolderName.getText().toString().length()<=0) {
                    CommonUtils.mShowAlert(getString(R.string.mTextFile_alert_folder_name),LocationSubFolder.this);
                } else {
                    mDiaTxtSubGroupFolder.setText(mEditFolderName.getText());
                    mDiaTxtCount.setText(mTxtCount.getText());
                    //update
                    MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
                    mainLocationSubFolder.setmStrId(mIdForUpdate);
                    mainLocationSubFolder.setmStrSubFolderCont(mTxtCount.getText().toString());
                    mainLocationSubFolder.setmStrSubFolderName(mEditFolderName.getText().toString());
                    funUpdateSubFolderCount(mIdForUpdate,"3",mainLocationSubFolder,db);
                    //db.update_tb_location_sub_folder(mainLocationSubFolder);
                    db.update_question_answer_table_detail(mAuditId,PreferenceManager.getFormiiId(LocationSubFolder.this),mIdForUpdate,"1",mEditFolderName.getText().toString(),"");
                    funDeleteSubFolderLayer(mIdForUpdate,mAuditId,"1",db);
                    //db.delete_tb_sub_folder_explation_list(mIdForUpdate);
                    for (int j=0;j<Integer.parseInt(mTxtCount.getText().toString());j++){
                        LayerList layerList = new LayerList();
                        layerList.setmStrUserId(PreferenceManager.getFormiiId(LocationSubFolder.this));
                        layerList.setmStrAuditId(mAuditId);
                        layerList.setmStrLayerDesc(mTxtMainLocation.getText().toString()+" Name");
                        layerList.setmStrLayerTitle(mTxtMainLocation.getText().toString());
                        layerList.setmStrMainLocationId(mLocationId);
                        layerList.setmStrMainLocationServerId(mMainLocationServerId);
                        System.out.println("#####adddlocsubStrMainLocationServerId " + mMainLocationServerId);
                        layerList.setmStrMainLocationTitle(mTxtMainLocation.getText().toString());
                        layerList.setmStrSubFolderTitle(mEditFolderName.getText().toString());
                        layerList.setmStrSubFolderId(mIdForUpdate);
                        layerList.setmStrLayerArchive("0");
                        funInsertSubFolderLayer(layerList,db);
                        //db.insert_tb_sub_folder_explation_list(layerList);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }
}