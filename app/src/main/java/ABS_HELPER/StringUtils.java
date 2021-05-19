package ABS_HELPER;

/**
 * Created by admin1 on 22/11/18.
 */

public class StringUtils {


    //CommonKeys For DataBase
    public static String database_name = "big_data_audit";

    //CommonKeys For All Table
    public static String id = "id";
    public static String user_id = "user_id";
    public static String audit_id = "audit_id";


    //Keys For tb_list_audit
    public static String tb_list_audit = "tb_list_audit";
    public static String country_id = "country_id";
    public static String lang_id = "lang_id";
    public static String audit_assign_by = "audit_assign_by";
    public static String audit_title = "audit_title";
    public static String audit_due_date = "audit_due_date";
    public static String audit_work_status = "audit_work_status";


    //Keys For tb_audit_main_location
    public static String tb_audit_main_location = "tb_audit_main_location";
    public static String audit_location_title = "location_title";
    public static String audit_location_id = "location_id";
    public static String audit_location_desc = "location_desc";
    public static String audit_location_server_id = "location_server_id";

    //Keys For tb_audit_sub_location
    public static String tb_audit_sub_location = "tb_audit_sub_location";
    public static String audit_main_location_id = "main_location_id";
    public static String audit_sub_location_server_id = "sub_location_server_id";
    public static String audit_sub_location_title = "sub_location_title";
    public static String audit_sub_location_desc = "sub_location_desc";

    //Keys For tb_audit_question

    public static String audit_question_server_id = "question_server_id";
    public static String audit_question_type = "question_type";
    public static String audit_sub_location_id = "sub_location_id";

    //Common Keys For tb_audit_question And tb_audit_sub_questions
    public static String audit_question = "question";
    public static String audit_answer = "answer";
    public static String audit_answer_id = "answer_id";
    public static String audit_answer_type = "answer_type";

    //Keys For tb_audit_sub_questions
    public static String tb_audit_sub_questions = "tb_audit_sub_questions";
    public static String audit_main_question_id = "main_question_id";
    public static String audit_main_question_server_id = "main_question_server_id";
    public static String audit_sub_question_server_id = "question_id";
    public static String audit_sub_question_condition = "question_condition";

    //Keys For tb_location_sub_folder
    public static String audit_sub_folder_count = "sub_folder_count";
    public static String audit_sub_folder_name = "sub_folder_name";
    public static String tb_location_sub_folder = "tb_location_sub_folder";


    //Keys For tb_selected_main_location
    public static String tb_selected_main_location = "tb_selected_main_location";
    public static String audit_main_location_title = "main_location_title";
    public static String audit_main_location_count = "main_location_count";
    public static String audit_main_location_server_id = "main_location_server_id";
    public static String audit_main_location_decs = "main_location_decs";


    //Keys For tb_sub_folder_explation_list
    public static String tb_sub_folder_explation_list = "tb_sub_folder_explation_list";
    public static String audit_sub_folder_id = "sub_folder_id";
    public static String audit_sub_folder_title = "sub_folder_title";
    public static String audit_layer_title = "layer_title";
    public static String audit_layer_desc = "layer_desc";
    public static String audit_layer_img = "layer_img";
    public static String audit_layer_archive = "audit_layer_archive";


    //Keys For tb_chat_list
    public static String tb_chat_user_list = "tb_chat_user_list";
    public static String chat_from_id = "from_id";
    public static String chat_to_id = "to_id";
    public static String chat_user_id = "chat_user_id";
    public static String chat_user_name = "user_name";
    public static String chat_user_photo = "user_photo";
    public static String chat_user_msg = "user_msg";
    public static String chat_user_time = "user_time";
    public static String chat_user_role = "user_role";
    public static String chat_user_email = "user_email";
    public static String chat_user_phone = "user_phone";


    //Keys For tb_chat_msg_list
    public static String tb_chat_msg_list = "tb_chat_msg_list";
    public static String chat_room_id = "chat_room_id";
    public static String chat_msg = "msg";
    public static String chat_msg_time = "msg_time";
    public static String chat_msg_type = "msg_type";


