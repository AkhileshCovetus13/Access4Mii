package Modal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ABS_GET_SET.Audit;
import ABS_HELPER.DatabaseHelper;

import static ABS_HELPER.StringUtils.audit_id;
import static ABS_HELPER.StringUtils.status;
import static ABS_HELPER.StringUtils.tb_audit_question_answer;
import static ABS_HELPER.StringUtils.user_id;


/**
 * Created by admin1 on 16/1/19.
 */

public class AuditListModal {

    public static String tb_all_audit = "tb_all_audit_list";
    public static String user_id = "user_id";
    public static String audit_id = "audit_id";
    public static String audit_type = "audit_type";
    public static String assign_by = "audit_assign_by";
    public static String title = "audit_title";
    public static String due_date = "audit_due_date";
    public static String country_id = "country_id";
    public static String lang_id = "lang_id";
    public static String work_status = "audit_work_status";
    public static String id = "id";
    public static String is_syn_start = "is_syn_start";
    public static String auditer_report = "auditer_report";
    public static String inspector_report = "inspector_report";


    public static String table_tb_all_audit = "CREATE TABLE "
            + tb_all_audit + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + audit_type + " TEXT NOT NULL, "//0 for only audit, 1 for audit and inspector
            + assign_by + " TEXT NOT NULL, "
            + title + " TEXT NOT NULL, "
            + due_date + " TEXT NOT NULL, "
            + country_id + " TEXT, "
            + lang_id + " TEXT, "
            + is_syn_start + " TEXT NOT NULL, "
            + auditer_report + " TEXT, "
            + inspector_report + " TEXT, "
            + work_status + " TEXT NOT NULL )";


