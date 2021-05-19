package ABS_GET_SET;

/**
 * Created by admin18 on 4/6/18.
 */

public class MessageChat {

    public static final int TYPE_MESSAGE_SENT = 5;
    public static final int TYPE_MESSAGE_RECEIVE = 0;

    public static final int TYPE_MEDIA_SENT_MESSAGE = 1;
    public static final int TYPE_MEDIA_RECEIVE_MESSAGE = 2;

    public static final int TYPE_VIDEO_SENT_MESSAGE = 3;
    public static final int TYPE_VIDEO_RECEIVE_MESSAGE = 4;

    public static final int TYPE_DOCUMENT_SENT_MESSAGE = 6;
    public static final int TYPE_DOCUMENT_RECEIVE_MESSAGE = 7;


    private int mType;
    private String mMessage;
    private String mChatUserId;

    public String getmUserMsgFrom() {
        return mUserMsgFrom;
    }

    public void setmUserMsgFrom(String mUserMsgFrom) {
        this.mUserMsgFrom = mUserMsgFrom;
    }

    private String mUserMsgFrom;
    private String mCreatedAt;
    private String mImage;
    private String mUserId;
    int mChatRoomId;

    public int getmChatRoomId() {
        return mChatRoomId;
    }

    public void setmChatRoomId(int mChatRoomId) {
        this.mChatRoomId = mChatRoomId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public void setmChatUserId(String mChatUserId) {
        this.mChatUserId = mChatUserId;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public MessageChat() {
    }

    public int getType() {
        return mType;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getmChatUserId() {
        return mChatUserId;
    }




    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mCreatedAt;
        private String mImage;


        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder createdAt(String createdAt) {
            mCreatedAt = createdAt;
            return this;
        }

        public Builder image(String image) {
            mImage = image;
            return this;
        }

        public MessageChat build() {
            MessageChat message = new MessageChat();
            message.mType = mType;
            message.mChatUserId = mUsername;
            message.mMessage = mMessage;
            message.mImage = mImage;
            message.mCreatedAt = mCreatedAt;
            return message;
        }
    }

}