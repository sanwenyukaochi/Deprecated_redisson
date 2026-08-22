package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;

import java.time.Duration;

public class 六612RateLimiterTest extends BaseTest {

    @Test
    public void RateLimiter01() {
        RRateLimiter rateLimiter = redissonSingleClient.getRateLimiter("myRateLimiter");
        // 初始化：每1秒产生10个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 10, Duration.ofSeconds(1));
        // 阻塞获取
        rateLimiter.acquire(3);
        // 当前可以令牌数
        rateLimiter.availablePermits();
        // 非阻塞获取[尝试获取]
        boolean isAcquire1 = rateLimiter.tryAcquire(8, Duration.ofSeconds(2));
        IO.println("acquire: " + isAcquire1);
        // 带超时的非阻塞获取[尝试获取]
        boolean isAcquire2 = rateLimiter.tryAcquire(10);
        IO.println("acquire: " + isAcquire2);
    }
}
