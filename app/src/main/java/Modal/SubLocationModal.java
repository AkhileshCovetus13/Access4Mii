package Modal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ABS_GET_SET.AuditSubLocation;
import ABS_GET_SET.SelectedSubLocation;
import ABS_HELPER.DatabaseHelper;


/**
 * Created by admin1 on 17/1/19.
 */

public class SubLocationModal {


    public static String tb_all_sub_location = "tb_all_sub_location";
    public static String user_id = "user_id";
    public static String audit_id = "audit_id";
    public static String sub_location_desc = "sub_location_desc";
    public static String sub_location_title = "sub_location_title";
    public static String sub_location_server = "sub_location_server";
    public static String main_location_local = "main_location_local";
    public static String main_location_server = "main_location_server";
    public static String id = "id";




     /*############## RAW DATA OF SUB LOCATION #################*/


    public static String table_all_sub_location = "CREATE TABLE "
            + tb_all_sub_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + main_location_local + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + sub_location_server + " TEXT NOT NULL, "
            + sub_location_title + " TEXT NOT NULL, "
            + sub_location_desc + " TEXT NOT NULL )";


    public static void funInsertAllSubLocation(AuditSubLocation auditSubLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, auditSubLocation.getmStrUserId());
        values.put(audit_id, auditSubLocation.getmStrAuditId());
        values.put(main_location_local, auditSubLocation.getmStrMainLocationLocal());
        values.put(main_location_server, auditSubLocation.getmStrMainLocationServer());
        values.put(sub_location_server, auditSubLocation.getmStrSubLocationServerId());
        values.put(sub_location_title, auditSubLocation.getmStrSubLocationTitle());
        values.put(sub_location_desc, auditSubLocation.getmStrSubLocationDesc());
        db.insert(tb_all_sub_location, null, values);
        db.rawQuery("select last_insert_rowid()", null);
    }


    public static ArrayList<AuditSubLocation> funGetAllSubLocation(AuditSubLocation auditSubLocationIt, String mStrSearch, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<AuditSubLocation> mAuditList = new ArrayList<AuditSubLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_all_sub_location + " WHERE " + audit_id + " = " + auditSubLocationIt.getmStrAuditId() + " AND " + user_id + " = " + auditSubLocationIt.getmStrUserId() + " AND " + sub_location_title + " LIKE '%" + mStrSearch + "%'" + " ORDER BY " + sub_location_title + " ASC ";
        System.out.println("<><><>wer " + selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        //System.out.println("<><><>wer$#$# " + c.getCount());


        if (c.moveToFirst()) {
            do {


                //String[] elements = c.getString((c.getColumnIndex(main_location_server))).split(",");
                List<String> fixedLenghtList = Arrays.asList(c.getString((c.getColumnIndex(main_location_server))).split("\\s*,\\s*"));
                System.out.println("<><><>wer@# " + fixedLenghtList.size());
                System.out.println("<><><>wer@#call " + c.getString((c.getColumnIndex(sub_location_server))));
                if (fixedLenghtList.indexOf(auditSubLocationIt.getmStrMainLocationServer()) >= 0) {
                    AuditSubLocation auditSubLocation = new AuditSubLocation();
                    //System.out.println("<><><>wer@# " + auditSubLocationIt.getmStrMainLocationServer());
                    auditSubLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                    auditSubLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                    auditSubLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                    auditSubLocation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                    auditSubLocation.setmStrSubLocationDesc(c.getString((c.getColumnIndex(sub_location_desc))));
                    auditSubLocation.setmStrMainLocationServer(c.getString((c.getColumnIndex(main_location_server))));
                    auditSubLocation.setmStrMainLocationLocal(c.getString((c.getColumnIndex(main_location_local))));
                    auditSubLocation.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server))));
                    mAuditList.add(auditSubLocation);
                   /* int co = databaseHelper.getAllAuditQuestionCountbySub(auditSubLocationIt.getmStrAuditId(), auditSubLocationIt.getmStrUserId(), c.getString((c.getColumnIndex(sub_location_server))));
                    if (co > 0) {

                    }*/


                }


            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


     /*############## SELECTED DATA OF SUB LOCATION #################*/

    public static String tb_selected_sub_location = "tb_selected_sub_location";
    public static String sub_folder_layer_id = "sub_folder_layer_id";
    public static String sub_folder_layer_title = "sub_folder_layer_title";
    public static String sub_folder_layer_desc = "sub_folder_layer_desc";
    public static String sub_location_local = "sub_location_local";
    public static String work_status = "work_status";
    public static String sub_location_count = "sub_location_count";


    public static String table_selected_sub_location = "CREATE TABLE "
            + tb_selected_sub_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + sub_folder_layer_title + " TEXT NOT NULL, "
            + sub_folder_layer_desc + " TEXT NOT NULL, "
            + main_location_local + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + sub_location_server + " TEXT NOT NULL, "
            + sub_location_title + " TEXT NOT NULL, "
            + sub_location_desc + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + work_status + " TEXT NOT NULL, "
            + sub_location_count + " TEXT NOT NULL )";


    public static int funGetExistingSubLocationCount(SelectedSubLocation selectedSubLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tb_selected_sub_location + " WHERE " + sub_folder_layer_id + " = " + selectedSubLocation.getmStrLayerId() + " AND " + sub_location_server + " = " + selectedSubLocation.getmStrSubLocationServer() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer(), null);
        int count = 0;
        if (c.moveToFirst()) {
            do {
                count = Integer.parseInt(c.getString((c.getColumnIndex(sub_location_count))));
            } while (c.moveToNext());
        }
        c.close();
        return count;
    }

    public static void funDeleteSelectedSubLocation(SelectedSubLocation selectedSubLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(tb_selected_sub_location, sub_folder_layer_id + " = " + selectedSubLocation.getmStrLayerId() + " AND " + sub_location_server + " = " + selectedSubLocation.getmStrSubLocationServer() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId(), null);
    }


    public static void funUpdateSelectedSubLocation(SelectedSubLocation selectedSubLocation, String opration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (opration.equals("1")) {
            values.put(sub_folder_layer_title, selectedSubLocation.getmStrLayerTitle());
            values.put(sub_folder_layer_desc, selectedSubLocation.getmStrLayerDesc());
            db.update(tb_selected_sub_location, values, sub_folder_layer_id + " = " + selectedSubLocation.getmStrLayerId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer(), null);
        } else if (opration.equals("2")) {
            values.put(sub_location_count, selectedSubLocation.getmStrSubLocationCount());
            db.update(tb_selected_sub_location, values, sub_location_server + " = " + selectedSubLocation.getmStrSubLocationServer() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer(), null);
        }

    }

    public static boolean funIsSubLocationSelected(SelectedSubLocation selectedSubLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_sub_location + " WHERE " + sub_location_server + " = " + selectedSubLocation.getmStrSubLocationServer() + " AND " + sub_folder_layer_id + " = " + selectedSubLocation.getmStrLayerId() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer(), null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }


    public static void funInsertSelectedSubLocation(SelectedSubLocation selectedSubLocation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, selectedSubLocation.getmStrAuditId());
        values.put(user_id, selectedSubLocation.getmStrUserId());
        values.put(sub_folder_layer_id, selectedSubLocation.getmStrLayerId());
        values.put(sub_folder_layer_title, selectedSubLocation.getmStrLayerTitle());
        values.put(sub_folder_layer_desc, selectedSubLocation.getmStrLayerDesc());
        values.put(main_location_local, selectedSubLocation.getmStrMainLocationLocal());
        values.put(main_location_server, selectedSubLocation.getmStrMainLocationServer());
        values.put(sub_location_server, selectedSubLocation.getmStrSubLocationServer());
        values.put(sub_location_title, selectedSubLocation.getmStrSubLocationTitle());
        values.put(sub_location_desc, selectedSubLocation.getmStrSubLocationDesc());
        values.put(sub_location_count, selectedSubLocation.getmStrSubLocationCount());
        values.put(work_status, selectedSubLocation.getmStrWorkStatus());
        db.insert(tb_selected_sub_location, null, values);
    }


    public static ArrayList<SelectedSubLocation> funGetAllSelectedSubLocation(SelectedSubLocation selectedSubLocation, String opration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<SelectedSubLocation> mAuditList = new ArrayList<SelectedSubLocation>();
        String selectQuery = "";
        if (opration.equals("1")) {
            selectQuery = "SELECT  * FROM  " + tb_selected_sub_location + " where " + sub_folder_layer_id + " = " + selectedSubLocation.getmStrLayerId() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId() + " AND " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer();
            System.out.println("<><><>query " + selectQuery);
        } else if (opration.equals("2")) {
            selectQuery = "SELECT  * FROM  " + tb_selected_sub_location + " where " + main_location_server + " = " + selectedSubLocation.getmStrMainLocationServer() + " AND " + audit_id + " = " + selectedSubLocation.getmStrAuditId() + " AND " + user_id + " = " + selectedSubLocation.getmStrUserId();
            System.out.println("<><><>query " + selectQuery);
        }
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                selectedSubLocation = new SelectedSubLocation();
                selectedSubLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                selectedSubLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                selectedSubLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                selectedSubLocation.setmStrLayerId(c.getString((c.getColumnIndex(sub_folder_layer_id))));
                selectedSubLocation.setmStrLayerTitle(c.getString((c.getColumnIndex(sub_folder_layer_title))));
                selectedSubLocation.setmStrLayerDesc(c.getString((c.getColumnIndex(sub_folder_layer_desc))));
                selectedSubLocation.setmStrMainLocationLocal(c.getString((c.getColumnIndex(main_location_local))));
                selectedSubLocation.setmStrMainLocationServer(c.getString((c.getColumnIndex(main_location_server))));
                selectedSubLocation.setmStrSubLocationServer(c.getString((c.getColumnIndex(sub_location_server))));
                selectedSubLocation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                selectedSubLocation.setmStrSubLocationDesc(c.getString((c.getColumnIndex(sub_location_desc))));
                selectedSubLocation.setmStrSubLocationCount(c.getString((c.getColumnIndex(sub_location_count))));
                selectedSubLocation.setmStrWorkStatus(c.getString((c.getColumnIndex(work_status))));
                mAuditList.add(selectedSubLocation);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


    public static ArrayList<SelectedSubLocation> funGetDataWithCount(ArrayList<SelectedSubLocation> mSubLocation) {
        ArrayList<SelectedSubLocation> mAuditList = mSubLocation;
        int size = mAuditList.size();
        for (int i = size; i < 9; i++) {
            SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
            selectedSubLocation.setmStrId("");
            selectedSubLocation.setmStrAuditId("");
            selectedSubLocation.setmStrUserId("");
            selectedSubLocation.setmStrLayerId("");
            selectedSubLocation.setmStrLayerTitle("");
            selectedSubLocation.setmStrLayerDesc("");
            selectedSubLocation.setmStrMainLocationLocal("");
            selectedSubLocation.setmStrMainLocationServer("");
            selectedSubLocation.setmStrSubLocationServer("");
            selectedSubLocation.setmStrSubLocationTitle("");
            selectedSubLocation.setmStrSubLocationDesc("");
            selectedSubLocation.setmStrSubLocationCount("");
            selectedSubLocation.setmStrWorkStatus("");
            mAuditList.add(selectedSubLocation);
        }


        return mAuditList;
    }


}
