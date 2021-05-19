package ABS_GET_SET;

import java.util.ArrayList;

/**
 * Created by admin18 on 29/11/19.
 */

public class BuildingType {

    String mStrId;
    String mStrTitle;
    String mStrDetail;
    String mStrIsSelected;
    ArrayList<BuildingType> mSubList;


    public ArrayList<BuildingType> getmSubList() {
        return mSubList;
    }

    public void setmSubList(ArrayList<BuildingType> mSubList) {
        this.mSubList = mSubList;
    }

    public String getmStrId() {
        return mStrId;
    }

    public void setmStrId(String mStrId) {
        this.mStrId = mStrId;
    }

    public String getmStrTitle() {
        return mStrTitle;
    }

    public void setmStrTitle(String mStrTitle) {
        this.mStrTitle = mStrTitle;
    }

    public String getmStrDetail() {
        return mStrDetail;
    }

    public void setmStrDetail(String mStrDetail) {
        this.mStrDetail = mStrDetail;
    }

    public String getmStrIsSelected() {
        return mStrIsSelected;
    }

    public void setmStrIsSelected(String mStrIsSelected) {
        this.mStrIsSelected = mStrIsSelected;
    }
}
