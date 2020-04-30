package com.example.charityapplicationsigninup.accounts;

/**
 * This class creates an instance of the user with all his/her specs
 */

public class User {

    //name of the user
    private String userName;
    //tel Number of the user
    private String telNum;
    //Highest education level of the user
    //0 not mentioned
    //1 elementary school
    //2 high school or equivalent
    //3 bachelor or some college
    //4 masters
    //5 phD
    private int highestEduLvl;
    //national id of the user
    private String nationalNum;
    //url of national id photo
    private String nationalIdPhotoUrl;
    //address of the user
    private String Address;
    //number of reports on the user
    private int numOfReports;
    //ids of the posted problems by a user
    private String postedProblemsIds;
    //requests sent to help by a user
    private String helpRequests;
    //whether he wants to be seen or not
    //0 not seen
    //1 seen
    private int seenOrNot;


    public User() {
        //required for the database
    }

    public User(String userName, String telNum, int highestEduLvl, String nationalNum, String nationalIdPhotoUrl, String address, int numOfReports, String postedProblemsIds, String helpRequests, int seenOrNot) {
        this.userName = userName;
        this.telNum = telNum;
        this.highestEduLvl = highestEduLvl;
        this.nationalNum = nationalNum;
        this.nationalIdPhotoUrl = nationalIdPhotoUrl;
        Address = address;
        this.numOfReports = numOfReports;
        this.postedProblemsIds = postedProblemsIds;
        this.helpRequests = helpRequests;
        this.seenOrNot = seenOrNot;
    }

    //getters
    public String getUserName() {
        return userName;
    }

    public String getTelNum() {
        return telNum;
    }

    public int getHighestEduLvl() {
        return highestEduLvl;
    }

    public String getNationalNum() {
        return nationalNum;
    }

    public String getNationalIdPhotoUrl() {
        return nationalIdPhotoUrl;
    }

    public String getAddress() {
        return Address;
    }

    public int getNumOfReports() {
        return numOfReports;
    }

    public String getPostedProblemsIds() {
        return postedProblemsIds;
    }

    public String getHelpRequests() {
        return helpRequests;
    }

    public int getSeenOrNot() {
        return seenOrNot;
    }

    //setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public void setHighestEduLvl(int highestEduLvl) {
        this.highestEduLvl = highestEduLvl;
    }

    public void setNationalNum(String nationalNum) {
        this.nationalNum = nationalNum;
    }

    public void setNationalIdPhotoUrl(String nationalIdPhotoUrl) {
        this.nationalIdPhotoUrl = nationalIdPhotoUrl;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setNumOfReports(int numOfReports) {
        this.numOfReports = numOfReports;
    }

    public void setPostedProblemsIds(String postedProblemsIds) {
        this.postedProblemsIds = postedProblemsIds;
    }

    public void setHelpRequests(String helpRequests) {
        this.helpRequests = helpRequests;
    }

    public void setSeenOrNot(int seenOrNot) {
        this.seenOrNot = seenOrNot;
    }
}
