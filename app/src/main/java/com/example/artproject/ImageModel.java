package com.example.artproject;

public class ImageModel {
    private String imageUrl;

    public ImageModel() {
        // Пустой конструктор нужен для Firebase
    }

    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
