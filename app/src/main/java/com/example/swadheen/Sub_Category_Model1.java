package com.example.swadheen;

public class Sub_Category_Model1 {
    private String subCategoryName;
    private String Description;
    private String Photo;
    private String Price;
    private String CostumerId;
    public Sub_Category_Model1() {
    }


    public Sub_Category_Model1(String subCategoryName,String Description,String Photo,String price,String costumerId) {
        this.subCategoryName = subCategoryName;
        this.Description=Description;
        this.Photo=Photo;
        this.Price=price;
        this.CostumerId=costumerId;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCostumerId() {
        return CostumerId;
    }

    public void setCostumerId(String costumerId) {
        CostumerId = costumerId;
    }
}
