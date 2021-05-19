/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.covetus.audit.R;
import com.covetus.audit.SelectMainLocationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
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
import static ABS_HELPER.CommonUtils.mStrDownloadPath;
import static ABS_HELPER.CommonUtils.showPDialog;
import static Modal.AuditListModal.funGetActiveAudit;
import static Modal.AuditListModal.funUpdateAudit;
import static Modal.MainLocationModal.funGetAllSelectedMainLocation;
import static Modal.SubFolderModal.funGetAllSubFolderLayer;
import static Modal.SubFolderModal.funGetAllSubFolders;
import static Modal.SubLocationLayer.update_tb_image_sub_location_explation_list_by_layer_id_resync;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;


public class AudittListHistoryAd extends BaseAdapter {

    private ArrayList<Audit> mListItems = new ArrayList<>();
    Activity context;
    ProgressDialog dialog;
    String mFileName;
    DatabaseHelper db;
    public AudittListHistoryAd(Activity context, ArrayList<Audit> mListItems) {
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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_audit_list_history, null);
            holder = new ViewHolder();
            holder.mTxtAuditDate = convertView.findViewById(R.id.mTxtAuditDate);
            holder.mTxtAuditTitle = convertView.findViewById(R.id.mTxtAuditTitle);
            holder.mTxtAgentName = convertView.findViewById(R.id.mTxtAgentName);
            holder.mLayoutMain = convertView.findViewById(R.id.mLayoutMain);
            holder.imgPreviewReport = convertView.findViewById(R.id.imgPreviewReport);
            holder.imgReEditAudit = convertView.findViewById(R.id.imgReEditAudit);
            holder.imgReportFinal = convertView.findViewById(R.id.imgReportFinal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        db = new DatabaseHelper(context);
        final Audit audit = mListItems.get(position);
        holder.mTxtAuditDate.setText(audit.getmDueDate());
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
            holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.red_border));
        } else if (prcent == 100) {
            holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.green_border));
        } else {
            holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.yellow_border));
        }


       /* if (audit.getmStatus().equals("0")) {
            holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.red_border));
        } else {
            holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.yellow_border));
        }*/


       if(funGetActiveAudit("3",audit.getmAuditId(),db)>0){
           holder.imgReportFinal.setVisibility(View.GONE);
           holder.imgPreviewReport.setVisibility(View.GONE);
           holder.imgReEditAudit.setVisibility(View.GONE);
           holder.mLayoutMain.setBackground(context.getResources().getDrawable(R.drawable.green_border));
       }else {
           holder.imgReportFinal.setVisibility(View.VISIBLE);
           holder.imgPreviewReport.setVisibility(View.VISIBLE);
           holder.imgReEditAudit.setVisibility(View.VISIBLE);
       }


        holder.imgReportFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                dialog.setCancelable(false);
                TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                mTxtMsg.setText("Are yousure want to finalize this audit and request for audit report?");
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
                        showPDialog(context);
                        mToDoFinalSync(audit.getmAuditId());


                    }
                });
                dialog.show();




            }
        });



        holder.imgPreviewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(audit.getmAuditType().equals("0")){

                    if(PreferenceManager.getFormiiUserRole(context).equals("0")){

                        if(!TextUtils.isEmpty(audit.getmStrAuditerReport()) || audit.getmStrAuditerReport()!=null || !audit.getmStrAuditerReport().isEmpty() || audit.getmStrAuditerReport().length()>0){
                            mShowAlert("Downloading...", context);
                            String mSlug=audit.getmTitle()+audit.getmAuditId().toString().trim()+".pdf";
                            if(mSlug.contains("/"))
                                mSlug=mSlug.replace("/", "-");
                            String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                            File file = new File(mFilePath);
                            new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrAuditerReport(), mSlug);
                        /*if (file.exists()) {
                            showPdf(file);
                        } else {
                            new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrAuditerReport(), mSlug);
                        }*/
                        }

                    }else {

                        if(!TextUtils.isEmpty(audit.getmStrInspectorReport()) || audit.getmStrInspectorReport()!=null || !audit.getmStrInspectorReport().isEmpty() || audit.getmStrInspectorReport().length()>0){
                            mShowAlert("Downloading...", context);
                            String mSlug=audit.getmTitle()+audit.getmAuditId().toString().trim()+".pdf";
                            if(mSlug.contains("/"))
                                mSlug=mSlug.replace("/", "-");
                            String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                            File file = new File(mFilePath);
                            new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrInspectorReport(), mSlug);
                        /*if (file.exists()) {
                            showPdf(file);
                        } else {
                            new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrAuditerReport(), mSlug);
                        }*/
                        }

                    }

                }else {



                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                    dialog.setCancelable(true);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    TextViewSemiBold mTextTitle = dialog.findViewById(R.id.mTextTitle);
                    TextViewSemiBold mTextInspectorButton = dialog.findViewById(R.id.mTextInspectorButton);
                    TextViewSemiBold mTextAuditerButton = dialog.findViewById(R.id.mTextAuditerButton);
                    RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                    RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                    mTxtMsg.setText("Witch type of file you want to open..");
                    mTextTitle.setText("Select One");
                    mTextInspectorButton.setText("Inspector");
                    mTextAuditerButton.setText("Auditor");
                    mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_signin_button);
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ins

                            if(audit.getmStrInspectorReport()!=null || !audit.getmStrInspectorReport().isEmpty() || audit.getmStrInspectorReport().length()>0){
                                mShowAlert("Downloading...", context);
                                String mSlug=audit.getmTitle()+audit.getmAuditId().toString().trim()+".pdf";
                                if(mSlug.contains("/"))
                                    mSlug=mSlug.replace("/", "-");
                                String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                                File file = new File(mFilePath);
                                new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrInspectorReport(), mSlug);

                                /*if (file.exists()) {
                                    showPdf(file);
                                } else {
                                    new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrInspectorReport(), mSlug);
                                }*/
                            }
                            dialog.cancel();


                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        //aud


                            System.out.println("<><><>### "+audit.getmStrAuditerReport());
                            if(audit.getmStrAuditerReport()!=null || audit.getmStrAuditerReport().length()>0){
                                mShowAlert("Downloading...", context);
                                String mSlug=audit.getmTitle()+audit.getmAuditId().toString().trim()+".pdf";
                                if(mSlug.contains("/"))
                                    mSlug=mSlug.replace("/", "-");
                                String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                                File file = new File(mFilePath);
                                new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrAuditerReport(), mSlug);

                                /*if (file.exists()) {
                                    showPdf(file);
                                } else {
                                    new AudittListHistoryAd.DownloadFileFromURL().execute(audit.getmStrAuditerReport(), mSlug);
                                }*/
                            }
                            dialog.cancel();


                        }
                    });
                    dialog.show();
                }
            }
        });

        holder.imgReEditAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHelper(context);
                if (funGetActiveAudit("1", PreferenceManager.getFormiiId(context), db) > 0) {
                CommonUtils.mShowAlert(context.getString(R.string.mTextFile_error_sync), context);
                }else{
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                    dialog.setCancelable(false);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                    RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                    mTxtMsg.setText("You want to re edit audit");
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Audit mAudit = new Audit();
                            mAudit.setmAuditId(audit.getmAuditId());
                            mAudit.setmStatus("1");
                            funUpdateAudit(mAudit,"5",db);
                            update_tb_image_sub_location_explation_list_by_layer_id_resync(audit.getmAuditId(),audit.getmUserId(),db);
                            String mAuditId = audit.getmAuditId();
                            SelectedLocation selectedLocation = new SelectedLocation();
                            selectedLocation.setmStrAuditId(audit.getmAuditId());
                            selectedLocation.setmStrUserId(audit.getmUserId());
                            Intent intent = new Intent(context, SelectMainLocationActivity.class);
                            intent.putExtra("mAuditId", mAuditId);
                            context.startActivity(intent);
                            dialog.cancel();

                        }
                    });
                    dialog.show();
                }




            }
        });




        return convertView;
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





    private class ViewHolder {
        TextViewSemiBold mTxtAuditDate;
        TextViewSemiBold mTxtAuditTitle;
        TextViewSemiBold mTxtAgentName;
        RelativeLayout mLayoutMain;
        ImageView imgPreviewReport,imgReEditAudit,imgReportFinal;

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.show();
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
                String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder);
                File file = new File(mFilePath);
                file.mkdirs();
                System.out.println("<><><><>call "+f_url[1]);
                mFileName = "Report_" + f_url[1].replace(" ","");
                File outputFile = new File(file, mFileName);
                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            dialog.dismiss();
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder);
            File file = new File(mFilePath);
            File outputFile = new File(file, mFileName);

            //  hidePDialog();

            System.out.println("<><><><>call "+outputFile.getAbsolutePath());

            Uri path = Uri.fromFile(outputFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try{
                context.startActivity(pdfIntent);
            }
            catch(ActivityNotFoundException e){
                Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
            //File file = null;
            //showPdf(file);
        }

    }

    public void showPdf(File mFile) {

        try {






          if (mFile == null) {
                File file = new File(mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + mFileName);
                Intent intentPDF = new Intent(Intent.ACTION_VIEW);
                intentPDF.setDataAndType(Uri.fromFile(file), "application/pdf");
                context.startActivity(intentPDF);
            } else {
                Intent intentPDF = new Intent(Intent.ACTION_VIEW);
                intentPDF.setDataAndType(Uri.fromFile(mFile), "application/pdf");
                context.startActivity(intentPDF);
            }


        } catch (Exception e) {
            mShowAlert(context.getString(R.string.mTextFile_alert_no_handler), context);
            e.printStackTrace();
        }
    }



    void mToDoFinalSync(final String mAuditId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "finalsync",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                Audit audit = new Audit();
                                audit.setmAuditId(mAuditId);
                                audit.setmStatus("3");
                                funUpdateAudit(audit, "1", db);
                                notifyDataSetChanged();

                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), context);
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
                        System.out.println("<><><>error" + error.toString());
                        Toast.makeText(context, context.getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",PreferenceManager.getFormiiId(context));
                params.put("audit_id", mAuditId);
                params.put("auth_token",PreferenceManager.getFormiiAuthToken(context));
                System.out.println("<><><>pra "+params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    File downloadFile(String dwnload_file_path) {
        Random r = new Random();
        int i1 = r.nextInt(45 - 28) + 28;
        File file = null;
        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory();
            file = new File(SDCardRoot, i1+".pdf");
            FileOutputStream fileOutput = new FileOutputStream(file);
            fileOutput.close();
        } catch (final MalformedURLException e) {

        } catch (final IOException e) {

        } catch (final Exception e) {

        }
        return file;
    }



}

