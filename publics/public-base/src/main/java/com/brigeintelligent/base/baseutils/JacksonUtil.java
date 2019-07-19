package com.brigeintelligent.base.baseutils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description：前端json数据与后端集合格式互转工具类
 * @Author：Sugweet
 * @Time：2019/5/24 15:01
 */
public class JacksonUtil {
    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * json转成class对象（List<T>）
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * object转json
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取objectMapper
     * @return
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * List<T>转json
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String listToJson(Class<T> list) {
        return JSON.toJSONString(list);
    }

    /**
     * json转List<T>
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return JSONArray.parseArray(jsonString, clazz);
    }

    /**
     * json转Set
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<T> jsonToSet(String jsonString, Class<T> clazz) {
        List<T> list = JSONArray.parseArray(jsonString, clazz);
        return new HashSet<>(list);

    }

    /**
     * set转json
     * @param set
     * @param <T>
     * @return
     */
    public static <T> String setToJson(Set<T> set) {
        return JSON.toJSONString(set);
    }
}