    // Key For tb_selected_sub_location

    public static String layer_id = "layer_id";
    public static String layer_title = "layer_title";
    public static String layer_desc = "layer_desc";
    public static String layer_archive = "layer_archive";
    public static String main_location_id = "main_location_id";
    public static String sub_location_id = "sub_location_id";
    public static String sub_location_title = "sub_location_title";
    public static String sub_location_desc = "sub_location_desc";
    public static String sub_location_server_id = "sub_location_server_id";
    public static String sub_location_count = "sub_location_count";
    public static String work_status = "work_status";
    public static String tb_selected_sub_location = "tb_selected_sub_location";

    // Key For tb_sub_location_explation_list
    public static String tb_sub_location_explation_list = "tb_sub_location_explation_list";
    public static String explation_list_img = "explation_list_img";
    public static String explanation_title = "explanation_title";
    public static String explanation_desc = "explanation_desc";
    public static String explanation_status = "explanation_status";
    public static String explanation_archive = "explanation_archive";
    public static String explanation_work_done_percentage = "explanation_work_done_percentage";


    public static String tb_image_sub_location_explation_list = "tb_image_sub_location_explation_list";
    public static String explation_list_id = "explation_list_id";
    public static String explation_list_image = "explation_list_image";
    public static String img_default = "img_default";
    public static String is_normal = "is_normal";
    public static String tb_audit_inspector_question_answer = "tb_audit_inspector_question_answer";




    //Keys For tb_audit_question
    public static String tb_main_question = "tb_main_question";
    public static String server_id = "server_id";
    public static String main_location = "main_location";
    public static String sub_location = "sub_location";
    public static String question_type = "question_type";
    public static String question_txt = "question_txt";
    public static String answer_option = "answer_option";
    public static String answer_option_id = "answer_option_id";
    public static String sub_question = "sub_question";
    public static String question_status = "question_status";


    //Keys For tb_sub_questions
    public static String tb_sub_questions = "tb_sub_questions";
    public static String main_question = "main_question";
    public static String question_for = "question_for";


    //Keys For tb_audit_question_answer
    public static String tb_audit_question_answer = "tb_audit_question_answer";
    public static String question_id = "question_id";
    public static String question_server_id = "question_server_id";
    public static String answer_id = "answer_id";
    public static String answer_value = "answer_value";
    public static String question_image = "question_image";
    public static String question_priority = "question_priority";
    public static String question_extra_text = "question_extra_text";
    public static String is_question_parent = "is_question_parent";
    public static String sub_location_explanation_id = "sub_location_explanation_id";
    public static String sub_location_explanation_title = "sub_location_explanation_title";
    public static String sub_location_explanation_desc = "sub_location_explanation_desc";
    public static String sub_location_explanation_image = "sub_location_explanation_image";
    public static String sub_folder_layer_id = "sub_folder_layer_id";
    public static String sub_folder_layer_title = "sub_folder_layer_title";
    public static String sub_folder_layer_desc = "sub_folder_layer_desc";
    public static String sub_folder_layer_image = "sub_folder_layer_image";
    public static String sub_folder_id = "sub_folder_id";
    public static String sub_folder_title = "sub_folder_title";
    public static String main_location_server_id = "main_location_server_id";
    public static String inspector_question = "inspector_question";
    public static String status = "status";
    public static String tb_audit_sub_question_answer = "tb_audit_sub_question_answer";
    public static String tb_audit_final_question_answer = "tb_audit_final_question_answer";
    public static String is_inspector = "is_inspector";



    public static String tb_inspector_questions = "tb_inspector_questions";


    public static String ct_tb_inspector_questions = "CREATE TABLE "
            + tb_inspector_questions + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + main_question + " TEXT NOT NULL, "
            + main_location_id + " TEXT NOT NULL, "
            + sub_location_id + " TEXT NOT NULL, "
            + question_server_id + " TEXT NOT NULL, "
            + question_type + " TEXT NOT NULL, "
            + question_txt + " TEXT NOT NULL, "
            + answer_option + " TEXT NOT NULL, "
            + answer_option_id + " TEXT NOT NULL )";










