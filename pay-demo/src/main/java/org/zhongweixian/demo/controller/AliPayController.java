package org.zhongweixian.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhongweixian.demo.conf.AutoConfig;
import org.zhongweixian.demo.entity.CommonResponse;
import org.zhongweixian.request.Channel;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.PayType;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.util.SnowFlakeIdGenerator;

/**
 * Created by caoliang on 2018/8/31
 */
@RestController
@RequestMapping("ali")
public class AliPayController {
    private Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private CommonPay commonPay;

    @Autowired
    AutoConfig autoConfig;


    /**
     * 商家展示二维码
     *
     * @return
     */
    @PostMapping("qrcode")
    public CommonResponse<PayResp> qrcodePay() {
        PayRequest payRequest = PayRequest.newBuilder()
                .setChannel(Channel.ALI_PAY)
                .setPayType(PayType.ALI_PRE_CREATE)
                .setAmount(100L)
                .setOrderNo(Long.toString(new SnowFlakeIdGenerator().nextId()))
                .setBody("王吉吉快来扫啊")
                .setClientIp("123.12.12.123")
                .build();
        PayResp payResp = commonPay.pay(payRequest);
        return new CommonResponse<PayResp>(payResp);
    }


    @PostMapping("test")
    public void test() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", autoConfig.aliAppId, autoConfig.privateKey, "json", "GBK", autoConfig.aliPublicKey, "RSA2");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"20150320010101001\"," +
                "\"seller_id\":\"2088102146225135\"," +
                "\"total_amount\":88.88," +
                "\"discountable_amount\":8.88," +
                "\"subject\":\"Iphone6 16G\"," +
                "\"body\":\"Iphone6 16G\"," +
                "\"buyer_id\":\"2088102146225135\"," +
                "      \"goods_detail\":[{" +
                "        \"goods_id\":\"apple-01\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "        }]," +
                "\"operator_id\":\"Yx_001\"," +
                "\"store_id\":\"NJ_001\"," +
                "\"terminal_id\":\"NJ_T_001\"," +
                "\"extend_params\":{" +
                "\"sys_service_provider_id\":\"2088511833207846\"," +
                "\"industry_reflux_info\":\"{\\\\\\\"scene_code\\\\\\\":\\\\\\\"metro_tradeorder\\\\\\\",\\\\\\\"channel\\\\\\\":\\\\\\\"xxxx\\\\\\\",\\\\\\\"scene_data\\\\\\\":{\\\\\\\"asset_name\\\\\\\":\\\\\\\"ALIPAY\\\\\\\"}}\"," +
                "\"card_type\":\"S0JP0000\"" +
                "    }," +
                "\"timeout_express\":\"90m\"," +
                "\"settle_info\":{" +
                "        \"settle_detail_infos\":[{" +
                "          \"trans_in_type\":\"cardSerialNo\"," +
                "\"trans_in\":\"A0001\"," +
                "\"summary_dimension\":\"A0001\"," +
                "\"amount\":0.1" +
                "          }]" +
                "    }," +
                "\"business_params\":\"{\\\"data\\\":\\\"123\\\"}\"" +
                "  }");
        AlipayTradeCreateResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
