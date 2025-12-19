package com.vinsguru.redisspring.fib.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedissonCacheConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisConnectionFactory redisConnectionFactory) {
        RedisStandaloneConfiguration redisConfig = ((LettuceConnectionFactory) redisConnectionFactory).getStandaloneConfiguration();
        Config redissonConfig = new Config();
        redissonConfig.setCodec(new JsonJacksonCodec());
        redissonConfig.setUsername(redisConfig.getUsername());
        redissonConfig.setPassword(new String(redisConfig.getPassword().get()));
        redissonConfig.useSingleServer()
                .setAddress("redis://%s:%d".formatted(redisConfig.getHostName(), redisConfig.getPort()))
                .setDatabase(redisConfig.getDatabase());
        return Redisson.create(redissonConfig);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient){
        return new RedissonSpringCacheManager(redissonClient);
    }

}