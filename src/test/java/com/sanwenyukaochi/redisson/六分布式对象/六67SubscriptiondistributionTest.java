package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RTopic;

public class 六67SubscriptiondistributionTest extends BaseTest {

    @Test
    public void SubscriptiondistributionTest01() throws InterruptedException {
        Thread listenerThread = new Thread(() -> {
            RTopic topic = redissonSingleClient.getTopic("anyTopic");
            topic.addListener(String.class, (channel, msg) -> {
                IO.println("Listener received channel: "  + channel + ", msg: " + msg);
            });
        });
        listenerThread.start();
        Thread.sleep(1000);
        RTopic topic = redissonSingleClient.getTopic("anyTopic");
        topic.publish("Hello, this is a test message!");
        listenerThread.interrupt();
    }
}
