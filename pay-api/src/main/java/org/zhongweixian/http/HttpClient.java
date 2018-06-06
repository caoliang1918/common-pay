package org.zhongweixian.http;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by caoliang on  6/6/2018
 */
public class HttpClient {
    private Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private RestTemplate restTemplate;

    public HttpClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        restTemplate = new RestTemplate(factory);
    }


    public String exchange(String url, HttpMethod httpMethod, MediaType mediaType, Map<String, String> parameter, String body, Map<String, String> header) {
        logger.info("url :{} ,mediaType:{} , method :{} ", url, mediaType, httpMethod);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (header != null) {
            Set<String> keySet = header.keySet();
            for (String key : keySet) {
                headers.add(key, header.get(key));
            }
        }
        if (parameter == null) {
            parameter = new HashMap<>();
        }
        HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(JSONObject.parseObject(body), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, entity, String.class, parameter);
        return response.getBody();
    }


    public String exchange(String url, HttpMethod httpMethod, Map<String, String> parameter, String body, Map<String, String> header) {
        return exchange(url, httpMethod, MediaType.APPLICATION_JSON_UTF8, parameter, body, header);
    }

    public String exchangeText(String url, HttpMethod httpMethod, Map<String, String> parameter, String body, Map<String, String> header) {
        return exchange(url, httpMethod, MediaType.TEXT_PLAIN, parameter, body, header);
    }

    public String exchangeXml(String url, HttpMethod httpMethod, Map<String, String> parameter, String body, Map<String, String> header) {
        return exchange(url, httpMethod, MediaType.APPLICATION_XML, parameter, body, header);
    }


    public String getExchange(String url, Map<String, String> parameter, Map<String, String> header) {
        return this.exchange(url, HttpMethod.GET, parameter, null, header);
    }

    public String getExchange(String url, Map<String, String> parameter) {
        return this.exchange(url, HttpMethod.GET, parameter, null, null);
    }

    public String postExchange(String url, Map<String, String> parameter, String body, Map<String, String> header) {
        return this.exchange(url, HttpMethod.POST, parameter, body, header);
    }

    public String postExchange(String url, String body, Map<String, String> header) {
        return this.exchange(url, HttpMethod.POST, null, body, header);
    }

    public String postExchange(String url, String body) {
        return this.exchange(url, HttpMethod.POST, null, body, null);
    }
    public String postExchange(String url, MediaType mediaType , String body) {
        return this.exchange(url, HttpMethod.POST, mediaType,null, body, null);
    }

    public String putExchange(String url, Map<String, String> parameter, Map<String, String> header) {
        return this.exchange(url, HttpMethod.PUT, parameter, null, header);
    }

}
