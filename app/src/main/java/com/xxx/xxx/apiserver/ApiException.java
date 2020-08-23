package com.xxx.xxx.apiserver;

public class ApiException extends RuntimeException {
    public String code;
    public String msg;
    public ApiException(String code, String message) {
        this.code=code;
        this.msg=message;
    }
}
