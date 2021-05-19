package ABS_ADAPTER;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.covetus.audit.R;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewSemiBold;

/**
 * Created by admin18 on 28/8/18.
 */

public class DocumentList extends RecyclerView.Adapter<DocumentList.MyViewHolder> {

    private ArrayList<String> mListItems = new ArrayList<>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayoutDoc;
        TextViewSemiBold mTextDoc;
        ImageView mFileImg;

        public MyViewHolder(View convertView) {
            super(convertView);
            mLayoutDoc = convertView.findViewById(R.id.mLayoutDoc);
            mTextDoc = convertView.findViewById(R.id.mTextDoc);
            mFileImg = convertView.findViewById(R.id.mFileImg);
        }
    }

    public DocumentList(Context mContext, ArrayList<String> mListItems) {
        this.context = mContext;
        this.mListItems = mListItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filetype_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTextDoc.setText(mListItems.get(position).toString());
        String mFileType = mListItems.get(position).toString();
        /*file type check*/
        if (mFileType.contains(".pdf")) {
            Glide.with(context).load(R.drawable.ic_pdf).into(holder.mFileImg);
        } else if (mFileType.contains(".docx") || mFileType.contains(".doc")) {
            Glide.with(context).load(R.drawable.ic_doc).into(holder.mFileImg);
        } else if (mFileType.contains(".png") || mFileType.contains(".jpg") || mFileType.contains(".jpeg")) {
            Glide.with(context).load(R.drawable.ic_jpg).into(holder.mFileImg);
        } else if (mFileType.contains(".xls") || mFileType.contains(".xlsx")) {
            Glide.with(context).load(R.drawable.ic_xls).into(holder.mFileImg);
        }
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }
}