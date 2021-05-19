package com.covetus.audit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.showPDialog;

/**
 * Created by admin18 on 30/11/18.
 */

public class ActivityChatWithAdmin extends Activity {

    String mStrFirstName, mStrDescription;
    @BindView(R.id.mEditFirstName)
    EditTextRegular mEditFirstName;
    @BindView(R.id.mEditDescription)
    EditTextRegular mEditDescription;
    @BindView(R.id.mLayoutSend)
    RelativeLayout mLayoutSend;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;


    /*click for going back*/
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        finish();
    }

    /*click to send query*/
    @OnClick(R.id.mLayoutSend)
    public void mLayoutSend() {
        CommonUtils.closeKeyBoard(ActivityChatWithAdmin.this);
        CommonUtils.OnClick(ActivityChatWithAdmin.this, mLayoutSend);
        mStrFirstName = mEditFirstName.getText().toString();
        mStrDescription = mEditDescription.getText().toString();

       /*contact us validation*/
        if (mStrFirstName.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_name), ActivityChatWithAdmin.this);
            return;
        } else if (mStrDescription.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_description), ActivityChatWithAdmin.this);
            return;
        }

        if (mCheckSignalStrength(ActivityChatWithAdmin.this)==2) {
            showPDialog(ActivityChatWithAdmin.this);
            mToDoSendRequest();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityChatWithAdmin.this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_admin);
        ButterKnife.bind(this);
    }

    /*api to send contact us form*/
    void mToDoSendRequest() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "request-to-admin-for-chat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {

                                CommonUtils.mShowAlert(response.getString("message"), ActivityChatWithAdmin.this);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 3000);

                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityChatWithAdmin.this);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityChatWithAdmin.this);
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
                        Toast.makeText(ActivityChatWithAdmin.this, R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityChatWithAdmin.this));
                params.put("msg", mStrDescription);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityChatWithAdmin.this));
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }
}
