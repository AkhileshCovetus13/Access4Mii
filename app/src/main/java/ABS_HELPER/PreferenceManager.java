package ABS_HELPER;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin1 on 23/5/18.
 */

public class PreferenceManager {

    /*shareprefence for storing data*/
    private static String FORMII_PREF = "com.access4me.org";
    private static String FORMII_DEVICE_ID = "com.access4me.org_device_tokan";
    private static String FORMII_ISLOGIN = "com.access4me.org_islogin";
    private static String FORMII_LASTNAME = "com.access4me.org_lastname";
    private static String FORMII_FIRSTNAME = "com.access4me.org_firstname";
    private static String FORMII_FULLNAME = "com.access4me.org_fullname";
    private static String FORMII_EMAIL = "com.access4me.org_email";
    private static String FORMII_ID = "com.access4me.org.id";
    private static String FORMII_PROFILEIMG = "com.access4me.org.profile_img";
    private static String FORMII_CONTACT = "com.access4me.org_contact";
    private static String FORMII_ADDRESS = "com.access4me.org_address";
    private static String FORMII_TOKAN = "com.access4me.org_tokan";
    private static String FORMII_PROGREESS_VIEW = "com.access4me.org_view";
    private static String FORMII_CHECK_NOTIFICATION_AUDIT = "com.access4me.org_tokan_notification_audit";
    private static String FORMII_CHECK_NOTIFICATION_CHAT = "com.access4me.org_tokan_notification_chat";
    private static String FORMII_CHECK_NOTIFICATION_REPORT = "com.access4me.org_tokan_notification_report";

    private static String FORMII_CHECK_IS_FIRST_TIME = "com.access4me.org_is_first_time";
    private static String FORMII_USER_ROLE = "com.access4me.org_is_user_role";
    private static String FORMII_FILTERBY_DATE = "com.access4me.org_is_filterby_date";
    private static String FORMII_FILTERBY_TITLE = "com.access4me.org_is_filterby_title";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(FORMII_PREF, Context.MODE_PRIVATE);
    }



    public static String getFormiiFilterByDate(Context context) {
        return getPrefs(context).getString(FORMII_FILTERBY_DATE, "0");
    }

    public static void setFormiiFilterByDate(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_FILTERBY_DATE, value).commit();
    }

    public static String getFormiiFilterByTitle(Context context) {
        return getPrefs(context).getString(FORMII_FILTERBY_TITLE, "0");
    }

    public static void setFormiiFilterByTitle(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_FILTERBY_TITLE, value).commit();
    }




    public static String getFormiiUserRole(Context context) {
        return getPrefs(context).getString(FORMII_USER_ROLE, "0");
    }

    public static void setFormiiUserRole(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_USER_ROLE, value).commit();
    }





    public static String getFormiiAuthToken(Context context) {
        return getPrefs(context).getString(FORMII_TOKAN, "0");
    }

    public static void setFormiiAuthToken(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_TOKAN, value).commit();
    }

    public static String getFormiiDeviceId(Context context) {
        return getPrefs(context).getString(FORMII_DEVICE_ID, "0");
    }

    public static void setFormiiDeviceId(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_DEVICE_ID, value).commit();
    }

    public static String getFormiiIsLogin(Context context) {
        return getPrefs(context).getString(FORMII_ISLOGIN, "0");
    }

    public static void setFormiiIsLogin(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_ISLOGIN, value).commit();
    }


    public static String getFormiiLastName(Context context) {
        return getPrefs(context).getString(FORMII_LASTNAME, "0");
    }

    public static void setFormiiLastName(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_LASTNAME, value).commit();
    }


    public static String getFormiiFirstName(Context context) {
        return getPrefs(context).getString(FORMII_FIRSTNAME, "0");
    }

    public static void setFormiiFirstName(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_FIRSTNAME, value).commit();
    }

    public static String getFormiiEmail(Context context) {
        return getPrefs(context).getString(FORMII_EMAIL, "0");
    }

    public static void setFormiiEmail(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_EMAIL, value).commit();
    }


    public static String getFormiiId(Context context) {
        return getPrefs(context).getString(FORMII_ID, "0");
    }

    public static void setFormiiId(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_ID, value).commit();
    }


    public static String getFormiiProfileimg(Context context) {
        return getPrefs(context).getString(FORMII_PROFILEIMG, "0");
    }

    public static void setFormiiProfileimg(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_PROFILEIMG, value).commit();
    }


    public static String getFormiiContact(Context context) {
        return getPrefs(context).getString(FORMII_CONTACT, "0");
    }

    public static void setFormiiContact(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_CONTACT, value).commit();
    }


    public static String getFormiiAddress(Context context) {
        return getPrefs(context).getString(FORMII_ADDRESS, "0");
    }

    public static void setFormiiAddress(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_ADDRESS, value).commit();
    }


    public static String getFormiiFullName(Context context) {
        return getPrefs(context).getString(FORMII_FULLNAME, "0");
    }

    public static void setFormiiFullName(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_FULLNAME, value).commit();
    }

    public static String getFormiiProgreessView(Context context) {
        return getPrefs(context).getString(FORMII_PROGREESS_VIEW, "0");
    }

    public static void setFormiiProgreessView(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_PROGREESS_VIEW, value).commit();
    }

    public static String getFormiiCheckNotificationChat(Context context) {
        return getPrefs(context).getString(FORMII_CHECK_NOTIFICATION_CHAT, "1");
    }

    public static void setFormiiCheckNotificationChat(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_CHECK_NOTIFICATION_CHAT, value).commit();
    }


    public static String getFormiiCheckNotificationAudit(Context context) {
        return getPrefs(context).getString(FORMII_CHECK_NOTIFICATION_AUDIT, "1");
    }

    public static void setFormiiCheckNotificationAudit(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_CHECK_NOTIFICATION_AUDIT, value).commit();
    }


    public static String getFormiiCheckNotificationReport(Context context) {
        return getPrefs(context).getString(FORMII_CHECK_NOTIFICATION_REPORT, "1");
    }

    public static void setFormiiCheckNotificationReport(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_CHECK_NOTIFICATION_REPORT, value).commit();
    }

    public static String getFormiiCheckIsFirstTime(Context context) {
        return getPrefs(context).getString(FORMII_CHECK_IS_FIRST_TIME, "1");
    }

    public static void setFormiiCheckIsFirstTime(Context context, String value) {
        getPrefs(context).edit().putString(FORMII_CHECK_IS_FIRST_TIME, value).commit();
    }

    public static void cleanData(Context context) {
        getPrefs(context).edit().clear().commit();
    }


}