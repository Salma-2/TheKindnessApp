package com.example.charityapplicationsigninup.problems;

public class Problem {

    private String mId;
    private int mIsUrgent;
    private String mPhone;
    private String mTitle;
    private String mProblem;
    private int mIsRequested;
    private String mPhotoUrl;
    private String mAddress;


    private int mCatNum;

    public Problem() {
        //for firebase
    }

    public Problem(String id, String phone, String title, String problem, String photoUrl, int isUrgent,
                   int isRequested, String mAddress)
    {
        this.mId=id;
        this.mIsUrgent=isUrgent;
        this.mPhone=phone;
        this.mTitle=title;
        this.mProblem=problem;
        this.mIsRequested= isRequested;
        this.mPhotoUrl = photoUrl;
        this.mAddress = mAddress;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public int getmIsUrgent() {
        return mIsUrgent;
    }

    public void setmIsUrgent(int mIsUrgent) {
        this.mIsUrgent = mIsUrgent;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmProblem() {
        return mProblem;
    }

    public void setmProblem(String mProblem) {
        this.mProblem = mProblem;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public int getmIsRequested() {
        return mIsRequested;
    }

    public void setmIsRequested(int mIsRequested) {
        this.mIsRequested = mIsRequested;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public int getmCatNum() {
        return mCatNum;
    }

    public void setmCatNum(int mCatNum) {
        this.mCatNum = mCatNum;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

}
