package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucket;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;

public class Lec05EventListenerTest extends BaseTest {

    @Test
    public void expiredEventTest() {
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        bucket.set("sam", Duration.ofSeconds(10));
        String value = bucket.get();
        IO.println(value);

        // 注册过期监听器（异步回调）
        bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String key) {
                IO.println("Expired : " + key);
            }
        });

        // 主线程等待过期事件发生
        sleep(11000);
    }


    @Test
    public void deletedEventTest() {
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        bucket.set("sam");
        String value = bucket.get();
        IO.println(value);

        // 注册删除监听器（异步回调）
        bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String key) {
                IO.println("Deleted : " + key);
            }
        });

        // 主动删除（阻塞）
        bucket.delete();

        // 给监听器时间执行
        sleep(1000);
    }

}