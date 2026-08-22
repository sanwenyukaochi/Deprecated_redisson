package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RDoubleAdder;
import org.redisson.api.RLongAdder;

public class 六611DoubleAdderTest extends BaseTest {

    @Test
    public void DoubleAdder01() {
        RDoubleAdder atomicDouble = redissonClient.getDoubleAdder("myDoubleAdder");
        atomicDouble.add(12);
        atomicDouble.increment();
        atomicDouble.decrement();
        atomicDouble.sum();
    }
}
