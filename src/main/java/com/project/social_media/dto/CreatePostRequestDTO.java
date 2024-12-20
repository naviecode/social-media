package com.project.social_media.dto;

import java.util.List;

public class CreatePostRequestDTO {
    private String content;
    private String privacy;
    private List<String> imagesData;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public List<String> getImagesData() {
        return imagesData;
    }

    public void setImagesData(List<String> imagesData) {
        this.imagesData = imagesData;
    }
}
