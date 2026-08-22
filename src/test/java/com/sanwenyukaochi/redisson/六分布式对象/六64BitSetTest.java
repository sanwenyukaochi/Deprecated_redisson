package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBitSet;

public class 六64BitSetTest extends BaseTest {

    @Test
    public void BitSetTest01() {
        RBitSet set = redissonSingleClient.getBitSet("simpleBitset");
        set.set(0, true);
        set.set(1812, false);
        set.clear(0);
        // AND操作
        set.and("simpleBitset");
        // OR操作
        set.or("simpleBitset");
        // XOR操作
        set.xor("simpleBitset");
        // AND操作OR操作一步执行
        set.andOr("simpleBitset");
        // 返回当前位图中值为1的位的数量
        set.cardinality();
        // 有效二进制长度
        set.length();
    }
}
