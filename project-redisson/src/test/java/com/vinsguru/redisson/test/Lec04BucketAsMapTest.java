package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;

public class Lec04BucketAsMapTest extends BaseTest {
    // user:1:name
    // user:2:name
    // user:3:name
    @Test
    public void bucketsAsMap() {
        redissonClient.<String>getBucket("user:1:name", StringCodec.INSTANCE).set("sam1");
        redissonClient.<String>getBucket("user:2:name", StringCodec.INSTANCE).set("sam2");
        redissonClient.<String>getBucket("user:3:name", StringCodec.INSTANCE).set("sam3");
        redissonClient.getBuckets(StringCodec.INSTANCE).get("user:1:name", "user:2:name", "user:3:name");
    }

}