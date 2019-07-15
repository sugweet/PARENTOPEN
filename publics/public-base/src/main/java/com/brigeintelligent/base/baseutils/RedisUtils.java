package com.brigeintelligent.base.baseutils;

import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description：redis工具类
 * @Author：Sugweet
 * @Time：2019/7/15 14:01
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, Long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return 返回0代表永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key删除缓存（可以删多个）
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 根据key普通获取缓存中的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通存入数据到缓存
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通存入数据到缓存并设置时间
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, Object value, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time);
            } else {
                // 如果time<0就设置永久有效
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key
     * @param delta 要增加几
     * @return
     */
    public Long increment(String key, Long delta) {
        if (delta < 0) {
            throw new BaseException(BaseCode.FAILED, "递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key
     * @param delta 要减少几
     * @return
     */
    public Long decrement(String key, Long delta) {
        if (delta < 0) {
            throw new BaseException(BaseCode.FAILED, "递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //============================Map============================

    /**
     * hashGet（获取hash的某个键值）
     *
     * @param key
     * @param item
     * @return
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取key对应的所有键值
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hMGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 将数据存入hash
     *
     * @param key
     * @param map （对应多个键值）
     * @return
     */
    public Boolean hMSet(String key, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据存入hash（并设置时间）
     *
     * @param key
     * @param map  （对应多个键值）
     * @param time （过期时间）
     * @return
     */
    public Boolean hMSet(String key, Map<Object, Object> map, Long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中插入一条数据，如果不存在就创建
     *
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中插入一条数据并设置时间
     *
     * @param key
     * @param item
     * @param value
     * @param time  （如果原表设置了时间，将覆盖原时间）
     * @return
     */
    public boolean hSet(String key, String item, Object value, Long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  不为null
     * @param item 不为null
     */
    public void hDelete(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项值
     *
     * @param key
     * @param item
     * @return true 存在
     */
    public Boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在，就会创建一个 并把增后的值返回
     *
     * @param key
     * @param item
     * @param by
     * @return
     */
    public Double hincrement(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减 如果不存在，就会创建一个 并把减后的值返回
     *
     * @param key
     * @param item
     * @param by
     * @return
     */
    public Double hdecrement(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================Set============================

    /**
     * 根据key获取Set中的值
     *
     * @param key
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据存入缓存
     *
     * @param key
     * @param values
     * @return 返回成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将数据存入缓存并设置过期时间
     *
     * @param key
     * @param values
     * @return 返回成功个数
     */
    public Long sSet(String key, Long time, Object... values) {

        try {

            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 查询Set中是否存在value值
     *
     * @param key
     * @param value
     * @return true 存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取Set缓存的长度
     * @param key
     * @return
     */
    public Long sGetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 从Set中移除value（支持多个）
     * @param key
     * @param values
     * @return
     */
    public Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    //============================List============================

    /**
     * 获取list缓存中的内容
     * @param key
     * @param start
     * @param end 0 到 -1 代表所有值
     * @return
     */
    public List<Object> lGet(String key, Long start, Long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key
     * @return
     */
    public Long lListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, Long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key
     * @param value
     * @return
     */
    public Boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key
     * @param value
     * @return
     */
    public Boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置过期时间
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean lSet(String key, Object value, Long time) {

        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置过期时间
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean lSet(String key, List<Object> value, Long time) {

        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的数据
     * @param key
     * @param index
     * @param value
     * @return
     */
    public Boolean lUpdateIndex(String key, Long index, Object value) {
        try {
            redisTemplate.opsForList().set(key,index,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value的数据
     * @param key
     * @param count
     * @param value
     * @return 返回移除的个数
     */
    public Long lRemove(String key, Long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

}
