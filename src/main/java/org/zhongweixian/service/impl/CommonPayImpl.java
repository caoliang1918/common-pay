package org.zhongweixian.service.impl;

import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.request.*;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.service.CommonPay;

/**
 * Created by caoliang on  6/5/2018
 */
public class CommonPayImpl implements CommonPay {

    private AliPayServiceImpl aliPayService;
    private WxPayServiceImpl wxPayService;

    public CommonPayImpl(Config config) {
        this.aliPayService = new AliPayServiceImpl(config);
        this.wxPayService = new WxPayServiceImpl(config);
    }

    @Override
    public PayResp pay(PayRequest payRequest) {
        return getInstance(payRequest).pay(payRequest);
    }

    @Override
    public OrderQueryResp queryOrder(OrderRequest orderRequest) {
        return getInstance(orderRequest).queryOrder(orderRequest);
    }

    @Override
    public CloseOrderResp closeOrder(OrderRequest orderRequest) {
        return getInstance(orderRequest).closeOrder(orderRequest);
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        return getInstance(refundRequest).refund(refundRequest);
    }

    @Override
    public boolean webhooksVerify(VerifyRequest verifyRequest) {
        return getInstance(verifyRequest).webhooksVerify(verifyRequest);
    }

    private CommonPay getInstance(BaseRequest baseRequest) {
        if (baseRequest == null || baseRequest.getChannel() == null) {
            throw new PayException(ErrorCode.PAY_CHANNEL_ERROR);
        }
        switch (baseRequest.getChannel()) {
            case WX_PAY:
                return wxPayService;
            case ALI_PAY:
                return aliPayService;
            default:
                throw new PayException(ErrorCode.PAY_CHANNEL_ERROR);
        }
    }
}
