package com.vinsguru.redisson.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void keyValueAccessTest(){
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        bucket.set("sam");
        String value = bucket.get();
        IO.println("value = " + value);
        Assertions.assertEquals("sam", value);
    }

    @Test
    public void keyValueExpiryTest() {
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        bucket.set("sam", Duration.ofSeconds(10));
        String value = bucket.get();
        IO.println("value = " + value);
        Assertions.assertEquals("sam", value);
    }

    @Test
    @SneakyThrows
    public void keyValueExtendExpiryTest() {
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        bucket.set("sam", Duration.ofSeconds(10));
        String value = bucket.get();
        IO.println("value = " + value);
        Assertions.assertEquals("sam", value);

        //extend
        sleep(1000);
        boolean extended = bucket.expire(Duration.ofSeconds(60));
        Assertions.assertTrue(extended);

        // access expiration time
        long ttl = bucket.remainTimeToLive();
        IO.println("TTL = " + ttl);

        Assertions.assertTrue(ttl > 0);
        Assertions.assertTrue(ttl <= Duration.ofSeconds(60).toMillis());
    }

}