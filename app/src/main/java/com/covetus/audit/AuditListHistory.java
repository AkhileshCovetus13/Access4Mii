package com.covetus.audit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ABS_ADAPTER.AudittListHistoryAd;
import ABS_GET_SET.Audit;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static Modal.AuditListModal.funGetAllAuditInHistory;

public class AuditListHistory extends Activity {

    ArrayList<Audit> mListItems = new ArrayList<>();
    AudittListHistoryAd audittList;
    @BindView(R.id.mListChat)
    ListView mListChat;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    DatabaseHelper db;
    String mUserId;

    /*click for going back*/
    @OnClick(R.id.mImageBack)
    void mClose() {
        closeKeyBoard(AuditListHistory.this);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_list_history);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);




    }







    @Override
    protected void onResume() {
        checkPermission();
        super.onResume();
    }

    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(AuditListHistory.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) AuditListHistory.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AuditListHistory.this);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read & Write file permission is necessary to show pdf file!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)AuditListHistory.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 404);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)AuditListHistory.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 404);
                }
                return false;
            } else {
                mListItems.clear();
                db = new DatabaseHelper(AuditListHistory.this);
                mUserId = PreferenceManager.getFormiiId(AuditListHistory.this);
                mListItems = funGetAllAuditInHistory(mUserId, "1", "", "1", db);
                if (mListItems.size() > 0) {
                    audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                    mListChat.setAdapter(audittList);
                } else {
                    CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found),AuditListHistory.this);
                    audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                    mListChat.setAdapter(audittList);
                }
                //gyhfh

                return true;
            }
        } else {
            mListItems.clear();
            db = new DatabaseHelper(AuditListHistory.this);
            mUserId = PreferenceManager.getFormiiId(AuditListHistory.this);
            mListItems = funGetAllAuditInHistory(mUserId, "1", "", "1", db);
            //mListItems = funGetAllAudit(mUserId, "3", "", "1", db);
            if (mListItems.size() > 0) {
                audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                mListChat.setAdapter(audittList);
            } else {
                CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found),AuditListHistory.this);
                audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                mListChat.setAdapter(audittList);
            }

            //hgyg

            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 404:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //ghghh
                    mListItems.clear();
                    db = new DatabaseHelper(AuditListHistory.this);
                    mUserId = PreferenceManager.getFormiiId(AuditListHistory.this);
                    mListItems = funGetAllAuditInHistory(mUserId, "1", "", "1", db);
                    //mListItems = funGetAllAudit(mUserId, "3", "", "1", db);

                    if (mListItems.size() > 0) {
                        audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                        mListChat.setAdapter(audittList);
                        audittList = new AudittListHistoryAd(AuditListHistory.this, mListItems);
                        mListChat.setAdapter(audittList);
                    } else {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found),AuditListHistory.this);
                    }

                } else {
                    checkPermission();
                    //code for deny
                }
                break;
        }
    }
}