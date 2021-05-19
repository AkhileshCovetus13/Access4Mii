package Modal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ABS_GET_SET.ExplanationListImage;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.DatabaseHelper;



/**
 * Created by admin1 on 17/1/19.
 */

public class SubLocationLayer {

    public static String tb_sub_location_layer = "tb_sub_location_layer";
    public static String sub_folder_layer_id = "sub_folder_layer_id";
    public static String sub_folder_layer_title = "sub_folder_layer_title";
    public static String sub_folder_layer_desc = "sub_folder_layer_desc";
    public static String sub_location_title = "sub_location_title";
    public static String sub_location_server = "sub_location_server";
    public static String explanation_title = "explanation_title";
    public static String explanation_desc = "explanation_desc";
    public static String explanation_status = "explanation_status";
    public static String id = "id";
    public static String audit_id = "audit_id";
    public static String user_id = "user_id";
    public static String explation_list_img = "explation_list_img";
    public static String explanation_archive = "explanation_archive";
    public static String main_location_server = "main_location_server";
    public static String main_location_local = "main_location_local";
    public static String explation_list_image = "explation_list_image";
    public static String explation_list_id = "explation_list_id";
    public static String tb_image_sub_location_layer_final = "tb_image_sub_location_layer_final";
    public static String tb_image_sub_location_layer = "tb_image_sub_location_layer";
    public static String img_default = "img_default";
    public static String img_status = "img_status";



    public static String table_sub_location_Layer = "CREATE TABLE "
            + tb_sub_location_layer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + sub_folder_layer_title + " TEXT NOT NULL, "
            + sub_folder_layer_desc + " TEXT NOT NULL, "
            + sub_location_title + " TEXT NOT NULL, "
            + sub_location_server + " TEXT NOT NULL, "
            + explanation_title + " TEXT NOT NULL, "
            + explanation_desc + " TEXT NOT NULL, "
            + explanation_status + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + explation_list_img + " TEXT , "
            + explanation_archive + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + main_location_local + " TEXT NOT NULL )";


    public static int funInsertSubLocationLayer(SubLocationExplation subLocationExplation, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sub_folder_layer_id, subLocationExplation.getmStrLayerId());
        values.put(sub_folder_layer_title, subLocationExplation.getmStrLayerTitle());
        values.put(sub_folder_layer_desc, subLocationExplation.getmStrLayerDesc());
        values.put(sub_location_title, subLocationExplation.getmStrSubLocationTitle());
        values.put(sub_location_server, subLocationExplation.getmStrSubLocationServer());
        values.put(explanation_title, subLocationExplation.getmStrExplanationTitle());
        values.put(explanation_desc, subLocationExplation.getmStrExplanationDesc());
        values.put(explanation_status, subLocationExplation.getmStrExplanationStatus());
        values.put(audit_id, subLocationExplation.getmStrAuditId());
        values.put(user_id, subLocationExplation.getmStrUserId());
        values.put(main_location_server, subLocationExplation.getmStrMainLocationServer());
        values.put(main_location_local, subLocationExplation.getmStrMainLocationLocal());
        values.put(explanation_archive, subLocationExplation.getmStrExplanationArchive());
        db.insert(tb_sub_location_layer, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }



    public static ArrayList<SubLocationExplation> funGetAllSubLocationLayer(SubLocationExplation subLocationExplation,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<SubLocationExplation> mAuditList = new ArrayList<SubLocationExplation>();
        String selectQuery = "SELECT * FROM " + tb_sub_location_layer + " WHERE " + sub_folder_layer_id + " = " + subLocationExplation.getmStrLayerId() + " AND " + sub_location_server + " = " + subLocationExplation.getmStrSubLocationServer() + " AND " + main_location_server + " = " + subLocationExplation.getmStrMainLocationServer() + " AND " + audit_id + " = " + subLocationExplation.getmStrAuditId() + " AND " + user_id + " = " + subLocationExplation.getmStrUserId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                subLocationExplation = new SubLocationExplation();
                subLocationExplation.setmStrId(c.getString((c.getColumnIndex(id))));
                subLocationExplation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subLocationExplation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subLocationExplation.setmStrMainLocationServer(c.getString((c.getColumnIndex(main_location_server))));
                subLocationExplation.setmStrMainLocationLocal(c.getString((c.getColumnIndex(main_location_local))));
                subLocationExplation.setmStrSubLocationServer(c.getString((c.getColumnIndex(sub_location_server))));
                subLocationExplation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                subLocationExplation.setmStrLayerId(c.getString((c.getColumnIndex(sub_folder_layer_id))));
                subLocationExplation.setmStrLayerTitle(c.getString((c.getColumnIndex(sub_folder_layer_title))));
                subLocationExplation.setmStrLayerDesc(c.getString((c.getColumnIndex(sub_folder_layer_desc))));
                subLocationExplation.setmStrExplanationTitle(c.getString((c.getColumnIndex(explanation_title))));
                subLocationExplation.setmStrExplanationImage(c.getString((c.getColumnIndex(explation_list_img))));
                subLocationExplation.setmStrExplanationDesc(c.getString((c.getColumnIndex(explanation_desc))));
                subLocationExplation.setmStrExplanationStatus(c.getString((c.getColumnIndex(explanation_status))));
                subLocationExplation.setmStrExplanationArchive(c.getString((c.getColumnIndex(explanation_archive))));
                mAuditList.add(subLocationExplation);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }

