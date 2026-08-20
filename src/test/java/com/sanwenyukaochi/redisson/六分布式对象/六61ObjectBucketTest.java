package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import com.sanwenyukaochi.redisson.common.AnyObject;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.codec.TypedJsonJackson3Codec;

public class 六61ObjectBucketTest extends BaseTest {

    @Test
    public void ObjectNameTest01() {
        redissonClient.getKeys().flushdb();
        // Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象。
        // 除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
        RBucket<AnyObject> bucket =
                redissonClient.getBucket(
                        "anyObject", new TypedJsonJackson3Codec(AnyObject.class));
        bucket.set(new AnyObject(1));

        bucket.setIfAbsent(new AnyObject(3));
        bucket.compareAndSet(new AnyObject(4), new AnyObject(5));
        bucket.getAndSet(new AnyObject(6));
    }

    @Test
    public void ObjectNameTest() {
        redissonClient.getKeys().flushdb();
        RBuckets buckets = redissonClient.getBuckets(new TypedJsonJackson3Codec(AnyObject.class));
        Map<String, Object> map = new HashMap<>();
        map.put("myBucket1", new AnyObject(1));
        map.put("myBucket2", new AnyObject(2));
        map.put("myBucket3", new AnyObject(3));
        // 利用Redis的事务特性，同时保存所有的通用对象桶，如果任意一个通用对象桶已经存在则放弃保存其他所有数据。
        buckets.trySet(map);
        // 同时保存全部通用对象桶。
        buckets.set(map);
        // 还可以通过RBuckets接口实现批量操作多个RBucket对象：
        Map<String, AnyObject> loadedBuckets = buckets.get("myBucket1", "myBucket2", "myBucket3");
        loadedBuckets.forEach(
                (key, value) -> System.out.println("key: " + key + " value: " + value));
    }
}
