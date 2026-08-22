package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLog;

public class 六69HyperLogLogTest extends BaseTest {

    @Test
    public void HyperLogLogTest01() {
        RHyperLogLog<Integer> log = redissonClient.getHyperLogLog("log");
        log.add(1);
        log.add(2);
        log.add(3);
        long count = log.count();
        IO.println("Count is " + count);
    }
}
