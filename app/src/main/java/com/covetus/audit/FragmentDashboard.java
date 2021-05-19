package com.covetus.audit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.DashboardSideMenu;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SideMenu;
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


public class FragmentDashboard extends Fragment {

    ArrayList<SideMenu> sideMenus = new ArrayList<>();
    DashboardSideMenu dashboardSideMenu;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.mDrawerList)
    ListView mDrawerList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mImageMenu)
    ImageView mImageMenu;
    @BindView(R.id.mImgNotification)
    ImageView mImgNotification;

    @BindView(R.id.mImgUserProfile)
    ImageView mImgUserProfile;
    @BindView(R.id.mTextUserName)
    TextView mTextUserName;
    @BindView(R.id.mLayoutProfile)
    LinearLayout mLayoutProfile;

    @BindView(R.id.mTxtNotificationCount)
    TextViewRegular mTxtNotificationCount;

    int mSelected = -1;

    /* click to open drawer */
    @OnClick(R.id.mImageMenu)
    void mGoForDawer() {
        CommonUtils.OnClick(getActivity(), mImageMenu);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /* click to view notification */
    @OnClick(R.id.mImgNotification)
    void mGoForNotification() {
        CommonUtils.OnClick(getActivity(), mImgNotification);
        Intent intent = new Intent(getActivity(), ActivityNotificationList.class);
        startActivity(intent);
    }

    /* click to view profile */
    @OnClick(R.id.mLayoutProfile)
    void mGoFor() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        ButterKnife.bind(this, view);
        initView();


        /* click for particular menu item */
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (pos == 0) {
                    Intent intent = new Intent(getActivity(), ActivityProfile.class);
                    startActivity(intent);
                }else if(pos==1){
                    Intent intent = new Intent(getActivity(), AuditListHistory.class);
                    startActivity(intent);
                }else if (pos == 2) {
                    Intent intent = new Intent(getActivity(), ActivityReportList.class);
                    startActivity(intent);
                } else if (pos == 4) {
                  /*  new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.mText_logout)
                            .setIcon(R.drawable.ic_access4mii_logo)
                            .setMessage(R.string.mText_alert_logout)
                            .setPositiveButton(R.string.mText_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showPDialog(getActivity());
                                    mToDoLogout();
                                }

                            })
                            .setNegativeButton(R.string.mText_no, null)
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
                mSelected = pos;
                mDrawerList.setAdapter(dashboardSideMenu);
                dashboardSideMenu.mSetSelection(pos);
            }
        });



         /* fragment intilization */
        FragmentDashboardHome fragmentDiscover = new FragmentDashboardHome();
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragmentDiscover)
                .commit();
        return view;
    }

    /*sidemenu intilaization*/
    private void initView() {
        sideMenus.clear();

        SideMenu sideMenu = new SideMenu();
        sideMenu.setmSideMenuIconActive(R.mipmap.ic_menu_account);
        sideMenu.setmSideMenuTitle(getString(R.string.mTextMenu_Account));


        SideMenu sideMenu31 = new SideMenu();
        sideMenu31.setmSideMenuIconActive(R.mipmap.ic_menu_history);
        sideMenu31.setmSideMenuTitle(getString(R.string.mTextMenu_History));


        SideMenu sideMenu3 = new SideMenu();
        sideMenu3.setmSideMenuIconActive(R.mipmap.ic_menu_reports);
        sideMenu3.setmSideMenuTitle(getString(R.string.mTextMenu_Reports));


        SideMenu sideMenu5 = new SideMenu();
        sideMenu5.setmSideMenuIconActive(R.mipmap.ic_menu_help);
        sideMenu5.setmSideMenuTitle(getString(R.string.mTextMenu_Help));

        SideMenu sideMenu6 = new SideMenu();
        sideMenu6.setmSideMenuIconActive(R.mipmap.ic_menu_logout);
        sideMenu6.setmSideMenuTitle(getString(R.string.mTextMenu_Logout));


        sideMenus.add(sideMenu);
        sideMenus.add(sideMenu31);
        sideMenus.add(sideMenu3);

        sideMenus.add(sideMenu5);
        sideMenus.add(sideMenu6);

        dashboardSideMenu = new DashboardSideMenu(getActivity(), sideMenus);
        mDrawerList.setAdapter(dashboardSideMenu);

    }


    /* api call to get notification count */
    void mGetNotificationCount() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "notifyCount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                String mStrCount = jsonObject.getString("count");
                                int count = Integer.parseInt(mStrCount);
                                if (count > 0) {
                                    mTxtNotificationCount.setVisibility(View.VISIBLE);
                                    mTxtNotificationCount.setText(mStrCount);
                                }
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(getActivity());
                            } else {

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
                        Toast.makeText(getActivity(), getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", PreferenceManager.getFormiiId(getActivity()));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(getActivity()));
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    @Override
    public void onResume() {
        mGetNotificationCount();

        /*if (mCheckSignalStrength(getActivity()) == 2) {
            mGetNotificationCount();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), getActivity());
        }*/



         /* user image and title set */
        mTextUserName.setText(PreferenceManager.getFormiiFullName(getActivity()));
        System.out.println("<><>mImg" + CommonUtils.mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(getActivity()));
        Glide.with(getActivity()).load(CommonUtils.mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(getActivity())).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImgUserProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(FragmentDashboard.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImgUserProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        super.onResume();
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
}
