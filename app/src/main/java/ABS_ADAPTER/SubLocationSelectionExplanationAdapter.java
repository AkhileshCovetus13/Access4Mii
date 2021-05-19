/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.covetus.audit.ActivityQuestionForm;
import com.covetus.audit.ActivitySubLocationSelectorExplaintion;
import com.covetus.audit.R;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.EditTextSemiBold;
import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.SubLocationExplation;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;

import static Modal.SubLocationLayer.funUpdateSubLocationLayer;


public class SubLocationSelectionExplanationAdapter extends BaseAdapter {

    private ArrayList<SubLocationExplation> mListItems = new ArrayList<>();
    Activity context;


    public SubLocationSelectionExplanationAdapter(Activity context, ArrayList<SubLocationExplation> mListItems) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_subloc_selector_explaination, null);
            holder = new ViewHolder();
            holder.mTxtTitle = convertView.findViewById(R.id.mTxtTitle);
            holder.mTxtDecs = convertView.findViewById(R.id.mTxtDecs);
            holder.mTextArchive = convertView.findViewById(R.id.mTextArchive);
            holder.mImgUpdateData = convertView.findViewById(R.id.mImgUpdateData);
            holder.mImgDelete = convertView.findViewById(R.id.mImgDelete);
            holder.mLayoutView = convertView.findViewById(R.id.mLayoutView);
            holder.mLayoutArchive = convertView.findViewById(R.id.mLayoutArchive);
            holder.mLayoutInComplete = convertView.findViewById(R.id.mLayoutInComplete);
            holder.mLayoutInProgress = convertView.findViewById(R.id.mLayoutInProgress);
            holder.mLayoutComplete = convertView.findViewById(R.id.mLayoutComplete);
            holder.mIncompleteBar = convertView.findViewById(R.id.mIncompleteBar);
            holder.mInProgressBar = convertView.findViewById(R.id.mInProgressBar);
            holder.mCompleteProgressBar = convertView.findViewById(R.id.mCompleteProgressBar);
            holder.mTextInComplete = convertView.findViewById(R.id.mTextInComplete);
            holder.mTextInProgress = convertView.findViewById(R.id.mTextInProgress);
            holder.mTextComplete = convertView.findViewById(R.id.mTextComplete);
            convertView.setTag(holder);
        } else {
            holder = (SubLocationSelectionExplanationAdapter.ViewHolder) convertView.getTag();
        }

        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final SubLocationExplation layerList = mListItems.get(position);
        holder.mTxtTitle.setText(layerList.getmStrExplanationTitle());
        holder.mTxtDecs.setText(layerList.getmStrExplanationDesc());

        int mGetPrgress = GetPercent(databaseHelper, layerList.getmStrAuditId(), layerList.getmStrUserId(), layerList.getmStrMainLocationServer(), layerList.getmStrSubLocationServer(), layerList.getmStrSubLocationServer(), layerList.getmStrId());
        if (mGetPrgress == 0) {
            holder.mLayoutInComplete.setVisibility(View.VISIBLE);
            holder.mLayoutInProgress.setVisibility(View.GONE);
            holder.mLayoutComplete.setVisibility(View.GONE);
            holder.mTextInComplete.setText(mGetPrgress + "");
            holder.mIncompleteBar.setProgress(mGetPrgress);
        } else if (mGetPrgress == 100) {
            holder.mLayoutInComplete.setVisibility(View.GONE);
            holder.mLayoutInProgress.setVisibility(View.GONE);
            holder.mLayoutComplete.setVisibility(View.VISIBLE);
            holder.mTextComplete.setText(mGetPrgress + " %");
            holder.mCompleteProgressBar.setProgress(mGetPrgress);
        } else {
            holder.mLayoutInComplete.setVisibility(View.GONE);
            holder.mLayoutComplete.setVisibility(View.GONE);
            holder.mLayoutInProgress.setVisibility(View.VISIBLE);
            holder.mTextInProgress.setText(mGetPrgress + " %");
            holder.mInProgressBar.setProgress(mGetPrgress);
        }


        if (layerList.getmStrExplanationArchive().equals("0")) {
            holder.mImgDelete.setImageResource(R.mipmap.ic_cancel_active);
            holder.mImgUpdateData.setImageResource(R.mipmap.ic_update_sub_group);
            holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_view_button);
            holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
            holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.black));
            holder.mTextArchive.setText(context.getString(R.string.mTextFile_archive));

            holder.mLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityQuestionForm.class);
                    intent.putExtra("mAuditId", layerList.getmStrAuditId());
                    intent.putExtra("mStrMainLocationServer", layerList.getmStrMainLocationServer());
                    intent.putExtra("mSubLocationID", layerList.getmStrSubLocationServer());
                    intent.putExtra("mSubLocationTitle", layerList.getmStrSubLocationTitle());
                    intent.putExtra("mExplanationID", layerList.getmStrId());
                    intent.putExtra("mExplanationTitle", layerList.getmStrExplanationTitle());
                    intent.putExtra("mExplanationDesc", layerList.getmStrExplanationDesc());
                    intent.putExtra("mExplanationImage", layerList.getmStrExplanationImage());
                    intent.putExtra("mLayerID", layerList.getmStrLayerId());
                    intent.putExtra("mLayerTitle", layerList.getmStrLayerTitle());
                    intent.putExtra("mLayerDesc", layerList.getmStrLayerDesc());
                    context.startActivity(intent);

                }
            });

            holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_confirmation_delete_data_exp);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    RelativeLayout mConfirm = dialog.findViewById(R.id.mConfirm);
                    RelativeLayout mCancel = dialog.findViewById(R.id.mCancel);
                    mTxtMsg.setText(context.getString(R.string.mtextFile_this) + " " + layerList.getmStrLayerTitle() + context.getString(R.string.mtextFile_question_msg));

                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivitySubLocationSelectorExplaintion.RemoveView(layerList.getmStrId());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
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
                            SubLocationExplation subLocationExplation = new SubLocationExplation();
                            subLocationExplation.setmStrExplanationTitle(mTitle);
                            subLocationExplation.setmStrExplanationDesc(mDescription);
                            subLocationExplation.setmStrId(layerList.getmStrId());
                            subLocationExplation.setmStrAuditId(layerList.getmStrAuditId());
                            subLocationExplation.setmStrUserId(layerList.getmStrUserId());
                            subLocationExplation.setmStrMainLocationServer(layerList.getmStrMainLocationServer());
                            funUpdateSubLocationLayer(subLocationExplation, "4", databaseHelper);
                            databaseHelper.update_question_answer_table_detail(layerList.getmStrAuditId(), layerList.getmStrUserId(), layerList.getmStrId(), "3", mTitle, mDescription);
                            ActivitySubLocationSelectorExplaintion.updateView(position);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        } else {
            // code you can right here
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

                if (holder.mTextArchive.getText().toString().equals(context.getString(R.string.mTextFile_error_archive))) {
                    holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
                    holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_green);
                    holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.white));
                    holder.mTextArchive.setText(R.string.mTextFile_error_reinitiate);
                    SubLocationExplation subLocationExplation = new SubLocationExplation();
                    subLocationExplation.setmStrExplanationArchive("1");
                    subLocationExplation.setmStrId(layerList.getmStrId());
                    subLocationExplation.setmStrMainLocationServer(layerList.getmStrMainLocationServer());
                    subLocationExplation.setmStrSubLocationServer(layerList.getmStrSubLocationServer());
                    subLocationExplation.setmStrAuditId(layerList.getmStrAuditId());
                    subLocationExplation.setmStrUserId(layerList.getmStrUserId());
                    funUpdateSubLocationLayer(subLocationExplation, "1", databaseHelper);
                    ActivitySubLocationSelectorExplaintion.updateView(position);
                } else {
                    holder.mLayoutView.setBackgroundResource(R.drawable.rounded_corner_view_button);
                    holder.mLayoutArchive.setBackgroundResource(R.drawable.rounded_corner_layer_gray);
                    holder.mTextArchive.setTextColor(context.getResources().getColor(R.color.black));
                    holder.mTextArchive.setText(R.string.mTextFile_error_archive);
                    SubLocationExplation subLocationExplation = new SubLocationExplation();
                    subLocationExplation.setmStrExplanationArchive("0");
                    subLocationExplation.setmStrId(layerList.getmStrId());
                    subLocationExplation.setmStrMainLocationServer(layerList.getmStrMainLocationServer());
                    subLocationExplation.setmStrSubLocationServer(layerList.getmStrSubLocationServer());
                    subLocationExplation.setmStrAuditId(layerList.getmStrAuditId());
                    subLocationExplation.setmStrUserId(layerList.getmStrUserId());
                    funUpdateSubLocationLayer(subLocationExplation, "1", databaseHelper);
                    ActivitySubLocationSelectorExplaintion.updateView(position);
                }


            }
        });

        return convertView;
    }

    public int GetPercent(DatabaseHelper databaseHelper, String mAuditId, String mUserId, String mStrMainLocationId, String mStrSubLocationId, String mStrSubLocationServerId, String mStrExplanationId) {
        int mIntAllMainQuestion = databaseHelper.getCountOfAllSubLocationQUestion(mAuditId, mUserId, mStrMainLocationId, mStrSubLocationId, "1");
        System.out.println("<><>##mIntAllMainQuestion "+mIntAllMainQuestion);

        int mIntAllInspectorQuestion = databaseHelper.getCountOfAllSubLocationInspectorQuestion(mAuditId, mUserId, mStrMainLocationId, mStrSubLocationId);
        System.out.println("<><>##mIntAllInspectorQuestion "+mIntAllInspectorQuestion);
        mIntAllMainQuestion = mIntAllMainQuestion+mIntAllInspectorQuestion;
        int mIntAllGivenQuestion = databaseHelper.getCountAllGivenAns(mAuditId, mUserId, mStrSubLocationServerId, mStrExplanationId, mStrMainLocationId,"1");
        System.out.println("<><>##mIntAllGivenQuestion "+mIntAllGivenQuestion);
        int mIntAllGivenInspectorQuestion = databaseHelper.getCountAllGivenInspectorAns(mAuditId, mUserId, mStrSubLocationServerId, mStrExplanationId, mStrMainLocationId,"1");
        System.out.println("<><>##mIntAllGivenInspectorQuestion "+mIntAllGivenInspectorQuestion);
        mIntAllGivenQuestion = mIntAllGivenQuestion+mIntAllGivenInspectorQuestion;
        double mGetPercent = (double) mIntAllGivenQuestion / (double) mIntAllMainQuestion;
        mGetPercent = 100 * mGetPercent;
        return (int) mGetPercent;
    }

    private class ViewHolder {
        TextViewSemiBold mTxtTitle;
        TextViewSemiBold mTxtDecs;
        TextViewBold mTextArchive;
        ImageView mImgUpdateData, mImgDelete;
        RelativeLayout mLayoutView;
        RelativeLayout mLayoutArchive;
        LinearLayout mLayoutInProgress, mLayoutComplete, mLayoutInComplete;
        ProgressBar mIncompleteBar, mInProgressBar, mCompleteProgressBar;
        TextViewBold mTextInComplete, mTextInProgress, mTextComplete;
    }
}