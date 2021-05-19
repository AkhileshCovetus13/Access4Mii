package ABS_SERVICE;

/**
 * Created by Meenkashi on 27/8/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.covetus.audit.ActivityAuditDetail;
import com.covetus.audit.ActivityLogin;
import com.covetus.audit.ActivityReportList;
import com.covetus.audit.ActivityTabHostMain;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ABS_HELPER.CommonUtils;
import ABS_HELPER.NotificationUtils;
import ABS_HELPER.PreferenceManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(CommonUtils.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            String title = json.getString("title");
            String message = json.getString("body");
            String timestamp = json.getString("timestamp");
            String notificationId = json.getString("notification_id");
            String type = json.getString("type");

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
               /* Intent pushNotification = new Intent(CommonUtils.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);*/


                if (PreferenceManager.getFormiiIsLogin(getApplicationContext()).equals("1")) {
                    if (type.equals("Audit")) {
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityAuditDetail.class);
                        resultIntent.putExtra("mStrNotifyId", notificationId);
                        resultIntent.putExtra("mStrType", type);
                        if (PreferenceManager.getFormiiCheckNotificationAudit(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    } else if (type.equals("Chat")) {
                        System.out.println("<><><type click");
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityTabHostMain.class);
                        resultIntent.putExtra("mStrCurrentTab", "1");
                        if (PreferenceManager.getFormiiCheckNotificationChat(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    } else if (type.equals("Report")) {
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityReportList.class);
                        if (PreferenceManager.getFormiiCheckNotificationReport(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    }
                } else {
                    mLoginCheck(title, message);
                }
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, showPDialog the notification in notification tray
                if (PreferenceManager.getFormiiIsLogin(getApplicationContext()).equals("1")) {
                    if (type.equals("Audit")) {
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityAuditDetail.class);
                        resultIntent.putExtra("mStrNotifyId", notificationId);
                        resultIntent.putExtra("mStrType", type);
                        if (PreferenceManager.getFormiiCheckNotificationAudit(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    } else if (type.equals("Chat")) {
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityTabHostMain.class);
                        resultIntent.putExtra("mStrCurrentTab", "1");
                        if (PreferenceManager.getFormiiCheckNotificationChat(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    } else if (type.equals("Report")) {
                        Intent resultIntent = new Intent(getApplicationContext(), ActivityReportList.class);
                        if (PreferenceManager.getFormiiCheckNotificationReport(getApplicationContext()).equals("1"))
                            showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    }
                } else {
                    mLoginCheck(title, message);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        notificationUtils.showNotificationMessage(title, message, intent);
    }


    void mLoginCheck(String title, String message) {
        Log.e(TAG, "<><><>push message login: " + message);
        Intent resultIntent = new Intent(getApplicationContext(), ActivityLogin.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showNotificationMessage(getApplicationContext(), title, message, resultIntent);
    }
}