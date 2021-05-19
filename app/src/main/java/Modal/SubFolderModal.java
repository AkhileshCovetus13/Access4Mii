package Modal;

import android.app.admin.DeviceAdminInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ABS_GET_SET.LayerList;
import ABS_GET_SET.MainLocationSubFolder;
import ABS_HELPER.DatabaseHelper;


/**
 * Created by admin1 on 16/1/19.
 */

public class SubFolderModal {

    public static String tb_all_sub_folders = "tb_all_sub_folders";
    public static String main_location_local = "main_location_local";
    public static String main_location_server = "main_location_server";
    public static String sub_folder_name = "sub_folder_name";
    public static String sub_folder_count = "sub_folder_count";
    public static String user_id = "user_id";
    public static String audit_id = "audit_id";
    public static String id = "id";


    public static String table_all_sub_folders = "CREATE TABLE "
            + tb_all_sub_folders + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + main_location_local + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + sub_folder_name + " TEXT NOT NULL, "
            + sub_folder_count + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL )";


    /*############### SUB FOLDER MODAL ####################*/




    public static int funGetSubFolderCount(String mStrFolderId, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_all_sub_folders + " WHERE " + id + " = " + mStrFolderId;
        Cursor c = db.rawQuery(selectQuery, null);
        int count = 0;
        if (c.moveToFirst()) {
            do {
                count = Integer.parseInt(c.getString((c.getColumnIndex(sub_folder_count))));
            } while (c.moveToNext());
        }
c.close();
        return count;

    }

    public static void funUpdateSubFolderCount(String mStrFolderId, String opration, MainLocationSubFolder mainLocationSubFolder, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int intExisting = funGetSubFolderCount(mStrFolderId, databaseHelper);
        ContentValues values = new ContentValues();
        if (opration.equals("1")) {
            values.put(sub_folder_count, intExisting + 1 + "");
            db.update(tb_all_sub_folders, values, "id = " + mStrFolderId, null);
        } else if (opration.equals("2")) {
            values.put(sub_folder_count, intExisting - 1 + "");
            db.update(tb_all_sub_folders, values, "id = " + mStrFolderId, null);
        } else if (opration.equals("3")) {
            values.put(sub_folder_name, mainLocationSubFolder.getmStrSubFolderName());
            values.put(sub_folder_count, mainLocationSubFolder.getmStrSubFolderCont());
            db.update(tb_all_sub_folders, values, "id = " + mainLocationSubFolder.getmStrId(), null);
        }
    }


    public static int funInsertSubFolders(MainLocationSubFolder mainLocationSubFolder, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, mainLocationSubFolder.getmStrUserId());
        values.put(audit_id, mainLocationSubFolder.getmStrAuditId());
        values.put(main_location_local, mainLocationSubFolder.getmStrMainLocationId());
        values.put(main_location_server, mainLocationSubFolder.getmStrMainLocationServerId());
        values.put(sub_folder_count, mainLocationSubFolder.getmStrSubFolderCont());
        values.put(sub_folder_name, mainLocationSubFolder.getmStrSubFolderName());
        db.insert(tb_all_sub_folders, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }


    public static ArrayList<MainLocationSubFolder> funGetAllSubFolders(MainLocationSubFolder mainLocationSubFolder, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<MainLocationSubFolder> mAuditList = new ArrayList<MainLocationSubFolder>();
        String selectQuery = "SELECT * FROM " + tb_all_sub_folders + " WHERE " + main_location_server + " = " + mainLocationSubFolder.getmStrMainLocationServerId() + " AND " + audit_id + " = " + mainLocationSubFolder.getmStrAuditId() + " AND " + user_id + " = " + mainLocationSubFolder.getmStrUserId();
        Cursor c = db.rawQuery(selectQuery, null);
        ///
        if (c.moveToFirst()) {
            do {
                mainLocationSubFolder = new MainLocationSubFolder();
                mainLocationSubFolder.setmStrId(c.getString((c.getColumnIndex(id))));
                mainLocationSubFolder.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                mainLocationSubFolder.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                mainLocationSubFolder.setmStrMainLocationId(c.getString((c.getColumnIndex(main_location_local))));
                mainLocationSubFolder.setmStrMainLocationServerId(c.getString((c.getColumnIndex(main_location_server))));
                mainLocationSubFolder.setmStrSubFolderName(c.getString((c.getColumnIndex(sub_folder_name))));
                mainLocationSubFolder.setmStrSubFolderCont(c.getString((c.getColumnIndex(sub_folder_count))));
                mAuditList.add(mainLocationSubFolder);
            } while (c.moveToNext());
        }
c.close();
        return mAuditList;
    }


    public static void funDeleteSubFolders(String mLocationServerId, String mStrAuditId, String mUserId, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(tb_all_sub_folders, main_location_server + " = " + mLocationServerId + " AND " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mUserId, null);
    }



     /*############### SUB FOLDER LAYER MODAL ####################*/

    public static String tb_sub_folder_layer = "tb_sub_folder_layer";
    public static String main_location_title = "main_location_title";
    public static String sub_folder_id = "sub_folder_id";
    public static String sub_folder_title = "sub_folder_title";
    public static String layer_title = "layer_title";
    public static String layer_desc = "layer_desc";
    public static String layer_img = "layer_img";
    public static String layer_archive = "layer_archive";

    public static String tabel_sub_folder_layer = "CREATE TABLE "
            + tb_sub_folder_layer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + main_location_local + " TEXT NOT NULL, "
            + main_location_server + " TEXT NOT NULL, "
            + main_location_title + " TEXT NOT NULL, "
            + sub_folder_id + " TEXT NOT NULL, "
            + sub_folder_title + " TEXT NOT NULL, "
            + layer_title + " TEXT NOT NULL, "
            + layer_desc + " TEXT NOT NULL, "
            + layer_img + " TEXT, "
            + layer_archive + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL )";


    public static void funInsertSubFolderLayer(LayerList layerList, DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, layerList.getmStrUserId());
        values.put(audit_id, layerList.getmStrAuditId());
        values.put(layer_desc, layerList.getmStrLayerDesc());
        values.put(layer_title, layerList.getmStrLayerTitle());
        values.put(sub_folder_title, layerList.getmStrSubFolderTitle());
        values.put(sub_folder_id, layerList.getmStrSubFolderId());
        values.put(main_location_title, layerList.getmStrMainLocationTitle());
        values.put(main_location_local, layerList.getmStrMainLocationId());
        values.put(main_location_server, layerList.getmStrMainLocationServerId());
        values.put(layer_img, layerList.getmStrLayerImg());
        values.put(layer_archive, layerList.getmStrLayerArchive());
        db.insert(tb_sub_folder_layer, null, values);
    }


    public static ArrayList<LayerList> funGetAllSubFolderLayer(String mStrSubFolderId,String mAuditId,String mUserId,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<LayerList> mAuditList = new ArrayList<LayerList>();
        String selectQuery = "";
        if(opration.equals("1")){
        selectQuery = "SELECT  * FROM  " + tb_sub_folder_layer + " WHERE " + sub_folder_id + " = " + mStrSubFolderId + " AND " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId;
        }else if(opration.equals("2")){
        selectQuery = "SELECT  * FROM  " + tb_sub_folder_layer + " WHERE " + id + " = " + mStrSubFolderId;
        }
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                LayerList layerList = new LayerList();
                layerList.setmStrId(c.getString((c.getColumnIndex(id))));
                layerList.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                layerList.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                layerList.setmStrLayerTitle(c.getString((c.getColumnIndex(layer_title))));
                layerList.setmStrLayerDesc(c.getString((c.getColumnIndex(layer_desc))));
                layerList.setmStrSubFolderTitle(c.getString((c.getColumnIndex(sub_folder_title))));
                layerList.setmStrSubFolderId(c.getString((c.getColumnIndex(sub_folder_id))));
                layerList.setmStrMainLocationTitle(c.getString((c.getColumnIndex(main_location_title))));
                layerList.setmStrMainLocationId(c.getString((c.getColumnIndex(main_location_local))));
                layerList.setmStrLayerImg(c.getString((c.getColumnIndex(layer_img))));
                layerList.setmStrLayerArchive(c.getString((c.getColumnIndex(layer_archive))));
                layerList.setmStrMainLocationServerId(c.getString((c.getColumnIndex(main_location_server))));
                mAuditList.add(layerList);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public static void funUpdateSubFolderLayer(LayerList layerList,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(opration.equals("1")){//update layer title,layer desc
            values.put(layer_title, layerList.getmStrLayerTitle());
            values.put(layer_desc, layerList.getmStrLayerDesc());
            db.update(tb_sub_folder_layer, values, "id = " + layerList.getmStrId(), null);
        }else if(opration.equals("2")){//update image
            values.put(layer_img, layerList.getmStrLayerImg());
            db.update(tb_sub_folder_layer, values, "id = " + layerList.getmStrId(), null);
        }else if(opration.equals("3")){//update archive
            values.put(layer_archive, layerList.getmStrLayerArchive());
            db.update(tb_sub_folder_layer, values, "id = " + layerList.getmStrId(), null);
        }

    }



    public static void funDeleteSubFolderLayer(String mStrId,String mAuditId,String opration,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if(opration.equals("1")){//delete layer by sub folder id
        db.delete(tb_sub_folder_layer, sub_folder_id + " = " + mStrId, null);
        }else if(opration.equals("2")){//delete layer by layer id
        db.delete(tb_sub_folder_layer, id + " = " + mStrId, null);
        }else if(opration.equals("3")){//delete layer by main location
        db.delete(tb_sub_folder_layer, main_location_server+ " = " + mStrId + " AND " + audit_id + " = " + mAuditId, null);
        }

    }

    public static boolean funIsLayerByMainLocation(String mAuditId, String mUserId, String mLocationId,DatabaseHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_sub_folder_layer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + main_location_server + " = " + mLocationId, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }





    /*public static String ct_tb_sub_folder_explation_list = "";


    public static String ct_tb_sub_folder_explation_list = "CREATE TABLE "
            + tb_sub_folder_layer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_main_location_id + " TEXT NOT NULL, "
            + audit_main_location_server_id + " TEXT NOT NULL, "
            + audit_main_location_title + " TEXT NOT NULL, "
            + audit_sub_folder_id + " TEXT NOT NULL, "
            + audit_sub_folder_title + " TEXT NOT NULL, "
            + audit_layer_title + " TEXT NOT NULL, "
            + audit_layer_desc + " TEXT NOT NULL, "
            + audit_layer_img + " TEXT, "
            + audit_layer_archive + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL )";*/
}
