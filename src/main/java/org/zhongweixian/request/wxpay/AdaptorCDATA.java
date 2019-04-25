package org.zhongweixian.request.wxpay;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by caoliang on  6/6/2018
 */
public class AdaptorCDATA extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return "<![CDATA[" + v + "]]>";
    }
}
