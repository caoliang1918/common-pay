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
    private UnionPayServiceImpl unionPayService;
    private CmbPayServiceImpl cmbPayService;

    public CommonPayImpl(Config config) {
        this.aliPayService = new AliPayServiceImpl(config);
        this.wxPayService = new WxPayServiceImpl(config);
        this.unionPayService = new UnionPayServiceImpl(config);
        this.cmbPayService = new CmbPayServiceImpl(config);
    }

    @Override
    public PayResp pay(PayRequest payRequest) {
        return getInstance(payRequest.getChannel()).pay(payRequest);
    }

    @Override
    public OrderQueryResp queryOrder(OrderRequest orderRequest) {
        return getInstance(orderRequest.getChannel()).queryOrder(orderRequest);
    }

    @Override
    public CloseOrderResp closeOrder(OrderRequest orderRequest) {
        return getInstance(orderRequest.getChannel()).closeOrder(orderRequest);
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        return getInstance(refundRequest.getChannel()).refund(refundRequest);
    }

    @Override
    public boolean webhooksVerify(VerifyRequest verifyRequest) {
        return getInstance(verifyRequest.getChannel()).webhooksVerify(verifyRequest);
    }

    private CommonPay getInstance(Channel channel) {
        if (channel == null) {
            throw new PayException(ErrorCode.PAY_CHANNEL_ERROR);
        }
        switch (channel) {
            case WX_PAY:
                return wxPayService;
            case ALI_PAY:
                return aliPayService;
            case UNION_PAY:
                return unionPayService;
            case CMB_PAY:
                return cmbPayService;
            default:
                throw new PayException(ErrorCode.PAY_CHANNEL_ERROR);
        }
    }
}
