package com.bpic.mobile.sms;

/**
 * 短信实体类
 */
public class SmsBean {
    public SmsBean() {
    }

    private String UserID;
    private String Password;
    private String RealFlag;
    private String BusinessType;
    private String Organization;
    private String SendNumber;
    private String SendTime;
    private String RecvMobile;
    private String MsgContent;
    private String KeyWord;
    private String SeqNo;

    public String getUserID() {
        return this.UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getRealFlag() {
        return this.RealFlag;
    }

    public void setRealFlag(String realFlag) {
        this.RealFlag = realFlag;
    }

    public String getBusinessType() {
        return this.BusinessType;
    }

    public void setBusinessType(String businessType) {
        this.BusinessType = businessType;
    }

    public String getOrganization() {
        return this.Organization;
    }

    public void setOrganization(String organization) {
        this.Organization = organization;
    }

    public String getSendNumber() {
        return this.SendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.SendNumber = sendNumber;
    }

    public String getSendTime() {
        return this.SendTime;
    }

    public void setSendTime(String sendTime) {
        this.SendTime = sendTime;
    }

    public String getRecvMobile() {
        return this.RecvMobile;
    }

    public void setRecvMobile(String recvMobile) {
        this.RecvMobile = recvMobile;
    }

    public String getMsgContent() {
        return this.MsgContent;
    }

    public void setMsgContent(String msgContent) {
        this.MsgContent = msgContent;
    }

    public String getKeyWord() {
        return this.KeyWord;
    }

    public void setKeyWord(String keyWord) {
        this.KeyWord = keyWord;
    }

    public String getSeqNo() {
        return this.SeqNo;
    }

    public void setSeqNo(String seqNo) {
        this.SeqNo = seqNo;
    }

    @Override
    public String toString() {
        return "SmsBean{" +
                "UserID='" + UserID + '\'' +
                ", Password='" + Password + '\'' +
                ", RealFlag='" + RealFlag + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", Organization='" + Organization + '\'' +
                ", SendNumber='" + SendNumber + '\'' +
                ", SendTime='" + SendTime + '\'' +
                ", RecvMobile='" + RecvMobile + '\'' +
                ", MsgContent='" + MsgContent + '\'' +
                ", KeyWord='" + KeyWord + '\'' +
                ", SeqNo='" + SeqNo + '\'' +
                '}';
    }
}
