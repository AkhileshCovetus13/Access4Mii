package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
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
import java.util.Locale;
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

/**
 * Created by admin18 on 14/11/18.
 */

public class ActivityContactUs extends Activity {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mStrFirstName, mStrEmailAddress, mStrDescription, mStrSubject;
    @BindView(R.id.mEditFirstName)
    EditTextRegular mEditFirstName;
    @BindView(R.id.mEditEmailAddress)
    EditTextRegular mEditEmailAddress;
    @BindView(R.id.mEditSubject)
    EditTextRegular mEditSubject;
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
        //setLocale("ar");
        CommonUtils.closeKeyBoard(ActivityContactUs.this);
        CommonUtils.OnClick(ActivityContactUs.this, mLayoutSend);
        mStrFirstName = mEditFirstName.getText().toString();
        mStrEmailAddress = mEditEmailAddress.getText().toString();
        mStrDescription = mEditDescription.getText().toString();
        mStrSubject = mEditSubject.getText().toString();
       /*contact us validation*/
        if (mStrFirstName.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_name), ActivityContactUs.this);
            return;
        } else if (mEditEmailAddress.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_email), ActivityContactUs.this);
            return;
        } else if (!mStrEmailAddress.matches(emailPattern)) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_email), ActivityContactUs.this);
            return;
        } else if (mStrSubject.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_subject), ActivityContactUs.this);
            return;
        } else if (mStrDescription.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_description), ActivityContactUs.this);
            return;
        }


        if (mCheckSignalStrength(ActivityContactUs.this) == 2) {
            CommonUtils.showPDialog(ActivityContactUs.this);
            mToDoSend();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityContactUs.this);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
    }

    /*api to send contact us form*/
    void mToDoSend() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "contactUs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                Intent intent = new Intent(ActivityContactUs.this, ActivityThankYou.class);
                                startActivity(intent);
                                finish();
                                //CommonUtils.mShowAlert(response.getString("message"), ActivityContactUs.this);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityContactUs.this);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityContactUs.this);
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
                        Toast.makeText(ActivityContactUs.this, R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(ActivityContactUs.this));
                params.put("fullname", mStrFirstName);
                params.put("email", mStrEmailAddress);
                params.put("description", mStrDescription);
                params.put("subject", mStrSubject);
                params.put("devicetoken", PreferenceManager.getFormiiDeviceId(ActivityContactUs.this));
                params.put("platform", "ANDROID");
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityContactUs.this));
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
}
