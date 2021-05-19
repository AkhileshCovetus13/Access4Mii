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
import ABS_GET_SET.Audit;
import ABS_GET_SET.SelectedLocation;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static Modal.AuditListModal.funUpdateAudit;
import static Modal.MainLocationModal.funDeleteSelectedMainLocation;
import static Modal.MainLocationModal.funGetSelectedMainLocationCount;
import static Modal.SubFolderModal.funDeleteSubFolderLayer;
import static Modal.SubFolderModal.funDeleteSubFolders;
import static Modal.SubFolderModal.funIsLayerByMainLocation;
import static com.covetus.audit.SelectMainLocationActivity.mAuditId;
import static com.covetus.audit.SelectMainLocationActivity.mTxtLocationDesc;
import static com.covetus.audit.SelectMainLocationActivity.mUserId;
import static com.covetus.audit.SelectMainLocationActivity.meMapDesc;

class DragListAdapter extends RecyclerView.Adapter<DragListAdapter.ListViewHolder>
        implements View.OnTouchListener, View.OnLongClickListener {

    private List<String> list;
    private Listener listener;
    String mStatus;
    String mAudit;
    Activity context;

    DragListAdapter(Activity context, List<String> list, Listener listener, String mStr, String mAudit) {
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
        System.out.println("##<><>##<><> "+list.size());
        System.out.println("##<><>##<><> "+SelectMainLocationActivity.meMap.size());
        System.out.println("##<><>##<><> "+SelectMainLocationActivity.meMap.get(list.get(position)));
        holder.text.setText(list.get(position));
        if (SelectMainLocationActivity.meMap.get(list.get(position)).equals("0")) {
            holder.count.setVisibility(View.GONE);
            holder.count.setText(SelectMainLocationActivity.meMap.get(list.get(position)));
        } else {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(SelectMainLocationActivity.meMap.get(list.get(position)));
        }
        holder.mTextPlus.setText("+");
        holder.mTextMin.setText("-");
        holder.frameLayout.setTag(position);
        if (mStatus.equals("1")) {
            holder.mMainViewLocationBox.setBackgroundResource(R.drawable.rounded_corner_location_box);
            holder.mMainViewLocationBox.setEnabled(false);
            holder.mCountButtonLayout.setVisibility(View.VISIBLE);

            if(holder.text.getText().toString().length()>49){
                holder.text.setTextSize(9f);
            }else if(holder.text.getText().toString().length()>25){
                holder.text.setTextSize(11f);
            }else {
                holder.text.setTextSize(13f);
            }


          /*  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
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

            holder.frameLayout.setEnabled(false);
            holder.mLayoutInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTxtLocationDesc.setText(meMapDesc.get(list.get(position)));
                    SelectMainLocationActivity.mInfoText.setText(list.get(position)+" "+context.getString(R.string.mText_desc));
                }
            });
        } else {
            holder.mMainViewLocationBox.setEnabled(false);
            holder.mMainViewLocationBox.setBackgroundResource(R.drawable.rounded_corner_location_box_high_redious);
            //holder.count.setText("0");
            holder.mCountButtonLayout.setVisibility(View.GONE);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    5.0f
            );
            param.gravity = Gravity.CENTER;
            holder.mLayoutInfo.setLayoutParams(param);

            if(holder.text.getText().toString().length()>49){
                holder.text.setTextSize(9f);
            }else if(holder.text.getText().toString().length()>25){
                holder.text.setTextSize(11f);
            }else {
                holder.text.setTextSize(13f);
            }

            holder.frameLayout.setEnabled(true);
        }

        System.out.println("<><><>call "+SelectMainLocationActivity.mStrDelete);
        System.out.println("<><><>call "+mStatus);
        if (SelectMainLocationActivity.mStrDelete.equals("1") && mStatus.equals("1")) {
            holder.mImgRemove.setVisibility(View.VISIBLE);
        } else {
            holder.mImgRemove.setVisibility(View.GONE);
        }



// db.update_tb_list_audit(mAuditId,"1");
        holder.mImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
                if(funIsLayerByMainLocation(mAudit,mUserId,SelectMainLocationActivity.meMapServerId.get(list.get(position)),db)){
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                    RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
                    mTxtMsg.setText(context.getString(R.string.mtextFile_this) + list.get(position) + context.getString(R.string.mtextFile_foldr_msg));
//                    mTxtMsg.setText(context.getString(R.string.mtextFile_this)+list.get(position)+" Folder and Sub folders including associated questions,Do you want to proceed ?");
                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper dbase = new DatabaseHelper(context);
                            SelectedLocation selectedLocation = new SelectedLocation();
                            selectedLocation.setmStrAuditId(mAudit);
                            selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                            selectedLocation.setmStrMainLocationServerId(SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                            funDeleteSelectedMainLocation(selectedLocation,dbase);
                            /*dbase.delete_tb_selected_main_location();*/
                            funDeleteSubFolders(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,mUserId,dbase);
                            //dbase.delete_tb_location_sub_folder(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                            funDeleteSubFolderLayer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,"3",dbase);
                            //dbase.delete_tb_sub_folder_explation_list_all(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                            dbase.deleteAllQuestionAnswer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,PreferenceManager.getFormiiId(context));
                            dbase.deleteSubQuestionAnswer(mAuditId,PreferenceManager.getFormiiId(context));
                            //dbase.delete_main_question_by_opration(mAudit,PreferenceManager.getFormiiId(context),"4",SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                            SelectMainLocationActivity.getRemove(position,holder.text.getText().toString());
                            if (SelectMainLocationActivity.getCount() == 0) {
                                Audit audit = new Audit();
                                audit.setmStatus("0");
                                audit.setmAuditId(mAudit);
                                funUpdateAudit(audit,"1",dbase);
                                //dbase.delete_all_data_of_audit(mAudit, PreferenceManager.getFormiiId(context));
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else {
                    DatabaseHelper dbase = new DatabaseHelper(context);
                    SelectedLocation selectedLocation = new SelectedLocation();
                    selectedLocation.setmStrAuditId(mAudit);
                    selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                    selectedLocation.setmStrMainLocationServerId(SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                    funDeleteSelectedMainLocation(selectedLocation,dbase);


                    /*dbase.delete_tb_selected_main_location(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));*/
                    funDeleteSubFolders(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,mUserId,dbase);
                    //dbase.delete_tb_location_sub_folder(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                    funDeleteSubFolderLayer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,"3",dbase);
                    //dbase.delete_tb_sub_folder_explation_list_all(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));




                    dbase.deleteAllQuestionAnswer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,PreferenceManager.getFormiiId(context));
                    dbase.deleteSubQuestionAnswer(mAuditId,PreferenceManager.getFormiiId(context));
                    SelectMainLocationActivity.getRemove(position,holder.text.getText().toString());

                    if (SelectMainLocationActivity.getCount() == 0) {
                        Audit audit = new Audit();
                        audit.setmStatus("0");
                        audit.setmAuditId(mAudit);
                        funUpdateAudit(audit,"1",dbase);
                        //dbase.delete_all_data_of_audit(mAudit, PreferenceManager.getFormiiId(context));
                    }
                }
             }
        });

        holder.mLayoutAddCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectMainLocationActivity.updateCount(1,list.get(position));
                int a = Integer.parseInt(holder.count.getText().toString());
                holder.count.setText(a + 1 + "");
                SelectMainLocationActivity.meMap.put(list.get(position), a + 1 + "");

                /*
                if (holder.count.getText().length() <= 0) {
                    holder.count.setVisibility(View.VISIBLE);
                    holder.count.setText("0");

                } else {
                    holder.count.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(holder.count.getText().toString());
                    holder.count.setText(a + 1 + "");
                    SelectMainLocationActivity.meMap.put(list.get(position), a + 1 + "");
                }*/

            }
        });

        holder.mLayoutMinCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(context);
                final int a = Integer.parseInt(holder.count.getText().toString());
                if(funIsLayerByMainLocation(mAudit,mUserId,SelectMainLocationActivity.meMapServerId.get(list.get(position)),db)){
                    SelectedLocation selectedLocation = new SelectedLocation();
                    selectedLocation.setmStrMainLocationServerId(SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                    selectedLocation.setmStrAuditId(mAuditId);
                    selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                    int existing = funGetSelectedMainLocationCount(selectedLocation,db);

                    //int existing = db.get_existing_location_count(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                    if (a == existing) {
                        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                        TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                        RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                        RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
//                        mTxtMsg.setText("This will delete " + list.get(position) + " Folder and Sub folders including associated questions,Do you want to proceed ?");
                        mTxtMsg.setText(context.getString(R.string.mtextFile_this) + list.get(position) + context.getString(R.string.mtextFile_foldr_msg));
                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        mConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseHelper dbase = new DatabaseHelper(context);
                                //dbase.delete_tb_selected_main_location(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                funDeleteSubFolders(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,mUserId,dbase);
                                //dbase.delete_tb_location_sub_folder(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                //dbase.delete_tb_sub_folder_explation_list_all(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                funDeleteSubFolderLayer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,"3",dbase);
                                dbase.deleteAllQuestionAnswer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,PreferenceManager.getFormiiId(context));
                                dbase.deleteSubQuestionAnswer(mAuditId,PreferenceManager.getFormiiId(context));
                                if (a != 1) {

                                    SelectMainLocationActivity.updateCount(0,list.get(position));
                                    int aexis = Integer.parseInt(holder.count.getText().toString());
                                    if(holder.count.getText().toString().length()<=0 && holder.count.getVisibility()==View.GONE){
                                        holder.count.setVisibility(View.VISIBLE);
                                        aexis=0;
                                    }
                                    holder.count.setText(aexis - 1 + "");
                                    SelectMainLocationActivity.meMap.put(list.get(position), aexis - 1 + "");
                                } else {

                                    SelectedLocation selectedLocation = new SelectedLocation();
                                    selectedLocation.setmStrAuditId(mAudit);
                                    selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                                    selectedLocation.setmStrMainLocationServerId(SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                                    funDeleteSelectedMainLocation(selectedLocation,dbase);


                                    //dbase.delete_tb_selected_main_location(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                    funDeleteSubFolders(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,mUserId,dbase);
                                    //dbase.delete_tb_location_sub_folder(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                    funDeleteSubFolderLayer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,"3",dbase);
                                    //dbase.delete_tb_sub_folder_explation_list_all(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                                    //dbase.deleteAllQuestionAnswer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,PreferenceManager.getFormiiId(context));
                                    //dbase.delete_main_question_by_opration(mAudit,PreferenceManager.getFormiiId(context),"4",SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                                    SelectMainLocationActivity.getRemove(position,holder.text.getText().toString());
                                    if (SelectMainLocationActivity.getCount() == 0) {
                                        Audit audit = new Audit();
                                        audit.setmStatus("0");
                                        audit.setmAuditId(mAudit);
                                        funUpdateAudit(audit,"1",dbase);
                                        //dbase.delete_all_data_of_audit(mAudit, PreferenceManager.getFormiiId(context));
                                    }

                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();


                        //alert and delete from selected location and sub folder
                    }

                }else {
                    if (a != 1) {
                        SelectMainLocationActivity.updateCount(0,list.get(position));
                        int aexis = Integer.parseInt(holder.count.getText().toString());
                        if(holder.count.getText().toString().length()<=0 && holder.count.getVisibility()==View.GONE){
                            holder.count.setVisibility(View.VISIBLE);
                            aexis=0;
                        }
                        holder.count.setText(aexis - 1 + "");
                        SelectMainLocationActivity.meMap.put(list.get(position), aexis - 1 + "");
                    } else {
                        DatabaseHelper dbase = new DatabaseHelper(context);
                        SelectedLocation selectedLocation = new SelectedLocation();
                        selectedLocation.setmStrAuditId(mAudit);
                        selectedLocation.setmStrUserId(PreferenceManager.getFormiiId(context));
                        selectedLocation.setmStrMainLocationServerId(SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                        funDeleteSelectedMainLocation(selectedLocation,dbase);

                       // dbase.delete_tb_selected_main_location(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                        funDeleteSubFolders(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,mUserId,dbase);
                        //dbase.delete_tb_location_sub_folder(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                        funDeleteSubFolderLayer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,"3",db);
                        //dbase.delete_tb_sub_folder_explation_list_all(SelectMainLocationActivity.meMapLocalId.get(list.get(position)));
                        dbase.deleteAllQuestionAnswer(SelectMainLocationActivity.meMapServerId.get(list.get(position)),mAuditId,PreferenceManager.getFormiiId(context));
                        dbase.deleteSubQuestionAnswer(mAuditId,PreferenceManager.getFormiiId(context));
                        //dbase.delete_main_question_by_opration(mAudit,PreferenceManager.getFormiiId(context),"4",SelectMainLocationActivity.meMapServerId.get(list.get(position)));
                        SelectMainLocationActivity.getRemove(position,holder.text.getText().toString());
                        if (SelectMainLocationActivity.getCount() == 0) {
                            Audit audit = new Audit();
                            audit.setmStatus("0");
                            audit.setmAuditId(mAudit);
                            funUpdateAudit(audit,"1",dbase);
                            //dbase.delete_all_data_of_audit(mAudit, PreferenceManager.getFormiiId(context));
                        }

                    }

                }
            }
        });

        holder.frameLayout.setOnLongClickListener(this);
        holder.frameLayout.setOnDragListener(new DragListener(listener));
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

    DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
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
