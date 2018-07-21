package org.zhongweixian.response;

/**
 * Created by caoliang on 2018/7/21
 */
public class CloseOrderResp {

    /**
     * 状态码
     */
    private String code = "200";
    /**
     * 代码描述
     */
    private String msg = "SUCCESS";
    /**
     * 商户订单号
     */
    private String orderNo;

    private String errorCode;


    private String errorDes;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
