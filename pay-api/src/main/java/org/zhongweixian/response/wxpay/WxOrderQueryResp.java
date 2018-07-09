package org.zhongweixian.response.wxpay;

import java.io.Serializable;

/**
 * @author : caoliang1918@gmail.com
 * @date :   2018/6/18 10:02
 */
public class WxOrderQueryResp implements Serializable {
    /**
     * 返回状态码
     */
    private String return_code;

    /**
     * 业务结果
     */
    private String result_code;

    private String device_info;
    /**
     * 用户标识	,用户在商户appid下的唯一标识
     */
    private String openid;
    /**
     * 是否关注公众账号
     */
    private String is_subscrbe;
    /**
     * 交易类型
     */
    private String trade_type;

    /**
     * 交易状态
     */
    private String trade_state;

    /**
     * 付款银行
     */
    private String bank_type;

    /**
     * 标价金额
     */
    private Long total_fee;

    /**
     * 现金支付金额
     */
    private Long cash_fee;

    private String transaction_id;

    private String out_trade_no;

    private String attach;

    /**
     * 支付完成时间
     */
    private String time_end;

    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscrbe() {
        return is_subscrbe;
    }

    public void setIs_subscrbe(String is_subscrbe) {
        this.is_subscrbe = is_subscrbe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public Long getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Long total_fee) {
        this.total_fee = total_fee;
    }

    public Long getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(Long cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }
}
