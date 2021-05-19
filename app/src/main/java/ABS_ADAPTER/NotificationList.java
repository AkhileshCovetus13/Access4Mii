/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.covetus.audit.R;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Notification;


public class NotificationList extends BaseAdapter {

    private ArrayList<Notification> mListItems = new ArrayList<>();
    Context context;


    public NotificationList(Context context, ArrayList<Notification> mListItems) {
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
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_notification_list, null);
            holder = new ViewHolder();
            holder.mImgUserProfile = convertView.findViewById(R.id.mImgUserProfile);
            holder.mTxtTitle = convertView.findViewById(R.id.mTxtTitle);
            holder.mTxtTime = convertView.findViewById(R.id.mTxtTime);
            holder.mTxtDay = convertView.findViewById(R.id.mTxtDay);
            holder.mTxtCount = convertView.findViewById(R.id.mTxtCount);
            holder.mTxtDesc = convertView.findViewById(R.id.mTxtDesc);
            Notification notification = mListItems.get(position);

            Glide.with(context).load(notification.getmStrNotificationImage()).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(holder.mImgUserProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.mImgUserProfile.setImageDrawable(circularBitmapDrawable);
                }
            });


            holder.mTxtTitle.setText(notification.getmStrNotificationTitle());
            holder.mTxtTime.setText(notification.getmStrNotificationTime());
            holder.mTxtDay.setText(notification.getmStrNotificationDay());
            holder.mTxtCount.setText(notification.getmStrNotificationCount());
            holder.mTxtDesc.setText(notification.getmStrNotificationDecs());
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView mImgUserProfile;
        TextViewSemiBold mTxtTitle;
        TextViewSemiBold mTxtTime;
        TextViewRegular mTxtDay;
        TextViewSemiBold mTxtCount;
        TextViewRegular mTxtDesc;
    }
}