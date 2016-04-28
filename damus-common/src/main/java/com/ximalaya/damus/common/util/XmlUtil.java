package com.ximalaya.damus.common.util;

import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

// ┴┬┴┬／￣＼＿／￣＼
// ┬┴┬┴▏　　▏▔▔▔▔＼ 这
// ┴┬┴／＼　／　　　　　　﹨ 地
// ┬┴∕　　　　　　　／　　　） 方
// ┴┬▏　　　　　　　　●　　▏ 不
// ┬┴▏　　　　　　　　　　　▔█◤ 错，
// ┴◢██◣　　　　　　 ＼＿＿／
// ┬█████◣　　　　　　　／　 让我用PP踩踩！ 　
// ┴█████████████◣
// ◢██████████████▆▄
// █◤◢██◣◥█████████◤＼
// ◥◢████　████████◤　　 ＼
// ┴█████　██████◤　　　　　 ﹨
// ┬│　　　│█████◤　　　　　　　　▏
// ┴│　　　│ 祝ads日进斗金，雄霸广告界 ▏
// ┬∕　　　∕　　　　／▔▔▔＼　　　　 ∕
// *∕＿＿_／﹨　　　∕　　　　　 ＼　　／＼
// ┴┬┴┬┴┬┴ ＼＿＿＿＼　　　　 ﹨／▔＼﹨／▔＼ ╃第一个发现的有红包哦╃
// ▲△▲▲╓╥╥╥╥╥╥╥╥＼　　 ∕　 ／▔﹨　／▔
//  ＊＊＊╠╬╬╬╬╬╬╬╬＊﹨　　／　　／／ ╃葛潇╃2016.2.4,,

public class XmlUtil {

    static Logger logger = Logger.getLogger(XmlUtil.class);

    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xmlStr, Class<T> clz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlStr));
    }

    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(URL url, Class<T> clz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(url);
    }
}
