package org.zhongweixian.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zhongweixian.rsa.RsaUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.model.Channel;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
import org.zhongweixian.request.alipay.AliPayRequest;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.service.BasePayService;
import org.zhongweixian.util.MapUtil;

import java.util.Map;

/**
 * Created by caoliang on  6/5/2018
 */
public class AliPayServiceImpl extends BasePayService {
    private Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);
    private final static String ALI_PAY_URL = "https://openapi.alipay.com/gateway.do";
    private final static String APP_PAY = "alipay.trade.wap.pay";
    private final static String CHART_SET = "UTF-8";
    private final static String FORMAT = "JSON";
    private final static String SUCCESS = "SUCCESS";
    private final static String SIGN_TYPE = "RSA2";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String VERSION = "1.0";
    private AlipayClient alipayClient = null;


    public AliPayServiceImpl(String wxAppId, String wxMchId, String wxPaySecret, String aliPayMerchantId, String aliPaySecret) {
        super(wxAppId, wxMchId, wxPaySecret, aliPayMerchantId, aliPaySecret);
        alipayClient = new DefaultAlipayClient(ALI_PAY_URL, aliPayMerchantId, aliPaySecret, FORMAT, CHART_SET);
    }


    @Override
    public PayResp pay(PayRequest payRequest) {
        if (!payRequest.getChannel().equals(Channel.ALI_PAY)) {
            return null;
        }
        switch (payRequest.getPayType()) {
            case ALI_APP:
                return appPay(payRequest);
            case ALI_H5:
                return h5Pay(payRequest);
            case ALI_WEB:
                return webPay(payRequest);
            default:
                throw new PayException(ErrorCode.PAY_TYPE_ERROR, payRequest.getPayType().getValue());
        }
    }

    /**
     * @param payRequest
     * @return
     */
    private PayResp appPay(PayRequest payRequest) {
        JSONObject jsonObject = new JSONObject();


        return null;
    }


    /**
     * @param payRequest
     * @return
     */
    private PayResp h5Pay(PayRequest payRequest) {

        return null;
    }

    /**
     * @param payRequest
     * @return
     */
    private PayResp webPay(PayRequest payRequest) {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payRequest.getReturnUrl());
        alipayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":" + payRequest.getOrderNo() + "," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + payRequest.getAmount() + "," +
                "    \"subject\":" + payRequest.getBody() + "," +
                "    \"body\":" + payRequest.getBody() + "," +
                "    \"extend_params\":{" +
                "    }" +
                "  }");//填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.debug("FAST_INSTANT_TRADE_PAY : {}  ", form);
        return null;
    }

    /**
     * biz_content只是业务数据
     *
     * @param bizContent
     * @param params
     * @param payRequest
     * @return
     */
    private AliPayRequest build(String bizContent, Map<String, String> params, PayRequest payRequest) {
        params.put("app_id", this.aliPayMerchantId);
        params.put("charset", CHART_SET);
        params.put("sign_type", SIGN_TYPE);
        params.put("out_trade_no", payRequest.getOrderNo());
        params.put("timestamp", new DateTime().toString(DATE_FORMAT));
        params.put("version", VERSION);
        params.put("biz_content", bizContent);

        String str = MapUtil.mapJoin(params, false, false);
        String sign = RsaUtil.sign(str, privateKey);

        AliPayRequest aliPayRequest = new AliPayRequest();


        return aliPayRequest;
    }


    @Override
    public OrderQueryResp queryOrder(String orderNo, String thirdOrderNo) {
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(thirdOrderNo)) {
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
