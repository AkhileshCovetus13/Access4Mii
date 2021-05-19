package com.covetus.audit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.AuditQuestion;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_GET_SET.AuditSubQuestion;
import ABS_GET_SET.SubQuestionAnswer;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.MySpinner;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;

import static ABS_HELPER.CommonUtils.FOLDER;

/**
 * Created by admin1 on 3/1/19.
 */

public class ActivityMesurementQuestionForm extends Activity {

    public static int CAMERA_SINGLE_RESULT = 2;
    DatabaseHelper db;
    ArrayList<AuditQuestion> mListQuestion = new ArrayList<>();
    ArrayList<AuditSubQuestion> mListSubQuestion = new ArrayList<>();
    HashMap<String, String> mImageMap = new HashMap<String, String>();
    String[] mStringArray;
    String mStrMainQues, mStrMainAns, mAuditId, mMainQuesID, mQuesFor, mMainQuesServerID;
    String mStrImagePath = "";
    String mStrDelete = "0";
    String mTextQuesId = "0";
    ImageView mImageTake;
    ImageView mMainImgCancel;
    ImageView mMainImgCamera;


    @BindView(R.id.mLayoutAddSubQuestion)
    LinearLayout mLayoutAddSubQuestion;
    @BindView(R.id.mLayoutSave)
    RelativeLayout mLayoutSave;
    @BindView(R.id.mLayoutCancel)
    RelativeLayout mLayoutCancel;
    TextView mQuesTextViewImage;
    String mExplanationID;
    RadioButton radioButton;
    String mStrQuestionId, mMainLocationID, mUserId;
    String mStrCountryId, mStrLanguageId, mStrSubLocationServerId, mExplanationTitle, mExplanationDesc, mExplanationImage,
            mLayerID, mLayerTitle, mLayerDesc, mStrLayerImage, mStrSubFolderId, mStrSubFolderTitle;


