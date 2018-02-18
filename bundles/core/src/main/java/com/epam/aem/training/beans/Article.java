package com.epam.aem.training.beans;

public class Article {

    private String title;
    private String text;
    private String imageLink;
    private String path;


    public Article() {
    }

    public Article(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Article(String title, String text, String imageLink, String path) {
        this.title = title;
        this.text = text;
        this.imageLink = imageLink;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return title + " : " + text;
    }
}
