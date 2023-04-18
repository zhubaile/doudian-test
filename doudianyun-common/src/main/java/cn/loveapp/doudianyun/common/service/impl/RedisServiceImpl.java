package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void redisHashSet(String str, String childStr, Object val) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(str, childStr, val);
    }

    @Override
    public Object redisHashGet(String str, String childStr) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Object value = hashOperations.get(str, childStr);
        return value;
    }
}
