package com.sanwenyukaochi.redisson.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJackson3Codec;
import org.redisson.config.Config;

public class RedissonConfig {

    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setPassword(null)
                .useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        config.setCodec(new JsonJackson3Codec());
        return Redisson.create(config);
    }

}