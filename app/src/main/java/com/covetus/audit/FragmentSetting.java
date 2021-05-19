package com.covetus.audit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mAboutUs;
import static ABS_HELPER.CommonUtils.mHelp;
import static ABS_HELPER.CommonUtils.mNews;
import static ABS_HELPER.CommonUtils.mStandard;
import static ABS_HELPER.CommonUtils.mTermNdCond;
import static ABS_HELPER.CommonUtils.showPDialog;


public class FragmentSetting extends Fragment {


    @BindView(R.id.mImgMainToggale)
    ImageView mImgMainToggale;
    @BindView(R.id.mImgMessage)
    ImageView mImgMessage;
    @BindView(R.id.mImgAudit)
    ImageView mImgAudit;
    @BindView(R.id.mImgReport)
    ImageView mImgReport;
    @BindView(R.id.mLayoutContactUs)
    LinearLayout mLayoutContactUs;
    @BindView(R.id.mLayoutAboutUs)
    RelativeLayout mLayoutAboutUs;
    @BindView(R.id.mLayoutTerm)
    RelativeLayout mLayoutTerm;
    @BindView(R.id.mLayoutStandard)
    RelativeLayout mLayoutStandard;
    @BindView(R.id.mLayoutNews)
    RelativeLayout mLayoutNews;
    @BindView(R.id.mLayoutChangePassword)
    LinearLayout mLayoutChangePassword;
    @BindView(R.id.mLayoutHelp)
    LinearLayout mLayoutHelp;
    @BindView(R.id.mLayoutLogout)
    RelativeLayout mLayoutLogout;

    int main = 0, audit = 0, message = 0, report = 0;


    /* click for contact us */
    @OnClick(R.id.mLayoutContactUs)
    void mGoForContactUs() {
        Intent intent = new Intent(getActivity(), ActivityContactUs.class);
        startActivity(intent);
    } /* click for contact us */

    @OnClick(R.id.mLayoutAboutUs)
    void mGoforAboutUs() {
        Intent intent = new Intent(getActivity(), ActivityStaticPage.class);
        intent.putExtra("mPage", mAboutUs);
        intent.putExtra("mType", "About Us");
        startActivity(intent);
    } /* click for contact us */

    @OnClick(R.id.mLayoutTerm)
    void mGoForTerm() {
        Intent intent = new Intent(getActivity(), ActivityStaticPage.class);
        intent.putExtra("mPage", mTermNdCond);
        intent.putExtra("mType", "Terms and Condition");
        startActivity(intent);
    } /* click for contact us */

    @OnClick(R.id.mLayoutStandard)
    void mGoForStandard() {
        Intent intent = new Intent(getActivity(), ActivityStaticPage.class);
        intent.putExtra("mPage", mStandard);
        intent.putExtra("mType", "Standard and Good Practices");
        startActivity(intent);
    }

    @OnClick(R.id.mLayoutNews)
    void mGoForNews() {
        Intent intent = new Intent(getActivity(), ActivityStaticPage.class);
        intent.putExtra("mPage", mNews);
        intent.putExtra("mType", "News");
        startActivity(intent);
    }


    @OnClick(R.id.mLayoutHelp)
    void mGoForHelp() {
        Intent intent = new Intent(getActivity(), ActivityStaticPage.class);
        intent.putExtra("mPage", mHelp);
        intent.putExtra("mType", "Help");
        startActivity(intent);
    }

    /* click for change password */
    @OnClick(R.id.mLayoutChangePassword)
    void mGoForChangePassword() {
        Intent intent = new Intent(getActivity(), ActivityResetPassword.class);
        startActivity(intent);
    }

