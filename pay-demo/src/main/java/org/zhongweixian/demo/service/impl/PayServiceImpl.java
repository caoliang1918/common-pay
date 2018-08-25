package org.zhongweixian.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhongweixian.demo.service.PayService;
import org.zhongweixian.request.OrderRequest;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.service.CommonPay;

/**
 * Created by caoliang on 2018/8/22
 */
@Service
public class PayServiceImpl implements PayService {
    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);


    @Autowired
    private CommonPay commonPay;

    @Override
    public PayResp pay(PayRequest payRequest) {
        return commonPay.pay(payRequest);
    }

    @Override
    public OrderQueryResp queryOrder(OrderRequest orderRequest) {
        return commonPay.queryOrder(orderRequest);
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        return commonPay.refund(refundRequest);
    }

    @Override
    public CloseOrderResp closeOrder(OrderRequest orderRequest) {
        return commonPay.closeOrder(orderRequest);
    }
}
