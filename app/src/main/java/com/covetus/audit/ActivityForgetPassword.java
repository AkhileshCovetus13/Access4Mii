package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;

public class ActivityForgetPassword extends Activity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @BindView(R.id.mEditEmailAddress)
    EditTextRegular mEditEmailAddress;
    @BindView(R.id.mLayoutSubmit)
    RelativeLayout mLayoutSubmit;
    @BindView(R.id.mImageBack)
    ImageView mImageBack;

    String mStrEmail;
    private static int SPLASH_TIME_OUT = 3000;

      /*click for going back*/
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        System.out.println("<><><calll");
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        mEditEmailAddress.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CommonUtils.closeKeyBoard(ActivityForgetPassword.this);
                    CommonUtils.OnClick(ActivityForgetPassword.this, mLayoutSubmit);
                    mStrEmail = mEditEmailAddress.getText().toString();
                    /*forget password validation*/
                    if (mStrEmail.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_email), ActivityForgetPassword.this);
                        return false;
                    } else if (!mStrEmail.matches(emailPattern)) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_email), ActivityForgetPassword.this);
                        return false;
                    }


                    if (mCheckSignalStrength(ActivityForgetPassword.this) == 2) {
                        CommonUtils.showPDialog(ActivityForgetPassword.this);
                        mToDoForget();
                    } else {
                        mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityForgetPassword.this);
                    }


                    return true;
                }
                return false;
            }
        });
    }


    /*click to submit passowrd*/
    @OnClick(R.id.mLayoutSubmit)
    public void mGoForSubmit() {
        CommonUtils.closeKeyBoard(ActivityForgetPassword.this);
        CommonUtils.OnClick(ActivityForgetPassword.this, mLayoutSubmit);
        mStrEmail = mEditEmailAddress.getText().toString();
    /*forget password validation*/
        if (mStrEmail.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_email), ActivityForgetPassword.this);
            return;
        } else if (!mStrEmail.matches(emailPattern)) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_email), ActivityForgetPassword.this);
            return;
        }


        if (mCheckSignalStrength(ActivityForgetPassword.this) == 2) {
            CommonUtils.showPDialog(ActivityForgetPassword.this);
            mToDoForget();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityForgetPassword.this);
        }


    }

    /*api call for forget password*/
    void mToDoForget() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "forgetPassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                mEditEmailAddress.setText("");
                                CommonUtils.mShowAlert(response.getString("message"), ActivityForgetPassword.this);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        Intent intent = new Intent(ActivityForgetPassword.this, ActivityLogin.class);
                                        startActivity(intent);
                                    }
                                }, SPLASH_TIME_OUT);


                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityForgetPassword.this);
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
                        Toast.makeText(ActivityForgetPassword.this, getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mStrEmail);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


}
