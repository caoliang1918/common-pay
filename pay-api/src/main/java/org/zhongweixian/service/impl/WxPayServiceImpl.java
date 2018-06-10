package org.zhongweixian.service.impl;

import com.zhongweixian.hmac.HmacUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;
import org.zhongweixian.http.HttpClient;
import org.zhongweixian.model.Channel;
import org.zhongweixian.model.PayType;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.request.wx.WxPayRequestXml;
import org.zhongweixian.response.PayResponse;
import org.zhongweixian.response.wx.WxResponse;
import org.zhongweixian.service.BaseBayService;
import org.zhongweixian.util.MapUtil;
import org.zhongweixian.util.XMLConverUtil;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/5/2018
 */
public class WxPayServiceImpl extends BaseBayService {
    private Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    private final static String PAY_URL = "https://api.mch.weixin.qq.com";
    private final static String SUCCESS = "SUCCESS";
    private final static String SIGN_TYPE = "HMAC-SHA256";

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
        switch (payRequest.getPayType()) {
            case WX_MICRO:
                return microPay(payRequest);
            case WX_H5:
                return h5Pay(payRequest);
            case WX_JS:
                break;
            case WX_QRCODE:
                return qrcodePay(payRequest);
            case WX_APP:
                return appPay(payRequest);
            default:
                throw new PayException(ErrorCode.PAY_TYPE_ERROR, payRequest.getPayType().getValue());
        }
        return null;
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
    private PayResponse microPay(PayRequest payRequest) {
        //二维码中的授权码
        Object authCode = payRequest.getExt().get("authCode");
        if(authCode==null){
            throw new PayException(ErrorCode.PAY_EX_AUTH_CODE_NOT_NULL);
        }
        if (String.valueOf(authCode).length()!=18){
            throw new PayException(ErrorCode.PAY_EX_AUTH_CODE_ERROR);
        }
        PayResponse response = new PayResponse();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        obj2Wx(payRequest, wxPayRequestXml);
        wxPayRequestXml.setAuth_code(String.valueOf(authCode));
        //签名运算
        sign(wxPayRequestXml);
        /**
         * 支付请求
         */
        WxResponse wxResponse = createChange(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", this.wxAppId);
        ext.put("mchId", this.wxMchId);
        ext.put("prepayId", wxResponse.getPrepay_id());
        //微信支付订单
        ext.put("transactionId", wxResponse.getTransaction_id());
        response.setExt(ext);
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
    private PayResponse appPay(PayRequest payRequest) {
        PayResponse response = new PayResponse();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        obj2Wx(payRequest, wxPayRequestXml);
        //设置支付方式
        wxPayRequestXml.setTrade_type(PayType.WX_APP.getValue());
        //签名运算
        sign(wxPayRequestXml);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxResponse wxResponse = createChange(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", this.wxAppId);
        ext.put("mchId", this.wxMchId);
        ext.put("prepayId", wxResponse.getPrepay_id());
        response.setExt(ext);
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
    private PayResponse h5Pay(PayRequest payRequest) {
        PayResponse response = new PayResponse();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        obj2Wx(payRequest, wxPayRequestXml);
        //签名运算
        sign(wxPayRequestXml);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxResponse wxResponse = createChange(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", this.wxAppId);
        ext.put("mchId", this.wxMchId);
        ext.put("prepayId", wxResponse.getPrepay_id());
        ext.put("mwebUrl", wxResponse.getMweb_url());
        response.setExt(ext);
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
    private PayResponse qrcodePay(PayRequest payRequest) {
        PayResponse response = new PayResponse();
        WxPayRequestXml wxPayRequestXml = new WxPayRequestXml();
        //通用请求对象转换xml对象
        obj2Wx(payRequest, wxPayRequestXml);
        //签名运算
        sign(wxPayRequestXml);
        /**
         * 预支付成功，构建APP支付需要的对象
         */
        WxResponse wxResponse = createChange(wxPayRequestXml);
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("appId", this.wxAppId);
        ext.put("mchId", this.wxMchId);
        ext.put("prepayId", wxResponse.getPrepay_id());
        //trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
        ext.put("codeUrl", wxResponse.getCode_url());
        response.setExt(ext);
        return response;
    }


    /**
     * 构建公共参数
     *
     * @param payRequest
     * @param wxPayRequestXml
     */
    private void obj2Wx(PayRequest payRequest, WxPayRequestXml wxPayRequestXml) {
        wxPayRequestXml.setAppid(this.wxAppId);
        wxPayRequestXml.setMch_id(this.wxMchId);
        String random = RandomStringUtils.randomAlphabetic(32);
        wxPayRequestXml.setNonce_str(random);
        wxPayRequestXml.setSign_type(SIGN_TYPE);
        wxPayRequestXml.setBody(payRequest.getBody());
        wxPayRequestXml.setOut_trade_no(payRequest.getOrderNo());
        wxPayRequestXml.setTotal_fee(payRequest.getAmount().toString());
        wxPayRequestXml.setSpbill_create_ip(payRequest.getClientIp());
        wxPayRequestXml.setNotify_url(this.wxPayNotifyUrl);
    }

    /**
     * 单向签名
     * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3
     *
     * @param wxPayRequestXml
     */
    void sign(WxPayRequestXml wxPayRequestXml) {
        Map<String, String> map = MapUtil.objectToMap(wxPayRequestXml);
        map = MapUtil.order(map);
        try {
            //最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行hash运算
            map.put("key", privateKey);
            String sign = HmacUtil.hmacSha256Hex(MapUtil.mapJoin(map, false, false), this.privateKey);
            wxPayRequestXml.setSign(sign);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用请求接口
     *
     * @param wxPayRequestXml
     * @return
     */
    private WxResponse createChange(WxPayRequestXml wxPayRequestXml) {
        String xmlRequest = XMLConverUtil.convertToXML(wxPayRequestXml);
        Map<String, String> header = new HashMap<String, String>();
        header.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        String result = new HttpClient().postExchange(PAY_URL + xmlRequest, MediaType.TEXT_XML, xmlRequest);
        if (StringUtils.isBlank(result)) {
            throw new PayException(ErrorCode.PAY_RESPONSE_NULL);
        }
        WxResponse wxResponse = XMLConverUtil.convertToObject(WxResponse.class, result);
        if (!SUCCESS.equals(wxResponse.getReturn_code()) || !SUCCESS.equals(wxResponse.getResult_code())) {
            logger.error("wxReponse error :{}", wxResponse);
            throw new PayException(ErrorCode.PAY_RESPONSE_ERROR, wxResponse.toString());
        }
        logger.info("wxReponse success :orderNo:{} , prepay_id:{} , trade_type:{}", wxPayRequestXml.getOut_trade_no(), wxResponse.getPrepay_id(), wxResponse.getTrade_type());
        return wxResponse;
    }


}
