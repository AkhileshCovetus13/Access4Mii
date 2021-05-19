package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.ReorderSelectedLocAdapter;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.ItemMoveCallback;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.showPDialog;
import static Modal.MainLocationModal.funGetAllSelectedMainLocation;

public class ActivityReorderSelectedLocationList extends Activity {

    DatabaseHelper db;
    String mAuditId;

    ArrayList<SelectedLocation> mListItems = new ArrayList<>();
    ReorderSelectedLocAdapter mAdapter;

    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    @BindView(R.id.mTxtSubmit)
    TextViewSemiBold mTxtSubmit;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    /*click for going back*/
    @OnClick(R.id.mTxtSubmit)
    void mSubmit() {
         /* Internet Connectivity Check */
        if (mCheckSignalStrength(ActivityReorderSelectedLocationList.this) == 2) {
            showPDialog(ActivityReorderSelectedLocationList.this);
            mToDoOrderLocation(mAuditId);
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityReorderSelectedLocationList.this);
        }

    }



    public  void mToDoOrderLocation(final String mAuditId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "LocationArrange",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>orderres" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                Intent intent = new Intent(ActivityReorderSelectedLocationList.this,SelectBuildingTypeList.class);
                                intent.putExtra("mStrAuditId",mAuditId);
                                startActivity(intent);
                                finish();

                               /* Intent intent = new Intent(ActivityReorderSelectedLocationList.this, ActivityTabHostMain.class);
                                intent.putExtra("mStrCurrentTab", "2");
                                intent.putExtra("mReorderStatus", 1);
                                startActivity(intent);
                                finish();*/
                                //CommonUtils.mShowAlert(response.getString("message"), ActivityContactUs.this);
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityReorderSelectedLocationList.this);
                                finish();
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityReorderSelectedLocationList.this);
                                finish();
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
                        Toast.makeText(ActivityReorderSelectedLocationList.this, R.string.mTextFile_error_something_went_wrong + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < mListItems.size(); k++) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(mListItems.get(k).getmStrMainLocationServerId());
                }
                params.put("audit_id", mAuditId);
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityReorderSelectedLocationList.this));
                params.put("id", PreferenceManager.getFormiiId(ActivityReorderSelectedLocationList.this));
                if (mListItems.size()>0){
                    params.put("location_id", sb.toString());
                }else{
                    // params.put("location_id", );
                }

                params.put("order_id", "");
                System.out.println("<><><>orderparam" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    /*click for going back*/
    @OnClick(R.id.mImageBack)
    void mClose() {
        closeKeyBoard(ActivityReorderSelectedLocationList.this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_location_list);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recyclerView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuditId = bundle.getString("mAuditId");
        }
        db = new DatabaseHelper(this);

        SelectedLocation selectedLocation = new SelectedLocation();
        selectedLocation.setmStrAuditId(mAuditId);
        selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(ActivityReorderSelectedLocationList.this));
        mListItems = funGetAllSelectedMainLocation(selectedLocation, db);
        mAdapter = new ReorderSelectedLocAdapter(mListItems, ActivityReorderSelectedLocationList.this);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter, ActivityReorderSelectedLocationList.this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }
}