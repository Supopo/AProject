package com.xxx.xxx.bean;

public class TopicBean {
    public TopicBean(String title) {
        setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

}