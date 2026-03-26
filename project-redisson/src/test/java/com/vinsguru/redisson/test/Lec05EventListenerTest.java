package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;

public class Lec05EventListenerTest extends BaseTest {
    // 全开 CONFIG SET notify-keyspace-events gxE
    @Test
    public void expiredEventTest() {
        // 必须开启 过期事件通知 CONFIG SET notify-keyspace-events xE
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam");
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).get();
        // 注册过期监听器（异步回调）
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String key) {
                IO.println("Expired : " + key);
            }
        });
        // 主动设置过期（设置过期）
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).expire(Duration.ofSeconds(3));
        // 主线程等待过期事件发生
        sleep(5000);
    }

    @Test
    public void deletedEventTest() {
        // 必须开启 过期事件通知 CONFIG SET notify-keyspace-events gE
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam");
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).get();
        // 注册删除监听器（异步回调）
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String key) {
                IO.println("Deleted : " + key);
            }
        });
        // 主动删除（阻塞）
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).delete();
        // 给监听器时间执行
        sleep(5000);
    }

}