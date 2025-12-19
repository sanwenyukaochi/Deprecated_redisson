package com.vinsguru.redisson.test_reactive;

import com.vinsguru.redisson.test_reactive.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class Lec02KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest(){
        Student student = new Student("marshal", 10, "atlanta", List.of(1,2, 3));
        RBucketReactive<Student> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        StepVerifier.create(set.then(get))
                .verifyComplete();
    }

}