package com.covetus.audit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.EditTextSemiBold;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditInspectorQuestion;
import ABS_GET_SET.AuditQuestion;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_GET_SET.ExplanationListImage;
import ABS_GET_SET.Inspector;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_GET_SET.SubQuestionAnswer;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.FlowLayout;
import ABS_HELPER.MySpinner;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;
import ly.img.android.ui.utilities.PermissionRequest;

import static ABS_HELPER.CommonUtils.FOLDER;
import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.hideKeyboard;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static Modal.AuditListModal.funGetAllAudit;
import static Modal.MainLocationModal.funGetMainLocationString;
import static Modal.SubFolderModal.funGetAllSubFolderLayer;
import static Modal.SubLocationLayer.funUpdateImg;
import static Modal.SubLocationLayer.funUpdateSubLocationLayer;
import static Modal.SubLocationLayer.getAllExplanationImageAllContent;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;

public class ActivityQuestionForm extends Activity implements PermissionRequest.Response {


    String mInsAuditId = "";
    String mInsUserId = "";
    String mInsQuestionId = "";
    String mInsAnswerId = "";
    String mInsAnswerValue = "";
    String mInsQuestionType = "";
    String mInsQuestionImage = "";
    String mInsQuestionPriority = "";
    String mInsMainQuestion = "";
    String mInsSubLocationExplanation = "";
    String mInsMainLocation = "";
    String mInsSubLocation = "";


    public static int CAMERA_PREVIEW_RESULT = 1;
    public static int CAMERA_MULTIPLE_RESULT = 3;
    public static int CAMERA_SINGLE_RESULT = 2;
    ArrayList<SelectedSubLocation> mSubLocationList = new ArrayList<SelectedSubLocation>();
    ArrayList<LayerList> mLayerList = new ArrayList<LayerList>();
    ArrayList<Audit> mAuditList = new ArrayList<Audit>();
    HashMap<String, String> mImageMap = new HashMap<String, String>();
    DatabaseHelper db;
    ArrayList<AuditQuestion> mListNormalQuestion = new ArrayList<>();
    ArrayList<AuditQuestion> mListMeasurementQuestion = new ArrayList<>();
    ArrayList<ExplanationListImage> mListImg = new ArrayList<>();

    //String[] mStringArray = new String[]{getResources().getString(R.string.mTextFile_low),getResources().getString(R.string.mTextFile_medium), getResources().getString(R.string.mTextFile_high)};
    String mStringArray[];

    String mLayerTitle, mSubLocationTitle, mAuditId, mMainLocationID, mExplanationID, mLayerID;
    String mStrImagePath = "";

    @BindView(R.id.mTxtTitle)
    TextViewSemiBold mTxtTitle;
    @BindView(R.id.mTxtFolderTitle)
    TextViewSemiBold mTxtFolderTitle;
    @BindView(R.id.mTxtSubmit)
    RelativeLayout mTxtSubmit;
    @BindView(R.id.mImgAddPhoto)
    RelativeLayout mImgAddPhoto;
    @BindView(R.id.mImgDeletePhoto)
    RelativeLayout mImgDeletePhoto;
    @BindView(R.id.mImgSubGroup)
    ImageView mImgSubGroup;
    @BindView(R.id.mImageStarBack)
    ImageView mImageStarBack;
    @BindView(R.id.mImageMainBack)
    ImageView mImageMainBack;
    @BindView(R.id.mLayoutAddQuestion)
    LinearLayout mLayoutAddQuestion;
    @BindView(R.id.mFlowImage)
    FlowLayout mFlowImage;
    @BindView(R.id.mLayoutMeasurementView)
    LinearLayout mLayoutMeasurementView;
    RadioButton radioButton;
    ImageView mImageTake;
    ImageView mMainImgCancel;
    ImageView mMainImgCamera;
    TextView mQuesTextViewImage;
    String mTextQuesId = "0";
    String mStrImgId;
    ImageView mMainImage;
    String mMainImagePath;
    String mLayerDesc, mExplanationTitle, mExplanationDesc;
    String mUserId;
    String mStrSubLocationServerId;
    String mStrSubFolderId, mStrSubFolderTitle, mExplanationImage, mStrLayerImage;
    String mStrCountryId, mStrLanguageId;


    @OnClick(R.id.mImageStarBack)
    public void mSingleBack() {
        finish();

    }

    @OnClick(R.id.mImageMainBack)
    public void mMainBack() {
        ActivitySubLocationSelectorExplaintion.activity.finish();
        SelectSubFolderLocationActivity.activity.finish();
        finish();
    }

