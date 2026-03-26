package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.codec.TypedJsonJackson3Codec;

import java.util.List;

public class Lec02KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest(){
        Student student = new Student("name", 10, "city", List.of(1,2, 3));
        redissonClient.<Student>getBucket("student:1", new TypedJsonJackson3Codec(Student.class)).set(student);
        redissonClient.<Student>getBucket("student:1", new TypedJsonJackson3Codec(Student.class)).get();
    }

}