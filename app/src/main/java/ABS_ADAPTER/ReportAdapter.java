/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.covetus.audit.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Report;

import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.mStrDownloadPath;


public class ReportAdapter extends BaseAdapter {

    private ArrayList<Report> mListItems = new ArrayList<>();
    Activity context;
    String mFileName;
    ProgressDialog dialog;

    public ReportAdapter(Activity context, ArrayList<Report> mListItems) {
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
            convertView = mInflater.inflate(R.layout.item_report_list, null);
            holder = new ViewHolder();
            holder.mImgPdf = convertView.findViewById(R.id.mImgPdf);
            holder.mTextAgentName = convertView.findViewById(R.id.mTextAgentName);
            holder.mTextAuditDate = convertView.findViewById(R.id.mTextAuditDate);
            holder.mTextAuditTitle = convertView.findViewById(R.id.mTextAuditTitle);
            holder.mTextAuditDesc = convertView.findViewById(R.id.mTextAuditDesc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Report report = mListItems.get(position);
        holder.mTextAgentName.setText("Agent Name: " + report.getmAgentName());
        holder.mTextAuditDate.setText("Assigned Date: " + report.getmAuditStartDate());
        holder.mTextAuditTitle.setText("Audit Title: " + report.getmAuditTitle());
        holder.mTextAuditDesc.setText(report.getmAuditDesc());


        holder.mImgPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (report.getmAuditUserType().equals("1")) {


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
                    mTxtMsg.setText("Witch type of report you want to open..");
                    mTextTitle.setText("Select One");
                    mTextInspectorButton.setText("Inspector");
                    mTextAuditerButton.setText("Auditor");
                    mLayoutNo.setBackgroundResource(R.drawable.rounded_corner_signin_button);
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ins

                            if (report.getmAuditInsReport() != null || !report.getmAuditInsReport().isEmpty() || report.getmAuditInsReport().length() > 0) {
                                mShowAlert("Downloading...", context);
                                String mSlug = report.getmAuditTitle() + report.getmAuditId().toString().trim();
                                if (mSlug.contains("/"))
                                    mSlug = mSlug.replace("/", "-");
                                String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                                File file = new File(mFilePath);
                                new ReportAdapter.DownloadFileFromURL().execute(report.getmAuditInsReport(), mSlug);

                               /* if (file.exists()) {
                                    showPdf(file);
                                } else {
                                    new ReportAdapter.DownloadFileFromURL().execute(report.getmAuditInsReport(), mSlug);
                                }*/
                            }
                            dialog.cancel();


                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //aud


                            System.out.println("<><><>### " + report.getmAuditAuditerReport());
                            if (report.getmAuditAuditerReport() != null || report.getmAuditAuditerReport().length() > 0) {
                                mShowAlert("Downloading...", context);
                                String mSlug = report.getmAuditTitle() + report.getmAuditId().toString().trim();
                                if (mSlug.contains("/"))
                                    mSlug = mSlug.replace("/", "-");
                                String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                                File file = new File(mFilePath);
                                new ReportAdapter.DownloadFileFromURL().execute(report.getmAuditAuditerReport(), mSlug);

                                /*if (file.exists()) {
                                    showPdf(file);
                                } else {
                                    new ReportAdapter.DownloadFileFromURL().execute(report.getmAuditAuditerReport(), mSlug);
                                }*/
                            }
                            dialog.cancel();


                        }
                    });
                    dialog.show();


                } else {
                    if (report.getmAuditReport().equals("")) {
                        mShowAlert("No PDF available", context);
                    } else {
                        mShowAlert("Downloading...", context);
                        String mSlug = report.getmAuditReportSlug();
                        if (mSlug.contains("/"))
                            mSlug = mSlug.replace("/", "-");
                        String mFilePath = mStrDownloadPath + context.getString(R.string.mTextFile_filefolder) + "Report_" + mSlug;
                        File file = new File(mFilePath);
                        new DownloadFileFromURL().execute(report.getmAuditReport(), mSlug);

                      /*  if (file.exists()) {
                            showPdf(file);
                        } else {

                        }*/


                    }
                }


            }
        });
        return convertView;
    }


    private class ViewHolder {
        TextViewRegular mTextAuditDesc;
        TextViewBold mTextAgentName, mTextAuditDate, mTextAuditTitle;
        ImageView mImgPdf;

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
                mFileName = "Report_" + f_url[1];
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
            System.out.println("<><><><>call " + file_url);
            //  hidePDialog();
            File file = null;
            showPdf(file);
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

}

