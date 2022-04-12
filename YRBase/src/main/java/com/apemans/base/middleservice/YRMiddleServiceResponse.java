package com.apemans.base.middleservice;

public class YRMiddleServiceResponse<T> {
    private int code;
    private String errorMsg;
    private T data;

    public YRMiddleServiceResponse(int code, String errorMsg, T data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "YRMiddleServiceResponse{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
