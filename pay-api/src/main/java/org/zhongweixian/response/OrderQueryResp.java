package org.zhongweixian.response;

import java.util.Date;
import java.util.Map;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/6/13 20:26
 */
public class OrderQueryResp extends BaseResp{
    /**
     * 商户订单号
     */
    private String orderNo;
    /**
     * 支付订单号
     */
    private String thirdOrderNo;
    /**
     * 创建订单时间
     */
    private Date cts;
    /***订单支付时间*/
    private Date uts;
    /**
     * 交易状态
     */
    private String state;

    /**
     * 交易金额(分)
     */
    private Long amount;

    /**
     * 货币种类
     */
    private String feeType = "CNY";

    /**
     * 扩展数据
     */
    private Map<String, Object> ext;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    public Date getCts() {
        return cts;
    }

    public void setCts(Date cts) {
        this.cts = cts;
    }

    public Date getUts() {
        return uts;
    }

    public void setUts(Date uts) {
        this.uts = uts;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }


}
