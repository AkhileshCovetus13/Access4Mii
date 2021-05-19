package com.covetus.audit;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SelectedSubLocation;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static Modal.SubLocationLayer.funDeleteSubLocationLayer;
import static Modal.SubLocationModal.funDeleteSelectedSubLocation;
import static com.covetus.audit.SelectSubFolderLocationActivity.mAuditId;
import static com.covetus.audit.SelectSubFolderLocationActivity.mLayerId;
import static com.covetus.audit.SelectSubFolderLocationActivity.mLayerTitle;
import static com.covetus.audit.SelectSubFolderLocationActivity.mStrMainLocationServer;
import static com.covetus.audit.SelectSubFolderLocationActivity.mUserId;
import static com.covetus.audit.SelectSubFolderLocationActivity.meMapDesc;
import static com.covetus.audit.SelectSubFolderLocationActivity.updateCount;

class SubLocationDragListAdapter extends RecyclerView.Adapter<SubLocationDragListAdapter.ListViewHolder>
        implements View.OnTouchListener, View.OnLongClickListener {

    private List<String> list;
    private Listener listener;
    String mStatus;
    String mAudit;
    Activity context;

    SubLocationDragListAdapter(Activity context, List<String> list, Listener listener, String mStr, String mAudit) {
        this.list = list;
        this.listener = listener;
        this.mStatus = mStr;
        this.context = context;
        this.mAudit = mAudit;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_location_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        holder.text.setText(list.get(position));
        if (SelectSubFolderLocationActivity.meMap.get(list.get(position)).equals("0")) {
            holder.count.setVisibility(View.GONE);
            holder.count.setText(SelectSubFolderLocationActivity.meMap.get(list.get(position)));
        } else {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(SelectSubFolderLocationActivity.meMap.get(list.get(position)));
        }

        holder.mTextPlus.setText("+");
        holder.mTextMin.setText("-");
        holder.frameLayout.setTag(position);

        if (mStatus.equals("0")) {
            holder.mMainViewLocationBox.setBackgroundResource(R.drawable.rounded_corner_location_box_high_redious);
            holder.mCountButtonLayout.setVisibility(View.GONE);


           /* LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    5.0f
            );
            holder.mLayoutInfo.setLayoutParams(param);
            if(holder.text.getText().toString().length()>62){
            holder.mLayoutInfo.setPadding(0,3,0,3);
            }else if(holder.text.getText().toString().length()>30){
            holder.mLayoutInfo.setPadding(0,10,0,10);
            }else {
            holder.mLayoutInfo.setPadding(0,15,0,15);
            }*/
           System.out.println("<><>##<><>no "+holder.text.getText().length());

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    5.0f
            );
            param.gravity = Gravity.CENTER;
            holder.mLayoutInfo.setLayoutParams(param);

            if(holder.text.getText().toString().length()>49){
                holder.text.setTextSize(9f);
            }else if(holder.text.getText().toString().length()>30){
                holder.text.setTextSize(11f);
            }else {
                holder.text.setTextSize(13f);
            }



           /* if(holder.text.getText().toString().length()>30){
                holder.text.setTextSize(9f);
            }else if(holder.text.getText().toString().length()>15){
                holder.text.setTextSize(9f);
            }else {
                holder.text.setTextSize(13f);
            }*/




            holder.frameLayout.setEnabled(true);

        } else {
            holder.mMainViewLocationBox.setBackgroundResource(R.drawable.rounded_corner_location_box);
            //holder.count.setText("0");
            holder.mCountButtonLayout.setVisibility(View.VISIBLE);

            /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    4.0f
            );
            holder.mLayoutInfo.setLayoutParams(param);
            if(holder.text.getText().toString().length()>49){
                holder.mLayoutInfo.setPadding(0,3,0,3);
            }else if(holder.text.getText().toString().length()>22){
                holder.mLayoutInfo.setPadding(0,10,0,10);
            }else {
                holder.mLayoutInfo.setPadding(0,15,0,15);
            }

*/

            if(holder.text.getText().toString().length()>49){
                holder.text.setTextSize(9f);
            }else if(holder.text.getText().toString().length()>25){
                holder.text.setTextSize(11f);
            }else {
                holder.text.setTextSize(13f);
            }

            holder.frameLayout.setEnabled(false);
            holder.mLayoutInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectSubFolderLocationActivity.mTxtLocationDesc.setText(meMapDesc.get(list.get(position)));
                    SelectSubFolderLocationActivity.mInfoText.setText(list.get(position) + " " + context.getString(R.string.mText_desc));
                }
            });
        }

        if (SelectSubFolderLocationActivity.mStrDelete.equals("1") && mStatus.equals("1")) {
            holder.mImgRemove.setVisibility(View.VISIBLE);
        } else {
            holder.mImgRemove.setVisibility(View.GONE);
        }


