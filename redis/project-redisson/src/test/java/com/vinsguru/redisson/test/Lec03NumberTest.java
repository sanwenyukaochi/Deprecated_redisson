package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;

public class Lec03NumberTest extends BaseTest {

    @Test
    public void keyValueIncreaseTest(){
        // set k v -- incr , decr
        RAtomicLong atomicLong = this.client.getAtomicLong("user:1:visit");
        for (int i = 0; i < 30; i++) {
            long value = atomicLong.incrementAndGet();
            IO.println("current value = " + value);
            sleep(1000);
        }
        long finalValue = atomicLong.get();
        Assertions.assertEquals(30, finalValue);
    }

}