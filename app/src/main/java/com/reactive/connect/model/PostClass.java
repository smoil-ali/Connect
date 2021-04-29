package com.reactive.connect.model;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class PostClass extends BaseObservable implements Serializable {
    String postId;
    String description;
    String postImage;
    String publisher;

    public PostClass() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostImage() {
        if (postImage == null)
            return "";
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
