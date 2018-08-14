package org.zhongweixian.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.model.Channel;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.RefundRequest;
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
    private final static String SIGN_TYPE = "RSA2";
    private final static String VERSION = "1.0";
    private AlipayClient alipayClient = null;


    public AliPayServiceImpl(String wxAppId, String wxMchId, String wxPaySecret, String aliPayMerchantId, String aliPaySecret, String privateKey) {
        super(wxAppId, wxMchId, wxPaySecret, aliPayMerchantId, aliPaySecret, privateKey);
        alipayClient = new DefaultAlipayClient(ALI_PAY_URL, aliPayMerchantId, privateKey, FORMAT, CHART_SET, aliPaySecret, SIGN_TYPE);
    }


    @Override
    public PayResp pay(PayRequest payRequest) {
        if (!payRequest.getChannel().equals(Channel.ALI_PAY)) {
            throw new PayException(ErrorCode.PAY_CHANNEL_ERROR, payRequest.getChannel().name());
        }
        if (payRequest.getAmount() == null || payRequest.getAmount() < 1L) {
            throw new PayException(ErrorCode.PAY_AMOUNT_ERROR);
        }
        BigDecimal amount = new BigDecimal(payRequest.getAmount());
        amount = amount.divide(new BigDecimal(100));
        switch (payRequest.getPayType()) {
            case ALI_APP:
                return appPay(payRequest, amount.toString());
            case ALI_H5:
                return h5Pay(payRequest, amount.toString());
            case ALI_WEB:
                return webPay(payRequest, amount.toString());
            case ALI_FACE:
                return facePay(payRequest, amount.toString());
            case ALI_PRE_CREATE:
                return preCreate(payRequest, amount.toString());
            default:
                throw new PayException(ErrorCode.PAY_TYPE_ERROR, payRequest.getPayType().name());
        }
    }

    /**
     * 统一收单线下交易预创建，商家生成二维码，用户扫码支付
     *
     * @param payRequest
     * @param amount
     * @return
     */
    private PayResp preCreate(PayRequest payRequest, String amount) {
        PayResp payResp = new PayResp();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", "");
        params.put("total_amount", "");
        params.put("subject", "");
        params.put("total_amount", amount);
        /**
         * 操作员
         */
        if (StringUtils.isNotBlank(payRequest.getExt().get("operatorId"))) {
            params.put("operator_id", payRequest.getExt().get("operatorId"));
        }
        /**
         * 店面
         */
        if (StringUtils.isNotBlank(payRequest.getExt().get("storeId"))) {
            params.put("store_id", payRequest.getExt().get("storeId"));
        }
        /**
         * 硬件设备编号
         */
        if (StringUtils.isNotBlank(payRequest.getExt().get("terminalId"))) {
            params.put("terminal_id", payRequest.getExt().get("terminalId"));
        }
        /**
         * 禁用渠道,当有多个渠道时用“,”分隔 [https://docs.open.alipay.com/common/wifww7]
         */
        if (StringUtils.isNotBlank(payRequest.getExt().get("disablePayChannels"))) {
            params.put("disable_pay_channels", payRequest.getExt().get("disablePayChannels"));
        }
        request.setBizContent(JSON.toJSONString(params));
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            logger.error("pay order:{} error", payRequest.getOrderNo());
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, payRequest.getPayType().name());
        }
        payResp.setAmount(payRequest.getAmount());
        payResp.setOrderNo(payRequest.getOrderNo());
        params.clear();
        params.put("qrCode", response.getQrCode());
        payResp.setExt(params);
        return payResp;
    }

    /**
     * 支付宝当面付，用户展示二维码，商家扫码提交到支付宝系统，用户收到支付请求确认支付
     *
     * @param payRequest
     * @param amount
     * @return
     */
    private PayResp facePay(PayRequest payRequest, String amount) {
        PayResp payResp = new PayResp();
        //二维码中的授权码
        String authCode = payRequest.getExt().get("authCode");
        if (StringUtils.isBlank(authCode)) {
            throw new PayException(ErrorCode.PAY_WX_AUTH_CODE_NOT_NULL);
        }
        //scene 面对面支付场景[条码支付:bar_code ;  声波支付:wave_code]
        String scene = payRequest.getExt().get("scene");
        if (StringUtils.isBlank(scene)) {
            scene = new String("bar_code");
        }
        String storeId = payRequest.getExt().get("storeId");
        String terminalId = payRequest.getExt().get("terminalId");
        //支付宝请求对象
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", payRequest.getOrderNo());
        params.put("scene", scene);
        params.put("auth_code", authCode);
        params.put("product_code", payRequest.getPayType().getValue());
        params.put("subject", payRequest.getBody());
        params.put("total_amount", amount);
        //门店编号
        if (StringUtils.isNotBlank(storeId)) {
            params.put("store_id", storeId);
        }
        //扫码硬件编号
        if (StringUtils.isNotBlank(terminalId)) {
            params.put("terminal_id", terminalId);
        }
        if (StringUtils.isNotBlank(payRequest.getBody())) {
            params.put("body", payRequest.getDetail());
        }
        request.setBizContent(JSON.toJSONString(params));
        AlipayTradePayResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, payRequest.getPayType().name());
        }
        //实收金额
        BigDecimal bigDecimal = new BigDecimal(response.getReceiptAmount());
        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
        payResp.setAmount(bigDecimal.longValue());
        payResp.setOrderNo(payRequest.getOrderNo());
        payResp.setOrderId(response.getTradeNo());
        payResp.setCts(response.getGmtPayment());
        return payResp;
    }

    /**
     * 手机APP支付,客户端直接拿body
     *
     * @param payRequest
     * @param amount
     * @return
     */
    private PayResp appPay(PayRequest payRequest, String amount) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(payRequest.getDetail());
        model.setSubject(payRequest.getBody());
        model.setOutTradeNo(payRequest.getOrderNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(amount);
        model.setProductCode(payRequest.getPayType().getValue());
        request.setBizModel(model);
        request.setNotifyUrl(payRequest.getNotifyUrl());
        AlipayTradeAppPayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
            System.out.println(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, payRequest.getPayType().name());
        }
        logger.debug("alipay appPay response : {}", response.getBody());
        PayResp resp = new PayResp();
        resp.setMsg(response.getBody());
        resp.setOrderId(response.getTradeNo());
        resp.setOrderNo(response.getOutTradeNo());
        resp.setAmount(payRequest.getAmount());
        Map<String, String> ext = new HashMap<>();
        ext.put("prepayId", response.getSellerId());
        ext.put("body", response.getBody());
        resp.setExt(ext);
        return resp;
    }


    /**
     * 手机网站支付
     *
     * @param payRequest
     * @param amount
     * @return
     */
    private PayResp h5Pay(PayRequest payRequest, String amount) {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(payRequest.getReturnUrl());
        alipayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", payRequest.getOrderNo());
        params.put("total_amount", amount);
        params.put("subject", payRequest.getBody());
        params.put("product_code", payRequest.getPayType().getValue());
        if (StringUtils.isNotBlank(payRequest.getBody())) {
            params.put("body", payRequest.getDetail());
        }
        alipayRequest.setBizContent(JSON.toJSONString(params));
        AlipayTradeWapPayResponse response = null;
        try {
            response = alipayClient.pageExecute(alipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, payRequest.getPayType().name());
        }
        PayResp resp = new PayResp();
        resp.setMsg(response.getBody());
        resp.setOrderId(response.getTradeNo());
        resp.setOrderNo(response.getOutTradeNo());
        resp.setAmount(payRequest.getAmount());
        Map<String, String> ext = new HashMap<>();
        ext.put("prepayId", response.getSellerId());
        ext.put("body", response.getBody());
        resp.setExt(ext);
        return resp;
    }

    /**
     * PC电脑端支付
     *
     * @param payRequest
     * @param amount
     * @return
     */
    private PayResp webPay(PayRequest payRequest, String amount) {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payRequest.getReturnUrl());
        alipayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", payRequest.getOrderNo());
        params.put("product_code", payRequest.getPayType().getValue());
        params.put("total_amount", amount);
        params.put("subject", payRequest.getBody());
        if (StringUtils.isNotBlank(payRequest.getBody())) {
            params.put("body", payRequest.getDetail());
        }
        alipayRequest.setBizContent(JSON.toJSONString(params));
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(alipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, payRequest.getPayType().name());
        }
        logger.debug("alipay webPay response : {}  ", response.toString());
        PayResp resp = new PayResp();
        resp.setMsg(response.getBody());
        resp.setOrderId(response.getTradeNo());
        resp.setOrderNo(response.getOutTradeNo());
        resp.setAmount(payRequest.getAmount());
        Map<String, String> ext = new HashMap<>();
        ext.put("prepayId", response.getSellerId());
        ext.put("body", response.getBody());
        resp.setExt(ext);
        return resp;
    }


    @Override
    public OrderQueryResp queryOrder(String orderNo, String thirdOrderNo) {
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(thirdOrderNo)) {
            throw new PayException(ErrorCode.PAY_ORDER_NO_NULL);
        }
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", thirdOrderNo);
        request.setBizContent(JSON.toJSONString(params));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            logger.error("orderQuery error :{}", response);
            throw new PayException(ErrorCode.ORDER_QUERY_ERRPR);
        }
        OrderQueryResp queryResp = new OrderQueryResp();
        queryResp.setOrderNo(response.getOutTradeNo());
        queryResp.setThirdOrderNo(response.getTradeNo());
        //支付宝的单位是元
        BigDecimal bigDecimal = new BigDecimal(response.getTotalAmount());
        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
        queryResp.setAmount(bigDecimal.longValue());
        queryResp.setUts(response.getSendPayDate());
        Map<String, String> ext = new HashMap<String, String>();
        //买家支付宝账号
        ext.put("customerId", response.getBuyerLogonId());
        //商家优惠
        ext.put("mdiscountAmount", response.getMdiscountAmount());
        //平台优惠
        ext.put("discountAmount", response.getDiscountAmount());
        //支付渠道
        ext.put("fundBillList", String.valueOf(response.getFundBillList()));
        queryResp.setExt(ext);
        queryResp.setState(response.getTradeStatus());
        return queryResp;
    }

    /**
     * 支付宝提供了撤销订单和关闭订单接口，从文字描述来看，撤销包含关闭，所以这里就用撤销接口了；
     * <p>
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；如果用户支付成功，支付宝系统会将此订单资金退还给用户。
     * 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】
     *
     * @param orderNo
     * @return
     */
    @Override
    public CloseOrderResp closeOrder(String orderNo) {
        CloseOrderResp closeOrderResp = new CloseOrderResp();
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        request.setBizContent(JSON.toJSONString(params));
        AlipayTradeCancelResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            if (response == null || !response.isSuccess()) {
                logger.error("close order error :{}", response);
                throw new PayException(ErrorCode.ORDER_CLOSE_ERROR);
            }
        }
        closeOrderResp.setOrderNo(response.getOutTradeNo());
        params.clear();
        params.put("tradeNo", response.getTradeNo());
        params.put("retryFlag", response.getRetryFlag());
        closeOrderResp.setExt(params);
        return closeOrderResp;
    }

    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        //支付宝只需要退款金额
        BigDecimal bigDecimal = new BigDecimal(refundRequest.getRefundFee());
        bigDecimal = bigDecimal.divide(new BigDecimal(100));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //回调通知地址
        request.setNotifyUrl(refundRequest.getNotifyUrl());
        Map<String, Object> params = new HashMap<>();
        //商户订单号
        params.put("out_trade_no", refundRequest.getOrderNo());
        //支付宝订单号
        params.put("trade_no", refundRequest.getOrderId());
        params.put("refund_amount", bigDecimal.toString());
        params.put("refund_reason", refundRequest.getRefundDesc());
        //商户退款单号
        params.put("out_request_no", refundRequest.getRefundNo());
        request.setBizContent(JSON.toJSONString(params));
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        RefundResp refundResp = new RefundResp();
        if (response == null || !response.isSuccess()) {
            logger.error("refund order:{} error ", refundRequest.getOrderNo());
            throw new PayException(ErrorCode.ORDER_REFUND_ERROR);
        }
        //支付宝退款单号
        refundResp.setRefundId(response.getTradeNo());
        //商家退款单号
        refundResp.setRefundNo(response.getOutTradeNo());
        //退款金额
        BigDecimal refundFee = new BigDecimal(response.getRefundFee());
        refundFee = refundFee.multiply(new BigDecimal(100));
        refundResp.setRefundFee(refundFee.longValue());
        refundResp.setTotalFee(refundRequest.getTotalFee());
        return refundResp;
    }

    @Override
    public boolean webhooksVerify(String body, String signature, String publickey) {
        Map<String, String> params = MapUtil.objectToMap(body);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV2(params, publickey, CHART_SET, SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return signVerified;
    }
}
