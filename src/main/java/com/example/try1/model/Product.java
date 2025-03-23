package com.example.try1.model;

public class Product {
    private String title;
    private float price;
    private String imageUrl;
    private String productUrl;

    public Product(String title, float price, String imageUrl, String productUrl) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productUrl = productUrl;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }


}