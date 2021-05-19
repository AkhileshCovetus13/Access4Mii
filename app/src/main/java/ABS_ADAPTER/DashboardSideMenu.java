/**
 * Created by admin1 on 27/2/18.
 */
package ABS_ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.covetus.audit.R;

import java.util.ArrayList;
import ABS_GET_SET.SideMenu;


public class DashboardSideMenu extends BaseAdapter {

    private ArrayList<SideMenu> mListItems = new ArrayList<>();
    Context context;
    int selectedPos = -1;


    public DashboardSideMenu(Context context, ArrayList<SideMenu> mListItems) {
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
            convertView = mInflater.inflate(R.layout.item_side_menu, null);
            holder = new ViewHolder();
            holder.mTxtMenu = convertView.findViewById(R.id.mTxtMenu);
            holder.mLayoutMain = convertView.findViewById(R.id.mLayoutMain);
            holder.mImgMenu = convertView.findViewById(R.id.mImgMenu);
            SideMenu sideMenu = mListItems.get(position);
            holder.mTxtMenu.setText(sideMenu.getmSideMenuTitle());
            holder.mImgMenu.setImageResource(sideMenu.getmSideMenuIconActive());
            if (position == selectedPos) {
                holder.mLayoutMain.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.mLayoutMain.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout mLayoutMain;
        ImageView mImgMenu;
        TextView mTxtMenu;
    }

    public void mSetSelection(int i){
    selectedPos = i;
    notifyDataSetChanged();
    }
}