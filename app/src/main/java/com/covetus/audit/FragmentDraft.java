package com.covetus.audit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.DraftList;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditInspectorQuestion;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_GET_SET.SubQuestionAnswer;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.ButterKnife;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.showPDialog;
import static Modal.AuditListModal.funGetAllAudit;
import static Modal.AuditListModal.funUpdateAudit;
import static Modal.SubLocationLayer.getAllExplanationImageFinal;
import static Modal.SubLocationLayer.update_tb_image_sub_location_explation_list_by_layer_id;

public class FragmentDraft extends Fragment {


    public static ArrayList<Audit> mListItems = new ArrayList<>();
    public static ArrayList<AuditQuestionAnswer> mAuditMainQuestion = new ArrayList<AuditQuestionAnswer>();
    public static ArrayList<String> mImageList = new ArrayList<String>();
    public static DraftList audittList;
    public static ListView mListChat;
    public static DatabaseHelper db;
    public static Activity activity;
    public static String mStrUserId;
    public static int mReorderStatus = 0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft_list, container, false);
        ButterKnife.bind(this, view);
        mStrUserId = PreferenceManager.getFormiiId(getActivity());
        mListChat = view.findViewById(R.id.mListChat);
        activity = getActivity();

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
        mReorderStatus = bundle.getInt("mReorderStatus");
        }


        db = new DatabaseHelper(getActivity());
        mListItems = funGetAllAudit(mStrUserId, "1", "", "1", db);
        if (mListItems.size() > 0) {
        audittList = new DraftList(getActivity(), mListItems);
        mListChat.setAdapter(audittList);
        } else {
        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found), getActivity());
        }


        return view;
    }



    public static void reload() {
        System.out.println("<><><><>##call");
        mListItems.clear();
        db = new DatabaseHelper(activity);
        /* database data into list */
        mListItems = funGetAllAudit(mStrUserId, "1", "", "1", db);
        if (mListItems.size() > 0) {
            audittList = new DraftList(activity, mListItems);
            mListChat.setAdapter(audittList);
        } else {
            audittList = new DraftList(activity, mListItems);
            mListChat.setAdapter(audittList);
            //audittList.notifyDataSetChanged();
            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_error_no_record_found), activity);
        }

    }

    public static void mDatapreparing(Activity myactivity,String mStrAuditId, String mStrUserId, ProgressBar progressBar, TextViewSemiBold textViewSemiBold) {
        if (mCheckSignalStrength(myactivity)==2) {
            db.delete_tb_audit_final_question_answer(mStrAuditId,mStrUserId);
            showPDialog(myactivity);
            ArrayList<AuditQuestionAnswer> mAuditMainQuestionList = new ArrayList<AuditQuestionAnswer>();
            mAuditMainQuestionList = db.getAllMainQuestionAnswer(mStrAuditId, mStrUserId);
            if(mAuditMainQuestionList.size()<=0){
            CommonUtils.mShowAlert("Please give some answer of location question",myactivity);
            hidePDialog();
            return;
            }else {
                for (int i = 0; i < mAuditMainQuestionList.size(); i++) {
                    if (mAuditMainQuestionList.get(i).getmStrQuestionType().equals("1") || mAuditMainQuestionList.get(i).getmStrQuestionType().equals("3")) {
                        AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                        auditQuestionAnswer.setmStrAuditId(mStrAuditId);
                        auditQuestionAnswer.setmStrUserId(mStrUserId);
                        auditQuestionAnswer.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                        auditQuestionAnswer.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                        auditQuestionAnswer.setmStrQuestionId(mAuditMainQuestionList.get(i).getmStrQuestionId());
                        auditQuestionAnswer.setmStrQuestionServerId(mAuditMainQuestionList.get(i).getmStrQuestionServerId());
                        auditQuestionAnswer.setmStrAnswerId(mAuditMainQuestionList.get(i).getmStrAnswerId());
                        auditQuestionAnswer.setmStrAnswerValue(mAuditMainQuestionList.get(i).getmStrAnswerValue());
                        auditQuestionAnswer.setmStrQuestionType(mAuditMainQuestionList.get(i).getmStrQuestionType());
                        auditQuestionAnswer.setmStrQuestionImage(mAuditMainQuestionList.get(i).getmStrQuestionImage());
                        auditQuestionAnswer.setmStrQuestionPriority(mAuditMainQuestionList.get(i).getmStrQuestionPriority());
                        auditQuestionAnswer.setmStrQuestionExtraText(mAuditMainQuestionList.get(i).getmStrQuestionExtraText());
                        auditQuestionAnswer.setmStrIsQuestionNormal(mAuditMainQuestionList.get(i).getmStrIsQuestionNormal());
                        auditQuestionAnswer.setmStrIsQuestionParent("0");
                        auditQuestionAnswer.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                        auditQuestionAnswer.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                        auditQuestionAnswer.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                        auditQuestionAnswer.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                        auditQuestionAnswer.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                        auditQuestionAnswer.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                        auditQuestionAnswer.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                        auditQuestionAnswer.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                        auditQuestionAnswer.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                        auditQuestionAnswer.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                        auditQuestionAnswer.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                        auditQuestionAnswer.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                        auditQuestionAnswer.setmStrStatus("0");
                        auditQuestionAnswer.setmStrIsInspector("6");
                        db.insert_tb_audit_final_question_answer(auditQuestionAnswer);


                        AuditInspectorQuestion auditInspectorQuestion = new AuditInspectorQuestion();
                        auditInspectorQuestion.setmStrSubLocationExplanation(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                        auditInspectorQuestion.setmStrSubLocation(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                        auditInspectorQuestion.setmStrAuditId(mStrAuditId);
                        auditInspectorQuestion.setmStrUserId(mStrUserId);
                        auditInspectorQuestion.setmStrMainQuestion(mAuditMainQuestionList.get(i).getmStrQuestionServerId());

                        if(db.isInspecterQuestionAnswerSubmmitedNew(auditInspectorQuestion)){
                        ArrayList<AuditInspectorQuestion> mAlredSubmitedAnswer = new ArrayList<AuditInspectorQuestion>();
                        mAlredSubmitedAnswer = db.get_tb_inspector_questions_answer_new(auditInspectorQuestion);
                            AuditQuestionAnswer auditQuestionAnswer3 = new AuditQuestionAnswer();
                            auditQuestionAnswer3.setmStrAuditId(mStrAuditId);
                            auditQuestionAnswer3.setmStrUserId(mStrUserId);
                            auditQuestionAnswer3.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                            auditQuestionAnswer3.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                            auditQuestionAnswer3.setmStrQuestionId(mAlredSubmitedAnswer.get(0).getmStrId());
                            auditQuestionAnswer3.setmStrQuestionServerId(mAlredSubmitedAnswer.get(0).getmStrQuestionId());
                            auditQuestionAnswer3.setmStrAnswerId(mAlredSubmitedAnswer.get(0).getmStrAnswerId());
                            auditQuestionAnswer3.setmStrAnswerValue("");
                            auditQuestionAnswer3.setmStrQuestionType(mAlredSubmitedAnswer.get(0).getmStrQuestionType());
                            auditQuestionAnswer3.setmStrQuestionImage(mAlredSubmitedAnswer.get(0).getmStrQuestionImage());
                            auditQuestionAnswer3.setmStrQuestionPriority(mAlredSubmitedAnswer.get(0).getmStrQuestionPriority());
                            auditQuestionAnswer3.setmStrQuestionExtraText("");
                            auditQuestionAnswer3.setmStrIsQuestionNormal("1");
                            auditQuestionAnswer3.setmStrIsQuestionParent("0");
                            auditQuestionAnswer3.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                            auditQuestionAnswer3.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                            auditQuestionAnswer3.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                            auditQuestionAnswer3.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                            auditQuestionAnswer3.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                            auditQuestionAnswer3.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                            auditQuestionAnswer3.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                            auditQuestionAnswer3.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                            auditQuestionAnswer3.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                            auditQuestionAnswer3.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                            auditQuestionAnswer3.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                            auditQuestionAnswer3.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                            auditQuestionAnswer3.setmStrStatus("0");
                            auditQuestionAnswer3.setmStrIsInspector("7");
                            db.insert_tb_audit_final_question_answer(auditQuestionAnswer3);
                        }






                    } else {
                        ArrayList<SubQuestionAnswer> mAuditSubQuestionList = new ArrayList<SubQuestionAnswer>();
                        mAuditSubQuestionList = db.getAllSubQuestion(mStrAuditId, mStrUserId, mAuditMainQuestionList.get(i).getmStrQuestionServerId(), mAuditMainQuestionList.get(i).getmStrAnswerId());
                        AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                        auditQuestionAnswer.setmStrAuditId(mStrAuditId);
                        auditQuestionAnswer.setmStrUserId(mStrUserId);
                        auditQuestionAnswer.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                        auditQuestionAnswer.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                        auditQuestionAnswer.setmStrQuestionId(mAuditMainQuestionList.get(i).getmStrQuestionId());
                        auditQuestionAnswer.setmStrQuestionServerId(mAuditMainQuestionList.get(i).getmStrQuestionServerId());
                        auditQuestionAnswer.setmStrAnswerId(mAuditMainQuestionList.get(i).getmStrAnswerId());
                        auditQuestionAnswer.setmStrAnswerValue(mAuditMainQuestionList.get(i).getmStrAnswerValue());
                        auditQuestionAnswer.setmStrQuestionType(mAuditMainQuestionList.get(i).getmStrQuestionType());
                        auditQuestionAnswer.setmStrQuestionImage(mAuditMainQuestionList.get(i).getmStrQuestionImage());
                        auditQuestionAnswer.setmStrQuestionPriority(mAuditMainQuestionList.get(i).getmStrQuestionPriority());
                        auditQuestionAnswer.setmStrQuestionExtraText(mAuditMainQuestionList.get(i).getmStrQuestionExtraText());
                        auditQuestionAnswer.setmStrIsQuestionNormal(mAuditMainQuestionList.get(i).getmStrIsQuestionNormal());
                        if(mAuditSubQuestionList.size()>0){
                        auditQuestionAnswer.setmStrIsQuestionParent("1");
                        }else {
                        auditQuestionAnswer.setmStrIsQuestionParent("0");
                        }
                        auditQuestionAnswer.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                        auditQuestionAnswer.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                        auditQuestionAnswer.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                        auditQuestionAnswer.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                        auditQuestionAnswer.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                        auditQuestionAnswer.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                        auditQuestionAnswer.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                        auditQuestionAnswer.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                        auditQuestionAnswer.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                        auditQuestionAnswer.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                        auditQuestionAnswer.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                        auditQuestionAnswer.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                        auditQuestionAnswer.setmStrStatus("0");
                        auditQuestionAnswer.setmStrIsInspector("6");
                        db.insert_tb_audit_final_question_answer(auditQuestionAnswer);




                        AuditInspectorQuestion auditInspectorQuestion = new AuditInspectorQuestion();
                        auditInspectorQuestion.setmStrSubLocationExplanation(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                        auditInspectorQuestion.setmStrSubLocation(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                        auditInspectorQuestion.setmStrAuditId(mStrAuditId);
                        auditInspectorQuestion.setmStrUserId(mStrUserId);
                        auditInspectorQuestion.setmStrMainQuestion(mAuditMainQuestionList.get(i).getmStrQuestionServerId());

                        if(db.isInspecterQuestionAnswerSubmmitedNew(auditInspectorQuestion)){
                            ArrayList<AuditInspectorQuestion> mAlredSubmitedAnswer = new ArrayList<AuditInspectorQuestion>();
                            mAlredSubmitedAnswer = db.get_tb_inspector_questions_answer_new(auditInspectorQuestion);
                            AuditQuestionAnswer auditQuestionAnswer3 = new AuditQuestionAnswer();
                            auditQuestionAnswer3.setmStrAuditId(mStrAuditId);
                            auditQuestionAnswer3.setmStrUserId(mStrUserId);
                            auditQuestionAnswer3.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                            auditQuestionAnswer3.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                            auditQuestionAnswer3.setmStrQuestionId(mAlredSubmitedAnswer.get(0).getmStrId());
                            auditQuestionAnswer3.setmStrQuestionServerId(mAlredSubmitedAnswer.get(0).getmStrQuestionId());
                            auditQuestionAnswer3.setmStrAnswerId(mAlredSubmitedAnswer.get(0).getmStrAnswerId());
                            auditQuestionAnswer3.setmStrAnswerValue("");
                            auditQuestionAnswer3.setmStrQuestionType(mAlredSubmitedAnswer.get(0).getmStrQuestionType());
                            auditQuestionAnswer3.setmStrQuestionImage(mAlredSubmitedAnswer.get(0).getmStrQuestionImage());
                            auditQuestionAnswer3.setmStrQuestionPriority(mAlredSubmitedAnswer.get(0).getmStrQuestionPriority());
                            auditQuestionAnswer3.setmStrQuestionExtraText("");
                            auditQuestionAnswer3.setmStrIsQuestionNormal("1");
                            auditQuestionAnswer3.setmStrIsQuestionParent("0");
                            auditQuestionAnswer3.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                            auditQuestionAnswer3.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                            auditQuestionAnswer3.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                            auditQuestionAnswer3.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                            auditQuestionAnswer3.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                            auditQuestionAnswer3.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                            auditQuestionAnswer3.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                            auditQuestionAnswer3.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                            auditQuestionAnswer3.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                            auditQuestionAnswer3.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                            auditQuestionAnswer3.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                            auditQuestionAnswer3.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                            auditQuestionAnswer3.setmStrStatus("0");
                            auditQuestionAnswer3.setmStrIsInspector("7");
                            db.insert_tb_audit_final_question_answer(auditQuestionAnswer3);
                        }





                        for (int j = 0; j < mAuditSubQuestionList.size(); j++) {
                            ArrayList<SubQuestionAnswer> mAuditSubQuestionOfSubQuestion = new ArrayList<SubQuestionAnswer>();
                            mAuditSubQuestionOfSubQuestion = db.getAllSubQuestion(mStrAuditId, mStrUserId, mAuditSubQuestionList.get(j).getmStrQuestionServerId(), mAuditSubQuestionList.get(j).getmStrAnswerId());
                            AuditQuestionAnswer auditQuestionAnswer2 = new AuditQuestionAnswer();
                            auditQuestionAnswer2.setmStrAuditId(mStrAuditId);
                            auditQuestionAnswer2.setmStrUserId(mStrUserId);
                            auditQuestionAnswer2.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                            auditQuestionAnswer2.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                            auditQuestionAnswer2.setmStrQuestionId(mAuditSubQuestionList.get(j).getmStrQuestionId());
                            auditQuestionAnswer2.setmStrQuestionServerId(mAuditSubQuestionList.get(j).getmStrQuestionServerId());
                            auditQuestionAnswer2.setmStrAnswerId(mAuditSubQuestionList.get(j).getmStrAnswerId());
                            auditQuestionAnswer2.setmStrAnswerValue(mAuditSubQuestionList.get(j).getmStrAnswerValue());
                            auditQuestionAnswer2.setmStrQuestionType(mAuditSubQuestionList.get(j).getmStrQuestionType());
                            auditQuestionAnswer2.setmStrQuestionImage(mAuditSubQuestionList.get(j).getmStrQuestionImage());
                            auditQuestionAnswer2.setmStrQuestionPriority(mAuditSubQuestionList.get(j).getmStrQuestionPriority());
                            auditQuestionAnswer2.setmStrQuestionExtraText(mAuditSubQuestionList.get(j).getmStrQuestionExtraText());
                            auditQuestionAnswer2.setmStrIsQuestionNormal(mAuditSubQuestionList.get(j).getmStrIsQuestionNormal());
                            if(mAuditSubQuestionOfSubQuestion.size()>0){
                            auditQuestionAnswer2.setmStrIsQuestionParent("1");
                            }else {
                            auditQuestionAnswer2.setmStrIsQuestionParent("0");
                            }
                            auditQuestionAnswer2.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                            auditQuestionAnswer2.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                            auditQuestionAnswer2.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                            auditQuestionAnswer2.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                            auditQuestionAnswer2.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                            auditQuestionAnswer2.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                            auditQuestionAnswer2.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                            auditQuestionAnswer2.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                            auditQuestionAnswer2.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                            auditQuestionAnswer2.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                            auditQuestionAnswer2.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                            auditQuestionAnswer2.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                            auditQuestionAnswer2.setmStrStatus("0");
                            auditQuestionAnswer2.setmStrIsInspector("6");
                            if (!db.isQuestionExist(mAuditSubQuestionList.get(j).getmStrQuestionServerId(), mAuditMainQuestionList.get(i).getmStrMainLocationServerId(), mAuditMainQuestionList.get(i).getmStrSubLocationServerId())) {
                                db.insert_tb_audit_final_question_answer(auditQuestionAnswer2);
                            }
                            if(mAuditSubQuestionList.get(j).getmStrQuestionType().equals("3") || mAuditSubQuestionList.get(j).getmStrQuestionType().equals("1")){

                            }else {
                                for (int k = 0; k < mAuditSubQuestionOfSubQuestion.size(); k++) {
                                    AuditQuestionAnswer subQuestion = new AuditQuestionAnswer();
                                    subQuestion.setmStrAuditId(mStrAuditId);
                                    subQuestion.setmStrUserId(mStrUserId);
                                    subQuestion.setmStrCountryId(mAuditMainQuestionList.get(i).getmStrCountryId());
                                    subQuestion.setmStrLangId(mAuditMainQuestionList.get(i).getmStrLangId());
                                    subQuestion.setmStrQuestionId(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionId());
                                    subQuestion.setmStrQuestionServerId(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionServerId());
                                    subQuestion.setmStrAnswerId(mAuditSubQuestionOfSubQuestion.get(k).getmStrAnswerId());
                                    subQuestion.setmStrAnswerValue(mAuditSubQuestionOfSubQuestion.get(k).getmStrAnswerValue());
                                    subQuestion.setmStrQuestionType(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionType());
                                    subQuestion.setmStrQuestionImage(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionImage());
                                    subQuestion.setmStrQuestionPriority(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionPriority());
                                    subQuestion.setmStrQuestionExtraText(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionExtraText());
                                    subQuestion.setmStrIsQuestionNormal(mAuditSubQuestionOfSubQuestion.get(k).getmStrIsQuestionNormal());
                                    subQuestion.setmStrIsQuestionParent("0");
                                    subQuestion.setmStrSubLocationExplanationId(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationId());
                                    subQuestion.setmStrSubLocationExplanationTitle(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationTitle());
                                    subQuestion.setmStrSubLocationExplanationDesc(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationDesc());
                                    subQuestion.setmStrSubLocationExplanationImage(mAuditMainQuestionList.get(i).getmStrSubLocationExplanationImage());
                                    subQuestion.setmStrSubLocationServerId(mAuditMainQuestionList.get(i).getmStrSubLocationServerId());
                                    subQuestion.setmStrSubFolderLayerId(mAuditMainQuestionList.get(i).getmStrSubFolderLayerId());
                                    subQuestion.setmStrSubFolderLayerTitle(mAuditMainQuestionList.get(i).getmStrSubFolderLayerTitle());
                                    subQuestion.setmStrSubFolderLayerDesc(mAuditMainQuestionList.get(i).getmStrSubFolderLayerDesc());
                                    subQuestion.setmStrSubFolderLayerImage(mAuditMainQuestionList.get(i).getmStrSubFolderLayerImage());
                                    subQuestion.setmStrSubFolderId(mAuditMainQuestionList.get(i).getmStrSubFolderId());
                                    subQuestion.setmStrSubFolderTitle(mAuditMainQuestionList.get(i).getmStrSubFolderTitle());
                                    subQuestion.setmStrMainLocationServerId(mAuditMainQuestionList.get(i).getmStrMainLocationServerId());
                                    subQuestion.setmStrStatus("0");
                                    subQuestion.setmStrIsInspector("6");
                                    if (!db.isQuestionExist(mAuditSubQuestionOfSubQuestion.get(k).getmStrQuestionServerId().toString(), mAuditMainQuestionList.get(i).getmStrMainLocationServerId(), mAuditMainQuestionList.get(i).getmStrSubLocationServerId())) {
                                        db.insert_tb_audit_final_question_answer(subQuestion);
                                    }
                                }
                            }
                        }
                    }
                }
                mDataUpload(mStrAuditId, mStrUserId, progressBar, textViewSemiBold,0);
            }



        } else {
            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_alert_check_internet), activity);
        }
    }

    public static void mDataUpload(final String mStrAuditId, final String mStrUserId, final ProgressBar progressBar, final TextViewSemiBold textViewSemiBold,int time) {

        mAuditMainQuestion = db.getAllMainQuestionAnswerFinal(mStrAuditId, mStrUserId);
        if(mAuditMainQuestion.size()>0){
                final int h = time;
                final String mStrExpId = mAuditMainQuestion.get(h).getmStrSubLocationExplanationId();
                StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getAuditSync",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String str) {
                                try {
                                    System.out.println("<><><>" + str);
                                    JSONObject response = new JSONObject(str);
                                    String mStrStatus = response.getString("status");
                                    if (mStrStatus.equals("1")) {
                                        int a = 100 / mAuditMainQuestion.size();
                                        int b = progressBar.getProgress();
                                        System.out.println("<><><>##a" + a);
                                        System.out.println("<><><>##b" + b);
                                        progressBar.setProgress(b + a);
                                        textViewSemiBold.setText(b + a + " % ");
                                        db.updateQuestionStatus(mAuditMainQuestion.get(h).getmStrQuestionServerId());
                                        int newInt = h+1;
                                        if(newInt<mAuditMainQuestion.size()){
                                        mDataUpload(mStrAuditId, mStrUserId, progressBar, textViewSemiBold,newInt);
                                        }else {
                                            System.out.println("<><><>##      calll");
                                            progressBar.setProgress(100);
                                            textViewSemiBold.setText("100 % ");
                                            Audit audit = new Audit();
                                            audit.setmAuditId(mStrAuditId);
                                            audit.setmStatus("2");
                                            funUpdateAudit(audit, "1", db);
                                            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_alert_sync_data_successfully), activity);
                                            //hidePDialog();
                                            mAllQuestionSent(mStrAuditId);
                                            hidePDialog();
                                        }
                                       /* if (!db.isQuestionRemaining()) {


                                        }*/
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hidePDialog();
                                Toast.makeText(activity, activity.getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        System.out.println("<><><>auth_token " + PreferenceManager.getFormiiAuthToken(activity));
                        params.put("auth_token", PreferenceManager.getFormiiAuthToken(activity));
                        params.put("audit_id", mAuditMainQuestion.get(h).getmStrAuditId());
                        params.put("id", mAuditMainQuestion.get(h).getmStrUserId());
                        params.put("country_id", mAuditMainQuestion.get(h).getmStrCountryId());
                        params.put("lang", mAuditMainQuestion.get(h).getmStrLangId());
                        params.put("location_id", mAuditMainQuestion.get(h).getmStrMainLocationServerId());
                        params.put("subfolder_id", mAuditMainQuestion.get(h).getmStrSubFolderId());
                        params.put("subfolder_title", mAuditMainQuestion.get(h).getmStrSubFolderTitle());
                        params.put("layer_id", mAuditMainQuestion.get(h).getmStrSubFolderLayerId());
                        params.put("layer_title", mAuditMainQuestion.get(h).getmStrSubFolderLayerTitle());
                        params.put("layer_desc", mAuditMainQuestion.get(h).getmStrSubFolderLayerDesc());
                        if (!TextUtils.isEmpty(mAuditMainQuestion.get(h).getmStrSubFolderLayerImage())) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(mAuditMainQuestion.get(h).getmStrSubFolderLayerImage(), options);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            params.put("layer_img", imageString);
                        } else {
                            params.put("layer_img", "");
                        }
                        params.put("sub_location_id", mAuditMainQuestion.get(h).getmStrSubLocationServerId());
                        params.put("sub_location_layer_id", mAuditMainQuestion.get(h).getmStrSubLocationExplanationId());
                        params.put("sub_location_layer_title", mAuditMainQuestion.get(h).getmStrSubLocationExplanationTitle());
                        params.put("sub_location_layer_desc", mAuditMainQuestion.get(h).getmStrSubLocationExplanationDesc());
                        if (!TextUtils.isEmpty(mAuditMainQuestion.get(h).getmStrSubLocationExplanationImage())) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(mAuditMainQuestion.get(h).getmStrSubLocationExplanationImage(), options);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            params.put("sub_location_layer_first_image", imageString);
                        } else {
                            params.put("sub_location_layer_first_image", "");
                        }
                        params.put("question_id", mAuditMainQuestion.get(h).getmStrQuestionServerId());
                        params.put("parent_id", mAuditMainQuestion.get(h).getmStrIsQuestionParent());
                        params.put("answer_id", mAuditMainQuestion.get(h).getmStrAnswerId());
                        params.put("answer_value", mAuditMainQuestion.get(h).getmStrAnswerValue());
                        params.put("question_type", mAuditMainQuestion.get(h).getmStrQuestionType());
                        if (!TextUtils.isEmpty(mAuditMainQuestion.get(h).getmStrQuestionImage())) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(mAuditMainQuestion.get(h).getmStrQuestionImage(), options);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            params.put("question_image", imageString);
                        } else {
                            params.put("question_image", "");
                        }
                        if (mAuditMainQuestion.get(h).getmStrQuestionPriority().equals(activity.getString(R.string.mTextFile_low))) {
                            params.put("question_priority", "1");
                        } else if (mAuditMainQuestion.get(h).getmStrQuestionPriority().equals(activity.getString(R.string.mTextFile_medium))) {
                            params.put("question_priority", "2");
                        } else if (mAuditMainQuestion.get(h).getmStrQuestionPriority().equals(activity.getString(R.string.mTextFile_high))) {
                            params.put("question_priority", "3");
                        } else if (mAuditMainQuestion.get(h).getmStrQuestionPriority().equals(activity.getString(R.string.mTextFile_ppp))) {
                            params.put("question_priority", "4");
                        }
                        params.put("question_extra_text", mAuditMainQuestion.get(h).getmStrQuestionExtraText());
                        mImageList.clear();
                        mImageList = getAllExplanationImageFinal(mAuditMainQuestion.get(h).getmStrAuditId(), mAuditMainQuestion.get(h).getmStrUserId(), mAuditMainQuestion.get(h).getmStrSubLocationExplanationId(), db);
                        if (mImageList.size() > 0) {
                            System.out.println("<><><>size " + mImageList.size());
                            for (int k = 0; k < mImageList.size(); k++) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = BitmapFactory.decodeFile(mImageList.get(k), options);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                                byte[] imageBytes = baos.toByteArray();
                                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                params.put("mul_image[" + k + "]", imageString);
                                System.out.println("<><>@# "+imageString);
                            }
                            update_tb_image_sub_location_explation_list_by_layer_id(mStrAuditId, mStrUserId, mStrExpId, db);
                        } else {
                            params.put("mul_image[]", "");
                        }
                        System.out.println("<><><>qwerty ");
                        return params;
                    }
                };
                strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(strRequest);



        }


        //hidePDialog();

    }



    public static void mAllQuestionSent(final String mAuditId) {
        System.out.println("<><><>mReorderStatusall" + mReorderStatus);
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "DataSyncCom",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println(" " + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                            String strAuditerReport = response.getString("AuditorPDF");
                            String strInspectorReport = response.getString("InspectorPDF");
                            Audit audit = new Audit();
                            audit.setmStrInspectorReport(strInspectorReport);
                            audit.setmStrAuditerReport(strAuditerReport);
                            audit.setmAuditId(mAuditId);
                            funUpdateAudit(audit,"4",db);
                            mReorderStatus=0;
                            mSendReportRequest(mAuditId);
                            ActivityTabHostMain.mTabHost.setCurrentTab(0);
                           //CommonUtils.mShowAlert(response.getString("message"), ActivityContactUs.this);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(activity);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), activity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(activity, R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("audit_id", mAuditId);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(activity));
                params.put("id", PreferenceManager.getFormiiId(activity));
                System.out.println("<><><>allparam" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    public static void mSendReportRequest(final String mAuditId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "generateReport",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        System.out.println("<><><>######2" + str);
                        hidePDialog();
                        ActivityTabHostMain.mTabHost.setCurrentTab(0);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("audit_id", mAuditId);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(activity));
                params.put("id", PreferenceManager.getFormiiId(activity));
                System.out.println("<><><>allparam" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

}