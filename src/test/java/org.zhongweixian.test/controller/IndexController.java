package org.zhongweixian.test.controller;

import com.zhongweixian.aes.AesUtil;
import com.zhongweixian.md5.Md5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.test.conf.AutoConfig;
import org.zhongweixian.util.XMLConverUtil;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.zhongweixian.exception.ErrorCode.ORDER_REFUND_ERROR;

/**
 * Created by caoliang on 2018/8/25
 */

@RestController
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CommonPay commonPay;

    @Autowired
    private AutoConfig config;

    /**
     * 微信扫码支付(商户展示二维码)
     *
     * @param request
     * @return
     */
    @PostMapping("wxQrcode")
    public PayResp wxQrcode(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_QRCODE);
        //1块钱
        payRequest.setAmount(100L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }

    /**
     * 微信扫码支付（用户展示二维码，商户使用扫码设备)
     *
     * @param code
     * @return
     */
    @PostMapping("wxMicro")
    public PayResp wxMicro(@RequestParam("code") String code) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_MICRO);
        //1块钱
        payRequest.setAmount(1L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        payRequest.getExt().put("authCode", code);
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }


    @PostMapping("wxApp")
    public PayResp wxApp(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_APP);
        //1分钱
        payRequest.setAmount(1L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }

    @PostMapping("wxJs")
    public PayResp wxJs(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_JS);
        //1分钱
        payRequest.setAmount(1L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        payRequest.getExt().put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }

    @PostMapping("wxH5")
    public PayResp wxH5(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.WX_PAY);
        payRequest.setPayType(PayType.WX_H5);
        //1分钱
        payRequest.setAmount(1L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        payRequest.getExt().put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"app_name\": \"h5_web支付\"}}");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }

    /**
     * 这个接口是微信调用商家后台
     *
     * @param body
     * @return
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity refundNotify(@RequestBody String body) throws IOException {
        Map<String, String> stringMap = XMLConverUtil.convertToMap(body);
        if (stringMap == null || !"SUCCESS".equals(stringMap.get("return_code"))) {
            new PayException(ORDER_REFUND_ERROR);
        }
        String req_info = stringMap.get("req_info");
        /**
         * 解密步骤如下：
         * （1）对加密串A做base64解码，得到加密串B
         * （2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
         * （3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
         */
        String decodeBase64 = new BASE64Decoder().decodeBuffer(req_info).toString();
        String requestXml = AesUtil.decryptAES(decodeBase64, Md5Util.encrypt(config.getWxKey()), null, "AES/ECB/PKCS7Padding");

        /**
         * req_info解密后的示例：
         * <root>
         * <out_refund_no><![CDATA[131811191610442717309]]></out_refund_no>
         * <out_trade_no><![CDATA[71106718111915575302817]]></out_trade_no>
         * <refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
         * <refund_fee><![CDATA[3960]]></refund_fee>
         * <refund_id><![CDATA[50000408942018111907145868882]]></refund_id>
         * <refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
         * <refund_request_source><![CDATA[API]]></refund_request_source>
         * <refund_status><![CDATA[SUCCESS]]></refund_status>
         * <settlement_refund_fee><![CDATA[3960]]></settlement_refund_fee>
         * <settlement_total_fee><![CDATA[3960]]></settlement_total_fee>
         * <success_time><![CDATA[2018-11-19 16:24:13]]></success_time>
         * <total_fee><![CDATA[3960]]></total_fee>
         * <transaction_id><![CDATA[4200000215201811190261405420]]></transaction_id>
         * </root>
         */

        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("aliWebPay")
    public PayResp aliWebPay(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setChannel(Channel.ALI_PAY);
        payRequest.setPayType(PayType.ALI_WEB);
        payRequest.setAmount(100L);
        payRequest.setOrderNo(RandomStringUtils.randomAlphabetic(32));
        payRequest.setBody("ssss");
        payRequest.setClientIp("123.12.12.123");
        PayResp payResp = commonPay.pay(payRequest);
        return payResp;
    }
}