    public static void funInsertAllAudit(Audit audit, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, audit.getmUserId());
        values.put(audit_id, audit.getmAuditId());
        values.put(audit_type, audit.getmAuditType());
        values.put(assign_by, audit.getmAssignBy());
        values.put(title, audit.getmTitle());
        values.put(due_date, audit.getmDueDate());
        values.put(work_status, audit.getmStatus());
        values.put(country_id, audit.getmStrCountryId());
        values.put(lang_id, audit.getmStrLangId());
        values.put(is_syn_start, "0");
        values.put(auditer_report,audit.getmStrAuditerReport());
        values.put(inspector_report, audit.getmStrInspectorReport());
        Cursor cursor = db.rawQuery("SELECT * FROM " + tb_all_audit + " WHERE " + audit_id + " = " + audit.getmAuditId() + " AND " + user_id + " = " + audit.getmUserId(), null);
        boolean exist = (cursor.getCount() > 0);
        if (!exist) {
            db.insert(tb_all_audit, null, values);
        }
        cursor.close();
    }


    public static void funUpdateAudit(Audit audit, String mStrOpration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (mStrOpration.equals("1")) {// here update work status
            values.put(work_status, audit.getmStatus());
            db.update(tb_all_audit, values, audit_id + "=" + audit.getmAuditId(), null);
        } else if (mStrOpration.equals("2")) {// here update country and language id
            values.put(country_id, audit.getmStrCountryId());
            values.put(lang_id, audit.getmStrLangId());
            db.update(tb_all_audit, values, audit_id + "=" + audit.getmAuditId(), null);
        } else if (mStrOpration.equals("3")) {// here update audit synchronization status
            values.put(is_syn_start, "1");
            db.update(tb_all_audit, values, audit_id + "=" + audit.getmAuditId(), null);
        }else if (mStrOpration.equals("4")) {// update report url
            values.put(auditer_report,audit.getmStrAuditerReport());
            values.put(inspector_report,audit.getmStrInspectorReport());
            db.update(tb_all_audit, values, audit_id + "=" + audit.getmAuditId(), null);
        }else if (mStrOpration.equals("5")) {// update report url
            values.put(work_status, audit.getmStatus());
            values.put(is_syn_start,"0");
            db.update(tb_all_audit, values, audit_id + "=" + audit.getmAuditId(), null);

        }

    }

    public static int funGetActiveAudit(String status, String mUserId, DatabaseHelper databaseHelper) {
        int count =0;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tb_all_audit + " WHERE " + work_status + " = " + status + " AND " + audit_id + " = " + mUserId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public static int funGetActiveAuditByUser(String status, String mUserId, DatabaseHelper databaseHelper) {
        int count =0;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tb_all_audit + " WHERE " + work_status + " = " + status + " AND " + user_id + " = " + mUserId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public static boolean funSynIsInProgress(String mStrAuditId, String mStrIsSynk, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tb_all_audit + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + is_syn_start + " = " + mStrIsSynk, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        return exist;
    }

    public static ArrayList<Audit> funGetAllAudit(String mUserId, String mStatus, String mAuditId, String mOpration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Audit> mAuditList = new ArrayList<Audit>();
        String selectQuery = "";
        if (mOpration.equals("1")) {// here get audit list by userid and audit status
            selectQuery = "SELECT  * FROM  " + tb_all_audit + " where " + user_id + " = " + mUserId + " AND " + work_status + " = " + mStatus;
        } else {// here get audit by audit id
            selectQuery = "SELECT  * FROM  " + tb_all_audit + " where " + audit_id + " = " + mAuditId;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Audit audit = new Audit();
                audit.setmAuditId(cursor.getString((cursor.getColumnIndex(audit_id))));
                audit.setmAuditType(cursor.getString((cursor.getColumnIndex(audit_type))));
                audit.setmUserId(cursor.getString((cursor.getColumnIndex(user_id))));
                audit.setmAssignBy(cursor.getString((cursor.getColumnIndex(assign_by))));
                audit.setmDueDate(cursor.getString((cursor.getColumnIndex(due_date))));
                audit.setmTitle(cursor.getString((cursor.getColumnIndex(title))));
                audit.setmStatus(cursor.getString((cursor.getColumnIndex(work_status))));
                audit.setmStrCountryId(cursor.getString((cursor.getColumnIndex(country_id))));
                audit.setmStrLangId(cursor.getString((cursor.getColumnIndex(lang_id))));
                audit.setmStrIsSynk(cursor.getString((cursor.getColumnIndex(is_syn_start))));
                audit.setmStrAuditerReport(cursor.getString((cursor.getColumnIndex(auditer_report))));
                audit.setmStrInspectorReport(cursor.getString((cursor.getColumnIndex(inspector_report))));
                mAuditList.add(audit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mAuditList;
    }




    public static ArrayList<Audit> funGetAudit(String mUserId,String mAuditId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Audit> mAuditList = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM  " + tb_all_audit + " where " + user_id + " = " + mUserId + " AND " + audit_id + " = " + mAuditId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Audit audit = new Audit();
                audit.setmAuditId(cursor.getString((cursor.getColumnIndex(audit_id))));
                audit.setmAuditType(cursor.getString((cursor.getColumnIndex(audit_type))));
                audit.setmUserId(cursor.getString((cursor.getColumnIndex(user_id))));
                audit.setmAssignBy(cursor.getString((cursor.getColumnIndex(assign_by))));
                audit.setmDueDate(cursor.getString((cursor.getColumnIndex(due_date))));
                audit.setmTitle(cursor.getString((cursor.getColumnIndex(title))));
                audit.setmStatus(cursor.getString((cursor.getColumnIndex(work_status))));
                audit.setmStrCountryId(cursor.getString((cursor.getColumnIndex(country_id))));
                audit.setmStrLangId(cursor.getString((cursor.getColumnIndex(lang_id))));
                audit.setmStrIsSynk(cursor.getString((cursor.getColumnIndex(is_syn_start))));
                audit.setmStrAuditerReport(cursor.getString((cursor.getColumnIndex(auditer_report))));
                audit.setmStrInspectorReport(cursor.getString((cursor.getColumnIndex(inspector_report))));
                mAuditList.add(audit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mAuditList;
    }



    public static ArrayList<Audit> funGetAllAuditInHistory(String mUserId, String mStatus, String mAuditId, String mOpration, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Audit> mAuditList = new ArrayList<Audit>();
        String selectQuery = "";
        if (mOpration.equals("1")) {// here get audit list by userid and audit status
            selectQuery = "SELECT  * FROM  " + tb_all_audit + " where " + user_id + " = " + mUserId + " AND " + work_status + " != 1 AND " + work_status +" != 0";
        } else {// here get audit by audit id
            selectQuery = "SELECT  * FROM  " + tb_all_audit + " where " + audit_id + " = " + mAuditId;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Audit audit = new Audit();
                audit.setmAuditId(cursor.getString((cursor.getColumnIndex(audit_id))));
                audit.setmAuditType(cursor.getString((cursor.getColumnIndex(audit_type))));
                audit.setmUserId(cursor.getString((cursor.getColumnIndex(user_id))));
                audit.setmAssignBy(cursor.getString((cursor.getColumnIndex(assign_by))));
                audit.setmDueDate(cursor.getString((cursor.getColumnIndex(due_date))));
                audit.setmTitle(cursor.getString((cursor.getColumnIndex(title))));
                audit.setmStatus(cursor.getString((cursor.getColumnIndex(work_status))));
                audit.setmStrCountryId(cursor.getString((cursor.getColumnIndex(country_id))));
                audit.setmStrLangId(cursor.getString((cursor.getColumnIndex(lang_id))));
                audit.setmStrIsSynk(cursor.getString((cursor.getColumnIndex(is_syn_start))));
                audit.setmStrAuditerReport(cursor.getString((cursor.getColumnIndex(auditer_report))));
                audit.setmStrInspectorReport(cursor.getString((cursor.getColumnIndex(inspector_report))));
                mAuditList.add(audit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mAuditList;
    }


}