// db.update_tb_list_audit(mAuditId,"1");
        holder.mImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
                mTxtMsg.setText(context.getString(R.string.mtextFile_this) + " " + mLayerTitle + context.getString(R.string.mtextFile_subfolder_msg));

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                mConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("<><><><>call");
                        DatabaseHelper dbase = new DatabaseHelper(context);
                        SubLocationExplation subLocationExplation = new SubLocationExplation();
                        subLocationExplation.setmStrLayerId(mLayerId);
                        subLocationExplation.setmStrSubLocationServer(SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                        funDeleteSubLocationLayer(subLocationExplation, "2", dbase);
                        //dbase.delete_tb_sub_location_explation_list(SelectSubFolderLocationActivity.mLayerId, SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                        SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
                        selectedSubLocation.setmStrUserId(mUserId);
                        selectedSubLocation.setmStrAuditId(mAuditId);
                        selectedSubLocation.setmStrSubLocationServer(SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                        selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
                        selectedSubLocation.setmStrLayerId(mLayerId);
                        funDeleteSelectedSubLocation(selectedSubLocation, dbase);
                        //dbase.delete_tb_selected_sub_location(SelectSubFolderLocationActivity.mLayerId, SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                        dbase.delete_main_question_by_opration(mAudit, PreferenceManager.getFormiiId(context), "2", SelectSubFolderLocationActivity.meMapServerId.get(list.get(position)));
                        SelectSubFolderLocationActivity.getRemove(position);
                        dialog.dismiss();
                    }
                });
                dialog.show();



            }
        });

        holder.mLayoutAddCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(holder.count.getText().toString());
                holder.count.setText(a + 1 + "");
                SelectSubFolderLocationActivity.meMap.put(list.get(position), a + 1 + "");
                updateCount(1, list.get(position));
            }
        });

        holder.mLayoutMinCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbase = new DatabaseHelper(context);
                final int a = Integer.parseInt(holder.count.getText().toString());
                if (a != 1) {
                int count = dbase.getCountAllGivenAnsByMainAndSubLocation(mAuditId, mUserId, SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)), mStrMainLocationServer);
                if(count>0){
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                    RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
                    mTxtMsg.setText(context.getString(R.string.mtextFile_this) + " " + mLayerTitle + context.getString(R.string.mtextFile_subfolder_msg));

                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.count.setText(a - 1 + "");
                            SelectSubFolderLocationActivity.meMap.put(list.get(position), a - 1 + "");
                            updateCount(0, list.get(position));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else {
                    holder.count.setText(a - 1 + "");
                    SelectSubFolderLocationActivity.meMap.put(list.get(position), a - 1 + "");
                    updateCount(0, list.get(position));
                }



                } else {





                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                    RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
                    mTxtMsg.setText(context.getString(R.string.mtextFile_this) + " " + mLayerTitle + context.getString(R.string.mtextFile_subfolder_msg));

                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {




                            try {
                                DatabaseHelper dbase = new DatabaseHelper(context);
                                SubLocationExplation subLocationExplation = new SubLocationExplation();
                                subLocationExplation.setmStrLayerId(mLayerId);
                                subLocationExplation.setmStrSubLocationServer(SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                                funDeleteSubLocationLayer(subLocationExplation, "2", dbase);

                                //dbase.delete_tb_sub_location_explation_list(SelectSubFolderLocationActivity.mLayerId, SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                                SelectedSubLocation selectedSubLocation = new SelectedSubLocation();
                                selectedSubLocation.setmStrUserId(mUserId);
                                selectedSubLocation.setmStrAuditId(mAuditId);
                                selectedSubLocation.setmStrSubLocationServer(SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                                selectedSubLocation.setmStrMainLocationServer(mStrMainLocationServer);
                                selectedSubLocation.setmStrLayerId(mLayerId);
                                funDeleteSelectedSubLocation(selectedSubLocation, dbase);
                                //dbase.delete_tb_selected_sub_location(SelectSubFolderLocationActivity.mLayerId, SelectSubFolderLocationActivity.meMapLocalId.get(list.get(position)));
                                dbase.delete_main_question_by_opration(mAudit, PreferenceManager.getFormiiId(context), "2", SelectSubFolderLocationActivity.meMapServerId.get(list.get(position)));
                                SelectSubFolderLocationActivity.getRemove(position);
                                dialog.dismiss();
                            } catch ( IndexOutOfBoundsException e ) {
                                dialog.dismiss();
                            }

                        }
                    });
                    dialog.show();





                }
            }
        });

        holder.frameLayout.setOnLongClickListener(this);
        holder.frameLayout.setOnDragListener(new SubLocationDragListener(listener));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                } else {
                    v.startDrag(data, shadowBuilder, v, 0);
                }
                return true;
        }
        return false;
    }

    List<String> getList() {
        return list;
    }

    void updateList(List<String> list) {
        this.list = list;
    }

    SubLocationDragListener getDragInstance() {
        if (listener != null) {
            return new SubLocationDragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data, shadowBuilder, view, 0);
        } else {
            view.startDrag(data, shadowBuilder, view, 0);
        }

        return true;
    }


    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextViewBold text;
        @BindView(R.id.count)
        TextViewSemiBold count;
        @BindView(R.id.frame_layout_item)
        RelativeLayout frameLayout;
        @BindView(R.id.mLayoutAddCount)
        RelativeLayout mLayoutAddCount;
        @BindView(R.id.mLayoutMinCount)
        RelativeLayout mLayoutMinCount;
        @BindView(R.id.mTextPlus)
        TextViewBold mTextPlus;
        @BindView(R.id.mTextMin)
        TextViewBold mTextMin;
        @BindView(R.id.mCountButtonLayout)
        LinearLayout mCountButtonLayout;
        @BindView(R.id.mImgRemove)
        ImageView mImgRemove;
        @BindView(R.id.mLayoutInfo)
        LinearLayout mLayoutInfo;
        @BindView(R.id.mMainViewLocationBox)
        LinearLayout mMainViewLocationBox;


        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
