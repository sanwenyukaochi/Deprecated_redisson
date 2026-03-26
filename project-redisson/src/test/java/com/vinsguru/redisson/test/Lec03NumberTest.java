package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;

public class Lec03NumberTest extends BaseTest {

    @Test
    public void keyValueIncreaseTest(){
        // set k v -- incr , decr
        redissonClient.getAtomicLong("user:1:visit").incrementAndGet();
        redissonClient.getAtomicLong("user:1:visit").get();
    }

}