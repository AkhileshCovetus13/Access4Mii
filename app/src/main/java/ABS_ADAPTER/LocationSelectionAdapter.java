/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.covetus.audit.R;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SelectedSubLocation;


public class LocationSelectionAdapter extends BaseAdapter {

    private ArrayList<SelectedSubLocation> mListItems = new ArrayList<>();
    Context context;


    public LocationSelectionAdapter(Context context, ArrayList<SelectedSubLocation> mListItems) {
        this.mListItems = mListItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = LayoutInflater.from(context);
        convertView = mInflater.inflate(R.layout.item_location_selector_list, null);
        holder = new ViewHolder();
        holder.mTxtMainLocation = convertView.findViewById(R.id.mTxtMainLocation);
        holder.mTxtMainLocationCount = convertView.findViewById(R.id.mTxtMainLocationCount);
        holder.mMainView = convertView.findViewById(R.id.mMainView);
        SelectedSubLocation selectedSubLocation = mListItems.get(position);
        holder.mTxtMainLocation.setText(selectedSubLocation.getmStrSubLocationTitle());
        holder.mTxtMainLocationCount.setText(selectedSubLocation.getmStrSubLocationCount());
        return convertView;
    }

    private class ViewHolder {
        TextViewSemiBold mTxtMainLocation;
        TextViewSemiBold mTxtMainLocationCount;
        LinearLayout mMainView;
    }
}