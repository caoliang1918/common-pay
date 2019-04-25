package org.zhongweixian.response;

import java.util.Date;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayResp extends BaseResp {

    /**
     * 支付平台的单号
     */
    private String orderId;

    /**
     * 商家的支付单号
     */
    private String orderNo;

    /**
     * 支付时间
     */
    private Date cts;

    /**
     * 支付金额
     */
    private Long amount;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCts() {
        return cts;
    }

    public void setCts(Date cts) {
        this.cts = cts;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        super.toString();
        return "PayResp{" +
                "orderId='" + orderId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", cts=" + cts +
                ", amount=" + amount +
                '}';
    }
}