    public static String ct_tb_audit_list = "CREATE TABLE "
            + tb_list_audit + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + audit_assign_by + " TEXT NOT NULL, "
            + audit_title + " TEXT NOT NULL, "
            + audit_due_date + " TEXT NOT NULL, "
            + country_id + " TEXT, "
            + lang_id + " TEXT, "
            + audit_work_status + " TEXT NOT NULL )";



    public static String ct_tb_audit_main_location = "CREATE TABLE "
            + tb_audit_main_location + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + audit_location_title + " TEXT NOT NULL, "
            + audit_location_desc + " TEXT NOT NULL, "
            + audit_location_server_id + " TEXT NOT NULL )";

    public static String ct_tb_audit_sub_location = "CREATE TABLE "
            + tb_audit_sub_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + audit_main_location_id + " TEXT NOT NULL, "
            + audit_sub_location_server_id + " TEXT NOT NULL, "
            + audit_sub_location_title + " TEXT NOT NULL, "
            + audit_sub_location_desc + " TEXT NOT NULL )";


    public static String ct_tb_main_question = "CREATE TABLE "
            + tb_main_question + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + server_id + " TEXT NOT NULL, "
            + main_location + " TEXT NOT NULL, "
            + sub_location + " TEXT NOT NULL, "
            + question_type + " TEXT NOT NULL, "
            + question_txt + " TEXT NOT NULL, "
            + answer_option + " TEXT NOT NULL, "
            + answer_option_id + " TEXT NOT NULL, "
            + sub_question + " TEXT NOT NULL, "
            + inspector_question + " TEXT NOT NULL, "
            + question_status + " TEXT NOT NULL )";




    public static String ct_tb_sub_questions = "CREATE TABLE "
            + tb_sub_questions + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + main_question + " TEXT NOT NULL, "
            + main_location + " TEXT NOT NULL, "
            + sub_location + " TEXT NOT NULL, "
            + server_id + " TEXT NOT NULL, "
            + question_type + " TEXT NOT NULL, "
            + question_txt + " TEXT NOT NULL, "
            + answer_option + " TEXT NOT NULL, "
            + answer_option_id + " TEXT NOT NULL, "
            + sub_question + " TEXT NOT NULL, "
            + question_for + " TEXT NOT NULL )";


