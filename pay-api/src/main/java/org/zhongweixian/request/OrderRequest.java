package org.zhongweixian.request;

/**
 * Created by caoliang on 2018/8/22
 */
public class OrderRequest extends BaseRequest {

    /**
     * 商户订单ID
     */
    private String orderNo;

    /**
     * 支付平台ID
     */
    private String orderId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
