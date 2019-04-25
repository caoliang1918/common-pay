package org.zhongweixian.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.zhongweixian.request.Config;
import org.zhongweixian.service.CommonPay;
import org.zhongweixian.service.impl.CommonPayImpl;
import org.zhongweixian.test.conf.AutoConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayDemoApplicationTests {

    @Autowired
    private AutoConfig autoConfig;

    @Bean
    public CommonPay commonPayService(){
        Config config = new Config();
        BeanUtils.copyProperties(autoConfig, config);
        return new CommonPayImpl(config);
    }

    @Test
    public void contextLoads() {
    }

}
