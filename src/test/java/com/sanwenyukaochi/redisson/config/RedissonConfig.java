package com.sanwenyukaochi.redisson.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedissonConfig {

    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setPassword("123456")
                .useClusterServers()
                .addNodeAddress("redis://172.30.0.10:6379")
                .addNodeAddress("redis://172.30.0.20:6379")
                .addNodeAddress("redis://172.30.0.30:6379")
                .setScanInterval(2000);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }

}