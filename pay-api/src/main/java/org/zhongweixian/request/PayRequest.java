package org.zhongweixian.request;

import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 */
public class PayRequest extends BasePayRequest {
    private Map<String, Object> ext;

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
