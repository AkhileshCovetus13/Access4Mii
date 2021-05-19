package ABS_HELPER;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.covetus.audit.ActivityLogin;
import com.covetus.audit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;

/**
 * Created by covetus on 9/10/18.
 */

public class CommonUtils {
    public static ProgressDialog pDialog;

    //public static String mBaseUrl = "http://dev.covetus.com/audit/";
    public static String mNewBaseUrl = "https://access4mii.com/";
    public static String mBaseUrl = "https://access4mii.com/";

    public static String      mStrBaseUrl = mBaseUrl + "api/v1/";
    public static String mStrNewBaseUrl = mNewBaseUrl + "api/v1/";

    //public static String CHAT_SERVER_URL = "http://dev.covetus.com:8090/";
    public static String CHAT_SERVER_NEW_URL = "https://chat.access4mii.com";


    public static String mStrBasenEWUrlImage = "https://access4mii.com/";
    public static String mStrBaseUrlImage = "https://access4mii.com/";
    //public static String mStrBaseUrlImage = "http://dev.covetus.com/audit/";

    public static String mChatImgOldUrl = mNewBaseUrl+"storage/app/public/profilePic/chatfile/";
    public static String mChatImgUrl =mBaseUrl+ "storage/app/public/profilePic/chatfile/";


    public static String mAboutUs = mBaseUrl + "about";
    public static String mTermNdCond = mBaseUrl + "topic/terms";
    public static String mNews = mBaseUrl + "news";
    public static String mStandard = mBaseUrl + "standard";
    public static String mHelp = mBaseUrl + "help";


