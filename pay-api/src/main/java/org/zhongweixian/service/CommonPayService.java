package org.zhongweixian.service;

import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;

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
    int closeOrder(String orderNo);


    int refund(RefundRequest refundRequest);



}
