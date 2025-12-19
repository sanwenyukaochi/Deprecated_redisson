package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.dto.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCache;
import org.redisson.codec.TypedJsonJacksonCodec;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec07MapCacheTest extends BaseTest {

    @Test
    public void mapCacheTest_blocking(){
        // Map<Integer, Student>
        RMapCache<Integer, Student> mapCache = client.getMapCache("users:cache", new TypedJsonJacksonCodec(Integer.class, Student.class));

        Student student1 = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "miami", List.of(10, 20, 30));

        // put with TTL
        mapCache.put(1, student1, 5, TimeUnit.SECONDS);
        mapCache.put(2, student2, 10, TimeUnit.SECONDS);

        sleep(3000);
        // access students
        IO.println("After 3 seconds:");
        IO.println(mapCache.get(1));
        IO.println(mapCache.get(2));

        sleep(3000);

        // access students
        IO.println("After 6 seconds:");
        IO.println(mapCache.get(1));
        IO.println(mapCache.get(2));

        Assertions.assertNull(mapCache.get(1));
        Assertions.assertNotNull(mapCache.get(2));
    }

}