    public static final String FOLDER = "ABS";
    public static String mStrDownloadPath = Environment.getExternalStorageDirectory().toString() + "/ABS/";
    public static String mStrChatFileDownloadPath = Environment.getExternalStorageDirectory().toString() + "/.ABSAudit";


    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SHARED_PREF = "audit_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    /*alert msg*/
    public static void mShowAlert(String str, Activity context) {
        TSnackbar snackbar = TSnackbar.make(context.findViewById(android.R.id.content), str, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorPrimary));
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, 70);
        params.width = FrameLayout.LayoutParams.FILL_PARENT;
        snackbarView.setLayoutParams(params);
        snackbarView.setPadding(0, 0, 0, 0);
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    /*click animation*/
    public static void OnClick(Activity activity, View view) {
        Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.click);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
    }

    /*hide loader*/
    public static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    /*hide loader*/
    public static void closePDialog() {

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog.cancel();
        }
    }

    /*showPDialog loader*/
    public static void showPDialog(Context context) {
        pDialog = new ProgressDialog(context, R.style.CustomDialogTheme);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
        pDialog.setContentView(R.layout.my_progress);
        ImageView mImgBlue = pDialog.findViewById(R.id.mImgBlue);
        RelativeLayout mLayoutGreen = pDialog.findViewById(R.id.mLayoutGreen);
        Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.rotating);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.revers);
        mImgBlue.startAnimation(animation1);
        mLayoutGreen.startAnimation(animation2);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void showPDialogMsg(Context context) {
        pDialog = new ProgressDialog(context, R.style.CustomDialogTheme);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
        pDialog.setContentView(R.layout.my_progress_msg);
        ImageView mImgBlue = pDialog.findViewById(R.id.mImgBlue);
        RelativeLayout mLayoutGreen = pDialog.findViewById(R.id.mLayoutGreen);
        Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.rotating);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.revers);
        mImgBlue.startAnimation(animation1);
        mLayoutGreen.startAnimation(animation2);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    /*auth token expire*/
    public static void showSessionExp(final Activity context) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_session_exp);
        dialog.setCancelable(false);
        TextViewSemiBold mCancel = dialog.findViewById(R.id.mCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.cleanData(context);
                Intent intent = new Intent(context, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                context.finish();
                dialog.cancel();
            }
        });
        dialog.show();
    }


    /*warning popup*/
    public static void showWarningPopupSingleButton(final Activity context, String mAlertMsg, String mPopTitle) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning_popup_single_button);
        dialog.setCancelable(false);
        RelativeLayout mLayoutOk = dialog.findViewById(R.id.mLayoutOk);
        TextViewSemiBold mTextTitle = dialog.findViewById(R.id.mTextTitle);
        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
        mTextTitle.setText(mPopTitle);
        mTxtMsg.setText(mAlertMsg);
        mLayoutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*keyboard close*/
    public static void closeKeyBoard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*permission check*/
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


 /*   public static boolean checkInternetConnection(Activity context) {
        boolean status = false;
        ConnectivityManager CManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        try {
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
                if (InetAddress.getByName("https://www.google.co.in").isReachable(5000))
                {
                    status = true;
                }
                else
                {
                    status = false;
                }
      }
            } catch (IOException e) {
                e.printStackTrace();
            }


        return status;
    }*/


    public static int mCheckSignalStrength(Activity context) {
        int mSignalStrength = 0;
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            /*1-poor, 2-good*/
            switch (level) {
                case 0:
                    mSignalStrength = 1;
                    Log.d("LEVEL", "None");
                    break;
                case 1:
                    mSignalStrength = 1;
                    Log.d("LEVEL", "Poor");
                    break;
                case 2:
                    mSignalStrength = 2;
                    Log.d("LEVEL", "Moderate");
                    break;
                case 3:
                    mSignalStrength = 2;
                    Log.d("LEVEL", "Good");
                    break;
                case 4:
                    mSignalStrength = 2;
                    Log.d("LEVEL", "Excellent");
                    break;
            }
        } else if (mobile.isConnectedOrConnecting()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return mSignalStrength;
            }
            CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            cellSignalStrengthGsm.getDbm();
            int level = cellSignalStrengthGsm.getDbm();
            if (level <= 0 && level >= -50) {
                mSignalStrength = 2;
                Log.d("LEVEL", "Best signal");
            } else if (level < -50 && level >= -70) {
                mSignalStrength = 2;
                Log.d("LEVEL", "Good signal");
            } else if (level < -70 && level >= -80) {
                mSignalStrength = 1;
                Log.d("LEVEL", "Low signal");
            } else if (level < -80 && level >= -100) {
                mSignalStrength = 1;
                Log.d("LEVEL", "Very weak signal");
            } else {
                mSignalStrength = 1;
                Log.d("LEVEL", "None");
            }
        } else {
            Log.d("LEVEL", "No network");
//            mShowAlert(context.getString(R.string.mTextFile_alert_no_internet),context);
        }
        return mSignalStrength;
    }


    public static void getOpenEmail(Activity mContext, String mStrEmail) {
        try {
            if (mStrEmail.equals("")) {
                CommonUtils.mShowAlert("No email found", mContext);
            } else {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + mStrEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                mContext.startActivity(Intent.createChooser(intent, "Send Email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCallNumber(Activity mContext, String mStrPhone) {
        try {
            if (mStrPhone.equals("")) {
            } else {
                CommonUtils.mShowAlert("No Phone no found", mContext);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mStrPhone));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mContext.startActivity(callIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String covertTimeToText(String date) {
        String strDate = null;
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTime(sdf.parse(date));
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            long time = cal.getTimeInMillis();
            long diff = now - time;
            int seconds = (int) (diff / 1000) % 60;
            int minutes = (int) ((diff / (1000 * 60)) % 60);
            int hours = (int) ((diff / (1000 * 60 * 60)) % 24);
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            if (days > 0) {
                strDate = days + " days ago";
            } else if (hours > 0) {
                strDate = hours + " hours ago";
            } else if (minutes > 0) {
                strDate = minutes + " minutes ago";
            } else if (seconds > 0) {
                strDate = seconds + " seconds ago";
            }
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
        return strDate;
    }


    /* yyyy-MM-dd HH:mm:ss a*/
    public static String getTimeformat(String mDate) {
        String newTimeStr = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = input.parse(mDate);
            newTimeStr = output.format(d);
            System.out.println("<><><>ddffTime: " + newTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTimeStr;

    }

    public static String getFormattedDate(String mStrDate) {
        long smsTimeInMilis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(mStrDate);
            smsTimeInMilis = mDate.getTime();
            System.out.println("Date in milli :: " + smsTimeInMilis);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EE, MMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today, " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday, " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }


    public static String mTogetDay(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.text.DateFormat responceDate = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            return responceDate.format(dateTime);
        }
    }


}
