package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.NotificationList;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Notification;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;


public class ActivityNotificationList extends Activity {


    ArrayList<Notification> mListItems = new ArrayList<>();
    @BindView(R.id.mListNotification)
    ListView mListNotification;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    NotificationList notificationList;

    @BindView(R.id.mTextClearAll)
    TextViewSemiBold mTextClearAll;


    /* click for going back */
    @OnClick(R.id.mTextClearAll)
    public void mGoForClearAllNotification() {
        CommonUtils.showPDialog(ActivityNotificationList.this);
        mToGoClearAllNotification();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);


        /*click to view detail screen*/
        mListNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = mListItems.get(position);
                System.out.println("<><><id" + notification.getmStrNotificationId());
                if (notification.getmStrNotificationTitle().equals("Chat")) {
                    mNotificationRead(notification.getmStrNotificationId());
                    Intent intent = new Intent(ActivityNotificationList.this, ActivityTabHostMain.class);
                    intent.putExtra("mStrCurrentTab", "1");
                    startActivity(intent);
                } else if (notification.getmStrNotificationTitle().equals("Audit")) {
                    Intent intent = new Intent(ActivityNotificationList.this, ActivityAuditDetail.class);
                    intent.putExtra("mStrNotifyId", notification.getmStrNotificationId());
                    intent.putExtra("mStrType", notification.getmStrNotificationTitle());
                    startActivity(intent);
                } else if (notification.getmStrNotificationTitle().equals("Report")) {
                    mNotificationRead(notification.getmStrNotificationId());
                    Intent intent = new Intent(ActivityNotificationList.this, ActivityReportList.class);
                    startActivity(intent);
                }

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCheckSignalStrength(ActivityNotificationList.this) == 2) {
            CommonUtils.showPDialog(ActivityNotificationList.this);
            mToGetNotification();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityNotificationList.this);
        }
    }

    /* click for going back */
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        finish();
    }

    /*api call for getting notification list*/
    void mToGetNotification() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getNotification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        mListItems.clear();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                JSONArray jsonArray = jsonObject.getJSONArray("notification");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Notification notification = new Notification();
                                    JSONObject mNotificationObj = jsonArray.getJSONObject(i);
                                    String mStrId = mNotificationObj.getString("notify_id");
                                    String mStrTitle = mNotificationObj.getString("type");
                                    String mStrDecs = mNotificationObj.getString("title");
                                    String mStrPhoto = mNotificationObj.getString("photo");
                                    String mStrNotifyCount = mNotificationObj.getString("notifyCount");
                                    String mStrDate = mNotificationObj.getString("created_date");
                                    String mStrRole = mNotificationObj.getString("Role");
                                    String mStrUserId = mNotificationObj.getString("User_id");
                                    String mStrUserName = mNotificationObj.getString("Username");
                                    notification.setmStrNotificationCount(mStrNotifyCount);
                                    notification.setmStrNotificationId(mStrId);
                                    notification.setmStrNotificationTitle(mStrTitle);
                                    notification.setmStrNotificationDecs(mStrDecs);
                                    notification.setmStrNotificationImage(mStrPhoto);
                                    notification.setmStrNotificationDay(mTogetDay(mStrDate));
                                    notification.setmStrNotificationTime(mTogetTime(mStrDate));
                                    notification.setmStrNotificationUserRole(mStrRole);
                                    notification.setmStrNotificationUserId(mStrUserId);
                                    notification.setmStrNotificationUserName(mStrUserName);
                                    mListItems.add(notification);
                                }
                                notificationList = new NotificationList(ActivityNotificationList.this, mListItems);
                                mListNotification.setAdapter(notificationList);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityNotificationList.this);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityNotificationList.this);
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
                        Toast.makeText(ActivityNotificationList.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityNotificationList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityNotificationList.this));
                params.put("pagenum", "1");
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }



    void mToGoClearAllNotification() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "notifyClearAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                            CommonUtils.mShowAlert("All Notification are cleared",ActivityNotificationList.this);
                            mListNotification.setVisibility(View.GONE);
                            } else if (mStrStatus.equals("2")) {
                            CommonUtils.showSessionExp(ActivityNotificationList.this);
                            } else {
                            CommonUtils.mShowAlert(response.getString("message"), ActivityNotificationList.this);
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
                        Toast.makeText(ActivityNotificationList.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityNotificationList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityNotificationList.this));
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    /*api call for getting notification list*/
    void mNotificationRead(final String mStrUserNotifyId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "seenNotify",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>read" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityNotificationList.this);
                            } else {
//                                CommonUtils.mShowAlert(response.getString("message"), ActivityNotificationList.this);
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
                        Toast.makeText(ActivityNotificationList.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityNotificationList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityNotificationList.this));
                params.put("notify_id", mStrUserNotifyId);
                System.out.println("<><><>readparams" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    // yyyy-MM-dd hh:mm:ss
    // dd MMM yyyy
    // hh:mm a

    /* date formate for getting days*/
    public static String mTogetDay(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
        DateFormat responceDate = new SimpleDateFormat("dd MMM yyyy");
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

    /* date formate for getting am pm*/
    public static String mTogetTime(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        return timeFormatter.format(dateTime);
    }


}
