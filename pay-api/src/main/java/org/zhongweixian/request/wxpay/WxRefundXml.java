package org.zhongweixian.request.wxpay;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by caoliang on 2018/7/21
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxRefundXml extends BaseReqXml {

    /**
     * 商户订单号
     */
    @XmlElement
    private String out_trade_no;

    /**
     * 商户退款单号
     */
    @XmlElement
    private String out_refund_no;

    /**
     * 订单金额
     */
    @XmlElement
    private Long total_fee;

    /**
     * 退款金额
     */
    @XmlElement
    private Long refund_fee;

    @XmlElement
    private String refund_fee_type;

    @XmlElement
    private String refund_desc;

    @XmlElement
    private String notify_url;

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Long getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Long total_fee) {
        this.total_fee = total_fee;
    }

    public Long getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Long refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public void setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = StringUtils.isBlank(refund_fee_type) ? null : refund_fee_type.trim();
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = StringUtils.isBlank(notify_url) ? null : notify_url.trim();
    }
}
