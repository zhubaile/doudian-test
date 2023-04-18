package cn.loveapp.doudianyun.common.service;

public interface RedisService {
    /**
     * redisHash数据存储
     */
    void redisHashSet(String str, String childStr, Object val);

    /**
     * redisHash数据获取
     * @return Object
     */
    Object redisHashGet(String str, String childStr);
}
