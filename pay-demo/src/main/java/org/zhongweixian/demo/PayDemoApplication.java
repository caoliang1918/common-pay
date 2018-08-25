package org.zhongweixian.demo;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    AutoConfig autoConfig;


    @Bean
    public CommonPay commonPayService(){
        Config config = new Config();
        BeanUtils.copyProperties(autoConfig, config);
        return new CommonPayImpl(config);

    }

    public static void main(String[] args) {
        SpringApplication.run(PayDemoApplication.class, args);
    }
}
