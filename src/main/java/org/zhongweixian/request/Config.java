package org.zhongweixian.request;

/**
 * Created by caoliang on 2018/8/22
 */
public class Config {

    /**
     * 支付宝商户id
     */
    public String aliAppId;

    /**
     * 支付宝公钥
     */
    public String aliPublicKey;

    /**
     * 异步通知接口地址
     */
    public String aliNotifyUrl;

    /**
     * 前端跳转地址
     */
    private String aliReturnUrl;

    /**
     * 商户私钥
     */
    public String privateKey;


    /**
     * 微信应用id
     */
    public String wxAppId;

    /**
     * 微信商户id
     */
    public String wxMchId;

    /**
     * 微信公钥
     */
    public String wxKey;

    /**
     * 微信异步通知地址
     */
    public String wxNotifyUrl;

    /**
     * 商户证书 微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->证书下载
     */
    public String certPath;

    public String getAliAppId() {
        return aliAppId;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId = aliAppId;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public String getAliNotifyUrl() {
        return aliNotifyUrl;
    }

    public void setAliNotifyUrl(String aliNotifyUrl) {
        this.aliNotifyUrl = aliNotifyUrl;
    }

    public String getAliReturnUrl() {
        return aliReturnUrl;
    }

    public void setAliReturnUrl(String aliReturnUrl) {
        this.aliReturnUrl = aliReturnUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }

    public String getWxKey() {
        return wxKey;
    }

    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }

    public String getWxNotifyUrl() {
        return wxNotifyUrl;
    }

    public void setWxNotifyUrl(String wxNotifyUrl) {
        this.wxNotifyUrl = wxNotifyUrl;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
}
