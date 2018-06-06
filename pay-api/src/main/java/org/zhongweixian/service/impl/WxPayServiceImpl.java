package org.zhongweixian.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.http.HttpClient;
import org.zhongweixian.model.Channel;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResponse;
import org.zhongweixian.response.wx.WxResponse;
import org.zhongweixian.service.BaseBayService;
import org.zhongweixian.util.XMLConverUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/5/2018
 */
public class WxPayServiceImpl extends BaseBayService {
    private Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    private final static String PAY_URL = "https://api.mch.weixin.qq.com";
    private final static String SUCCESS = "SUCCESS";

    public WxPayServiceImpl(String wxAppid, String aliPayMerchantId, String aliPaySecret, String aliPayNotifyUrl, String aliPayReturenUrl, String wxPayMerchantid, String wxPaySecret, String wxPayNotifyUrl, String wxPayReturenUrl) {
        super(wxAppid, aliPayMerchantId, aliPaySecret, aliPayNotifyUrl, aliPayReturenUrl, wxPayMerchantid, wxPaySecret, wxPayNotifyUrl, wxPayReturenUrl);
    }


    /**
     * 微信统一下单，其中支付方式不走统一下单
     *
     * @param payRequest
     * @return
     */
    @Override
    public PayResponse pay(PayRequest payRequest) {
        if (!payRequest.getChannel().equals(Channel.WX_PAY)) {
            return null;
        }
        switch (payRequest.getPayType()){
            case WX_MICRO:
                return microPay(payRequest);
            case WX_H5:
                break;
            case WX_JS:
                break;
            case WX_QRCODE:
                break;
            case WX_APP:
                return appPay(payRequest);
            default:
                throw new PayException(ErrorCode.PAY_TYPE_ERROR,payRequest.getPayType().getValue());
        }
        return null;
    }


    /**
     * 步骤1：用户选择刷卡支付付款并打开微信，进入“我”->“钱包”->“收付款”条码界面；
     *
     * 步骤2：收银员在商户系统操作生成支付订单，用户确认支付金额；
     *
     * 步骤3：商户收银员用扫码设备扫描用户的条码/二维码，商户收银系统提交支付；
     *
     * 步骤4：微信支付后台系统收到支付请求，根据验证密码规则判断是否验证用户的支付密码，不需要验证密码的交易直接发起扣款，
     * 需要验证密码的交易会弹出密码输入框。支付成功后微信端会弹出成功页面，支付失败会弹出错误提示。
     */
    private PayResponse microPay(PayRequest payRequest) {
        PayResponse response = new PayResponse();

        return response;
    }

    private PayResponse appPay(PayRequest payRequest){
        PayResponse payResponse = new PayResponse();
        String xmlRequest = XMLConverUtil.convertToXML(payRequest);
        Map<String, String> header = new HashMap<String, String>();
        header.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        String result = new HttpClient().postExchange(PAY_URL + xmlRequest, MediaType.TEXT_XML, xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.PAY_RESPONSE_NULL);
        }
        WxResponse wxResponse = XMLConverUtil.convertToObject(WxResponse.class, result);
        if (!SUCCESS.equals(wxResponse)) {
            logger.error("wxReponse error :{}", wxResponse);
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR , wxResponse.toString());
        }
        logger.info("wxReponse success :orderNo:{} , prepay_id:{} , trade_type:{}", payRequest.getOrderNo(), wxResponse.getPrepay_id(), wxResponse.getTrade_type());
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appid", this.wxAppid);
        ext.put("partnerid", this.wxPayMerchantid);
        ext.put("prepayid", wxResponse.getPrepay_id());
        payResponse.setExt(ext);
        return payResponse;
    }

    /**
     * H5支付是指商户在微信客户端外的移动端网页展示商品或服务，用户在前述页面确认使用微信支付时，商户发起本服务呼起微信客户端进行支付。
     * 主要用于触屏版的手机浏览器请求微信支付的场景。可以方便的从外部浏览器唤起微信支付
     * @param payRequest
     * @return
     */
    private PayResponse h5Pay(PayRequest payRequest){
        PayResponse response = new PayResponse();
        String xmlRequest = XMLConverUtil.convertToXML(payRequest);
        Map<String, String> header = new HashMap<String, String>();
        header.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        String result = new HttpClient().postExchange(PAY_URL + xmlRequest, MediaType.TEXT_XML, xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.PAY_RESPONSE_NULL);
        }
        WxResponse wxResponse = XMLConverUtil.convertToObject(WxResponse.class, result);
        if (!SUCCESS.equals(wxResponse)) {
            logger.error("wxReponse error :{}", wxResponse);
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR , wxResponse.toString());
        }
        logger.info("wxReponse success :orderNo:{} , prepay_id:{} , trade_type:{}", payRequest.getOrderNo(), wxResponse.getPrepay_id(), wxResponse.getTrade_type());
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appid", this.wxAppid);
        ext.put("partnerid", this.wxPayMerchantid);
        ext.put("prepayid", wxResponse.getPrepay_id());
        response.setExt(ext);
        return response;
    }




}
