package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBuckets;
import org.redisson.client.codec.StringCodec;

import java.util.Map;

public class Lec04BucketAsMapTest extends BaseTest {
    // user:1:name
    // user:2:name
    // user:3:name
    @Test
    public void bucketsAsMap() {
        RBuckets buckets = client.getBuckets(StringCodec.INSTANCE);
        Map<String, String> result = buckets.get("user:1:name", "user:2:name", "user:3:name", "user:4:name");
        result.forEach((k, v) -> IO.println(k + " = " + v));
        Assertions.assertNotNull(result);
    }

}