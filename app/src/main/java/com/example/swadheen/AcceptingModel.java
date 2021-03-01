package com.example.swadheen;

public class AcceptingModel {
    private String subCategoryName;
    private String Description;
    private String Photo;
    public AcceptingModel() {
    }


    public AcceptingModel(String subCategoryName,String Description,String Photo) {
        this.subCategoryName = subCategoryName;
        this.Description=Description;
        this.Photo=Photo;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }
    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    //public String getCostumerId()


}
