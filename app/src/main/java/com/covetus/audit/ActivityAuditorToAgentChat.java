package com.covetus.audit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ABS_ADAPTER.MessageChatList;
import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.MessageChat;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.FloatingView;
import ABS_HELPER.PreferenceManager;
import ABS_HELPER.VolleyMultipartRequest;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;
import ly.img.android.ui.utilities.PermissionRequest;

import static ABS_HELPER.CommonUtils.CHAT_SERVER_NEW_URL;
import static ABS_HELPER.CommonUtils.FOLDER;
import static ABS_HELPER.CommonUtils.closeKeyBoard;
import static ABS_HELPER.CommonUtils.getCallNumber;
import static ABS_HELPER.CommonUtils.getFormattedDate;
import static ABS_HELPER.CommonUtils.getOpenEmail;
import static ABS_HELPER.CommonUtils.hasPermissions;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.mStrBaseUrl;
import static ABS_HELPER.CommonUtils.mStrBaseUrlImage;
import static ABS_HELPER.CommonUtils.mStrChatFileDownloadPath;
import static ABS_HELPER.CommonUtils.showPDialog;
import static ABS_HELPER.UploadHelper.copyFile;
import static ABS_HELPER.UploadHelper.getBase64ImageString;
import static ABS_HELPER.UploadHelper.getFileDataFromDrawable;
import static ABS_HELPER.UploadHelper.getFileExt;
import static ABS_HELPER.UploadHelper.getMimeTypefromfilepath;
import static ABS_HELPER.UploadHelper.getPath;

public class ActivityAuditorToAgentChat extends Activity implements PermissionRequest.Response {

    Socket mSocket;
    Boolean isConnected = true;
    Calendar mCalendar;
    DatabaseHelper db;
    String mStrUserId, mStrMsgFrom, mUsername, mCurrentDate, mStrUserImage, mStrMyImage, mStrUserEmail, mStrUserPhone;
    String mText_Type = "1", mImage_Type = "2", mDocument_Type = "4", mVideo_Type = "3";
    int mChatRoomId;
    int DOCUMENT = 103, textlength;
    public static int CAMERA_PREVIEW_RESULT = 1;
    Uri mVideoURI;

    MessageChatList messageChatAdapter;
    List<MessageChat> mMsgChatArrayList = new ArrayList<MessageChat>();
    List<MessageChat> mMsgChatArrayListOffline = new ArrayList<MessageChat>();

    @BindView(R.id.mMsgRecycleList)
    RecyclerView mMsgRecycleList;
    @BindView(R.id.mEditChatBox)
    EditText mEditChatBox;
    @BindView(R.id.mTextBtnSend)
    ImageView mTextBtnSend;
    @BindView(R.id.mImageUser)
    ImageView mImageUser;
    @BindView(R.id.mTextUserName)
    TextViewSemiBold mTextUserName;
    @BindView(R.id.mTextUserType)
    TextViewSemiBold mTextUserType;
    @BindView(R.id.mImgClose)
    ImageView mImgClose;
    @BindView(R.id.mImgMenu)
    ImageView mImgMenu;

