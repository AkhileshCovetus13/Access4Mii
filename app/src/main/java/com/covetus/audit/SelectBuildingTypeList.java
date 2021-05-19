package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ABS_ADAPTER.AgentList;
import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Agent;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditSubLocation;
import ABS_GET_SET.BuildingType;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.FlowLayout;
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
import static Modal.AuditListModal.funGetAudit;
import static Modal.SubLocationModal.funInsertAllSubLocation;

public class SelectBuildingTypeList extends Activity {


    ArrayList<BuildingType> mListBuildingType = new ArrayList<>();
    Map<String, ArrayList<BuildingType>> mMapSubList = new HashMap<String, ArrayList<BuildingType>>();

    ArrayList<RelativeLayout> childViews = new ArrayList<>();

    ArrayList<String> mlistSkillName = new ArrayList<>();
    ArrayList<String> mlistSkillId = new ArrayList<>();


    @BindView(R.id.mLayoutMainCategory)
    LinearLayout mLayoutMainCategory;

    @BindView(R.id.mLayoutClear)
    RelativeLayout mLayoutClear;


    @BindView(R.id.mLayoutSync)
    RelativeLayout mLayoutSync;

    @BindView(R.id.mFlowLayout)
    FlowLayout mFlowLayout;

    @BindView(R.id.mImgBack)
    ImageView mImgBack;

    String mStrAuditId,mStrLanguage,mStrCountry;
    DatabaseHelper db;


    /*click for going back*/
    @OnClick(R.id.mImgBack)
    void mClose() {
        closeKeyBoard(SelectBuildingTypeList.this);
        finish();
    }


    @OnClick(R.id.mLayoutClear)
    void mClearFilter() {
        mListBuildingType.clear();
        mMapSubList.clear();
        mlistSkillName.clear();
        childViews.clear();
        mFlowLayout.removeAllViews();
        mLayoutMainCategory.removeAllViews();
        showPDialog(SelectBuildingTypeList.this);
        mToGetBuildingType();

    }


    @OnClick(R.id.mLayoutSync)
    void mSubmitFilter() {
        System.out.println("<><><> "+ TextUtils.join(",", mlistSkillId));
        if(mlistSkillId.size()>0){
            showPDialog(SelectBuildingTypeList.this);
            mToSubmittedBuildingType(TextUtils.join(",", mlistSkillId));
        }else {
            mShowAlert("Please select building type", SelectBuildingTypeList.this);
            return;
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilding_type);
        ButterKnife.bind(this);
        db = new DatabaseHelper(SelectBuildingTypeList.this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
        mStrAuditId = bundle.getString("mStrAuditId");
        }
        ArrayList<Audit> mList = funGetAudit(PreferenceManager.getFormiiId(SelectBuildingTypeList.this),mStrAuditId,db);
        mStrLanguage = mList.get(0).getmStrLangId();
        mStrCountry = mList.get(0).getmStrCountryId();
        showPDialog(SelectBuildingTypeList.this);
        mToGetBuildingType();
    }

    void mToGetBuildingType() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "getBusinessSectors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONArray jsonArray = response.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                                    String mStrId = jsonObjectArr.getString("id");
                                    final String mStrTitle = jsonObjectArr.getString("title");
                                    String mStrDetail = jsonObjectArr.getString("details");


                                    final View mainCatLayout = getLayoutInflater().inflate(R.layout.bilding_type_item, mLayoutMainCategory, false);
                                    TextViewSemiBold mTextMainCat = mainCatLayout.findViewById(R.id.mTextMainCat);
                                    final TextViewSemiBold mTextMainCatCount = mainCatLayout.findViewById(R.id.mTextMainCatCount);
                                    final RelativeLayout mLayoutMain = mainCatLayout.findViewById(R.id.mLayoutMain);
                                    mTextMainCat.setText(mStrTitle);
                                    if (i == 0) {
                                    mLayoutMain.setBackgroundResource(R.drawable.rounded_corner_city_ex);
                                    mLayoutMain.setPadding(0,19,0,19);
                                    }


