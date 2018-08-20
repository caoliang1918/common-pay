package org.zhongweixian.service;

/**
 * Created by caoliang on  6/5/2018
 */
public abstract class BasePayService implements CommonPayService {

    public BasePayService(String wxAppId, String wxMchId, String wxPaySecret, String aliPayMerchantId, String aliPaySecret, String privateKey, byte[] cert) {
        this.wxAppId = wxAppId;
        this.aliPayMerchantId = aliPayMerchantId;
        this.aliPaySecret = aliPaySecret;
        this.wxMchId = wxMchId;
        this.wxPaySecret = wxPaySecret;
        this.privateKey = privateKey;
        this.cert = cert;
    }

    /**
     * 支付宝商户id
     */
    public String aliPayMerchantId;

    /**
     * 支付宝公钥
     */
    public String aliPaySecret;

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
    public String wxPaySecret;

    /**
     * 商户证书 微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->证书下载
     */
    public byte[] cert;


}