    /*permission list*/
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
    };

    /*click for going back*/
    @OnClick(R.id.mImgClose)
    public void mGoBack() {

        closeKeyBoard(ActivityAuditorToAgentChat.this);
        finish();
    }


    /*click for going to menu*/
    @OnClick(R.id.mImgMenu)
    public void mGoToMenu() {
        PopupMenu popup = new PopupMenu(ActivityAuditorToAgentChat.this, mImgMenu);
        popup.getMenuInflater().inflate(R.menu.chat_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mMenuEmail:
                        getOpenEmail(ActivityAuditorToAgentChat.this, mStrUserEmail);
                        return true;
                    case R.id.mMenuCall:
                        getCallNumber(ActivityAuditorToAgentChat.this, mStrUserPhone);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    /* Press For Send Massage */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.mTextBtnSend)
    void mFunMsgSend() {
        if (mEditChatBox.getText().toString().equals("")) {
            mTextBtnSend.setImageResource(R.drawable.ic_send);//off
        } else {
            mTextBtnSend.setImageResource(R.drawable.ic_send);//on
            try {
                if (mCheckSignalStrength(ActivityAuditorToAgentChat.this) == 2) {
                    System.out.println("######<><><mEditChatBox" + mEditChatBox.getText().toString());
                    sendText();
                } else {
                    mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityAuditorToAgentChat.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* Press For Send Massage */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.mAttachmentBtn)
    void mFunMsgAttachment() {
        if (mCheckSignalStrength(ActivityAuditorToAgentChat.this) == 2) {
            openDialog();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityAuditorToAgentChat.this);
        }
    }

    private void openDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView;
        inflatedView = layoutInflater.inflate(R.layout.dialog_attachment, null, false);
        LinearLayout layoutCamera, layoutDoc;
        layoutCamera = inflatedView.findViewById(R.id.layoutCamera);
        layoutDoc = inflatedView.findViewById(R.id.layoutDoc);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CameraPreviewIntent(ActivityAuditorToAgentChat.this)
                        .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                        .setExportPrefix(getString(R.string.mTextFile_imagename))
                        .setEditorIntent(
                                new PhotoEditorIntent(ActivityAuditorToAgentChat.this)
                                        .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                        .setExportPrefix(getString(R.string.mTextFile_filename))
                                        .destroySourceAfterSave(true)
                        )
                        .startActivityForResult(CAMERA_PREVIEW_RESULT);
                FloatingView.dismissWindow();
            }
        });

        layoutDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, DOCUMENT);
                FloatingView.dismissWindow();
            }
        });
        FloatingView.onShowPopup(this, inflatedView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        db = new DatabaseHelper(getApplicationContext());
        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "0");

        /*for downloading data we have to use this*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

         /*permission check*/
        if (!hasPermissions(ActivityAuditorToAgentChat.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(ActivityAuditorToAgentChat.this, PERMISSIONS, PERMISSION_ALL);
        }

        Bundle bundle = getIntent().getExtras();
        mChatRoomId = bundle.getInt("mChatRoomId");
        mStrMsgFrom = bundle.getString("mChatUserId");
        mStrUserImage = bundle.getString("mChatUserPhoto");
        mStrUserEmail = bundle.getString("mChatUserEmail");
        mStrUserPhone = bundle.getString("mChatUserPhone");
        mTextUserName.setText(bundle.getString("mChatUserName"));
        mTextUserType.setText(bundle.getString("mChatUserRole"));
        Glide.with(getApplicationContext()).load(bundle.getString("mChatUserPhoto")).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImageUser) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImageUser.setImageDrawable(circularBitmapDrawable);
            }
        });

        mStrUserId = PreferenceManager.getFormiiId(ActivityAuditorToAgentChat.this);
        mStrMyImage = mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(ActivityAuditorToAgentChat.this);
        mUsername = mStrUserId;

        mCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mCurrentDate = df.format(mCalendar.getTime());

        messageChatAdapter = new MessageChatList(ActivityAuditorToAgentChat.this, mMsgChatArrayList);
        mMsgRecycleList.setLayoutManager(new LinearLayoutManager(ActivityAuditorToAgentChat.this));
        mMsgRecycleList.setAdapter(messageChatAdapter);
        if (mCheckSignalStrength(ActivityAuditorToAgentChat.this) == 2) {
            showPDialog(ActivityAuditorToAgentChat.this);
            try {
                mSocket = IO.socket(CHAT_SERVER_NEW_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            /* Connection by Socket to Server  and Create chat room */
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("chat agent auditor message", onNewMessage);
            mSocket.on("get agent auditor past chats", chatHistory);
            mSocket.connect();
            mSocket.emit("load id", mStrUserId);
            mSocket.emit("get agent auditor past chats", mStrMsgFrom, mStrUserId, 1);
            mEditChatBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textlength = mEditChatBox.getText().length();
                    if (null == mUsername) return;
                    if (!mSocket.connected()) return;
                    if (textlength == 0) {
                        mTextBtnSend.setImageResource(R.drawable.ic_send);//off
                    } else {
                        mTextBtnSend.setImageResource(R.drawable.ic_send);//on
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
             /* database data into list */
            mMsgChatArrayListOffline = db.get_all_tb_chat_msg_list(mStrMsgFrom);
            if (mMsgChatArrayListOffline.size() > 0) {
                for (int i = 0; i < mMsgChatArrayListOffline.size(); i++) {
                    MessageChat messageChat = mMsgChatArrayListOffline.get(i);
                    int msgType = messageChat.getType();
                    if (msgType == 1) {
                        addMessageOffline(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                    } else if (msgType == 2) {
                        addImageOffline(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                    } else if (msgType == 4) {
                        addDocumentOffline(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                    } else if (msgType == 3) {
                        addVideoOffline(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                    }
                }
                messageChatAdapter.notifyDataSetChanged();
                scrollToBottom();

            } else {
                CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found), ActivityAuditorToAgentChat.this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mCheckSignalStrength(ActivityAuditorToAgentChat.this) == 2) {
            /* Disconnect Connection */
                mSocket.disconnect();
                mSocket.off(Socket.EVENT_CONNECT, onConnect);
                mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.off("chat agent auditor message", onNewMessage);
                mSocket.off("get agent auditor past chats", chatHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* add massage to list */
    private void addMessageOffline(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MESSAGE_SENT).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MESSAGE_RECEIVE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
        }
    }

    private void addImageOffline(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MEDIA_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MEDIA_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
        }
    }

    private void addVideoOffline(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_VIDEO_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_VIDEO_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
        }
    }

    private void addDocumentOffline(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_DOCUMENT_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_DOCUMENT_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
        }
    }

    private void addMessage(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MESSAGE_SENT).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MESSAGE_RECEIVE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        }
        scrollToBottom();
    }

    /* add Image to list */
    private void addImage(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MEDIA_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_MEDIA_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        }
        scrollToBottom();
    }

    /* add Video to list */
    private void addVideo(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_VIDEO_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_VIDEO_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        }
        scrollToBottom();
    }

    private void addDocument(String mStrFromId, String message, String messageTime) {
        if (mStrUserId.equals(mStrFromId)) {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_DOCUMENT_SENT_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrMyImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        } else {
            mMsgChatArrayList.add(new MessageChat.Builder(MessageChat.TYPE_DOCUMENT_RECEIVE_MESSAGE).username(mStrFromId).message(message).createdAt(messageTime).image(mStrUserImage).build());
            messageChatAdapter.notifyItemInserted(mMsgChatArrayList.size() - 1);
        }
        scrollToBottom();
    }

    /* Send massage by soket */
    private void sendText() throws JSONException {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;
        String message = mEditChatBox.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mEditChatBox.requestFocus();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yname", mStrUserId);
        jsonObject.put("msg", message);
        jsonObject.put("from_name", PreferenceManager.getFormiiFullName(ActivityAuditorToAgentChat.this));
        jsonObject.put("to_name", mTextUserName.getText().toString());
        jsonObject.put("type", mText_Type);
        mSocket.emit("chat agent auditor message", mStrMsgFrom, jsonObject);
        mEditChatBox.setText("");
        System.out.println("####<><><>sendText" + jsonObject.toString());
        addMessage(mUsername, message, getFormattedDate(mCurrentDate));
    }

    private void sendImg(String message) throws JSONException {
        System.out.println("<><><>message" + message);
        if (null == mUsername) return;
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yname", mStrUserId);
        jsonObject.put("msg", message);
        jsonObject.put("from_name", PreferenceManager.getFormiiFullName(ActivityAuditorToAgentChat.this));
        jsonObject.put("to_name", mTextUserName.getText().toString());
        jsonObject.put("type", mImage_Type);//media type 1-text,file-2
        mSocket.emit("chat agent auditor message", mStrMsgFrom, jsonObject);
        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "0");
        System.out.println("####<><><>sendImg" + jsonObject.toString());
