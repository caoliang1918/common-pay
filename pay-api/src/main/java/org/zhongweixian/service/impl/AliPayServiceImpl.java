package org.zhongweixian.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/5/2018
 * <p>
 * 因为支付宝没有标准的rest接口,所以就直接用sdk了
 */
public class AliPayServiceImpl extends BasePayService {
    private Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);
    private final static String ALI_PAY_URL = "https://openapi.alipay.com/gateway.do";
    private final static String CHART_SET = "UTF-8";
    private final static String FORMAT = "JSON";
    private final static String SUCCESS = "SUCCESS";
    private final static String SIGN_TYPE = "RSA2";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String VERSION = "1.0";
    private AlipayClient alipayClient = null;


    public AliPayServiceImpl(String wxAppId, String wxMchId, String wxPaySecret, String aliPayMerchantId, String aliPaySecret, String privateKey) {
        super(wxAppId, wxMchId, wxPaySecret, aliPayMerchantId, aliPaySecret, privateKey);
        alipayClient = new DefaultAlipayClient(ALI_PAY_URL, aliPayMerchantId, privateKey, FORMAT, CHART_SET, aliPaySecret, SIGN_TYPE);
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
     * 手机APP支付
     *
     * @param payRequest
     * @return
     */
    private PayResp appPay(PayRequest payRequest) {
        BigDecimal bigDecimal = new BigDecimal(payRequest.getAmount());
        bigDecimal = bigDecimal.divide(new BigDecimal(100));
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(payRequest.getDetail());
        model.setSubject(payRequest.getBody());
        model.setOutTradeNo(payRequest.getOrderNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(bigDecimal.toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(payRequest.getNotifyUrl());
        AlipayTradeAppPayResponse response = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.debug("alipay appPay response : {}", response.getBody());
        PayResp resp = new PayResp();
        resp.setMsg(response.getBody());
        return resp;
    }


    /**
     * 手机网站支付
     *
     * @param payRequest
     * @return
     */
    private PayResp h5Pay(PayRequest payRequest) {
        BigDecimal bigDecimal = new BigDecimal(payRequest.getAmount());
        bigDecimal = bigDecimal.divide(new BigDecimal(100));
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(payRequest.getReturnUrl());
        alipayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        alipayRequest.setBizContent("{" +
                "out_trade_no:" + payRequest.getOrderNo() + "," +
                "total_amount:" + bigDecimal + "," +
                "subject:" + payRequest.getBody() + "," +
                "product_code:QUICK_WAP_WAY" +
                "}");
        try {
            String form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * PC电脑端支付
     *
     * @param payRequest
     * @return
     */
    private PayResp webPay(PayRequest payRequest) {
        //货币转换
        BigDecimal bigDecimal = new BigDecimal(payRequest.getAmount());
        bigDecimal = bigDecimal.divide(new BigDecimal(100));
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payRequest.getReturnUrl());
        alipayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        alipayRequest.setBizContent("{" +
                "out_trade_no:" + payRequest.getOrderNo() + "," +
                "product_code:FAST_INSTANT_TRADE_PAY," +
                "total_amount:" + bigDecimal.toString() + "," +
                "subject:" + payRequest.getBody() + "," +
                "body:" + payRequest.getDetail() +
                "}");
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.debug("alipay webPay response : {}  ", form);
        return null;
    }


    @Override
    public OrderQueryResp queryOrder(String orderNo, String thirdOrderNo) {
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(thirdOrderNo)) {
            throw new PayException(ErrorCode.PAY_ORDER_NO_NULL);
        }
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "out_trade_no:" + orderNo + "," +
                "trade_no:" + thirdOrderNo + "" +
                "}");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        OrderQueryResp queryResp = new OrderQueryResp();
        queryResp.setOrderNo(response.getOutTradeNo());
        queryResp.setThirdOrderNo(response.getTradeNo());
        //支付宝的单位是元
        BigDecimal bigDecimal = new BigDecimal(response.getTotalAmount());
        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
        queryResp.setAmount(bigDecimal.longValue());
        Map<String, String> ext = new HashMap<String, String>();
        //买家支付宝账号
        ext.put("customerId", response.getBuyerLogonId());
        queryResp.setExt(ext);

        return queryResp;
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
