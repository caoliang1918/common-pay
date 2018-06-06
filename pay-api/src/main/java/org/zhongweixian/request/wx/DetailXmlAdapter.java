package org.zhongweixian.request.wx;

import com.alibaba.fastjson.JSON;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by caoliang on  6/6/2018
 */
public class DetailXmlAdapter extends XmlAdapter<String, Detail> {
    @Override
    public Detail unmarshal(String v) throws Exception {
        return JSON.parseObject(v, Detail.class);
    }

    @Override
    public String marshal(Detail v) throws Exception {
        return "<![CDATA[" + JSON.toJSONString(v) + "]]>";
    }
}
