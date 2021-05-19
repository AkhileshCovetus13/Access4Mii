package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ABS_ADAPTER.LocationSelectionAdapter;
import ABS_GET_SET.SelectedSubLocation;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static Modal.SubLocationModal.funGetAllSelectedSubLocation;

/**
 * Created by admin1 on 12/12/18.
 */

public class ActivityLocationSelector extends Activity {


    ArrayList<SelectedSubLocation> mListItems = new ArrayList<>();
    @BindView(R.id.mListLocationSelector)
    ListView mListLocationSelector;
    DatabaseHelper databaseHelper;
    @BindView(R.id.mImageMainBack)
    ImageView mImageMainBack;

    @BindView(R.id.mImageStarBack)
    ImageView mImageStarBack;

    LocationSelectionAdapter locationSelectionAdapter;
    String mStrLayerId;
    String mStrAuditId;
    String mStrUserId;
    String mStrMainLocationId;


    @OnClick(R.id.mImageMainBack)
    public void mGoMainLocation() {
        SelectSubFolderLocationActivity.activity.finish();
        finish();
    }


    @OnClick(R.id.mImageStarBack)
    public void mGoSubLocation() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);
        mStrUserId = PreferenceManager.getFormiiId(ActivityLocationSelector.this);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
        mStrLayerId = bundle.getString("mStrLayerId");
        mStrAuditId = bundle.getString("mStrAuditId");
        mStrMainLocationId = bundle.getString("mStrMainLocationId");
        }
        databaseHelper = new DatabaseHelper(ActivityLocationSelector.this);
        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrUserId(mStrUserId);
        selectedSubLocation.setmStrLayerId(mStrLayerId);
        selectedSubLocation.setmStrAuditId(mStrAuditId);
        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationId);
        mListItems = funGetAllSelectedSubLocation(selectedSubLocation,"1",databaseHelper);
        //mListItems = databaseHelper.get_all_tb_selected_sub_location(mStrLayerId);
        locationSelectionAdapter = new LocationSelectionAdapter(ActivityLocationSelector.this, mListItems);
        mListLocationSelector.setAdapter(locationSelectionAdapter);

        mListLocationSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                SelectedSubLocation selectedSubLocation = mListItems.get(position);
                Intent intent = new Intent(ActivityLocationSelector.this, ActivitySubLocationSelectorExplaintion.class);
                intent.putExtra("mSubLocationId", selectedSubLocation.getmStrSubLocationServer());
                intent.putExtra("mSubLocationServerId", selectedSubLocation.getmStrSubLocationServer());
                intent.putExtra("mLayerId", mStrLayerId);
                intent.putExtra("mLayerTitle", selectedSubLocation.getmStrLayerTitle());
                intent.putExtra("mLayerDesc", selectedSubLocation.getmStrLayerDesc());
                intent.putExtra("mSubLocationTitle", selectedSubLocation.getmStrSubLocationTitle());
                intent.putExtra("mAuditId", selectedSubLocation.getmStrAuditId());
                intent.putExtra("mStrMainLocationServer", selectedSubLocation.getmStrMainLocationServer());
                intent.putExtra("mStrMainLocationLocal", selectedSubLocation.getmStrMainLocationLocal());
                System.out.println("<><>title" + selectedSubLocation.getmStrLayerDesc());
                startActivity(intent);
                finish();
            }
        });
    }


}