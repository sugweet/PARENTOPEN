package com.brigeintelligent.base.baseutils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description：获取配置文件数据
 * @Author：Sugweet
 * @Time：2019/5/23 15:35
 */
@Log4j2
public class PropertyUtil {
    private static final Properties PROPERTIES = new Properties();

    public PropertyUtil(String proptName) {
        loadProperties(proptName);
    }

    /**
     * 将指定名称的Properties文件加载到PROPERTIES对象中
     *
     * @param propName
     */
    private static Properties loadProperties(String propName) {
        InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(propName);
        try {
            if (is != null && is.available() > 0) {
                PROPERTIES.load(is);
            }
        } catch (IOException e) {
            log.error("读取配置文件中的数据失败");
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return PROPERTIES;
    }

    /**
     * 根据key获取配置文件中的value
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * 根据配置文件名和key获取配置文件中的数据
     * @param propFileName
     * @param key
     * @return
     */
    public static String getValue(String propFileName, String key) {
        Properties prop = loadProperties(propFileName);
        return prop.getProperty(key);
    }
}