    @OnClick(R.id.mLayoutSave)
    public void mTxtSaveData() {
        CommonUtils.closeKeyBoard(ActivityMesurementQuestionForm.this);
        int childCount = mLayoutAddSubQuestion.getChildCount();
        System.out.println("<><><>count " + childCount);
        for (int i = 0; i < childCount; i++) {
            String mStrQuestionType = "";
            String mStrQuesionId = "";
            String mStrQuesionServer = "";
            String mStrAnsOption = "";
            String mStrPriority = "";
            String mStrComment = "";
            String mStrImageText = "";
            String mStrQuestionFor = "";
            String mStrMainQuestion = "";
            String mStrAnswerId = "";


            View element = mLayoutAddSubQuestion.getChildAt(i);
            if (element instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) element;
                int childCount2 = linearLayout.getChildCount();
                System.out.println("<><><>count " + childCount2);
                for (int j = 0; j < childCount2; j++) {
                    View element2 = linearLayout.getChildAt(j);
                    if (j == 0) {
                        if (element2 instanceof LinearLayout) {
                            LinearLayout linearLayout4 = (LinearLayout) element2;
                            TextView mQuestionType = linearLayout4.findViewById(R.id.mQuestionType);
                            if (mQuestionType.getText().equals("1")) {

                                TextView mEditAnsOption = linearLayout4.findViewById(R.id.mEditAnsOption);
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                TextView mQuestionFor = linearLayout4.findViewById(R.id.mQuestionFor);
                                TextView mMainQuestion = linearLayout4.findViewById(R.id.mMainQuestion);

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnsOption = mEditAnsOption.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                                mStrQuestionFor = mQuestionFor.getText().toString();
                                mStrMainQuestion = mMainQuestion.getText().toString();


                            } else if (mQuestionType.getText().equals("2")) {
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                TextView mQuestionFor = linearLayout4.findViewById(R.id.mQuestionFor);
                                TextView mMainQuestion = linearLayout4.findViewById(R.id.mMainQuestion);

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                                mStrQuestionFor = mQuestionFor.getText().toString();
                                mStrMainQuestion = mMainQuestion.getText().toString();


                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                //System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());
                                System.out.println("<><><><>mQuestionFor " + mQuestionFor.getText());
                                System.out.println("<><><><>mMainQuestion " + mMainQuestion.getText());


                            } else if (mQuestionType.getText().equals("3")) {

                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                TextView mQuestionFor = linearLayout4.findViewById(R.id.mQuestionFor);
                                TextView mMainQuestion = linearLayout4.findViewById(R.id.mMainQuestion);


                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                                mStrQuestionFor = mQuestionFor.getText().toString();
                                mStrMainQuestion = mMainQuestion.getText().toString();

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                //System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());
                                System.out.println("<><><><>mQuestionFor " + mQuestionFor.getText());
                                System.out.println("<><><><>mMainQuestion " + mMainQuestion.getText());

                            } else if (mQuestionType.getText().equals("4")) {
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                TextView mQuestionFor = linearLayout4.findViewById(R.id.mQuestionFor);
                                TextView mMainQuestion = linearLayout4.findViewById(R.id.mMainQuestion);

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                                mStrQuestionFor = mQuestionFor.getText().toString();
                                mStrMainQuestion = mMainQuestion.getText().toString();

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                                //System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());
                                System.out.println("<><><><>mQuestionFor " + mQuestionFor.getText());
                                System.out.println("<><><><>mMainQuestion " + mMainQuestion.getText());

                            } else if (mQuestionType.getText().equals("6")) {
                                TextView mQuesionId = linearLayout4.findViewById(R.id.mQuesionId);
                                TextView mQuesionServerId = linearLayout4.findViewById(R.id.mQuesionServerId);
                                TextView mEditComment = linearLayout4.findViewById(R.id.mEditComment);
                                TextView mAnswerId = linearLayout4.findViewById(R.id.mAnswerId);
                                TextView mImageText = linearLayout4.findViewById(R.id.mImageText);
                                TextView mPriority = linearLayout4.findViewById(R.id.mPriority);
                                TextView mQuestionFor = linearLayout4.findViewById(R.id.mQuestionFor);
                                TextView mMainQuestion = linearLayout4.findViewById(R.id.mMainQuestion);

                                mStrQuestionType = mQuestionType.getText().toString();
                                mStrQuesionId = mQuesionId.getText().toString();
                                mStrQuesionServer = mQuesionServerId.getText().toString();
                                mStrAnswerId = mAnswerId.getText().toString();
                                mStrPriority = mPriority.getText().toString();
                                mStrComment = mEditComment.getText().toString();
                                mStrImageText = mImageText.getText().toString();
                                mStrQuestionFor = mQuestionFor.getText().toString();
                                mStrMainQuestion = mMainQuestion.getText().toString();

                                System.out.println("<><><><>mQuestionType " + mQuestionType.getText());
                                System.out.println("<><><><>mQuesionId " + mQuesionId.getText());
                                System.out.println("<><><><>mQuesionServerId " + mQuesionServerId.getText());
                                System.out.println("<><><><>mAnswerId " + mAnswerId.getText());
                                System.out.println("<><><><>mPriority " + mPriority.getText());
                               // System.out.println("<><><><>mEditComment " + mEditComment.getText());
                                System.out.println("<><><><>mImageText " + mImageText.getText());
                                System.out.println("<><><><>mQuestionFor " + mQuestionFor.getText());
                                System.out.println("<><><><>mMainQuestion " + mMainQuestion.getText());
                            }
                        }
                    }


                    if (i == 0) {
                        AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                        auditQuestionAnswer.setmStrAuditId(mAuditId);
                        auditQuestionAnswer.setmStrUserId(PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this));
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
                        auditQuestionAnswer.setmStrIsQuestionNormal("0");


                        if (mStrQuestionType.equals("1")) {
                            if (mStrAnsOption.equals("") || mStrAnsOption.length() <= 0) {
                                db.delete_tb_audit_question_answer_when_update(auditQuestionAnswer);
                            } else {
                                if (db.isNormalQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "0") == true) {
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
                                if (db.isNormalQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "0") == true) {
                                    ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                                    mAuditList = db.getAllSubmitedQuestionAnswerById(mStrQuesionServer, mAuditId, mExplanationID, "0");
                                    if (mAuditList.get(0).getmStrAnswerId().equals(mStrAnswerId)) {
                                        ArrayList<SubQuestionAnswer> mAuditListwe = new ArrayList<SubQuestionAnswer>();
                                        mAuditListwe = db.getSubQuestionAnsByQuestionId(mAuditId, mStrQuesionServer, mExplanationID, mStrAnswerId, "0");
                                        if (mAuditListwe.size() > 0) {
                                            System.out.println("####WEWEWE");
                                        } else {
                                            SaveSub(mStrQuesionServer, mStrAnswerId);
                                            //db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                        }
                                    } else {
                                        ArrayList<SubQuestionAnswer> mAudList = new ArrayList<SubQuestionAnswer>();
                                        mAudList = db.getSubQuestionAnsByCondition(mAuditId, mUserId, mStrQuesionServer, mStrAnswerId, mExplanationID);
                                        if (mAudList.size() > 0) {
                                            db.update_tb_audit_question_answer_normal(auditQuestionAnswer);
                                        } else {
                                            SaveSub(mStrQuesionServer, mStrAnswerId);
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
                                    mAuditList = db.getSubQuestionAnsByQuestionId(mAuditId, mStrQuesionServer, mExplanationID, mStrAnswerId, "0");
                                    System.out.println("<><>321<><>call " + mAuditList.size());
                                    if (mAuditList.size() > 0) {
                                        db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                    } else {
                                        SaveSub(mStrQuesionServer, mStrAnswerId);
                                        db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                    }
                                    //1. get sub question ans by question server id
                                    //2. qusetion ans and sub question ans match
                                    //3. ans same nothing or not match delete
                                    //db.insert_tb_audit_question_answer(auditQuestionAnswer);
                                }
                            }
                        }

                    } else {
                        SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                        subQuestionAnswer.setmStrQuestionFor(mStrQuestionFor);
                        subQuestionAnswer.setmStrMainQuestionId(mStrMainQuestion);
                        subQuestionAnswer.setmStrQuestionImage(mStrImageText);
                        subQuestionAnswer.setmStrQuestionExtraText(mStrComment);
                        subQuestionAnswer.setmStrQuestionPriority(mStrPriority);
                        subQuestionAnswer.setmStrAnswerId(mStrAnswerId);
                        subQuestionAnswer.setmStrQuestionServerId(mStrQuesionServer);
                        subQuestionAnswer.setmStrQuestionId(mStrQuesionId);
                        subQuestionAnswer.setmStrQuestionType(mStrQuestionType);
                        subQuestionAnswer.setmStrIsQuestionParent("1");
                        subQuestionAnswer.setmStrAnswerValue(mStrAnsOption);
                        subQuestionAnswer.setmStrAuditId(mAuditId);
                        subQuestionAnswer.setmSubLocationExplanationId(mExplanationID);
                        subQuestionAnswer.setmStrIsQuestionNormal("0");
                        subQuestionAnswer.setmStrUserId(PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this));

                        if (mStrQuestionType.equals("1")) {
                            if (mStrAnsOption.equals("") || mStrAnsOption.length() <= 0) {


                            } else {

                                if (db.isSubQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "0") == true) {

                                    ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                                    mAuditList = db.get_tb_audit_sub_question_ans(mStrQuesionServer, mAuditId, mExplanationID, "0");





                                    if (mStrAnswerId.equals(mAuditList.get(0).getmStrAnswerId())) {
                                        db.update_tb_audit_sub_question_answer(subQuestionAnswer);
                                        SaveSub(mStrQuesionServer,mStrQuestionFor);
                                    } else {
                                        SaveSub(mStrMainQuestion,mStrQuestionFor);
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    }
                                } else {
                                    if (db.isSubQuestionPresentByMainQuestionAndQuestionFor(mStrMainQuestion, mAuditId, mExplanationID, "0") == true) {
                                        SaveSub(mStrMainQuestion,mStrQuestionFor);
                                        //db.delete_tb_audit_sub_question_answer_by_main_question(mAuditId, mStrMainQuestion, mExplanationID, "1");
                                        //db.delete_tb_audit_sub_question_answer(mAuditId, mStrMainQuestion, mExplanationID, "1");
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    } else {
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    }




                                }


                            }

                        } else {
                            if (mStrAnswerId.equals("") || mStrAnswerId.length() <= 0) {
                            } else {
                                if (db.isSubQuestionAnswerSubmmited(mStrQuesionServer, mAuditId, mExplanationID, "0") == true) {
                                    ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                                    mAuditList = db.get_tb_audit_sub_question_ans(mStrQuesionServer, mAuditId, mExplanationID, "0");
                                    if (mStrAnswerId.equals(mAuditList.get(0).getmStrAnswerId())) {
                                        db.update_tb_audit_sub_question_answer(subQuestionAnswer);
                                        SaveSub(mStrQuesionServer,mStrQuestionFor);
                                    } else {
                                        SaveSub(mStrMainQuestion,mStrQuestionFor);
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    }
                                } else {
                                    if (db.isSubQuestionPresentByMainQuestionAndQuestionFor(mStrMainQuestion, mAuditId, mExplanationID, "0") == true) {
                                        SaveSub(mStrMainQuestion,mStrQuestionFor);
                                        //db.delete_tb_audit_sub_question_answer_by_main_question(mAuditId, mStrMainQuestion, mExplanationID, "1");
                                        //db.delete_tb_audit_sub_question_answer(mAuditId, mStrMainQuestion, mExplanationID, "1");
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    } else {
                                        db.insert_tb_audit_sub_question_answer(subQuestionAnswer);
                                    }




                                }

                            }
                        }
                    }
                }
                // finish();
            }
        }
        CommonUtils.mShowAlert(getString(R.string.mText_data_saved), ActivityMesurementQuestionForm.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);

    }

    public void SaveSub(String mStrMainQuestion,String mStrQuestionFor){
        ArrayList<SubQuestionAnswer> mQuestionList = new ArrayList<SubQuestionAnswer>();
        mQuestionList = db.sub_question_ans(mStrMainQuestion, mAuditId, mExplanationID,mStrQuestionFor);
        db.delete_answer_by_main_question(mAuditId,mStrMainQuestion,mStrQuestionFor,mExplanationID);
        if(mQuestionList.size()>0){
            System.out.println("<><>345<><> "+mQuestionList.get(0).getmStrMainQuestionId());
            System.out.println("<><>345<><> "+mQuestionList.get(0).getmStrQuestionFor());
            SaveSub(mQuestionList.get(0).getmStrQuestionServerId(),mQuestionList.get(0).getmStrAnswerId());
        }
    }


    @OnClick(R.id.mLayoutCancel)
    public void mLayoutCancel() {
        CommonUtils.closeKeyBoard(ActivityMesurementQuestionForm.this);
        finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesurement_ques_form);
        ButterKnife.bind(this);
        mStringArray = getResources().getStringArray(R.array.question_priority);
        db = new DatabaseHelper(ActivityMesurementQuestionForm.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStrQuestionId = bundle.getString("mStrQuestionId");
            mMainLocationID = bundle.getString("mStrMainLocationServer");
            mUserId = bundle.getString("mUserId");
            mAuditId = bundle.getString("mAuditId");
            mStrCountryId = bundle.getString("mStrCountryId");
            mStrLanguageId = bundle.getString("mStrLanguageId");
            mStrSubLocationServerId = bundle.getString("mStrSubLocationServerId");
            mExplanationTitle = bundle.getString("mExplanationTitle");
            mExplanationDesc = bundle.getString("mExplanationDesc");
            mExplanationImage = bundle.getString("mExplanationImage");
            mLayerID = bundle.getString("mLayerID");
            mLayerTitle = bundle.getString("mLayerTitle");
            mLayerDesc = bundle.getString("mLayerDesc");
            mStrLayerImage = bundle.getString("mStrLayerImage");
            mStrSubFolderId = bundle.getString("mStrSubFolderId");
            mStrSubFolderTitle = bundle.getString("mStrSubFolderTitle");
            mExplanationID = bundle.getString("mExplanationID");
        }


        mListQuestion = db.get_single_tb_audit_question(mAuditId, mUserId, mMainLocationID, mStrSubLocationServerId, "0", mStrQuestionId);
        if (mListQuestion.size() > 0) {
            mCreateViews();
        }


    }


    public View mCreateSubViews(String mMainQuesServerID,String mQuesFor) {
        View view = null;
        mListSubQuestion.clear();
        mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mMainQuesServerID, mQuesFor,mMainLocationID, mStrSubLocationServerId);
        for (int i = 0; i < mListSubQuestion.size(); i++) {
            LayoutInflater mInflater = LayoutInflater.from(ActivityMesurementQuestionForm.this);
            View convertView = null;
            final AuditSubQuestion mAuditSubQues = mListSubQuestion.get(i);
            if (mAuditSubQues.getmStrQuestionType().equals("1")) {
                /*Type:text*/
                convertView = mInflater.inflate(R.layout.view_question_edittext, null);
                TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                EditTextRegular mEditAnsOption = convertView.findViewById(R.id.mEditAnsOption);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
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
                final TextView mQuestionFor = convertView.findViewById(R.id.mQuestionFor);
                final TextView mMainQuestion = convertView.findViewById(R.id.mMainQuestion);

                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });

                /*set question-answer*/
                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());
                mQuestionType.setText(mAuditSubQues.getmStrQuestionType());
                mQuesionId.setText(mAuditSubQues.getmStrId());
                mQuestionFor.setText(mAuditSubQues.getmStrQuestionFor());
                mQuesionServerId.setText(mAuditSubQues.getmStrServerId());
                mMainQuestion.setText(mAuditSubQues.getmStrMainQuestion());
                mAnswerId.setText("");

                /*Question level spinner*/
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_blue);
                        }

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_sub_question_answer_by_questionServer_id(mAuditId, mAuditSubQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditAnsOption.setText(mAuditList.get(0).getmStrAnswerValue());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                }


                 /*getting & displaying image*/
                mImgCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageTake = mImgQues;
                        mMainImgCamera = mImgCamera;
                        mMainImgCancel = mImgCancel;
                        mTextQuesId = mAuditSubQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                view = convertView;
                mLayoutAddSubQuestion.addView(convertView);
            } else if (mAuditSubQues.getmStrQuestionType().equals("2")) {
                  /*Type:Radio*/
                convertView = mInflater.inflate(R.layout.view_question_radio_button, null);
                final View mView = convertView;
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                RadioGroup mRadioGroupOption = convertView.findViewById(R.id.mRadioGroupOption);
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
                final TextView mQuestionFor = convertView.findViewById(R.id.mQuestionFor);
                final TextView mMainQuestion = convertView.findViewById(R.id.mMainQuestion);

                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                String alredySelected = "";
                String mMainalredy = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_sub_question_answer_by_questionServer_id(mAuditId, mAuditSubQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainalredy = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());

                }


                mQuestionType.setText(mAuditSubQues.getmStrQuestionType());
                mQuesionId.setText(mAuditSubQues.getmStrId());
                mQuesionServerId.setText(mAuditSubQues.getmStrServerId());
                mQuestionFor.setText(mAuditSubQues.getmStrQuestionFor());
                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());
                mMainQuestion.setText(mAuditSubQues.getmStrMainQuestion());
                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());

                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = mAuditSubQues.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");

                String mAnswerOptionId = mAuditSubQues.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");
                for (int k = 0; k < separatedId.length; k++) {
                    if (separatedId[k].equals(alredySelected)) {
                        alredySelected = k + "";
                    }
                    mStrListAnswerId.put(separatedAns[k], separatedId[k]);
                }
                for (int j = 0; j < separatedAns.length; j++) {
                    RadioButton radioButton = new RadioButton(ActivityMesurementQuestionForm.this);
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
                        View view1 = null;
                        for (int i = 0; i < rg.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rg.getChildAt(i);
                            mAnswerId.setText(mStrListAnswerId.get(btn.getText()));
                            if (btn.getId() == checkedId) {
                                if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                                    //have sub quest new screen
                                    if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()), mAuditId)) {
                                        mListSubQuestion.clear();
                                        mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()),mMainLocationID, mStrSubLocationServerId);
                                        System.out.println("<><>##<><>## you you" +mListSubQuestion.size());
                                        if (mListSubQuestion.size() > 0) {
                                            int childCount = mLayoutAddSubQuestion.getChildCount();
                                            int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                            System.out.println("<>##<>childCount " + childCount);
                                            System.out.println("<>##<>indexValue " + indexValue);
                                            if (childCount > 1) {
                                                for (int w = indexValue+1; w < childCount; w++) {
                                                    mLayoutAddSubQuestion.removeViewAt(w);
                                                }
                                            }
                                            mCreateSubViews(mAuditSubQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()));
                                        }
                                    } else {
                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                        System.out.println("<>##<>childCount " + childCount);
                                        System.out.println("<>##<>indexValue " + indexValue);
                                        if (childCount > 1) {
                                            for (int w = indexValue+1; w < childCount; w++) {



                                                // System.out.println("<>##<>  ww " + w);
                                                //System.out.println("<>##<>  count " + mLayoutAddSubQuestion.getChildCount());
                                                mLayoutAddSubQuestion.removeViewAt(indexValue+1);





                                            }

                                            //mLayoutAddSubQuestion.removeViewAt(1);
                                        }
                                    }
                                }
                                return;
                            }
                        }
                    }
                });

                /*Question level spinner*/

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        mTextQuesId = mAuditSubQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                view = convertView;
                mLayoutAddSubQuestion.addView(convertView);

                if (mAuditList.size() > 0) {

                    if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                        //have sub quest new screen
                        if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(),mAnswerId.getText().toString(), mAuditId)) {
                            mCreateSubViews(mAuditSubQues.getmStrServerId(),mAnswerId.getText().toString());
                        }
                    }






                }


            } else if (mAuditSubQues.getmStrQuestionType().equals("3")) {
                  /*Type:Checkbox*/
                convertView = mInflater.inflate(R.layout.view_question_checkbox, null);
                LinearLayout mLayoutCheckOption = convertView.findViewById(R.id.mLayoutCheckOption);
                TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final Spinner mSpinnerQuesLevel = convertView.findViewById(R.id.mSpinnerQuesLevel);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
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
                final TextView mQuestionFor = convertView.findViewById(R.id.mQuestionFor);
                final TextView mMainQuestion = convertView.findViewById(R.id.mMainQuestion);

                mEditComment.setVisibility(View.VISIBLE);
                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });

                final ArrayList<String> listOfSelectedCheckBoxId = new ArrayList<>();
                String alredySelected = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_sub_question_answer_by_questionServer_id(mAuditId, mAuditSubQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());


                }


                mQuestionType.setText(mAuditSubQues.getmStrQuestionType());
                mQuesionId.setText(mAuditSubQues.getmStrId());
                mQuesionServerId.setText(mAuditSubQues.getmStrServerId());
                mQuestionFor.setText(mAuditSubQues.getmStrQuestionFor());
                mMainQuestion.setText(mAuditSubQues.getmStrMainQuestion());

                /*set question-answer*/


                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());
                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = mAuditSubQues.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");
                String mAnswerOptionId = mAuditSubQues.getmStrAnswerOptionId();
                String[] separatedId = mAnswerOptionId.split("#");
                for (int j = 0; j < separatedAns.length; j++) {
                    CheckBox checkBox = new CheckBox(ActivityMesurementQuestionForm.this);
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
                                checkBox.setChecked(true);
                                listOfSelectedCheckBoxId.add(mStrListAnswerId.get(separatedAns[finalJ]));
                            } else {
                                checkBox.setChecked(false);
                                listOfSelectedCheckBoxId.remove(mStrListAnswerId.get(separatedAns[finalJ]));
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
                            } else {
                                System.out.println("<><><>ans " + sb.toString());
                                mAnswerId.setText(sb.toString());
                            }
                        }
                    });
                }
                /*Question level spinner*/


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        mTextQuesId = mAuditSubQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                view = convertView;
                mLayoutAddSubQuestion.addView(convertView);
            } else if (mAuditSubQues.getmStrQuestionType().equals("4")) {
                  /*Type:dropdown*/
                convertView = mInflater.inflate(R.layout.view_question_dropdown, null);
                final View mView = convertView;
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                final Spinner mSpinnerQuesOption = convertView.findViewById(R.id.mSpinnerQuesOption);
                final RelativeLayout mLayoutQuesLevel = convertView.findViewById(R.id.mLayoutQuesLevel);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
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
                final TextView mQuestionFor = convertView.findViewById(R.id.mQuestionFor);
                final TextView mMainQuestion = convertView.findViewById(R.id.mMainQuestion);
                final RelativeLayout mViewSpiner = convertView.findViewById(R.id.mViewSpiner);

                mEditComment.setVisibility(View.VISIBLE);

                String alredySelected = "";
                String mMainAlredy = "";

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_sub_question_answer_by_questionServer_id(mAuditId, mAuditSubQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainAlredy = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());


                }

                mQuestionType.setText(mAuditSubQues.getmStrQuestionType());
                mQuesionId.setText(mAuditSubQues.getmStrId());
                mQuesionServerId.setText(mAuditSubQues.getmStrServerId());
                mQuestionFor.setText(mAuditSubQues.getmStrQuestionFor());
                mMainQuestion.setText(mAuditSubQues.getmStrMainQuestion());

                /*set question-answer*/
                final Map<String, String> mStrListAnswerId = new HashMap<>();
                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt().replace("#", "*"));
                String mAnswerOption = mAuditSubQues.getmStrAnswerOption();
                final ArrayList<AuditQuestion> ansList = new ArrayList<>();
                String[] separatedAns = mAnswerOption.split("#");
                List<String> listAns = new LinkedList<String>(Arrays.asList(separatedAns));
                listAns.add(0, "Select Option");
                separatedAns = listAns.toArray(new String[listAns.size()]);
                String mAnswerOptionId = mAuditSubQues.getmStrAnswerOptionId();
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

                ArrayAdapter<AuditQuestion> spinnerQuesAdapter = new ArrayAdapter<AuditQuestion>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, ansList);
                spinnerQuesAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesOption.setAdapter(spinnerQuesAdapter);


                if (alredySelected.length() <= 0) {

                } else {
                    System.out.println("<><><><>ss " + Integer.parseInt(alredySelected));
                    mSpinnerQuesOption.setSelection(Integer.parseInt(alredySelected));
                }

                if (alredySelected.length() <= 0) {

                } else {
                    System.out.println("<><><><>ss " + Integer.parseInt(alredySelected));
                    mSpinnerQuesOption.setSelection(Integer.parseInt(alredySelected), false);

                    if(Integer.parseInt(alredySelected)==0){
                        mSpinnerQuesOption.setBackgroundResource(R.color.transparent);
                        mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                    }else {
                        mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                    }

                   /* if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                        //have sub quest new screen
                        if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(),mAnswerId.getText().toString(), mAuditId)) {
                            mCreateViews(mAuditSubQues.getmStrServerId(),mAuditSubQues.getmStrQuestionFor());
                        }
                    }*/

                }


                mSpinnerQuesOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        AuditQuestion auditQuestion = ansList.get(pos);
                        String mSelectedTitle = auditQuestion.getmStrAnswerOption();
                        mAnswerId.setText(mStrListAnswerId.get(mSelectedTitle));


                        if (!mSelectedTitle.equals("Select Option")) {
                            mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                            mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                            if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                                //have sub quest new screen
                                if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), auditQuestion.getmStrAnswerOptionId(), mAuditId)) {
                                    mListSubQuestion.clear();
                                    mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), auditQuestion.getmStrAnswerOptionId(),mMainLocationID, mStrSubLocationServerId);
                                    System.out.println("<><>##<><>## you you" +mListSubQuestion.size());
                                    if (mListSubQuestion.size() > 0) {
                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                        System.out.println("<>##<>childCount " + childCount);
                                        System.out.println("<>##<>indexValue " + indexValue);
                                        if (childCount > 1) {
                                            for (int w = indexValue+1; w < childCount; w++) {
                                                mLayoutAddSubQuestion.removeViewAt(w);
                                            }
                                        }
                                        mCreateSubViews(mAuditSubQues.getmStrServerId(),auditQuestion.getmStrAnswerOptionId());
                                    }
                                } else {
                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                    System.out.println("<>##<>childCount " + childCount);
                                    System.out.println("<>##<>indexValue " + indexValue);
                                    if (childCount > 1) {
                                        for (int w = indexValue+1; w < childCount; w++) {



                                            // System.out.println("<>##<>  ww " + w);
                                            //System.out.println("<>##<>  count " + mLayoutAddSubQuestion.getChildCount());
                                            mLayoutAddSubQuestion.removeViewAt(indexValue+1);





                                        }

                                        //mLayoutAddSubQuestion.removeViewAt(1);
                                    }
                                }
                            }


                        } else {
                            mSpinnerQuesOption.setBackgroundResource(R.color.transparent);
                            mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_archive_button);

                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                            System.out.println("<>##<>childCount " + childCount);
                            System.out.println("<>##<>indexValue " + indexValue);
                            if (childCount > 1) {
                                for (int w = indexValue+1; w < childCount; w++) {



                                    // System.out.println("<>##<>  ww " + w);
                                    //System.out.println("<>##<>  count " + mLayoutAddSubQuestion.getChildCount());
                                    mLayoutAddSubQuestion.removeViewAt(indexValue+1);





                                }

                                //mLayoutAddSubQuestion.removeViewAt(1);
                            }


                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
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
                        mTextQuesId = mAuditSubQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                view = convertView;
                mLayoutAddSubQuestion.addView(convertView);


                if (mAuditList.size() > 0) {

                    if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                        //have sub quest new screen
                        if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(),mAnswerId.getText().toString(), mAuditId)) {
                            mCreateSubViews(mAuditSubQues.getmStrServerId(),mAnswerId.getText().toString());
                        }
                    }






                }



            } else if (mAuditSubQues.getmStrQuestionType().equals("5")) {
                /*Type:popUp*/
               /* int mIntLayout = Integer.parseInt(mStrMainAns);
                for (int k = 0; k < mIntLayout; k++) {
                    convertView = mInflater.inflate(R.layout.view_question_popup, null);
                    TextViewSemiBold mTxtQuestion = (TextViewSemiBold) convertView.findViewById(R.id.mTxtQuestion);
                    LinearLayout mOptionLayout = (LinearLayout) convertView.findViewById(R.id.mOptionLayout);
                   *//*set question-answer*//*
                    mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());
                    Map<String, String> mStrListAnswerId = new HashMap<>();
                    String mAnswerOption = mAuditSubQues.getmStrAnswerOption();
                    final String[] separatedAns = mAnswerOption.split("#");


                    for (int j = 0; j < separatedAns.length; j++) {
                        LinearLayout mFlowView = (LinearLayout) View.inflate(ActivityMesurementQuestionForm.this, R.layout.view_ques_popup_option_item, null);
                        TextViewSemiBold mTxtMeasurement = (TextViewSemiBold) mFlowView.findViewById(R.id.mTxtMeasurement);
                        EditTextRegular mEditAnswer = (EditTextRegular) mFlowView.findViewById(R.id.mEditAnswer);
                        mTxtMeasurement.setText(separatedAns[j]);
                        mOptionLayout.addView(mFlowView);
                    }

                    String mAnswerOptionId = mAuditSubQues.getmStrAnswerOptionId();
                    String[] separatedId = mAnswerOptionId.split("#");
                    for (int j = 0; j < separatedId.length; j++) {
                        mStrListAnswerId.put(separatedAns[j], separatedId[j]);
                    }
                    mLayoutAddSubQuestion.addView(convertView);
                }*/
            } else if (mAuditSubQues.getmStrQuestionType().equals("6")) {
                  /*Type:True-false*/
                convertView = mInflater.inflate(R.layout.view_question_yes_no, null);
                final RelativeLayout mLayoutYes = convertView.findViewById(R.id.mLayoutYes);
                final RelativeLayout mLayoutNo = convertView.findViewById(R.id.mLayoutNo);
                final TextViewRegular mTextYes = convertView.findViewById(R.id.mTextYes);
                final TextViewRegular mTextNo = convertView.findViewById(R.id.mTextNo);
                final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
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
                final TextView mQuestionFor = convertView.findViewById(R.id.mQuestionFor);
                final TextView mMainQuestion = convertView.findViewById(R.id.mMainQuestion);

                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                String alredySelected = "";
                String mMainalredy = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_sub_question_answer_by_questionServer_id(mAuditId, mAuditSubQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainalredy = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());

                }
                mQuestionType.setText(mAuditSubQues.getmStrQuestionType());
                mQuesionId.setText(mAuditSubQues.getmStrId());
                mQuesionServerId.setText(mAuditSubQues.getmStrServerId());
                mQuestionFor.setText(mAuditSubQues.getmStrQuestionFor());
                mMainQuestion.setText(mAuditSubQues.getmStrMainQuestion());




                /*set question-answer*/
                mTxtQuestion.setText(mAuditSubQues.getmStrQuestionTxt());
                final Map<String, String> mStrListAnswerId = new HashMap<>();
                String mAnswerOption = mAuditSubQues.getmStrAnswerOption();
                final String[] separatedAns = mAnswerOption.split("#");

                String mAnswerOptionId = mAuditSubQues.getmStrAnswerOptionId();
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
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_button_red);
                    } else if (separatedId[1].equals(alredySelected)) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextNo.getText().toString()));
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_signin_button);
                    }

                }
                mLayoutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextYes.getText().toString()));
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_button_red);
                        if (mAuditSubQues.getmStrSubQuestion().equals("1")) {


                            if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                                //have sub quest new screen
                                if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()), mMainLocationID)) {
                                    mListSubQuestion.clear();
                                    mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()), mMainLocationID, mStrSubLocationServerId);
                                    if (mListSubQuestion.size() > 0) {
                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        System.out.println("<><><><>childCount0 " + childCount);
                                        if (childCount > 2) {
                                            mLayoutAddSubQuestion.removeViewAt(2);
                                        }
                                        mCreateSubViews(mAuditSubQues.getmStrServerId(),mAuditSubQues.getmStrQuestionFor());
                                    }
                                } else {
                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount > 2) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                    }
                                }
                            }


                            //have sub quest new screen
                            /*if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()))) {
                                mListSubQuestion.clear();
                                mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()));
                                if (mListSubQuestion.size() > 0) {
                                    mCreateViews();
                                }

                            }*//* else {
                                 int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount > 1) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
                            }*/
                        } /*else {
                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            System.out.println("<><><><>childCount1 " + childCount);
                            if (childCount > 1) {
                                //mLayoutAddSubQuestion.removeViewAt(1);
                            }
                        }*/
                    }
                });

                mLayoutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerId.setText(mStrListAnswerId.get(mTextNo.getText().toString()));
                        mLayoutYes.setBackgroundResource(R.drawable.rounded_corner_layer_darkgrey);
                        mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_signin_button);
                        if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                            if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                                //have sub quest new screen
                                if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()), mMainLocationID)) {
                                    mListSubQuestion.clear();
                                    mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()), mMainLocationID, mStrSubLocationServerId);
                                    if (mListSubQuestion.size() > 0) {
                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        System.out.println("<><><><>childCount0 " + childCount);
                                        if (childCount > 2) {
                                            mLayoutAddSubQuestion.removeViewAt(2);
                                        }
                                        mCreateSubViews(mAuditSubQues.getmStrServerId(),mAuditSubQues.getmStrQuestionFor());
                                    }
                                } else {
                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount > 2) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                    }
                                }
                            }


                            //have sub quest new screen
                            /*if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()))) {
                                mListSubQuestion.clear();
                                mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()));
                                if (mListSubQuestion.size() > 0) {
                                    mCreateViews();
                                }
                            }*/ /*else {
                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount > 1) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
                            }*/
                        } /*else {
                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            System.out.println("<><><><>childCount1 " + childCount);
                            if (childCount > 1) {
                                //mLayoutAddSubQuestion.removeViewAt(1);
                            }
                        }*/
                    }
                });


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        mTextQuesId = mAuditSubQues.getmStrId();
                        mQuesTextViewImage = mImageText;
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                view = convertView;
                mLayoutAddSubQuestion.addView(convertView);


                if (mAuditList.size() > 0) {
                    if (mAuditSubQues.getmStrSubQuestion().equals("1")) {
                        //have sub quest new screen
                        if (db.isSubQuestionForOption(mAuditSubQues.getmStrServerId(), mMainalredy, mMainLocationID)) {
                            mListSubQuestion.clear();
                            mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, PreferenceManager.getFormiiId(ActivityMesurementQuestionForm.this), mAuditSubQues.getmStrServerId(), mMainalredy, mMainLocationID, mStrSubLocationServerId);
                            if (mListSubQuestion.size() > 0) {

                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount > 2) {
                                    mLayoutAddSubQuestion.removeViewAt(2);
                                }

                                mCreateSubViews(mAuditSubQues.getmStrServerId(),mAuditSubQues.getmStrQuestionFor());
                            }
                        } else {
                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            System.out.println("<><><><>childCount0 " + childCount);
                            if (childCount > 2) {
                                mLayoutAddSubQuestion.removeViewAt(2);
                            }
                        }
                    }
                }


            }
        }
        return view;
    }


    public void mCreateViews() {
        for (int i = 0; i < mListQuestion.size(); i++) {
            System.out.println("<><>mListQuestion" + mListQuestion.get(i).getmStrQuestionType());
            LayoutInflater mInflater = LayoutInflater.from(ActivityMesurementQuestionForm.this);
            View convertView = null;
            final AuditQuestion mAuditQues = mListQuestion.get(i);
            if (mAuditQues.getmStrQuestionType().equals("1")) {
                /*Type:text*/
                convertView = mInflater.inflate(R.layout.view_question_edittext, null);
                TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
                EditTextRegular mEditAnsOption = convertView.findViewById(R.id.mEditAnsOption);
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


                mEditComment.setVisibility(View.VISIBLE);

                /*set question-answer*/
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt());
                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mAnswerId.setText("");


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);


                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                   mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mEditAnsOption.setText(mAuditList.get(0).getmStrAnswerValue());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                }



                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });







                /*Question level spinner*/

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                        mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                        mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                        mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                mLayoutAddSubQuestion.addView(convertView);
            } else if (mAuditQues.getmStrQuestionType().equals("2")) {
                  /*Type:Radio*/
                convertView = mInflater.inflate(R.layout.view_question_radio_button, null);
                final View mView = convertView;
                final TextViewSemiBold mTxtQuestion = convertView.findViewById(R.id.mTxtQuestion);
               final EditTextRegular mEditComment = convertView.findViewById(R.id.mEditComment);
                final RadioGroup mRadioGroupOption = convertView.findViewById(R.id.mRadioGroupOption);
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

                mEditComment.setVisibility(View.VISIBLE);
                String alredySelected = "";
                String mMainAlredy = "";

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    System.out.println("<><><><>007 ");
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                   mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }
                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainAlredy = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());
                }


                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt());

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
                    radioButton = new RadioButton(ActivityMesurementQuestionForm.this);
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

