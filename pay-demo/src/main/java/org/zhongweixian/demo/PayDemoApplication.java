package org.zhongweixian.demo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zhongweixian.demo.conf.AutoConfig;
import org.zhongweixian.request.Config;
import org.zhongweixian.service.impl.CommonPayImpl;
import org.zhongweixian.service.CommonPay;

@SpringBootApplication
public class PayDemoApplication {

    @Autowired
    private AutoConfig autoConfig;


    @Bean
    public CommonPay commonPayService() {
        Config config = Config.newBuilder()
                .setWxKey(autoConfig.getWxKey())
                .setWxAppId(autoConfig.getWxAppId())
                .setWxMchId(autoConfig.getWxMchId())
                .setAliAppId(autoConfig.getAliAppId())
                .setAliPublicKey(autoConfig.getAliPublicKey())
                .setCertPath(autoConfig.getWxCertPath())
                .setNotifyUrl(autoConfig.getNotifyUrl())
                .setPrivateKey(autoConfig.getPrivateKey()).build();
        return new CommonPayImpl(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(PayDemoApplication.class, args);
    }
}
