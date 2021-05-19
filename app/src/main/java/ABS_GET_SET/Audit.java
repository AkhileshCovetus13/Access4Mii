package ABS_GET_SET;

import java.io.Serializable;

/**
 * Created by admin1 on 14/11/18.
 */

public class Audit  {

    String mUserId;
    String mAuditId;
    String mAuditType;
    String mAssignBy;
    String mTitle;
    String mDueDate;
    String mStatus;
    String mStrCountryId;
    String mStrLangId;
    String mStrIsSynk;
    String mStrAuditerReport;
    String mStrInspectorReport;


    public String getmStrAuditerReport() {
        return mStrAuditerReport;
    }

    public void setmStrAuditerReport(String mStrAuditerReport) {
        this.mStrAuditerReport = mStrAuditerReport;
    }

    public String getmStrInspectorReport() {
        return mStrInspectorReport;
    }

    public void setmStrInspectorReport(String mStrInspectorReport) {
        this.mStrInspectorReport = mStrInspectorReport;
    }

    public String getmAuditType() {
        return mAuditType;
    }

    public void setmAuditType(String mAuditType) {
        this.mAuditType = mAuditType;
    }

    public String getmStrIsSynk() {
        return mStrIsSynk;
    }

    public void setmStrIsSynk(String mStrIsSynk) {
        this.mStrIsSynk = mStrIsSynk;
    }

    public String getmStrCountryId() {
        return mStrCountryId;
    }

    public void setmStrCountryId(String mStrCountryId) {
        this.mStrCountryId = mStrCountryId;
    }

    public String getmStrLangId() {
        return mStrLangId;
    }

    public void setmStrLangId(String mStrLangId) {
        this.mStrLangId = mStrLangId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmAuditId() {
        return mAuditId;
    }

    public void setmAuditId(String mAuditId) {
        this.mAuditId = mAuditId;
    }

    public String getmAssignBy() {
        return mAssignBy;
    }

    public void setmAssignBy(String mAssignBy) {
        this.mAssignBy = mAssignBy;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDueDate() {
        return mDueDate;
    }

    public void setmDueDate(String mDueDate) {
        this.mDueDate = mDueDate;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
