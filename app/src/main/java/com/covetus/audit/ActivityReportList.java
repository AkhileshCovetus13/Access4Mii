package com.covetus.audit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.ReportAdapter;
import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_GET_SET.Report;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.showPDialog;

/**
 * Created by admin18 on 8/3/19.
 */


public class ActivityReportList extends Activity {
    ArrayList<Report> mListItems = new ArrayList<>();
    ArrayList<Report> mListSearchItems = new ArrayList<>();
    ReportAdapter reportAdapter;
    int textlength = 0;
    @BindView(R.id.mListReport)
    ListView mListReport;
    @BindView(R.id.mImgBack)
    ImageView mImgBack;
    @BindView(R.id.mEditReportSearch)
    EditTextRegular mEditReportSearch;


    /*click for going back*/
    @OnClick(R.id.mImgBack)
    void mClose() {
        closeKeyBoard(ActivityReportList.this);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ButterKnife.bind(this);
        /* Internet Connectivity Check */
        checkPermission();

        /* Search Box */
        mEditReportSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                textlength = mEditReportSearch.getText().length();
                mListSearchItems.clear();
                if (textlength == 0) {
//                    isLoadMore = true;
                    mListSearchItems.addAll(mListItems);
                } else {
//                    isLoadMore = false;
                    for (int i = 0; i < mListItems.size(); i++) {
                        if (textlength <= mListItems.get(i).getmAuditTitle().length()) {
                            if (mListItems.get(i).getmAuditTitle().toLowerCase().trim().contains(
                                    mEditReportSearch.getText().toString().toLowerCase().trim())) {
                                mListSearchItems.add(mListItems.get(i));
                            }
                        }
                    }
                }
                reportAdapter = new ReportAdapter(ActivityReportList.this, mListSearchItems);
                mListReport.setAdapter(reportAdapter);
                reportAdapter.notifyDataSetChanged();
            }
        });

    }

    void mToGetAgentList() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getAuditReport",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        mListItems.clear();
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                JSONArray jsonArray = jsonObject.getJSONArray("history");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Report report = new Report();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    JSONObject jsonAudit = jsonObject1.getJSONObject("auditDetails");
                                    report.setmAuditId(jsonAudit.getString("id"));
                                    report.setmAuditTitle(jsonAudit.getString("title"));
                                    report.setmAuditDesc(jsonAudit.getString("description"));
                                    report.setmAuditTime(jsonAudit.getString("time"));
                                    report.setmAuditStartDate(jsonAudit.getString("start_date"));
                                    report.setmAuditEndDate(jsonAudit.getString("end_date"));
                                    report.setmAuditCountry(jsonAudit.getString("country"));
                                    report.setmAuditState(jsonAudit.getString("State"));
                                    report.setmAuditCity(jsonAudit.getString("city"));
                                    report.setmAuditAddress(jsonAudit.getString("address"));
                                    report.setmAuditReport(jsonAudit.getString("report"));
                                    report.setmAuditReportSlug(jsonAudit.getString("slugs"));
                                    report.setmAuditUserType(jsonAudit.getString("auditType"));
                                    report.setmAuditInsReport(jsonAudit.getString("inspector_pdf"));
                                    report.setmAuditAuditerReport(jsonAudit.getString("auditor_pdf"));
                                    JSONObject jsonAgent = jsonObject1.getJSONObject("agentDetails");
                                    report.setmAgentName(jsonAgent.getString("fulname"));
                                    report.setmAgentEmail(jsonAgent.getString("email"));
                                    report.setmAgentPhoto(jsonAgent.getString("photo"));
                                    report.setmAgentPhone(jsonAgent.getString("phone"));


                                    mListItems.add(report);
                                    mListSearchItems.add(report);
                                }
                                reportAdapter = new ReportAdapter(ActivityReportList.this, mListItems);
                                mListReport.setAdapter(reportAdapter);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityReportList.this);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityReportList.this);
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
                        Toast.makeText(ActivityReportList.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityReportList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityReportList.this));
                System.out.println("<><><>params" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }




    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(ActivityReportList.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) ActivityReportList.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActivityReportList.this);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read & Write file permission is necessary to show pdf file!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)ActivityReportList.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 404);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)ActivityReportList.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 404);
                }
                return false;
            } else {
                if (mCheckSignalStrength(ActivityReportList.this) == 2) {
                    showPDialog(ActivityReportList.this);
                    mToGetAgentList();
                } else {
                    mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityReportList.this);
                }
                return true;
            }
        } else {
            if (mCheckSignalStrength(ActivityReportList.this) == 2) {
                showPDialog(ActivityReportList.this);
                mToGetAgentList();
            } else {
                mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityReportList.this);
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 404:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCheckSignalStrength(ActivityReportList.this) == 2) {
                        showPDialog(ActivityReportList.this);
                        mToGetAgentList();
                    } else {
                        mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityReportList.this);
                    }
                } else {
                    checkPermission();
                    //code for deny
                }
                break;
        }
    }


}
