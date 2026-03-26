package com.vinsguru.redisson.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDeque;
import org.redisson.client.codec.LongCodec;

import java.util.stream.LongStream;

@Slf4j
public class Lec10MessageQueueTest extends BaseTest {

    private RBlockingDeque<Long> msgQueue;

    @BeforeAll
    public void setupQueue() {
        this.msgQueue = this.client.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    @Test
    public void consumer1() {
        new Thread(() -> {
            while (true) {
                try {
                    Long value = msgQueue.take();
                    IO.println("Consumer 1 : " + value);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }).start();

        sleep(600_000);
    }

    @Test
    public void consumer2() {
        new Thread(() -> {
            while (true) {
                try {
                    Long value = msgQueue.take();
                    IO.println("Consumer 2 : " + value);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }).start();

        sleep(600_000);
    }

    @Test
    public void producer() {
        LongStream.rangeClosed(1, 100)
                .forEach(i -> {
                    msgQueue.add(i);
                    IO.println("Produced: " + i);
                    sleep(500);
                });

    }
}
