package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.atomic.LongIncrementArgs;

public class 六66AtomicDoubleTest extends BaseTest {

    @Test
    public void AtomicDoubleTest01() {
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(2.81);
        atomicDouble.addAndGet(4.11);
        atomicDouble.get();
    }
}
