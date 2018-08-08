package org.zhongweixian.model;

public enum PayType {

    /**
     *手机网站支付
     */
    ALI_H5("QUICK_WAP_WAY"),

    /**
     * 支付宝电脑网站支付
     */
    ALI_WEB("FAST_INSTANT_TRADE_PAY"),

    /**
     * APP支付
     */
    ALI_APP("QUICK_MSECURITY_PAY"),

    /**
     *
     */
    ALI_FACE("FACE_TO_FACE_PAYMENT"),

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
