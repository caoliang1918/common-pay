package org.zhongweixian.service;

import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;

/**
 * Created by caoliang on  6/5/2018
 */
public interface CommonPayService {


    /**
     * 统一支付接口
     *
     * @param payRequest
     * @return
     */
    PayResp pay(PayRequest payRequest);

    /**
     * 支付订单查询接口【商户订单和支付平台订单不能同时为空】
     *
     * @param orderNo
     * @param thirdOrderNo
     * @return
     */
    OrderQueryResp queryOrder(String orderNo, String thirdOrderNo);

    /**
     * 关闭订单
     *
     * @param orderNo
     * @return
     */
    CloseOrderResp closeOrder(String orderNo);


    /**
     * 退款
     *
     * @param refundRequest
     * @return
     */
    RefundResp refund(RefundRequest refundRequest);


    /**
     * 回调验证【可选】
     * @param body 回调数据
     * @param signature 回调签名
     * @param publickey 支付平台的公钥(公钥签名，私钥加密)
     */
    boolean webhooksVerify(String body , String signature , String publickey);

}
