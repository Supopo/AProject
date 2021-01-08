package com.xxx.xxx.bean;

import com.xxx.xxx.utils.ColorsUtils;

public class TestBean {
    public TestBean() {
    }

    public TestBean(String name, String status, String acc, String psd) {
        this.name = name;
        this.status = status;
        this.acc = acc;
        this.psd = psd;
    }

    public int getPlaceholderRes() {
        return ColorsUtils.randomColor();
    }

    private int placeholderRes;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPsd() {
        return psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }

    private String status;
    private String acc;
    private String psd;

}