package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.client.codec.LongCodec;

import java.util.List;
import java.util.stream.LongStream;

public class Lec09ListQueueStackTest extends BaseTest {

    @Test
    public void listTest(){
        // LRANGE number-input 0 -1
        RList<Long> list = client.getList("number-input", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1, 10)
                .boxed()
                .toList();

        list.addAll(longList);

        IO.println("List size: " + list.size());
    }

    @Test
    public void queueTest(){ // Queue
        RQueue<Long> queue = client.getQueue("number-input", LongCodec.INSTANCE);

        for (int i = 0; i < 3; i++) {
            Long value = queue.poll();
            IO.println(value);
        }

        IO.println("Queue size: " + queue.size());
    }

    @Test
    public void stackTest(){ // Stack
        RDeque<Long> deque = client.getDeque("number-input", LongCodec.INSTANCE);

        for (int i = 0; i < 3; i++) {
            Long value = deque.pollLast();
            IO.println(value);
        }

        IO.println("Deque size: " + deque.size());
    }

}