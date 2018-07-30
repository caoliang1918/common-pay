package org.zhongweixian.request;

import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayRequest extends BasePayRequest {
    private Map<String, String> ext;

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }
}
