package org.zhongweixian.request;

import org.hibernate.validator.constraints.NotBlank;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;

import java.io.Serializable;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/7/12 23:12
 */
public class RefundRequest implements Serializable {

    /**
     * 支付平台
     **/
    @NotBlank(message = "支付平台不能为空")
    private Channel channel;
    /**
     * 订单号
     **/
    @NotBlank(message = "商户订单号不能为空")
    private String orderNo;

    /**
     * 交易平台单号[可选值]
     */
    private String thirdOrderNo;

    @NotBlank(message = "商户退款单号不能为空")
    private String refundNo;

    @NotBlank(message = "订单总金额不能为空")
    private Long totalFee;

    @NotBlank(message = "退款金额不能为空")
    private Long refundFee;

    /**
     * 货币种类
     */
    private String feeType;

    /**
     * 退款原因
     */
    private String refundDesc;

    /**
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效
     */
    private String notifyUrl;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public Long getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Long refundFee) {
        this.refundFee = refundFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }
}