    public static String ct_tb_location_sub_folder = "CREATE TABLE "
            + tb_location_sub_folder + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_main_location_id + " TEXT NOT NULL, "
            + audit_sub_folder_name + " TEXT NOT NULL, "
            + audit_sub_folder_count + " TEXT NOT NULL, "
            + audit_main_location_server_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL )";


    public static String ct_tb_selected_main_location = "CREATE TABLE "
            + tb_selected_main_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_main_location_id + " TEXT NOT NULL, "
            + audit_main_location_title + " TEXT NOT NULL, "
            + audit_main_location_server_id + " TEXT NOT NULL, "
            + audit_main_location_count + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, "
            + audit_main_location_decs + " TEXT NOT NULL )";


    public static String ct_tb_sub_folder_explation_list = "CREATE TABLE "
            + tb_sub_folder_explation_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
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
            + user_id + " TEXT NOT NULL )";


    public static String ct_tb_chat_user_list = "CREATE TABLE "
            + tb_chat_user_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + chat_from_id + " TEXT NOT NULL, "
            + chat_to_id + " TEXT NOT NULL, "
            + chat_user_id + " TEXT NOT NULL, "
            + chat_user_name + " TEXT NOT NULL, "
            + chat_user_photo + " TEXT NOT NULL, "
            + chat_user_msg + " TEXT NOT NULL, "
            + chat_user_time + " TEXT NOT NULL, "
            + chat_user_email + " TEXT NOT NULL, "
            + chat_user_phone + " TEXT NOT NULL, "
            + chat_user_role + " TEXT NOT NULL )";


    public static String ct_tb_chat_msg_list = "CREATE TABLE "
            + tb_chat_msg_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + user_id + " TEXT NOT NULL, "
            + chat_room_id + " TEXT NOT NULL, "
            + chat_user_id + " TEXT NOT NULL, "
            + chat_from_id + " TEXT NOT NULL, "
            + chat_msg + " TEXT NOT NULL, "
            + chat_msg_time + " TEXT NOT NULL, "
            + chat_msg_type + " TEXT NOT NULL )";


    public static String ct_tb_selected_sub_location = "CREATE TABLE "
            + tb_selected_sub_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + layer_id + " TEXT NOT NULL, "
            + layer_title + " TEXT NOT NULL, "
            + layer_desc + " TEXT NOT NULL, "
            + main_location_id + " TEXT NOT NULL, "
            + sub_location_id + " TEXT NOT NULL, "
            + sub_location_title + " TEXT NOT NULL, "
            + sub_location_desc + " TEXT NOT NULL, "
            + sub_location_server_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + work_status + " TEXT NOT NULL, "
            + sub_location_count + " TEXT NOT NULL )";



    public static String ct_tb_sub_location_explation_list = "CREATE TABLE "
            + tb_sub_location_explation_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + layer_id + " TEXT NOT NULL, "
            + layer_title + " TEXT NOT NULL, "
            + layer_desc + " TEXT NOT NULL, "
            + sub_location_title + " TEXT NOT NULL, "
            + sub_location_id + " TEXT NOT NULL, "
            + sub_location_server_id + " TEXT NOT NULL, "
            + explanation_title + " TEXT NOT NULL, "
            + explanation_desc + " TEXT NOT NULL, "
            + explanation_status + " TEXT NOT NULL, "
            + explanation_work_done_percentage + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + explation_list_img + " TEXT , "
            + explanation_archive + " TEXT NOT NULL, "
            + main_location_id + " TEXT NOT NULL )";



       public static String ct_tb_image_sub_location_explation_list = "CREATE TABLE "
            + tb_image_sub_location_explation_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + explation_list_id + " TEXT NOT NULL, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + layer_id + " TEXT NOT NULL, "
            + img_default + " TEXT NOT NULL, "
            + explation_list_image + " TEXT )";


       public static String ct_tb_audit_question_answer = "CREATE TABLE "
            + tb_audit_question_answer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + country_id + " TEXT NOT NULL, "
            + lang_id + " TEXT NOT NULL, "
            + question_id + " TEXT NOT NULL, "
            + question_server_id + " TEXT NOT NULL, "
            + answer_id + " TEXT, "
            + answer_value + " TEXT, "
            + question_type + " TEXT NOT NULL, "
            + question_image + " TEXT, "
            + question_priority + " TEXT NOT NULL, "
            + question_extra_text + " TEXT, "
            + is_question_parent + " TEXT NOT NULL, "
            + sub_location_explanation_id + " TEXT NOT NULL, "
            + sub_location_explanation_title + " TEXT NOT NULL, "
            + sub_location_explanation_desc + " TEXT NOT NULL, "
            + sub_location_explanation_image + " TEXT, "
            + sub_location_server_id + " TEXT NOT NULL, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + sub_folder_layer_title + " TEXT NOT NULL, "
            + sub_folder_layer_desc + " TEXT NOT NULL, "
            + sub_folder_layer_image + " TEXT, "
            + sub_folder_id + " TEXT NOT NULL, "
            + sub_folder_title + " TEXT NOT NULL, "
            + main_location_server_id + " TEXT NOT NULL, "
            + is_normal + " TEXT NOT NULL, "
            + status + " TEXT NOT NULL )";


    public static String ct_tb_audit_final_question_answer = "CREATE TABLE "
            + tb_audit_final_question_answer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + country_id + " TEXT NOT NULL, "
            + lang_id + " TEXT NOT NULL, "
            + question_id + " TEXT NOT NULL, "
            + question_server_id + " TEXT NOT NULL, "
            + answer_id + " TEXT, "
            + answer_value + " TEXT, "
            + question_type + " TEXT NOT NULL, "
            + question_image + " TEXT, "
            + question_priority + " TEXT NOT NULL, "
            + question_extra_text + " TEXT, "
            + is_question_parent + " TEXT NOT NULL, "
            + sub_location_explanation_id + " TEXT NOT NULL, "
            + sub_location_explanation_title + " TEXT NOT NULL, "
            + sub_location_explanation_desc + " TEXT NOT NULL, "
            + sub_location_explanation_image + " TEXT, "
            + sub_location_server_id + " TEXT NOT NULL, "
            + sub_folder_layer_id + " TEXT NOT NULL, "
            + sub_folder_layer_title + " TEXT NOT NULL, "
            + sub_folder_layer_desc + " TEXT NOT NULL, "
            + sub_folder_layer_image + " TEXT, "
            + sub_folder_id + " TEXT NOT NULL, "
            + sub_folder_title + " TEXT NOT NULL, "
            + main_location_server_id + " TEXT NOT NULL, "
            + is_inspector + " TEXT NOT NULL, "
            + is_normal + " TEXT NOT NULL, "
            + status + " TEXT NOT NULL )";



    public static String ct_tb_audit_sub_question_answer = "CREATE TABLE "
            + tb_audit_sub_question_answer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + question_id + " TEXT NOT NULL, "
            + question_server_id + " TEXT NOT NULL, "
            + answer_id + " TEXT, "
            + answer_value + " TEXT, "
            + question_type + " TEXT NOT NULL, "
            + question_image + " TEXT, "
            + question_priority + " TEXT NOT NULL, "
            + question_extra_text + " TEXT, "
            + is_question_parent + " TEXT NOT NULL, "
            + main_question + " TEXT NOT NULL, "
            + sub_location_explanation_id + " TEXT NOT NULL, "
            + is_normal + " TEXT NOT NULL, "
            + question_for + " TEXT NOT NULL )";



    public static String ct_tb_audit_inspector_question_answer = "CREATE TABLE "
            + tb_audit_inspector_question_answer + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + audit_id + " TEXT NOT NULL, "
            + user_id + " TEXT NOT NULL, "
            + question_id + " TEXT NOT NULL, "
            + answer_id + " TEXT, "
            + answer_value + " TEXT, "
            + question_type + " TEXT NOT NULL, "
            + question_image + " TEXT, "
            + question_priority + " TEXT NOT NULL, "
            + main_question + " TEXT NOT NULL, "
            + sub_location_explanation_id + " TEXT NOT NULL, "
            + main_location + " TEXT NOT NULL, "
            + sub_location + " TEXT NOT NULL )";









    //CREATE TABLE Queries
    //public static String ct_tb_audit_list = "CREATE TABLE " + tb_list_audit + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + audit_assign_by + " TEXT NOT NULL, " + audit_title + " TEXT NOT NULL, " + audit_due_date + " TEXT NOT NULL, " + audit_work_status + " TEXT NOT NULL )";
    //public static String ct_tb_audit_main_location = "CREATE TABLE " + tb_audit_main_location + " (" + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + audit_id + " TEXT NOT NULL, " + user_id + " TEXT NOT NULL, " + audit_location_title + " TEXT NOT NULL, " + audit_location_desc + " TEXT NOT NULL, " + audit_location_server_id + " TEXT NOT NULL )";
    //public static String ct_tb_audit_sub_location = "CREATE TABLE " + tb_audit_sub_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + audit_main_location_id + " TEXT NOT NULL, " + audit_sub_location_server_id + " TEXT NOT NULL, " + audit_sub_location_title + " TEXT NOT NULL, " + audit_sub_location_desc + " TEXT NOT NULL )";
    //public static String ct_tb_audit_question = "CREATE TABLE " + tb_audit_question + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + audit_question_server_id + " TEXT NOT NULL, " + audit_question + " TEXT NOT NULL, " + audit_answer + " TEXT NOT NULL, " + audit_main_location_id + " TEXT NOT NULL, " + audit_answer_id + " TEXT NOT NULL, " + audit_sub_location_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + user_id + " TEXT NOT NULL, " + audit_answer_type + " TEXT NOT NULL, " + audit_question_type + " TEXT NOT NULL )";
    //public static String ct_tb_audit_sub_questions = "CREATE TABLE " + tb_audit_sub_questions + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + audit_main_question_id + " TEXT NOT NULL, " + audit_main_question_server_id + " TEXT NOT NULL, " + audit_sub_question_server_id + " TEXT NOT NULL, " + audit_question + " TEXT NOT NULL, 'answer' TEXT NOT NULL, " + audit_answer_id + " TEXT NOT NULL, " + audit_answer_type + " TEXT NOT NULL, " + audit_sub_question_condition + " TEXT NOT NULL )";
    //public static String ct_tb_location_sub_folder = "CREATE TABLE " + tb_location_sub_folder + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + audit_main_location_id + " TEXT NOT NULL, " + audit_sub_folder_name + " TEXT NOT NULL, " + audit_sub_folder_count + " TEXT NOT NULL, " + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL )";
    //public static String ct_tb_selected_main_location = "CREATE TABLE " + tb_selected_main_location + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + audit_main_location_id + " TEXT NOT NULL, " + audit_main_location_title + " TEXT NOT NULL, " + audit_main_location_server_id + " TEXT NOT NULL, " + audit_main_location_count + " TEXT NOT NULL, " + user_id + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + audit_main_location_decs + " TEXT NOT NULL )";
    //public static String ct_tb_sub_folder_explation_list = "CREATE TABLE " + tb_sub_folder_explation_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + audit_main_location_id + " TEXT NOT NULL, " + audit_main_location_title + " TEXT NOT NULL, " + audit_sub_folder_id + " TEXT NOT NULL, " + audit_sub_folder_title + " TEXT NOT NULL, " + audit_layer_title + " TEXT NOT NULL, " + audit_layer_desc + " TEXT NOT NULL, " + audit_id + " TEXT NOT NULL, " + user_id + " TEXT NOT NULL )";
    //public static String ct_tb_chat_user_list = "CREATE TABLE " + tb_chat_user_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + user_id + " TEXT NOT NULL, " + chat_from_id + " TEXT NOT NULL, " + chat_to_id + " TEXT NOT NULL, " + chat_user_id + " TEXT NOT NULL, " + chat_user_name + " TEXT NOT NULL, " + chat_user_photo + " TEXT NOT NULL, " + chat_user_msg + " TEXT NOT NULL, " + chat_user_time + " TEXT NOT NULL, " + chat_user_email + " TEXT NOT NULL, " + chat_user_phone + " TEXT NOT NULL, " + chat_user_role + " TEXT NOT NULL )";
    //public static String ct_tb_chat_msg_list = "CREATE TABLE " + tb_chat_msg_list + " ( " + id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + user_id + " TEXT NOT NULL, " + chat_room_id + " TEXT NOT NULL, " + chat_user_id + " TEXT NOT NULL, " + chat_from_id + " TEXT NOT NULL, " + chat_msg + " TEXT NOT NULL, " + chat_msg_time + " TEXT NOT NULL, " + chat_msg_type + " TEXT NOT NULL )";
    //public static String ct_tb_selected_sub_location = "CREATE TABLE "+tb_selected_sub_location+" ( "+id+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+layer_id+" TEXT NOT NULL, "+layer_title+" TEXT NOT NULL, "+main_location_id+" TEXT NOT NULL, "+sub_location_id+" TEXT NOT NULL, "+sub_location_title+" TEXT NOT NULL, "+sub_location_desc+" TEXT NOT NULL, "+sub_location_count+" TEXT NOT NULL )";

}
