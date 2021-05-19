/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.covetus.audit.ActivityReorderSelectedLocationList;
import com.covetus.audit.ActivitySelectCountryStandard;
import com.covetus.audit.FragmentDraft;
import com.covetus.audit.R;
import com.covetus.audit.SelectMainLocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditMainLocation;
import ABS_GET_SET.AuditQuestion;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_GET_SET.AuditSubLocation;
import ABS_GET_SET.AuditSubQuestion;
import ABS_GET_SET.Inspector;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.MainLocationSubFolder;
import ABS_GET_SET.SelectedLocation;
import ABS_GET_SET.SelectedSubLocation;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static Modal.AuditListModal.funUpdateAudit;
import static Modal.AuditListModal.funSynIsInProgress;
import static Modal.MainLocationModal.funGetAllSelectedMainLocation;
import static Modal.MainLocationModal.funInsertAllMainLocation;
import static Modal.SubFolderModal.funGetAllSubFolderLayer;
import static Modal.SubFolderModal.funGetAllSubFolders;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;
import static Modal.SubLocationModal.funInsertAllSubLocation;
import static com.covetus.audit.ActivityTabHostMain.mStrCurrentTab;
import static com.covetus.audit.ActivityTabHostMain.mTabHost;
import static com.covetus.audit.FragmentDraft.mReorderStatus;


public class DraftList extends BaseAdapter {

    private ArrayList<Audit> mListItems = new ArrayList<>();
    Activity context;


    public DraftList(Activity context, ArrayList<Audit> mListItems) {
        this.mListItems = mListItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_draft_list, null);
            holder = new ViewHolder();
            holder.mTxtAuditDate = convertView.findViewById(R.id.mTxtAuditDate);
            holder.mTxtAuditTitle = convertView.findViewById(R.id.mTxtAuditTitle);
            holder.mTxtAgentName = convertView.findViewById(R.id.mTxtAgentName);
            holder.mLayoutViewAudit = convertView.findViewById(R.id.mLayoutViewAudit);
            holder.mLayoutUploadAudit = convertView.findViewById(R.id.mLayoutUploadAudit);
            holder.mLayoutRefrashAudit = convertView.findViewById(R.id.mLayoutRefrashAudit);
            holder.mLayoutMain = convertView.findViewById(R.id.mLayoutMain);
            holder.progressbar = convertView.findViewById(R.id.progressbar);
            holder.mButtonLayout = convertView.findViewById(R.id.mButtonLayout);
            holder.mProgressLayout = convertView.findViewById(R.id.mProgressLayout);
            holder.mResumeLayout = convertView.findViewById(R.id.mResumeLayout);
            holder.mTextProgress = convertView.findViewById(R.id.mTextProgress);

