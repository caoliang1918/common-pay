package org.zhongweixian.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caoliang on  6/6/2018
 * <p>
 * 恶心的微信
 */
public class XMLConverUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLConverUtil.class);
    private static Map<Class<?>, Marshaller> M_MAP;

    private static Map<Class<?>, Unmarshaller> U_MAP;

    private final static String CHAT_SET = "UTF-8";

    static {
        M_MAP = new HashMap<Class<?>, Marshaller>();
        U_MAP = new HashMap<Class<?>, Unmarshaller>();
    }

    /**
     * XML to Object
     *
     * @param <T>   T
     * @param clazz clazz
     * @param xml   xml
     * @return T
     */
    public static <T> T convertToObject(Class<T> clazz, String xml) {
        return convertToObject(clazz, new StringReader(xml));
    }

    /**
     * XML to Object
     *
     * @param <T>         T
     * @param clazz       clazz
     * @param inputStream inputStream
     * @return T
     */
    public static <T> T convertToObject(Class<T> clazz, InputStream inputStream) {
        return convertToObject(clazz, new InputStreamReader(inputStream));
    }

    /**
     * XML to Object
     *
     * @param <T>         T
     * @param clazz       clazz
     * @param inputStream inputStream
     * @param charset     charset
     * @return T
     */
    public static <T> T convertToObject(Class<T> clazz, InputStream inputStream, Charset charset) {
        return convertToObject(clazz, new InputStreamReader(inputStream, charset));
    }

    /**
     * XML to Object
     *
     * @param <T>    T
     * @param clazz  clazz
     * @param reader reader
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToObject(Class<T> clazz, Reader reader) {
        try {
            if (!U_MAP.containsKey(clazz)) {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                U_MAP.put(clazz, unmarshaller);
            }
            return (T) U_MAP.get(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * Object to XML
     *
     * @param object object
     * @return xml
     */
    public static String convertToXML(Object object) {
        try {
            if (!M_MAP.containsKey(object.getClass())) {
                JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_ENCODING, CHAT_SET);
                //是否省略xml头信息，默认false
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                //格式化生成的xml串
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                //设置CDATA输出字符
                /*marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
                    @Override
                    public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
                        writer.write(ac, i, j);
                    }
                });*/
                M_MAP.put(object.getClass(), marshaller);
            }
            StringWriter stringWriter = new StringWriter();
            M_MAP.get(object.getClass()).marshal(object, stringWriter);
            return stringWriter.getBuffer().toString();
        } catch (JAXBException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 转换简单的xml to map
     *
     * @param xml xml
     * @return map
     */
    public static Map<String, String> convertToMap(String xml) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            if (root != null) {
                NodeList childNodes = root.getChildNodes();
                if (childNodes != null && childNodes.getLength() > 0) {
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node node = childNodes.item(i);
                        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                            map.put(node.getNodeName(), node.getTextContent());
                        }
                    }
                }
            }
        } catch (DOMException e) {
            logger.error("", e);
        } catch (ParserConfigurationException e) {
            logger.error("", e);
        } catch (SAXException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        return map;
    }
}
