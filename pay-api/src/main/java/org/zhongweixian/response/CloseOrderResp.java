package org.zhongweixian.response;

/**
 * Created by caoliang on 2018/7/21
 */
public class CloseOrderResp extends BaseResp {
    /**
     * 商户订单号
     */
    private String orderNo;

    private String errorCode;

    private String errorDes;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }
}
