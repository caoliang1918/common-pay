package org.zhongweixian.request;

import org.hibernate.validator.constraints.NotBlank;
import org.zhongweixian.model.PayType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayRequest extends BaseRequest {

    /**
     * 付款类型
     **/
    @NotBlank(message = "付款类型不能为空")
    private PayType payType;
    /**
     * 支付金额
     **/
    @NotBlank(message = "金额不能为空")
    private Long amount;
    /**
     * 订单号
     **/
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    /**
     * 客户端IP
     **/
    @NotBlank(message = "客户端ip不能为空")
    private String clientIp;
    /**
     * 货币种类
     **/
    private String feeType;
    /**
     * 商品描述
     **/
    @NotBlank(message = "商品描述不能为空")
    private String body;

    /**
     * 详情
     */
    private String detail;
    /**
     * 签名
     **/
    @NotBlank(message = "签名不能为空")
    private String sing;
    /**
     * 签名类型
     **/
    @NotBlank(message = "签名类型不能为空")
    private String signType;
    /**
     * 随机字符串
     **/
    @NotBlank(message = "随机字符串不能为空")
    private String random;

    /**
     * 请求地址(可以不填)
     */
    private String requestUrl;

    /**
     * 后端通知地址:如果不填，则从配置文件中取
     */
    private String notifyUrl;

    /**
     * 前端返回地址
     */
    private String returnUrl;
    /** **/
    private Date cts;

    private Map<String, String> ext = new HashMap<>();


    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Date getCts() {
        return cts;
    }

    public void setCts(Date cts) {
        this.cts = cts;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }
}
