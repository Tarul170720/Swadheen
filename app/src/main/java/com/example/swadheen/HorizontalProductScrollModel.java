package com.example.swadheen;

public class HorizontalProductScrollModel {

    private int productImage;
    private String productTitle;

    public HorizontalProductScrollModel(int productImage, String productTitle) {
        this.productImage = productImage;
        this.productTitle = productTitle;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }


}
