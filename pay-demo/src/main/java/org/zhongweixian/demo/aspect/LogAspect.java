package org.zhongweixian.demo.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhongweixian.demo.entity.CommonResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on 2018/8/29
 */
@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    /**
     * 请求地址
     */
    private Map<String, Object> outputParamMap = null;
    /**
     * 存放输出结果
     */
    private Map<?, ?> inputParamMap = null;
    /**
     * 传入参数
     */
    private String requestPath = null;
    /**
     * 开始时间
     */
    private long startTimeMillis = 0;
    /**
     * 结束时间
     */
    private long endTimeMillis = 0;

    /**
     * 是否需要输出
     */
    private boolean isLog = false;

    /**
     * 定义切面点
     */
    @Pointcut("execution(* org.zhongweixian.demo.controller..*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        isLog = false;
        // 记录方法开始执行的时间
        startTimeMillis = System.currentTimeMillis();
    }

    @After("pointcut()")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        // 记录方法执行完成的时间
        endTimeMillis = System.currentTimeMillis();
        this.printLog();
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        // 获取输入参数
        inputParamMap = request.getParameterMap();
        // 获取请求地址
        requestPath = request.getRequestURI();
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        outputParamMap = new HashMap<String, Object>();
        //result的值就是被拦截方法的返回值
        Object result = proceedingJoinPoint.proceed();
        if (result instanceof CommonResponse) {
            isLog = true;
        }
        outputParamMap.put("result", result);
        return result;
    }

    /**
     * 输出日志
     */
    private void printLog() {
        logger.debug(" request url:{} , ", requestPath);
        logger.debug("请求耗时:{} ms", endTimeMillis - startTimeMillis);
        if (isLog) {
            logger.debug("request:\n{}", JSON.toJSONString(inputParamMap, true));
            logger.debug("response:\n{}", JSON.toJSONString(outputParamMap, true));
        }
    }
}
