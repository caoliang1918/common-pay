package org.zhongweixian.request;

import org.hibernate.validator.constraints.NotBlank;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/7/12 23:12
 */
public class RefundRequest {

    /**
     * 支付平台
     **/
    @NotBlank(message = "支付平台不能为空")
    private Channel channel;
    /**
     * 支付金额
     **/
    @NotBlank(message = "金额不能为空")
    private Long amount;
    /**
     * 订单号
     **/
    @NotBlank(message = "商户订单号不能为空")
    private String orderNo;
}
