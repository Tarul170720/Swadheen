package com.example.swadheen;

public class NearbyworkerModel {

    private String fullName;
    private String mobileNumber;
    public String workerType;
    public String Image;

    public NearbyworkerModel(String fullName, String mobileNumber,String workerType,String Image) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.workerType=workerType;
        this.Image=Image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWorkerType() {
        return workerType;
    }
    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }
}
