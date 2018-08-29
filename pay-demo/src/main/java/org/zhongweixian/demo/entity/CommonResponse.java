package org.zhongweixian.demo.entity;

/**
 * Created by caoliang on 2018/8/29
 */
public class CommonResponse<T> {
    int code;
    String message = "success";
    T data;


    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
