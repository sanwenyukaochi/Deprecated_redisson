package docs.redisson.pro.Distributed_Data_And_Services.Objects;

import docs.redisson.pro.BaseTest;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.api.bloomfilter.BloomFilterInitArgs;
import org.redisson.api.cuckoofilter.CuckooFilterAddArgs;
import org.redisson.api.cuckoofilter.CuckooFilterInitArgs;
import org.redisson.api.geo.GeoEntry;
import org.redisson.api.geo.GeoPosition;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.GeoUnit;
import org.redisson.api.listener.SetObjectListener;
import org.redisson.api.listener.TrackingListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.Jackson3Codec;
import org.redisson.codec.TypedJsonJackson3Codec;
import tools.jackson.core.type.TypeReference;

/**
 * <p>参考文档：<a href="https://redisson.pro/docs/data-and-services/objects/"></a></p>
 */
public class ObjectTest extends BaseTest {
    @Test
    public void ObjectHolderTest() {
        record AnyObject(int num) {}
        RBucket<AnyObject> bucket = redissonClient.getBucket("anyObject", new TypedJsonJackson3Codec(AnyObject.class));
        bucket.set(new AnyObject(1)); // 设置对象(覆盖已有值)
        AnyObject obj = bucket.get(); // 获取对象
        bucket.setIfAbsent(new AnyObject(3)); // 设置对象if不存在
        bucket.compareAndSet(
                new AnyObject(4),
                new AnyObject(
                        5)); // 原子性的比较并设置，只有当当前值等于期望值（new AnyObject(4)）时，才设置为新值（new AnyObject(5)），避免你执行完 GET 后，到执行 SET
        // 前，中间可能已经有别的客户端把值改掉了
        bucket.getAndSet(new AnyObject(6)); // 获取旧值并设置新值
    }

    @Test
    public void ObjectHolderMultipleBucketTest() {
        record MyObject(int num) {}
        RBuckets buckets = redissonClient.getBuckets(new TypedJsonJackson3Codec(MyObject.class));
        Map<String, MyObject> loadedBuckets = buckets.get("myBucket1", "myBucket2", "myBucket3"); // 获取所有 bucket 值
        Map<String, Object> map = new HashMap<>();
        map.put("myBucket1", new MyObject(1));
        map.put("myBucket2", new MyObject(2));
        buckets.trySet(map); // 会进行一个判断如果key已经存在，不会覆盖已有的值
        buckets.set(map); // 覆盖写入
        // PS1: bucket.trySet(value) 如果 key 不存在就写入，如果 key 已存在就不覆盖。
        // PS2: bucket.trySet(map) 如果 map 里的所有 key 都不存在，才全部写入，只要其中一个 key 已存在，整批都不写
    }

