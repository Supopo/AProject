package com.xxx.xxx.bean;

import com.xxx.xxx.utils.ColorsUtils;

import java.io.Serializable;

public class BannerBean implements Serializable {
    private String image;
    private String title;
    private String url;

    public BannerBean(String image) {
        this.image = image;
    }

    public int getPlaceholderRes() {
        return ColorsUtils.randomColor();
    }

    private int placeholderRes;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
