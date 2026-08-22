package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLongAdder;

public class 六610LongAdderTest extends BaseTest {

    @Test
    public void LongAdder01() {
        RLongAdder atomicLong = redissonSingleClient.getLongAdder("myLongAdder");
        atomicLong.add(12);
        atomicLong.increment();
        atomicLong.decrement();
        atomicLong.sum();
        atomicLong.reset();
        // 释放资源
        atomicLong.destroy();
    }
}