                                    JSONArray jsonArrayBusinessTypes = jsonObjectArr.getJSONArray("businessTypes");
                                    ArrayList<BuildingType> mListBuildingSubType = new ArrayList<>();
                                    final ArrayList<String> mListSubTypeXX = new ArrayList<>();
                                    for (int j = 0; j < jsonArrayBusinessTypes.length(); j++) {
                                        JSONObject mSubObj = jsonArrayBusinessTypes.getJSONObject(j);
                                        final String mStrSubId = mSubObj.getString("id");
                                        final String mStrSubTitle = mSubObj.getString("title");
                                        String mStrSubDetail = mSubObj.getString("details");
                                        mListSubTypeXX.add(mStrSubTitle);


                                        BuildingType buildingTypeSub = new BuildingType();
                                        buildingTypeSub.setmStrId(mStrSubId);
                                        buildingTypeSub.setmStrTitle(mStrSubTitle);
                                        buildingTypeSub.setmStrDetail(mStrSubDetail);
                                        mListBuildingSubType.add(buildingTypeSub);
                                        if (i == 0) {
                                            final View subCatLayout = getLayoutInflater().inflate(R.layout.item_sub_cat, mFlowLayout, false);
                                            final TextViewSemiBold mTextPostTitle = subCatLayout.findViewById(R.id.mTextPostTitle);
                                            mTextPostTitle.setText(mStrSubTitle);

                                            if(mlistSkillName.indexOf(mStrSubTitle)>=0){

                                                subCatLayout.setBackgroundResource(R.drawable.rounded_corner_green);
                                                subCatLayout.setPadding(21,21,21,15);
                                                mTextPostTitle.setTextColor(Color.parseColor("#ffffff"));

                                            }

                                            subCatLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {


                                                    if (subCatLayout.getBackground().getConstantState() == getResources().getDrawable(R.drawable.rounded_corner_green).getConstantState()) {
                                                        subCatLayout.setBackgroundResource(R.drawable.rounded_corner_city);
                                                        subCatLayout.setPadding(21 ,21,21,21);
                                                        mTextPostTitle.setTextColor(Color.parseColor("#3DBAAA"));
                                                        int index = mlistSkillName.indexOf(mTextPostTitle.getText().toString());
                                                        if(index>=0){
                                                        mlistSkillName.remove(mTextPostTitle.getText().toString());
                                                        mlistSkillId.remove(index);
                                                        }
                                                        ArrayList<String> list3 = new ArrayList<String>(mlistSkillName);
                                                        list3.retainAll(mListSubTypeXX);
                                                        if(list3.size()>0){
                                                        mTextMainCatCount.setVisibility(View.VISIBLE);
                                                        }else {
                                                        mTextMainCatCount.setVisibility(View.GONE);
                                                        }
                                                        mTextMainCatCount.setText(list3.size()+"");
                                                    } else {

                                                        mlistSkillName.add(mTextPostTitle.getText().toString());
                                                        mlistSkillId.add(mStrSubId);
                                                        subCatLayout.setBackgroundResource(R.drawable.rounded_corner_green);
                                                        subCatLayout.setPadding(21,21,21,21);
                                                        mTextPostTitle.setTextColor(Color.parseColor("#ffffff"));
                                                        ArrayList<String> list3 = new ArrayList<String>(mlistSkillName);
                                                        list3.retainAll(mListSubTypeXX);

                                                        if(list3.size()>0){
                                                            mTextMainCatCount.setVisibility(View.VISIBLE);
                                                        }else {
                                                            mTextMainCatCount.setVisibility(View.GONE);
                                                        }


                                                        mTextMainCatCount.setText(list3.size()+"");
                                                    }
                                                }
                                            });


