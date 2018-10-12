package org.zhongweixian.service.impl;

import com.zhongweixian.hmac.HmacUtil;
import com.zhongweixian.md5.Md5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.http.HttpClientBuild;
import org.zhongweixian.request.*;
import org.zhongweixian.request.wxpay.WxCloseOrderXml;
import org.zhongweixian.request.wxpay.WxOrderQueryXml;
import org.zhongweixian.request.wxpay.WxPayRequestXml;
import org.zhongweixian.request.wxpay.WxRefundXml;
import org.zhongweixian.response.CloseOrderResp;
import org.zhongweixian.response.OrderQueryResp;
import org.zhongweixian.response.PayResp;
import org.zhongweixian.response.RefundResp;
import org.zhongweixian.response.wxpay.WxCloseOrderResp;
import org.zhongweixian.response.wxpay.WxOrderQueryResp;
import org.zhongweixian.response.wxpay.WxPayResp;
import org.zhongweixian.response.wxpay.WxRefundResp;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.util.DateUtil;
import org.zhongweixian.util.MapUtil;
import org.zhongweixian.util.XMLConverUtil;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/5/2018
 */
public class WxPayServiceImpl implements CommonPay {
    private Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    private final static String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private final static String WX_MICRO_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
    private final static String WX_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    private final static String WX_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    private final static String SUCCESS = "SUCCESS";
    private final static String OK = "OK";
    private final static String SIGN_TYPE = "HMAC-SHA256";
    private Config config;

    public WxPayServiceImpl(Config config) {
        this.config = config;
    }


