package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLog;
import org.redisson.client.codec.LongCodec;

import java.util.List;
import java.util.stream.LongStream;

public class Lec11HyperLogLogTest extends BaseTest {

    @Test // 12.5 kb
    public void count() {

        RHyperLogLog<Long> counter =
                this.client.getHyperLogLog("user:visits", LongCodec.INSTANCE);

        List<Long> list1 = LongStream.rangeClosed(1, 25000).boxed().toList();
        List<Long> list2 = LongStream.rangeClosed(25001, 50000).boxed().toList();
        List<Long> list3 = LongStream.rangeClosed(1, 75000).boxed().toList();
        List<Long> list4 = LongStream.rangeClosed(50000, 100_000).boxed().toList();

        counter.addAll(list1);
        counter.addAll(list2);
        counter.addAll(list3);
        counter.addAll(list4);

        long count = counter.count();

        IO.println(count);
    }

}