            //funSynIsInProgress
            final Audit audit = mListItems.get(position);
            final DatabaseHelper db = new DatabaseHelper(context);
            try {
                holder.mTxtAuditDate.setText(CommonUtils.mTogetDay(audit.getmDueDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.mTxtAuditTitle.setText(audit.getmTitle());
            holder.mTxtAgentName.setText(audit.getmAssignBy());
            double prcent = 0.0;
            if (getAllQuestionCount(audit.getmAuditId(), audit.getmUserId(), db) > 0) {
                int AllQuestionCount = getAllQuestionCount(audit.getmAuditId(), audit.getmUserId(), db);
                int AllGivenQuestionCount = db.getAllMainQuestionAnswerCount(audit.getmAuditId(), audit.getmUserId());
                prcent = (double) AllGivenQuestionCount / (double) AllQuestionCount;
                prcent = prcent * 100;
            } else {
                prcent = 0.0;
            }
            if (prcent == 0.0) {
                holder.mLayoutMain.setBackgroundColor(Color.parseColor("#FE0000"));

                //holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.red_border));
            } else if (prcent == 100) {
                //holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.green_border));
                holder.mLayoutMain.setBackgroundColor(Color.parseColor("#13CD66"));
            } else {
                holder.mLayoutMain.setBackgroundColor(Color.parseColor("#FEB95C"));
                //holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.yellow_border));
            }


            holder.mLayoutViewAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mAuditId = audit.getmAuditId();
                    SelectedLocation selectedLocation = new SelectedLocation();
                    selectedLocation.setmStrAuditId(audit.getmAuditId());
                    selectedLocation.setmStrUserId(audit.getmUserId());
                    if (funGetAllSelectedMainLocation(selectedLocation, db).size() > 0) {
                        Intent intent = new Intent(context, SelectMainLocationActivity.class);
                        intent.putExtra("mAuditId", mAuditId);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ActivitySelectCountryStandard.class);
                        intent.putExtra("mAuditId", mAuditId);
                        context.startActivity(intent);
                    }
                }
            });
            System.out.println("<><>else" + funSynIsInProgress(audit.getmAuditId(), "1", db));

            if (funSynIsInProgress(audit.getmAuditId(), "1", db)) {
                holder.mResumeLayout.setVisibility(View.VISIBLE);
                holder.mProgressLayout.setVisibility(View.GONE);
                holder.mButtonLayout.setVisibility(View.GONE);
            }

            holder.mResumeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.mResumeLayout.setVisibility(View.GONE);
                    holder.mButtonLayout.setVisibility(View.GONE);
                    holder.mProgressLayout.setVisibility(View.VISIBLE);
                    holder.mTextProgress.setText("0 %");
                    FragmentDraft.mDatapreparing(context, audit.getmAuditId(), audit.getmUserId(), holder.progressbar, holder.mTextProgress);

                }
            });


            holder.mLayoutRefrashAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.showPDialogMsg(context);
                    db.delete_raw_data_of_audit(audit.getmAuditId(),audit.getmUserId());
                    mToGetLocationStandard(audit.getmAuditId(),audit.getmStrLangId(),audit.getmStrCountryId());
                }
            });




            holder.mLayoutUploadAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ArrayList<AuditQuestionAnswer> mAuditMainQuestionList = new ArrayList<AuditQuestionAnswer>();
                    mAuditMainQuestionList = db.getAllMainQuestionAnswer(audit.getmAuditId(), audit.getmUserId());
                    if (mAuditMainQuestionList.size() <= 0) {
                        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                        dialog.setCancelable(false);
                        RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                        RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                        mTxtMsg.setText(context.getString(R.string.mTextFile_location_answer));
                        mLayoutNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        mLayoutYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();


                    }else {


                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                    dialog.setCancelable(false);
                    RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                    RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (mReorderStatus == 0) {
                                mReorderStatus = 1;
                                Intent intent = new Intent(context, ActivityReorderSelectedLocationList.class);
                                intent.putExtra("mAuditId", audit.getmAuditId());
                                context.startActivity(intent);
                            } else {
                                mReorderStatus = 0;
                                ArrayList<AuditQuestionAnswer> mAuditMainQuestionList = new ArrayList<AuditQuestionAnswer>();
                                mAuditMainQuestionList = db.getAllMainQuestionAnswer(audit.getmAuditId(), audit.getmUserId());
                                if (mAuditMainQuestionList.size() <= 0) {
                                    CommonUtils.mShowAlert(context.getString(R.string.mTextFile_location_answer), context);
                                } else {
                                    funUpdateAudit(audit, "3", db);
                                    holder.mButtonLayout.setVisibility(View.GONE);
                                    holder.mProgressLayout.setVisibility(View.VISIBLE);
                                    holder.mTextProgress.setText("0 %");
                                    FragmentDraft.mDatapreparing(context, audit.getmAuditId(), audit.getmUserId(), holder.progressbar, holder.mTextProgress);
                                }
                            }
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

//1st
    void mToGetLocationStandard(final String mAuditId,final String mStrLanguageCode,final String mStrCountryId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "get-location",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {

                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                DatabaseHelper db = new DatabaseHelper(context);
                                JSONArray jsonArray = response.getJSONArray("response");
                                for (int i = 0;i<jsonArray.length();i++){
                                    JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                                    String mStrId = jsonObjectArr.getString("id");
                                    String mStrTitle = jsonObjectArr.getString("title");
                                    String mStrDetail = jsonObjectArr.getString("details");
                                    AuditMainLocation auditMainLocation = new AuditMainLocation();
                                    auditMainLocation.setmStrAuditId(mAuditId);//audit id
                                    auditMainLocation.setmStrUserId(PreferenceManager.getFormiiId(context));//user id
                                    auditMainLocation.setmStrLocationServerId(mStrId);
                                    auditMainLocation.setmStrLocationTitle(mStrTitle);
                                    auditMainLocation.setmStrLocationDesc(mStrDetail);
                                    funInsertAllMainLocation(auditMainLocation,db);
                            /*if(i==jsonArray.length()-1){
                            getLocationContent();
                            }*/
                                }
                                mToGetSubLocationStandard(mAuditId,mStrLanguageCode,mStrCountryId);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(context);
                            } else {
                                mShowAlert(response.getString("message"), context);
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
                        Toast.makeText(context, context.getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(context));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(context));
                params.put("audit_id", mAuditId);
                params.put("lang", mStrLanguageCode);
                params.put("country_id", mStrCountryId);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

//2nd
    void mToGetSubLocationStandard(final String mAuditId,final String mStrLanguageCode,final String mStrCountryId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getContent",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {

                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                DatabaseHelper db = new DatabaseHelper(context);
                                JSONArray jsonArray = response.getJSONArray("response");
                                for (int i = 0;i<jsonArray.length();i++){
                                    JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                                    String mStrId = jsonObjectArr.getString("id");
                                    String mStrTitle = jsonObjectArr.getString("title");
                                    String mStrDetail = jsonObjectArr.getString("details");
                                    String mStrMainLocation = jsonObjectArr.getString("location_id");
                                    AuditSubLocation auditSubLocation = new AuditSubLocation();
                                    auditSubLocation.setmStrAuditId(mAuditId);
                                    auditSubLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                                    auditSubLocation.setmStrMainLocationServer(mStrMainLocation);
                                    auditSubLocation.setmStrMainLocationLocal("demo");
                                    auditSubLocation.setmStrSubLocationServerId(mStrId);
                                    auditSubLocation.setmStrSubLocationTitle(mStrTitle);
                                    auditSubLocation.setmStrSubLocationDesc(mStrDetail);
                                    funInsertAllSubLocation(auditSubLocation, db);
                            /*if(i==jsonArray.length()-1){
                            getLocationContent();
                            }*/
                                }
                                mToGetQuestion(mAuditId,mStrLanguageCode,mStrCountryId);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(context);
                            } else {
                                mShowAlert(response.getString("message"), context);
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
                        Toast.makeText(context, context.getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(context));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(context));
                params.put("audit_id", mAuditId);
                params.put("lang", mStrLanguageCode);
                params.put("country_id", mStrCountryId);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }



    void mToGetQuestion(final String mAuditId,final String mStrLanguageCode,final String mStrCountryId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getFinalQuestion",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {

                            JSONObject response = new JSONObject(str);
                            System.out.println("<><><>" + response.toString());
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                DatabaseHelper db = new DatabaseHelper(context);
                                JSONObject jsonObject = response.getJSONObject("response");
                                String mStrMainLocation = "demo";



                                JSONArray JANormalQuestion = jsonObject.getJSONArray("normal_questions");
                                for (int l = 0; l < JANormalQuestion.length(); l++) {
                                    JSONArray jsonArray = JANormalQuestion.getJSONArray(l);
                                    for (int u =0;u<jsonArray.length();u++){
                                        JSONObject JOMeasurementQuestion = jsonArray.getJSONObject(u);
                                        String mStrNormalQuestionID = JOMeasurementQuestion.getString("id");
                                        String mStrNormalQuestion = JOMeasurementQuestion.getString("question");
                                        String mStrNormalQuesAnswer = JOMeasurementQuestion.getString("answer");
                                        String mStrNormalAnswerID = JOMeasurementQuestion.getString("answernum");
                                        String mStrAnswerType = JOMeasurementQuestion.getString("type");
                                        String mStrSubLocation = JOMeasurementQuestion.getString("content_id");
                                        JSONArray JASubQuestion = JOMeasurementQuestion.getJSONArray("sub_questions");

                                        JSONArray JAInspectorQuestion = JOMeasurementQuestion.getJSONArray("ins_questions");


                                        AuditQuestion auditQuestion = new AuditQuestion();
                                        auditQuestion.setmStrAuditId(mAuditId);
                                        auditQuestion.setmStrUserId(PreferenceManager.getFormiiId(context));
                                        auditQuestion.setmStrMainLocation(mStrMainLocation + "");
                                        auditQuestion.setmStrSubLocation(mStrSubLocation + "");
                                        System.out.println("<>##<> "+mStrNormalQuestion);
                                        auditQuestion.setmStrQuestionTxt(mStrNormalQuestion.replace("*","#"));
                                        auditQuestion.setmStrServerId(mStrNormalQuestionID);

                                        if (mStrNormalQuesAnswer.contains("\\r\\n")) {
                                            mStrNormalQuesAnswer = mStrNormalQuesAnswer.replace("\\r\\n", "\r\n");
                                        }
                                        auditQuestion.setmStrAnswerOption(mStrNormalQuesAnswer.replace("\r\n", "#"));
                                        auditQuestion.setmStrAnswerOptionId(mStrNormalAnswerID.replace(",", "#"));
                                        auditQuestion.setmStrQuestionType(mStrAnswerType);
                                        auditQuestion.setmStrQuestionStatus("1");

                                        if (JASubQuestion.length() > 0) {
                                            auditQuestion.setmStrSubQuestion("1");
                                        } else {
                                            auditQuestion.setmStrSubQuestion("0");
                                        }

                                        if (JAInspectorQuestion.length() > 0) {
                                            auditQuestion.setmStrInspectorQuestion("1");
                                        } else {
                                            auditQuestion.setmStrInspectorQuestion("0");

                                        }


                                        if (mStrAnswerType.equals("5")) {

                                        } else {
                                            db.insert_tb_audit_question(auditQuestion);
                                        }
                                        for (int p = 0; p < JAInspectorQuestion.length(); p++) {
                                            JSONObject JOInspectorQuestion = JAInspectorQuestion.getJSONObject(p);
                                            String mStrInspectorQuestionId = JOInspectorQuestion.getString("id");
                                            String mStrInspectorQuestion = JOInspectorQuestion.getString("question");
                                            String mStrInspectorQuestionAns = JOInspectorQuestion.getString("answer");
                                            String mStrInspectorQuestionAnsID = JOInspectorQuestion.getString("answernum");
                                            String mStrInspectorAnsType = JOInspectorQuestion.getString("type");


                                            if (mStrInspectorQuestion.contains("\\r\\n")) {
                                                mStrInspectorQuestion = mStrInspectorQuestion.replace("\\r\\n", "\r\n");
                                            }

                                            Inspector inspector = new Inspector();
                                            inspector.setmStrAuditId(mAuditId);
                                            inspector.setmStrUserId(PreferenceManager.getFormiiId(context));
                                            inspector.setmStrMainLocation(mStrMainLocation);
                                            inspector.setmStrSubLocation(mStrSubLocation);
                                            inspector.setmStrMainQuestion(mStrNormalQuestionID);
                                            inspector.setmStrQuestionId(mStrInspectorQuestionId);
                                            inspector.setmStrQuestionTxt(mStrInspectorQuestion.replace("*","#"));
                                            inspector.setmStrQuestionType(mStrInspectorAnsType);
                                            inspector.setmStrAnswerOption(mStrInspectorQuestionAns.replace("\r\n", "#"));
                                            inspector.setmStrAnswerOptionId(mStrInspectorQuestionAnsID.replace(",", "#"));
                                            db.insert_tb_inspector_questions(inspector);
                                        }


                                        if(JASubQuestion.length()>0){
                                            RecurJson(JASubQuestion,mStrMainLocation,mStrSubLocation,mStrNormalQuestionID,db,mAuditId);
                                        }





                                    }


                                }






                                JSONArray JAMeasurementQuestion = jsonObject.getJSONArray("measurement_questions");

                                for (int l = 0; l < JAMeasurementQuestion.length(); l++) {
                                    JSONArray jsonArray = JAMeasurementQuestion.getJSONArray(l);
                                    for (int p = 0;p<jsonArray.length();p++) {
                                        JSONObject JOMeasurementQuestion = jsonArray.getJSONObject(p);
                                        String mStrNormalQuestionID = JOMeasurementQuestion.getString("id");
                                        String mStrNormalQuestion = JOMeasurementQuestion.getString("question");
                                        String mStrNormalQuesAnswer = JOMeasurementQuestion.getString("answer");
                                        String mStrNormalAnswerID = JOMeasurementQuestion.getString("answernum");
                                        String mStrAnswerType = JOMeasurementQuestion.getString("type");
                                        String mStrSubLocation = JOMeasurementQuestion.getString("content_id");
                                        JSONArray JASubQuestion = JOMeasurementQuestion.getJSONArray("sub_questions");

                                        JSONArray JAInspectorQuestion = JOMeasurementQuestion.getJSONArray("ins_questions");

                                        System.out.println("<><><><>JASubQuestion" + JASubQuestion.toString());
                                        AuditQuestion auditQuestion = new AuditQuestion();
                                        auditQuestion.setmStrAuditId(mAuditId);
                                        auditQuestion.setmStrUserId(PreferenceManager.getFormiiId(context));
                                        auditQuestion.setmStrMainLocation(mStrMainLocation + "");
                                        auditQuestion.setmStrSubLocation(mStrSubLocation + "");
                                        auditQuestion.setmStrQuestionTxt(mStrNormalQuestion.replace("*","#"));
                                        auditQuestion.setmStrServerId(mStrNormalQuestionID);
                                        auditQuestion.setmStrAnswerOption(mStrNormalQuesAnswer.replace("\r\n", "#"));
                                        auditQuestion.setmStrAnswerOptionId(mStrNormalAnswerID.replace(",", "#"));
                                        auditQuestion.setmStrQuestionType(mStrAnswerType);
                                        auditQuestion.setmStrQuestionStatus("0");
                                        if (JASubQuestion.length() > 0) {
                                            auditQuestion.setmStrSubQuestion("1");
                                        } else {
                                            auditQuestion.setmStrSubQuestion("0");
                                        }

                                        if (JAInspectorQuestion.length() > 0) {
                                            auditQuestion.setmStrInspectorQuestion("1");
                                        } else {
                                            auditQuestion.setmStrInspectorQuestion("0");

                                        }

                                        if (mStrAnswerType.equals("5")) {

                                        } else {
                                            db.insert_tb_audit_question(auditQuestion);
                                        }


                                        if(JASubQuestion.length()>0){
                                            RecurJson(JASubQuestion,mStrMainLocation,mStrSubLocation,mStrNormalQuestionID,db,mAuditId);
                                        }




                                    }
                                }






                                Audit audit = new Audit();
                                audit.setmAuditId(mAuditId);
                                audit.setmStrCountryId(mStrCountryId);
                                audit.setmStrLangId(mStrLanguageCode);
                                funUpdateAudit(audit,"2",db);
                                //hidePDialog();
                                /*mStrCurrentTab = "0";
                                Intent intent = new Intent(context, SelectMainLocationActivity.class);
                                intent.putExtra("mAuditId", mAuditId);
                                startActivity(intent);
                                finish();*/
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(context);
                            } else {
                                mShowAlert(response.getString("message"), context);
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
                        Toast.makeText(context, context.getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(context));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(context));
                params.put("audit_id", mAuditId);
                params.put("lang", mStrLanguageCode);
                params.put("country_id", mStrCountryId);
                System.out.println("<><>** "+params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    void RecurJson(JSONArray jsonArray,String mStrMainLocation,String mStrSubLocation, String mStrNormalQuestionID,DatabaseHelper db,String mAuditId) {
        try {
            for (int b = 0; b < jsonArray.length(); b++) {
                JSONObject jsonObjectSub = jsonArray.getJSONObject(b);
                String mStrSUBQuestionID = jsonObjectSub.getString("id");
                String mStrSUBQuestion = jsonObjectSub.getString("question");
                String mStrSUBQuesAnswer = jsonObjectSub.getString("answer");
                String mStrSUBAnswerID = jsonObjectSub.getString("answernum");
                String mStrSUBAnswerType = jsonObjectSub.getString("type");
                String mStrSUBLocation = jsonObjectSub.getString("content_id");
                String mStrQuestionCondition = jsonObjectSub.getString("answer_id");
                JSONArray jsonArraySUB = jsonObjectSub.getJSONArray("sub_questions");
                System.out.println("<>###<><>###<><>## "+mStrSUBQuestionID);

                AuditSubQuestion auditSubQuestion = new AuditSubQuestion();
                auditSubQuestion.setmStrUserId(PreferenceManager.getFormiiId(context));
                auditSubQuestion.setmStrAuditId(mAuditId);
                auditSubQuestion.setmStrServerId(mStrSUBQuestionID);
                auditSubQuestion.setmStrQuestionTxt(mStrSUBQuestion.replace("*", "#"));
                if (mStrSUBQuesAnswer.contains("\\r\\n")) {
                    mStrSUBQuesAnswer = mStrSUBQuesAnswer.replace("\\r\\n", "\r\n");
                }
                auditSubQuestion.setmStrAnswerOption(mStrSUBQuesAnswer.replace("\r\n", "#"));
                auditSubQuestion.setmStrAnswerOptionId(mStrSUBAnswerID.replace(",", "#"));
                auditSubQuestion.setmStrQuestionType(mStrSUBAnswerType);
                auditSubQuestion.setmStrMainLocation(mStrMainLocation);
                auditSubQuestion.setmStrMainQuestion(mStrNormalQuestionID + "");
                auditSubQuestion.setmStrQuestionFor(mStrQuestionCondition);
                auditSubQuestion.setmStrSubLocation(mStrSubLocation);

                if (jsonArraySUB.length() > 0) {
                    auditSubQuestion.setmStrSubQuestion("1");
                } else {
                    auditSubQuestion.setmStrSubQuestion("0");
                }

                if (mStrSUBAnswerType.equals("5")) {

                } else {
                    db.insert_tb_audit_sub_questions(auditSubQuestion);
                }
                if(jsonArraySUB.length()>0){
                    RecurJson(jsonArraySUB,mStrMainLocation,mStrSubLocation,mStrSUBQuestionID,db,mAuditId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private class ViewHolder {
        TextViewSemiBold mTxtAuditDate;
        TextViewSemiBold mTxtAuditTitle;
        TextViewSemiBold mTxtAgentName;
        ImageView mLayoutViewAudit, mLayoutUploadAudit,mLayoutRefrashAudit;
        RelativeLayout mLayoutMain;
        ProgressBar progressbar;
        LinearLayout mButtonLayout;
        RelativeLayout mProgressLayout;
        TextViewSemiBold mTextProgress;
        RelativeLayout mResumeLayout;
    }


    public int getAllQuestionCount(String mAuditId, String mUserId, DatabaseHelper databaseHelper) {
        int mTotalQuestion = 0;
        int mCountQuestion = 0;
        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrAuditId(mAuditId);
        selectedLocation.setmStrUserId(mUserId);
        ArrayList<SelectedLocation> mAuditList = new ArrayList<SelectedLocation>();
        mAuditList = funGetAllSelectedMainLocation(selectedLocation, databaseHelper);
        for (int i = 0; i < mAuditList.size(); i++) {
            mCountQuestion = 0;
            int intMainLocationCount = Integer.parseInt(mAuditList.get(i).getmStrMainLocationCount());
            String mMainLocaionId = mAuditList.get(i).getmStrMainLocationServerId();
            MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
            mainLocationSubFolder.setmStrMainLocationServerId(mMainLocaionId);
            mainLocationSubFolder.setmStrAuditId(mAuditId);
            mainLocationSubFolder.setmStrUserId(mUserId);
            ArrayList<MainLocationSubFolder> mSubFolderList = new ArrayList<MainLocationSubFolder>();
            mSubFolderList = funGetAllSubFolders(mainLocationSubFolder, databaseHelper);
            for (int k = 0; k < mSubFolderList.size(); k++) {
                ArrayList<LayerList> mLayerList = new ArrayList<LayerList>();
                mLayerList = funGetAllSubFolderLayer(mSubFolderList.get(k).getmStrId(), mAuditId, mUserId, "1", databaseHelper);
                for (int l = 0; l < mLayerList.size(); l++) {
                    SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
                    selectedSubLocation.setmStrAuditId(mAuditId);
                    selectedSubLocation.setmStrUserId(mUserId);
                    selectedSubLocation.setmStrLayerId(mLayerList.get(l).getmStrId());
                    selectedSubLocation.setmStrMainLocationServer(mMainLocaionId);
                    ArrayList<SelectedSubLocation> mSubLocationList = new ArrayList<SelectedSubLocation>();
                    mSubLocationList = funGetAllSelectedSubLocation(selectedSubLocation, "1", databaseHelper);
                    for (int j = 0; j < mSubLocationList.size(); j++) {
                        int intSubLocationCount = Integer.parseInt(mSubLocationList.get(j).getmStrSubLocationCount());
                        String mSubLocaionId = mSubLocationList.get(j).getmStrSubLocationServer();
                        int mQuestionCount = databaseHelper.getAllAuditQuestionCount(mAuditId, mUserId, mMainLocaionId, mSubLocaionId);
                        mCountQuestion = mCountQuestion + (mQuestionCount * intSubLocationCount);
                    }
                }

            }
            mTotalQuestion = mTotalQuestion + (mCountQuestion * intMainLocationCount);
        }
        return mTotalQuestion;
    }
}