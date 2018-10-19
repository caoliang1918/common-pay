package org.zhongweixian.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhongweixian.request.wxpay.WxPayRequestXml;
import org.zhongweixian.response.wxpay.WxPayResp;

/**
 * Created by caoliang on  6/6/2018
 */
public class XMLConverUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLConverUtil.class);


    public static <T> T xmlToObject(String xml, Class<T> tClass) {
        //将从API返回的XML数据映射到Java对象
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{WxPayResp.class, WxPayRequestXml.class});
        xStream.alias("xml", tClass);
        xStream.ignoreUnknownElements();
        return (T) xStream.fromXML(xml);
    }


    public static String objToXml(Object obj) {
        //解决XStream对出现双下划线的bug
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{WxPayResp.class, WxPayRequestXml.class});
        //将要提交给API的数据对象转换成XML格式数据Post给API
        return xStream.toXML(obj);
    }

}
