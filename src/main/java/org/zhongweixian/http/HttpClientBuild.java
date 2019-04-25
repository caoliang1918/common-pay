package org.zhongweixian.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class HttpClientBuild {
    private Logger logger = LoggerFactory.getLogger(HttpClientBuild.class);

    private static final String CHARSET = "UTF-8";

    private BasicHttpClientConnectionManager manager = null;

    private char[] password;

    private String certPath;

    private HttpClient httpClient;

    public HttpClientBuild(String key, String certPath) {
        char[] password = key.toCharArray();
        this.certPath = certPath;
        this.password = password;
        bulid(true);
    }

    public HttpClientBuild() {
        bulid(false);
    }


    public void bulid(boolean userCert) {
        if (userCert) {

            KeyStore keyStore = null;
            try {
                File file = new File(certPath);
                if (!file.exists()){
                    throw new FileNotFoundException("证书文件未找到");
                }
                InputStream inputStream = new FileInputStream(file);
                keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(inputStream, password);

                //初始化密钥库
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, password);

                //创建SSLContext  这里去掉不是特别安全的SSL，采用TLS传输协议
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagerFactory.getKeyManagers(),
                        null, new SecureRandom());

                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{""}, null, new DefaultHostnameVerifier());
                manager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build(), null, null, null);
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } else {
            manager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }
        httpClient = HttpClientBuilder.create().setConnectionManager(manager).build();
    }


    /**
     * @param url
     * @param header
     * @param data
     * @return
     */
    public String postExchange(String url, Map<String, String> header, String data) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(data, CHARSET);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity, CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String postExchange(String url, String contentType, String data) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", contentType);
        return this.postExchange(url, header, data);
    }
}
