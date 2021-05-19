package ABS_ADAPTER;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.covetus.audit.R;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SelectedSubLocation;
import ABS_HELPER.DatabaseHelper;

/**
 * Created by admin1 on 6/2/19.
 */

public class GridAdapter extends BaseAdapter {

    Activity context;
    LayoutInflater inflter;
    ArrayList<SelectedSubLocation> mSelectedLocation;
    String mLayerArchive;
    String mSubFolderLayerId;

    public GridAdapter(Activity applicationContext, ArrayList<SelectedSubLocation> mSelectedLocation, String mLayerArchive, String mSubFolderLayerId) {
        this.context = applicationContext;
        this.mSelectedLocation = mSelectedLocation;
        this.mLayerArchive = mLayerArchive;
        this.mSubFolderLayerId = mSubFolderLayerId;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mSelectedLocation.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.grid_item, null); // inflate the layout
        RelativeLayout m1stRow1stColom = view.findViewById(R.id.m1stRow1stColom);
        TextViewSemiBold m1stRow1stColomText = view.findViewById(R.id.m1stRow1stColomText);
        //m1stRow1stColom.setLayoutParams(new GridView.LayoutParams(120, 120));
        m1stRow1stColom.setLayoutParams(new GridView.LayoutParams(70, 70));
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        double percent = 0;
        SelectedSubLocation selectedSubLocation = mSelectedLocation.get(position);
        String mStrSubLocationServer = selectedSubLocation.getmStrSubLocationServer();
        String mStrMainLocationServer = selectedSubLocation.getmStrMainLocationServer();
        String mStrAuditId = selectedSubLocation.getmStrAuditId();
        String mStrUserId = selectedSubLocation.getmStrUserId();
        String locationcount = selectedSubLocation.getmStrSubLocationCount();
        if(mStrUserId.equals("")){
            percent = -1;
        }else {
            int allQuestion = databaseHelper.getAllQuestion(mStrAuditId, mStrUserId, mStrMainLocationServer, mStrSubLocationServer, "1");
            int mIntAllInspectorQuestion = databaseHelper.getCountOfAllSubLocationInspectorQuestion(mStrAuditId, mStrUserId, mStrMainLocationServer, mStrSubLocationServer);
            allQuestion = allQuestion+mIntAllInspectorQuestion;
            allQuestion = allQuestion * Integer.parseInt(locationcount);
            int allGivenQuestion = databaseHelper.getAllGivenQuestion(mStrAuditId, mStrUserId, mStrMainLocationServer, mStrSubLocationServer, "1", mSubFolderLayerId);
            int mIntAllGivenInspectorQuestion = databaseHelper.getCountAllGivenInspectorAns2(mStrAuditId, mStrUserId, mStrSubLocationServer, mStrMainLocationServer,"1");
            allGivenQuestion = allGivenQuestion+mIntAllGivenInspectorQuestion;
            percent = (double) allGivenQuestion / (double) allQuestion;
            percent = percent * 100;
        }
        if (percent == 0 && mLayerArchive.equals("0")) {
            m1stRow1stColom.setBackgroundResource(R.drawable.rounded_corner_layer_red_dark);
            m1stRow1stColomText.setText(selectedSubLocation.getmStrSubLocationCount());
        } else if (percent == 100 && mLayerArchive.equals("0")) {
            m1stRow1stColom.setBackgroundResource(R.drawable.rounded_corner_layer_green_dark);
            m1stRow1stColomText.setText(selectedSubLocation.getmStrSubLocationCount());
        } else if (percent > 0 && percent < 100 && mLayerArchive.equals("0")) {
            m1stRow1stColom.setBackgroundResource(R.drawable.rounded_corner_layer_yello_dark);
            m1stRow1stColomText.setText(selectedSubLocation.getmStrSubLocationCount());
        } else {
            m1stRow1stColom.setBackgroundResource(R.drawable.rounded_corner_layer_gray_sublocation_box);
            m1stRow1stColomText.setText(selectedSubLocation.getmStrSubLocationCount());
        }
        return view;
    }
}
