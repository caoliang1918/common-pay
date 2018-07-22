package org.zhongweixian.response;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/7/21 21:53
 */
public class RefundResp extends BaseResp {

    private String errorCode;
    private String errorMsg;
    /**
     * 第三方订单号
     */
    private String transactionId;

    /**
     * 第三方退款单号
     */
    private String refundId;
    /**
     * 商户订单号
     */
    private String tradeNo;
    /**
     * 商户退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private Long refundFee;

    /**
     * 订单总金额
     */
    private Long totalFee;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Long getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Long refundFee) {
        this.refundFee = refundFee;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }
}
