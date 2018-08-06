package org.zhongweixian.request.alipay;

import java.io.Serializable;

/**
 * Created by caoliang on 2018/7/30
 */
public class AliPayRequest implements Serializable {

    /**
     * 前端返回地址
     */
    private String app_id;

    private String method;

    private String charset;

    private String sign_type;

    private String timestamp;

    /**
     * 后端通知地址
     */
    private String notifyUrl;
    /**
     * 参数
     */
    private String biz_content;

    private String sign;

    private String version;


}