    public static void funUpdateSubLocationLayer(SubLocationExplation subLocationExplation,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(opration.equals("1")){
        values.put(explanation_archive, subLocationExplation.getmStrExplanationArchive());
        db.update(tb_sub_location_layer, values, "id = " + subLocationExplation.getmStrId() + " AND " + sub_location_server + " = " + subLocationExplation.getmStrSubLocationServer() + " AND " + main_location_server + " = " + subLocationExplation.getmStrMainLocationServer() + " AND " + audit_id + " = " + subLocationExplation.getmStrAuditId() + " AND " + user_id + " = " + subLocationExplation.getmStrUserId(), null);
        }else if(opration.equals("2")){
        values.put(sub_folder_layer_title,subLocationExplation.getmStrLayerTitle());
        values.put(sub_folder_layer_desc,subLocationExplation.getmStrLayerDesc());
        db.update(tb_sub_location_layer, values, sub_folder_layer_id + " = " + subLocationExplation.getmStrLayerId() + " AND " + user_id + " = " + subLocationExplation.getmStrUserId() + " AND " + audit_id + " = " + subLocationExplation.getmStrAuditId() + " AND " + main_location_server + " = " + subLocationExplation.getmStrMainLocationServer(), null);
        }else if(opration.equals("3")){
        values.put(explation_list_img, subLocationExplation.getmStrExplanationImage());
        db.update(tb_sub_location_layer, values, "id = " + subLocationExplation.getmStrId()+ " AND " + user_id + " = " + subLocationExplation.getmStrUserId() + " AND " + audit_id + " = " + subLocationExplation.getmStrAuditId() + " AND " + main_location_server + " = " + subLocationExplation.getmStrMainLocationServer(), null);
        }else if(opration.equals("4")){
        values.put(explanation_title, subLocationExplation.getmStrExplanationTitle());
        values.put(explanation_desc, subLocationExplation.getmStrExplanationDesc());
        db.update(tb_sub_location_layer, values, "id = " + subLocationExplation.getmStrId()+ " AND " + user_id + " = " + subLocationExplation.getmStrUserId() + " AND " + audit_id + " = " + subLocationExplation.getmStrAuditId() + " AND " + main_location_server + " = " + subLocationExplation.getmStrMainLocationServer(), null);
        }

    }

    public static void funDeleteSubLocationLayer(SubLocationExplation subLocationExplation,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if(opration.equals("1")){
        db.delete(tb_sub_location_layer, id + "=" + subLocationExplation.getmStrId(), null);
        }else if(opration.equals("2")){
        db.delete(tb_sub_location_layer, sub_folder_layer_id + " = " + subLocationExplation.getmStrLayerId() + " AND " + sub_location_server + " = " + subLocationExplation.getmStrSubLocationServer(), null);
        }

    }






    /*########### Layer IMAGE MODAL #################*/



    public static String table_image_sub_location_Layer = "CREATE TABLE "
            + tb_image_sub_location_layer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + explation_list_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + sub_location_server + " TEXT NOT NULL, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + img_default + " TEXT NOT NULL, "
            + img_status + " TEXT NOT NULL, "
            + explation_list_image + " TEXT )";


    public static String table_image_sub_location_Layer_final = "CREATE TABLE "
            + tb_image_sub_location_layer_final + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + explation_list_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + sub_location_server + " TEXT NOT NULL, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + img_default + " TEXT NOT NULL, "
            + img_status + " TEXT NOT NULL, "
            + explation_list_image + " TEXT )";


