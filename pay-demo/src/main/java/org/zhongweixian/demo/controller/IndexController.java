package org.zhongweixian.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhongweixian.demo.entity.CommonResponse;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.util.SnowFlakeIdGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on 2018/8/25
 */

@RestController
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CommonPay commonPay;

    /**
     * 商家展示二维码
     *
     * @return
     */
    @PostMapping("qrcode")
    public CommonResponse<PayResp> qrcodePay() {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_QRCODE);
        payRequest.setAmount(100L);
        payRequest.setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId()));
        payRequest.setBody("王吉吉快来扫啊");
        payRequest.setClientIp("123.12.12.123");
        payRequest.setNotifyUrl("https://www.google.com");
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }

    /**
     * 微信展示二维码，商家扫码1312510133026816
     *
     * @return
     */
    @PostMapping("face")
    public CommonResponse<PayResp> facePay(String authCode) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_MICRO);
        payRequest.setAmount(2L);
        payRequest.setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId()));
        payRequest.setBody("王吉吉快来扫啊");
        payRequest.setClientIp("123.12.12.123");
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("authCode", authCode);
        payRequest.setExt(ext);
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }
    @PostMapping("app")
    public CommonResponse<PayResp> appPay(String authCode) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_APP);
        payRequest.setAmount(1L);
        payRequest.setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId()));
        payRequest.setBody("王吉吉快来扫啊");
        payRequest.setClientIp("123.12.12.123");
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("authCode", authCode);
        payRequest.setExt(ext);
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }


}
