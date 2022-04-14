package com.xxx.xxx.bean;

import com.xxx.xxx.utils.ColorsUtils;

import java.io.Serializable;

public class BannerBean implements Serializable {
    private String imagePath;
    private String title;
    private String url;
    private String desc;
    private int id;
    private int isVisible;
    private int order;
    private int type;
    private int placeholderRes;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BannerBean(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPlaceholderRes() {
        return ColorsUtils.randomColor();
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