    public static void funInsertImageSubLocationLayer(ExplanationListImage explanationListImage,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_id, explanationListImage.getmStrExplationListId());
        values.put(explation_list_image, explanationListImage.getmStrExplationListImage());
        values.put(user_id, explanationListImage.getmStrUserId());
        values.put(main_location_server, explanationListImage.getmStrMainLocationServer());
        values.put(sub_location_server, explanationListImage.getmStrSubLocationServer());
        values.put(audit_id, explanationListImage.getmStrAuditId());
        values.put(img_default, explanationListImage.getmStrImgDefault());
        values.put(sub_folder_layer_id, explanationListImage.getmStrLayerId());
        values.put(img_status, explanationListImage.getmStrImgStatus());
        db.insert(tb_image_sub_location_layer, null, values);
        db.insert(tb_image_sub_location_layer_final, null, values);
    }



   /* public static void funUpdateImageSubLocationLayer(ExplanationListImage explanationListImage,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(opration.equals("1")){
        values.put(explation_list_image, explanationListImage.getmStrExplationListImage());
        values.put(img_default, explanationListImage.getmStrImgDefault());
        db.update(tb_image_sub_location_layer, values, id + " = " + explanationListImage.getmStrId() + " AND " + sub_location_server + " = " + explanationListImage.getmStrSubLocationServer() + " AND " + main_location_server + " = " + explanationListImage.getmStrMainLocationServer() + " AND " + audit_id + " = " + explanationListImage.getmStrAuditId() + " AND " + user_id + " = " + explanationListImage.getmStrUserId(), null);
        }else if(opration.equals("2")){
        values.put(explation_list_image,"");
        values.put(img_default, "1");
        db.update(tb_image_sub_location_layer, values, id + " = " + explanationListImage.getmStrId() + " AND " + sub_location_server + " = " + explanationListImage.getmStrSubLocationServer() + " AND " + main_location_server + " = " + explanationListImage.getmStrMainLocationServer() + " AND " + audit_id + " = " + explanationListImage.getmStrAuditId() + " AND " + user_id + " = " + explanationListImage.getmStrUserId(), null);
        }

    }
*/


    public static void update_tb_image_sub_location_explation_list_by_layer_id(String mStrAuditId,String mStrUserId,String mExplanationId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(explation_list_image,"");
        //values.put(img_default, "1");
        values.put(img_status, "2");
        //db.update(tb_image_sub_location_layer, values, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId, null);
        db.update(tb_image_sub_location_layer_final, values, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId, null);
    }

    public static void update_tb_image_sub_location_explation_list_by_layer_id_resync(String mStrAuditId,String mStrUserId,DatabaseHelper databaseHelper) {
        System.out.println("<><>@# "+mStrAuditId);
        System.out.println("<><>@# "+mStrUserId);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(explation_list_image,"");
        //values.put(img_default, "1");
        values.put(img_status, "1");
        //db.update(tb_image_sub_location_layer, values, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId, null);
        db.update(tb_image_sub_location_layer_final, values, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId, null);
    }


    public static void funUpdateImg(ExplanationListImage explanationListImage,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_image, explanationListImage.getmStrExplationListImage());
        values.put(img_default, explanationListImage.getmStrImgDefault());
        db.update(tb_image_sub_location_layer, values, id + "=" + explanationListImage.getmStrId(), null);
        db.update(tb_image_sub_location_layer_final, values, id + "=" + explanationListImage.getmStrId(), null);
    }



   /* public static ArrayList<String> getAllExplanationImage(ExplanationListImage explanationListImage,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<String> mAuditList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_layer + " WHERE " + audit_id + " = " + explanationListImage.getmStrAuditId() + " AND " + user_id + " = " + explanationListImage.getmStrUserId() + " AND " + explation_list_id + " = " + explanationListImage.getmStrExplationListId() + " AND " + img_default + " = 0" + " AND " + main_location_server + " = "+explanationListImage.getmStrMainLocationServer()+ " AND " + sub_location_server + " = "+explanationListImage.getmStrSubLocationServer();
        System.out.println("<><><>selectQuery "+selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                mAuditList.add(c.getString((c.getColumnIndex(explation_list_image))));
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/


    public static ArrayList<String> getAllExplanationImage(String mStrAuditId,String mStrUserId,String mExplanationId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<String> mAuditList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_layer + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId + " AND " + img_default + " = 0";
        System.out.println("<><><>selectQuery "+selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                mAuditList.add(c.getString((c.getColumnIndex(explation_list_image))));
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public static ArrayList<String> getAllExplanationImageFinal(String mStrAuditId,String mStrUserId,String mExplanationId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<String> mAuditList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_layer_final + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId + " AND " + img_default + " = 0 AND " + img_status + " = 1";
        System.out.println("<><>@# "+selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                mAuditList.add(c.getString((c.getColumnIndex(explation_list_image))));
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }



    public static ArrayList<ExplanationListImage> getAllExplanationImageAllContent(String mLayerId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<ExplanationListImage> mAuditList = new ArrayList<ExplanationListImage>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_layer + " WHERE " + explation_list_id + " = " + mLayerId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                ExplanationListImage explanationListImage = new ExplanationListImage();
                explanationListImage.setmStrId(c.getString((c.getColumnIndex(id))));
                explanationListImage.setmStrExplationListImage(c.getString((c.getColumnIndex(explation_list_image))));
                explanationListImage.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                explanationListImage.setmStrImgDefault(c.getString((c.getColumnIndex(img_default))));
                explanationListImage.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                mAuditList.add(explanationListImage);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public static void funDeleteAllExplanationImage(String mLayerId,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        if(opration.equals("1")){
        db.delete(tb_image_sub_location_layer, sub_folder_layer_id + " = " + mLayerId, null);
        }else if(opration.equals("2")){
        db.delete(tb_image_sub_location_layer, id + "=" + mLayerId, null);
        }

    }




}