//        addImage(mUsername, message, mCurrentDate);
    }

    private void sendVideo(String message) throws JSONException {
        System.out.println("<><><>message" + message);
        if (null == mUsername) return;
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yname", mStrUserId);
        jsonObject.put("msg", message);
        jsonObject.put("from_name", PreferenceManager.getFormiiFullName(ActivityAuditorToAgentChat.this));
        jsonObject.put("to_name", mTextUserName.getText().toString());
        jsonObject.put("type", mVideo_Type);//media type 1-text,file-2
        mSocket.emit("chat agent auditor message", mStrMsgFrom, jsonObject);
        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "0");
        System.out.println("<><><>sendVideo" + jsonObject.toString());
//        addVideo(mUsername, message, mCurrentDate);
    }

    private void sendDocument(String message) throws JSONException {
        System.out.println("<><><>message" + message);
        if (null == mUsername) return;
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yname", mStrUserId);
        jsonObject.put("msg", message);
        jsonObject.put("from_name", PreferenceManager.getFormiiFullName(ActivityAuditorToAgentChat.this));
        jsonObject.put("to_name", mTextUserName.getText().toString());
        jsonObject.put("type", mDocument_Type);
        mSocket.emit("chat agent auditor message", mStrMsgFrom, jsonObject);
        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "0");
        System.out.println("<><><>sendDocument" + jsonObject.toString());
