package org.zhongweixian.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.service.BasePayService;

/**
 * Created by caoliang on  6/5/2018
 */
public class AliPayServiceImpl extends BasePayService {
    private Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);


    public AliPayServiceImpl(String wxAppid, String aliPayMerchantId, String aliPaySecret, String aliPayNotifyUrl, String aliPayReturenUrl, String wxPayMerchantid, String wxPaySecret, String wxPayNotifyUrl, String wxPayReturenUrl) {
        super(wxAppid, aliPayMerchantId, aliPaySecret, aliPayNotifyUrl, aliPayReturenUrl, wxPayMerchantid, wxPaySecret, wxPayNotifyUrl, wxPayReturenUrl);
    }

    @Override
    public PayResp pay(PayRequest payRequest) {


        return null;
    }

    @Override
    public OrderQueryResp queryOrder(String orderNo, String thirdOrderNo) {
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(thirdOrderNo)){
            throw new PayException(ErrorCode.PAY_ORDER_NO_NULL);
        }
        return null;
    }

    @Override
    public CloseOrderResp closeOrder(String orderNo) {
        return null;
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        return null;
    }

    @Override
    public boolean webhooksVerify(String body, String signature, String publickey) {
        return false;
    }


}
