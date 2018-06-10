package org.zhongweixian.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.request.PayRequest;
import org.zhongweixian.response.PayResponse;
import org.zhongweixian.util.ValidationUtils;

/**
 * Created by caoliang on  6/5/2018
 */
public interface CommonPayService {


    PayResponse pay(PayRequest payRequest);

    //OrderResponse queryOrder();

}
