package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.covetus.audit.ActivityLayerList;
import com.covetus.audit.R;
import com.covetus.audit.SelectSubFolderLocationActivity;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.EditTextSemiBold;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.LayerList;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;

import static Modal.SubFolderModal.funUpdateSubFolderLayer;
import static Modal.SubLocationLayer.funUpdateSubLocationLayer;
import static Modal.SubLocationModal.funGetAllSelectedSubLocation;
import static Modal.SubLocationModal.funGetDataWithCount;
import static Modal.SubLocationModal.funUpdateSelectedSubLocation;
import static com.covetus.audit.ActivityLayerList.updateView;

/**
 * Created by admin1 on 4/12/18.
 */


public class LayerListAdapter extends BaseAdapter {

    private ArrayList<LayerList> mListItems = new ArrayList<>();
    Activity context;
    ArrayList<SelectedSubLocation> mAuditList;

    public LayerListAdapter(Activity context, ArrayList<LayerList> mListItems) {
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
        return arg0;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_layer_list, null);
            holder = new ViewHolder();
            holder.mTxtTitle = convertView.findViewById(R.id.mTxtTitle);
            holder.mTxtDecs = convertView.findViewById(R.id.mTxtDecs);
            holder.mTextArchive = convertView.findViewById(R.id.mTextArchive);
            holder.mImgUpdateData = convertView.findViewById(R.id.mImgUpdateData);
            holder.mImgDelete = convertView.findViewById(R.id.mImgDelete);
            holder.mLayoutView = convertView.findViewById(R.id.mLayoutView);
            holder.mLayoutArchive = convertView.findViewById(R.id.mLayoutArchive);
            holder.simpleGridView = convertView.findViewById(R.id.simpleGridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final LayerList layerList = mListItems.get(position);
        holder.mTxtTitle.setText(layerList.getmStrLayerTitle());
        holder.mTxtDecs.setText(layerList.getmStrLayerDesc());
        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
        selectedSubLocation.setmStrAuditId(layerList.getmStrAuditId());
        selectedSubLocation.setmStrUserId(layerList.getmStrUserId());
        selectedSubLocation.setmStrLayerId(layerList.getmStrId());
        selectedSubLocation.setmStrMainLocationServer(layerList.getmStrMainLocationServerId());
        mAuditList = funGetAllSelectedSubLocation(selectedSubLocation,"1",databaseHelper);

        if (mAuditList.size() > 9) {
            GridAdapter gridAdapter = new GridAdapter(context,mAuditList,layerList.getmStrLayerArchive(),layerList.getmStrId());
            holder.simpleGridView.setAdapter(gridAdapter);
        }else if(mAuditList.size() == 9) {
            GridAdapter gridAdapter = new GridAdapter(context, mAuditList, layerList.getmStrLayerArchive(), layerList.getmStrId());
            holder.simpleGridView.setAdapter(gridAdapter);
        }else if(mAuditList.size() > 0 && mAuditList.size() < 9) {
            mAuditList = funGetDataWithCount(mAuditList);
            GridAdapter gridAdapter = new GridAdapter(context, mAuditList, layerList.getmStrLayerArchive(), layerList.getmStrId());
            holder.simpleGridView.setAdapter(gridAdapter);
        }else if(mAuditList.size() <= 0) {
            mAuditList = funGetDataWithCount(mAuditList);
            GridAdapter gridAdapter = new GridAdapter(context, mAuditList, layerList.getmStrLayerArchive(), layerList.getmStrId());
            holder.simpleGridView.setAdapter(gridAdapter);
        }



        holder.simpleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(mAuditList.get(position).getmStrId().length()>0){
                    SelectedSubLocation selectedSubLocation = mAuditList.get(position);
                    Intent intent = new Intent(context, SelectSubFolderLocationActivity.class);
                    intent.putExtra("mOpration","1");
                    intent.putExtra("mStrMainLocationServer", layerList.getmStrMainLocationServerId());
                    intent.putExtra("mStrMainLocationLocal", layerList.getmStrMainLocationId());
                    intent.putExtra("mLayerId", layerList.getmStrId());
                    intent.putExtra("mAuditId", layerList.getmStrAuditId());
                    intent.putExtra("mLayerTitle", layerList.getmStrLayerTitle());
                    intent.putExtra("mLayerDesc", layerList.getmStrLayerDesc());
                    intent.putExtra("mLayerImg", layerList.getmStrLayerImg());
                    intent.putExtra("mSubLocationId", selectedSubLocation.getmStrSubLocationServer());
                    intent.putExtra("mSubLocationServerId", selectedSubLocation.getmStrSubLocationServer());
                    intent.putExtra("mLayerId", layerList.getmStrId());
                    intent.putExtra("mLayerTitle", selectedSubLocation.getmStrLayerTitle());
                    intent.putExtra("mLayerDesc", selectedSubLocation.getmStrLayerDesc());
                    intent.putExtra("mSubLocationTitle", selectedSubLocation.getmStrSubLocationTitle());
                    intent.putExtra("mAuditId", selectedSubLocation.getmStrAuditId());
                    intent.putExtra("mStrMainLocationServer", selectedSubLocation.getmStrMainLocationServer());
                    intent.putExtra("mStrMainLocationLocal", selectedSubLocation.getmStrMainLocationLocal());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                    context.finish();
                   }
            }
        });





        if (layerList.getmStrLayerArchive().equals("0")) {

            holder.mImgDelete.setImageResource(R.mipmap.ic_cancel_active);
            holder.mImgUpdateData.setImageResource(R.mipmap.ic_update_sub_group);

            holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_view_button);
            holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
            holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.black));
            holder.mTextArchive.setText(R.string.mTextFile_archive);

            holder.mLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SelectSubFolderLocationActivity.class);
                    intent.putExtra("mOpration","0");
                    intent.putExtra("mStrMainLocationServer", layerList.getmStrMainLocationServerId());
                    intent.putExtra("mStrMainLocationLocal", layerList.getmStrMainLocationId());
                    intent.putExtra("mLayerId", layerList.getmStrId());
                    intent.putExtra("mAuditId", layerList.getmStrAuditId());
                    intent.putExtra("mLayerTitle", layerList.getmStrLayerTitle());
                    intent.putExtra("mLayerDesc", layerList.getmStrLayerDesc());
                    intent.putExtra("mLayerImg", layerList.getmStrLayerImg());
                    context.startActivity(intent);
                    context.finish();
                }
            });

            holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityLayerList.RemoveView(layerList.getmStrId());
                }
            });


            holder.mImgUpdateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_update_layer_title_desc);
                    final EditTextSemiBold mEditLayerTitle = dialog.findViewById(R.id.mEditLayerTitle);
                    final EditTextSemiBold mEditLayerDescription = dialog.findViewById(R.id.mEditLayerDescription);
                    TextViewBold mTxtUpdateButton = dialog.findViewById(R.id.mTxtUpdateButton);
                    mEditLayerTitle.setText(holder.mTxtTitle.getText());
                    mEditLayerDescription.setText(holder.mTxtDecs.getText());
                    mTxtUpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String mTitle = mEditLayerTitle.getText().toString();
                            String mDescription = mEditLayerDescription.getText().toString();

                            if (mTitle.length() <= 0) {
                                CommonUtils.mShowAlert(context.getString(R.string.mTextFile_error_audit_title), context);
                                return;
                            } else if (mDescription.length() <= 0) {
                                CommonUtils.mShowAlert(context.getString(R.string.mTextFile_error_audit_desc), context);
                                return;
                            }
                            holder.mTxtTitle.setText(mTitle);
                            holder.mTxtDecs.setText(mDescription);
                            LayerList layerUpdateList = new LayerList();
                            layerUpdateList.setmStrLayerTitle(mTitle);
                            layerUpdateList.setmStrLayerDesc(mDescription);
                            layerUpdateList.setmStrId(layerList.getmStrId());
                            funUpdateSubFolderLayer(layerUpdateList,"1",databaseHelper);

                            SelectedSubLocation selectedSubLocation1 = new SelectedSubLocation();
                            selectedSubLocation1.setmStrAuditId(layerList.getmStrAuditId());
                            selectedSubLocation1.setmStrUserId(layerList.getmStrUserId());
                            selectedSubLocation1.setmStrMainLocationServer(layerList.getmStrMainLocationServerId());
                            selectedSubLocation1.setmStrLayerId(layerList.getmStrId());
                            selectedSubLocation1.setmStrLayerTitle(mTitle);
                            selectedSubLocation1.setmStrLayerDesc(mDescription);
                            funUpdateSelectedSubLocation(selectedSubLocation1,"1",databaseHelper);


                            SubLocationExplation subLocationExplation = new SubLocationExplation();
                            subLocationExplation.setmStrLayerDesc(mDescription);
                            subLocationExplation.setmStrLayerTitle(mTitle);
                            subLocationExplation.setmStrLayerId(layerList.getmStrId());
                            subLocationExplation.setmStrMainLocationServer(layerList.getmStrMainLocationServerId());
                            subLocationExplation.setmStrUserId(layerList.getmStrUserId());
                            subLocationExplation.setmStrAuditId(layerList.getmStrAuditId());
                            funUpdateSubLocationLayer(subLocationExplation,"2",databaseHelper);
                            databaseHelper.update_question_answer_table_detail(layerList.getmStrAuditId(),layerList.getmStrUserId(),layerList.getmStrId(),"2",mTitle,mDescription);
                            updateView(position);
                            dialog.dismiss();
                        }
                    });


                    dialog.show();
                }
            });



        } else {
            holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
            holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_green);
            holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.white));
            holder.mTextArchive.setText(R.string.mTextFile_error_reinitiate);
            holder.mImgDelete.setImageResource(R.mipmap.ic_cancel_de_active);
            holder.mImgUpdateData.setImageResource(R.mipmap.ic_update_sub_group_de_active);
        }

        holder.mLayoutArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mTextArchive.getText().toString().equals(context.getString(R.string.mTextFile_archive))) {
                    holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
                    holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_green);
                    holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.white));
                    holder.mTextArchive.setText(R.string.mTextFile_error_reinitiate);
                    LayerList layerList1 = new LayerList();
                    layerList1.setmStrLayerArchive("1");
                    layerList1.setmStrId(layerList.getmStrId());
                    funUpdateSubFolderLayer(layerList1,"3",databaseHelper);
                    updateView(position);
                } else {
                    holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_view_button);
                    holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
                    holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.black));
                    holder.mTextArchive.setText(R.string.mTextFile_archive);
                    LayerList layerList1 = new LayerList();
                    layerList1.setmStrLayerArchive("0");
                    layerList1.setmStrId(layerList.getmStrId());
                    funUpdateSubFolderLayer(layerList1,"3",databaseHelper);
                    updateView(position);
                }
            }
        });
        return convertView;
    }


    private class ViewHolder {
        TextViewSemiBold mTxtTitle;
        TextViewSemiBold mTxtDecs;
        TextViewBold mTextArchive;
        ImageView mImgUpdateData, mImgDelete;
        RelativeLayout mLayoutView;
        RelativeLayout mLayoutArchive;
        GridView simpleGridView;
}


}
