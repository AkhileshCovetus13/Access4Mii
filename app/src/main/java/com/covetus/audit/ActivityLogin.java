package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_HELPER.AppController;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ABS_HELPER.CommonUtils;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;

public class ActivityLogin extends Activity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int userStatus = 0;
    String mStrUserName, mStrPassword;
    @BindView(R.id.mLayoutSignIn)
    RelativeLayout mLayoutSignIn;
    @BindView(R.id.mLayoutMain)
    RelativeLayout mLayoutMain;
    @BindView(R.id.mEditPassword)
    EditTextRegular mEditPassword;
    @BindView(R.id.mEditUserName)
    EditTextRegular mEditUserName;
    @BindView(R.id.mTxtAuditorLogin)
    TextViewSemiBold mTxtAuditorLogin;
    @BindView(R.id.mTxtInspectorLogin)
    TextViewSemiBold mTxtInspectorLogin;
    @BindView(R.id.mTxtAuditorBy)
    TextViewSemiBold mTxtAuditorBy;
    @BindView(R.id.mViewAuditor)
    View mViewAuditor;
    @BindView(R.id.mViewInspector)
    View mViewInspector;
    @BindView(R.id.mTxtForgetPassword)
    TextViewSemiBold mTxtForgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        mEditPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CommonUtils.closeKeyBoard(ActivityLogin.this);
                    CommonUtils.OnClick(ActivityLogin.this, mLayoutSignIn);
                    mStrUserName = mEditUserName.getText().toString();
                    mStrPassword = mEditPassword.getText().toString();
                     /*login validation*/
                    if (mStrUserName.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_email), ActivityLogin.this);
                        return false;
                    } else if (!mStrUserName.matches(emailPattern)) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_email), ActivityLogin.this);
                        return false;
                    } else if (mStrPassword.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_password), ActivityLogin.this);
                        return false;
                    }


                    if (mCheckSignalStrength(ActivityLogin.this) == 2) {
                        CommonUtils.showPDialog(ActivityLogin.this);
                        mToDoLogin();
                    } else {
                        mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityLogin.this);
                    }

                    return true;
                }
                return false;
            }
        });


    }

    /*click to login*/
    @OnClick(R.id.mLayoutSignIn)
    public void mLayoutSignIn() {
        CommonUtils.closeKeyBoard(ActivityLogin.this);
        CommonUtils.OnClick(ActivityLogin.this, mLayoutSignIn);
        mStrUserName = mEditUserName.getText().toString();
        mStrPassword = mEditPassword.getText().toString();
        /*login validation*/
        if (mStrUserName.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_email), ActivityLogin.this);
            return;
        } else if (!mStrUserName.matches(emailPattern)) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_email), ActivityLogin.this);
            return;
        } else if (mStrPassword.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_password), ActivityLogin.this);
            return;
        }
        if (mCheckSignalStrength(ActivityLogin.this) == 2) {
            CommonUtils.showPDialog(ActivityLogin.this);
            mToDoLogin();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityLogin.this);
        }
    }


    /*click to change login type to auditor*/
    @OnClick(R.id.mTxtAuditorLogin)
    public void mTxtAuditorLogin() {
        mTxtAuditorBy.setText(R.string.mTextFile_full_audit_by);
        userStatus = 0;
        mTxtInspectorLogin.setTextColor(getResources().getColor(R.color.abs_textcolor_gray));
        mTxtAuditorLogin.setTextColor(getResources().getColor(R.color.black));
        mViewInspector.setBackgroundColor(getResources().getColor(R.color.white));
        mViewAuditor.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    /*click to change login type to inspector*/
    @OnClick(R.id.mTxtInspectorLogin)
    public void mTxtInspectorLogin() {
        mTxtAuditorBy.setText(R.string.mTextFile_mini_audit_by);
        userStatus = 1;
        mTxtInspectorLogin.setTextColor(getResources().getColor(R.color.black));
        mTxtAuditorLogin.setTextColor(getResources().getColor(R.color.abs_textcolor_gray));
        mViewInspector.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mViewAuditor.setBackgroundColor(getResources().getColor(R.color.white));
    }

    /*click to view forget password screen*/
    @OnClick(R.id.mTxtForgetPassword)
    public void mTxtGoForForget() {
        Intent intent = new Intent(ActivityLogin.this, ActivityForgetPassword.class);
        startActivity(intent);
    }


    /*api call to login*/
    void mToDoLogin() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "auth",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                String mStrId = jsonObject.getString("id");
                                String mStrFirstName = jsonObject.getString("firstname");
                                String mStrLastName = jsonObject.getString("lastname");
                                String mStrEmail = jsonObject.getString("email");
                                String mStrPhoto = jsonObject.getString("photo");
                                String mStrPhone = jsonObject.getString("phone");
                                String mStrAddressEN = jsonObject.getString("address");
                                String mStrAuthToken = jsonObject.getString("auth_token");
                                PreferenceManager.setFormiiId(ActivityLogin.this, mStrId);
                                PreferenceManager.setFormiiFirstName(ActivityLogin.this, mStrFirstName);
                                PreferenceManager.setFormiiLastName(ActivityLogin.this, mStrLastName);
                                PreferenceManager.setFormiiEmail(ActivityLogin.this, mStrEmail);
                                PreferenceManager.setFormiiProfileimg(ActivityLogin.this, mStrPhoto);
                                PreferenceManager.setFormiiContact(ActivityLogin.this, mStrPhone);
                                PreferenceManager.setFormiiAddress(ActivityLogin.this, mStrAddressEN);
                                PreferenceManager.setFormiiFullName(ActivityLogin.this, mStrFirstName + " " + mStrLastName);
                                PreferenceManager.setFormiiIsLogin(ActivityLogin.this, "1");
                                PreferenceManager.setFormiiAuthToken(ActivityLogin.this, mStrAuthToken);
                                PreferenceManager.setFormiiUserRole(ActivityLogin.this, userStatus + "");
                                PreferenceManager.setFormiiCheckNotificationChat(ActivityLogin.this, jsonObject.getString("msg_push"));
                                PreferenceManager.setFormiiCheckNotificationAudit(ActivityLogin.this, jsonObject.getString("audit_push"));
                                PreferenceManager.setFormiiCheckNotificationReport(ActivityLogin.this, jsonObject.getString("report_push"));
                                Intent intent = new Intent(ActivityLogin.this, ActivityTabHostMain.class);
                                intent.putExtra("mStrCurrentTab", "0");
                                startActivity(intent);
                                finish();
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityLogin.this);
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
                        System.out.println("<><><>error" + error.toString());
                        Toast.makeText(ActivityLogin.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mStrUserName);
                params.put("password", mStrPassword);
                params.put("platform", "android");
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                PreferenceManager.setFormiiDeviceId(ActivityLogin.this, refreshedToken);
                params.put("devicetoken", PreferenceManager.getFormiiDeviceId(ActivityLogin.this));
                if (userStatus == 0) {
                    params.put("role", getString(R.string.mTextFile_auditor));
                } else {
                    params.put("role", getString(R.string.mTextFile_inspector));
                }
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }
}
