package org.zhongweixian.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by caoliang on  6/5/2018
 */
public class AliPayServiceImpl extends BasePayService {
    private Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);
    private final static String ALI_PAY_URL = "https://openapi.alipay.com/gateway.do";
    private final  static String APP_PAY = "alipay.trade.wap.pay";
    private final static String CHART_SET = "UTF-8";
    private final static String SUCCESS = "SUCCESS";
    private final static String SIGN_TYPE = "RSA2";


    public AliPayServiceImpl(String wxAppId, String wxMchId, String wxPaySecret, String aliPayMerchantId, String aliPaySecret) {
        super(wxAppId, wxMchId, wxPaySecret, aliPayMerchantId, aliPaySecret);
    }


    @Override
    public PayResp pay(PayRequest payRequest) {
        if (!payRequest.getChannel().equals(Channel.ALI_PAY)){
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
     *
     * @param payRequest
     * @return
     */
    private PayResp appPay(PayRequest payRequest){
        JSONObject jsonObject = new JSONObject();


        return null;
    }


    public void doPost(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse) throws ServletException, IOException, AlipayApiException {
        AlipayClient alipayClient = null; //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101002\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"product_code\":\"QUICK_WAP_WAY\"" +
                "  }");//填充业务参数
        String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        httpResponse.setContentType("text/html;charset=" + CHART_SET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
    }
    /**
     *
     * @param payRequest
     * @return
     */
    private PayResp h5Pay(PayRequest payRequest){

        return null;
    }

    /**
     *
     * @param payRequest
     * @return
     */
    private PayResp webPay(PayRequest payRequest){

        return null;
    }

    private JSONObject build(PayRequest payRequest){
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("app_id" , this.aliPayMerchantId);
        jsonObject.put("charset" , CHART_SET);
        jsonObject.put("sign_type" , SIGN_TYPE);
        jsonObject.put("out_trade_no" , payRequest.getOrderNo());


        AliPayRequest aliPayRequest = new AliPayRequest();
        aliPayRequest.setNotifyUrl(payRequest.getNotifyUrl());
        aliPayRequest.setReturnUrl(payRequest.getReturnUrl());


        return jsonObject;
    }


    @Override
    public OrderQueryResp queryOrder(String orderNo, String thirdOrderNo) {
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(thirdOrderNo)){
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
