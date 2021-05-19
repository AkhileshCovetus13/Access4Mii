package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import ABS_ADAPTER.AgentList;
import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_GET_SET.Agent;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.showPDialog;
import static ABS_HELPER.CommonUtils.showWarningPopupSingleButton;


public class ActivityAgentList extends Activity {
    ArrayList<Agent> mListItems = new ArrayList<>();
    ArrayList<Agent> mListSearchItems = new ArrayList<>();
    AgentList agentList;
    int textlength = 0;


    @BindView(R.id.mListChat)
    ListView mListChat;
    @BindView(R.id.mImgBack)
    ImageView mImgBack;
    @BindView(R.id.mEditAgentSearch)
    EditTextRegular mEditAgentSearch;


    /*click for going back*/
    @OnClick(R.id.mImgBack)
    void mClose() {
        closeKeyBoard(ActivityAgentList.this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_list);
        ButterKnife.bind(this);
        /* Internet Connectivity Check */
        if (mCheckSignalStrength(ActivityAgentList.this) == 2) {
            showPDialog(ActivityAgentList.this);
            mToGetAgentList();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityAgentList.this);
        }


        /* Search Box */
        mEditAgentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                textlength = mEditAgentSearch.getText().length();
                mListSearchItems.clear();
                if (textlength == 0) {
//                    isLoadMore = true;
                    mListSearchItems.addAll(mListItems);
                } else {
//                    isLoadMore = false;
                    for (int i = 0; i < mListItems.size(); i++) {
                        if (textlength <= mListItems.get(i).getmAgentName().length()) {
                            if (mListItems.get(i).getmAgentName().toLowerCase().trim().contains(
                                    mEditAgentSearch.getText().toString().toLowerCase().trim())) {
                                mListSearchItems.add(mListItems.get(i));
                            }
                        }
                    }
                }
                agentList = new AgentList(ActivityAgentList.this, mListSearchItems);
                mListChat.setAdapter(agentList);
                agentList.notifyDataSetChanged();
            }
        });

        mListChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeKeyBoard(ActivityAgentList.this);
                Intent intent = new Intent(ActivityAgentList.this, ActivityAuditorToAgentChat.class);
                Agent agent = mListSearchItems.get(position);
                intent.putExtra("mChatUserId", agent.getmAgentId());
                intent.putExtra("mChatUserName", agent.getmAgentName());
                intent.putExtra("mChatUserRole", agent.getmRole());
                intent.putExtra("mChatUserPhoto", agent.getmAgentphoto());
                intent.putExtra("mChatUserEmail", agent.getmAgentEmail());
                intent.putExtra("mChatUserPhone", agent.getmAgentPhone());
                startActivity(intent);
            }
        });

    }

    /*api call for getting agent list*/
    void mToGetAgentList() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getAgentByAuditorId",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        mListItems.clear();
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONArray jsonArray = response.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Agent agent = new Agent();
                                    JSONObject mAgentObj = jsonArray.getJSONObject(i);
                                    agent.setmAgentId(mAgentObj.getString("user_id"));
                                    agent.setmAgentName(mAgentObj.getString("username"));
                                    agent.setmAgentphoto(mAgentObj.getString("photo"));
                                    agent.setmRole(mAgentObj.getString("role"));
                                    agent.setmAgentEmail(mAgentObj.getString("email"));
                                    agent.setmAgentPhone(mAgentObj.getString("phone"));
                                    mListItems.add(agent);
                                    mListSearchItems.add(agent);
                                }
                                agentList = new AgentList(ActivityAgentList.this, mListItems);
                                mListChat.setAdapter(agentList);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityAgentList.this);
                            } else {
                              /*  CommonUtils.mShowAlert(response.getString("message"), ActivityAgentList.this);*/
                                showWarningPopupSingleButton(ActivityAgentList.this, getString(R.string.mTextFile_alert_noAgentToChat), "Alert !");
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
                        Toast.makeText(ActivityAgentList.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityAgentList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityAgentList.this));
                System.out.println("<><><>params" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }
}
