package ABS_ADAPTER;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.covetus.audit.R;

import java.util.ArrayList;
import java.util.Collections;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.ItemMoveCallback;


/**
 * Created by admin1 on 21/1/19.
 */

public class ReorderSelectedLocAdapter extends RecyclerView.Adapter<ReorderSelectedLocAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<SelectedLocation> mSelectedList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextViewSemiBold mTitle;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            mTitle = itemView.findViewById(R.id.mTxtMainLocation);
        }
    }

    public ReorderSelectedLocAdapter(ArrayList<SelectedLocation> mSelectedList, Context context) {
        this.mSelectedList = mSelectedList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reorder_location_selector_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectedLocation selectedLocation = mSelectedList.get(position);
        holder.mTitle.setText(selectedLocation.getmStrMainLocationTitle());


    }

    @Override
    public int getItemCount() {
        return mSelectedList.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mSelectedList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mSelectedList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ReorderSelectedLocAdapter.MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(ReorderSelectedLocAdapter.MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
    }

}