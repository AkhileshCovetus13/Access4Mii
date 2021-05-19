package Modal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ABS_GET_SET.AuditMainLocation;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.DatabaseHelper;

import static ABS_HELPER.StringUtils.audit_id;
import static ABS_HELPER.StringUtils.audit_main_location_count;
import static ABS_HELPER.StringUtils.audit_main_location_decs;
import static ABS_HELPER.StringUtils.audit_main_location_id;
import static ABS_HELPER.StringUtils.audit_main_location_server_id;
import static ABS_HELPER.StringUtils.audit_main_location_title;
import static ABS_HELPER.StringUtils.id;
import static ABS_HELPER.StringUtils.tb_selected_main_location;
import static ABS_HELPER.StringUtils.user_id;


/**
 * Created by admin1 on 16/1/19.
 */

public class MainLocationModal {

    public static String tb_all_main_location = "tb_all_main_location";
    public static String audit_id = "audit_id";
    public static String user_id = "user_id";
    public static String location_title = "location_title";
    public static String location_desc = "location_desc";
    public static String location_server_id = "location_server_id";
    public static String id = "id";


    public static String tb_selected_main_location = "tb_selected_main_location";
    public static String location_local_id = "location_local_id";
    public static String location_count = "location_count";


    public static String table_all_main_location = "CREATE TABLE "
            + tb_all_main_location + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + location_title + " TEXT NOT NULL, "
            + location_desc + " TEXT NOT NULL, "
            + location_server_id + " TEXT NOT NULL )";


    public static String table_selected_main_location = "CREATE TABLE "
            + tb_selected_main_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + location_server_id + " TEXT NOT NULL, "
            + location_title + " TEXT NOT NULL, "
            + location_local_id + " TEXT NOT NULL, "
            + location_count + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, "
            + location_desc + " TEXT NOT NULL )";


    /*############## RAW DATA OF MAIN LOCATION #################*/


    public static void funInsertAllMainLocation(AuditMainLocation auditMainLocation, DatabaseHelper databaseHelper) {

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(user_id, auditMainLocation.getmStrUserId());
            values.put(audit_id, auditMainLocation.getmStrAuditId());
            values.put(location_title, auditMainLocation.getmStrLocationTitle());
            values.put(location_desc, auditMainLocation.getmStrLocationDesc());
            values.put(location_server_id, auditMainLocation.getmStrLocationServerId());
            db.insert(tb_all_main_location, null, values);

    }

    public static String funGetMainLocationString(String mStrLocationId, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_all_main_location + " WHERE " + location_server_id + " = " + mStrLocationId;
        Cursor c = db.rawQuery(selectQuery, null);
        String mLocation="";
        if (c.moveToFirst()) {
            do {
            mLocation = c.getString((c.getColumnIndex(location_title)));
            } while (c.moveToNext());
        }
        c.close();
        return mLocation;
    }

    public static ArrayList<AuditMainLocation> funGetAllMainLocation(String mStrAuditId,String mStrUserid,String mStrSearch, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<AuditMainLocation> mAuditList = new ArrayList<AuditMainLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_all_main_location + " where " + audit_id + " = " + mStrAuditId + " AND "+user_id + " = " + mStrUserid + " AND "+location_title+" LIKE '%" + mStrSearch + "%'  ORDER BY "+location_title+" ASC ";
        System.out.println("<><><>new "+selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditMainLocation auditMainLocation = new AuditMainLocation();
                auditMainLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                auditMainLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditMainLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditMainLocation.setmStrLocationTitle(c.getString((c.getColumnIndex(location_title))));
                auditMainLocation.setmStrLocationDesc(c.getString((c.getColumnIndex(location_desc))));
                auditMainLocation.setmStrLocationServerId(c.getString((c.getColumnIndex(location_server_id))));
                mAuditList.add(auditMainLocation);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


    /*############## SELECTED DATA OF MAIN LOCATION #################*/

    public static int funIsMainLocationSelected(String mAuditId, String mUserId, String mLocationId, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM " + tb_selected_main_location + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + location_server_id + " = " + mLocationId, null);
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static int funGetSelectedMainLocationCount(SelectedLocation selectedLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_selected_main_location + " WHERE " + location_server_id + " = " + selectedLocation.getmStrMainLocationServerId() + " AND " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId();
        int count = 0;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                count = Integer.parseInt(c.getString((c.getColumnIndex(location_count))));
            } while (c.moveToNext());
        }
        c.close();
        return count;
    }


    public static void funUpdateSelectedMainLocation(SelectedLocation selectedLocation, String opration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int intExisting = funGetSelectedMainLocationCount(selectedLocation, databaseHelper);
        ContentValues values = new ContentValues();
        if (opration.equals("1")) {//here increes selected location count
            values.put(location_count, intExisting + 1 + "");
            db.update(tb_selected_main_location, values, location_server_id + "=" + selectedLocation.getmStrMainLocationServerId() + " AND " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId(), null);
        } else if (opration.equals("2")) {//here decrees selected location count
            values.put(location_count, intExisting - 1 + "");
            db.update(tb_selected_main_location, values, location_server_id + "=" + selectedLocation.getmStrMainLocationServerId() + " AND " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId(), null);
        } else if (opration.equals("3")) {//here update location count
            values.put(location_count, selectedLocation.getmStrMainLocationCount());
            db.update(tb_selected_main_location, values, location_server_id + "=" + selectedLocation.getmStrMainLocationServerId() + " AND " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId(), null);
        }
    }

    public static ArrayList<SelectedLocation> funGetAllSelectedMainLocation(SelectedLocation selectedLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<SelectedLocation> mAuditList = new ArrayList<SelectedLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_selected_main_location + " WHERE " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId();
        System.out.println("<><><>qery " + selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SelectedLocation resultselectedLocation = new SelectedLocation();
                resultselectedLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                resultselectedLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                resultselectedLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                resultselectedLocation.setmStrMainLocationTitle(c.getString((c.getColumnIndex(location_title))));
                resultselectedLocation.setmStrMainLocationCount(c.getString((c.getColumnIndex(location_count))));
                resultselectedLocation.setmStrMainLocationServerId(c.getString((c.getColumnIndex(location_server_id))));
                resultselectedLocation.setmStrMainLocationLocalId(c.getString((c.getColumnIndex(location_local_id))));
                resultselectedLocation.setmStrMainLocationDesc(c.getString((c.getColumnIndex(location_desc))));
                mAuditList.add(resultselectedLocation);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }

    public static void funDeleteSelectedMainLocation(SelectedLocation selectedLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(tb_selected_main_location, location_server_id + " = " + selectedLocation.getmStrMainLocationServerId() + " AND " + audit_id + " = " + selectedLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedLocation.getmStrUserId(), null);
    }

    public static void funInsertSelectedMainLocation(SelectedLocation selectedLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, selectedLocation.getmStrUserId());
        values.put(audit_id, selectedLocation.getmStrAuditId());
        values.put(location_title, selectedLocation.getmStrMainLocationTitle());
        values.put(location_count, selectedLocation.getmStrMainLocationCount());
        values.put(location_server_id, selectedLocation.getmStrMainLocationServerId());
        values.put(location_local_id, selectedLocation.getmStrMainLocationLocalId());
        values.put(location_desc, selectedLocation.getmStrMainLocationDesc());
        db.insert(tb_selected_main_location, null, values);
    }

}
