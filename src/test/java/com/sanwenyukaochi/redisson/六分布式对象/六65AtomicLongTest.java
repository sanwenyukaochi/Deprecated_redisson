package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.atomic.LongIncrementArgs;

public class 六65AtomicLongTest extends BaseTest {

    @Test
    public void AtomicLongTest01() {
        RAtomicLong atomicLong = redissonSingleClient.getAtomicLong("myAtomicLong");
        atomicLong.set(3);
        atomicLong.incrementAndGet(LongIncrementArgs.by(5));
        atomicLong.get();
    }
}
