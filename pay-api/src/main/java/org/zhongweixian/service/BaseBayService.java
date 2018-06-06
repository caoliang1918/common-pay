package org.zhongweixian.service;

/**
 * Created by caoliang on  6/5/2018
 */
public abstract class BaseBayService implements CommonPayService {

    public BaseBayService(String wxAppid, String aliPayMerchantId, String aliPaySecret, String aliPayNotifyUrl, String aliPayReturenUrl, String wxPayMerchantid, String wxPaySecret, String wxPayNotifyUrl, String wxPayReturenUrl) {
        this.wxAppid = wxAppid;
        this.aliPayMerchantId = aliPayMerchantId;
        this.aliPaySecret = aliPaySecret;
        this.aliPayNotifyUrl = aliPayNotifyUrl;
        this.aliPayReturenUrl = aliPayReturenUrl;
        this.wxPayMerchantid = wxPayMerchantid;
        this.wxPaySecret = wxPaySecret;
        this.wxPayNotifyUrl = wxPayNotifyUrl;
        this.wxPayReturenUrl = wxPayReturenUrl;
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
     * 支付宝网关
     */
    public String aliPayGateWay = "https://mapi.alipay.com/gateway.do?";

    /**
     * 支付宝回调地址
     */
    public String aliPayNotifyUrl;

    /**
     * 支付宝前端页面跳转地址
     */
    public String aliPayReturenUrl;

    /**
     * 微信应用id
     */
    public String wxAppid;

    /**
     * 微信商户id
     */
    public String wxPayMerchantid;

    /**
     * 微信公钥
     */
    public String wxPaySecret;

    /**
     * 微信支付网关
     */
    public String wxPayGateWay = "https://gw.tenpay.com/gateway/pay.htm";

    /**
     * 微信回调地址
     */
    public String wxPayNotifyUrl;

    /**
     * 微信前端页面跳转地址
     */
    public String wxPayReturenUrl;

}