/*here....*/
                mRadioGroupOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup rg, int checkedId) {
                        for (int i = 0; i < rg.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rg.getChildAt(i);
                            mAnswerId.setText(mStrListAnswerId.get(btn.getText()));
                            if (btn.getId() == checkedId) {
                                if (mAuditQues.getmStrSubQuestion().equals("1")) {

                                    if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()),mAuditId)) {
                                        mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()), mMainLocationID, mStrSubLocationServerId);
                                        System.out.println("<><><><>mCall02");
                                        if (mListSubQuestion.size() > 0) {
                                          /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                            System.out.println("<><><><>childCount0 " + childCount);
                                            if (childCount == 2) {
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            } else if (childCount == 3) {
                                                mLayoutAddSubQuestion.removeViewAt(2);
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            }
                                            System.out.println("<><><><>mCall03");*/

                                            int childCount = mLayoutAddSubQuestion.getChildCount();
                                            int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                            System.out.println("<>##<>childCount " + childCount);
                                            System.out.println("<>##<>indexValue " + indexValue);
                                            if (childCount > 1) {
                                                for (int w = indexValue+1; w < childCount; w++) {
                                                    View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                                    mLayoutAddSubQuestion.removeView(currentChild);
                                                }
                                            }

                                            mCreateSubViews(mAuditQues.getmStrServerId(),mStrListAnswerId.get(btn.getText()));
                                        }

                                    } else {



                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                        System.out.println("<>##<>childCount " + childCount);
                                        System.out.println("<>##<>indexValue " + indexValue);
                                        if (childCount > 1) {
                                            for (int w = indexValue+1; w < childCount; w++) {
                                                View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                                mLayoutAddSubQuestion.removeView(currentChild);
                                            }
                                        }


                                      /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                        System.out.println("<><><><>childCount0 " + childCount);
                                        if (childCount == 2) {
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        } else if (childCount == 3) {
                                            mLayoutAddSubQuestion.removeViewAt(2);
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        }*/

                                    }
                                }
                                return;
                            }
                        }
                    }
                });

                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                mLayoutAddSubQuestion.addView(convertView);

                if (mAuditList.size() > 0) {
                    if (mAuditQues.getmStrSubQuestion().equals("1")) {
                        if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(),mMainAlredy,mAuditId)) {
                            mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(),mMainAlredy, mMainLocationID, mStrSubLocationServerId);
                            System.out.println("<><><><>mCall02");
                            if (mListSubQuestion.size() > 0) {
                                          /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                            System.out.println("<><><><>childCount0 " + childCount);
                                            if (childCount == 2) {
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            } else if (childCount == 3) {
                                                mLayoutAddSubQuestion.removeViewAt(2);
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            }
                                            System.out.println("<><><><>mCall03");*/

                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                System.out.println("<>##<>childCount " + childCount);
                                System.out.println("<>##<>indexValue " + indexValue);
                                if (childCount > 1) {
                                    for (int w = indexValue+1; w < childCount; w++) {
                                        View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                        mLayoutAddSubQuestion.removeView(currentChild);
                                    }
                                }

                                mCreateSubViews(mAuditQues.getmStrServerId(),mMainAlredy);
                            }

                        } else {



                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                            System.out.println("<>##<>childCount " + childCount);
                            System.out.println("<>##<>indexValue " + indexValue);
                            if (childCount > 1) {
                                for (int w = indexValue+1; w < childCount; w++) {
                                    View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                    mLayoutAddSubQuestion.removeView(currentChild);
                                }
                            }


                                      /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                        System.out.println("<><><><>childCount0 " + childCount);
                                        if (childCount == 2) {
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        } else if (childCount == 3) {
                                            mLayoutAddSubQuestion.removeViewAt(2);
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        }*/

                        }
                    }
                }


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

                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });



                final ArrayList<String> listOfSelectedCheckBoxId = new ArrayList<>();

                String mAnswerOption = mAuditQues.getmStrAnswerOption();
                String mAnswerOptionId = mAuditQues.getmStrAnswerOptionId();

                String alredySelected = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
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

                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt());
                final Map<String, String> mStrListAnswerId = new HashMap<>();

                final String[] separatedAns = mAnswerOption.split("#");


                String[] separatedId = mAnswerOptionId.split("#");

                for (int j = 0; j < separatedAns.length; j++) {
                    CheckBox checkBox = new CheckBox(ActivityMesurementQuestionForm.this);
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
                                checkBox.setChecked(true);
                                listOfSelectedCheckBoxId.add(mStrListAnswerId.get(separatedAns[finalJ]));
                            } else {
                                checkBox.setChecked(false);
                                listOfSelectedCheckBoxId.remove(mStrListAnswerId.get(separatedAns[finalJ]));
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

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                mLayoutAddSubQuestion.addView(convertView);
            } else if (mAuditQues.getmStrQuestionType().equals("4")) {
                  /*Type:dropdown*/
                convertView = mInflater.inflate(R.layout.view_question_dropdown, null);
                final View mView = convertView;
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
                final RelativeLayout mViewSpiner = convertView.findViewById(R.id.mViewSpiner);
                final TextView mAnswerId = convertView.findViewById(R.id.mAnswerId);
                TextView mQuesionId = convertView.findViewById(R.id.mQuesionId);
                TextView mQuesionServerId = convertView.findViewById(R.id.mQuesionServerId);
                final TextView mPriority = convertView.findViewById(R.id.mPriority);
                /*set question-answer*/


                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());

                /*set question-answer*/
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt());
                String mAnswerOption = mAuditQues.getmStrAnswerOption();


                String alredySelected = "";
                String mMainAlredy = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainAlredy = mAuditList.get(0).getmStrAnswerId();
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

                ArrayAdapter<AuditQuestion> spinnerQuesAdapter = new ArrayAdapter<AuditQuestion>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, ansList.toArray(new AuditQuestion[ansList.size()]));
                spinnerQuesAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesOption.setAdapter(spinnerQuesAdapter);

                if (alredySelected.length() <= 0) {

                } else {
                    System.out.println("<><><><>ss " + Integer.parseInt(alredySelected));
                    mSpinnerQuesOption.setSelection(Integer.parseInt(alredySelected));
                    mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                    mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
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
                                System.out.println("<><><><>mCall01");
                                //have sub quest new screen
                                if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), auditQuestion.getmStrAnswerOptionId(),mAuditId)) {
                                    mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(), auditQuestion.getmStrAnswerOptionId(), mMainLocationID, mStrSubLocationServerId);
                                    System.out.println("<><><><>mCall02");
                                    if (mListSubQuestion.size() > 0) {
                                        int childCount = mLayoutAddSubQuestion.getChildCount();
                                        int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                        System.out.println("<>##<>childCount " + childCount);
                                        System.out.println("<>##<>indexValue " + indexValue);
                                        if (childCount > 1) {
                                            for (int w = indexValue+1; w < childCount; w++) {
                                                View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                                mLayoutAddSubQuestion.removeView(currentChild);
                                            }
                                        }
                                        mCreateSubViews(mAuditQues.getmStrServerId(),auditQuestion.getmStrAnswerOptionId());
                                    }

                                } else {

                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                    System.out.println("<>##<>childCount " + childCount);
                                    System.out.println("<>##<>indexValue " + indexValue);
                                    if (childCount > 1) {
                                        for (int w = indexValue+1; w < childCount; w++) {
                                            View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                            mLayoutAddSubQuestion.removeView(currentChild);
                                        }
                                    }

                                }

                            }
                        }else {
                            mSpinnerQuesOption.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                            mViewSpiner.setBackgroundResource(R.drawable.rounded_corner_archive_button);
                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            System.out.println("<><><><>childCount0 " + childCount);
                            if (childCount == 2) {
                                mLayoutAddSubQuestion.removeViewAt(1);
                            } else if (childCount == 3) {
                                mLayoutAddSubQuestion.removeViewAt(2);
                                mLayoutAddSubQuestion.removeViewAt(1);
                            }
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

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                mLayoutAddSubQuestion.addView(convertView);

                if (mAuditList.size() > 0) {
                    if (mAuditQues.getmStrSubQuestion().equals("1")) {
                        if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(),mMainAlredy,mAuditId)) {
                            mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(),mMainAlredy, mMainLocationID, mStrSubLocationServerId);
                            System.out.println("<><><><>mCall02");
                            if (mListSubQuestion.size() > 0) {
                                          /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                            System.out.println("<><><><>childCount0 " + childCount);
                                            if (childCount == 2) {
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            } else if (childCount == 3) {
                                                mLayoutAddSubQuestion.removeViewAt(2);
                                                mLayoutAddSubQuestion.removeViewAt(1);
                                            }
                                            System.out.println("<><><><>mCall03");*/

                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                                System.out.println("<>##<>childCount " + childCount);
                                System.out.println("<>##<>indexValue " + indexValue);
                                if (childCount > 1) {
                                    for (int w = indexValue+1; w < childCount; w++) {
                                        View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                        mLayoutAddSubQuestion.removeView(currentChild);
                                    }
                                }

                                mCreateSubViews(mAuditQues.getmStrServerId(),mMainAlredy);
                            }

                        } else {



                            int childCount = mLayoutAddSubQuestion.getChildCount();
                            int indexValue = mLayoutAddSubQuestion.indexOfChild(mView);
                            System.out.println("<>##<>childCount " + childCount);
                            System.out.println("<>##<>indexValue " + indexValue);
                            if (childCount > 1) {
                                for (int w = indexValue+1; w < childCount; w++) {
                                    View currentChild = mLayoutAddSubQuestion.getChildAt(1);
                                    mLayoutAddSubQuestion.removeView(currentChild);
                                }
                            }


                                      /*  int childCount = mLayoutAddSubQuestion.getChildCount();
                                        System.out.println("<><><><>childCount0 " + childCount);
                                        if (childCount == 2) {
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        } else if (childCount == 3) {
                                            mLayoutAddSubQuestion.removeViewAt(2);
                                            mLayoutAddSubQuestion.removeViewAt(1);
                                        }*/

                        }

                    }
                }


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

                mEditComment.setVisibility(View.VISIBLE);

                mEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funConfirmationBeforEdit(mEditComment);
                    }
                });


                String alredySelected = "";
                String mMainAlredy = "";

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ActivityMesurementQuestionForm.this, R.layout.spinner_queslevel_item, mStringArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_queslevel_item);
                mSpinnerQuesLevel.setAdapter(spinnerArrayAdapter);

                ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
                mAuditList = db.get_all_tb_audit_question_answer_normal(mAuditId, mAuditQues.getmStrServerId(), mExplanationID, "0");
                if (mAuditList.size() > 0) {
                    mPriority.setText(mAuditList.get(0).getmStrQuestionPriority());
                    mEditComment.setText(mAuditList.get(0).getmStrQuestionExtraText());
                    mSpinnerQuesLevel.setSelection(Arrays.asList(mStringArray).indexOf(mAuditList.get(0).getmStrQuestionPriority()));
                    if (TextUtils.isEmpty(mAuditList.get(0).getmStrQuestionImage()) || mAuditList.get(0).getmStrQuestionImage() == null) {
                        mImgCancel.setVisibility(View.GONE);
                        mImgQues.setVisibility(View.GONE);
                        mImgCamera.setVisibility(View.VISIBLE);
                    } else {
                        mImageText.setText(mAuditList.get(0).getmStrQuestionImage());
                        Glide.with(ActivityMesurementQuestionForm.this).load(mAuditList.get(0).getmStrQuestionImage()).into(mImgQues);
                        mImgCancel.setVisibility(View.VISIBLE);
                        mImgQues.setVisibility(View.VISIBLE);
                        mImgCamera.setVisibility(View.GONE);
                    }

                    alredySelected = mAuditList.get(0).getmStrAnswerId();
                    mMainAlredy = mAuditList.get(0).getmStrAnswerId();
                    mAnswerId.setText(mAuditList.get(0).getmStrAnswerId());

                }

                mQuestionType.setText(mAuditQues.getmStrQuestionType());
                mQuesionId.setText(mAuditQues.getmStrId());
                mQuesionServerId.setText(mAuditQues.getmStrServerId());
                mTxtQuestion.setText(mAuditQues.getmStrQuestionTxt());

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
                            if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()), mMainLocationID)) {
                                mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextYes.getText().toString()), mMainLocationID, mStrSubLocationServerId);
                                if (mListSubQuestion.size() > 0) {


                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount == 2) {
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    } else if (childCount == 3) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    }
                                    mCreateSubViews(mAuditQues.getmStrServerId(),mStrListAnswerId.get(mTextYes.getText().toString()));
                                } else {

                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount == 2) {
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    } else if (childCount == 3) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    }

                                }


                            } else {
                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount == 2) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                } else if (childCount == 3) {
                                    mLayoutAddSubQuestion.removeViewAt(2);
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
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

                            //have sub quest new screen
                            if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()), mMainLocationID)) {
                                mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(), mStrListAnswerId.get(mTextNo.getText().toString()), mMainLocationID, mStrSubLocationServerId);
                                if (mListSubQuestion.size() > 0) {
                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount == 2) {
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    } else if (childCount == 3) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    }
                                    mCreateSubViews(mAuditQues.getmStrServerId(),mStrListAnswerId.get(mTextNo.getText().toString()));
                                } else {
                                    int childCount = mLayoutAddSubQuestion.getChildCount();
                                    System.out.println("<><><><>childCount0 " + childCount);
                                    if (childCount == 2) {
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    } else if (childCount == 3) {
                                        mLayoutAddSubQuestion.removeViewAt(2);
                                        mLayoutAddSubQuestion.removeViewAt(1);
                                    }
                                }

                            } else {
                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount == 2) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                } else if (childCount == 3) {
                                    mLayoutAddSubQuestion.removeViewAt(2);
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
                            }


                        }
                    }
                });


                mSpinnerQuesLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String mSelectedTitle = mSpinnerQuesLevel.getSelectedItem().toString();
                        mPriority.setText(mSelectedTitle);

                        if (mSelectedTitle.equals(getString(R.string.mTextFile_high))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_red);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_medium))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_orange_yellow);
                        }else if (mSelectedTitle.equals(getString(R.string.mTextFile_low))){
                            mLayoutQuesLevel.setBackgroundResource(R.drawable.rounded_corner_layer_spinner_green);
                        }else {
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
                        new CameraPreviewIntent(ActivityMesurementQuestionForm.this)
                                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_imagename))
                                .setEditorIntent(
                                        new PhotoEditorIntent(ActivityMesurementQuestionForm.this)
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
                mLayoutAddSubQuestion.addView(convertView);

                if (mAuditList.size() > 0) {
                    if (mAuditQues.getmStrSubQuestion().equals("1")) {
                        if (db.isSubQuestionForOption(mAuditQues.getmStrServerId(), mMainAlredy, mMainLocationID)) {
                            mListSubQuestion = db.get_all_tb_Sub_question(mAuditId, mUserId, mAuditQues.getmStrServerId(), mMainAlredy, mMainLocationID, mStrSubLocationServerId);
                            if (mListSubQuestion.size() > 0) {
                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount == 2) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                } else if (childCount == 3) {
                                    mLayoutAddSubQuestion.removeViewAt(2);
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
                                mCreateSubViews(mAuditQues.getmStrServerId(),mMainAlredy);
                            } else {
                                int childCount = mLayoutAddSubQuestion.getChildCount();
                                System.out.println("<><><><>childCount0 " + childCount);
                                if (childCount == 2) {
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                } else if (childCount == 3) {
                                    mLayoutAddSubQuestion.removeViewAt(2);
                                    mLayoutAddSubQuestion.removeViewAt(1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* / getting result for image from gallery or camera /*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_SINGLE_RESULT) {
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
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
            mQuesTextViewImage.setText(mStrImagePath);
            mImageMap.put(mTextQuesId, mStrImagePath);
            Glide.with(ActivityMesurementQuestionForm.this).load(mStrImagePath).into(mImageTake);
        }
    }

    public void funConfirmationBeforEdit(final EditTextRegular mExtraText){
        final Dialog dialog = new Dialog(ActivityMesurementQuestionForm.this, R.style.Theme_Dialog);
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
}