                                            mFlowLayout.addView(subCatLayout);
                                        }
                                    }
                                    BuildingType buildingType = new BuildingType();
                                    buildingType.setmStrId(mStrId);
                                    buildingType.setmStrTitle(mStrTitle);
                                    buildingType.setmStrDetail(mStrDetail);
                                    buildingType.setmSubList(mListBuildingSubType);
                                    mListBuildingType.add(buildingType);
                                    mMapSubList.put(mStrTitle, mListBuildingSubType);




                                    mainCatLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mFlowLayout.removeAllViews();
                                            clearBackgrounds();

                                            mLayoutMain.setBackgroundResource(R.drawable.rounded_corner_city_ex);
                                            mLayoutMain.setPadding(0,19,0,19);
                                            final ArrayList<BuildingType> mListSubType = mMapSubList.get(mStrTitle);
                                            final ArrayList<String> mListSubTypeXX = new ArrayList<>();
                                            for (int k = 0; k < mListSubType.size(); k++) {
                                                final BuildingType buildingType1 = mListSubType.get(k);
                                                final View subCatLayout = getLayoutInflater().inflate(R.layout.item_sub_cat, mFlowLayout, false);
                                                final TextViewSemiBold mTextPostTitle = subCatLayout.findViewById(R.id.mTextPostTitle);
                                                mTextPostTitle.setText(mListSubType.get(k).getmStrTitle());
                                                mListSubTypeXX.add(mListSubType.get(k).getmStrTitle());

                                                if(mlistSkillName.indexOf(mListSubType.get(k).getmStrTitle())>=0){

                                                    subCatLayout.setBackgroundResource(R.drawable.rounded_corner_green);
                                                    subCatLayout.setPadding(21,21,21,21);
                                                    mTextPostTitle.setTextColor(Color.parseColor("#ffffff"));

                                                }





                                                subCatLayout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (subCatLayout.getBackground().getConstantState() == getResources().getDrawable(R.drawable.rounded_corner_green).getConstantState()) {
                                                            subCatLayout.setBackgroundResource(R.drawable.rounded_corner_city);
                                                            subCatLayout.setPadding(21,21,21,21);
                                                            mTextPostTitle.setTextColor(Color.parseColor("#3DBAAA"));
                                                            int index = mlistSkillName.indexOf(mTextPostTitle.getText().toString());
                                                            if(index>=0){
                                                            mlistSkillName.remove(mTextPostTitle.getText().toString());
                                                            mlistSkillId.remove(index);
                                                            }
                                                            ArrayList<String> list3 = new ArrayList<String>(mlistSkillName);
                                                            list3.retainAll(mListSubTypeXX);

                                                            if(list3.size()>0){
                                                                mTextMainCatCount.setVisibility(View.VISIBLE);
                                                            }else {
                                                                mTextMainCatCount.setVisibility(View.GONE);
                                                            }

                                                            mTextMainCatCount.setText(list3.size()+"");

                                                        } else {

                                                            mlistSkillName.add(mTextPostTitle.getText().toString());
                                                            mlistSkillId.add(buildingType1.getmStrId());
                                                            subCatLayout.setBackgroundResource(R.drawable.rounded_corner_green);
                                                            subCatLayout.setPadding(21,21,21,21);
                                                            mTextPostTitle.setTextColor(Color.parseColor("#ffffff"));
                                                            ArrayList<String> list3 = new ArrayList<String>(mlistSkillName);
                                                            list3.retainAll(mListSubTypeXX);

                                                            if(list3.size()>0){
                                                                mTextMainCatCount.setVisibility(View.VISIBLE);
                                                            }else {
                                                                mTextMainCatCount.setVisibility(View.GONE);
                                                            }


                                                            mTextMainCatCount.setText(list3.size()+"");
                                                        }
                                                    }
                                                });


                                                mFlowLayout.addView(subCatLayout);
                                            }
                                        }
                                    });

                                    childViews.add(mLayoutMain);
                                    mLayoutMainCategory.addView(mainCatLayout);


                                }
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(SelectBuildingTypeList.this);
                            } else {
                                mShowAlert(response.getString("message"), SelectBuildingTypeList.this);
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
                        Toast.makeText(SelectBuildingTypeList.this, getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",PreferenceManager.getFormiiId(SelectBuildingTypeList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(SelectBuildingTypeList.this));
                params.put("audit_id", mStrAuditId);
                params.put("lang", mStrLanguage);
                params.put("country_id", mStrCountry);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    private void clearBackgrounds() {
        for (RelativeLayout textView : childViews) {
            textView.setBackgroundResource(0);
            textView.setBackgroundColor(Color.parseColor("#e2e0e0"));
            textView.setPadding(0,19,0,19);

        }
    }






    void mToSubmittedBuildingType(final String mStrId) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "StoreBusinessSectors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                mShowAlert("Submitted successfully", SelectBuildingTypeList.this);
                                Intent intent = new Intent(SelectBuildingTypeList.this, ActivityTabHostMain.class);
                                intent.putExtra("mStrCurrentTab", "2");
                                intent.putExtra("mReorderStatus", 1);
                                startActivity(intent);
                                finish();
                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(SelectBuildingTypeList.this);
                            } else {
                                mShowAlert(response.getString("message"), SelectBuildingTypeList.this);
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
                        Toast.makeText(SelectBuildingTypeList.this, getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",PreferenceManager.getFormiiId(SelectBuildingTypeList.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(SelectBuildingTypeList.this));
                params.put("audit_id", mStrAuditId);
                params.put("business_sector_id", mStrId);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }




}