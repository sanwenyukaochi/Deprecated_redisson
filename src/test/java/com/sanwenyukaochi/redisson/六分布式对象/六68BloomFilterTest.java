package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;

public class 六68BloomFilterTest extends BaseTest {

    @Test
    public void ObjectNameTest01() {
        record AnyObject(int x) {}
        RBloomFilter<AnyObject> bloomFilter = redissonClient.getBloomFilter("sample");
        // 初始化布隆过滤器，预计统计元素数量为55000000，期望误差率为0.03
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add(new AnyObject(1));
        bloomFilter.add(new AnyObject(2));
        boolean contains = bloomFilter.contains(new AnyObject(2));
        IO.println("ObjectNameTest01 contains " + contains);
    }
}
