package com.vinsguru.redisspring.config;

import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class RedissonConfig {

    private RedissonClient redissonClient;

    @SneakyThrows
    public RedissonClient getClient() {
        Properties props = new Properties();
        InputStream in = RedissonConfig.class.getClassLoader().getResourceAsStream("redisson.properties");
        props.load(in);
        if (Objects.isNull(this.redissonClient)) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress(props.getProperty("redis.address"))
                    .setDatabase(Integer.parseInt(props.getProperty("redis.database")))
                    .setUsername(props.getProperty("redis.username"))
                    .setPassword(props.getProperty("redis.password"));
            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    public RedissonReactiveClient getReactiveClient(){
        return getClient().reactive();
    }

}
