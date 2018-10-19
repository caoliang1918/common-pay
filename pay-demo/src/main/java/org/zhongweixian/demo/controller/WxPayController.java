package org.zhongweixian.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhongweixian.demo.entity.CommonResponse;
import org.zhongweixian.request.Channel;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.PayType;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.util.SnowFlakeIdGenerator;

/**
 * Created by caoliang on 2018/8/25
 */

@RestController
@RequestMapping("wx")
public class WxPayController {
    Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private CommonPay commonPay;

    /**
     * 商家展示二维码
     *
     * @return
     */
    @PostMapping("qrcode")
    public CommonResponse<PayResp> qrcodePay() {
        PayRequest payRequest = PayRequest.newBuilder().
                setChannel(Channel.WX_PAY).
                setPayType(PayType.WX_QRCODE).
                setAmount(100L).
                setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId())).
                setBody("王吉吉快来扫啊").
                setClientIp("123.12.12.123").
                build();
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
        PayRequest payRequest = PayRequest.newBuilder().
                setChannel(Channel.WX_PAY).
                setPayType(PayType.WX_MICRO).
                setAmount(1L).
                setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId())).
                setBody("王吉吉快来扫啊").
                setClientIp("123.12.12.123").
                putExt("authCode", authCode).
                build();
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }


    @PostMapping("js")
    public CommonResponse<PayResp> h5Pay() {
        PayRequest payRequest = PayRequest.newBuilder().
                setChannel(Channel.WX_PAY).
                setPayType(PayType.WX_JS).
                setAmount(1L).
                setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId())).
                setBody("王吉吉快来扫啊").
                setClientIp("123.12.12.123").
                putExt("openid", "openid").
                build();
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }


}
