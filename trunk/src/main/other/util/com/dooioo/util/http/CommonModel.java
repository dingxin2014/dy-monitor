package com.dooioo.util.http;

/**
 * Created by gelif on 2015/5/14.
 */
public class CommonModel<T> {
    public String status;
    public String message;
    public T      data;

    public static <T> CommonModel<T> buildSuccess(T data) {
        return new CommonModel<>(data);
    }

    public static <T> CommonModel<T> buildFail(T data, String message) {
        return new CommonModel<>(data, message);
    }

    /**
     * 成功的结果
     *
     * @param data
     */
    public CommonModel(T data) {
        this.data = data;
        this.status = "ok";
    }

    /**
     * 错误的结果
     *
     * @param data
     * @param message
     */
    public CommonModel(T data, String message) {
        this.data = data;
        this.status = "fail";
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

