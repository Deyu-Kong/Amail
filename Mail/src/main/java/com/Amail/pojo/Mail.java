package com.Amail.pojo;

import java.util.Date;

/**
 * @author 孔德昱
 * @date 2022/11/16 17:56 星期三
 */
public class Mail implements Comparable{
    private String subject;

    private String fromAddr;

    private Integer mailId;

    private String sendDateStr;

    private Date sendDate;

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Mail(String subject, String fromAddr, Integer mailId, String sendDate, Date date) {
        this.subject = subject;
        this.fromAddr = fromAddr;
        this.mailId = mailId;
        this.sendDateStr = sendDate;
        this.sendDate = date;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "subject='" + subject + '\'' +
                ", fromAddr='" + fromAddr + '\'' +
                ", mailId=" + mailId +
                '}';
    }

    public String getSendDateStr() {
        return sendDateStr;
    }

    public void setSendDateStr(String sendDateStr) {
        this.sendDateStr = sendDateStr;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public Mail() {
    }

    @Override
    public int compareTo(Object anotherMail) {
        if(((Mail)anotherMail).sendDate==null){
            return -1;
        }else if(this.sendDate==null){
            return 1;
        }
        return -this.sendDate.compareTo(((Mail)anotherMail).sendDate);
    }
}
