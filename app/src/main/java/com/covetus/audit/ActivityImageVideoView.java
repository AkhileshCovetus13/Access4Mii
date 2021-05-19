package com.covetus.audit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mChatImgUrl;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.mStrChatFileDownloadPath;
import static ABS_HELPER.CommonUtils.mStrDownloadPath;
import static ABS_HELPER.CommonUtils.showPDialog;
import static ABS_HELPER.UploadHelper.copyFile;
import static ABS_HELPER.UploadHelper.getFileExt;

/**
 * Created by admin18 on 5/11/18.
 */

public class ActivityImageVideoView extends Activity {
    @BindView(R.id.imgPreview)
    ImageView imgPreview;
    @BindView(R.id.videoPreview)
    VideoView vidPreview;
    @BindView(R.id.mDocWebview)
    WebView mDocWebview;
    private String filePath = null;
    String isImage;

    ProgressDialog dialog;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(ActivityImageVideoView.this);
        pDialog = new ProgressDialog(ActivityImageVideoView.this);
        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        isImage = i.getStringExtra("isImage");
        int mType = i.getIntExtra("type", 0);
        System.out.println("<><><>mType" + mType);
        System.out.println("<><><>filePath" + filePath);

        if (filePath != null) {
            if (filePath.contains(".png") || filePath.contains(".jpg") || filePath.contains(".jpeg")) {
                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder) + filePath;
                File file = new File(mFilePath);
                if (file.exists()) {
                    previewMedia(isImage);
                } else {
                    new DownloadFileFromURL().execute(filePath, getString(R.string.mTextFile_mediaFolder), String.valueOf(mType));
                }
            } else if (filePath.contains(".mp4")) {
                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder) + filePath;
                File file = new File(mFilePath);
                if (file.exists()) {
                    previewMedia(isImage);
                } else {
                    new DownloadFileFromURL().execute(filePath, getString(R.string.mTextFile_mediaFolder), String.valueOf(mType));
                }
            } else if (filePath.contains(".pdf") || filePath.contains(".doc") || filePath.contains(".docx") || filePath.contains(".xlsx") || filePath.contains(".xls")) {
                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_filefolder) + filePath;
                File file = new File(mFilePath);
                if (file.exists()) {
                    previewMedia(isImage);
                } else {
                    System.out.println("<><><> not exists" + filePath);
                    System.out.println("<><><>isImage" + isImage);
                    System.out.println("<><><>mFilePath" + mFilePath);
                    new DownloadFileFromURL().execute(filePath, getString(R.string.mTextFile_filefolder), String.valueOf(mType));
                }
            } else if (mType == 1) {
                imgPreview.setVisibility(View.VISIBLE);
                vidPreview.setVisibility(View.GONE);
                byte[] imageByteArray = Base64.decode(filePath, Base64.DEFAULT);
                Glide.with(ActivityImageVideoView.this).load(imageByteArray).asBitmap()
                        .placeholder(R.drawable.ic_placeholder_audit)
                        .thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPreview);
            } else if (mType == 3) {
                imgPreview.setVisibility(View.GONE);
                vidPreview.setVisibility(View.VISIBLE);
                showPDialog(ActivityImageVideoView.this);
                vidPreview.setVideoPath(filePath);
                vidPreview.start();
                vidPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer arg0) {
                        hidePDialog();
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.mtextFile_alert_no_filepath, Toast.LENGTH_LONG).show();
        }
    }

    private void previewMedia(String isImage) {
        try {
            if (isImage.contains("1")) {
                imgPreview.setVisibility(View.VISIBLE);
                vidPreview.setVisibility(View.GONE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                System.out.println("<><><>isImage" + filePath);
                if (filePath.contains(".png") || filePath.contains(".jpg") || filePath.contains(".jpeg")) {
                    Glide.with(getApplicationContext()).load(mChatImgUrl + filePath).placeholder(R.drawable.ic_placeholder_audit).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPreview);
                } else {
                    byte[] imageByteArray = Base64.decode(filePath, Base64.DEFAULT);
                    Glide.with(getApplicationContext()).load(imageByteArray).asBitmap().placeholder(R.drawable.ic_placeholder_audit).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPreview);
                }

            } else if (isImage.contains("2")) {
                if (filePath.contains(".mp4")) {
                    imgPreview.setVisibility(View.GONE);
                    vidPreview.setVisibility(View.VISIBLE);
                    showPDialog(ActivityImageVideoView.this);
                    MediaController mediaController = new MediaController(ActivityImageVideoView.this);
                    vidPreview.setVideoURI(Uri.parse(mChatImgUrl + filePath));
                    vidPreview.setMediaController(mediaController);
                    vidPreview.start();
                    vidPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer arg0) {
                            hidePDialog();

                        }
                    });
                } else {
                    imgPreview.setVisibility(View.GONE);
                    vidPreview.setVisibility(View.VISIBLE);
                    showPDialog(ActivityImageVideoView.this);
                    vidPreview.setVideoPath(filePath);
                    vidPreview.start();
                    vidPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer arg0) {
                            hidePDialog();

                        }
                    });
                }
            } else if (isImage.contains("3")) {
                if (filePath.contains(".pdf") || filePath.contains(".doc") || filePath.contains(".docx") || filePath.contains(".xlsx") || filePath.contains(".xls")) {
                    String mDoc = mChatImgUrl + filePath;
                    setmDocWebview(mDoc);
                }
            } else if (isImage.contains("4")) {
                if (getFileExt(filePath).contains("pdf") || getFileExt(filePath).contains("doc") || getFileExt(filePath).contains("docx") || getFileExt(filePath).contains("xlsx") || getFileExt(filePath).contains("xls")) {
                    openLocalDoc(filePath);
                }
            } else if (isImage.contains("5")) {
                File logfile = new File(mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder) + filePath);
                if (logfile.exists()) {
                    imgPreview.setVisibility(View.VISIBLE);
                    vidPreview.setVisibility(View.GONE);
                    Glide.with(this).load(logfile).placeholder(R.drawable.ic_placeholder_audit).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPreview);
                } else {
                    mShowAlert(getString(R.string.mTextFile_alert_file_not_download), ActivityImageVideoView.this);
                }


            } else if (isImage.contains("6")) {
                File logfile = new File(mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder) + filePath);
                if (logfile.exists()) {
                    Uri uri = Uri.parse("file://" + logfile.getAbsolutePath());
                    System.out.println("<><>uri" + uri.toString());
                    imgPreview.setVisibility(View.GONE);
                    vidPreview.setVisibility(View.VISIBLE);
                    showPDialog(ActivityImageVideoView.this);
                    vidPreview.setVideoURI(uri);
                    vidPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer arg0) {
                     /*   hidePDialog();*/
                            vidPreview.start();
                        }
                    });
                } else {
                    mShowAlert(getString(R.string.mTextFile_alert_file_not_download), ActivityImageVideoView.this);
                }

            } else if (isImage.contains("7")) {
                File logfile = new File(mStrChatFileDownloadPath + getString(R.string.mTextFile_filefolder) + filePath);
                if (logfile.exists()) {
                    if (filePath.contains(".pdf")) {
                        Intent intentPDF = new Intent(Intent.ACTION_VIEW);
                        intentPDF.setDataAndType(Uri.fromFile(logfile), "application/pdf");
                        startActivity(intentPDF);
                    } else if (filePath.contains(".docx") || filePath.contains(".doc")) {
                        Intent intentTxt = new Intent(Intent.ACTION_VIEW);
                        intentTxt.setDataAndType(Uri.fromFile(logfile), "text/plain");
                        startActivity(intentTxt);
                    } else if (filePath.contains(".xls") || filePath.contains(".xlsx")) {
                        Intent intentExcel = new Intent(Intent.ACTION_VIEW);
                        intentExcel.setDataAndType(Uri.fromFile(logfile), "application/vnd.ms-excel");
                        startActivity(intentExcel);
                    }
                } else {
                    mShowAlert(getString(R.string.mTextFile_alert_file_not_download), ActivityImageVideoView.this);
                }

            }
        } catch (Exception e) {
            Toast.makeText(ActivityImageVideoView.this, R.string.mTextFile_alert_no_handler, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void setmDocWebview(String mDoc) {
        mDocWebview.setVisibility(View.VISIBLE);
        imgPreview.setVisibility(View.GONE);
        vidPreview.setVisibility(View.GONE);

        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setIcon(R.drawable.ic_access4mii_logo);
        pDialog.setMessage(getString(R.string.mTextFile_loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        mDocWebview.getSettings().setJavaScriptEnabled(true);
        mDocWebview.getSettings().setSupportZoom(true);
        mDocWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                System.out.println("<><><>filurl" + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
        mDocWebview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mDoc);
        /*mDocWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);*/
    }

    public void openLocalDoc(String mDoc) {
        try {
            System.out.println("<><><>filePath...." + getFileExt(filePath));
            if (getFileExt(filePath).contains("pdf")) {
                Intent intentPDF = new Intent(Intent.ACTION_VIEW);
                intentPDF.setDataAndType(Uri.parse(mDoc), "application/pdf");
                startActivity(intentPDF);
            } else if (getFileExt(filePath).contains("docx") || getFileExt(filePath).contains("doc")) {
                Intent intentTxt = new Intent(Intent.ACTION_VIEW);
                intentTxt.setDataAndType(Uri.parse(mDoc), "text/plain");
                startActivity(intentTxt);
            } else if (getFileExt(filePath).contains("xls") || getFileExt(filePath).contains("xlsx")) {
                Intent intentExcel = new Intent(Intent.ACTION_VIEW);
                intentExcel.setDataAndType(Uri.parse(mDoc), "application/vnd.ms-excel");
                startActivity(intentExcel);
            }
        } catch (Exception e) {
            mShowAlert(getString(R.string.mTextFile_alert_cannot_open), ActivityImageVideoView.this);
            e.printStackTrace();
        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showPDialog(ActivityImageVideoView.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);
            dialog.setMax(100);
            dialog.show();
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(mChatImgUrl + f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String PATH = mStrChatFileDownloadPath + f_url[1];
                //for hidden folder
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, f_url[0]);
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
                if (f_url[2].equals("2") || f_url[2].equals("4") || f_url[2].equals("7")) {
                    //copying to new folder
                    String mfilePath = mStrDownloadPath + f_url[1];
                    File root = new File(mfilePath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File mNewFile = new File(root, f_url[0]);
                    System.out.println("<><><file" + mNewFile.getPath());
                    FileOutputStream out = new FileOutputStream(mNewFile);
                    out.flush();
                    out.close();
                    copyFile(outputFile, mNewFile);
                }


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
            //System.out.println("<><><outputFile" + outputFile.getPath());
            // hidePDialog();
            previewMedia(isImage);
            Toast.makeText(ActivityImageVideoView.this, R.string.mTextfile_alert_new_download, Toast.LENGTH_LONG).show();
        }

    }


    public boolean isFilePresent(String fileName) {
        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        dialog.cancel();
        pDialog.dismiss();
        pDialog.cancel();
    }
}