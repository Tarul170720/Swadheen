package com.example.swadheen;

import android.graphics.Bitmap;

public class HistoryModel {
    private String fullName;
    private String Date_of_work;
    public String workerType;
    public String photo;
    public HistoryModel(String fullName, String Date_Of_Work, String workerType,String photo) {
        this.fullName = fullName;
        this.Date_of_work = Date_Of_Work;
        this.workerType=workerType;
        this.photo=photo;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDate_of_work() {
        return Date_of_work;
    }
    public void setDate_of_work(String Date_of_work) {
        this.Date_of_work = Date_of_work;
    }

    public String getWorkerType() {
        return workerType;
    }
    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
