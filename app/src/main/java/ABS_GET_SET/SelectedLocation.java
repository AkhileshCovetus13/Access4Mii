package ABS_GET_SET;

import java.io.Serializable;

/**
 * Created by admin1 on 1/12/18.
 */

public class SelectedLocation implements Serializable{

    String mStrId;
    String mStrMainLocationTitle;
    String mStrMainLocationServerId;
    String mStrMainLocationLocalId;
    String mStrMainLocationCount;
    String mStrMainLocationDesc;
    String mStrUserId;
    String mStrAuditId;

    public String getmStrMainLocationDesc() {
        return mStrMainLocationDesc;
    }

    public void setmStrMainLocationDesc(String mStrMainLocationDesc) {
        this.mStrMainLocationDesc = mStrMainLocationDesc;
    }

    public String getmStrId() {
        return mStrId;
    }

    public void setmStrId(String mStrId) {
        this.mStrId = mStrId;
    }

    public String getmStrMainLocationTitle() {
        return mStrMainLocationTitle;
    }

    public void setmStrMainLocationTitle(String mStrMainLocationTitle) {
        this.mStrMainLocationTitle = mStrMainLocationTitle;
    }

    public String getmStrMainLocationServerId() {
        return mStrMainLocationServerId;
    }

    public void setmStrMainLocationServerId(String mStrMainLocationServerId) {
        this.mStrMainLocationServerId = mStrMainLocationServerId;
    }

    public String getmStrMainLocationLocalId() {
        return mStrMainLocationLocalId;
    }

    public void setmStrMainLocationLocalId(String mStrMainLocationLocalId) {
        this.mStrMainLocationLocalId = mStrMainLocationLocalId;
    }

    public String getmStrMainLocationCount() {
        return mStrMainLocationCount;
    }

    public void setmStrMainLocationCount(String mStrMainLocationCount) {
        this.mStrMainLocationCount = mStrMainLocationCount;
    }

    public String getmStrUserId() {
        return mStrUserId;
    }

    public void setmStrUserId(String mStrUserId) {
        this.mStrUserId = mStrUserId;
    }

    public String getmStrAuditId() {
        return mStrAuditId;
    }

    public void setmStrAuditId(String mStrAuditId) {
        this.mStrAuditId = mStrAuditId;
    }
}
