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
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam");
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).get();
    }

    @Test
    public void keyValueExpiryTest() {
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam", Duration.ofSeconds(10));
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).get();
    }

    @Test
    @SneakyThrows
    public void keyValueExtendExpiryTest() {
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam", Duration.ofSeconds(10));
        client.<String>getBucket("user:1:name", StringCodec.INSTANCE).get();

        //extend
        sleep(1000);
        RBucket<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        IO.println("TTL = " + bucket.remainTimeToLive());
        bucket.expire(Duration.ofSeconds(10));
        IO.println("TTL = " + bucket.remainTimeToLive());

        Assertions.assertTrue(bucket.remainTimeToLive() > 0);
        Assertions.assertTrue(bucket.remainTimeToLive() <= Duration.ofSeconds(60).toMillis());
    }

}