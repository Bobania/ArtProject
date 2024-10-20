package com.example.artproject;

import java.io.Serializable;

public class Post  implements Serializable {
    private String title;
    private boolean isLiked;
    private String imageUrl;
    private String description;
    private String creationTime;
    private String author;

    public Post() {
        // Пустой конструктор необходим для Firebase
    }


    // Конструктор, геттеры и сеттеры
    public Post(String author, String title, String imageUrl, String description, String creationTime) {
        this.author = author;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.creationTime = creationTime;

    }

    // Геттеры и сеттеры
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCreationTime() { return creationTime; }
    public void setCreationTime(String creationTime) { this.creationTime = creationTime; }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}