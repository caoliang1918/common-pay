package org.zhongweixian.response;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/7/21 21:54
 */
public class BaseResp implements Serializable {
    private String code = "200";
    private String msg = "SUCCESS";
    private Map<String ,String> ext;

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

    @Override
    public String toString() {
        return "BaseResp{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", ext=" + ext +
                '}';
    }
}