    /**
     * 微信统一下单，其中支付方式不走统一下单
     *
     * @param payRequest
     * @return
     */
    @Override
    public PayResp pay(PayRequest payRequest) {
        if (!Channel.WX_PAY.equals(payRequest.getChannel())) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, "支付方式channle错误");
        }
        if (payRequest.getAmount() < 1L) {
            throw new PayException(ErrorCode.PAY_AMOUNT_ERROR);
        }
        switch (payRequest.getPayType()) {
            case WX_MICRO:
                return microPay(payRequest);
            case WX_H5:
                return h5Pay(payRequest);
            case WX_JS:
                return jsPay(payRequest);
            case WX_QRCODE:
                return qrcodePay(payRequest);
            case WX_APP:
                return appPay(payRequest);
            default:
                throw new PayException(ErrorCode.PAY_TYPE_ERROR, payRequest.getPayType().name());
        }
    }

    @Override
    public OrderQueryResp queryOrder(OrderRequest orderRequest) {
        if (StringUtils.isBlank(orderRequest.getOrderNo()) || StringUtils.isBlank(orderRequest.getOrderId())) {
            throw new PayException(ErrorCode.PAY_ORDER_NO_NULL);
        }
        WxOrderQueryXml orderQueryXml = new WxOrderQueryXml();
        orderQueryXml.setAppid(config.getWxAppId());
        orderQueryXml.setMch_id(config.getWxMchId());
        orderQueryXml.setTransaction_id(orderRequest.getOrderId());
        orderQueryXml.setOut_trade_no(orderRequest.getOrderNo());
        String random = RandomStringUtils.randomAlphabetic(32);
        orderQueryXml.setNonce_str(random);
        orderQueryXml.setSign_type(SIGN_TYPE);
        orderQueryXml.setSign(sign(orderQueryXml));
        /**
         * 解析XML
         */
        String xmlRequest = XMLConverUtil.objToXml(orderQueryXml);
        ;

        String result = new HttpClientBuild().postExchange(WX_ORDER_QUERY_URL, "text/xml", xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.PAY_RESPONSE_NULL);
        }
        WxOrderQueryResp wxOrderQueryResp = XMLConverUtil.xmlToObject(result, WxOrderQueryResp.class);
        ;
        if (!SUCCESS.equals(wxOrderQueryResp.getReturn_code()) || !SUCCESS.equals(wxOrderQueryResp.getResult_code())) {
            logger.error("orderQuery error :{}", wxOrderQueryResp);
            throw new PayException(ErrorCode.ORDER_QUERY_ERRPR);
        }
        logger.info("queryOrder success ,orderNo:{} , transaction_id:{} , trade_type:{}", wxOrderQueryResp.getOut_trade_no(), wxOrderQueryResp.getTransaction_id(), wxOrderQueryResp.getTrade_type());
        //转换公共对象
        OrderQueryResp orderQueryResp = new OrderQueryResp();
        orderQueryResp.setAmount(wxOrderQueryResp.getTotal_fee());
        orderQueryResp.setOrderNo(wxOrderQueryResp.getOut_trade_no());
        orderQueryResp.setThirdOrderNo(wxOrderQueryResp.getTransaction_id());
        orderQueryResp.setUts(DateUtil.parseDate(wxOrderQueryResp.getTime_end(), DateUtil.DATETIME_FORMAT_S));
        orderQueryResp.setState(wxOrderQueryResp.getTrade_state());
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("device_info", wxOrderQueryResp.getDevice_info());
        ext.put("customerId", wxOrderQueryResp.getOpenid());
        ext.put("is_subscrbe", wxOrderQueryResp.getIs_subscrbe());
        ext.put("trade_type", wxOrderQueryResp.getTrade_type());
        ext.put("bank_type", wxOrderQueryResp.getBank_type());
        ext.put("attach", wxOrderQueryResp.getAttach());
        ext.put("trade_state_desc", wxOrderQueryResp.getTrade_state_desc());
        orderQueryResp.setExt(ext);
        return orderQueryResp;
    }

    @Override
    public CloseOrderResp closeOrder(OrderRequest orderRequest) {
        WxCloseOrderXml wxCloseOrderXml = new WxCloseOrderXml();
        wxCloseOrderXml.setAppid(config.getWxAppId());
        wxCloseOrderXml.setMch_id(config.getWxMchId());
        wxCloseOrderXml.setNonce_str(RandomStringUtils.randomAlphabetic(32));
        wxCloseOrderXml.setOut_trade_no(orderRequest.getOrderNo());
        wxCloseOrderXml.setSign_type(SIGN_TYPE);
        wxCloseOrderXml.setSign(sign(wxCloseOrderXml));
        /**
         * 构建XML
         */
        String xmlRequest = XMLConverUtil.objToXml(wxCloseOrderXml);
        ;
        Map<String, String> header = new HashMap<String, String>();
        String result = new HttpClientBuild().postExchange(WX_ORDER_QUERY_URL, "text/xml", xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.ORDER_CLOSE_ERROR);
        }
        /**
         * 解析XML
         */
        WxCloseOrderResp wxCloseOrderResp = XMLConverUtil.xmlToObject(result, WxCloseOrderResp.class);
        ;
        CloseOrderResp closeOrderResp = new CloseOrderResp();
        closeOrderResp.setOrderNo(orderRequest.getOrderNo());
        if (!SUCCESS.equals(wxCloseOrderResp.getReturn_code())) {
            logger.error("order close error :{}", wxCloseOrderResp);
            closeOrderResp.setCode("500");
            closeOrderResp.setErrorCode(wxCloseOrderResp.getErr_code());
            closeOrderResp.setErrorDes(wxCloseOrderResp.getErr_code_des());
            closeOrderResp.setMsg(wxCloseOrderResp.getErr_code_des());
            return closeOrderResp;
        }
        return closeOrderResp;
    }

    /**
     * 退款，TLS双向认证
     *
     * @param refundRequest
     * @return
     */
    @Override
    public RefundResp refund(RefundRequest refundRequest) {
        WxRefundXml wxRefundXml = new WxRefundXml();
        wxRefundXml.setAppid(config.getWxAppId());
        wxRefundXml.setMch_id(config.getWxMchId());
        //商户支付订单号
        wxRefundXml.setOut_trade_no(refundRequest.getOrderNo());
        //商户退款单号
        wxRefundXml.setOut_refund_no(refundRequest.getRefundNo());
        wxRefundXml.setNotify_url(refundRequest.getNotifyUrl());
        wxRefundXml.setRefund_desc(refundRequest.getRefundDesc());
        wxRefundXml.setRefund_fee(refundRequest.getRefundFee());
        wxRefundXml.setTotal_fee(refundRequest.getTotalFee());
        //微信支付单号
        wxRefundXml.setTransaction_id(refundRequest.getOrderId());
        /**
         * 构建XML
         */
        String xmlRequest = XMLConverUtil.objToXml(wxRefundXml);
        ;
        String result = new HttpClientBuild(config.getWxKey(), config.getCertPath()).postExchange(WX_REFUND_URL, "text/xml", xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.ORDER_REFUND_ERROR);
        }
        /**
         * 解析XML
         */
        WxRefundResp wxRefundResp = XMLConverUtil.xmlToObject(result, WxRefundResp.class);
        ;
        RefundResp refundResp = new RefundResp();
        if (!SUCCESS.equals(wxRefundResp.getReturn_code())) {
            logger.error("refund order error :{}", result);
            refundResp.setCode("500");
            refundResp.setErrorCode(wxRefundResp.getErr_code());
            refundResp.setMsg(wxRefundResp.getErr_code_des());
            return refundResp;
        }
        //微信退款单号
        refundResp.setRefundId(wxRefundResp.getRefund_id());
        //微信支付单号
        refundResp.setOrderId(wxRefundResp.getTransaction_id());
        //退款金额
        refundResp.setRefundFee(Long.parseLong(wxRefundResp.getRefund_fee()));
        //商户支付单号
        refundResp.setTradeNo(wxRefundResp.getOut_trade_no());
        //商户退款单号
        refundResp.setRefundNo(wxRefundResp.getOut_refund_no());
        //订单总金额
        refundResp.setTotalFee(Long.parseLong(wxRefundResp.getTotal_fee()));
        return refundResp;
    }

    @Override
    public boolean webhooksVerify(VerifyRequest verifyRequest) {
        Map<String, String> map = MapUtil.objectToMap(verifyRequest.getBody());
        String sign = verifyRequest.getSignature();
        if (StringUtils.isBlank(sign)) {
            throw new PayException(ErrorCode.SIGN_ERROR);
        }
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + verifyRequest.getKey();
        result = Md5Util.encrypt(result).toUpperCase();
        return result.equals(sign);
    }

    /**
     * 刷卡支付
     * 步骤1：用户选择刷卡支付付款并打开微信，进入“我”->“钱包”->“收付款”条码界面；
     * <p>
     * 步骤2：收银员在商户系统操作生成支付订单，用户确认支付金额；
     * <p>
     * 步骤3：商户收银员用扫码设备扫描用户的条码/二维码，商户收银系统提交支付；
     * <p>
     * 步骤4：微信支付后台系统收到支付请求，根据验证密码规则判断是否验证用户的支付密码，不需要验证密码的交易直接发起扣款，
     * 需要验证密码的交易会弹出密码输入框。支付成功后微信端会弹出成功页面，支付失败会弹出错误提示。
     */
    private PayResp microPay(PayRequest payRequest) {
        //二维码中的授权码
        String authCode = payRequest.getExtMap().get("authCode");
        if (StringUtils.isBlank(authCode) || authCode.length() != 18) {
            throw new PayException(ErrorCode.PAY_WX_AUTH_CODE_ERROR);
        }
        PayResp response = new PayResp();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        pay2Wx(payRequest, wxPayRequestXml);
        wxPayRequestXml.setNotify_url(null);
        wxPayRequestXml.setAuth_code(String.valueOf(authCode));
        //签名运算
        wxPayRequestXml.setSign(sign(wxPayRequestXml));
        wxPayRequestXml.setRequestUrl(WX_MICRO_PAY_URL);
        /**
         * 支付请求
         */
        WxPayResp wxResponse = createCharge(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", config.getWxAppId());
        ext.put("mchId", config.getWxMchId());
        response.setExt(ext);
        response.setOrderId(wxResponse.getTransaction_id());
        response.setOrderNo(payRequest.getOrderNo());
        response.setAmount(payRequest.getAmount());
        response.setMsg(wxResponse.getReturn_msg());
        return response;
    }

    /**
     * APP支付
     * 步骤1：用户进入商户APP，选择商品下单、确认购买，进入支付环节。商户服务后台生成支付订单，签名后将数据传输到APP端。以微信提供的DEMO为例。
     * <p>
     * 步骤2：用户点击后发起支付操作，进入到微信界面，调起微信支付，出现确认支付界面。
     * <p>
     * 步骤3：用户确认收款方和金额，点击立即支付后出现输入密码界面，可选择零钱或银行卡支付。
     *
     * @param payRequest
     * @return
     */
    private PayResp appPay(PayRequest payRequest) {
        PayResp response = new PayResp();
        payRequest.toBuilder().setNotifyUrl(payRequest.getNotifyUrl() == null ? config.getNotifyUrl() : payRequest.getNotifyUrl());
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        pay2Wx(payRequest, wxPayRequestXml);
        //签名运算
        wxPayRequestXml.setSign(sign(wxPayRequestXml));
        wxPayRequestXml.setRequestUrl(WX_PAY_URL);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxPayResp wxResponse = createCharge(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", config.getWxAppId());
        ext.put("mchId", config.getWxMchId());
        ext.put("prepayId", wxResponse.getPrepay_id());
        response.setExt(ext);
        response.setAmount(payRequest.getAmount());
        return response;
    }

    /**
     * H5支付是指商户在微信客户端外的移动端网页展示商品或服务，用户在前述页面确认使用微信支付时，商户发起本服务呼起微信客户端进行支付。
     * <p>
     * 主要用于触屏版的手机浏览器请求微信支付的场景。可以方便的从外部浏览器唤起微信支付
     *
     * @param payRequest
     * @return
     */
    private PayResp h5Pay(PayRequest payRequest) {
        PayResp response = new PayResp();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        payRequest.toBuilder().setNotifyUrl(payRequest.getNotifyUrl() == null ? config.getNotifyUrl() : payRequest.getNotifyUrl());
        //通用请求对象转换xml对象
        pay2Wx(payRequest, wxPayRequestXml);
        //签名运算
        wxPayRequestXml.setSign(sign(wxPayRequestXml));
        wxPayRequestXml.setRequestUrl(WX_PAY_URL);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxPayResp wxResponse = createCharge(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", config.getWxAppId());
        ext.put("mchId", config.getWxMchId());
        ext.put("prepayId", wxResponse.getPrepay_id());
        ext.put("mwebUrl", wxResponse.getMweb_url());
        response.setExt(ext);
        response.setAmount(payRequest.getAmount());
        return response;
    }

    /**
     * 用户扫描商户展示在各种场景的二维码进行支付。
     * <p>
     * 步骤1：商户根据微信支付的规则，为不同商品生成不同的二维码（如图6.1），展示在各种场景，用于用户扫描购买。
     * <p>
     * 步骤2：用户使用微信“扫一扫”（如图6.2）扫描二维码后，获取商品支付信息，引导用户完成支付（如图6.3）。
     *
     * @param payRequest
     * @return
     */
    private PayResp qrcodePay(PayRequest payRequest) {
        PayResp response = new PayResp();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        payRequest.toBuilder().setNotifyUrl(payRequest.getNotifyUrl() == null ? config.getNotifyUrl() : payRequest.getNotifyUrl());
        //通用请求对象转换xml对象
        pay2Wx(payRequest, wxPayRequestXml);
        //签名运算
        wxPayRequestXml.setSign(sign(wxPayRequestXml));
        wxPayRequestXml.setRequestUrl(WX_PAY_URL);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxPayResp wxResponse = createCharge(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", config.getWxAppId());
        ext.put("mchId", config.getWxMchId());
        ext.put("prepayId", wxResponse.getPrepay_id());
        //trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
        ext.put("qrCode", wxResponse.getCode_url());
        response.setExt(ext);
        response.setAmount(payRequest.getAmount());
        response.setOrderNo(payRequest.getOrderNo());
        return response;
    }

    private PayResp jsPay(PayRequest payRequest) {
        PayResp response = new PayResp();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        payRequest.toBuilder().setNotifyUrl(payRequest.getNotifyUrl() == null ? config.getNotifyUrl() : payRequest.getNotifyUrl());
        //通用请求对象转换xml对象
        pay2Wx(payRequest, wxPayRequestXml);

        String openid = payRequest.getExt().get("openid");
        if (openid == null) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, "openid不能为空");
        }
        wxPayRequestXml.setOpenid(openid);
        //签名运算
        wxPayRequestXml.setSign(sign(wxPayRequestXml));
        wxPayRequestXml.setRequestUrl(StringUtils.isBlank(payRequest.getReturnUrl()) ? WX_PAY_URL : payRequest.getReturnUrl());
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxPayResp wxResponse = createCharge(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", config.getWxAppId());
        ext.put("mchId", config.getWxMchId());
        ext.put("prepayId", wxResponse.getPrepay_id());
        ext.put("tradeType", wxResponse.getTrade_type());
        response.setExt(ext);
        response.setAmount(payRequest.getAmount());
        return response;
    }


    /**
     * 构建公共参数
     *
     * @param payRequest
     * @param wxPayRequestXml
     */
    private void pay2Wx(PayRequest payRequest, WxPayRequestXml wxPayRequestXml) {
        wxPayRequestXml.setAppid(config.getWxAppId());
        wxPayRequestXml.setMch_id(config.getWxMchId());
        wxPayRequestXml.setNotify_url(payRequest.getNotifyUrl());
        wxPayRequestXml.setTrade_type(payType2String(payRequest.getPayType().getNumber()));
        String random = RandomStringUtils.randomAlphabetic(32);
        wxPayRequestXml.setNonce_str(random);
        wxPayRequestXml.setSign_type(SIGN_TYPE);
        wxPayRequestXml.setBody(payRequest.getBody());
        wxPayRequestXml.setOut_trade_no(payRequest.getOrderNo());
        wxPayRequestXml.setTotal_fee(String.valueOf(payRequest.getAmount()));
        wxPayRequestXml.setSpbill_create_ip(payRequest.getClientIp());
        if (payRequest.getExtMap().containsKey("scene_info")) {
            wxPayRequestXml.setScene_info(payRequest.getExtMap().get("scene_info"));
        }
    }

    /**
     * 转换成微信能识别的支付类型
     *
     * @param num
     * @return
     */
    private String payType2String(int num) {
        switch (num) {
            case 5:
                return null;
            case 6:
                return "MWEB";
            case 7:
                return "JSAPI";
            case 8:
                return "NATIVE";
            case 9:
                return "APP";
            default:
                return null;
        }
    }


    /**
     * 单向签名
     *
     * @param obj
     * @return
     */
    private String sign(Object obj) {
        Map<String, String> map = MapUtil.objectToMap(obj);
        map = MapUtil.order(map);
        try {
            //最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行hash运算
            map.put("key", config.getWxKey());
            String sign = HmacUtil.hmacSha256Hex(MapUtil.mapJoin(map, false, false), config.getWxKey());
            return sign;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通用请求接口
     *
     * @param wxPayRequestXml
     * @return
     */
    private WxPayResp createCharge(WxPayRequestXml wxPayRequestXml) {
        String requestUrl = wxPayRequestXml.getRequestUrl();
        wxPayRequestXml.setRequestUrl(null);
        String xmlRequest = XMLConverUtil.objToXml(wxPayRequestXml);
        logger.debug("pay request:{}", xmlRequest);
        String result = new HttpClientBuild().postExchange(requestUrl, "text/xml", xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.PAY_RESPONSE_NULL);
        }
        logger.debug("pay response:{}", result);
        WxPayResp wxResponse = XMLConverUtil.xmlToObject(result, WxPayResp.class);
        if (!SUCCESS.equals(wxResponse.getResult_code()) || !OK.equals(wxResponse.getReturn_msg())) {
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, StringUtils.isEmpty(wxResponse.getErr_code_des()) ? wxResponse.getReturn_msg() : wxResponse.getErr_code_des());
        }
        logger.info("wxReponse success :orderNo:{} , prepay_id:{} , trade_type:{}", wxPayRequestXml.getOut_trade_no(), wxResponse.getPrepay_id(), wxResponse.getTrade_type());
        return wxResponse;
    }
}
