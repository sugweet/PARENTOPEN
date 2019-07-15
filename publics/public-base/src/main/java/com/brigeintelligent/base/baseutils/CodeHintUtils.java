package com.brigeintelligent.base.baseutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * @Description：配置文件读取工具类
 * @Author：Sugweet
 * @Time：2019/6/20 15:16
 */
@Slf4j
public class CodeHintUtils {
    private static final String LOCATIONS = "codehint.properties";

    //private static CodeHintUtils INSTANCE = new CodeHintUtils();
    private CodeHintUtils() {
    }

    private static class holder {
        private static final CodeHintUtils INSTANCE = new CodeHintUtils();
    }

    // 单例模式
    public static CodeHintUtils getInstance() {
        return holder.INSTANCE;
    }

    public String getValues(String key) {
        Properties properties = new Properties();
        InputStreamReader is = null;
        InputStream ras = null;

        try {
            String path = Objects.requireNonNull(CodeHintUtils.class.getClassLoader().getResource(LOCATIONS)).getPath();
            path = URLDecoder.decode(path, "UTF-8");
            log.info("==========propertiesPath：" + path);
            String[] split = path.split("/");
            String savePath = "";
            // 服务器中将文件路径改为conf文件夹下
            for (int i = 1; i < split.length; i++) {
                if (!split[i].equals("core")) {
                    savePath = "/" + split[i];
                } else {
                    savePath = "/conf/" + LOCATIONS;
                    break;
                }
            }
            log.info("===========savePath：" + savePath);
            is = new InputStreamReader(new FileInputStream(savePath), StandardCharsets.UTF_8);

            properties.load(is);

            String value = properties.getProperty(key);

            String defaultKey = "";
            if (StringUtils.isEmpty(value)) {
                log.info("========key：" + key + " 没有对应的提示信息，将使用通用的提示信息");
                String[] keys = key.split("\\.");
                defaultKey = keys[0]+".800."+keys[keys.length-1];
                log.info("=========defaultKey：" + defaultKey);
                String defValue = properties.getProperty(defaultKey);
                if (StringUtils.isEmpty(defValue)) {
                    return properties.getProperty("default.800.0000");
                }
                return defValue;
            }
            return value;
        } catch (Exception e) {
            log.error("=======获取指定属性文件的属性值异常：", e);
            return null;
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //properties.clear();
        }
    }

}

