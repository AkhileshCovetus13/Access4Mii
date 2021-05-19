package ABS_GET_SET;

/**
 * Created by admin1 on 28/11/18.
 */

public class AuditQuestion {


    String mStrId;
    String mStrUserId;
    String mStrAuditId;
    String mStrServerId;
    String mStrMainLocation;
    String mStrSubLocation;
    String mStrQuestionType;
    String mStrQuestionTxt;
    String mStrAnswerOption;
    String mStrAnswerOptionId;
    String mStrSubQuestion;
    String mStrQuestionStatus;
    String mStrInspectorQuestion;

    public String getmStrInspectorQuestion() {
        return mStrInspectorQuestion;
    }

    public void setmStrInspectorQuestion(String mStrInspectorQuestion) {
        this.mStrInspectorQuestion = mStrInspectorQuestion;
    }

    @Override
    public String toString() {
        return mStrAnswerOption;
    }

    public AuditQuestion() {
    }

    public AuditQuestion(String mStrAnswerOptionId, String mStrAnswerOption) {
        this.mStrAnswerOptionId = mStrAnswerOptionId;
        this.mStrAnswerOption = mStrAnswerOption;
    }


    public String getmStrId() {
        return mStrId;
    }

    public void setmStrId(String mStrId) {
        this.mStrId = mStrId;
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

    public String getmStrServerId() {
        return mStrServerId;
    }

    public void setmStrServerId(String mStrServerId) {
        this.mStrServerId = mStrServerId;
    }

    public String getmStrMainLocation() {
        return mStrMainLocation;
    }

    public void setmStrMainLocation(String mStrMainLocation) {
        this.mStrMainLocation = mStrMainLocation;
    }

    public String getmStrSubLocation() {
        return mStrSubLocation;
    }

    public void setmStrSubLocation(String mStrSubLocation) {
        this.mStrSubLocation = mStrSubLocation;
    }

    public String getmStrQuestionType() {
        return mStrQuestionType;
    }

    public void setmStrQuestionType(String mStrQuestionType) {
        this.mStrQuestionType = mStrQuestionType;
    }

    public String getmStrQuestionTxt() {
        return mStrQuestionTxt;
    }

    public void setmStrQuestionTxt(String mStrQuestionTxt) {
        this.mStrQuestionTxt = mStrQuestionTxt;
    }

    public String getmStrAnswerOption() {
        return mStrAnswerOption;
    }

    public void setmStrAnswerOption(String mStrAnswerOption) {
        this.mStrAnswerOption = mStrAnswerOption;
    }

    public String getmStrAnswerOptionId() {
        return mStrAnswerOptionId;
    }

    public void setmStrAnswerOptionId(String mStrAnswerOptionId) {
        this.mStrAnswerOptionId = mStrAnswerOptionId;
    }

    public String getmStrSubQuestion() {
        return mStrSubQuestion;
    }

    public void setmStrSubQuestion(String mStrSubQuestion) {
        this.mStrSubQuestion = mStrSubQuestion;
    }

    public String getmStrQuestionStatus() {
        return mStrQuestionStatus;
    }

    public void setmStrQuestionStatus(String mStrQuestionStatus) {
        this.mStrQuestionStatus = mStrQuestionStatus;
    }
}
