package org.zhongweixian.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.request.*;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.service.CommonPay;

/**
 * Created by caoliang on 2018/9/10
 */
public class CmbPayServiceImpl implements CommonPay {
    private Logger logger = LoggerFactory.getLogger(CmbPayServiceImpl.class);

    private Config config;

    public CmbPayServiceImpl(Config config) {
        this.config = config;
    }

    @Override
    public PayResp pay(PayRequest payRequest) {
        return null;
    }

    @Override
    public OrderQueryResp queryOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public CloseOrderResp closeOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        return null;
    }

    @Override
    public String webhooksVerify(VerifyRequest verifyRequest) {
        return null;
    }
}
