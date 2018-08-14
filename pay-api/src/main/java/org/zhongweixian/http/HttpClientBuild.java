package org.zhongweixian.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class HttpClientBuild {
    private Logger logger = LoggerFactory.getLogger(HttpClientBuild.class);

    private BasicHttpClientConnectionManager manager = null;

    private boolean userCert;

    private String key;

    private byte[] cert;

    private HttpClient httpClient;

    public HttpClientBuild(String key , byte[] cert) {
        this.key = key;
        this.cert = cert;
        bulid(true);
    }
    public HttpClientBuild() {
        bulid(false);
    }



    public void bulid(boolean userCert) {
        if (userCert) {
            char[] password = key.toCharArray();

        }else {
            httpClient = HttpClientBuilder.create().build();
        }

    }



    public String postExchange(String url, Map<String, String> header, String data , boolean userCert) {

    }


    private String request(){

    }


}
