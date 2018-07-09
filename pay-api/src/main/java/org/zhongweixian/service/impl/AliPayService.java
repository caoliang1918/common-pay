package org.zhongweixian.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.BasePayService;

/**
 * Created by caoliang on  6/5/2018
 */
public class AliPayService extends BasePayService {


    public AliPayService(String wxAppid, String aliPayMerchantId, String aliPaySecret, String aliPayNotifyUrl, String aliPayReturenUrl, String wxPayMerchantid, String wxPaySecret, String wxPayNotifyUrl, String wxPayReturenUrl) {
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
}
