package com.vinsguru.redisson.test.config;

import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Objects;

public class RedissonConfig {

    @SneakyThrows
    public RedissonClient getClient() {
            Config config = new Config();
            config.setUsername("default")
                    .setPassword("");
            config.useSingleServer()
                    .setAddress("redis://10.0.0.1:6379")
                    .setDatabase(0);return Redisson.create(config);
    }

}
