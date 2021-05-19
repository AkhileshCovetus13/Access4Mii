package ABS_GET_SET;

/**
 * Created by admin18 on 4/12/18.
 */

public class Message {


    //for chat list
    String mUserRole;

    public String getmStrUserId() {
        return mStrUserId;
    }

    public void setmStrUserId(String mStrUserId) {
        this.mStrUserId = mStrUserId;
    }

    String mStrUserId;
    String mChatUserId;
    String mUserName;
    String mUserLastMsg;
    String mUserPhoto;
    String mUserMsgDate;
    String mUserMsgFrom;
    String mUserMsgTo;
    String mUserPhone;
    String mUserEmail;
            int mStrChatRoomId;

    public int getmStrChatRoomId() {
        return mStrChatRoomId;
    }

    public void setmStrChatRoomId(int mStrChatRoomId) {
        this.mStrChatRoomId = mStrChatRoomId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserPhone() {
        return mUserPhone;
    }

    public void setmUserPhone(String mUserPhone) {
        this.mUserPhone = mUserPhone;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserMsgFrom() {
        return mUserMsgFrom;
    }

    public void setmUserMsgFrom(String mUserMsgFrom) {
        this.mUserMsgFrom = mUserMsgFrom;
    }

    public String getmUserMsgTo() {
        return mUserMsgTo;
    }

    public void setmUserMsgTo(String mUserMsgTo) {
        this.mUserMsgTo = mUserMsgTo;
    }

    public String getmUserMsgDate() {
        return mUserMsgDate;
    }

    public void setmUserMsgDate(String mUserMsgDate) {
        this.mUserMsgDate = mUserMsgDate;
    }

    public String getmUserPhoto() {
        return mUserPhoto;
    }

    public void setmUserPhoto(String mUserPhoto) {
        this.mUserPhoto = mUserPhoto;
    }

    public String getmUserRole() {
        return mUserRole;
    }

    public void setmUserRole(String mUserRole) {
        this.mUserRole = mUserRole;
    }

    public String getmChatUserId() {
        return mChatUserId;
    }

    public void setmChatUserId(String mChatUserId) {
        this.mChatUserId = mChatUserId;
    }

    public String getmUserLastMsg() {
        return mUserLastMsg;
    }

    public void setmUserLastMsg(String mUserLastMsg) {
        this.mUserLastMsg = mUserLastMsg;
    }
}
