package com.covetus.audit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.AudittList;
import ABS_GET_SET.Audit;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.ButterKnife;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.showPDialog;
import static Modal.AuditListModal.funGetAllAudit;
import static Modal.AuditListModal.funInsertAllAudit;

public class FragmentAuditList extends Fragment {

    public static ArrayList<Audit> mListItems = new ArrayList<>();
    public static AudittList audittList;
    public static ListView mListChat;
    public static DatabaseHelper db;
    public static Activity activity;
    ImageView mImgFilterBy;
    public static String mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_list, container, false);
        ButterKnife.bind(this, view);
        mUserId = PreferenceManager.getFormiiId(getActivity());
        PreferenceManager.setFormiiFilterByDate(getActivity(), "0");
        PreferenceManager.setFormiiFilterByTitle(getActivity(), "0");
        setHasOptionsMenu(true);
        mListChat = view.findViewById(R.id.mListChat);
        mImgFilterBy = view.findViewById(R.id.mImgFilterBy);
        activity = getActivity();
        mListItems.clear();
        /* database intilization */
        db = new DatabaseHelper(getActivity());
        if (PreferenceManager.getFormiiCheckIsFirstTime(getActivity()).equals("1")) {
            System.out.println("<><>call 1");
            if (mCheckSignalStrength(getActivity()) == 2) {
                PreferenceManager.setFormiiCheckIsFirstTime(getActivity(), "0");
                audittList = new AudittList(getActivity(), mListItems);
                mListChat.setAdapter(audittList);
                showPDialog(getActivity());
                mToGetAuditList();
            } else {
                mShowAlert(getString(R.string.mTextFile_alert_no_internet), getActivity());
            }
        } else {
            System.out.println("<><>call 2");
               /* database data into list */
            mListItems = funGetAllAudit(mUserId, "0", "", "1", db);
            if (mListItems.size() > 0) {
                audittList = new AudittList(getActivity(), mListItems);
                mListChat.setAdapter(audittList);
            } else {
                CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found), getActivity());
            }
        }


        mImgFilterBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListItems.size() == 0) {
                    CommonUtils.mShowAlert("No record to sort", getActivity());
                } else {

                    System.out.println("<><><><> " + PreferenceManager.getFormiiFilterByTitle(getActivity()));
                    System.out.println("<><><><> " + PreferenceManager.getFormiiFilterByDate(getActivity()));
                    Context wrapper = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.PopupMenu);
                    PopupMenu popup = new PopupMenu(wrapper, v);
                    popup.getMenuInflater().inflate(R.menu.overflow_menu_example, popup.getMenu());
                    if (PreferenceManager.getFormiiFilterByDate(getActivity()).equals("0") && PreferenceManager.getFormiiFilterByTitle(getActivity()).equals("0")) {
                        System.out.println("<><><><> 1");
                    } else if (PreferenceManager.getFormiiFilterByDate(getActivity()).equals("1") && PreferenceManager.getFormiiFilterByTitle(getActivity()).equals("0")) {
                        System.out.println("<><><><> 2");
                        MenuItem subMenuItem2 = popup.getMenu().findItem(R.id.yellow_menu);
                        subMenuItem2.setChecked(subMenuItem2.isChecked());
                        MenuItem subMenuItem = popup.getMenu().findItem(R.id.red_menu);
                        subMenuItem.setChecked(!subMenuItem.isChecked());

                    } else if (PreferenceManager.getFormiiFilterByDate(getActivity()).equals("0") && PreferenceManager.getFormiiFilterByTitle(getActivity()).equals("1")) {
                        System.out.println("<><><><> 3");
                        MenuItem subMenuItem = popup.getMenu().findItem(R.id.red_menu);
                        subMenuItem.setChecked(subMenuItem.isChecked());
                        MenuItem subMenuItem2 = popup.getMenu().findItem(R.id.yellow_menu);
                        subMenuItem2.setChecked(!subMenuItem2.isChecked());
                    }

                    /** Defining menu item click listener for the popup menu */
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("By Date")) {
                                PreferenceManager.setFormiiFilterByDate(getActivity(), "1");
                                PreferenceManager.setFormiiFilterByTitle(getActivity(), "0");
                                Collections.sort(mListItems, new Comparator<Audit>() {
                                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

                                    @Override
                                    public int compare(Audit lhs, Audit rhs) {
                                        try {
                                            return f.parse(rhs.getmDueDate()).compareTo(f.parse(lhs.getmDueDate()));
                                        } catch (ParseException e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    }
                                });
                                audittList.notifyDataSetChanged();
                            } else if (item.getTitle().equals("By Title(a-z)")) {
                                PreferenceManager.setFormiiFilterByDate(getActivity(), "0");
                                PreferenceManager.setFormiiFilterByTitle(getActivity(), "1");
                                Collections.sort(mListItems, new Comparator<Audit>() {
                                    @Override
                                    public int compare(Audit item, Audit t1) {
                                        String s1 = item.getmTitle();
                                        String s2 = t1.getmTitle();
                                        return s1.compareToIgnoreCase(s2);
                                    }

                                });
                                audittList.notifyDataSetChanged();
                            }


                            return true;
                        }
                    });

                    /** Showing the popup menu */
                    popup.show();
                }
            }
        });


        return view;
    }

    public static void reload() {
        mListItems.clear();
        db = new DatabaseHelper(activity);
        /* database data into list */
        mListItems = funGetAllAudit(mUserId, "0", "", "1", db);
        if (mListItems.size() > 0) {
            audittList = new AudittList(activity, mListItems);
            mListChat.setAdapter(audittList);
        } else {
            audittList.notifyDataSetChanged();
            CommonUtils.mShowAlert(activity.getString(R.string.mTextFile_error_no_record_found), activity);
        }
    }

    /*api call for getting audit list*/
    void mToGetAuditList() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getauditHistory",
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
                                JSONObject jsonObject = response.getJSONObject("response");
                                JSONArray jsonArray = jsonObject.getJSONArray("history");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Audit audit = new Audit();
                                    JSONObject mAgentObj = jsonArray.getJSONObject(i);
                                    JSONObject jAuditDetail = mAgentObj.getJSONObject("auditDetails");
                                    audit.setmUserId(PreferenceManager.getFormiiId(getActivity()));
                                    audit.setmAuditId(jAuditDetail.getString("id"));
                                    audit.setmTitle(jAuditDetail.getString("title"));
                                    audit.setmAuditType(jAuditDetail.getString("auditType"));
                                    audit.setmDueDate(jAuditDetail.getString("end_date"));
                                    JSONObject jAgentDetail = mAgentObj.getJSONObject("agentDetails");
                                    audit.setmAssignBy(jAgentDetail.getString("fulname"));
                                    audit.setmStatus("0");
                                    //mListItems.add(audit);
                                    funInsertAllAudit(audit, db);
                                }
                                fungetAuditList();

                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(getActivity());
                                audittList = new AudittList(getActivity(), mListItems);
                                mListChat.setAdapter(audittList);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), getActivity());
                                audittList = new AudittList(getActivity(), mListItems);
                                mListChat.setAdapter(audittList);
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
                        Toast.makeText(getActivity(), getString(R.string.mTextFile_error_something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

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

    public void fungetAuditList() {
        mListItems = funGetAllAudit(mUserId, "0", "", "1", db);
        if (mListItems.size() > 0) {
            audittList = new AudittList(getActivity(), mListItems);
            mListChat.setAdapter(audittList);
        } else {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found), getActivity());
        }
    }

}