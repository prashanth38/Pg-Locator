package com.guru.pglocator.models;

public class Messages {

    String userid;
    String ownerid;
    String message;
    String hostelname;
    String username;
    String type;

    public Messages() {
    }

    public Messages(String userid, String ownerid, String message,String hostelname) {
        this.userid = userid;
        this.ownerid = ownerid;
        this.message = message;
        this.hostelname=hostelname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
