package com.guru.pglocator.models;

import java.io.Serializable;

public class PgDetails implements Serializable {

    String id;
    String hostelname;
    String faci;
    String contact;
    String imgpath;
    String location;
    String key;


    public PgDetails() {
    }

    public PgDetails(String id, String hostelname, String faci, String contact, String imgpath, String location) {
        this.id = id;
        this.hostelname = hostelname;
        this.faci = faci;
        this.contact = contact;
        this.imgpath = imgpath;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }

    public String getFaci() {
        return faci;
    }

    public void setFaci(String faci) {
        this.faci = faci;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
