package cn.loveapp.doudianyun.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {

    /**
     * 如果要存取对象数据（复杂数据），用redisTemplate
     */
    @Autowired
    private static RedisTemplate redisTemplate;

    // 往redis存一个hash类型数据
//    static HashOperations = redisTemplate.opsForHash();

    /**
     * redisHash数据存储
     */
    public static void redisHashSet(String str, String childStr, Object val) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(str, childStr, val);
    }

    /**
     * redisHash数据获取
     * @return Object
     */
    public static Object redisHashGet(String str, String childStr) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Object value = hashOperations.get(str, childStr);
        return value;
    }
}
