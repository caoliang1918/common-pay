package org.zhongweixian.request;

/**
 * Created by caoliang on 2018/8/25
 */
public class VerifyRequest extends BaseRequest {
    private String body;
    private String signature;
    private String key;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