    @OnClick(R.id.mTxtSubmit)
    public void mTxtSaveData() {
        closeKeyBoard(ActivityQuestionForm.this);
        int childCount = mLayoutAddQuestion.getChildCount();
        System.out.println("<><><>count " + childCount);
        for (int i = 0; i < childCount; i++) {
            String mStrQuestionType = "";
            String mStrQuesionId = "";
            String mStrQuesionServer = "";
            String mStrAnsOption = "";
            String mStrPriority = "";
            String mStrComment = "";
            String mStrImageText = "";
            String mStrAnswerId = "";

            View element = mLayoutAddQuestion.getChildAt(i);
            if (element instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) element;
                int childCount2 = linearLayout.getChildCount();
                for (int j = 0; j < childCount2; j++) {
                    View element2 = linearLayout.getChildAt(j);
                    if (j == 0) {
                        if (element2 instanceof LinearLayout) {
                            LinearLayout linearLayout4 = (LinearLayout) element2;
                            TextView mQuestionType = linearLayout4.findViewById(R.id.mQuestionType);
                            if (mQuestionType.getText().equals("1")) {
                                TextView mEditComment;
                                TextView mEditAnsOption = linearLayout4.findViewById(R.id.mEditAnsOption);
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mInspectorQuestion = linearLayout4.findViewById(R.id.mInspectorQuestion);
                                if (mInspectorQuestion.getText().equals("1")) {
                                    mEditComment = linearLayout4.findViewById(R.id.mExtraText);
                                } else {
                                    mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                }


                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mEditAnsOption " + mEditAnsOption.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());


                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                if (mEditAnsOption.getText().toString().trim().equals("")) {
                                } else {
                                    mStrAnsOption = mEditAnsOption.getText().toString().trim();
                                }

                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();

                            } else if (mQuestionType.getText().equals("2")) {
                                TextView mEditComment;
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);

                                TextView mInspectorQuestion = linearLayout4.findViewById(R.id.mInspectorQuestion);
                                if (mInspectorQuestion.getText().equals("1")) {
                                    mEditComment = linearLayout4.findViewById(R.id.mExtraText);
                                } else {
                                    mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                }

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();


                            } else if (mQuestionType.getText().equals("3")) {

                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment;
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);

                                TextView mInspectorQuestion = linearLayout4.findViewById(R.id.mInspectorQuestion);
                                if (mInspectorQuestion.getText().equals("1")) {
                                    mEditComment = linearLayout4.findViewById(R.id.mExtraText);
                                } else {
                                    mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                }

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();

                            } else if (mQuestionType.getText().equals("4")) {
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment;
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);

                                TextView mInspectorQuestion = linearLayout4.findViewById(R.id.mInspectorQuestion);
                                if (mInspectorQuestion.getText().equals("1")) {
                                    mEditComment = linearLayout4.findViewById(R.id.mExtraText);
                                } else {
                                    mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                }

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();

                            } else if (mQuestionType.getText().equals("6")) {
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment;
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);


                                TextView mInspectorQuestion = linearLayout4.findViewById(R.id.mInspectorQuestion);
                                if (mInspectorQuestion.getText().equals("1")) {
                                    mEditComment = linearLayout4.findViewById(R.id.mExtraText);
                                } else {
                                    mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                }

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                            }
                        }
                    }

                    AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                    auditQuestionAnswer.setmStrAuditId(mAuditId);
                    auditQuestionAnswer.setmStrUserId(PreferenceManager.getFormiiId(ActivityQuestionForm.this));
                    auditQuestionAnswer.setmStrCountryId(mStrCountryId);
                    auditQuestionAnswer.setmStrLangId(mStrLanguageId);
                    auditQuestionAnswer.setmStrMainLocationServerId(mMainLocationID);
                    auditQuestionAnswer.setmStrSubLocationServerId(mStrSubLocationServerId);
                    auditQuestionAnswer.setmStrSubLocationExplanationId(mExplanationID);
                    auditQuestionAnswer.setmStrSubLocationExplanationTitle(mExplanationTitle);
                    auditQuestionAnswer.setmStrSubLocationExplanationDesc(mExplanationDesc);
                    auditQuestionAnswer.setmStrSubLocationExplanationImage(mExplanationImage);
                    auditQuestionAnswer.setmStrSubFolderLayerId(mLayerID);
                    auditQuestionAnswer.setmStrSubFolderLayerTitle(mLayerTitle);
                    auditQuestionAnswer.setmStrSubFolderLayerDesc(mLayerDesc);
                    auditQuestionAnswer.setmStrSubFolderLayerImage(mStrLayerImage);
                    auditQuestionAnswer.setmStrSubFolderId(mStrSubFolderId);
                    auditQuestionAnswer.setmStrSubFolderTitle(mStrSubFolderTitle);
                    auditQuestionAnswer.setmStrQuestionId(mStrQuesionId);
                    auditQuestionAnswer.setmStrQuestionServerId(mStrQuesionServer);
                    auditQuestionAnswer.setmStrAnswerId(mStrAnswerId);
                    auditQuestionAnswer.setmStrAnswerValue(mStrAnsOption);
                    auditQuestionAnswer.setmStrQuestionType(mStrQuestionType);
                    auditQuestionAnswer.setmStrQuestionImage(mStrImageText);
                    auditQuestionAnswer.setmStrQuestionPriority(mStrPriority);
                    auditQuestionAnswer.setmStrQuestionExtraText(mStrComment);
                    auditQuestionAnswer.setmStrIsQuestionParent("0");
                    auditQuestionAnswer.setmStrStatus("0");
                    auditQuestionAnswer.setmStrIsQuestionNormal("1");


                    if (mStrQuestionType.equals("1")) {
                        if (mStrAnsOption.equals("") || mStrAnsOption.length() <= 0) {
                            db.delete_tb_audit_question_answer_when_update(auditQuestionAnswer);
                        } else {
                            if (db.isNormalQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "1") == true) {

                                db.update_tb_audit_question_answer_normal(auditQuestionAnswer);
                            } else {
                                db.insert_tb_audit_question_answer(auditQuestionAnswer);
                            }
                        }
                    } else {
                        if (mStrAnswerId.equals("") || mStrAnswerId.length() <= 0 || mStrAnswerId.equals("0")) {
                            db.delete_tb_audit_question_answer_when_update(auditQuestionAnswer);
                            //SaveSub(mStrQuesionServer,mStrAnswerId);
                        } else {
                            if (db.isNormalQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "1") == true) {
                                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                                mAuditList = db.getAllSubmitedQuestionAnswerById(mStrQuesionServer, mAuditId, mExplanationID, "1");
                                if (mAuditList.get(0).getmStrAnswerId().equals(mStrAnswerId)) {
                                    ArrayList<SubQuestionAnswer> mAuditListwe = new ArrayList<SubQuestionAnswer>();
                                    mAuditListwe = db.getSubQuestionAnsByQuestionId(mAuditId, mStrQuesionServer, mExplanationID, mStrAnswerId, "1");
                                    if (mAuditListwe.size() > 0) {
                                        System.out.println("####WEWEWE");
                                    } else {
                                        SaveSub2(mStrQuesionServer, mStrAnswerId);
                                        //db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                    }
                                } else {
                                    ArrayList<SubQuestionAnswer> mAudList = new ArrayList<SubQuestionAnswer>();
                                    mAudList = db.getSubQuestionAnsByCondition(mAuditId, mUserId, mStrQuesionServer, mStrAnswerId, mExplanationID);
                                    if (mAudList.size() > 0) {
                                        db.update_tb_audit_question_answer_normal(auditQuestionAnswer);
                                    } else {
                                        SaveSub2(mStrQuesionServer, mStrAnswerId);
                                        db.update_tb_audit_question_answer_normal(auditQuestionAnswer);
                                    }
                                }

                                //1. submited ans match by current ans
                                //2. if ans is match noting is
                                //3. if ans is not match deltetd


                                //SaveSub(mStrQuesionServer,mStrAnswerId);
                                //db.update_tb_audit_question_answer_normal(auditQuestionAnswer);

                            } else {

                                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                                mAuditList = db.getSubQuestionAnsByQuestionId(mAuditId, mStrQuesionServer, mExplanationID, mStrAnswerId, "1");
                                System.out.println("<><>321<><>call " + mAuditList.size());
                                if (mAuditList.size() > 0) {
                                    db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                } else {
                                    // changeeeee
                                    SaveSub2(mStrQuesionServer, mStrAnswerId);
                                    db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                }
                                //1. get sub question ans by question server id
                                //2. qusetion ans and sub question ans match
                                //3. ans same nothing or not match delete
                                //db.insert_tb_audit_question_answer(auditQuestionAnswer);
                            }
                        }
                    }
                }
            }
        }
        CommonUtils.mShowAlert(getString(R.string.mText_data_saved), ActivityQuestionForm.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);


    }

    public void SaveSub(String mStrMainQuestion, String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mQuestionList = new ArrayList<SubQuestionAnswer>();
        mQuestionList = db.sub_question_ans2(mStrMainQuestion, mAuditId, mExplanationID, mStrQuestionFor);
        db.delete_answer_by_main_question2(mAuditId, mStrMainQuestion, mExplanationID, mStrQuestionFor);
        if (mQuestionList.size() > 0) {
            System.out.println("<><>345<><> " + mQuestionList.get(0).getmStrMainQuestionId());
            System.out.println("<><>345<><> " + mQuestionList.get(0).getmStrQuestionFor());
            SaveSub(mQuestionList.get(0).getmStrQuestionServerId(), mQuestionList.get(0).getmStrQuestionFor());
        }
    }

    public void SaveSub2(String mStrMainQuestion, String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mQuestionList = new ArrayList<SubQuestionAnswer>();
        mQuestionList = db.sub_question_ans3(mStrMainQuestion, mAuditId, mExplanationID, mStrQuestionFor);
        db.delete_answer_by_main_question3(mAuditId, mStrMainQuestion, mExplanationID, mStrQuestionFor);
        if (mQuestionList.size() > 0) {
            System.out.println("<><>345<><> " + mQuestionList.get(0).getmStrMainQuestionId());
            System.out.println("<><>345<><> " + mQuestionList.get(0).getmStrQuestionFor());
            SaveSub2(mQuestionList.get(0).getmStrQuestionServerId(), mQuestionList.get(0).getmStrQuestionFor());
        }
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

    @OnClick(R.id.mImgDeletePhoto)
    public void mLayoutDelete() {
        Glide.with(ActivityQuestionForm.this).load(R.drawable.ic_placeholder_audit).into(mImgSubGroup);
        SubLocationExplation subLocationExplation = new SubLocationExplation();
        subLocationExplation.setmStrId(mExplanationID);
        subLocationExplation.setmStrExplanationImage(null);
        subLocationExplation.setmStrAuditId(mAuditId);
        subLocationExplation.setmStrUserId(mUserId);
        subLocationExplation.setmStrMainLocationServer(mMainLocationID);
        funUpdateSubLocationLayer(subLocationExplation, "3", db);
        //db.update_tb_sub_location_explation_list_image(subLocationExplation);
        db.update_question_answer_table_image(mAuditId, PreferenceManager.getFormiiId(ActivityQuestionForm.this), mExplanationID, "2", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);
        ButterKnife.bind(this);
        mStringArray = getResources().getStringArray(R.array.question_priority);
        mUserId = PreferenceManager.getFormiiId(ActivityQuestionForm.this);
        db = new DatabaseHelper(ActivityQuestionForm.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLayerTitle = bundle.getString("mLayerTitle");
            mSubLocationTitle = bundle.getString("mSubLocationTitle");
            mAuditId = bundle.getString("mAuditId");
            mMainLocationID = bundle.getString("mStrMainLocationServer");
            mStrSubLocationServerId = bundle.getString("mSubLocationID");
            mExplanationID = bundle.getString("mExplanationID");
            mLayerID = bundle.getString("mLayerID");
            mLayerDesc = bundle.getString("mLayerDesc");
            mExplanationTitle = bundle.getString("mExplanationTitle");
            mExplanationDesc = bundle.getString("mExplanationDesc");
            mExplanationImage = bundle.getString("mExplanationImage");
        }

        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrAuditId(mAuditId);
        selectedSubLocation.setmStrUserId(PreferenceManager.getFormiiId(ActivityQuestionForm.this));
        selectedSubLocation.setmStrMainLocationServer(mMainLocationID);
        selectedSubLocation.setmStrLayerId(mLayerID);
        mSubLocationList = funGetAllSelectedSubLocation(selectedSubLocation, "1", db);
        mLayerList = funGetAllSubFolderLayer(mLayerID, mAuditId, "", "2", db);
        mAuditList = funGetAllAudit("", "", mAuditId, "2", db);
        mStrSubFolderId = mLayerList.get(0).getmStrSubFolderId();
        mStrSubFolderTitle = mLayerList.get(0).getmStrSubFolderTitle();
        mStrLayerImage = mLayerList.get(0).getmStrLayerImg();
        mStrCountryId = mAuditList.get(0).getmStrCountryId();
        mStrLanguageId = mAuditList.get(0).getmStrLangId();


        System.out.println("#####mLayerTitle " + mLayerTitle);
        System.out.println("#####mSubLocationTitle " + mSubLocationTitle);
        System.out.println("#####mAuditId " + mAuditId);
        System.out.println("#####mStrMainLocationServer " + mMainLocationID);

        System.out.println("#####mExplanationID " + mExplanationID);
        System.out.println("#####mLayerID " + mLayerID);
        System.out.println("#####mLayerDesc " + mLayerDesc);
        System.out.println("#####mExplanationTitle " + mExplanationTitle);
        System.out.println("#####mExplanationDesc " + mExplanationDesc);
        System.out.println("#####mExplanationImage " + mExplanationImage);

        System.out.println("#####mStrSubLocationServerId " + mStrSubLocationServerId);

        System.out.println("#####mStrSubFolderId " + mStrSubFolderId);
        System.out.println("#####mStrSubFolderTitle " + mStrSubFolderTitle);
        System.out.println("#####mStrLayerImage " + mStrLayerImage);
        System.out.println("#####mStrCountryId " + mStrCountryId);
        System.out.println("#####mStrLanguageId " + mStrLanguageId);


        mTxtTitle.setText(funGetMainLocationString(mMainLocationID, db));
        mTxtFolderTitle.setText(mSubLocationTitle);

        Glide.with(ActivityQuestionForm.this).load(mExplanationImage).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mImgSubGroup);
        mImgSubGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityQuestionForm.this, ActivityPreviewImage.class);
                intent.putExtra("mMediaFolder", mExplanationImage);
                startActivity(intent);
            }
        });
        mListNormalQuestion = db.get_all_tb_audit_question(mAuditId, PreferenceManager.getFormiiId(ActivityQuestionForm.this), mMainLocationID, mStrSubLocationServerId, "1");
        if (mListNormalQuestion.size() > 0) {
            mCreateViews();
        }

        mListMeasurementQuestion = db.get_all_tb_audit_question(mAuditId, PreferenceManager.getFormiiId(ActivityQuestionForm.this), mMainLocationID, mStrSubLocationServerId, "0");
        if (mListMeasurementQuestion.size() > 0) {
            mCreateMeasurementViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //create layout for already added image
        mFlowImage.removeAllViews();
        mListImg = getAllExplanationImageAllContent(mExplanationID, db);
        //mListImg = db.get_all_tb_image_sub_location_explation_list(mExplanationID);
        if (mListImg.size() > 0) {
            for (int i = 0; i < mListImg.size(); i++) {
                final View Imghidden = getLayoutInflater().inflate(R.layout.view_multiple_image, mFlowImage, false);
                final ImageView mMultipleImg = Imghidden.findViewById(R.id.mMultipleImg);
                final ImageView mImgDelete = Imghidden.findViewById(R.id.mImgDelete);

                if (mListImg.get(i).getmStrImgDefault().equals("0")) {
                    mImgDelete.setVisibility(View.VISIBLE);
                    Glide.with(ActivityQuestionForm.this).load(mListImg.get(i).getmStrExplationListImage()).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mMultipleImg);
                } else {
                    mImgDelete.setVisibility(View.GONE);
                    Glide.with(ActivityQuestionForm.this).load(R.drawable.ic_placeholder_audit).into(mMultipleImg);
                }

                final int finalI = i;
                mMultipleImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mListImg.get(finalI).getmStrImgDefault().equals("0")) {
                            Intent intent = new Intent(ActivityQuestionForm.this, ActivityPreviewImage.class);
                            intent.putExtra("mMediaFolder", mListImg.get(finalI).getmStrExplationListImage());
                            startActivity(intent);
                        } else {
                            mStrImgId = mListImg.get(finalI).getmStrId();
                            mMainImage = mMultipleImg;
                            mMainImagePath = mListImg.get(finalI).getmStrExplationListImage();
                            new CameraPreviewIntent(ActivityQuestionForm.this)
                                    .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                    .setExportPrefix(getString(R.string.mTextFile_imagename))
                                    .setEditorIntent(
                                            new PhotoEditorIntent(ActivityQuestionForm.this)
                                                    .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                    .setExportPrefix(getString(R.string.mTextFile_filename))
                                                    .destroySourceAfterSave(true)
                                    )
                                    .startActivityForResult(CAMERA_MULTIPLE_RESULT);
                        }
                    }
                });
                mImgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExplanationListImage explanationListImage = new ExplanationListImage();
                        explanationListImage.setmStrExplationListImage(null);
                        explanationListImage.setmStrImgDefault("1");
                        explanationListImage.setmStrId(mListImg.get(finalI).getmStrId());
                        funUpdateImg(explanationListImage, db);
                        //db.update_tb_audit_question_answer_normal(explanationListImage);
                        Glide.with(ActivityQuestionForm.this).load(R.drawable.ic_placeholder_audit).into(mMultipleImg);
                        mImgDelete.setVisibility(View.GONE);
                    }
                });
                mFlowImage.addView(Imghidden);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("<><><>resultcall");
        if (resultCode == RESULT_OK && requestCode == CAMERA_MULTIPLE_RESULT) {
                System.out.println("<><><>resultcall1");
                Toast.makeText(this, "<><><>resultcall1", Toast.LENGTH_LONG).show();
                String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
                System.out.println("<><><>resultcall1 "+path);
                File checkfile = new File(path);
                if(checkfile.exists()){
                    if (TextUtils.isEmpty(path) || path.equals(null)) {
                        mShowAlert("Image not loaded properly try again.", ActivityQuestionForm.this);
                    } else {
                        Toast.makeText(this, "Image saved at: " + path, Toast.LENGTH_LONG).show();
                        final File mMediaFolder = new File(path);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] imageBytes = baos.toByteArray();
                        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Glide.with(ActivityQuestionForm.this).load(mMediaFolder).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mMainImage);
                        ExplanationListImage explanationListImage = new ExplanationListImage();
                        explanationListImage.setmStrExplationListImage(mMediaFolder.toString());
                        explanationListImage.setmStrImgDefault("0");
                        explanationListImage.setmStrId(mStrImgId);
                        funUpdateImg(explanationListImage, db);
                    }

                }else{
                    mShowAlert("Image not loaded properly try again.", ActivityQuestionForm.this);
                }






            //db.update_tb_audit_question_answer_normal(explanationListImage);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);

            File checkfile = new File(path);
            if(checkfile.exists()) {

                Toast.makeText(this, "Image saved at: " + path, Toast.LENGTH_LONG).show();
                final File mMediaFolder = new File(path);
                //have to showPDialog and save in database acc to ques
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Glide.with(ActivityQuestionForm.this).load(mMediaFolder).centerCrop().placeholder(R.drawable.ic_placeholder_audit).into(mImgSubGroup);
                mExplanationImage = mMediaFolder.toString();


                SubLocationExplation subLocationExplation = new SubLocationExplation();
                subLocationExplation.setmStrId(mExplanationID);
                subLocationExplation.setmStrExplanationImage(mMediaFolder.toString());
                subLocationExplation.setmStrAuditId(mAuditId);
                subLocationExplation.setmStrUserId(mUserId);
                subLocationExplation.setmStrMainLocationServer(mMainLocationID);
                funUpdateSubLocationLayer(subLocationExplation, "3", db);


                //db.update_tb_sub_location_explation_list_image(subLocationExplation);
                db.update_question_answer_table_image(mAuditId, PreferenceManager.getFormiiId(ActivityQuestionForm.this), mExplanationID, "2", mMediaFolder.toString());
            }else {
                mShowAlert("Image not loaded properly try again.", ActivityQuestionForm.this);
            }

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_SINGLE_RESULT) {
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);

            File checkfile = new File(path);
            if (checkfile.exists()) {

                Toast.makeText(this, "Image saved at: " + path, Toast.LENGTH_LONG).show();
                final File mMediaFolder = new File(path);
                mStrImagePath = mMediaFolder.toString();

                //have to showPDialog and save in database acc to ques
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                mImageTake.setVisibility(View.VISIBLE);
                mMainImgCancel.setVisibility(View.VISIBLE);
                mMainImgCamera.setVisibility(View.GONE);
                mImageMap.put(mTextQuesId, mStrImagePath);
                mQuesTextViewImage.setText(mStrImagePath);
                Glide.with(ActivityQuestionForm.this).load(mStrImagePath).into(mImageTake);
            } else {
                System.out.println("<><><>resultcall else");
                return;
            }
        }else {
            mShowAlert("Image not loaded properly try again.", ActivityQuestionForm.this);
        }


    }

    /*Normal question view*/
    public void mCreateViews() {
        for (int i = 0; i < mListNormalQuestion.size(); i++) {
            LayoutInflater mInflater = LayoutInflater.from(ActivityQuestionForm.this);
            View convertView = null;
            final AuditQuestion mAuditQues = mListNormalQuestion.get(i);
            if (mAuditQues.getmStrQuestionType().equals("1")) {
                /*Type:text*/
                convertView = mInflater.inflate(R.layout.view_question_edittext, null);
                TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                EditTextRegular mEditAnsOption = convertView.findViewById(R.id.mEditAnsOption);
                ImageView mImgExtraText = convertView.findViewById(R.id.mImgExtraText);
                ImageView mImgInspectorQuestion = convertView.findViewById(R.id.mImgInspectorQuestion);
                final TextView mExtraText = convertView.findViewById(R.id.mExtraText);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = convertView.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = convertView.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = convertView.findViewById(R.id.mImgCancel);
                TextView mQuestionType = convertView.findViewById(R.id.mQuestionType);
                final TextView mImageText = convertView.findViewById(R.id.mImageText);
                TextView mQuestionSub = convertView.findViewById(R.id.mQuestionSub);
                TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);

                TextView mInspectorQuestion = convertView.findViewById(R.id.mInspectorQuestion);

                if (mAuditQues.getmStrInspectorQuestion().equals("1")) {
                    mInspectorQuestion.setText("1");
                    mEditComment.setVisibility(View.GONE);
                    mImgExtraText.setVisibility(View.VISIBLE);
                    mImgInspectorQuestion.setVisibility(View.VISIBLE);
                } else {
                    mInspectorQuestion.setText("0");
                    mEditComment.setVisibility(View.VISIBLE);
                    mImgExtraText.setVisibility(View.GONE);
                    mImgInspectorQuestion.setVisibility(View.GONE);
                }


                /*set question-answer*/
                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                mImgExtraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmation(mExtraText);
                    }
                });


                mImgInspectorQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inspector inspector = new Inspector();
                        inspector.setmStrAuditId(mAuditId);
                        inspector.setmStrUserId(mUserId);
                        inspector.setmStrMainLocation(mMainLocationID);
                        inspector.setmStrSubLocation(mStrSubLocationServerId);
                        inspector.setmStrMainQuestion(mAuditQues.getmStrServerId());
                        funInspectorQuestion(inspector);
                    }
                });


                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt().replace("#", "*"));
                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mAnswerId.setText("");


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);


                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "1");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mExtraText.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditAnsOption.setText(mAuditList.get(0).getmStrAnswerValue().trim());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                }



                /*Question level spinner*/

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                 /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });

                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                mLayoutAddQuestion.addView(convertView);


            } else if (mAuditQues.getmStrQuestionType().equals("2")) {
                  /*Type:Radio*/
                convertView = mInflater.inflate(R.layout.view_question_radio_button, null);
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                final RadioGroup mRadioGroupOption = convertView.findViewById(R.id.mRadioGroupOption);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = convertView.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = convertView.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = convertView.findViewById(R.id.mImgCancel);
                final TextView mImageText = convertView.findViewById(R.id.mImageText);
                TextView mQuestionSub = convertView.findViewById(R.id.mQuestionSub);
                final TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuestionType = convertView.findViewById(R.id.mQuestionType);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);
                /*set question-answer*/

                ImageView mImgExtraText = convertView.findViewById(R.id.mImgExtraText);
                ImageView mImgInspectorQuestion = convertView.findViewById(R.id.mImgInspectorQuestion);
                final TextView mExtraText = convertView.findViewById(R.id.mExtraText);


                TextView mInspectorQuestion = convertView.findViewById(R.id.mInspectorQuestion);

                if (mAuditQues.getmStrInspectorQuestion().equals("1")) {
                    mInspectorQuestion.setText("1");
                    mEditComment.setVisibility(View.GONE);
                    mImgExtraText.setVisibility(View.VISIBLE);
                    mImgInspectorQuestion.setVisibility(View.VISIBLE);
                } else {
                    mInspectorQuestion.setText("0");
                    mEditComment.setVisibility(View.VISIBLE);
                    mImgExtraText.setVisibility(View.GONE);
                    mImgInspectorQuestion.setVisibility(View.GONE);
                }

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                mImgExtraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmation(mExtraText);
                    }
                });

                mImgInspectorQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inspector inspector = new Inspector();
                        inspector.setmStrAuditId(mAuditId);
                        inspector.setmStrUserId(mUserId);
                        inspector.setmStrMainLocation(mMainLocationID);
                        inspector.setmStrSubLocation(mStrSubLocationServerId);
                        inspector.setmStrMainQuestion(mAuditQues.getmStrServerId());
                        funInspectorQuestion(inspector);
                    }
                });


                String alredySelected = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "1");
                if (mAuditList.size() > 0) {
                    System.out.println("<><><><>007 ");
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mExtraText.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());
                }


                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt().replace("#", "*"));

                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = mAuditQues.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");

                String mAnswerOptionId = mAuditQues.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");
                for (int k = 0; k < separatedId.length; k++) {
                    if (separatedId[k].equals(alredySelected)) {
                        alredySelected = k + "";
                    }
                    mStrListAnswerId.put(separatedAns[k], separatedId[k]);
                }

                for (int j = 0; j < separatedAns.length; j++) {
                    radioButton = new RadioButton(ActivityQuestionForm.this);
                    radioButton.setText(separatedAns[j]);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    mRadioGroupOption.addView(radioButton, params);
                }

                if (alredySelected.length() <= 0) {
                } else {
                    System.out.println("<><><><>003 " + alredySelected);
                    //((RadioButton)mRadioGroupOption.getChildAt(1)).setChecked(true);
                    ((RadioButton) mRadioGroupOption.getChildAt(Integer.parseInt(alredySelected))).setChecked(true);

                }


                mRadioGroupOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup rg, int checkedId) {
                        for (int i = 0; i < rg.getChildCount(); i++) {
                            final RadioButton btn = (RadioButton) rg.getChildAt(i);
                            mAnswerId.setText(mStrListAnswerId.get(btn.getText()));
                            if (btn.getId() == checkedId) {
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mAuditQues.getmStrSubQuestion().equals("1")) {
                                            System.out.println("<><><><>isSubQuestionForOption");
                                            if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mStrListAnswerId.get(btn.getText()), mAuditId)) {
                                                //have sub quest new screen
                                                Intent intent = new Intent(ActivityQuestionForm.this, ActivitySubQuestionForm.class);
                                                intent.putExtra("mStrMainQues", mTxtQuestion.getText().toString());
                                                intent.putExtra("mStrMainAns", btn.getText());
                                                intent.putExtra("mStrMainLocationServerId", mMainLocationID);
                                                intent.putExtra("mAuditId", mAuditId);
                                                intent.putExtra("mMainQuesID", mAuditQues.getmStrId());
                                                intent.putExtra("mMainQuesServerID", mAuditQues.getmStrServerId());
                                                intent.putExtra("mQuesFor", mStrListAnswerId.get(btn.getText()));
                                                intent.putExtra("mExplanationID", mExplanationID);
                                                intent.putExtra("msubLocationServer", mStrSubLocationServerId);
                                                startActivity(intent);

                                            }
                                        }
                                    }
                                });


                                return;
                            }
                        }
                    }
                });

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

                 /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });
                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                mLayoutAddQuestion.addView(convertView);
            } else if (mAuditQues.getmStrQuestionType().equals("3")) {
                  /*Type:Checkbox*/
                convertView = mInflater.inflate(R.layout.view_question_checkbox, null);
                LinearLayout mLayoutCheckOption = convertView.findViewById(R.id.mLayoutCheckOption);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = convertView.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = convertView.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = convertView.findViewById(R.id.mImgCancel);
                TextView mQuestionType = convertView.findViewById(R.id.mQuestionType);
                final TextView mImageText = convertView.findViewById(R.id.mImageText);
                TextView mQuestionSub = convertView.findViewById(R.id.mQuestionSub);
                final TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                TextView mEditAnsOption = convertView.findViewById(R.id.mEditAnsOption);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);


                ImageView mImgExtraText = convertView.findViewById(R.id.mImgExtraText);
                ImageView mImgInspectorQuestion = convertView.findViewById(R.id.mImgInspectorQuestion);
                final TextView mExtraText = convertView.findViewById(R.id.mExtraText);


                TextView mInspectorQuestion = convertView.findViewById(R.id.mInspectorQuestion);

                if (mAuditQues.getmStrInspectorQuestion().equals("1")) {
                    mInspectorQuestion.setText("1");
                    mEditComment.setVisibility(View.GONE);
                    mImgExtraText.setVisibility(View.VISIBLE);
                    mImgInspectorQuestion.setVisibility(View.VISIBLE);
                } else {
                    mInspectorQuestion.setText("0");
                    mEditComment.setVisibility(View.VISIBLE);
                    mImgExtraText.setVisibility(View.GONE);
                    mImgInspectorQuestion.setVisibility(View.GONE);
                }


                final ArrayList<String> listOfSelectedCheckBoxId = new ArrayList<>();

                String mAnswerOption = mAuditQues.getmStrAnswerOption();
                String mAnswerOptionId = mAuditQues.getmStrAnswerOptionId();

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });

                mImgExtraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmation(mExtraText);
                    }
                });

                mImgInspectorQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inspector inspector = new Inspector();
                        inspector.setmStrAuditId(mAuditId);
                        inspector.setmStrUserId(mUserId);
                        inspector.setmStrMainLocation(mMainLocationID);
                        inspector.setmStrSubLocation(mStrSubLocationServerId);
                        inspector.setmStrMainQuestion(mAuditQues.getmStrServerId());
                        funInspectorQuestion(inspector);
                    }
                });

                String alredySelected = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "1");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mExtraText.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());


                }



                /*set question-answer*/

                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());

                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt().replace("#", "*"));
                final Map<String, String> mStrListAnswerId = new HashMap<>();

                final String[] separatedAns = mAnswerOption.split("#");


                String[] separatedId = mAnswerOptionId.split("#");

                for (int j = 0; j < separatedAns.length; j++) {
                    CheckBox checkBox = new CheckBox(ActivityQuestionForm.this);
                    checkBox.setText(separatedAns[j]);
                    if (alredySelected.contains(separatedId[j])) {
                        checkBox.setChecked(true);
                    }
                    mLayoutCheckOption.addView(checkBox);

                }

                for (int j = 0; j < separatedId.length; j++) {
                    mStrListAnswerId.put(separatedAns[j], separatedId[j]);
                }


                if (alredySelected.length() > 0) {
                    System.out.println("<><><>007 " + alredySelected);
                    String mIDCovert[] = alredySelected.split(",");
                    for (int g = 0; g < mIDCovert.length; g++) {
                        listOfSelectedCheckBoxId.add(mIDCovert[g]);
                    }
                }


                for (int k = 0; k < mLayoutCheckOption.getChildCount(); k++) {
                    final CheckBox checkBox = (CheckBox) mLayoutCheckOption.getChildAt(k);
                    final int finalJ = k;
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (checkBox.isChecked()) {
                                System.out.println("<><><>abc 1 " + mStrListAnswerId.get(separatedAns[finalJ]));
                                checkBox.setChecked(true);
                                listOfSelectedCheckBoxId.add(mStrListAnswerId.get(separatedAns[finalJ]));
                                System.out.println("<><><>abc 1 " + listOfSelectedCheckBoxId.size());

                            } else {
                                System.out.println("<><><>abc 2 " + mStrListAnswerId.get(separatedAns[finalJ]));
                                checkBox.setChecked(false);
                                listOfSelectedCheckBoxId.remove(mStrListAnswerId.get(separatedAns[finalJ]));
                                System.out.println("<><><>abc 2 " + listOfSelectedCheckBoxId.size());
                            }


                            StringBuilder sb = new StringBuilder();
                            for (String item : listOfSelectedCheckBoxId) {
                                if (sb.length() > 0) {
                                    sb.append(',');
                                }
                                sb.append(item);
                            }

                            if (sb.toString().equals("")) {
                                System.out.println("<><><>no ans ");
                                mAnswerId.setText("");
                            } else {
                                System.out.println("<><><>ans " + sb.toString());
                                mAnswerId.setText(sb.toString());
                            }


                        }
                    });
                }


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                 /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });
                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                mLayoutAddQuestion.addView(convertView);
            } else if (mAuditQues.getmStrQuestionType().equals("4")) {
                  /*Type:dropdown*/
                convertView = mInflater.inflate(R.layout.view_question_dropdown, null);
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final Spinner mSpinnerQuesOption = convertView.findViewById(R.id.mSpinnerQuesOption);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = convertView.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = convertView.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = convertView.findViewById(R.id.mImgCancel);
                TextView mQuestionType = convertView.findViewById(R.id.mQuestionType);
                final TextView mImageText = convertView.findViewById(R.id.mImageText);
                TextView mQuestionSub = convertView.findViewById(R.id.mQuestionSub);
                final TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);
                final RelativeLayout mViewSpiner = convertView.findViewById(R.id.mViewSpiner);

                ImageView mImgExtraText = convertView.findViewById(R.id.mImgExtraText);
                ImageView mImgInspectorQuestion = convertView.findViewById(R.id.mImgInspectorQuestion);
                final TextView mExtraText = convertView.findViewById(R.id.mExtraText);


                TextView mInspectorQuestion = convertView.findViewById(R.id.mInspectorQuestion);

                if (mAuditQues.getmStrInspectorQuestion().equals("1")) {
                    mInspectorQuestion.setText("1");
                    mEditComment.setVisibility(View.GONE);
                    mImgExtraText.setVisibility(View.VISIBLE);
                    mImgInspectorQuestion.setVisibility(View.VISIBLE);
                } else {
                    mInspectorQuestion.setText("0");
                    mEditComment.setVisibility(View.VISIBLE);
                    mImgExtraText.setVisibility(View.GONE);
                    mImgInspectorQuestion.setVisibility(View.GONE);
                }


                /*set question-answer*/
                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                /*set question-answer*/
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt().replace("#", "*"));
                String mAnswerOption = mAuditQues.getmStrAnswerOption();


                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                mImgExtraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmation(mExtraText);
                    }
                });


                mImgInspectorQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inspector inspector = new Inspector();
                        inspector.setmStrAuditId(mAuditId);
                        inspector.setmStrUserId(mUserId);
                        inspector.setmStrMainLocation(mMainLocationID);
                        inspector.setmStrSubLocation(mStrSubLocationServerId);
                        inspector.setmStrMainQuestion(mAuditQues.getmStrServerId());
                        funInspectorQuestion(inspector);
                    }
                });


                String alredySelected = "";
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "1");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mExtraText.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());
                }


                final ArrayList<AuditQuestion> ansList = new ArrayList<>();
                String[] separatedAns = mAnswerOption.split("#");
                List<String> listAns = new LinkedList<String>(Arrays.asList(separatedAns));
                listAns.add(0, getString(R.string.mTextFile_select_option));
                separatedAns = listAns.toArray(new String[listAns.size()]);

                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOptionId = mAuditQues.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");
                List<String> listAnsId = new LinkedList<String>(Arrays.asList(separatedId));
                listAnsId.add(0, "0");
                separatedId = listAnsId.toArray(new String[listAnsId.size()]);

                for (int j = 0; j < separatedId.length; j++) {
                    if (separatedId[j].equals(alredySelected)) {
                        alredySelected = j + "";
                    }

                    ansList.add(new AuditQuestion(separatedId[j], separatedAns[j]));
                    mStrListAnswerId.put(separatedAns[j], separatedId[j]);
                }

                ArrayAdapter<AuditQuestion> spinnerQuesAdapter = new ArrayAdapter<AuditQuestion>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, ansList.toArray(new AuditQuestion[ansList.size()]));
                spinnerQuesAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesOption.setAdapter(spinnerQuesAdapter);


                if (alredySelected.length() <= 0) {

                } else {
                    System.out.println("<><><><>ss " + Integer.parseInt(alredySelected));
                    mSpinnerQuesOption.setSelection(Integer.parseInt(alredySelected), false);

                    if (Integer.parseInt(alredySelected) == 0) {
                        mSpinnerQuesOption.setBackgroundResource(R.color.transparent);
                        mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                    } else {
                        mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                    }

                }

                mSpinnerQuesOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        System.out.println("<><><><>mCall1");
                        AuditQuestion auditQuestion = ansList.get(pos);
                        String mSelectedTitle = auditQuestion.getmStrAnswerOption();
                        mAnswerId.setText(mStrListAnswerId.get(mSelectedTitle));
                        if (!mSelectedTitle.equals(getString(R.string.mTextFile_select_option))) {
                            mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                            mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                            if (mAuditQues.getmStrSubQuestion().equals("1")) {
                                //have sub quest new screen

                                if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), auditQuestion.getmStrAnswerOptionId(), mAuditId)) {
                                    Intent intent = new Intent(ActivityQuestionForm.this, ActivitySubQuestionForm.class);
                                    intent.putExtra("mStrMainQues", mTxtQuestion.getText().toString());
                                    intent.putExtra("mStrMainLocationServerId", mMainLocationID);
                                    intent.putExtra("mStrMainAns", mSelectedTitle);
                                    intent.putExtra("mAuditId", mAuditId);
                                    intent.putExtra("mMainQuesID", mAuditQues.getmStrId());
                                    intent.putExtra("mMainQuesServerID", mAuditQues.getmStrServerId());
                                    intent.putExtra("mQuesFor", auditQuestion.getmStrAnswerOptionId());
                                    intent.putExtra("mExplanationID", mExplanationID);
                                    intent.putExtra("msubLocationServer", mStrSubLocationServerId);
                                    startActivity(intent);
                                }

                            }
                        } else {
                            mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                            mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println("<><><><>mCall2");
                    }
                });


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                 /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });

                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                mLayoutAddQuestion.addView(convertView);
            } else if (mAuditQues.getmStrQuestionType().equals("5")) {

            /*    convertView = mInflater.inflate(R.layout.view_question_popup, null);
                TextViewSemiBold mTxtQuestion = (TextViewSemiBold)convertView.findViewById(R.id.mTxtQuestion);
                LinearLayout mOptionLayout = (LinearLayout)convertView.findViewById(R.id.mOptionLayout);
                String []mQuestionOption = mAuditQues.getmStrAnswerOption().split("#");
                for (int j = 0; j < mQuestionOption.length; j++) {
                    LinearLayout mFlowView = (LinearLayout) View.inflate(ActivityQuestionForm.this, R.layout.view_ques_popup_option_item, null);
                    TextViewSemiBold mTxtMeasurement = (TextViewSemiBold) mFlowView.findViewById(R.id.mTxtMeasurement);
                    EditTextRegular mEditAnswer = (EditTextRegular) mFlowView.findViewById(R.id.mEditAnswer);
                    mTxtMeasurement.setText(mQuestionOption[j]);
                    mOptionLayout.addView(mFlowView);
                }
                mLayoutAddQuestion.addView(convertView);*/

            } else if (mAuditQues.getmStrQuestionType().equals("6")) {
                  /*Type:True-false*/
                convertView = mInflater.inflate(R.layout.view_question_yes_no, null);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                final RelativeLayout mLayoutYes = convertView.findViewById(R.id.mLayoutYes);
                final RelativeLayout mLayoutNo = convertView.findViewById(R.id.mLayoutNo);
                final TextViewRegular mTextYes = convertView.findViewById(R.id.mTextYes);
                final TextViewRegular mTextNo = convertView.findViewById(R.id.mTextNo);
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = convertView.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = convertView.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = convertView.findViewById(R.id.mImgCancel);
                TextView mQuestionType = convertView.findViewById(R.id.mQuestionType);
                final TextView mImageText = convertView.findViewById(R.id.mImageText);
                TextView mQuestionSub = convertView.findViewById(R.id.mQuestionSub);
                final TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);
                /*set question-answer*/


                ImageView mImgExtraText = convertView.findViewById(R.id.mImgExtraText);
                ImageView mImgInspectorQuestion = convertView.findViewById(R.id.mImgInspectorQuestion);
                final TextView mExtraText = convertView.findViewById(R.id.mExtraText);

                TextView mInspectorQuestion = convertView.findViewById(R.id.mInspectorQuestion);

                if (mAuditQues.getmStrInspectorQuestion().equals("1")) {
                    mInspectorQuestion.setText("1");
                    mEditComment.setVisibility(View.GONE);
                    mImgExtraText.setVisibility(View.VISIBLE);
                    mImgInspectorQuestion.setVisibility(View.VISIBLE);
                } else {
                    mInspectorQuestion.setText("0");
                    mEditComment.setVisibility(View.VISIBLE);
                    mImgExtraText.setVisibility(View.GONE);
                    mImgInspectorQuestion.setVisibility(View.GONE);
                }


                String alredySelected = "";

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                mImgExtraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmation(mExtraText);
                    }
                });


                mImgInspectorQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inspector inspector = new Inspector();
                        inspector.setmStrAuditId(mAuditId);
                        inspector.setmStrUserId(mUserId);
                        inspector.setmStrMainLocation(mMainLocationID);
                        inspector.setmStrSubLocation(mStrSubLocationServerId);
                        inspector.setmStrMainQuestion(mAuditQues.getmStrServerId());
                        funInspectorQuestion(inspector);
                    }
                });


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "1");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mExtraText.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    //mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());

                }

                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt().replace("#", "*"));

                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = mAuditQues.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");

                String mAnswerOptionId = mAuditQues.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");
                for (int j = 0; j < separatedId.length; j++) {
                    mStrListAnswerId.put(separatedAns[j], separatedId[j]);
                }


                for (int j = 0; j < separatedAns.length; j++) {
                    mTextYes.setText(separatedAns[0]);
                    mTextNo.setText(separatedAns[1]);
                    if (separatedId[0].equals(alredySelected)) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextYes.getText().toString()));
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_red_dark);
                    } else if (separatedId[1].equals(alredySelected)) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextNo.getText().toString()));
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_layer_app_green);
                    }
                }


                mLayoutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextYes.getText().toString()));
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_red_dark);
                        if (mAuditQues.getmStrSubQuestion().equals("1")) {
                            //have sub quest new screen
                            if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()), mAuditId)) {
                                Intent intent = new Intent(ActivityQuestionForm.this, ActivitySubQuestionForm.class);
                                intent.putExtra("mStrMainQues", mTxtQuestion.getText().toString());
                                intent.putExtra("mStrMainLocationServerId", mMainLocationID);
                                intent.putExtra("mStrMainAns", mTextYes.getText().toString());
                                intent.putExtra("mAuditId", mAuditId);
                                intent.putExtra("mMainQuesID", mAuditQues.getmStrId());
                                intent.putExtra("mMainQuesServerID", mAuditQues.getmStrServerId());
                                intent.putExtra("mQuesFor", mStrListAnswerId.get(mTextYes.getText().toString()));
                                intent.putExtra("mExplanationID", mExplanationID);
                                intent.putExtra("msubLocationServer", mStrSubLocationServerId);
                                startActivity(intent);
                            }
                        }
                    }
                });

                mLayoutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextNo.getText().toString()));
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_layer_app_green);
                        if (mAuditQues.getmStrSubQuestion().equals("1")) {
                            //have sub quest new screen
                            if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()), mAuditId)) {
                                Intent intent = new Intent(ActivityQuestionForm.this, ActivitySubQuestionForm.class);
                                intent.putExtra("mStrMainQues", mTxtQuestion.getText().toString());
                                intent.putExtra("mStrMainAns", mTextNo.getText().toString());
                                intent.putExtra("mStrMainLocationServerId", mMainLocationID);
                                intent.putExtra("mAuditId", mAuditId);
                                intent.putExtra("mMainQuesID", mAuditQues.getmStrId());
                                intent.putExtra("mMainQuesServerID", mAuditQues.getmStrServerId());
                                intent.putExtra("mQuesFor", mStrListAnswerId.get(mTextNo.getText().toString()));
                                intent.putExtra("mExplanationID", mExplanationID);
                                intent.putExtra("msubLocationServer", mStrSubLocationServerId);
                                startActivity(intent);
                            } else {
                                System.out.println("<><><no sub question");
                            }
                        }
                    }
                });


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });
                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                mLayoutAddQuestion.addView(convertView);
            }
        }
    }

    /*Measurement question view*/
    public void mCreateMeasurementViews() {
        for (int i = 0; i < mListMeasurementQuestion.size(); i++) {
            final String mQuestionId = mListMeasurementQuestion.get(i).getmStrServerId();
            LinearLayout mFlowView = (LinearLayout) View.inflate(ActivityQuestionForm.this, R.layout.view_measurement_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 0, 5, 0);
            TextViewSemiBold mTxtMeasurement = mFlowView.findViewById(R.id.mTxtMeasurement);
            mTxtMeasurement.setText(mListMeasurementQuestion.get(i).getmStrQuestionTxt());
            mFlowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityQuestionForm.this, ActivityMesurementQuestionForm.class);
                    intent.putExtra("mStrQuestionId", mQuestionId);
                    System.out.println("<><>mMainLocationIDque" + mMainLocationID);
                    intent.putExtra("mStrMainLocationServer", mMainLocationID);
                    intent.putExtra("mUserId", PreferenceManager.getFormiiId(ActivityQuestionForm.this));
                    intent.putExtra("mAuditId", mAuditId);
                    intent.putExtra("mStrCountryId", mStrCountryId);
                    intent.putExtra("mStrLanguageId", mStrLanguageId);
                    intent.putExtra("mStrSubLocationServerId", mStrSubLocationServerId);
                    intent.putExtra("mExplanationTitle", mExplanationTitle);
                    intent.putExtra("mExplanationDesc", mExplanationDesc);
                    intent.putExtra("mExplanationImage", mExplanationImage);
                    intent.putExtra("mLayerTitle", mLayerTitle);
                    intent.putExtra("mLayerDesc", mLayerDesc);
                    intent.putExtra("mStrLayerImage", mStrLayerImage);
                    intent.putExtra("mStrSubFolderId", mStrSubFolderId);
                    intent.putExtra("mStrSubFolderTitle", mStrSubFolderTitle);
                    intent.putExtra("mExplanationID", mExplanationID);
                    intent.putExtra("mLayerID", mLayerID);
                    startActivity(intent);
                }
            });

            mLayoutMeasurementView.addView(mFlowView, layoutParams);

        }
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


    public void funConfirmation(final TextView mExtraText) {
        final Dialog dialog = new Dialog(ActivityQuestionForm.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning_popup_two_button);
        dialog.setCancelable(false);
        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
        RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
        RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
        mTxtMsg.setText("Do you really really want to show the description message?");
        mLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        mLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funEnterComment(mExtraText);
                dialog.cancel();
            }
        });
        dialog.show();

    }


    public void funConfirmationBeforEdit(final EditTextRegular mExtraText) {
        final Dialog dialog = new Dialog(ActivityQuestionForm.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning_popup_two_button);
        dialog.setCancelable(false);
        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
        RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
        RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
        mTxtMsg.setText("Do you really really want to show the description message?");
        mLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        mLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtraText.setOnClickListener(null);
                mExtraText.setFocusableInTouchMode(true);
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void funEnterComment(final TextView mExtraText) {
        final Dialog dialog = new Dialog(ActivityQuestionForm.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_question_extra_text);
        dialog.setCancelable(false);
        final EditTextSemiBold mEditComment = dialog.findViewById(R.id.mEditComment);
        mEditComment.setText(mExtraText.getText().toString());
        TextViewBold mTxtAddButton = dialog.findViewById(R.id.mTxtAddButton);
        TextViewBold mTxtCancelButton = dialog.findViewById(R.id.mTxtCancelButton);
        mTxtCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(ActivityQuestionForm.this);
                dialog.dismiss();
            }
        });
        mTxtAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditComment.getText().toString().length() <= 0) {
                    hideKeyboard(ActivityQuestionForm.this);
                    mShowAlert("Please enter some text otherwise press cancel", ActivityQuestionForm.this);

                } else {
                    hideKeyboard(ActivityQuestionForm.this);
                    mExtraText.setText(mEditComment.getText().toString());
                    dialog.cancel();
                }

            }
        });
        dialog.show();

    }

    public void funInspectorQuestion(final Inspector inspector) {

        mInsAuditId = "";
        mInsUserId = "";
        mInsQuestionId = "";
        mInsAnswerId = "";
        mInsAnswerValue = "";
        mInsQuestionType = "";
        mInsQuestionImage = "";
        mInsQuestionPriority = "";
        mInsMainQuestion = "";
        mInsSubLocationExplanation = "";
        mInsMainLocation = "";
        mInsSubLocation = "";


        ArrayList<Inspector> mInspectorQuestionList = new ArrayList<Inspector>();
        mInspectorQuestionList = db.get_tb_inspector_questions(inspector);
        for (int i = 0; i < mInspectorQuestionList.size(); i++) {
            final Inspector inspectors = mInspectorQuestionList.get(i);

            String alredySelected = "";

            AuditInspectorQuestion auditInspectorQuestion = new AuditInspectorQuestion();
            auditInspectorQuestion.setmStrSubLocationExplanation(mExplanationID);
            auditInspectorQuestion.setmStrSubLocation(mStrSubLocationServerId);
            auditInspectorQuestion.setmStrAuditId(inspectors.getmStrAuditId());
            auditInspectorQuestion.setmStrUserId(inspectors.getmStrUserId());
            auditInspectorQuestion.setmStrMainQuestion(inspector.getmStrMainQuestion());
            auditInspectorQuestion.setmStrQuestionId(inspectors.getmStrQuestionId());


            if (inspectors.getmStrQuestionType().equals("2")) {
                final Dialog dialog = new Dialog(ActivityQuestionForm.this, R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.view_inpector_question_radio_button);
                dialog.setCancelable(false);
                final TextViewSemiBold mTxtQuestion = dialog.findViewById(R.id.mTxtQuestion);
                final RadioGroup mRadioGroupOption = dialog.findViewById(R.id.mRadioGroupOption);
                final RelativeLayout mLayoutQuesLevel = dialog.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = dialog.findViewById(R.id.mSpinnerQuesLevel);
                final ImageView mImgQues = dialog.findViewById(R.id.mImgQues);
                final ImageView mImgCamera = dialog.findViewById(R.id.mImgCamera);
                final ImageView mImgCancel = dialog.findViewById(R.id.mImgCancel);
                final TextView mImageText = dialog.findViewById(R.id.mImageText);
                RelativeLayout mLayoutCancel = dialog.findViewById(R.id.mLayoutCancel);
                RelativeLayout mLayoutSave = dialog.findViewById(R.id.mLayoutSave);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                if (db.isInspecterQuestionAnswerSubmmited(auditInspectorQuestion)) {
                    ArrayList<AuditInspectorQuestion> mAlredSubmitedAnswer = new ArrayList<AuditInspectorQuestion>();
                    mAlredSubmitedAnswer = db.get_tb_inspector_questions_answer(auditInspectorQuestion);
                    for (int x = 0; x < mAlredSubmitedAnswer.size(); x++) {
                        alredySelected = mAlredSubmitedAnswer.get(x).getmStrAnswerId();
                        mInsAnswerId = mAlredSubmitedAnswer.get(x).getmStrAnswerId();
                        System.out.println("<><><>##### " + mInsAnswerId);
                        mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAlredSubmitedAnswer.get(x).getmStrQuestionPriority()));
                        if (TextUtils.isEmpty(mAlredSubmitedAnswer.get(x).getmStrQuestionImage()) || mAlredSubmitedAnswer.get(x).getmStrQuestionImage() == null) {
                            mImgCancel.setVisibility(View.GONE);
                            mImgQues.setVisibility(View.GONE);
                            mImgCamera.setVisibility(View.VISIBLE);
                        } else {
                            mImageText.setText(mAlredSubmitedAnswer.get(x).getmStrQuestionImage());
                            Glide.with(ActivityQuestionForm.this).load(mAlredSubmitedAnswer.get(x).getmStrQuestionImage()).into(mImgQues);
                            mImgCancel.setVisibility(View.VISIBLE);
                            mImgQues.setVisibility(View.VISIBLE);
                            mImgCamera.setVisibility(View.GONE);
                        }
                    }
                }


                mLayoutSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInsAuditId = inspectors.getmStrAuditId();
                        mInsUserId = inspectors.getmStrUserId();
                        mInsQuestionId = inspectors.getmStrQuestionId();
                        mInsQuestionType = inspectors.getmStrQuestionType();
                        mInsQuestionImage = mImageText.getText().toString();
                        mInsMainQuestion = inspector.getmStrMainQuestion();
                        mInsSubLocationExplanation = mExplanationID;
                        mInsMainLocation = mMainLocationID;
                        mInsSubLocation = mStrSubLocationServerId;
                        mInsQuestionPriority = mSpinnerQuesLevel.getSelectedItem().toString();
                        AuditInspectorQuestion auditInspectorQuestion = new AuditInspectorQuestion();
                        auditInspectorQuestion.setmStrAuditId(inspectors.getmStrAuditId());
                        auditInspectorQuestion.setmStrUserId(inspectors.getmStrUserId());
                        auditInspectorQuestion.setmStrQuestionId(inspectors.getmStrQuestionId());
                        auditInspectorQuestion.setmStrQuestionType(inspectors.getmStrQuestionType());
                        auditInspectorQuestion.setmStrQuestionImage(mImageText.getText().toString());
                        auditInspectorQuestion.setmStrMainQuestion(inspector.getmStrMainQuestion());
                        auditInspectorQuestion.setmStrSubLocationExplanation(mExplanationID);
                        auditInspectorQuestion.setmStrMainLocation(mMainLocationID);
                        auditInspectorQuestion.setmStrSubLocation(mStrSubLocationServerId);
                        auditInspectorQuestion.setmStrQuestionPriority(mInsQuestionPriority);
                        auditInspectorQuestion.setmStrAnswerId(mInsAnswerId);
                        if (!mInsAnswerId.equals("") || !mInsAnswerId.isEmpty()) {
                            System.out.println("<><><> " + mInsAnswerId);
                            if (db.isInspecterQuestionAnswerSubmmited(auditInspectorQuestion)) {
                                db.delete_tb_inspector_questions(auditInspectorQuestion);
                                db.insert_tb_inspector_questions_answer(auditInspectorQuestion);
                            } else {
                                db.insert_tb_inspector_questions_answer(auditInspectorQuestion);
                            }
                        } else {
                            db.delete_tb_inspector_questions(auditInspectorQuestion);
                        }
                        dialog.cancel();

                    }
                });

                mLayoutCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                mTxtQuestion.setText(inspectors.getmStrQuestionTxt().replace("#", "*"));
                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = inspectors.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");


                String mAnswerOptionId = inspectors.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");


                for (int k = 0; k < separatedId.length; k++) {
                    if (separatedId[k].equals(alredySelected)) {
                        alredySelected = k + "";
                    }
                    System.out.println("???? " + separatedAns[k] + " ???? " + separatedId[k]);
                    mStrListAnswerId.put(separatedAns[k], separatedId[k]);
                }


                for (int j = 0; j < separatedAns.length; j++) {
                    radioButton = new RadioButton(ActivityQuestionForm.this);
                    radioButton.setText(separatedAns[j]);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    mRadioGroupOption.addView(radioButton, params);
                }

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        mInsQuestionPriority = mSpinnerQuesLevel.getSelectedItem().toString();

                        if (mInsQuestionPriority.equals(getString(R.string.mTextFile_high))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        } else if (mInsQuestionPriority.equals(getString(R.string.mTextFile_medium))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        } else if (mInsQuestionPriority.equals(getString(R.string.mTextFile_low))) {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        } else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

                if (alredySelected.length() <= 0) {
                } else {
                    System.out.println("<><><><>003 " + alredySelected);
                    //((RadioButton)mRadioGroupOption.getChildAt(1)).setChecked(true);
                    ((RadioButton) mRadioGroupOption.getChildAt(Integer.parseInt(alredySelected))).setChecked(true);

                }


                mRadioGroupOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup rg, int checkedId) {
                        for (int i = 0; i < rg.getChildCount(); i++) {
                            final RadioButton btn = (RadioButton) rg.getChildAt(i);
                            if (btn.getId() == checkedId) {
                                mInsAnswerId = mStrListAnswerId.get(btn.getText());
                                return;
                            }
                        }
                    }
                });


                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = inspectors.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityQuestionForm.this)
                                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_SINGLE_RESULT);
                    }
                });
                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    }
                });
                dialog.show();


            } else if (inspectors.getmStrQuestionType().equals("6")) {


            }


        }







   /*     final Dialog dialog = new Dialog(ActivityQuestionForm.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_question_extra_text);
        final EditTextSemiBold mEditComment = dialog.findViewById(R.id.mEditComment);
        mEditComment.setText(mExtraText.getText().toString());
        TextViewBold mTxtAddButton = dialog.findViewById(R.id.mTxtAddButton);
        TextViewBold mTxtCancelButton = dialog.findViewById(R.id.mTxtCancelButton);
        mTxtCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mTxtAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtraText.setText(mEditComment.getText().toString());
                dialog.cancel();
            }
        });
        dialog.show();*/

    }


}