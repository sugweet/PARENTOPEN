package com.brigeintelligent.base.baseutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * @Description：配置文件读取工具类
 * @Author：Sugweet
 * @Time：2019/6/20 15:16
 */
@Slf4j
public class CodeHintUtils {
    private static final String FILENAME = "codehint.properties";

    private static Properties properties;

    // 最近文件修改时间（毫秒）
    private static Long lastModified = 0L;

    private CodeHintUtils() {
    }

    private static class holder {
        private static final CodeHintUtils INSTANCE = new CodeHintUtils();
    }

    // 单例模式
    public static CodeHintUtils getInstance() {
        return holder.INSTANCE;
    }


    /*static {
        for (String location : LOCATIONS) {
            loadFile(location);
        }
    }*/

    /**
     * 读取配置文件
     *
     */
    private static synchronized void loadFile() {
        properties = new Properties();
        InputStreamReader is = null;
        try {

            is = new InputStreamReader(new FileInputStream(getFilaPath()), StandardCharsets.UTF_8);

            properties.load(is);
        } catch (Exception e) {
            log.error("=======获取指定属性文件的属性值异常：", e);
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取配置文件信息
     * @param key
     * @return
     */
    public String getValues(String key) {

        // 判断是否刷新缓存
        if (properties == null || isUpdate()) {
            loadFile();
        }

        String value = properties.getProperty(key);
        String defaultKey = "";
        if (StringUtils.isEmpty(value)) {
            log.info("========key：" + key + " 没有对应的提示信息，将使用通用的提示信息");
            String[] keys = key.split("\\.");
            defaultKey = keys[0] + ".800." + keys[keys.length - 1];
            log.info("=========defaultKey：" + defaultKey);
            String defValue = properties.getProperty(defaultKey);
            if (StringUtils.isEmpty(defValue)) {
                return properties.getProperty("default.800.0000");
            }
            return defValue;
        }
        return value;
    }

    /**
     * 刷新配置文件缓存
     * @return
     */
    public boolean onLoad() {
        loadFile();
        return true;
    }

    /**
     * 配置文件放在conf文件夹下，所以要将文件路径进行修改
     * @return
     */
    private static String getFilaPath() {
        String savePath = "";

        try {
            String path = Objects.requireNonNull(CodeHintUtils.class.getClassLoader().getResource(FILENAME)).getPath();
            path = URLDecoder.decode(path, "UTF-8");
            log.info("==========propertiesPath：" + path);
            String[] split = path.split("/");
            log.info("============split:" + Arrays.toString(split));

            // 服务器中将文件路径改为conf文件夹下
            for (int i = 1; i < split.length; i++) {
                if (!split[i].equals("core")) {
                    savePath += "/" + split[i];
                } else {
                    savePath += "/conf/" + FILENAME;
                    break;
                }
            }
            log.info("===========savePath：" + savePath);
        } catch (UnsupportedEncodingException e) {
            log.error("===============文件路径解析失败");
        }
        return savePath;
    }

    /**
     * 判断文件是否修改
     * @return
     */
    private static Boolean isUpdate() {
        boolean flag = false;
        File file = new File(getFilaPath());
        // 判断文件最新修改时间
        if (file.lastModified() > lastModified) {
            lastModified = file.lastModified();
            flag = true;
            log.info("=============配置文件信息已修改");
        }
        return flag;

    }

}

