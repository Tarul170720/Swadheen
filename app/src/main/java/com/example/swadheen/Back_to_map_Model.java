package com.example.swadheen;

public class Back_to_map_Model {
    private String subCategoryName;
    private String Description;

    public Back_to_map_Model() {
    }


    public Back_to_map_Model(String subCategoryName,String Description) {
        this.subCategoryName = subCategoryName;
        this.Description=Description;

    }

    public String getSubCategoryName() {
        return subCategoryName;
    }
    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
