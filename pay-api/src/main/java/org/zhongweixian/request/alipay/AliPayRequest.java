package org.zhongweixian.request.alipay;

import java.io.Serializable;

/**
 * Created by caoliang on 2018/7/30
 */
public class AliPayRequest implements Serializable {

    /**
     * 前端返回地址
     */
    private String returnUrl;
    /**
     * 后端通知地址
     */
    private String notifyUrl;
    /**
     * 参数
     */
    private String bizContent;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }
}
