package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.dto.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;

import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest {

    @Test
    public void mapTest1(){
        RMap<String, String> map = client.getMap("user:1", StringCodec.INSTANCE);
        map.put("name", "sam");
        map.put("age", "10");
        map.put("city", "atlanta");
        Assertions.assertEquals("sam", map.get("name"));
        Assertions.assertEquals("10", map.get("age"));
        Assertions.assertEquals("atlanta", map.get("city"));
    }


    @Test
    public void mapTest2(){
        RMap<String, String> map = client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> javaMap = Map.of("name", "jake", "age", "30", "city", "miami");
        map.putAll(javaMap);
        Assertions.assertEquals("jake", map.get("name"));
        Assertions.assertEquals("30", map.get("age"));
        Assertions.assertEquals("miami", map.get("city"));
    }

    @Test
    public void mapTest3(){
        // Map<Integer, Student>
        RMap<Integer, Student> map = client.getMap("users", new TypedJsonJacksonCodec(Integer.class, Student.class));
        Student student1 = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "miami", List.of(10, 20, 30));

        map.put(1, student1);
        map.put(2, student2);

        Student s1 = map.get(1);
        Student s2 = map.get(2);

        Assertions.assertEquals("sam", s1.getName());
        Assertions.assertEquals("jake", s2.getName());
    }

}