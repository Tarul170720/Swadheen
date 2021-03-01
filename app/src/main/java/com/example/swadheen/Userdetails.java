package com.example.swadheen;

import android.content.Context;

public class Userdetails {
    // Here Use same name as mentioned in firebase ->Strings. Eg. Servicename, Serviceworkername, price,etc.
    private String servicename;
    private String Photo;


    public Userdetails() {
    }

    public Userdetails(String servicename,String photo) {
        this.servicename = servicename;
        this.Photo=photo;

    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }


}
