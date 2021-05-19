/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.covetus.audit.ActivityReorderSelectedLocationList;
import com.covetus.audit.ActivitySelectCountryStandard;
import com.covetus.audit.AuditListHistory;
import com.covetus.audit.FragmentDraft;
import com.covetus.audit.R;

import java.text.ParseException;
import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewBold;
import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_GET_SET.AuditQuestionAnswer;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.PreferenceManager;

import static Modal.AuditListModal.funGetActiveAudit;
import static Modal.AuditListModal.funGetActiveAuditByUser;
import static Modal.AuditListModal.funUpdateAudit;
import static com.covetus.audit.ActivityTabHostMain.mTabHost;
import static com.covetus.audit.FragmentDraft.mReorderStatus;


public class AudittList extends BaseAdapter {

    private ArrayList<Audit> mListItems = new ArrayList<>();
    Activity context;

    public AudittList(Activity context, ArrayList<Audit> mListItems) {
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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_audit_list, null);
            holder = new ViewHolder();
            holder.mTxtAuditDate = convertView.findViewById(R.id.mTxtAuditDate);
            holder.mTxtAuditTitle = convertView.findViewById(R.id.mTxtAuditTitle);
            holder.mTxtAgentName = convertView.findViewById(R.id.mTxtAgentName);
            holder.mLayoutViewAudit = convertView.findViewById(R.id.mLayoutViewAudit);
            holder.mLayoutMain = convertView.findViewById(R.id.mLayoutMain);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Audit audit = mListItems.get(position);
        try {
            holder.mTxtAuditDate.setText(CommonUtils.mTogetDay(audit.getmDueDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mTxtAuditTitle.setText(audit.getmTitle());
        holder.mTxtAgentName.setText(audit.getmAssignBy());
        if (audit.getmStatus().equals("0")) {
            holder.mLayoutMain.setBackgroundColor(Color.parseColor("#FE0000"));
        } else {
            holder.mLayoutMain.setBackgroundColor(Color.parseColor("#FEB95C"));
        }
        holder.mLayoutViewAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
                String mAuditId = audit.getmAuditId();
                if(funGetActiveAuditByUser("1", PreferenceManager.getFormiiId(context), db) > 0){
                   //draft
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                    dialog.setCancelable(false);
                    RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                    RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    mTxtMsg.setText("You cannot download another audit data until you sync previous one");
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTabHost.setCurrentTab(2);
                            dialog.cancel();
                        }
                    });
                    dialog.show();


                //CommonUtils.mShowAlert(context.getString(R.string.mTextFile_error_sync), context);


                }else if(funGetActiveAuditByUser("2", PreferenceManager.getFormiiId(context), db) > 0){

                    //history


                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_warning_popup_two_button);
                    dialog.setCancelable(false);
                    RelativeLayout mLayoutYes = dialog.findViewById(R.id.mLayoutYes);
                    RelativeLayout mLayoutNo = dialog.findViewById(R.id.mLayoutNo);
                    TextViewRegular mTxtMsg = dialog.findViewById(R.id.mTxtMsg);
                    mTxtMsg.setText("Please do final sync before accepting the new Audit, go to History and click on Final Sync");
                    mLayoutNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    mLayoutYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AuditListHistory.class);
                            context.startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    dialog.show();


                    CommonUtils.mShowAlert("You can not download another audit data until you sync previous one", context);



                }else {
                    db.delete_all_data_of_audit(mAuditId, PreferenceManager.getFormiiId(context));
                    Intent intent = new Intent(context, ActivitySelectCountryStandard.class);
                    intent.putExtra("mAuditId", mAuditId);
                    context.startActivity(intent);
                }

            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextViewSemiBold mTxtAuditDate;
        TextViewSemiBold mTxtAuditTitle;
        TextViewSemiBold mTxtAgentName;
        ImageView mLayoutViewAudit;
        RelativeLayout mLayoutMain;
    }
}