package org.example.request;

public class PostRequest {
    private String text;
    private String image;

    public PostRequest(String text, String fileName) {
        this.text = text;
        this.image = fileName;
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
