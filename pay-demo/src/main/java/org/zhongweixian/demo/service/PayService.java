package org.zhongweixian.demo.service;

import org.zhongweixian.request.OrderRequest;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.request.VerifyRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;

/**
 * Created by caoliang on 2018/8/22
 */
public interface PayService {

    /**
     * 支付
     *
     * @param payRequest
     * @return
     */
    PayResp pay(PayRequest payRequest);


    /**
     * 订单查询
     *
     * @param orderRequest
     * @return
     */
    OrderQueryResp queryOrder(OrderRequest orderRequest);


    /**
     * 退款
     *
     * @param refundRequest
     * @return
     */
    RefundResp refund(RefundRequest refundRequest);

    /**
     * 关闭订单
     *
     * @param orderRequest
     * @return
     */
    CloseOrderResp closeOrder(OrderRequest orderRequest);


    String webhooksVerify(VerifyRequest verifyRequest);
}
