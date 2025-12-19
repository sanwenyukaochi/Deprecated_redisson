package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.dto.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.codec.TypedJsonJacksonCodec;

import java.util.List;

public class Lec02KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest(){
        Student student = new Student("marshal", 10, "atlanta", List.of(1,2, 3));
        RBucket<Student> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        bucket.set(student);
        Student value = bucket.get();
        IO.println("value = " + value);
        Assertions.assertEquals(student, value);

    }

}