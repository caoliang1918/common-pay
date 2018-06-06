package org.zhongweixian.response;

import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayResponse {
    private String code = "200";
    private String msg = "SUCCESS";
    private Map<String, String> ext;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }
}
