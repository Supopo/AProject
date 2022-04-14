package me.goldze.mvvmhabit.http;

/**
 * 该类仅供参考，实际业务返回的固定字段, 根据需求来定义，
 */
public class BaseResponse<T> {
    private T data;
    private int errorCode;
    private String errorMsg;


    public int getStatus() {
        return errorCode;
    }

    public void setStatus(int status) {
        this.errorCode = status;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //TODO 根据自己情况而定
    public boolean isOk() {
        return errorCode == 0;
    }

    public String getMessage() {
        return errorMsg;
    }

    public void setMessage(String message) {
        this.errorMsg = message;
    }
}
