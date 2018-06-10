package org.zhongweixian.service.impl;

import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResponse;
import org.zhongweixian.service.BaseBayService;

/**
 * Created by caoliang on  6/5/2018
 */
public class AliPayService extends BaseBayService {


    public AliPayService(String wxAppid, String aliPayMerchantId, String aliPaySecret, String aliPayNotifyUrl, String aliPayReturenUrl, String wxPayMerchantid, String wxPaySecret, String wxPayNotifyUrl, String wxPayReturenUrl) {
        super(wxAppid, aliPayMerchantId, aliPaySecret, aliPayNotifyUrl, aliPayReturenUrl, wxPayMerchantid, wxPaySecret, wxPayNotifyUrl, wxPayReturenUrl);
    }

    @Override
    public PayResponse pay(PayRequest payRequest) {
        return null;
    }
}
