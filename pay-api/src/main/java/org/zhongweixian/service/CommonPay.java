package org.zhongweixian.service;

import org.zhongweixian.request.OrderRequest;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.request.VerifyRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;

/**
 * Created by caoliang on  6/5/2018
 */
public interface CommonPay {


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
     * @param orderRequest
     * @return
     */
    OrderQueryResp queryOrder(OrderRequest orderRequest);

    /**
     * 关闭订单
     *
     * @param orderRequest
     * @return
     */
    CloseOrderResp closeOrder(OrderRequest orderRequest);


    /**
     * 退款
     *
     * @param refundRequest
     * @return
     */
    RefundResp refund(RefundRequest refundRequest);


    /**
     * 回调验证【可选】
     */
    String webhooksVerify(VerifyRequest verifyRequest);

}
