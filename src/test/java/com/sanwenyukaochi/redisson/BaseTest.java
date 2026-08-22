package com.sanwenyukaochi.redisson;

import com.sanwenyukaochi.redisson.config.RedissonConfig;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonClient redissonSingleClient;
    protected RedissonClient redissonClusterClient;

    @BeforeAll
    public void setClient() {
        this.redissonSingleClient = redissonConfig.redissonSingleClient();
        this.redissonClusterClient = redissonConfig.redissonClusterClient();
    }

    @BeforeEach
    public void flushDatabase() {
        redissonSingleClient.getKeys().flushdb();
        redissonClusterClient.getKeys().flushdb();
    }

    @AfterAll
    public void shutdown() {
        this.redissonSingleClient.shutdown();
        this.redissonClusterClient.shutdown();
    }
}
