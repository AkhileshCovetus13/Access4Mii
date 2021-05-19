package com.covetus.audit;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.DocumentList;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.NotificationUtils;
import ABS_HELPER.PreferenceManager;
import ABS_HELPER.RecyclerItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hasPermissions;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.mStrDownloadPath;
import static ABS_HELPER.CommonUtils.showPDialog;
import static ABS_HELPER.CommonUtils.showWarningPopupSingleButton;
import static Modal.AuditListModal.funInsertAllAudit;

public class ActivityAuditDetail extends Activity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @BindView(R.id.mLayoutReject)
    RelativeLayout mLayoutReject;
    @BindView(R.id.mLayoutAccept)
    RelativeLayout mLayoutAccept;

    @BindView(R.id.mTxtAuditTitle)
    TextViewBold mTxtAuditTitle;
    @BindView(R.id.mTxtAuditDetail)
    TextViewSemiBold mTxtAuditDetail;



    @BindView(R.id.mTxtAuditDeuDate)
    TextViewSemiBold mTxtAuditDeuDate;
    @BindView(R.id.mTxtAuditArea)
    TextViewSemiBold mTxtAuditArea;
    @BindView(R.id.mImgAgentProfile)
    ImageView mImgAgentProfile;
    @BindView(R.id.mTxtAgentName)
    TextViewSemiBold mTxtAgentName;
    @BindView(R.id.mTxtAgentEmail)
    TextViewSemiBold mTxtAgentEmail;

    @BindView(R.id.mTxtAgentNumber)
    TextViewSemiBold mTxtAgentNumber;
    @BindView(R.id.mTxtClientAddress)
    TextViewSemiBold mTxtClientAddress;
    @BindView(R.id.mTxtClientNumber)
    TextViewSemiBold mTxtClientNumber;
    @BindView(R.id.mTxtClientLandmark)
    TextViewSemiBold mTxtClientLandmark;
    @BindView(R.id.mTxtClientPinCode)
    TextViewSemiBold mTxtClientPinCode;
    @BindView(R.id.mTxtSimple)
    TextViewSemiBold mTxtSimple;
    @BindView(R.id.mTxtClientName)
    TextViewSemiBold mTxtClientName;

    @BindView(R.id.mImageBack)
    ImageView mImageBack;

    @BindView(R.id.mScrollView)
    ScrollView mScrollView;

    @BindView(R.id.mImgMap)
    ImageView mImgMap;

    @BindView(R.id.mTxtAuditType)
    TextViewSemiBold mTxtAuditType;

    @BindView(R.id.mImgCall)
    ImageView mImgCall;
    @BindView(R.id.mLayoutGetPdf)
    RelativeLayout mLayoutGetPdf;

    String mStrNotifyId;
    String mStrType;
    String mStrId, mStrTitle,mStrAuditType, mStrAssignBy, mStrAssignedDate, mStrAgentNumber, mStrmapUrl;

    ArrayList<String> listPdf = new ArrayList<>();
    HashMap<String, String> listPdfUrl = new HashMap<String, String>();
    DatabaseHelper db;
    File outputFile = null;

    DocumentList documentList;
    ArrayList<String> mListItems = new ArrayList<>();
    ProgressDialog dialog;

    /*permission list*/
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    /*click for going back*/
    @OnClick(R.id.mImgMap)
    public void mOpanMap() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mStrmapUrl));
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        ButterKnife.bind(this);
        db = new DatabaseHelper(ActivityAuditDetail.this);
        /*for downloading data we have to use this*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

         /*permission check*/
        if (!hasPermissions(ActivityAuditDetail.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(ActivityAuditDetail.this, PERMISSIONS, PERMISSION_ALL);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(CommonUtils.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(CommonUtils.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(CommonUtils.PUSH_NOTIFICATION)) {
                    mStrNotifyId = intent.getStringExtra("mStrNotifyId");
                    mStrType = intent.getStringExtra("mStrType");
                }
            }
        };

        /*to get id and type from previous screen*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStrNotifyId = bundle.getString("mStrNotifyId");
            mStrType = bundle.getString("mStrType");
        }



         /* Internet Connectivity Check */
        if (mCheckSignalStrength(ActivityAuditDetail.this) == 2) {
            /*api call to display detail*/
            mScrollView.setVisibility(View.VISIBLE);
            showPDialog(ActivityAuditDetail.this);
            mToGetDetail();
        } else {
            mScrollView.setVisibility(View.GONE);
            showWarningPopupSingleButton(ActivityAuditDetail.this, getString(R.string.mTextFile_alert_no_internet), "Warning !");
        }
    }

    /*click for going back*/
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        finish();
    }

    /*click to downlaod attachmnet*/
    @OnClick(R.id.mLayoutGetPdf)
    public void mGetPdf() {
          /*for multiple and single file*/
        if (listPdf.size() > 1) {
            final Dialog dialog = new Dialog(ActivityAuditDetail.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.filetype_dialog);
            RecyclerView mListView = dialog.findViewById(R.id.mListView);
            TextViewSemiBold mTextCancel = dialog.findViewById(R.id.mTextCancel);
            mListItems.clear();
            for (int i = 0; i < listPdf.size(); i++) {
                mListItems.add(listPdf.get(i));
            }
            documentList = new DocumentList(ActivityAuditDetail.this, mListItems);
            LinearLayoutManager mLayoutNewsManager = new LinearLayoutManager(ActivityAuditDetail.this, LinearLayoutManager.HORIZONTAL, false);
            mListView.setLayoutManager(mLayoutNewsManager);
            mListView.setAdapter(documentList);
            /*item click to download single item*/
            mListView.addOnItemTouchListener(
                    new RecyclerItemClickListener(
                            ActivityAuditDetail.this,
                            mListView,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    dialog.dismiss();
                                    mShowAlert(getString(R.string.mTextFile_alert_file_downloading), ActivityAuditDetail.this);
                                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listPdfUrl.get(listPdf.get(position))));
                                    startActivity(browserIntent);*/

                                    /*for downloading file and checking file type*/
                                    String mFileType = listPdfUrl.get(listPdf.get(position));
                                    System.out.println("<><><>mFileType" + mFileType);
                                    if (mFileType.contains(".pdf")) {
                                        String mFilePath = mStrDownloadPath + getString(R.string.mTextFile_filefolder) + listPdf.get(position);
                                        File file = new File(mFilePath);
                                        if (file.exists()) {
                                            openFile(file, ".pdf");
                                            System.out.println("<><>file is already there");
                                        } else {
                                            //downloadFile(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_filefolder), listPdf.get(position), ".pdf");

                                            new DownloadFileFromURL().execute(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_filefolder), listPdf.get(position));
                                            System.out.println("<><><Not find file ");
                                        }
                                    } else if (mFileType.contains(".docx") || mFileType.contains(".doc")) {
                                        String mFilePath = mStrDownloadPath + getString(R.string.mTextFile_filefolder) + listPdf.get(position);
                                        File file = new File(mFilePath);
                                        if (file.exists()) {
                                            openFile(file, ".doc");
                                        } else {
                                            // downloadFile(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_filefolder), listPdf.get(position), ".doc");
                                            new DownloadFileFromURL().execute(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_filefolder), listPdf.get(position));
                                        }
                                    } else if (mFileType.contains(".png") || mFileType.contains(".jpg") || mFileType.contains(".jpeg")) {
                                        String mFilePath = mStrDownloadPath + getString(R.string.mTextFile_mediaFolder) + listPdf.get(position);
                                        File file = new File(mFilePath);
                                        if (file.exists()) {
                                            openFile(file, ".png");
                                        } else {
                                            // downloadFile(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_mediaFolder), listPdf.get(position), ".png");
                                            new DownloadFileFromURL().execute(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_mediaFolder), listPdf.get(position));
                                        }
                                    } else if (mFileType.contains(".xls") || mFileType.contains(".xlsx")) {
                                        String mFilePath = mStrDownloadPath + getString(R.string.mTextFile_filefolder) + listPdf.get(position);
                                        File file = new File(mFilePath);
                                        if (file.exists()) {
                                            openFile(file, ".xls");
                                        } else {
                                            //downloadFile(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_filefolder), listPdf.get(position), ".xls");
                                            new DownloadFileFromURL().execute(listPdfUrl.get(listPdf.get(position)), getString(R.string.mTextFile_mediaFolder), listPdf.get(position));

                                        }
                                    }
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                }
                            }
                    ));
            mTextCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (listPdf.size() > 0) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listPdfUrl.get(listPdf.get(0))));
            startActivity(browserIntent);
        } else {
            mShowAlert("No attachment available", ActivityAuditDetail.this);
        }
    }

    /*to reject audit*/
    @OnClick(R.id.mLayoutReject)
    public void mRejectAudit() {
        final Dialog dialog = new Dialog(ActivityAuditDetail.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning_popup_two_button);
        dialog.setCancelable(false);
        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
        TextViewSemiBold mTextTitle = dialog.findViewById(R.id.mTextTitle);
        RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
        RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
        mTextTitle.setText("Alert!");
        mTxtMsg.setText("Are you sure you want to reject this audit?");
        mLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        mLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPDialog(ActivityAuditDetail.this);
                mToGetStatus("2");
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /*to accept audit*/
    @OnClick(R.id.mLayoutAccept)
    public void mAcceptAudit() {
        showPDialog(ActivityAuditDetail.this);
        mToGetStatus("1");

    }

    /*for calling number*/
    @OnClick(R.id.mImgCall)
    public void mGoToCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mStrAgentNumber));
        if (ActivityCompat.checkSelfPermission(ActivityAuditDetail.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    /*api cal to get audit detail*/
    void mToGetDetail() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "notificationDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                JSONObject jsonObjectAudit = jsonObject.getJSONObject("auditDetails");
                                mStrId = jsonObjectAudit.getString("id");
                                mStrTitle = jsonObjectAudit.getString("title");
                                mStrAssignedDate = jsonObjectAudit.getString("assign_date");
                                mStrAuditType = jsonObjectAudit.getString("auditType");

                                if(mStrAuditType.equals("1")){
                                mTxtAuditType.setText("Type: Super User Auditer");
                                }else {
                                    if (PreferenceManager.getFormiiUserRole(ActivityAuditDetail.this).equals("0")) {
                                        mTxtAuditType.setText("Type: Auditor");
                                    } else {
                                        mTxtAuditType.setText("Type: Inspector");
                                    }
                                }


                                mTxtAuditTitle.setText(mStrTitle);
                                mTxtAuditDetail.setText(jsonObjectAudit.getString("description"));

                                //mTxtAuditTime.setText(getString(R.string.mTextFile_time) + " " + mTogetTime(jsonObjectAudit.getString("time")));
                                mTxtAuditDeuDate.setText(getString(R.string.mTextFile_due_date) + " " + CommonUtils.mTogetDay(mStrAssignedDate));
                                mTxtAuditArea.setText(getString(R.string.mTextFile_area) + " " + jsonObjectAudit.getString("city"));

                                JSONObject jsonObjectAgent = jsonObject.getJSONObject("agentDetails");
                                mStrAssignBy = jsonObjectAgent.getString("fulname");
                                mTxtAgentName.setText(getString(R.string.mTextFile_name) + " " + jsonObjectAgent.getString("fulname"));
                                mTxtAgentEmail.setText(getString(R.string.mTextFile_email) + " " + jsonObjectAgent.getString("email"));
                                Glide.with(ActivityAuditDetail.this).load(jsonObjectAgent.getString("photo")).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImgAgentProfile) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ActivityAuditDetail.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        mImgAgentProfile.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                                mStrAgentNumber = jsonObjectAgent.getString("phone");
                                mTxtAgentNumber.setText(getString(R.string.mTextFile_number) + " " + jsonObjectAgent.getString("phone"));
                                JSONObject jsonObjectContact = jsonObject.getJSONObject("contactPerson");
                                mTxtClientName.setText(getString(R.string.mTextFile_name) + " " + jsonObjectContact.getString("name"));
                                mTxtClientAddress.setText(getString(R.string.mTextFile_address) + " " + jsonObjectContact.getString("address"));
                                mTxtClientNumber.setText(getString(R.string.mTextFile_number) + " " + jsonObjectContact.getString("phone"));
                                mTxtClientLandmark.setText(getString(R.string.mTextFile_landmark) + " " + jsonObjectContact.getString("landmark"));
                                mTxtClientPinCode.setText(getString(R.string.mTextFile_pincode) + " " + jsonObjectContact.getString("pincode"));

                                mStrmapUrl = jsonObjectContact.getString("mapurl");
                                Object intervention = jsonObjectContact.get("document");
                                if (intervention instanceof JSONArray) {
                                    JSONArray jsonArray = jsonObjectContact.getJSONArray("document");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            listPdfUrl.put(jsonArray.getString(i).substring(jsonArray.getString(i).lastIndexOf('/') + 1), jsonArray.getString(i));
                                            listPdf.add(jsonArray.getString(i).substring(jsonArray.getString(i).lastIndexOf('/') + 1));
                                        }
                                    }
                                } else {
                                }
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityAuditDetail.this);
                            } else {
                                mShowAlert(response.getString("message"), ActivityAuditDetail.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(ActivityAuditDetail.this, getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityAuditDetail.this));
                params.put("notify_id", mStrNotifyId);
                params.put("type", mStrType);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityAuditDetail.this));
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    /*api call to get sudit status*/
    void mToGetStatus(final String status) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getAuditStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                mShowAlert(response.getString("message"), ActivityAuditDetail.this);
                                if (status.equals("1")) {
                                    Audit audit = new Audit();
                                    audit.setmUserId(PreferenceManager.getFormiiId(ActivityAuditDetail.this));
                                    audit.setmTitle(mStrTitle);
                                    audit.setmAuditId(mStrId);
                                    audit.setmStatus("0");
                                    audit.setmDueDate(mStrAssignedDate);
                                    audit.setmAssignBy(mStrAssignBy);
                                    audit.setmAuditType(mStrAuditType);
                                    funInsertAllAudit(audit, db);
                                    Intent intent = new Intent(ActivityAuditDetail.this, ActivityTabHostMain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("mStrCurrentTab", "4");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(ActivityAuditDetail.this, ActivityTabHostMain.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mStrCurrentTab", "0");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 3000);

                                }
                            } else if (mStrStatus.equals("1")) {
                                CommonUtils.showSessionExp(ActivityAuditDetail.this);
                            } else {
                                mShowAlert(response.getString("message"), ActivityAuditDetail.this);
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
                        Toast.makeText(ActivityAuditDetail.this, getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityAuditDetail.this));
                params.put("audit_id", mStrId);
                params.put("status", status);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityAuditDetail.this));
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    /*getting day formate*/


    /*getting am pm formate*/
    public static String mTogetTime(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("hh:mm:ss").parse(date);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        return timeFormatter.format(dateTime);
    }

    /*downloading file*/
    private void downloadFile(String urlString, String mFolder, String mFileName, String mExtension) {
        try {
            showPDialog(ActivityAuditDetail.this);
            URL url = new URL(urlString);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            String PATH = mStrDownloadPath + mFolder;
            File file = new File(PATH);
            file.mkdirs();
//            String name = String.valueOf(System.currentTimeMillis()) + mExtension;
//            String name = mFileName + mExtension;
            File outputFile = new File(file, mFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[4096];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
            //openfile
            openFile(outputFile, mExtension);
            Toast.makeText(this, R.string.mTextfile_alert_new_download, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ActivityAuditDetail.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.show();
            // showPDialog(ActivityAuditDetail.this);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String PATH = mStrDownloadPath + f_url[1];
                File file = new File(PATH);
                file.mkdirs();
                outputFile = new File(file, f_url[2]);
                FileOutputStream fos = new FileOutputStream(outputFile);
                System.out.println("<><><outputFile" + outputFile.getPath());
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    fos.write(data, 0, count);
                }
                fos.flush();
                fos.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            dialog.dismiss();
            return f_url[0];
        }

        protected void onProgressUpdate(String... progress) {
            System.out.println("<><><" + Integer.parseInt(progress[0]));
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String mFileType) {
            System.out.println("<><><mFileType" + mFileType);
            System.out.println("<><><outputFile" + outputFile.getPath());
            // hidePDialog();
            if (mFileType.contains(".pdf")) {
                mFileType = ".pdf";
            } else if (mFileType.contains(".docx") || mFileType.contains(".doc")) {
                mFileType = ".doc";
            } else if (mFileType.contains(".png") || mFileType.contains(".jpg") || mFileType.contains(".jpeg")) {
                mFileType = ".png";
            } else if (mFileType.contains(".xls") || mFileType.contains(".xlsx")) {
                mFileType = ".xls";
            }
            openFile(outputFile, mFileType);
        }

    }


    /*open downloaded file*/
    protected void openFile(File file, String mType) {
        hidePDialog();
        try {
            System.out.println("<><>file" + file);
            System.out.println("<><>mType" + mType);
            if (mType.contains(".pdf")) {
                Intent intentPDF = new Intent(Intent.ACTION_VIEW);
                intentPDF.setDataAndType(Uri.fromFile(file), "application/pdf");
                startActivity(intentPDF);
            } else if (mType.contains(".docx") || mType.contains(".doc")) {
                Intent intentTxt = new Intent(Intent.ACTION_VIEW);
                intentTxt.setDataAndType(Uri.fromFile(file), "text/plain");
                startActivity(intentTxt);
            } else if (mType.contains(".png") || mType.contains(".jpg")) {
                Intent intentImage = new Intent(Intent.ACTION_VIEW);
                intentImage.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intentImage);
            } else if (mType.contains(".xls") || mType.contains(".xlsx")) {
                Intent intentExcel = new Intent(Intent.ACTION_VIEW);
                intentExcel.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                startActivity(intentExcel);
            }
        } catch (Exception e) {
            Toast.makeText(ActivityAuditDetail.this, R.string.mTextFile_alert_no_handler, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(CommonUtils.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(CommonUtils.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                if (!hasPermissions(ActivityAuditDetail.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ActivityAuditDetail.this, PERMISSIONS, PERMISSION_ALL);
                }
                mShowAlert(getString(R.string.mTextFile_alert_deny_permission), ActivityAuditDetail.this);
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                } else {
                    //set to never ask again
                    if (!hasPermissions(ActivityAuditDetail.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(ActivityAuditDetail.this, PERMISSIONS, PERMISSION_ALL);
                    }
                }
            }
        }
    }
}