    @Test
    public void ListenersTest() {
        // Redisson 允许为每个 RBucket 对象绑定监听器。这需要在 Redis 端启用 notify-keyspace-events 设置。
        RBucket<String> bucket = redissonClient.getBucket("anyObject");
        int trackingListenerId = bucket.addListener(new TrackingListener() {
            @Override
            public void onChange(String name) {
                // 读取操作后创建/更新的数据
            }
        });
        int setListenerId = bucket.addListener(new SetObjectListener() {
            @Override
            public void onSet(String name) {
                // 数据创建/更新
            }
        });
        int expiredObjectListenerId = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String name) {
                // RBucket 对象已过期
            }
        });
        int deletedListenerId = bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String name) {
                // RBucket 对象已删除
            }
        });
        // ...
        bucket.removeListener(trackingListenerId);
        bucket.removeListener(setListenerId);
        bucket.removeListener(expiredObjectListenerId);
        bucket.removeListener(deletedListenerId);
    }

    @Test
    public void BinaryStreamHolderTest() {
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        byte[] oldContent = stream.get();
        byte[] content = "Hello, Redisson Binary Stream!".getBytes(StandardCharsets.UTF_8);
        stream.set(content); // 设置对象
        stream.getAndSet(content); // 获取旧值并设置新值
        stream.setIfAbsent(content); // 设置对象if不存在
        stream.compareAndSet(oldContent, content); // 原子性的比较并设置，避免你执行完 GET 后，到执行 SET 前，中间可能已经有别的客户端把值改掉了
    }

    @Test
    @SneakyThrows
    public void BinaryStreamHolderInputStreamAndOutputStreamTest() {
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        try (OutputStream os = stream.getOutputStream()) {
            os.write("Hello".getBytes(StandardCharsets.UTF_8));
            os.write("World".getBytes(StandardCharsets.UTF_8));
        }
        try (InputStream is = stream.getInputStream()) {
            byte[] buffer = new byte[5];
            int readHello = is.read(buffer);
            IO.println(new String(buffer, 0, readHello)); // 输出: Hello
            int readWorld = is.read(buffer);
            IO.println(new String(buffer, 0, readWorld)); // 输出: World
        }
        // PS: 因为流是有状态的，会记住当前读取位置！
    }

    @Test
    @SneakyThrows
    public void BinaryStreamHolderSeekableByteChannelTest() {
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        SeekableByteChannel channel = stream.getChannel();
        ByteBuffer contentToWrite = ByteBuffer.wrap("0123456789".getBytes(StandardCharsets.UTF_8));
        channel.write(contentToWrite); // 1. 写入 "0123456789"，写入10个字节，当前位置移动到10
        channel.truncate(5); // 2. 截断到5个字节，删除后5个字节，现在内容是 "01234"，当前位置变成5
        ByteBuffer readBuffer = ByteBuffer.allocate(3);
        channel.read(readBuffer); // 3. 读取3个字节，从位置5开始读，但位置5已经是文件末尾，readBuffer 读不到数据，因为当前位置等于文件大小
        channel.position(0); // 4. 跳转到开头
    }

    @Test
    public void BinaryStreamHolderListenersTest() {
        // Redisson 允许为每个 RBinaryStream 对象绑定监听器。这需要在 Redis 端启用 notify-keyspace-events 设置。
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        int trackingListenerId = stream.addListener(new TrackingListener() {
            @Override
            public void onChange(String name) {
                // 读取操作后创建/更新的数据
            }
        });
        int setListenerId = stream.addListener(new SetObjectListener() {
            @Override
            public void onSet(String name) {
                // 数据创建/更新
            }
        });
        int expiredObjectListenerId = stream.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String name) {
                // RBucket 对象已过期
            }
        });
        int deletedListenerId = stream.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String name) {
                // RBucket 对象已删除
            }
        });
        // ...
        stream.removeListener(trackingListenerId);
        stream.removeListener(setListenerId);
        stream.removeListener(expiredObjectListenerId);
        stream.removeListener(deletedListenerId);
    }

    @Test
    @Disabled // 禁用此测试，需要 RedisJSON 模块支持
    public void JSONObjectHolderTest() {
        record AnyObject(int num) {}
        RJsonBucket<AnyObject> bucket = redissonClient.getJsonBucket("anyObject", new Jackson3Codec<>(AnyObject.class));
        bucket.set(new AnyObject(1));
        AnyObject obj = bucket.get();
        bucket.setIfAbsent(new AnyObject(3));
        bucket.compareAndSet(new AnyObject(4), new AnyObject(5));
        bucket.getAndSet(new AnyObject(6));
        List<String> values = bucket.get(new Jackson3Codec<>(new TypeReference<List<String>>() {}), "values");
        long aa = bucket.arrayAppend("$.obj.values", "t3", "t4");
    }

    @Test
    public void GeospatialHolderTest() {
        RGeo<String> geo = redissonClient.getGeo("test", StringCodec.INSTANCE);
        geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"), new GeoEntry(15.087269, 37.502669, "Catania"));
        Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS); // 计算两个地点之间的距离，单位：米
        Map<String, GeoPosition> positions = geo.pos(
                "test2", "Palermo", "test3", "Catania", "test1"); // 获取指定地点的经纬度坐标（参数是地点名称，多个地点可混合传入，不存在的地点返回 null）
        List<String> cities =
                geo.search(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS)); // 搜索指定坐标半径范围内的地点，返回地点名称列表
        Map<String, GeoPosition> citiesWithPositions = geo.searchWithPosition(
                GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS)); // 搜索指定坐标半径范围内的地点，返回地点名称及经纬度坐标
    }

    @Test
    public void BitSetTest() {
        RBitSet set1 = redissonClient.getBitSet("userSign");
        RBitSet set2 = redissonClient.getBitSet("userSign2");
        // 创建第一个位集
        set1.set(0, true); // 设置第0位为 true（用户第1天签到）
        set1.set(1, true); // 设置第1位为 true（用户第2天签到）
        set1.set(2, false); // 设置第2位为 false（用户第3天未签到）
        set1.set(3, true); // 设置第3位为 true（用户第4天签到）
        boolean day1 = set1.get(0); // 获取第0位的值
        IO.println("第1天签到: " + day1); // true
        boolean day3 = set1.get(2); // 获取第2位的值
        IO.println("第3天签到: " + day3); // false
        set1.clear(1); // 清除第1位
        IO.println("清除后第2天签到: " + set1.get(1)); // false
        long signedDays = set1.cardinality(); // 统计签到的总天数（true 的数量）
        IO.println("签到总天数: " + signedDays); // 2
        // 创建第二个位集
        set2.set(0, true); // 第1天签到
        set2.set(2, true); // 第3天签到
        set2.set(3, true); // 第4天签到
        // AND 运算：两个位集都为 true 才为 true
        set1.and("userSign2");
        IO.println("AND 运算后第0位: " + set1.get(0)); // true (都有)
        IO.println("AND 运算后第1位: " + set1.get(1)); // false (set1无)
        IO.println("AND 运算后第2位: " + set1.get(2)); // false (set2有但set1无)
        IO.println("AND 运算后第3位: " + set1.get(3)); // true (都有)
        // 重置位集
        set1.clear();
        // OR 运算
        set1.set(0, true);
        set1.set(1, true);
        set2.clear();
        set2.set(1, true);
        set2.set(2, true);
        set1.or("userSign2");
        IO.println("OR 运算后第0位: " + set1.get(0)); // true (set1有)
        IO.println("OR 运算后第1位: " + set1.get(1)); // true (都有)
        IO.println("OR 运算后第2位: " + set1.get(2)); // true (set2有)
        // XOR 运算（异或）
        set1.clear();
        set1.set(0, true);
        set1.set(1, true);
        set2.clear();
        set2.set(1, true);
        set2.set(2, true);
        set1.xor("userSign2");
        IO.println("XOR 运算后第0位: " + set1.get(0)); // true (只有set1有)
        IO.println("XOR 运算后第1位: " + set1.get(1)); // false (两个都有)
        IO.println("XOR 运算后第2位: " + set1.get(2)); // true (只有set2有)
        // NOT 运算（取反）
        set1.clear();
        set1.set(0, true);
        set1.set(1, false);
        set1.not();
        IO.println("NOT 运算后第0位: " + set1.get(0)); // false
        IO.println("NOT 运算后第1位: " + set1.get(1)); // true
        // 删除位集
        set1.delete();
        set2.delete();
    }

    @Test
    @Disabled // 禁用此测试，未研究
    public void BloomFilterTest() {
        // 使用前必须通过 tryInit(expectedInsertions, falseProbability) 方法初始化容量大小。
        record SomeObject(String val1, String val2) {}
        RBloomFilter<SomeObject> bloomFilter = redissonClient.getBloomFilter("sample");
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add(new SomeObject("field1Value", "field2Value"));
        boolean contains = bloomFilter.contains(new SomeObject("field1Value", "field8Value"));
        long count = bloomFilter.count();
    }

    @Test
    @Disabled // 禁用此测试，未研究
    public void BloomFilterNativeTest() {
        // 使用前必须通过 init(errorRate, capacity) 方法初始化错误率和容量。
        RBloomFilterNative<String> bloomFilter = redissonClient.getBloomFilterNative("sample");
        bloomFilter.init(0.03, 55000000L);
        bloomFilter.add("field1Value");
        bloomFilter.add("field2Value");
        Set<String> addedItems = bloomFilter.add(Arrays.asList("field3Value", "field4Value", "field5Value"));
        boolean isPresent = bloomFilter.exists("field1Value");
        Set<String> presentItems = bloomFilter.exists(Arrays.asList("field1Value", "field8Value"));
        long count = bloomFilter.count();
    }

    @Test
    public void BloomFilterNativeAdvancedInitializationTest() {
        // ...
    }

    @Test
    @Disabled // 禁用此测试，未研究
    public void CuckooFilterTest() {
        RCuckooFilter<String> filter = redissonClient.getCuckooFilter("myCuckooFilter");
        filter.init(100000); // 仅容量的简单初始化
        filter.init(CuckooFilterInitArgs.capacity(100000) // 带有详细参数的高级初始化
                .bucketSize(4)
                .maxIterations(500)
                .expansion(2));

        boolean added = filter.add("element1"); // 添加单个元素（允许重复）
        boolean addedNew = filter.addIfAbsent("element2"); // 仅当元素不存在时才添加
        Set<String> addedItems = filter.add( // 批量添加，带可选参数
                CuckooFilterAddArgs.<String>items(List.of("a", "b", "c"))
                        .capacity(50000)
                        .noCreate());
        Set<String> newItems = filter.addIfAbsent(  // 仅批量添加不存在的元素
                CuckooFilterAddArgs.<String>items(List.of("d", "e", "f"))
                        .capacity(50000));
        // ...
    }

    @Test
    @Disabled // 禁用此测试，未研究
    public void HyperLogLogTest() {
        RHyperLogLog<Integer> log = redissonClient.getHyperLogLog("log");
        log.add(1);
        log.add(2);
        log.add(3);
        long count = log.count();
    }

    @Test
    public void RateLimiterTest() {
        RRateLimiter limiter = redissonClient.getRateLimiter("order:create:limit");
        limiter.trySetRate(RateType.OVERALL, 5, Duration.ofSeconds(2)); // 只有在这个限流器还没初始化过时，才设置成功。
        RateLimiterConfig config = limiter.getConfig();
        limiter.acquire(3); // 1）阻塞获取：一次拿 3 个 permit，不够就一直等
        boolean r1 = limiter.tryAcquire(); // 2）立即尝试获取，不阻塞：拿不到就直接返回 false
        boolean r2 = limiter.tryAcquire(1, Duration.ofSeconds(1)); // 3）带超时的尝试获取：最多等 1 秒
    }
}
