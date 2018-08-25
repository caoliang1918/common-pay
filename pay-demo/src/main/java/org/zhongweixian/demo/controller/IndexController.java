package org.zhongweixian.demo.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.CommonPay;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caoliang on 2018/8/25
 */

@RestController
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    CommonPay commonPay;

    @GetMapping("qrcode")
    public String qrcode(HttpServletRequest request){
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_QRCODE);
        payRequest.setAmount(100L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp.toString();
    }


}