//        addDocument(mUsername, message, mCurrentDate);
    }

    private void scrollToBottom() {
        mMsgRecycleList.scrollToPosition(messageChatAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isConnected) {
                            if (null != mUsername)
                                mSocket.emit("load id", mUsername);
                            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                            isConnected = true;
                        }
                    } catch (WebsocketNotConnectedException ex) {
                        isConnected = false;
                        System.out.println("<>disconnected");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("<>disconnected");
                        isConnected = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hidePDialog();
                    System.out.println("<>Failed to connect");
                }
            });
        }
    };

    /* Socket Listener handel real time coming massage */
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "0");
                        String data = args[1].toString();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray mArrayChat = jsonObject.getJSONArray("response");
                        System.out.println("<><><>onNewMessage.." + mArrayChat.toString());
                        if (mArrayChat.length() > 0) {
                            String mStrFromId = "";
                            for (int i = 0; i < mArrayChat.length(); i++) {
                                MessageChat messageChat = new MessageChat();
                                JSONObject mArrayChatJSONObject = mArrayChat.getJSONObject(i);
                                messageChat.setmChatRoomId(mChatRoomId);
                                messageChat.setmChatUserId(mStrMsgFrom);
                                messageChat.setmUserId(PreferenceManager.getFormiiId(ActivityAuditorToAgentChat.this));
                                mStrFromId = mArrayChatJSONObject.getString("from");
                                messageChat.setmUserMsgFrom(mArrayChatJSONObject.getString("from"));
                                messageChat.setmMessage(mArrayChatJSONObject.getString("msg"));
                                messageChat.setmCreatedAt(mArrayChatJSONObject.getString("msgtime"));
                                messageChat.setmType(Integer.parseInt(mArrayChatJSONObject.getString("msgtype")));
                                int msgType = messageChat.getType();
                                if (mStrFromId.equals(mUsername)) {
                                } else {
                                    if (mStrMsgFrom.equals(mStrFromId)) {
                                        if (msgType == 1) {
                                            addMessage(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                                        } else if (msgType == 2) {
                                            System.out.println("####<><><>onNewMessage" + messageChat.getMessage());
                                            addImage(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                                        } else if (msgType == 4) {
                                            addDocument(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                                        } else if (msgType == 3) {
                                            addVideo(messageChat.getmUserMsgFrom(), messageChat.getMessage(), messageChat.getmCreatedAt());
                                        }
                                    }
                                }
                                System.out.println("<><><>new insert");
                                db.insert_tb_chat_msg_list(messageChat);
                            }
                        } else {
                            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_no_record_found), ActivityAuditorToAgentChat.this);
                        }
                        hidePDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    /* Socket Listener handel to get all massage history */
    private Emitter.Listener chatHistory = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String data = args[0].toString();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray mArrayChat = jsonObject.getJSONArray("chats");
                        System.out.println("<><><>chatHistory.." + mArrayChat.toString());
                        if (mArrayChat.length() > 0) {
                            String mStrFromName;
                            for (int i = 0; i < mArrayChat.length(); i++) {
                                MessageChat messageChat = new MessageChat();
                                JSONObject mArrayChatJSONObject = mArrayChat.getJSONObject(i);
                                mStrFromName = mArrayChatJSONObject.getString("from_name");
                                messageChat.setmChatRoomId(mChatRoomId);
                                messageChat.setmChatUserId(mStrMsgFrom);
                                messageChat.setmUserId(PreferenceManager.getFormiiId(ActivityAuditorToAgentChat.this));
                                messageChat.setmUserMsgFrom(mArrayChatJSONObject.getString("from"));
                                messageChat.setmMessage(mArrayChatJSONObject.getString("msg"));
                                messageChat.setmCreatedAt(mArrayChatJSONObject.getString("msgtime"));
                                messageChat.setmType(Integer.parseInt(mArrayChatJSONObject.getString("msgtype")));
                                int msgType = messageChat.getType();
                                if (msgType == 1) {
                                    addMessage(messageChat.getmUserMsgFrom(), messageChat.getMessage(), getFormattedDate(messageChat.getmCreatedAt()));
                                } else if (msgType == 2) {
                                    System.out.println("####<><><>history" + messageChat.getMessage());
                                    addImage(messageChat.getmUserMsgFrom(), messageChat.getMessage(), getFormattedDate(messageChat.getmCreatedAt()));
                                } else if (msgType == 4) {
                                    addDocument(messageChat.getmUserMsgFrom(), messageChat.getMessage(), getFormattedDate(messageChat.getmCreatedAt()));
                                } else if (msgType == 3) {
                                    addVideo(messageChat.getmUserMsgFrom(), messageChat.getMessage(), getFormattedDate(messageChat.getmCreatedAt()));
                                }
                                db.insert_tb_chat_msg_list(messageChat);
                            }
                        } else {
                            mShowAlert(getString(R.string.mTextFile_error_no_record_found), ActivityAuditorToAgentChat.this);
                        }

                        hidePDialog();
                    } catch (JSONException e) {
                        Log.e("error", e.getMessage());
                        return;
                    }

                }
            });
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PREVIEW_RESULT && resultCode == RESULT_OK) {
            try {
                String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
                System.out.println("<><>path" + path);
                System.out.println("<><>ext" + getFileExt(path));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                if (getFileExt(path).equalsIgnoreCase("jpg") || getFileExt(path).equalsIgnoreCase("jpeg") || getFileExt(path).equalsIgnoreCase("png")) {
                    addImage(mUsername, imageString, getFormattedDate(mCurrentDate));
                    PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                    uploadBitmap(bitmap, path);
                } else if (getFileExt(path).equalsIgnoreCase("mp4")) {
                    String Fpath = data.getDataString();
                    addVideo(mUsername, Fpath, getFormattedDate(mCurrentDate));
                    mVideoURI = Uri.fromFile(new File(path));
                    System.out.println("<><>mVideoURI" + mVideoURI);
                    PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                    uploadVideo(mVideoURI, path);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == DOCUMENT && resultCode == RESULT_OK) {
            try {
                Uri selectedUri = data.getData();
                String Fpath = data.getDataString();
                System.out.println("<><><>Fpath" + getMimeTypefromfilepath(Fpath));
                System.out.println("<><><>uritype" + selectedUri.toString());
                String picturePathNew = getPath(ActivityAuditorToAgentChat.this, selectedUri);
                String picturePath = getMimeTypefromfilepath(Fpath);
                System.out.println("<><><>picturePath" + picturePath);
                System.out.println("<><><>picturePathNew" + picturePathNew);
                String mFinalPath = null;
                if (picturePath == null) {
                    mFinalPath = picturePathNew;
                    System.out.println("<><><>mFinalPath" + mFinalPath);
                } else if (picturePathNew == null) {
                    mFinalPath = picturePath;
                }

                if (mFinalPath.toString().contains("image") || getFileExt(mFinalPath).equalsIgnoreCase("jpg") || getFileExt(mFinalPath).equalsIgnoreCase("jpeg") || getFileExt(mFinalPath).equalsIgnoreCase("png")) {
                    try {
                        System.out.println("<><>image");
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
                        addImage(mUsername, getBase64ImageString(bitmap), getFormattedDate(mCurrentDate));
                        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                        uploadBitmap(bitmap, mFinalPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mFinalPath.toString().contains("video") || getFileExt(mFinalPath).equalsIgnoreCase("mp4")) {
                    System.out.println("<><>video");
                    mVideoURI = Uri.parse(String.valueOf(selectedUri));
                    addVideo(mUsername, Fpath, getFormattedDate(mCurrentDate));
                    PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                    uploadVideo(mVideoURI, mFinalPath);
                } else {
                    System.out.println("<><>doc");
                    if (getFileExt(mFinalPath).contains("pdf")) {
                        addDocument(mUsername, mFinalPath, getFormattedDate(mCurrentDate));
                        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                        uploadDocument(selectedUri, getFileExt(mFinalPath), mFinalPath);
                    } else if (getFileExt(mFinalPath).contains("docx") || getFileExt(mFinalPath).contains("doc")) {
                        addDocument(mUsername, mFinalPath, getFormattedDate(mCurrentDate));
                        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                        uploadDocument(selectedUri, getFileExt(mFinalPath), mFinalPath);
                    } else if (getFileExt(mFinalPath).contains("xlsx") || getFileExt(mFinalPath).contains("xls")) {
                        addDocument(mUsername, mFinalPath, getFormattedDate(mCurrentDate));
                        PreferenceManager.setFormiiProgreessView(ActivityAuditorToAgentChat.this, "1");
                        uploadDocument(selectedUri, getFileExt(mFinalPath), mFinalPath);
                    } else {
                        mShowAlert(getString(R.string.mTextFile_alert_select_file), ActivityAuditorToAgentChat.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mShowAlert(getString(R.string.mTextFile_alert_select_file), ActivityAuditorToAgentChat.this);
            }

        } else {
            Toast.makeText(this, R.string.mtextFile_alert_no_image, Toast.LENGTH_LONG).show();
        }
    }

    private void uploadBitmap(final Bitmap bitmap, final String path) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, mStrBaseUrl + "uploadChatFileAuditor",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("<><><>response" + obj.toString());
                            String status = obj.getString("status");
                            String mImgName = obj.getString("image_name");
                            sendImg(mImgName);
                            try {
                                File file = new File(path);
                                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder);
                                File root = new File(mFilePath);
                                System.out.println("<><>mFilePath" + mFilePath);
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File mNewFile = new File(root, mImgName);
                                System.out.println("<><><file" + mNewFile.getPath());
                                FileOutputStream out = new FileOutputStream(mNewFile);
                                out.flush();
                                out.close();
                                copyFile(file, mNewFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            messageChatAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(ActivityAuditorToAgentChat.this, bitmap)));
                System.out.println("<><><>para" + params);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void uploadVideo(final Uri videoUri, final String path) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, mStrBaseUrl + "uploadChatFileAuditor",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("<><><>response" + obj.toString());
                            String status = obj.getString("status");
                            String mImgName = obj.getString("image_name");
                            sendVideo(mImgName);
                            try {
                                File file = new File(path);
                                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_mediaFolder);
                                File root = new File(mFilePath);
                                System.out.println("<><>mFilePath" + mFilePath);
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File mNewFile = new File(root, mImgName);
                                System.out.println("<><><file" + mNewFile.getPath());
                                FileOutputStream out = new FileOutputStream(mNewFile);
                                out.flush();
                                out.close();
                                copyFile(file, mNewFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            messageChatAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".mp4", getFileDataFromDrawable(ActivityAuditorToAgentChat.this, videoUri)));
                System.out.println("<><><>para" + params);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

        private void uploadDocument(final Uri docUri, final String docType, final String path) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, mStrBaseUrl + "uploadChatFileAuditor",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("<><><>response" + obj.toString());
                            String status = obj.getString("status");
                            String mImgName = obj.getString("image_name");
                            sendDocument(mImgName);

                            try {
                                File file = new File(path);
                                String mFilePath = mStrChatFileDownloadPath + getString(R.string.mTextFile_filefolder);
                                File root = new File(mFilePath);
                                System.out.println("<><>mFilePath" + mFilePath);
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File mNewFile = new File(root, mImgName);
                                System.out.println("<><><file" + mNewFile.getPath());
                                FileOutputStream out = new FileOutputStream(mNewFile);
                                out.flush();
                                out.close();
                                copyFile(file, mNewFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            messageChatAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("<><><>docType" + docType);
                long imagename = System.currentTimeMillis();
                if (docType.contains("pdf"))
                    params.put("file", new DataPart(imagename + ".pdf", getFileDataFromDrawable(ActivityAuditorToAgentChat.this, docUri)));
                else if (docType.contains("doc") || docType.contains("docx"))
                    params.put("file", new DataPart(imagename + ".doc", getFileDataFromDrawable(ActivityAuditorToAgentChat.this, docUri)));
                else if (docType.contains("xls") || docType.contains("xlsx"))
                    params.put("file", new DataPart(imagename + ".xlsx", getFileDataFromDrawable(ActivityAuditorToAgentChat.this, docUri)));
                System.out.println("<><><>para" + params);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    /* permmission request*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* permmission grant*/
    @Override
    public void permissionGranted() {

    }

    /* permmission denied*/
    @Override
    public void permissionDenied() {
        finish();
        System.exit(0);
    }
}