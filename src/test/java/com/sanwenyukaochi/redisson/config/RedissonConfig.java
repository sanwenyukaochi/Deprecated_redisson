package com.sanwenyukaochi.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJackson3Codec;
import org.redisson.config.Config;

public class RedissonConfig {

    public RedissonClient redissonClusterClient() {
        Config config = new Config();
        config.setPassword("123456")
                .useClusterServers()
                .addNodeAddress(
                        "redis://172.30.0.10:6379",
                        "redis://172.30.0.20:6379",
                        "redis://172.30.0.30:6379")
                .setDatabase(0)
                .setScanInterval(2000);
        config.setCodec(new JsonJackson3Codec());
        return Redisson.create(config);
    }

    public RedissonClient redissonSingleClient() {
        Config config = new Config();
        config.setPassword("123456")
                .useSingleServer()
                .setAddress("redis://172.30.0.40:6379")
                .setDatabase(0);
        config.setCodec(new JsonJackson3Codec());
        return Redisson.create(config);
    }
}
