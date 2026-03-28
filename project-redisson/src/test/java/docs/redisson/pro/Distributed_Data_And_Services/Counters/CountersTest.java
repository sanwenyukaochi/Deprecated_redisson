package docs.redisson.pro.Distributed_Data_And_Services.Counters;

import docs.redisson.pro.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;

/**
 * <p>参考文档：<a href="https://redisson.pro/docs/data-and-services/counters/"></a></p>
 */
public class CountersTest extends BaseTest {
    @Test
    public void IdGeneratorTest() {
        RIdGenerator idGenerator = redissonClient.getIdGenerator("generator");
        idGenerator.tryInit(12, 20000); // 初始化时，起始值为 12，分配大小为 20000
        long id = idGenerator.nextId();
    }

    @Test
    public void AtomicLongTest() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("myAtomicLong");
        atomicLong.set(3); // 设置初始值为 3
        atomicLong.incrementAndGet(); // 先自增 1，再返回自增后的值（此时值为 4）
        atomicLong.get(); // 获取当前值（值为 4）
    }

    @Test
    public void AtomicDoubleTest() {
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(2.81); // 设置初始值为 2.81
        atomicDouble.addAndGet(4.11); // 增加 4.11，并返回增加后的值（此时值为 6.92）
        atomicDouble.get(); // 获取当前值（值为 6.92）
    }

    @Test
    public void LongAdderTest() {
        RLongAdder longAdder = redissonClient.getLongAdder("myLongAdder");
        longAdder.add(12); // 当前值: 12
        longAdder.increment(); // 当前值: 13
        longAdder.decrement(); // 当前值: 12
        longAdder.sum(); // 返回: 12
    }

    @Test
    public void RLongDoubleTest() {
        RDoubleAdder doubleAdder = redissonClient.getDoubleAdder("myDoubleAdder");
        doubleAdder.add(12.0); // 当前值: 12.0
        doubleAdder.increment(); // 当前值: 13.0
        doubleAdder.decrement(); // 当前值: 12.0
        doubleAdder.sum(); // 返回: 12.0
    }
}
