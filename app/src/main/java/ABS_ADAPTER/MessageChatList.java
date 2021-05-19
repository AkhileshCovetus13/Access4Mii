package ABS_ADAPTER;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.covetus.audit.ActivityImageVideoView;
import com.covetus.audit.R;

import java.io.File;
import java.util.List;

import ABS_CUSTOM_VIEW.TextViewRegular;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.MessageChat;
import ABS_HELPER.PreferenceManager;

import static ABS_HELPER.CommonUtils.mChatImgUrl;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mStrChatFileDownloadPath;
import static ABS_HELPER.UploadHelper.isValid;

/**
 * Created by admin18 on 29/11/18.
 */

public class MessageChatList extends RecyclerView.Adapter<MessageChatList.ViewHolder> {
    List<MessageChat> mMsgChatArrayList;
    Activity mContext;
    File outputFile;


    public MessageChatList(Activity context, List<MessageChat> mMsgChatList) {
        mMsgChatArrayList = mMsgChatList;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return mMsgChatArrayList.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
       /* Check view type*/
        switch (viewType) {
            case MessageChat.TYPE_MESSAGE_RECEIVE:
                layout = R.layout.item_received_msg;
                break;
            case MessageChat.TYPE_MESSAGE_SENT:
                layout = R.layout.item_sent_msg;
                break;
            case MessageChat.TYPE_MEDIA_RECEIVE_MESSAGE:
                layout = R.layout.item_received_msg;
                break;
            case MessageChat.TYPE_MEDIA_SENT_MESSAGE:
                layout = R.layout.item_sent_msg;
                break;
            case MessageChat.TYPE_VIDEO_RECEIVE_MESSAGE:
                layout = R.layout.item_received_msg;
                break;
            case MessageChat.TYPE_VIDEO_SENT_MESSAGE:
                layout = R.layout.item_sent_msg;
                break;
            case MessageChat.TYPE_DOCUMENT_RECEIVE_MESSAGE:
                layout = R.layout.item_received_msg;
                break;
            case MessageChat.TYPE_DOCUMENT_SENT_MESSAGE:
                layout = R.layout.item_sent_msg;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final MessageChat messageChat = mMsgChatArrayList.get(position);
        /*load user image*/
        Glide.with(mContext).load(messageChat.getmImage()).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(viewHolder.mImageUser) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.mImageUser.setImageDrawable(circularBitmapDrawable);
            }
        });


        if (messageChat.getType() == 2 || messageChat.getType() == 1) {
            //image
            //for hiding nd viewing progessbar when img or video is uploading
            if (PreferenceManager.getFormiiProgreessView(mContext).equals("0")) {
                viewHolder.progressBar.setVisibility(View.GONE);
            } else {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }
            viewHolder.mImgBody.setVisibility(View.VISIBLE);
            String mFilePath = mStrChatFileDownloadPath + mContext.getString(R.string.mTextFile_mediaFolder) + messageChat.getMessage();
            File file = new File(mFilePath);
            if (file.exists()) {
                viewHolder.mImgPlay.setVisibility(View.GONE);
            } else {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.rounded_downloaded).asBitmap().placeholder(R.drawable.rounded_downloaded).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);
            }
            viewHolder.mTextMsgBody.setVisibility(View.GONE);
            viewHolder.mTextMsgTime.setText(messageChat.getmCreatedAt());
            if (messageChat.getMessage().contains(".png") || messageChat.getMessage().contains(".jpg") || messageChat.getMessage().contains(".jpeg")) {
                Glide.with(mContext).load(mChatImgUrl + messageChat.getMessage()).placeholder(R.mipmap.ic_pic_placeholder).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
            } else {
                //load locally when send
                try {
                    viewHolder.mImgPlay.setVisibility(View.GONE);
                    byte[] imageByteArray = Base64.decode(messageChat.getMessage(), Base64.DEFAULT);
                    Glide.with(mContext).load(imageByteArray).placeholder(R.mipmap.ic_pic_placeholder).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
                } catch (Exception e) {
                    e.toString();
                }
            }

            viewHolder.mImgBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCheckSignalStrength(mContext) == 2) {
                        launchUploadActivity("1", messageChat.getMessage(), messageChat.getType());
                    } else {
                        launchUploadActivity("5", messageChat.getMessage(), messageChat.getType());
                    }
                }
            });

        } else if (messageChat.getType() == 4 || messageChat.getType() == 3) {
            //video
            //for hiding nd viewing progessbar when img or video is uploading
            if (PreferenceManager.getFormiiProgreessView(mContext).equals("0")) {
                viewHolder.progressBar.setVisibility(View.GONE);
            } else {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }

            viewHolder.mImgBody.setVisibility(View.VISIBLE);
            viewHolder.mTextMsgBody.setVisibility(View.GONE);
            viewHolder.mTextMsgTime.setText(messageChat.getmCreatedAt());
            String mFilePath = mStrChatFileDownloadPath + mContext.getString(R.string.mTextFile_mediaFolder) + messageChat.getMessage();
            File file = new File(mFilePath);
            if (file.exists()) {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.ic_play).asBitmap().placeholder(R.drawable.ic_play).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);
            } else {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
              /*  Glide.with(mContext).load(R.drawable.rounded_downloaded).asBitmap().placeholder(R.drawable.rounded_downloaded).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);*/
            }
            String url = mChatImgUrl + messageChat.getMessage();
            if (isValid(url)) {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                if (messageChat.getMessage().contains(".mp4")) {
                    Glide.with(mContext).load(mChatImgUrl + messageChat.getMessage()).asBitmap().placeholder(R.mipmap.ic_pic_placeholder).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
                } else {
                    //load locally when send
                    Glide.with(mContext).load(Uri.fromFile(new File(messageChat.getMessage()))).asBitmap().placeholder(R.mipmap.ic_pic_placeholder).thumbnail(.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
                }
                viewHolder.mImgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCheckSignalStrength(mContext) == 2) {
                            launchUploadActivity("2", messageChat.getMessage(), messageChat.getType());
                        } else {
                            //load from locally
                            launchUploadActivity("6", messageChat.getMessage(), messageChat.getType());
                        }
                    }
                });
            } else {
                viewHolder.mImgPlay.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.ic_novideo).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
            }
        } else if (messageChat.getType() == 7 || messageChat.getType() == 6) {
            //document
            //for hiding nd viewing progessbar when img or video is uploading
            if (PreferenceManager.getFormiiProgreessView(mContext).equals("0")) {
                viewHolder.progressBar.setVisibility(View.GONE);
            } else {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }
            viewHolder.mImgBody.setVisibility(View.VISIBLE);
            viewHolder.mTextMsgBody.setVisibility(View.GONE);
            viewHolder.mTextMsgTime.setText(messageChat.getmCreatedAt());
            String mFilePath = mStrChatFileDownloadPath + mContext.getString(R.string.mTextFile_filefolder) + messageChat.getMessage();
            File file = new File(mFilePath);
            if (file.exists()) {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.ic_forward).asBitmap().placeholder(R.drawable.ic_forward).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);
            } else {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
               /* Glide.with(mContext).load(R.drawable.rounded_downloaded).asBitmap().placeholder(R.drawable.rounded_downloaded).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);*/
            }
            String url = mChatImgUrl + messageChat.getMessage();
            if (isValid(url)) {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.mipmap.ic_doc_placeholder).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
                viewHolder.mImgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCheckSignalStrength(mContext) == 2) {
                            launchUploadActivity("3", messageChat.getMessage(), messageChat.getType());
                        } else {
                            //load from locally
                            launchUploadActivity("7", messageChat.getMessage(), messageChat.getType());
                        }
                    }
                });
            } else {
                viewHolder.mImgPlay.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.ic_forward).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgPlay);
                Glide.with(mContext).load(R.mipmap.ic_doc_placeholder).thumbnail(.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mImgBody);
                viewHolder.mImgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //load from locally
                        launchUploadActivity("4", messageChat.getMessage(), messageChat.getType());
                    }
                });
            }
        } else {
            //text
            viewHolder.mImgBody.setVisibility(View.GONE);
            viewHolder.mImgPlay.setVisibility(View.GONE);
            viewHolder.mTextMsgBody.setVisibility(View.VISIBLE);
            viewHolder.mTextMsgBody.setText(messageChat.getMessage());
            viewHolder.mTextMsgTime.setText(messageChat.getmCreatedAt());
            System.out.println("<><><>### "+messageChat.getmCreatedAt());
        }
    }


    @Override
    public int getItemCount() {
        return mMsgChatArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextViewSemiBold mTextMsgBody;
        ImageView mImgBody, mImgPlay, mImageUser;
        ProgressBar progressBar;
        TextViewRegular mTextMsgTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextMsgBody = itemView.findViewById(R.id.mTextMsgBody);
            mTextMsgTime = itemView.findViewById(R.id.mTextMsgTime);
            mImgBody = itemView.findViewById(R.id.mImgBody);
            mImgPlay = itemView.findViewById(R.id.mImgPlay);
            mImageUser = itemView.findViewById(R.id.mImageUser);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void launchUploadActivity(String isImage, String message, int mType) {
        try {
            Intent i = new Intent(mContext, ActivityImageVideoView.class);
            i.putExtra("filePath", message);
            i.putExtra("isImage", isImage);
            i.putExtra("type", mType);
            mContext.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}