package org.zhongweixian.model;

public enum PayType {

    /**
     *手机网站支付
     */
    ALI_H5("alipay.trade.wap.pay"),

    /**
     * 支付宝电脑网站支付
     */
    ALI_WEB("alipay.trade.page.pay"),

    /**
     * APP支付
     */
    ALI_APP("alipay.trade.app.pay"),

    /**
     * 刷卡支付
     */
    WX_MICRO("MICROPAY"),
    /**
     * H5支付
     */
    WX_H5("MWEB"),

    /**
     * 公众号支付
     */
    WX_JS("JSAPI"),

    /**
     * 扫码支付
     */
    WX_QRCODE("NATIVE"),

    /**
     * 微信APP支付
     */
    WX_APP("APP");


    PayType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