    /* click for logout */
    @OnClick(R.id.mLayoutLogout)
    void mGoForLogOut() {
       /* new AlertDialog.Builder(getActivity())
                .setTitle(R.string.mText_logout)
                .setIcon(R.drawable.ic_access4mii_logo)
                .setMessage(R.string.mText_alert_logout)
                .setPositiveButton(getString(R.string.mText_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPDialog(getActivity());
                        mToDoLogout();
                    }

                })
                .setNegativeButton(getString(R.string.mText_no), null)
                .show();*/


        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning_popup_two_button);
        dialog.setCancelable(false);
        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
        TextViewSemiBold mTextTitle = dialog.findViewById(R.id.mTextTitle);
        RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
        RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
        mTextTitle.setText(R.string.mText_logout);
        mTxtMsg.setText(R.string.mText_alert_logout);
        mLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        mLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPDialog(getActivity());
                mToDoLogout();
                dialog.cancel();
            }
        });
        dialog.show();

    }

    /* click for push notification indicator */
    @OnClick(R.id.mImgMainToggale)
    void mMainSwitch() {
        if (main == 0) {
            main = 1;
            audit = 1;
            message = 1;
            report = 1;
            mImgMainToggale.setImageResource(R.drawable.toggle_on);
            mImgAudit.setImageResource(R.drawable.toggle_on);
            mImgMessage.setImageResource(R.drawable.toggle_on);
            mImgReport.setImageResource(R.drawable.toggle_on);
            PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "1");
            PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "1");
            PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "1");
        } else {
            main = 0;
            audit = 0;
            message = 0;
            report = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
            mImgAudit.setImageResource(R.drawable.toggle_off);
            mImgMessage.setImageResource(R.drawable.toggle_off);
            mImgReport.setImageResource(R.drawable.toggle_off);
            PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "0");
        }
        showPDialog(getActivity());
        mToDoNotifyByType();
    }

    /* click for auditor notification indicator */
    @OnClick(R.id.mImgAudit)
    void mAuditSwitch() {
        if (main == 1) {
            if (audit == 0) {
                audit = 1;
                mImgAudit.setImageResource(R.drawable.toggle_on);
                PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "1");
            } else {
                audit = 0;
                mImgAudit.setImageResource(R.drawable.toggle_off);
                PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "0");
            }
            showPDialog(getActivity());
            mToDoNotifyByType();
        }


        if (audit == 0 && message == 0 && report == 0) {
            main = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
            PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "0");
        }

    }

    /* click for message notification indicator */
    @OnClick(R.id.mImgMessage)
    void mMessageSwitch() {
        if (main == 1) {
            if (message == 0) {
                message = 1;
                mImgMessage.setImageResource(R.drawable.toggle_on);
                PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "1");
            } else {
                message = 0;
                mImgMessage.setImageResource(R.drawable.toggle_off);
                PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "0");
            }
            showPDialog(getActivity());
            mToDoNotifyByType();
        }
        if (audit == 0 && message == 0 && report == 0) {
            main = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
            PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "0");
        }

    }

    /* click for report notification indicator */
    @OnClick(R.id.mImgReport)
    void mReportSwitch() {
        if (main == 1) {
            if (report == 0) {
                report = 1;
                mImgReport.setImageResource(R.drawable.toggle_on);
                PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "1");
            } else {
                report = 0;
                mImgReport.setImageResource(R.drawable.toggle_off);
                PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "0");
            }

            showPDialog(getActivity());
            mToDoNotifyByType();
        }
        if (audit == 0 && message == 0 && report == 0) {
            main = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
            PreferenceManager.setFormiiCheckNotificationChat(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationAudit(getActivity(), "0");
            PreferenceManager.setFormiiCheckNotificationReport(getActivity(), "0");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        ButterKnife.bind(this, view);
        audit = Integer.parseInt(PreferenceManager.getFormiiCheckNotificationAudit(getActivity()));
        message = Integer.parseInt(PreferenceManager.getFormiiCheckNotificationChat(getActivity()));
        report = Integer.parseInt(PreferenceManager.getFormiiCheckNotificationReport(getActivity()));
        /*if (audit == 1 && message == 1 && report == 1) {
            main = 1;
            mImgMainToggale.setImageResource(R.drawable.toggle_on);
        } else {
            main = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
        }*/

        if (audit == 1 || message == 1 || report == 1) {
            main = 1;
            mImgMainToggale.setImageResource(R.drawable.toggle_on);
        } else {
            main = 0;
            mImgMainToggale.setImageResource(R.drawable.toggle_off);
        }

        if (audit == 1) {
            audit = 1;
            mImgAudit.setImageResource(R.drawable.toggle_on);
        } else {
            audit = 0;
            mImgAudit.setImageResource(R.drawable.toggle_off);
        }

        if (message == 1) {
            message = 1;
            mImgMessage.setImageResource(R.drawable.toggle_on);
        } else {
            message = 0;
            mImgMessage.setImageResource(R.drawable.toggle_off);
        }

        if (report == 1) {
            report = 1;
            mImgReport.setImageResource(R.drawable.toggle_on);
        } else {
            report = 0;
            mImgReport.setImageResource(R.drawable.toggle_off);
        }


        return view;
    }


    /*api to logout*/
    void mToDoLogout() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                PreferenceManager.cleanData(getActivity());
                                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(getActivity());
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), getActivity());
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
                        Toast.makeText(getActivity(), R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(getActivity()));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(getActivity()));
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    /*api to logout*/
    void mToDoNotifyByType() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "setNotifyByType",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {

                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(getActivity());
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), getActivity());
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
                        Toast.makeText(getActivity(), R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(getActivity()));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(getActivity()));
                params.put("devicetoken", PreferenceManager.getFormiiDeviceId(getActivity()));
                params.put("audit_push", audit + "");
                params.put("msg_push", message + "");
                params.put("report_push", report + "");
                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }
}
