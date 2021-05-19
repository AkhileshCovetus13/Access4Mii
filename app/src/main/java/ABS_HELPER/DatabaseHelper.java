package ABS_HELPER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditInspectorQuestion;
import ABS_GET_SET.AuditMainLocation;
import ABS_GET_SET.AuditQuestion;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_GET_SET.AuditSubLocation;
import ABS_GET_SET.AuditSubQuestion;
import ABS_GET_SET.ExplanationListImage;
import ABS_GET_SET.Inspector;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.MainLocationSubFolder;
import ABS_GET_SET.Message;
import ABS_GET_SET.MessageChat;
import ABS_GET_SET.SelectedLocation;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_GET_SET.SubQuestionAnswer;

import static ABS_HELPER.StringUtils.*;
import static Modal.AuditListModal.table_tb_all_audit;
import static Modal.MainLocationModal.location_count;
import static Modal.MainLocationModal.location_server_id;
import static Modal.MainLocationModal.table_all_main_location;
import static Modal.MainLocationModal.table_selected_main_location;
import static Modal.MainLocationModal.tb_all_main_location;
import static Modal.SubFolderModal.tabel_sub_folder_layer;
import static Modal.SubFolderModal.table_all_sub_folders;
import static Modal.SubFolderModal.tb_all_sub_folders;
import static Modal.SubFolderModal.tb_sub_folder_layer;
import static Modal.SubLocationLayer.table_image_sub_location_Layer;
import static Modal.SubLocationLayer.table_image_sub_location_Layer_final;
import static Modal.SubLocationLayer.table_sub_location_Layer;
import static Modal.SubLocationLayer.tb_image_sub_location_layer;
import static Modal.SubLocationLayer.tb_sub_location_layer;
import static Modal.SubLocationModal.main_location_server;
import static Modal.SubLocationModal.table_all_sub_location;
import static Modal.SubLocationModal.table_selected_sub_location;
import static Modal.SubLocationModal.tb_all_sub_location;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(tb_list_audit);
        sqLiteDatabase.execSQL(table_tb_all_audit);
        sqLiteDatabase.execSQL(table_all_main_location);
        sqLiteDatabase.execSQL(table_all_sub_folders);
        sqLiteDatabase.execSQL(table_selected_main_location);
        sqLiteDatabase.execSQL(ct_tb_inspector_questions);

        sqLiteDatabase.execSQL(tabel_sub_folder_layer);
        sqLiteDatabase.execSQL(table_all_sub_location);
        sqLiteDatabase.execSQL(table_selected_sub_location);

        sqLiteDatabase.execSQL(table_sub_location_Layer);
        sqLiteDatabase.execSQL(table_image_sub_location_Layer);

        //sqLiteDatabase.execSQL(ct_tb_audit_sub_location);
        sqLiteDatabase.execSQL(ct_tb_main_question);
        sqLiteDatabase.execSQL(ct_tb_sub_questions);
        //sqLiteDatabase.execSQL(ct_tb_location_sub_folder);

        //sqLiteDatabase.execSQL(ct_tb_sub_folder_explation_list);
        sqLiteDatabase.execSQL(ct_tb_chat_user_list);
        sqLiteDatabase.execSQL(ct_tb_chat_msg_list);
        //sqLiteDatabase.execSQL(ct_tb_selected_sub_location);
        //sqLiteDatabase.execSQL(ct_tb_sub_location_explation_list);
        //sqLiteDatabase.execSQL(ct_tb_image_sub_location_explation_list);
        sqLiteDatabase.execSQL(ct_tb_audit_question_answer);
        sqLiteDatabase.execSQL(ct_tb_audit_sub_question_answer);
        sqLiteDatabase.execSQL(ct_tb_audit_inspector_question_answer);
        sqLiteDatabase.execSQL(ct_tb_audit_final_question_answer);
        sqLiteDatabase.execSQL(table_image_sub_location_Layer_final);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_audit_list);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tb_chat_user_list);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tb_chat_msg_list);


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_tb_all_audit);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_all_main_location);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_all_sub_folders);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_selected_main_location);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_inspector_questions);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tabel_sub_folder_layer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_all_sub_location);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_selected_sub_location);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_sub_location_Layer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_image_sub_location_Layer);

        //sqLiteDatabase.execSQL(ct_tb_audit_sub_location);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_main_question);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_sub_questions);
        //sqLiteDatabase.execSQL(ct_tb_location_sub_folder);

        //sqLiteDatabase.execSQL(ct_tb_sub_folder_explation_list);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_chat_user_list);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_chat_msg_list);
        //sqLiteDatabase.execSQL(ct_tb_selected_sub_location);
        //sqLiteDatabase.execSQL(ct_tb_sub_location_explation_list);
        //sqLiteDatabase.execSQL(ct_tb_image_sub_location_explation_list);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_audit_question_answer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_audit_sub_question_answer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_audit_inspector_question_answer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ct_tb_audit_final_question_answer);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_image_sub_location_Layer_final);




        onCreate(sqLiteDatabase);
    }


    public void delete_all_data_of_audit(String mAudiId, String mUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_all_main_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_all_sub_folders, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_selected_main_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_sub_folder_layer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_all_sub_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_selected_sub_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_sub_location_layer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_image_sub_location_layer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_main_question, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_sub_questions, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_inspector_questions, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_audit_question_answer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_audit_inspector_question_answer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        db.delete(tb_audit_final_question_answer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    }







    public void delete_raw_data_of_audit(String mAudiId, String mUserId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(tb_all_main_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    db.delete(tb_all_sub_location, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    db.delete(tb_main_question, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    db.delete(tb_inspector_questions, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    db.delete(tb_sub_questions, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    }




    public ArrayList<Inspector> get_tb_inspector_questions(Inspector inspector) {
        ArrayList<Inspector> mAuditList = new ArrayList<Inspector>();
        String selectQuery = "SELECT * FROM " + tb_inspector_questions + " WHERE " + main_question + " = " + inspector.getmStrMainQuestion() + " AND " + sub_location_id + " = " + inspector.getmStrSubLocation() + " AND " + user_id + " = " + inspector.getmStrUserId() + " AND " + audit_id + " = " + inspector.getmStrAuditId();
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Inspector inspectors = new Inspector();
                inspectors.setmStrId(c.getString((c.getColumnIndex(id))));
                inspectors.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                inspectors.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                inspectors.setmStrQuestionId(c.getString((c.getColumnIndex(question_server_id))));
                inspectors.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                inspectors.setmStrQuestionTxt(c.getString((c.getColumnIndex(question_txt))));
                inspectors.setmStrMainQuestion(c.getString((c.getColumnIndex(main_question))));
                inspectors.setmStrAnswerOptionId(c.getString((c.getColumnIndex(answer_option_id))));
                inspectors.setmStrAnswerOption(c.getString((c.getColumnIndex(answer_option))));
                inspectors.setmStrSubLocation(c.getString((c.getColumnIndex(sub_location_id))));
                inspectors.setmStrMainLocation(c.getString((c.getColumnIndex(main_location_id))));
                mAuditList.add(inspectors);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }





    //Insert tb_inspector_questions
    public void insert_tb_inspector_questions_answer(AuditInspectorQuestion auditInspectorQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, auditInspectorQuestion.getmStrAuditId());
        values.put(user_id, auditInspectorQuestion.getmStrUserId());
        values.put(question_id, auditInspectorQuestion.getmStrQuestionId());
        values.put(answer_id, auditInspectorQuestion.getmStrAnswerId());
        values.put(question_type, auditInspectorQuestion.getmStrQuestionType());
        values.put(question_image,auditInspectorQuestion.getmStrQuestionImage());
        values.put(question_priority,auditInspectorQuestion.getmStrQuestionPriority());
        values.put(main_question,auditInspectorQuestion.getmStrMainQuestion());
        values.put(sub_location_explanation_id,auditInspectorQuestion.getmStrSubLocationExplanation());
        values.put(main_location,auditInspectorQuestion.getmStrMainLocation());
        values.put(sub_location,auditInspectorQuestion.getmStrSubLocation());
        db.insert(tb_audit_inspector_question_answer, null, values);
    }

    public void delete_tb_inspector_questions(AuditInspectorQuestion auditInspectorQuestion) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(tb_audit_inspector_question_answer, audit_id + " = " + auditInspectorQuestion.getmStrAuditId() + " AND " + user_id + " = " + auditInspectorQuestion.getmStrUserId() + " AND " + question_id + " = " + auditInspectorQuestion.getmStrQuestionId() + " AND " + main_question + " = " + auditInspectorQuestion.getmStrMainQuestion() + " AND " + sub_location + " = " + auditInspectorQuestion.getmStrSubLocation() + " AND " + sub_location_explanation_id + " = " + auditInspectorQuestion.getmStrSubLocationExplanation(), null);
    }

    public boolean isInspecterQuestionAnswerSubmmited(AuditInspectorQuestion auditInspectorQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + auditInspectorQuestion.getmStrAuditId() + " AND " + user_id + " = " + auditInspectorQuestion.getmStrUserId() + " AND " + question_id + " = " + auditInspectorQuestion.getmStrQuestionId() + " AND " + main_question + " = " + auditInspectorQuestion.getmStrMainQuestion() + " AND " + sub_location + " = " + auditInspectorQuestion.getmStrSubLocation() + " AND " + sub_location_explanation_id + " = " + auditInspectorQuestion.getmStrSubLocationExplanation(), null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }

    public boolean isInspecterQuestionAnswerSubmmitedNew(AuditInspectorQuestion auditInspectorQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + auditInspectorQuestion.getmStrAuditId() + " AND " + user_id + " = " + auditInspectorQuestion.getmStrUserId() + " AND " + main_question + " = " + auditInspectorQuestion.getmStrMainQuestion() + " AND " + sub_location + " = " + auditInspectorQuestion.getmStrSubLocation() + " AND " + sub_location_explanation_id + " = " + auditInspectorQuestion.getmStrSubLocationExplanation(), null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }


    public ArrayList<AuditInspectorQuestion> get_tb_inspector_questions_answer(AuditInspectorQuestion auditInspectorQuestion) {
        ArrayList<AuditInspectorQuestion> mAuditList = new ArrayList<AuditInspectorQuestion>();
        String selectQuery = "SELECT * FROM " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + auditInspectorQuestion.getmStrAuditId() + " AND " + user_id + " = " + auditInspectorQuestion.getmStrUserId() + " AND " + question_id + " = " + auditInspectorQuestion.getmStrQuestionId() + " AND " + main_question + " = " + auditInspectorQuestion.getmStrMainQuestion() + " AND " + sub_location + " = " + auditInspectorQuestion.getmStrSubLocation() + " AND " + sub_location_explanation_id + " = " + auditInspectorQuestion.getmStrSubLocationExplanation();
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditInspectorQuestion auditInspectorQuestion1 = new AuditInspectorQuestion();
                auditInspectorQuestion1.setmStrId(c.getString((c.getColumnIndex(id))));
                auditInspectorQuestion1.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditInspectorQuestion1.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditInspectorQuestion1.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditInspectorQuestion1.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditInspectorQuestion1.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditInspectorQuestion1.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditInspectorQuestion1.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditInspectorQuestion1.setmStrMainQuestion(c.getString((c.getColumnIndex(main_question))));
                auditInspectorQuestion1.setmStrSubLocationExplanation(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                auditInspectorQuestion1.setmStrMainLocation(c.getString((c.getColumnIndex(main_location))));
                auditInspectorQuestion1.setmStrSubLocation(c.getString((c.getColumnIndex(sub_location))));
                mAuditList.add(auditInspectorQuestion1);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }

    public ArrayList<AuditInspectorQuestion> get_tb_inspector_questions_answer_new(AuditInspectorQuestion auditInspectorQuestion) {
        ArrayList<AuditInspectorQuestion> mAuditList = new ArrayList<AuditInspectorQuestion>();
        String selectQuery = "SELECT * FROM " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + auditInspectorQuestion.getmStrAuditId() + " AND " + user_id + " = " + auditInspectorQuestion.getmStrUserId() + " AND " + main_question + " = " + auditInspectorQuestion.getmStrMainQuestion() + " AND " + sub_location + " = " + auditInspectorQuestion.getmStrSubLocation() + " AND " + sub_location_explanation_id + " = " + auditInspectorQuestion.getmStrSubLocationExplanation();
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditInspectorQuestion auditInspectorQuestion1 = new AuditInspectorQuestion();
                auditInspectorQuestion1.setmStrId(c.getString((c.getColumnIndex(id))));
                auditInspectorQuestion1.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditInspectorQuestion1.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditInspectorQuestion1.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditInspectorQuestion1.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditInspectorQuestion1.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditInspectorQuestion1.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditInspectorQuestion1.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditInspectorQuestion1.setmStrMainQuestion(c.getString((c.getColumnIndex(main_question))));
                auditInspectorQuestion1.setmStrSubLocationExplanation(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                auditInspectorQuestion1.setmStrMainLocation(c.getString((c.getColumnIndex(main_location))));
                auditInspectorQuestion1.setmStrSubLocation(c.getString((c.getColumnIndex(sub_location))));
                mAuditList.add(auditInspectorQuestion1);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }



    //Insert tb_inspector_questions
    public void insert_tb_inspector_questions(Inspector inspector) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, inspector.getmStrAuditId());
        values.put(user_id, inspector.getmStrUserId());
        values.put(main_question, inspector.getmStrMainQuestion());
        values.put(main_location_id, inspector.getmStrMainLocation());
        values.put(sub_location_id, inspector.getmStrSubLocation());
        values.put(question_server_id, inspector.getmStrQuestionId());
        values.put(question_type, inspector.getmStrQuestionType());
        values.put(question_txt,inspector.getmStrQuestionTxt());
        values.put(answer_option,inspector.getmStrAnswerOption());
        values.put(answer_option_id,inspector.getmStrAnswerOptionId());
        db.insert(tb_inspector_questions, null, values);
    }



    public SQLiteDatabase funGetWritableDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }

    public SQLiteDatabase funGetReadableDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db;
    }

    public boolean isSubQuestionAnswerSubmmited(String mStrQuestionId, String mAuditId, String mSubLocationExplantionId, String mStrIsNormal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + question_server_id + " = " + mStrQuestionId + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplantionId + " AND " + is_normal + " = " + mStrIsNormal, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }

    public ArrayList<SubQuestionAnswer> get_tb_audit_sub_question_ans(String mStrQuestionId, String mAuditId, String mSubLocationExplanationId, String mStrIsnormal) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + question_server_id + " = " + mStrQuestionId + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsnormal;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }



    public ArrayList<SubQuestionAnswer> sub_question_ans(String mStrMainQuestion, String mAuditId, String mSubLocationExplanationId, String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + main_question + " = " + mStrMainQuestion + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public ArrayList<SubQuestionAnswer> sub_question_ans2(String mStrMainQuestion, String mAuditId, String mSubLocationExplanationId,String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + main_question + " = " + mStrMainQuestion + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + question_for + " = " + mStrQuestionFor;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }

    public ArrayList<SubQuestionAnswer> sub_question_ans3(String mStrMainQuestion, String mAuditId, String mSubLocationExplanationId,String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + main_question + " = " + mStrMainQuestion + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public ArrayList<SubQuestionAnswer> getSubQuestionAnsByCondition(String mAuditId,String mUserId,String mStrMainQuestion, String mStrQuestionFor, String mSubLocationExplanationId) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + main_question + " = " + mStrMainQuestion + " AND " + question_for + " = " + mStrQuestionFor;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public ArrayList<SubQuestionAnswer> get_sub_question_ans(String mStrQuestionId, String mAuditId, String mSubLocationExplanationId, String mStrIsnormal) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + main_question + " = " + mStrQuestionId + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsnormal;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public boolean isSubQuestionPresentByMainQuestionAndQuestionFor(String mStrMainQuestion, String mAuditId, String mSubLocationExplanationId, String mStrIsNormal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + main_question + " = " + mStrMainQuestion + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }

    public void update_tb_audit_sub_question_answer(SubQuestionAnswer subQuestionAnswer) {
        System.out.println("<><>222 call ");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question_id, subQuestionAnswer.getmStrQuestionId());
        values.put(answer_id, subQuestionAnswer.getmStrAnswerId());
        values.put(answer_value, subQuestionAnswer.getmStrAnswerValue());
        values.put(question_type, subQuestionAnswer.getmStrQuestionType());
        values.put(question_image, subQuestionAnswer.getmStrQuestionImage());
        values.put(question_priority, subQuestionAnswer.getmStrQuestionPriority());
        values.put(question_extra_text, subQuestionAnswer.getmStrQuestionExtraText());
        values.put(is_question_parent, subQuestionAnswer.getmStrIsQuestionParent());
        db.update(tb_audit_sub_question_answer, values, question_server_id + "=" + subQuestionAnswer.getmStrQuestionServerId() + " AND " + audit_id + " = " + subQuestionAnswer.getmStrAuditId() + " AND " + sub_location_explanation_id + " = " + subQuestionAnswer.getmSubLocationExplanationId() + " AND " + is_normal + " = " + subQuestionAnswer.getmStrIsQuestionNormal(), null);
    }


    //Insert tb_audit_sub_question_answer
    public void insert_tb_audit_sub_question_answer(SubQuestionAnswer subQuestionAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, subQuestionAnswer.getmStrAuditId());
        values.put(user_id, subQuestionAnswer.getmStrUserId());
        values.put(question_id, subQuestionAnswer.getmStrQuestionId());
        values.put(question_server_id, subQuestionAnswer.getmStrQuestionServerId());
        values.put(answer_id, subQuestionAnswer.getmStrAnswerId());
        values.put(answer_value, subQuestionAnswer.getmStrAnswerValue());
        values.put(question_type, subQuestionAnswer.getmStrQuestionType());
        values.put(question_image, subQuestionAnswer.getmStrQuestionImage());
        values.put(question_priority, subQuestionAnswer.getmStrQuestionPriority());
        values.put(question_extra_text, subQuestionAnswer.getmStrQuestionExtraText());
        values.put(is_question_parent, subQuestionAnswer.getmStrIsQuestionParent());
        values.put(main_question, subQuestionAnswer.getmStrMainQuestionId());
        values.put(question_for, subQuestionAnswer.getmStrQuestionFor());
        values.put(sub_location_explanation_id, subQuestionAnswer.getmSubLocationExplanationId());
        values.put(is_normal, subQuestionAnswer.getmStrIsQuestionNormal());
        db.insert(tb_audit_sub_question_answer, null, values);
    }


    public ArrayList<SubQuestionAnswer> get_tb_audit_sub_question(String mAuditId, String mMainQuestion, String mSubLocationExplanationId, String mStrIsNormal) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public ArrayList<SubQuestionAnswer> get_all_tb_audit_sub_question_answer_by_questionServer_id(String mAuditId, String mQuestionServer, String mSubLocationExplanationId, String mStrIsNormal) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + question_server_id + " = " + mQuestionServer + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }





    public ArrayList<SubQuestionAnswer> getSubQuestionAnsByQuestionId(String mAuditId, String mMainQuestionId, String mSubLocationExplanationId,String mAnswerId, String mStrIsNormal) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestionId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + question_for + " = " + mAnswerId + " AND " + is_normal + " = " + mStrIsNormal;
        System.out.println("<><>query "+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }




  /*  //Delete tb_audit_sub_question_answer
    public void delete_tb_audit_sub_question_by_is parent(String mAuditId,String mMainQuestion,String mQuestionFor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion+ " AND " + is_question_parent + " = " + mQuestionFor, null);
    }*/


    //Delete tb_audit_sub_question_answer
    public void delete_tb_audit_sub_question_answer(String mAuditId, String mMainQuestion, String mSubLocationExplanationID, String mStrIsNormal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + question_server_id + " = " + mMainQuestion + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationID + " AND " + is_normal + " = " + mStrIsNormal, null);
    }

    //Delete tb_audit_sub_question_answer
    public void delete_tb_audit_sub_question_answer_by_main_question(String mAuditId, String mMainQuestion, String mSubLocationExplanationID, String mStrIsNormal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationID + " AND " + is_normal + " = " + mStrIsNormal, null);
    }

    public void delete_answer_by_main_question(String mAuditId, String mMainQuestion,String mStrQuestionFor,String mSubLocationExplanationID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion+ " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationID, null);
    }

    public void delete_answer_by_main_question2(String mAuditId, String mMainQuestion,String mSubLocationExplanationID,String mStrQuestionFor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationID + " AND " + question_for + " = " + mStrQuestionFor, null);
    }

    public void delete_answer_by_main_question3(String mAuditId, String mMainQuestion,String mSubLocationExplanationID,String mStrQuestionFor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestion + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationID, null);
    }

    public boolean isNormalQuestionAnswerSubmmited(String mStrQuestionId, String mAuditId, String mSubLocationExplanationId, String mStrIsNormal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_question_answer + " WHERE " + question_server_id + " = " + mStrQuestionId + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }

    public void update_tb_audit_question_answer_normal(AuditQuestionAnswer auditQuestionAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question_id, auditQuestionAnswer.getmStrQuestionId());
        values.put(question_server_id, auditQuestionAnswer.getmStrQuestionServerId());
        values.put(answer_id, auditQuestionAnswer.getmStrAnswerId());
        values.put(answer_value, auditQuestionAnswer.getmStrAnswerValue());
        values.put(question_type, auditQuestionAnswer.getmStrQuestionType());
        values.put(question_image, auditQuestionAnswer.getmStrQuestionImage());
        values.put(question_priority, auditQuestionAnswer.getmStrQuestionPriority());
        values.put(question_extra_text, auditQuestionAnswer.getmStrQuestionExtraText());
        db.update(tb_audit_question_answer, values, question_server_id + "=" + auditQuestionAnswer.getmStrQuestionServerId() + " AND " + audit_id + " = " + auditQuestionAnswer.getmStrAuditId() + " AND " + sub_location_explanation_id + " = " + auditQuestionAnswer.getmStrSubLocationExplanationId() + " AND " + is_normal + " = " + auditQuestionAnswer.getmStrIsQuestionNormal(), null);
    }

  /*  public ArrayList<String> getAllExplanationImage(String mStrAuditId,String mStrUserId,String mExplanationId) {
        ArrayList<String> mAuditList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_explation_list + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId + " AND " + img_default + " = 0";
        System.out.println("<><><>selectQuery "+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                mAuditList.add(c.getString((c.getColumnIndex(explation_list_image))));
            } while (c.moveToNext());
        }
        return mAuditList;

    }*/

    public void deleteAllExplanationImage(String mStrAuditId, String mStrUserId, String mExplanationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_image_sub_location_explation_list, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId, null);
    }


    public ArrayList<SubQuestionAnswer> getAllSubQuestion(String mStrAuditId, String mStrUserId, String mStrMainQuestion, String mStrQuestionFor) {
        ArrayList<SubQuestionAnswer> mAuditList = new ArrayList<SubQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_sub_question_answer + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + main_question + " = " + mStrMainQuestion + " AND " + question_for + " = " + mStrQuestionFor;
        System.out.println("<><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubQuestionAnswer subQuestionAnswer = new SubQuestionAnswer();
                subQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                subQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                subQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                subQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                subQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                subQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                subQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                subQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                subQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                subQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                subQuestionAnswer.setmStrMainQuestionId(c.getString((c.getColumnIndex(main_question))));
                subQuestionAnswer.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                subQuestionAnswer.setmSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                subQuestionAnswer.setmStrIsQuestionNormal(c.getString((c.getColumnIndex(is_normal))));
                mAuditList.add(subQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }



    //get All tb_audit_question_answer
    public ArrayList<AuditQuestionAnswer> getAllMainQuestionAnswer(String mStrAuditId, String mStrUserId) {
        ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId;
        System.out.println("<><><>selectQuery" + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                auditQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditQuestionAnswer.setmStrCountryId(c.getString((c.getColumnIndex(country_id))));
                auditQuestionAnswer.setmStrLangId(c.getString((c.getColumnIndex(lang_id))));
                auditQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                auditQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                auditQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                auditQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                auditQuestionAnswer.setmStrSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                auditQuestionAnswer.setmStrSubLocationExplanationTitle(c.getString((c.getColumnIndex(sub_location_explanation_title))));
                auditQuestionAnswer.setmStrSubLocationExplanationDesc(c.getString((c.getColumnIndex(sub_location_explanation_desc))));
                auditQuestionAnswer.setmStrSubLocationExplanationImage(c.getString((c.getColumnIndex(sub_location_explanation_image))));
                auditQuestionAnswer.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                auditQuestionAnswer.setmStrSubFolderLayerId(c.getString((c.getColumnIndex(sub_folder_layer_id))));
                auditQuestionAnswer.setmStrSubFolderLayerTitle(c.getString((c.getColumnIndex(sub_folder_layer_title))));
                auditQuestionAnswer.setmStrSubFolderLayerDesc(c.getString((c.getColumnIndex(sub_folder_layer_desc))));
                auditQuestionAnswer.setmStrSubFolderLayerImage(c.getString((c.getColumnIndex(sub_folder_layer_image))));
                auditQuestionAnswer.setmStrSubFolderId(c.getString((c.getColumnIndex(sub_folder_id))));
                auditQuestionAnswer.setmStrSubFolderTitle(c.getString((c.getColumnIndex(sub_folder_title))));
                auditQuestionAnswer.setmStrMainLocationServerId(c.getString((c.getColumnIndex(main_location_server_id))));
                auditQuestionAnswer.setmStrIsQuestionNormal(c.getString((c.getColumnIndex(is_normal))));
                auditQuestionAnswer.setmStrStatus(c.getString((c.getColumnIndex(status))));
                mAuditList.add(auditQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    public ArrayList<AuditQuestionAnswer> getAllSubmitedQuestionAnswerById(String mStrQuestionId, String mAuditId, String mSubLocationExplanationId, String mStrIsNormal) {
        ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_question_answer + " WHERE " + question_server_id + " = " + mStrQuestionId + " AND " + audit_id + " = " + mAuditId + " AND " + sub_location_explanation_id + " = " + mSubLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal;
        System.out.println("<><><>selectQuery" + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                auditQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditQuestionAnswer.setmStrCountryId(c.getString((c.getColumnIndex(country_id))));
                auditQuestionAnswer.setmStrLangId(c.getString((c.getColumnIndex(lang_id))));
                auditQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                auditQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                auditQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                auditQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                auditQuestionAnswer.setmStrSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                auditQuestionAnswer.setmStrSubLocationExplanationTitle(c.getString((c.getColumnIndex(sub_location_explanation_title))));
                auditQuestionAnswer.setmStrSubLocationExplanationDesc(c.getString((c.getColumnIndex(sub_location_explanation_desc))));
                auditQuestionAnswer.setmStrSubLocationExplanationImage(c.getString((c.getColumnIndex(sub_location_explanation_image))));
                auditQuestionAnswer.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                auditQuestionAnswer.setmStrSubFolderLayerId(c.getString((c.getColumnIndex(sub_folder_layer_id))));
                auditQuestionAnswer.setmStrSubFolderLayerTitle(c.getString((c.getColumnIndex(sub_folder_layer_title))));
                auditQuestionAnswer.setmStrSubFolderLayerDesc(c.getString((c.getColumnIndex(sub_folder_layer_desc))));
                auditQuestionAnswer.setmStrSubFolderLayerImage(c.getString((c.getColumnIndex(sub_folder_layer_image))));
                auditQuestionAnswer.setmStrSubFolderId(c.getString((c.getColumnIndex(sub_folder_id))));
                auditQuestionAnswer.setmStrSubFolderTitle(c.getString((c.getColumnIndex(sub_folder_title))));
                auditQuestionAnswer.setmStrMainLocationServerId(c.getString((c.getColumnIndex(main_location_server_id))));
                auditQuestionAnswer.setmStrIsQuestionNormal(c.getString((c.getColumnIndex(is_normal))));
                auditQuestionAnswer.setmStrStatus(c.getString((c.getColumnIndex(status))));
                mAuditList.add(auditQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }






    public ArrayList<AuditQuestionAnswer> getAllMainQuestionAnswerFinal(String mStrAuditId, String mStrUserId) {
        ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_final_question_answer + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId;
        System.out.println("<><><>selectQuery" + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                auditQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestionAnswer.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditQuestionAnswer.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditQuestionAnswer.setmStrCountryId(c.getString((c.getColumnIndex(country_id))));
                auditQuestionAnswer.setmStrLangId(c.getString((c.getColumnIndex(lang_id))));
                auditQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                auditQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                auditQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                auditQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                auditQuestionAnswer.setmStrSubLocationExplanationId(c.getString((c.getColumnIndex(sub_location_explanation_id))));
                auditQuestionAnswer.setmStrSubLocationExplanationTitle(c.getString((c.getColumnIndex(sub_location_explanation_title))));
                auditQuestionAnswer.setmStrSubLocationExplanationDesc(c.getString((c.getColumnIndex(sub_location_explanation_desc))));
                auditQuestionAnswer.setmStrSubLocationExplanationImage(c.getString((c.getColumnIndex(sub_location_explanation_image))));
                auditQuestionAnswer.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                auditQuestionAnswer.setmStrSubFolderLayerId(c.getString((c.getColumnIndex(sub_folder_layer_id))));
                auditQuestionAnswer.setmStrSubFolderLayerTitle(c.getString((c.getColumnIndex(sub_folder_layer_title))));
                auditQuestionAnswer.setmStrSubFolderLayerDesc(c.getString((c.getColumnIndex(sub_folder_layer_desc))));
                auditQuestionAnswer.setmStrSubFolderLayerImage(c.getString((c.getColumnIndex(sub_folder_layer_image))));
                auditQuestionAnswer.setmStrSubFolderId(c.getString((c.getColumnIndex(sub_folder_id))));
                auditQuestionAnswer.setmStrSubFolderTitle(c.getString((c.getColumnIndex(sub_folder_title))));
                auditQuestionAnswer.setmStrMainLocationServerId(c.getString((c.getColumnIndex(main_location_server_id))));
                auditQuestionAnswer.setmStrIsQuestionNormal(c.getString((c.getColumnIndex(is_normal))));
                auditQuestionAnswer.setmStrStatus(c.getString((c.getColumnIndex(status))));
                auditQuestionAnswer.setmStrIsInspector(c.getString((c.getColumnIndex(is_inspector))));
                mAuditList.add(auditQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }

    public int getAllMainQuestionAnswerCount(String mStrAuditId, String mStrUserId) {
        String selectQuery = "SELECT * FROM " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + is_normal + " = 1";
        System.out.println("<><><>selectQuery" + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();

    }


    public void deleteAllQuestionAnswer(String mStrMainLocation,String mAudit,String mUser) {
        System.out.println("<><>dlete "+mStrMainLocation);
        System.out.println("<><>dlete "+mAudit);
        System.out.println("<><>dlete "+mUser);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_question_answer, main_location_server_id + "=" + mStrMainLocation + " AND " + audit_id + " = " + mAudit + " AND " + user_id + " = " + mUser, null);
    }


    public void deleteSubQuestionAnswer(String mAudiId,String mUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_sub_question_answer, audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
    }



    public boolean isQuestionExist(String mStrQuestionId, String mStrMainLocation, String mStrSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_question_answer + " WHERE " + question_server_id + " = " + mStrQuestionId + " AND " + main_location_server_id + " = " + mStrMainLocation + " AND " + sub_location_server_id + " = " + mStrSubLocation, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }

    public boolean isQuestionRemaining() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_audit_final_question_answer + " WHERE " + status + " = 0", null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }




    public void updateQuestionStatus(String mQuestionServerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(status, "1");
        db.update(tb_audit_final_question_answer, values, question_server_id + " = " + mQuestionServerId, null);
    }


    //Insert tb_audit_question_answer
    public void insert_tb_audit_question_answer(AuditQuestionAnswer auditQuestionAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, auditQuestionAnswer.getmStrAuditId());
        values.put(user_id, auditQuestionAnswer.getmStrUserId());
        values.put(country_id, auditQuestionAnswer.getmStrCountryId());
        values.put(lang_id, auditQuestionAnswer.getmStrLangId());
        values.put(question_id, auditQuestionAnswer.getmStrQuestionId());
        values.put(question_server_id, auditQuestionAnswer.getmStrQuestionServerId());
        values.put(answer_id, auditQuestionAnswer.getmStrAnswerId());
        values.put(answer_value, auditQuestionAnswer.getmStrAnswerValue());
        values.put(question_type, auditQuestionAnswer.getmStrQuestionType());
        values.put(question_image, auditQuestionAnswer.getmStrQuestionImage());
        values.put(question_priority, auditQuestionAnswer.getmStrQuestionPriority());
        values.put(question_extra_text, auditQuestionAnswer.getmStrQuestionExtraText());
        values.put(is_question_parent, auditQuestionAnswer.getmStrIsQuestionParent());
        values.put(sub_location_explanation_id, auditQuestionAnswer.getmStrSubLocationExplanationId());
        values.put(sub_location_explanation_title, auditQuestionAnswer.getmStrSubLocationExplanationTitle());
        values.put(sub_location_explanation_desc, auditQuestionAnswer.getmStrSubLocationExplanationDesc());
        values.put(sub_location_explanation_image, auditQuestionAnswer.getmStrSubLocationExplanationImage());
        values.put(sub_location_server_id, auditQuestionAnswer.getmStrSubLocationServerId());
        values.put(sub_folder_layer_id, auditQuestionAnswer.getmStrSubFolderLayerId());
        values.put(sub_folder_layer_title, auditQuestionAnswer.getmStrSubFolderLayerTitle());
        values.put(sub_folder_layer_desc, auditQuestionAnswer.getmStrSubFolderLayerDesc());
        values.put(sub_folder_layer_image, auditQuestionAnswer.getmStrSubFolderLayerImage());
        values.put(sub_folder_id, auditQuestionAnswer.getmStrSubFolderId());
        values.put(sub_folder_title, auditQuestionAnswer.getmStrSubFolderTitle());
        values.put(main_location_server_id, auditQuestionAnswer.getmStrMainLocationServerId());
        values.put(status, auditQuestionAnswer.getmStrStatus());
        values.put(is_normal, auditQuestionAnswer.getmStrIsQuestionNormal());
        db.insert(tb_audit_question_answer, null, values);
    }


    //Insert tb_audit_question_answer
    public void insert_tb_audit_final_question_answer(AuditQuestionAnswer auditQuestionAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, auditQuestionAnswer.getmStrAuditId());
        values.put(user_id, auditQuestionAnswer.getmStrUserId());
        values.put(country_id, auditQuestionAnswer.getmStrCountryId());
        values.put(lang_id, auditQuestionAnswer.getmStrLangId());
        values.put(question_id, auditQuestionAnswer.getmStrQuestionId());
        values.put(question_server_id, auditQuestionAnswer.getmStrQuestionServerId());
        values.put(answer_id, auditQuestionAnswer.getmStrAnswerId());
        values.put(answer_value, auditQuestionAnswer.getmStrAnswerValue());
        values.put(question_type, auditQuestionAnswer.getmStrQuestionType());
        values.put(question_image, auditQuestionAnswer.getmStrQuestionImage());
        values.put(question_priority, auditQuestionAnswer.getmStrQuestionPriority());
        values.put(question_extra_text, auditQuestionAnswer.getmStrQuestionExtraText());
        values.put(is_question_parent, auditQuestionAnswer.getmStrIsQuestionParent());
        values.put(sub_location_explanation_id, auditQuestionAnswer.getmStrSubLocationExplanationId());
        values.put(sub_location_explanation_title, auditQuestionAnswer.getmStrSubLocationExplanationTitle());
        values.put(sub_location_explanation_desc, auditQuestionAnswer.getmStrSubLocationExplanationDesc());
        values.put(sub_location_explanation_image, auditQuestionAnswer.getmStrSubLocationExplanationImage());
        values.put(sub_location_server_id, auditQuestionAnswer.getmStrSubLocationServerId());
        values.put(sub_folder_layer_id, auditQuestionAnswer.getmStrSubFolderLayerId());
        values.put(sub_folder_layer_title, auditQuestionAnswer.getmStrSubFolderLayerTitle());
        values.put(sub_folder_layer_desc, auditQuestionAnswer.getmStrSubFolderLayerDesc());
        values.put(sub_folder_layer_image, auditQuestionAnswer.getmStrSubFolderLayerImage());
        values.put(sub_folder_id, auditQuestionAnswer.getmStrSubFolderId());
        values.put(sub_folder_title, auditQuestionAnswer.getmStrSubFolderTitle());
        values.put(main_location_server_id, auditQuestionAnswer.getmStrMainLocationServerId());
        values.put(status, auditQuestionAnswer.getmStrStatus());
        values.put(is_normal, auditQuestionAnswer.getmStrIsQuestionNormal());
        values.put(is_inspector, auditQuestionAnswer.getmStrIsInspector());
        db.insert(tb_audit_final_question_answer, null, values);
    }

    public void delete_tb_audit_final_question_answer(String mStrAuditId, String mStrUserid) {
        System.out.println("<><>dlete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_final_question_answer, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserid, null);
    }

    public void delete_tb_audit_question_answer_when_update(AuditQuestionAnswer auditQuestionAnswer) {
        System.out.println("<><>dlete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_question_answer, audit_id + " = " + auditQuestionAnswer.getmStrAuditId() + " AND " + user_id + " = " + auditQuestionAnswer.getmStrUserId() + " AND " +question_server_id + " = " + auditQuestionAnswer.getmStrQuestionServerId() + " AND " + sub_location_explanation_id + " = " + auditQuestionAnswer.getmStrSubLocationExplanationId() + " AND " + sub_location_server_id + " = " + auditQuestionAnswer.getmStrSubLocationServerId() + " AND " + sub_folder_layer_id + " = " + auditQuestionAnswer.getmStrSubFolderLayerId() + " AND " + main_location_server_id  + " = " + auditQuestionAnswer.getmStrMainLocationServerId()  , null);
    }


    //get All tb_audit_question_answer
    public ArrayList<AuditQuestionAnswer> get_all_tb_audit_question_answer_normal(String mAuditId, String mQuestionId, String subLocationExplanationId, String mStrIsNormal) {
        ArrayList<AuditQuestionAnswer> mAuditList = new ArrayList<AuditQuestionAnswer>();
        String selectQuery = "SELECT * FROM " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + question_server_id + " = " + mQuestionId + " AND " + sub_location_explanation_id + " = " + subLocationExplanationId + " AND " + is_normal + " = " + mStrIsNormal;
        System.out.println("<><><><>selectQuery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestionAnswer auditQuestionAnswer = new AuditQuestionAnswer();
                auditQuestionAnswer.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestionAnswer.setmStrQuestionId(c.getString((c.getColumnIndex(question_id))));
                auditQuestionAnswer.setmStrQuestionServerId(c.getString((c.getColumnIndex(question_server_id))));
                auditQuestionAnswer.setmStrAnswerId(c.getString((c.getColumnIndex(answer_id))));
                auditQuestionAnswer.setmStrAnswerValue(c.getString((c.getColumnIndex(answer_value))));
                auditQuestionAnswer.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestionAnswer.setmStrQuestionImage(c.getString((c.getColumnIndex(question_image))));
                auditQuestionAnswer.setmStrQuestionPriority(c.getString((c.getColumnIndex(question_priority))));
                auditQuestionAnswer.setmStrQuestionExtraText(c.getString((c.getColumnIndex(question_extra_text))));
                auditQuestionAnswer.setmStrIsQuestionParent(c.getString((c.getColumnIndex(is_question_parent))));
                mAuditList.add(auditQuestionAnswer);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;

    }


    //Insert tb_image_sub_location_explation_list
    /*public void insert_tb_image_sub_location_explation_list(ExplanationListImage explanationListImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_id, explanationListImage.getmStrExplationListId());
        values.put(explation_list_image, explanationListImage.getmStrExplationListImage());
        values.put(user_id, explanationListImage.getmStrUserId());
        values.put(audit_id, explanationListImage.getmStrAuditId());
        values.put(img_default, explanationListImage.getmStrImgDefault());
        values.put(layer_id, explanationListImage.getmStrLayerId());
        db.insert(tb_image_sub_location_explation_list, null, values);
    }*/

    //Delete tb_sub_location_explation_list
   /* public void delete_tb_image_sub_location_explation_list(String mLayerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_image_sub_location_explation_list, layer_id + " = " + mLayerId, null);
    }*/

   /* public void update_tb_audit_question_answer_normal(ExplanationListImage explanationListImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_image, explanationListImage.getmStrExplationListImage());
        values.put(img_default, explanationListImage.getmStrImgDefault());
        db.update(tb_image_sub_location_explation_list, values, id + "=" + explanationListImage.getmStrId(), null);
    }

    public void update_tb_image_sub_location_explation_list_by_layer_id(String mStrAuditId,String mStrUserId,String mExplanationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_image,"");
        values.put(img_default, "1");
        db.update(tb_image_sub_location_explation_list, values, audit_id + " = " + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + explation_list_id + " = " + mExplanationId, null);
    }*/

    //Delete tb_image_sub_location_explation_list
    public void delete_tb_audit_question_answer(String mId) {
        System.out.println("<><>dlete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_question_answer, question_server_id + "=" + mId, null);
    }



  /*  //Update tb_image_sub_location_explation_list
    public void update_tb_audit_question_answer_normal(String mStrId, String mStrImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_image, mStrImage);
        db.update(tb_image_sub_location_explation_list, values, id + "=" + mStrId, null);
    }*/

    //Delete tb_image_sub_location_explation_list
   /* public void delete_tb_image_sub_location_explation_list(String mId) {
        System.out.println("<><>dlete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_image_sub_location_explation_list, id + "=" + mId, null);
    }*/


    //get All tb_image_sub_location_explation_list
 /*   public ArrayList<ExplanationListImage> get_all_tb_image_sub_location_explation_list(String mLayerId) {
        ArrayList<ExplanationListImage> mAuditList = new ArrayList<ExplanationListImage>();
        String selectQuery = "SELECT * FROM " + tb_image_sub_location_explation_list + " WHERE " + explation_list_id + " = " + mLayerId;
        SQLiteDatabase db = this.getReadableDatabase();
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
        return mAuditList;

    }*/


    /*//Insert tb_list_audit
    public void insert_tb_list_audit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, audit.getmUserId());
        values.put(audit_id, audit.getmAuditId());
        values.put(audit_assign_by, audit.getmAssignBy());
        values.put(audit_title, audit.getmTitle());
        values.put(audit_due_date, audit.getmDueDate());
        values.put(audit_work_status, audit.getmStatus());
        values.put(country_id, audit.getmStrCountryId());
        values.put(lang_id, audit.getmStrLangId());
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_list_audit + " WHERE " + audit_id + " = " + audit.getmAuditId(), null);
        boolean exist = (clientCur.getCount() > 0);
        if(exist){
        }else {
        db.insert(tb_list_audit, null, values);
        }
     }*/

    /*public void update_tb_list_audit(String mId, String mStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_work_status, mStatus);
        db.update(tb_list_audit, values, audit_id + "=" + mId, null);
    }*/


    //Delete tb_sub_location_explation_list


    public boolean mCheckSubFolderCount(String mAudiId, String mUserId) {
        boolean myStatus = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_main_location + " WHERE " + audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId, null);
        if (clientCur.moveToFirst()) {
            do {
                int mCount = Integer.parseInt(clientCur.getString((clientCur.getColumnIndex(location_count))));
                String mMainLocationId = clientCur.getString((clientCur.getColumnIndex(location_server_id)));
                Cursor clientgetSub = db.rawQuery("SELECT * FROM " + tb_all_sub_folders + " WHERE " + audit_id + " = " + mAudiId + " AND " + user_id + " = " + mUserId + " AND " + main_location_server + " = " + mMainLocationId, null);
                int mSubCount = 0;
                if (clientgetSub.moveToFirst()) {
                    do {
                        mSubCount = mSubCount + Integer.parseInt(clientgetSub.getString((clientgetSub.getColumnIndex(audit_sub_folder_count))));
                    } while (clientgetSub.moveToNext());
                    clientgetSub.close();
                }
                if (mSubCount != mCount) {
                    myStatus = false;
                    break;
                } else {
                    myStatus = true;
                }
            } while (clientCur.moveToNext());
        }
        clientCur.close();
        return myStatus;
    }


    //Insert tb_sub_location_explation_list
 /*   public int insert_tb_sub_location_explation_list(SubLocationExplation subLocationExplation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(layer_id, subLocationExplation.getmStrLayerId());
        values.put(layer_title, subLocationExplation.getmStrLayerTitle());
        values.put(layer_desc, subLocationExplation.getmStrLayerDesc());
        values.put(sub_location_title, subLocationExplation.getmStrSubLocationTitle());
        values.put(sub_location_id, subLocationExplation.getmStrSubLocationId());
        values.put(sub_location_server_id, subLocationExplation.getmStrSubLocationServerId());
        values.put(explanation_title, subLocationExplation.getmStrExplanationTitle());
        values.put(explanation_desc, subLocationExplation.getmStrExplanationDesc());
        values.put(explanation_status, subLocationExplation.getmStrExplanationStatus());
        values.put(explanation_work_done_percentage, subLocationExplation.getmStrExplanationWorkPercentage());
        values.put(audit_id, subLocationExplation.getmStrAuditId());
        values.put(user_id, subLocationExplation.getmStrUserId());
        values.put(main_location_id, subLocationExplation.getmStrMainLocationId());
        values.put(explanation_archive, subLocationExplation.getmStrExplanationArchive());
        db.insert(tb_sub_location_explation_list, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        return ID;
    }*/


    //update image tb_sub_location_explation_list
 /*   public void update_tb_sub_location_explation_list_image(SubLocationExplation subLocationExplation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explation_list_img, subLocationExplation.getmStrExplanationImage());
        db.update(tb_sub_location_explation_list, values, "id=" + subLocationExplation.getmStrId(), null);
    }*/


  /*  public void update_tb_tb_sub_location_explation_list(SubLocationExplation subLocationExplation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explanation_title, subLocationExplation.getmStrExplanationTitle());
        values.put(explanation_desc, subLocationExplation.getmStrExplanationDesc());
        db.update(tb_sub_location_explation_list, values, "id=" + subLocationExplation.getmStrId(), null);
    }*/

   /* public void delete_tb_sub_location_explation_list_single_row(String mId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_sub_location_explation_list, id + "=" + mId, null);
    }*/


    public void delete_main_question_by_opration(String mStrAuditId, String mStrUserId, String mStrOpration, String mStrId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (mStrOpration.equals("1")) {// delete by explanation id
            db.delete(tb_audit_question_answer, audit_id + "=" + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + sub_location_explanation_id + " = " + mStrId, null);
        } else if (mStrOpration.equals("2")) {// delete by sub location id
            db.delete(tb_audit_question_answer, audit_id + "=" + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + sub_location_server_id + " = " + mStrId, null);
        } else if (mStrOpration.equals("3")) {// delete by layer list id
            db.delete(tb_audit_question_answer, audit_id + "=" + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + sub_folder_layer_id + " = " + mStrId, null);
        } else if (mStrOpration.equals("4")) {// delete by main location id
            db.delete(tb_audit_question_answer, audit_id + "=" + mStrAuditId + " AND " + user_id + " = " + mStrUserId + " AND " + main_location_server_id + " = " + mStrId, null);
        }

    }

    /*  public int get_existing_count_main_location(String mAuditId, String mUserId, String mLocationId) {
          SQLiteDatabase db = this.getWritableDatabase();
          int a = 0;
          Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_main_location + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + audit_main_location_id + " = " + mLocationId, null);
          if (clientCur.moveToFirst()) {
              do {
                  a = Integer.parseInt(clientCur.getString((clientCur.getColumnIndex(audit_main_location_count))));
              } while (clientCur.moveToNext());
          }
          return a;
      }
  */
    public boolean isSubQuestionForOption(String mStrMainQuestion, String mQuestionOption, String mStrAuditId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + tb_sub_questions + " WHERE " + main_question + " = " + mStrMainQuestion + " AND " + question_for + " = " + mQuestionOption + " AND " + audit_id + " = " + mStrAuditId;
        Cursor clientCur = db.rawQuery(selectQuery, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        System.out.println("<><><><>selectQuerysub" + selectQuery);
        System.out.println("<><><><>selectexist" + exist);
        return exist;
    }

/*
    public ArrayList<SubLocationExplation> get_all_tb_sub_location_explation_list(String mLayerId, String mSubLocation) {
        ArrayList<SubLocationExplation> mAuditList = new ArrayList<SubLocationExplation>();
        String selectQuery = "SELECT * FROM " + tb_sub_location_explation_list + " WHERE " + layer_id + " = '" + mLayerId + "' AND " + sub_location_id + " = " + mSubLocation;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SubLocationExplation subLocationExplation = new SubLocationExplation();
                subLocationExplation.setmStrId(c.getString((c.getColumnIndex(id))));
                subLocationExplation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                subLocationExplation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                subLocationExplation.setmStrMainLocationId(c.getString((c.getColumnIndex(main_location_id))));
                subLocationExplation.setmStrSubLocationId(c.getString((c.getColumnIndex(sub_location_id))));
                subLocationExplation.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                subLocationExplation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                subLocationExplation.setmStrLayerId(c.getString((c.getColumnIndex(layer_id))));
                subLocationExplation.setmStrLayerTitle(c.getString((c.getColumnIndex(layer_title))));
                subLocationExplation.setmStrLayerDesc(c.getString((c.getColumnIndex(layer_desc))));
                subLocationExplation.setmStrExplanationTitle(c.getString((c.getColumnIndex(explanation_title))));
                subLocationExplation.setmStrExplanationImage(c.getString((c.getColumnIndex(explation_list_img))));
                subLocationExplation.setmStrExplanationDesc(c.getString((c.getColumnIndex(explanation_desc))));
                subLocationExplation.setmStrExplanationStatus(c.getString((c.getColumnIndex(explanation_status))));
                subLocationExplation.setmStrExplanationWorkPercentage(c.getString((c.getColumnIndex(explanation_work_done_percentage))));
                subLocationExplation.setmStrExplanationArchive(c.getString((c.getColumnIndex(explanation_archive))));
                mAuditList.add(subLocationExplation);
            } while (c.moveToNext());
        }
        return mAuditList;

    }*/

   /* public void update_archive_tb_sub_location_explation_list(SubLocationExplation subLocationExplation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(explanation_archive, subLocationExplation.getmStrExplanationArchive());
        db.update(tb_sub_location_explation_list, values, "id=" + subLocationExplation.getmStrId(), null);
    }*/


 /*   public int get_existing_sub_location_count(String mAuditId, String mLayerId, String mSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = 0;
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_sub_location + " WHERE " + layer_id + " = '" + mLayerId + "' AND " + sub_location_id + " = " + mSubLocation + " AND " + audit_id + " = " + mAuditId, null);
        if (clientCur.moveToFirst()) {
            do {
                a = Integer.parseInt(clientCur.getString((clientCur.getColumnIndex(sub_location_count))));
            } while (clientCur.moveToNext());
        }
        return a;
    }*/

  /*  //Delete tb_sub_location_explation_list
    public void delete_tb_sub_location_explation_list(String mLayerId, String mSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_sub_location_explation_list, layer_id + " = " + mLayerId + " AND " + sub_location_id + " = " + mSubLocation, null);
    }*/


    //Delete tb_selected_sub_location
    /*public void delete_tb_selected_sub_location(String mLayerId, String mSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_selected_sub_location, layer_id + " = " + mLayerId + " AND " + sub_location_id + " = " + mSubLocation, null);
    }*/


  /*  //Insert tb_audit_main_location
    public int insert_tb_audit_main_location(AuditMainLocation auditMainLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, auditMainLocation.getmStrUserId());
        values.put(audit_id, auditMainLocation.getmStrAuditId());
        values.put(audit_location_title, auditMainLocation.getmStrLocationTitle());
        values.put(audit_location_desc, auditMainLocation.getmStrLocationDesc());
        values.put(audit_location_server_id, auditMainLocation.getmStrLocationServerId());
        db.insert(tb_audit_main_location, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        return ID;
    }*/


    //Insert tb_sub_folder_explation_list
   /* public void insert_tb_sub_folder_explation_list(LayerList layerList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, layerList.getmStrUserId());
        values.put(audit_id, layerList.getmStrAuditId());
        values.put(audit_layer_desc, layerList.getmStrLayerDesc());
        values.put(audit_layer_title, layerList.getmStrLayerTitle());
        values.put(audit_sub_folder_title, layerList.getmStrSubFolderTitle());
        values.put(audit_sub_folder_id, layerList.getmStrSubFolderId());
        values.put(audit_main_location_title, layerList.getmStrMainLocationTitle());
        values.put(audit_main_location_id, layerList.getmStrMainLocationId());
        values.put(audit_main_location_server_id, layerList.getmStrMainLocationServerId());
        values.put(audit_layer_img, layerList.getmStrLayerImg());
        values.put(audit_layer_archive, layerList.getmStrLayerArchive());
        db.insert(tb_sub_folder_explation_list, null, values);
    }*/


   /* public ArrayList<LayerList> get_all_tb_sub_folder_explation_list(String mId) {
        ArrayList<LayerList> mAuditList = new ArrayList<LayerList>();
        String selectQuery = "SELECT  * FROM  " + tb_sub_folder_explation_list + " WHERE " + audit_sub_folder_id + " = '" + mId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                LayerList layerList = new LayerList();
                layerList.setmStrId(c.getString((c.getColumnIndex(id))));
                layerList.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                layerList.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                layerList.setmStrLayerTitle(c.getString((c.getColumnIndex(audit_layer_title))));
                layerList.setmStrLayerDesc(c.getString((c.getColumnIndex(audit_layer_desc))));
                layerList.setmStrSubFolderTitle(c.getString((c.getColumnIndex(audit_sub_folder_title))));
                layerList.setmStrSubFolderId(c.getString((c.getColumnIndex(audit_sub_folder_id))));
                layerList.setmStrMainLocationTitle(c.getString((c.getColumnIndex(audit_main_location_title))));
                layerList.setmStrMainLocationId(c.getString((c.getColumnIndex(audit_main_location_id))));
                layerList.setmStrLayerImg(c.getString((c.getColumnIndex(audit_layer_img))));
                layerList.setmStrLayerArchive(c.getString((c.getColumnIndex(audit_layer_archive))));
                layerList.setmStrMainLocationServerId(c.getString((c.getColumnIndex(audit_main_location_server_id))));
                mAuditList.add(layerList);
            } while (c.moveToNext());
        }
        return mAuditList;

    }*/


   /* public ArrayList<LayerList> get_all_tb_sub_folder_explation_list_by_id(String mId) {
        ArrayList<LayerList> mAuditList = new ArrayList<LayerList>();
        String selectQuery = "SELECT  * FROM  " + tb_sub_folder_explation_list + " WHERE " + id + " = '" + mId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                LayerList layerList = new LayerList();
                layerList.setmStrId(c.getString((c.getColumnIndex(id))));
                layerList.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                layerList.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                layerList.setmStrLayerTitle(c.getString((c.getColumnIndex(audit_layer_title))));
                layerList.setmStrLayerDesc(c.getString((c.getColumnIndex(audit_layer_desc))));
                layerList.setmStrSubFolderTitle(c.getString((c.getColumnIndex(audit_sub_folder_title))));
                layerList.setmStrSubFolderId(c.getString((c.getColumnIndex(audit_sub_folder_id))));
                layerList.setmStrMainLocationTitle(c.getString((c.getColumnIndex(audit_main_location_title))));
                layerList.setmStrMainLocationId(c.getString((c.getColumnIndex(audit_main_location_id))));
                layerList.setmStrLayerImg(c.getString((c.getColumnIndex(audit_layer_img))));
                layerList.setmStrLayerArchive(c.getString((c.getColumnIndex(audit_layer_archive))));
                layerList.setmStrMainLocationServerId(c.getString((c.getColumnIndex(audit_main_location_server_id))));
                mAuditList.add(layerList);
            } while (c.moveToNext());
        }
        return mAuditList;

    }*/


    /*public void update_tb_sub_folder_explation_list(LayerList layerList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_layer_title, layerList.getmStrLayerTitle());
        values.put(audit_layer_desc, layerList.getmStrLayerDesc());
        db.update(tb_sub_folder_explation_list, values, "id=" + layerList.getmStrId(), null);
    }*/

   /* public void update_tb_selected_sub_location_layer_detail(String mStrAuditId,String mStrUserId,String mStrLayerId,String mStrLayerTitle,String mStrLayerDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(layer_title,mStrLayerTitle);
        values.put(layer_desc, mStrLayerDesc);
        db.update(tb_selected_sub_location, values, layer_id+ " = " + mStrLayerId + " AND " + user_id + " = " + mStrUserId + " AND " + audit_id + " = " + mStrAuditId, null);
    }*/

    /*public void update_tb_sub_location_explation_list_layer_detail(String mStrAuditId,String mStrUserId,String mStrLayerId,String mStrLayerTitle,String mStrLayerDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(layer_title,mStrLayerTitle);
        values.put(layer_desc, mStrLayerDesc);
        db.update(tb_sub_location_explation_list, values, layer_id+ " = " + mStrLayerId + " AND " + user_id + " = " + mStrUserId + " AND " + audit_id + " = " + mStrAuditId, null);
    }*/

   /* public void update_tb_sub_folder_explation_list_add_img(LayerList layerList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_layer_img, layerList.getmStrLayerImg());
        db.update(tb_sub_folder_explation_list, values, "id=" + layerList.getmStrId(), null);
    }*/

   /* public void update_archive_tb_sub_folder_explation_list_add_img(LayerList layerList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_layer_archive, layerList.getmStrLayerArchive());
        db.update(tb_sub_folder_explation_list, values, "id=" + layerList.getmStrId(), null);
    }*/

    /*public int get_data_count_tb_list_audit_status(String status, String mUserId) {
        String selectQuery = "SELECT * FROM " + tb_list_audit + " WHERE " + audit_work_status + " = '" + status + "' AND " + user_id + " = " + mUserId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }*/

   /* public void update_tb_location_sub_folder_count(String mFolder, String op) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_location_sub_folder + " WHERE " + id + " = '" + mFolder + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        String mStr = "0";
        if (c.moveToFirst()) {
            do {
                mStr = c.getString((c.getColumnIndex(audit_sub_folder_count)));
            } while (c.moveToNext());
        }
        ContentValues values = new ContentValues();
        if (op.equals("add")) {
            values.put(audit_sub_folder_count, Integer.parseInt(mStr) + 1 + "");
        } else {
            values.put(audit_sub_folder_count, Integer.parseInt(mStr) - 1 + "");
        }

        db.update(tb_location_sub_folder, values, "id=" + mFolder, null);
    }*/


   /* public void update_tb_selected_main_location_count(String mFolder, String op) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_selected_main_location + " WHERE " + audit_main_location_id + " = '" + mFolder + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        String mStr = "0";
        if (c.moveToFirst()) {
            do {
                mStr = c.getString((c.getColumnIndex(audit_main_location_count)));
            } while (c.moveToNext());
        }
        ContentValues values = new ContentValues();
        if (op.equals("add")) {
            values.put(audit_main_location_count, Integer.parseInt(mStr) + 1 + "");
        } else {
            values.put(audit_main_location_count, Integer.parseInt(mStr) - 1 + "");
        }

        db.update(tb_selected_main_location, values, audit_main_location_id + "=" + mFolder, null);
    }*/

//###################################################################


   /* //Delete tb_sub_folder_explation_list
    public void delete_tb_sub_folder_explation_list(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_sub_folder_explation_list, audit_sub_folder_id + "=" + id, null);
    }


    //Delete tb_sub_folder_explation_list
    public void delete_tb_sub_folder_single_row(String mId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_sub_folder_explation_list, id + "=" + mId, null);
    }*/

    //Delete tb_selected_main_location
   /* public void delete_tb_selected_main_location(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_selected_main_location, audit_main_location_id + "=" + id, null);
    }
*/
    //Delete tb_location_sub_folder
   /* public void delete_tb_location_sub_folder(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_location_sub_folder, audit_main_location_id + "=" + id, null);
    }*/

    //Delete tb_sub_folder_explation_list
 /*   public void delete_tb_sub_folder_explation_list_all(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_sub_folder_explation_list, audit_main_location_id + "=" + id, null);
    }*/


    //Insert tb_audit_sub_location
   /* public int insert_tb_audit_sub_location(AuditSubLocation auditSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, auditSubLocation.getmStrUserId());
        values.put(audit_id, auditSubLocation.getmStrAuditId());
        values.put(audit_main_location_id, auditSubLocation.getmStrMainLocationId());
        values.put(audit_sub_location_server_id, auditSubLocation.getmStrSubLocationServerId());
        values.put(audit_sub_location_title, auditSubLocation.getmStrSubLocationTitle());
        values.put(audit_sub_location_desc, auditSubLocation.getmStrSubLocationDesc());
        db.insert(tb_audit_sub_location, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        return ID;
    }*/


    //Insert tb_audit_question
    public int insert_tb_audit_question(AuditQuestion auditQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, auditQuestion.getmStrUserId());
        values.put(audit_id, auditQuestion.getmStrAuditId());
        values.put(server_id, auditQuestion.getmStrServerId());
        values.put(main_location, auditQuestion.getmStrMainLocation());
        values.put(sub_location, auditQuestion.getmStrSubLocation());
        values.put(question_type, auditQuestion.getmStrQuestionType());
        values.put(question_txt, auditQuestion.getmStrQuestionTxt());
        values.put(answer_option, auditQuestion.getmStrAnswerOption());
        values.put(answer_option_id, auditQuestion.getmStrAnswerOptionId());
        values.put(sub_question, auditQuestion.getmStrSubQuestion());
        values.put(inspector_question, auditQuestion.getmStrInspectorQuestion());
        values.put(question_status, auditQuestion.getmStrQuestionStatus());
        db.insert(tb_main_question, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public int getCountOfAllSubLocationQUestion(String mAuditId, String mUserId, String mMainLocation, String mSubLocation, String mQuestionStatus) {
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = " + mQuestionStatus;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int getCountOfAllSubLocationInspectorQuestion(String mAuditId, String mUserId, String mMainLocation, String mSubLocation) {
        String selectQuery = "SELECT  * FROM  " + tb_inspector_questions + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_id + " = " + mSubLocation;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //get all data tb_audit_question
    public ArrayList<AuditQuestion> get_all_tb_audit_question(String mAuditId, String mUserId, String mMainLocation, String mSubLocation, String mQuestionStatus) {
        ArrayList<AuditQuestion> mAuditList = new ArrayList<AuditQuestion>();
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = " + mQuestionStatus;
        System.out.println("<><><>## "+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestion auditQuestion = new AuditQuestion();
                auditQuestion.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestion.setmStrServerId(c.getString((c.getColumnIndex(server_id))));
                auditQuestion.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestion.setmStrQuestionTxt(c.getString((c.getColumnIndex(question_txt))));
                auditQuestion.setmStrAnswerOption(c.getString((c.getColumnIndex(answer_option))));
                auditQuestion.setmStrAnswerOptionId(c.getString((c.getColumnIndex(answer_option_id))));
                auditQuestion.setmStrSubQuestion(c.getString((c.getColumnIndex(sub_question))));
                auditQuestion.setmStrInspectorQuestion(c.getString((c.getColumnIndex(inspector_question))));
                mAuditList.add(auditQuestion);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


    public int getAllAuditQuestionCount(String mAuditId, String mUserId, String mMainLocation, String mSubLocation) {
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }


    public int getAllAuditQuestionCountbySub(String mAuditId, String mUserId, String mSubLocation) {
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = 1";
        System.out.println("<><><>$$$ "+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAllQuestion(String mAuditId, String mUserId, String mMainLocation, String mSubLocation, String mQuestionStatus) {
        int count = 0;
        Cursor c = null;
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery(selectQuery, null);
        count = c.getCount();
        c.close();
        return count;
    }


    public int getAllGivenQuestion(String mAuditId, String mUserId, String mMainLocation, String mSubLocation, String mQuestionStatus,String mSubFolderLayerId) {
        String selectQuery = "SELECT  * FROM  " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + main_location_server_id + " = " + mMainLocation + " AND " + sub_location_server_id + " = " + mSubLocation + " AND " + sub_folder_layer_id + " = " + mSubFolderLayerId + " AND " + is_normal + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getCountAllGivenAns(String mAuditId, String mUserId, String mSubLocation, String mStrSubLocationExplanationId, String mStrMainLocation, String mIsNormal) {
        String selectQuery = "SELECT  * FROM  " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_server_id + " = " + mSubLocation + " AND " + sub_location_explanation_id + " = " + mStrSubLocationExplanationId + " AND " + main_location_server_id + " = " + mStrMainLocation + " AND " + is_normal + " = " + mIsNormal;
        System.out.println("<><><>query " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int getCountAllGivenInspectorAns(String mAuditId, String mUserId, String mSubLocation, String mStrSubLocationExplanationId, String mStrMainLocation, String mIsNormal) {
    String selectQuery = "SELECT  * FROM  " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + sub_location_explanation_id + " = " + mStrSubLocationExplanationId + " AND " + main_location + " = " + mStrMainLocation;
    System.out.println("<><><>query " + selectQuery);
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    int count = cursor.getCount();
    cursor.close();
    return count;
    }


    public int getCountAllGivenInspectorAns2(String mAuditId, String mUserId, String mSubLocation, String mStrMainLocation, String mIsNormal) {
        String selectQuery = "SELECT  * FROM  " + tb_audit_inspector_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + main_location + " = " + mStrMainLocation;
        System.out.println("<><><>query " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int getCountAllGivenAnsByMainAndSubLocation(String mAuditId, String mUserId, String mSubLocation, String mainLocation) {
        String selectQuery = "SELECT  * FROM  " + tb_audit_question_answer + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_server_id + " = " + mSubLocation + " AND " + main_location_server_id + " = " + mainLocation;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteAllGivenAnsByMainAndSubLocation(String mAuditId, String mUserId, String mSubLocation, String mainLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_audit_question_answer, audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_server_id + " = " + mSubLocation + " AND " + main_location_server_id + " = " + mainLocation, null);
    }


    //get all data tb_audit_question
    public ArrayList<AuditQuestion> get_single_tb_audit_question(String mAuditId, String mUserId, String mMainLocation, String mSubLocation, String mQuestionStatus, String mQuestionId) {
        ArrayList<AuditQuestion> mAuditList = new ArrayList<AuditQuestion>();
        String selectQuery = "SELECT  * FROM  " + tb_main_question + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location + " = " + mSubLocation + " AND " + question_status + " = " + mQuestionStatus + " AND " + server_id + " = " + mQuestionId;
        System.out.println("<><>selectQuery" + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditQuestion auditQuestion = new AuditQuestion();
                auditQuestion.setmStrId(c.getString((c.getColumnIndex(id))));
                auditQuestion.setmStrServerId(c.getString((c.getColumnIndex(server_id))));
                auditQuestion.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditQuestion.setmStrQuestionTxt(c.getString((c.getColumnIndex(question_txt))));
                auditQuestion.setmStrAnswerOption(c.getString((c.getColumnIndex(answer_option))));
                auditQuestion.setmStrAnswerOptionId(c.getString((c.getColumnIndex(answer_option_id))));
                auditQuestion.setmStrSubQuestion(c.getString((c.getColumnIndex(sub_question))));
                auditQuestion.setmStrInspectorQuestion(c.getString((c.getColumnIndex(inspector_question))));
                mAuditList.add(auditQuestion);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


    //get all data tb_sub_question
    public ArrayList<AuditSubQuestion> get_all_tb_Sub_question(String mAuditId, String mUserId, String mMainQuestionId, String mQuestionFor, String mStrMainLocation, String mStrSubLocation) {
        ArrayList<AuditSubQuestion> mAuditList = new ArrayList<AuditSubQuestion>();
        String selectQuery = "SELECT  * FROM  " + tb_sub_questions + " WHERE " + audit_id + " = " + mAuditId + " AND " + main_question + " = " + mMainQuestionId + " AND " + user_id + " = " + mUserId + " AND " + question_for + " = " + mQuestionFor;
        System.out.println("<><><>qery " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditSubQuestion auditSubQuestion = new AuditSubQuestion();
                auditSubQuestion.setmStrId(c.getString((c.getColumnIndex(id))));
                auditSubQuestion.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditSubQuestion.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditSubQuestion.setmStrMainQuestion(c.getString((c.getColumnIndex(main_question))));
                auditSubQuestion.setmStrServerId(c.getString((c.getColumnIndex(server_id))));
                auditSubQuestion.setmStrQuestionType(c.getString((c.getColumnIndex(question_type))));
                auditSubQuestion.setmStrQuestionTxt(c.getString((c.getColumnIndex(question_txt))));
                auditSubQuestion.setmStrAnswerOption(c.getString((c.getColumnIndex(answer_option))));
                auditSubQuestion.setmStrAnswerOptionId(c.getString((c.getColumnIndex(answer_option_id))));
                auditSubQuestion.setmStrSubQuestion(c.getString((c.getColumnIndex(sub_question))));
                auditSubQuestion.setmStrQuestionFor(c.getString((c.getColumnIndex(question_for))));
                auditSubQuestion.setmStrMainLocation(c.getString((c.getColumnIndex(main_location))));
                auditSubQuestion.setmStrSubLocation(c.getString((c.getColumnIndex(sub_location))));
                mAuditList.add(auditSubQuestion);
            } while (c.moveToNext());
        }
        c.close();
        return mAuditList;
    }


    //Insert tb_audit_sub_questions
    public int insert_tb_audit_sub_questions(AuditSubQuestion auditSubQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, auditSubQuestion.getmStrUserId());
        values.put(audit_id, auditSubQuestion.getmStrAuditId());
        values.put(main_question, auditSubQuestion.getmStrMainQuestion());
        values.put(server_id, auditSubQuestion.getmStrServerId());
        values.put(question_type, auditSubQuestion.getmStrQuestionType());
        values.put(question_txt, auditSubQuestion.getmStrQuestionTxt());
        values.put(answer_option, auditSubQuestion.getmStrAnswerOption());
        values.put(answer_option_id, auditSubQuestion.getmStrAnswerOptionId());
        values.put(sub_question, auditSubQuestion.getmStrSubQuestion());
        values.put(question_for, auditSubQuestion.getmStrQuestionFor());
        values.put(main_location, auditSubQuestion.getmStrMainLocation());
        values.put(sub_location, auditSubQuestion.getmStrSubLocation());
        db.insert(tb_sub_questions, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }


    //Insert tb_location_sub_folder
   /* public int insert_tb_location_sub_folder(MainLocationSubFolder mainLocationSubFolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, mainLocationSubFolder.getmStrUserId());
        values.put(audit_id, mainLocationSubFolder.getmStrAuditId());
        values.put(audit_main_location_id, mainLocationSubFolder.getmStrMainLocationId());
        values.put(audit_sub_folder_name, mainLocationSubFolder.getmStrSubFolderName());
        values.put(audit_sub_folder_count, mainLocationSubFolder.getmStrSubFolderCont());
        values.put(audit_main_location_server_id, mainLocationSubFolder.getmStrMainLocationServerId());
        db.insert(tb_location_sub_folder, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        return ID;
    }*/


    //Insert tb_chat_user_list
    public int insert_tb_chat_user_list(Message messageChat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, messageChat.getmStrUserId());
        values.put(chat_from_id, messageChat.getmUserMsgFrom());
        values.put(chat_to_id, messageChat.getmUserMsgTo());
        values.put(chat_user_id, messageChat.getmChatUserId());
        values.put(chat_user_name, messageChat.getmUserName());
        values.put(chat_user_photo, messageChat.getmUserPhoto());
        values.put(chat_user_msg, messageChat.getmUserLastMsg());
        values.put(chat_user_time, messageChat.getmUserMsgDate());
        values.put(chat_user_role, messageChat.getmUserRole());
        values.put(chat_user_email, messageChat.getmUserEmail());
        values.put(chat_user_phone, messageChat.getmUserPhone());
        db.insert(tb_chat_user_list, null, values);
        Cursor cur = db.rawQuery("select last_insert_rowid()", null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;

    }


    //Insert tb_chat_msg_list
    public void insert_tb_chat_msg_list(MessageChat messageChat) {
        System.out.println("<><><dbinsert");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user_id, messageChat.getmUserId());
        values.put(chat_user_id, messageChat.getmChatUserId());
        values.put(chat_room_id, messageChat.getmChatRoomId());
        values.put(chat_from_id, messageChat.getmUserMsgFrom());
        values.put(chat_msg, messageChat.getMessage());
        values.put(chat_msg_time, messageChat.getmCreatedAt());
        values.put(chat_msg_type, messageChat.getType());
        db.insert(tb_chat_msg_list, null, values);

    }


    //Update tb_location_sub_folder
   /* public void update_tb_location_sub_folder(MainLocationSubFolder mainLocationSubFolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_sub_folder_name, mainLocationSubFolder.getmStrSubFolderName());
        values.put(audit_sub_folder_count, mainLocationSubFolder.getmStrSubFolderCont());
        db.update(tb_location_sub_folder, values, "id=" + mainLocationSubFolder.getmStrId(), null);
    }*/


    public void update_question_answer_table_detail(String mAuditId, String mUserId, String mStrId, String mStrOpration, String mStrTitle, String mStrDesc) {
        if (mStrOpration.equals("1")) {//update sub folder title
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(sub_folder_title, mStrTitle);
            db.update(tb_audit_question_answer, values, audit_id + "=" + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_folder_id + " = " + mStrId, null);
        } else if (mStrOpration.equals("2")) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(sub_folder_layer_title, mStrTitle);
            values.put(sub_folder_layer_desc, mStrDesc);
            db.update(tb_audit_question_answer, values, audit_id + "=" + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_folder_layer_id + " = " + mStrId, null);

        } else if (mStrOpration.equals("3")) {
            System.out.println("<><><>oper-3mStrTitle" + mStrTitle);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(sub_location_explanation_title, mStrTitle);
            values.put(sub_location_explanation_desc, mStrDesc);
            db.update(tb_audit_question_answer, values, audit_id + "=" + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_explanation_id + " = " + mStrId, null);
        }
    }

    public void update_question_answer_table_image(String mAuditId, String mUserId, String mStrId, String mStrOpration, String mStrImage) {
        if (mStrOpration.equals("1")) {//update sub folder title
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(sub_folder_layer_image, mStrImage);
            db.update(tb_audit_question_answer, values, audit_id + "=" + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_folder_layer_id + " = " + mStrId, null);
        } else if (mStrOpration.equals("2")) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(sub_location_explanation_image, mStrImage);
            db.update(tb_audit_question_answer, values, audit_id + "=" + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + sub_location_explanation_id + " = " + mStrId, null);
        }
    }


 /*   //Insert tb_selected_sub_location
    public void insert_tb_selected_sub_location(SelectedSubLocation selectedSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_id, selectedSubLocation.getmStrAuditId());
        values.put(user_id, selectedSubLocation.getmStrUserId());
        values.put(layer_id, selectedSubLocation.getmStrLayerId());
        values.put(layer_title, selectedSubLocation.getmStrLayerTitle());
        values.put(layer_desc, selectedSubLocation.getmStrLayerDesc());
        values.put(main_location_id, selectedSubLocation.getmStrMainLocationId());
        values.put(sub_location_id, selectedSubLocation.getmStrSubLocationId());
        values.put(sub_location_title, selectedSubLocation.getmStrSubLocationTitle());
        values.put(sub_location_desc, selectedSubLocation.getmStrSubLocationDesc());
        values.put(sub_location_server_id, selectedSubLocation.getmStrSubLocationServerId());
        values.put(sub_location_count, selectedSubLocation.getmStrSubLocationCount());
        values.put(work_status, selectedSubLocation.getmStrWorkStatus());
        db.insert(tb_selected_sub_location, null, values);
    }*/


    //Insert tb_selected_main_location
//    public void insert_tb_selected_main_location(SelectedLocation selectedLocation) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(user_id, selectedLocation.getmStrUserId());
//        values.put(audit_id, selectedLocation.getmStrAuditId());
//        values.put(audit_main_location_title, selectedLocation.getmStrMainLocationTitle());
//        values.put(audit_main_location_count, selectedLocation.getmStrMainLocationCount());
//        values.put(audit_main_location_server_id, selectedLocation.getmStrMainLocationServerId());
//        values.put(audit_main_location_id, selectedLocation.getmStrMainLocationLocalId());
//        values.put(audit_main_location_decs, selectedLocation.getmStrMainLocationDesc());
//        db.insert(tb_selected_main_location, null, values);
//    }

    /*//Check data tb_selected_main_location
    public int get_data_count_tb_selected_main_location(String mId) {
        String selectQuery = "SELECT  * FROM  " + tb_audit_main_location + " WHERE " + audit_id + " = '" + mId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }
*/

   /* public void update_tb_selected_main_location(SelectedLocation selectedLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(audit_main_location_count, selectedLocation.getmStrMainLocationCount());
        db.update(tb_selected_main_location, values, audit_main_location_id + "=" + selectedLocation.getmStrMainLocationLocalId(), null);
    }*/


    //get all data tb_audit_sub_location
 /*   public ArrayList<AuditSubLocation> get_all_tb_audit_sub_location(String mId, String mAuditId) {
        ArrayList<AuditSubLocation> mAuditList = new ArrayList<AuditSubLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_audit_sub_location + " WHERE " + audit_main_location_id + " = '" + mId + "' AND " + audit_id + " = '" + mAuditId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditSubLocation auditSubLocation = new AuditSubLocation();
                auditSubLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                auditSubLocation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(audit_sub_location_title))));
                auditSubLocation.setmStrSubLocationDesc(c.getString((c.getColumnIndex(audit_sub_location_desc))));
                auditSubLocation.setmStrMainLocationId(c.getString((c.getColumnIndex(audit_main_location_id))));
                auditSubLocation.setmStrSubLocationServerId(c.getString((c.getColumnIndex(audit_sub_location_server_id))));
                auditSubLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditSubLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                mAuditList.add(auditSubLocation);
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/


   /* public ArrayList<SelectedLocation> get_all_tb_selected_main_location(String mId) {
        ArrayList<SelectedLocation> mAuditList = new ArrayList<SelectedLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_selected_main_location + " WHERE " + audit_id + " = '" + mId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SelectedLocation selectedLocation = new SelectedLocation();
                selectedLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                selectedLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                selectedLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                selectedLocation.setmStrMainLocationTitle(c.getString((c.getColumnIndex(audit_main_location_title))));
                selectedLocation.setmStrMainLocationCount(c.getString((c.getColumnIndex(audit_main_location_count))));
                selectedLocation.setmStrMainLocationServerId(c.getString((c.getColumnIndex(audit_main_location_server_id))));
                selectedLocation.setmStrMainLocationLocalId(c.getString((c.getColumnIndex(audit_main_location_id))));
                selectedLocation.setmStrMainLocationDesc(c.getString((c.getColumnIndex(audit_main_location_decs))));
                mAuditList.add(selectedLocation);
            } while (c.moveToNext());
        }
        return mAuditList;

    }*/


 /*   public ArrayList<MainLocationSubFolder> get_all_tb_location_sub_folder(String mStrId) {
        ArrayList<MainLocationSubFolder> mAuditList = new ArrayList<MainLocationSubFolder>();
        String selectQuery = "SELECT * FROM " + tb_location_sub_folder + " WHERE " + audit_main_location_id + " = '" + mStrId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ///
        if (c.moveToFirst()) {
            do {
                MainLocationSubFolder mainLocationSubFolder = new MainLocationSubFolder();
                mainLocationSubFolder.setmStrId(c.getString((c.getColumnIndex(id))));
                mainLocationSubFolder.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                mainLocationSubFolder.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                mainLocationSubFolder.setmStrMainLocationId(c.getString((c.getColumnIndex(audit_main_location_id))));
                mainLocationSubFolder.setmStrSubFolderName(c.getString((c.getColumnIndex(audit_sub_folder_name))));
                mainLocationSubFolder.setmStrSubFolderCont(c.getString((c.getColumnIndex(audit_sub_folder_count))));
                mAuditList.add(mainLocationSubFolder);
            } while (c.moveToNext());
        }

        return mAuditList;
    }*/


  /*  public void update_tb_selected_sub_location(SelectedSubLocation selectedSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sub_location_count, selectedSubLocation.getmStrSubLocationCount());
        db.update(tb_selected_sub_location, values, sub_location_id + "=" + selectedSubLocation.getmStrSubLocationId(), null);
    }*/


  /*  public boolean isExistSubLocation(String mAuditId, String mLayerId, String mSubLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_sub_location + " WHERE " + sub_location_id + " = " + mSubLocation + " AND " + layer_id + " = " + mLayerId + " AND " + audit_id + " = " + mAuditId, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }*/


 /*   public boolean isExistNotification(String mId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_main_location + " WHERE " + audit_main_location_id + " = '" + mId + "'", null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }*/

    /*public boolean isCheckMainLocationData(String mAuditId, String mUserId, String mLocationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_sub_folder_explation_list + " WHERE " + audit_id + " = " + mAuditId + " AND " + user_id + " = " + mUserId + " AND " + audit_main_location_id + " = " + mLocationId, null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }*/


   /* public int get_existing_location_count_for_location(String mLocationId, String mAuditId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM  " + tb_selected_main_location + " where " + audit_main_location_id + " = " + mLocationId + " AND " + audit_id + " = " + mAuditId;
        int a = 0;
        Cursor clientCur = db.rawQuery(selectQuery, null);
        if (clientCur.moveToFirst()) {
            do {
                a = Integer.parseInt(clientCur.getString((clientCur.getColumnIndex(audit_main_location_count))));
            } while (clientCur.moveToNext());
        }
        return a;
    }*/


   /* public int get_existing_location_count(String mId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = 0;
        Cursor clientCur = db.rawQuery("SELECT * FROM " + tb_selected_main_location + " WHERE " + audit_main_location_id + " = '" + mId + "'", null);
        if (clientCur.moveToFirst()) {
            do {
                a = Integer.parseInt(clientCur.getString((clientCur.getColumnIndex(audit_main_location_count))));
            } while (clientCur.moveToNext());
        }
        return a;
    }*/


    //get all tb_audit_main_location
  /*  public ArrayList<AuditMainLocation> get_all_tb_audit_main_location(String mId) {
        ArrayList<AuditMainLocation> mAuditList = new ArrayList<AuditMainLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_audit_main_location + " where " + audit_id + " = '" + mId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AuditMainLocation auditMainLocation = new AuditMainLocation();
                auditMainLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                auditMainLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                auditMainLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                auditMainLocation.setmStrLocationTitle(c.getString((c.getColumnIndex(audit_location_title))));
                auditMainLocation.setmStrLocationDesc(c.getString((c.getColumnIndex(audit_location_desc))));
                auditMainLocation.setmStrLocationServerId(c.getString((c.getColumnIndex(audit_location_server_id))));
                mAuditList.add(auditMainLocation);
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/


 /*   public ArrayList<SelectedSubLocation> get_all_tb_selected_sub_location(String mAuditId) {
        ArrayList<SelectedSubLocation> mAuditList = new ArrayList<SelectedSubLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_selected_sub_location + " where " + layer_id + " = '" + mAuditId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
                selectedSubLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                selectedSubLocation.setmStrLayerId(c.getString((c.getColumnIndex(layer_id))));
                selectedSubLocation.setmStrLayerTitle(c.getString((c.getColumnIndex(layer_title))));
                selectedSubLocation.setmStrLayerDesc(c.getString((c.getColumnIndex(layer_desc))));
                selectedSubLocation.setmStrMainLocationId(c.getString((c.getColumnIndex(main_location_id))));
                selectedSubLocation.setmStrSubLocationId(c.getString((c.getColumnIndex(sub_location_id))));
                selectedSubLocation.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                selectedSubLocation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                selectedSubLocation.setmStrSubLocationDesc(c.getString((c.getColumnIndex(sub_location_desc))));
                selectedSubLocation.setmStrSubLocationCount(c.getString((c.getColumnIndex(sub_location_count))));
                selectedSubLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                selectedSubLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                selectedSubLocation.setmStrWorkStatus(c.getString((c.getColumnIndex(work_status))));
                mAuditList.add(selectedSubLocation);
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/

   /* public ArrayList<SelectedSubLocation> get_all_count_status_tb_selected_sub_location(String mAuditId) {
        ArrayList<SelectedSubLocation> mAuditList = new ArrayList<SelectedSubLocation>();
        String selectQuery = "SELECT  * FROM  " + tb_selected_sub_location + " where " + layer_id + " = '" + mAuditId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
                selectedSubLocation.setmStrId(c.getString((c.getColumnIndex(id))));
                selectedSubLocation.setmStrLayerId(c.getString((c.getColumnIndex(layer_id))));
                selectedSubLocation.setmStrLayerTitle(c.getString((c.getColumnIndex(layer_title))));
                selectedSubLocation.setmStrMainLocationId(c.getString((c.getColumnIndex(main_location_id))));
                selectedSubLocation.setmStrSubLocationId(c.getString((c.getColumnIndex(sub_location_id))));
                selectedSubLocation.setmStrSubLocationServerId(c.getString((c.getColumnIndex(sub_location_server_id))));
                selectedSubLocation.setmStrSubLocationTitle(c.getString((c.getColumnIndex(sub_location_title))));
                selectedSubLocation.setmStrSubLocationDesc(c.getString((c.getColumnIndex(sub_location_desc))));
                selectedSubLocation.setmStrSubLocationCount(c.getString((c.getColumnIndex(sub_location_count))));
                selectedSubLocation.setmStrAuditId(c.getString((c.getColumnIndex(audit_id))));
                selectedSubLocation.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                selectedSubLocation.setmStrWorkStatus(c.getString((c.getColumnIndex(work_status))));
                mAuditList.add(selectedSubLocation);
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/


    //get all tb_list_audit
 /*   public ArrayList<Audit> get_all_tb_list_audit(String mUserId, String mStatus) {
        ArrayList<Audit> mAuditList = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM  " + tb_list_audit + " where " + user_id + " = " + mUserId + " AND " + audit_work_status + " = " + mStatus;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Audit audit = new Audit();
                audit.setmAuditId(c.getString((c.getColumnIndex(audit_id))));
                audit.setmUserId(c.getString((c.getColumnIndex(user_id))));
                audit.setmAssignBy(c.getString((c.getColumnIndex(audit_assign_by))));
                audit.setmDueDate(c.getString((c.getColumnIndex(audit_due_date))));
                audit.setmTitle(c.getString((c.getColumnIndex(audit_title))));
                audit.setmStatus(c.getString((c.getColumnIndex(audit_work_status))));
                audit.setmStrCountryId(c.getString((c.getColumnIndex(country_id))));
                audit.setmStrLangId(c.getString((c.getColumnIndex(lang_id))));
                mAuditList.add(audit);
            } while (c.moveToNext());
        }
        return mAuditList;
    }
*/
    //get all tb_list_audit by audit id
   /* public ArrayList<Audit> get_all_tb_list_audit_audit_id(String mAuditId) {
        ArrayList<Audit> mAuditList = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM  " + tb_list_audit + " where " + audit_id + " = " + mAuditId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Audit audit = new Audit();
                audit.setmAuditId(c.getString((c.getColumnIndex(audit_id))));
                audit.setmAssignBy(c.getString((c.getColumnIndex(audit_assign_by))));
                audit.setmDueDate(c.getString((c.getColumnIndex(audit_due_date))));
                audit.setmTitle(c.getString((c.getColumnIndex(audit_title))));
                audit.setmStatus(c.getString((c.getColumnIndex(audit_work_status))));
                audit.setmStrCountryId(c.getString((c.getColumnIndex(country_id))));
                audit.setmStrLangId(c.getString((c.getColumnIndex(lang_id))));
                mAuditList.add(audit);
            } while (c.moveToNext());
        }
        return mAuditList;
    }*/


   /* public void update_tb_list_audit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(country_id,audit.getmStrCountryId());
        values.put(lang_id,audit.getmStrLangId());
        db.update(tb_list_audit, values, audit_id + "=" + audit.getmAuditId(), null);
    }*/


    //get all tb_chat_user_list
    public ArrayList<Message> get_all_tb_chat_user_list(String mUserId) {
        ArrayList<Message> mMsgList = new ArrayList<Message>();
        String selectQuery = "SELECT  * FROM  " + tb_chat_user_list + " where " + user_id + " = '" + mUserId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Message messageChat = new Message();
                messageChat.setmStrUserId(c.getString((c.getColumnIndex(user_id))));
                messageChat.setmChatUserId(c.getString((c.getColumnIndex(chat_user_id))));
                messageChat.setmUserName(c.getString((c.getColumnIndex(chat_user_name))));
                messageChat.setmUserMsgFrom(c.getString((c.getColumnIndex(chat_from_id))));
                messageChat.setmUserMsgTo(c.getString((c.getColumnIndex(chat_to_id))));
                messageChat.setmUserLastMsg(c.getString((c.getColumnIndex(chat_user_msg))));
                messageChat.setmUserMsgDate(c.getString((c.getColumnIndex(chat_user_time))));
                messageChat.setmUserRole(c.getString((c.getColumnIndex(chat_user_role))));
                messageChat.setmUserEmail(c.getString((c.getColumnIndex(chat_user_email))));
                messageChat.setmUserPhone(c.getString((c.getColumnIndex(chat_user_phone))));
                messageChat.setmUserPhoto(c.getString((c.getColumnIndex(chat_user_photo))));
                mMsgList.add(messageChat);
            } while (c.moveToNext());
        }
        c.close();
        return mMsgList;
    }


    //get all tb_chat_msg_list
    public ArrayList<MessageChat> get_all_tb_chat_msg_list(String mUserId) {
        ArrayList<MessageChat> mMsgList = new ArrayList<MessageChat>();
        String selectQuery = "SELECT  * FROM  " + tb_chat_msg_list + " where " + chat_user_id + " = '" + mUserId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                MessageChat messageChat = new MessageChat();
                messageChat.setmUserId(c.getString((c.getColumnIndex(user_id))));
                messageChat.setmChatUserId(c.getString((c.getColumnIndex(chat_user_id))));
                messageChat.setmUserMsgFrom(c.getString((c.getColumnIndex(chat_from_id))));
                messageChat.setmMessage(c.getString((c.getColumnIndex(chat_msg))));
                messageChat.setmCreatedAt(c.getString((c.getColumnIndex(chat_msg_time))));
                messageChat.setmType(Integer.parseInt(c.getString((c.getColumnIndex(chat_msg_type)))));
                messageChat.setmChatRoomId(Integer.parseInt(c.getString((c.getColumnIndex(chat_room_id)))));
                mMsgList.add(messageChat);
            } while (c.moveToNext());
        }
        c.close();
        return mMsgList;
    }


    //delete_tb_chat_msg_list
    public void delete_tb_chat_msg_list() {
        System.out.println("<><><dbdelete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_chat_msg_list, null, null);

    }  //delete_tb_chat_msg_list

    public void delete_tb_chat_user_list() {
        System.out.println("<><><mChatIddelete");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_chat_user_list, null, null);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}