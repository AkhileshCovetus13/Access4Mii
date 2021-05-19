package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.ChatList;
import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_GET_SET.Message;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.showPDialog;
import static ABS_HELPER.CommonUtils.showWarningPopupSingleButton;


public class FragmentChatList extends Fragment {

    public static ArrayList<Message> mListItems = new ArrayList<>();
    public static ArrayList<Message> mListSearchItems = new ArrayList<>();
    public static ChatList chatList;
    public static Activity mActivity;
    int textlength = 0;
    public static DatabaseHelper db;
    public static int mChatId;


    public static EditTextRegular mEditChatSearch;

    public static ListView mListChat;
    @BindView(R.id.mImgAddNewChat)
    ImageView mImgAddNewChat;

    @OnClick(R.id.mImgAddNewChat)
    void mAddNewChat() {
        CommonUtils.OnClick(getActivity(), mImgAddNewChat);
         /* Internet Connectivity Check */
        if (mCheckSignalStrength(getActivity()) == 2) {
            Intent intent = new Intent(getActivity(), ActivityAgentList.class);
            startActivity(intent);
        } else {
            showWarningPopupSingleButton(getActivity(), getActivity().getString(R.string.mTextFile_error_no_internet_agent), getActivity().getString(R.string.mText_warning));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        mListSearchItems.clear();
        mListItems.clear();
        db = new DatabaseHelper(getActivity());
        mListChat = view.findViewById(R.id.mListChat);
        mEditChatSearch = view.findViewById(R.id.mEditChatSearch);

        if (mCheckSignalStrength(getActivity()) == 2) {
            showPDialog(getActivity());
            mToGetChatList();
            chatList = new ChatList(getActivity(), mListSearchItems);
            mListChat.setAdapter(chatList);
            mEditChatSearch.setText("");
        } else {
          /* database data into list */
            mListItems.clear();
            mListSearchItems.clear();
            mListSearchItems = db.get_all_tb_chat_user_list(PreferenceManager.getFormiiId(getActivity()));
            mListItems = db.get_all_tb_chat_user_list(PreferenceManager.getFormiiId(getActivity()));
            if (mListSearchItems.size() > 0) {
                chatList = new ChatList(getActivity(), mListSearchItems);
                mListChat.setAdapter(chatList);
            } else {
                CommonUtils.mShowAlert(mActivity.getString(R.string.mTextFile_error_no_record_found), getActivity());
            }
            mEditChatSearch.setText("");
            showWarningPopupSingleButton(getActivity(), getActivity().getString(R.string.mTextFile_error_no_internet), getActivity().getString(R.string.mText_warning));
        }


        mListChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeKeyBoard(getActivity());
                Message messageChat = mListSearchItems.get(position);
                String mRoleType = messageChat.getmUserRole();
                System.out.println("<><>mChatRoomId" + messageChat.getmStrChatRoomId());
                if (mRoleType.equals("Admin")) {
                    Intent intent = new Intent(getActivity(), ActivityAuditorToAdminChat.class);
                    intent.putExtra("mChatRoomId", messageChat.getmStrChatRoomId());
                    intent.putExtra("mChatUserId", messageChat.getmChatUserId());
                    intent.putExtra("mChatUserName", messageChat.getmUserName());
                    intent.putExtra("mChatUserRole", messageChat.getmUserRole());
                    intent.putExtra("mChatUserPhoto", messageChat.getmUserPhoto());
                    intent.putExtra("mChatUserEmail", messageChat.getmUserEmail());
                    intent.putExtra("mChatUserPhone", messageChat.getmUserPhone());
                    startActivity(intent);
                } else if (mRoleType.equals("Agent")) {
                    Intent intent = new Intent(getActivity(), ActivityAuditorToAgentChat.class);
                    intent.putExtra("mChatRoomId", messageChat.getmStrChatRoomId());
                    intent.putExtra("mChatUserId", messageChat.getmChatUserId());
                    intent.putExtra("mChatUserName", messageChat.getmUserName());
                    intent.putExtra("mChatUserRole", messageChat.getmUserRole());
                    intent.putExtra("mChatUserPhoto", messageChat.getmUserPhoto());
                    intent.putExtra("mChatUserEmail", messageChat.getmUserEmail());
                    intent.putExtra("mChatUserPhone", messageChat.getmUserPhone());
                    startActivity(intent);
                }
            }
        });

           /* Search Box */
        mEditChatSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                textlength = mEditChatSearch.getText().length();
                mListSearchItems.clear();
                if (textlength == 0) {
//                    isLoadMore = true;
                    mListSearchItems.addAll(mListItems);
                } else {
//                    isLoadMore = false;
                    for (int i = 0; i < mListItems.size(); i++) {
                        if (textlength <= mListItems.get(i).getmUserName().length()) {
                            if (mListItems.get(i).getmUserName().toLowerCase().trim().contains(
                                    mEditChatSearch.getText().toString().toLowerCase().trim())) {
                                mListSearchItems.add(mListItems.get(i));
                            }
                        }
                    }
                }
                chatList = new ChatList(getActivity(), mListSearchItems);
                mListChat.setAdapter(chatList);
                chatList.notifyDataSetChanged();

            }
        });
        return view;
    }

    /*api call for getting chat list*/
    public static void mToGetChatList() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getChatUsersList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            hidePDialog();
                            if (mStrStatus.equals("1")) {
                                mListItems.clear();
                                mListSearchItems.clear();

                                db.delete_tb_chat_user_list();
                                JSONArray jsonArray = response.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Message messageChat = new Message();
                                    JSONObject mAgentObj = jsonArray.getJSONObject(i);
                                    messageChat.setmStrUserId(PreferenceManager.getFormiiId(mActivity));
                                    messageChat.setmUserRole(mAgentObj.getString("role"));
                                    messageChat.setmChatUserId(mAgentObj.getString("user_id"));
                                    messageChat.setmUserName(mAgentObj.getString("username"));
                                    messageChat.setmUserPhoto(mAgentObj.getString("photo"));
                                    messageChat.setmUserLastMsg(mAgentObj.getString("msg"));
                                    messageChat.setmUserMsgDate(mAgentObj.getString("time"));
                                    messageChat.setmUserMsgFrom(mAgentObj.getString("from_id"));
                                    messageChat.setmUserMsgTo(mAgentObj.getString("to_id"));
                                    messageChat.setmUserEmail(mAgentObj.getString("email"));
                                    messageChat.setmUserPhone(mAgentObj.getString("phone"));
                                    mChatId = db.insert_tb_chat_user_list(messageChat);
                                    messageChat.setmStrChatRoomId(mChatId);
                                    mListItems.add(messageChat);
                                    mListSearchItems.add(messageChat);
                                    System.out.println("<><><mChatId" + mChatId);
                                }
                                chatList.notifyDataSetChanged();
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(mActivity);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), mActivity);
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
                        Toast.makeText(mActivity, mActivity.getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(mActivity));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(mActivity));
                System.out.println("<><><>params" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    public static void reload() {
        try {
            db = new DatabaseHelper(mActivity);
         /* Internet Connectivity Check */
            if (mCheckSignalStrength(mActivity) == 2) {
                showPDialog(mActivity);
                System.out.println("<><><onreload");
                mToGetChatList();
                if (mActivity != null) {
                    chatList = new ChatList(mActivity, mListSearchItems);
                    mListChat.setAdapter(chatList);
                }
                mEditChatSearch.setText("");
            } else {
             /* database data into list */
                mListItems.clear();
                mListSearchItems.clear();
                mListSearchItems = db.get_all_tb_chat_user_list(PreferenceManager.getFormiiId(mActivity));
                mListItems = db.get_all_tb_chat_user_list(PreferenceManager.getFormiiId(mActivity));
                if (mListSearchItems.size() > 0) {
                    chatList = new ChatList(mActivity, mListSearchItems);
                    mListChat.setAdapter(chatList);
                } else {
                    CommonUtils.mShowAlert(mActivity.getString(R.string.mTextFile_error_no_record_found), mActivity);
                }
                mEditChatSearch.setText("");
                showWarningPopupSingleButton(mActivity, mActivity.getString(R.string.mTextFile_error_no_internet), mActivity.getString(R.string.mText_warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        System.out.println("<><>onresume frag");
        super.onResume();
        mActivity = getActivity();
      //  mListSearchItems.clear();
    }


}