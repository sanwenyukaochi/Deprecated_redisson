package com.vinsguru.redisson.test_reactive;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec01KeyValueTest extends BaseTest{

    @Test
    public void keyValueAccessTest(){
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        StepVerifier.create(set.then(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExpiryTest(){
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        StepVerifier.create(set.then(get))
                .verifyComplete();
    }

    @Test
    @SneakyThrows
    public void keyValueExtendExpiryTest(){
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        StepVerifier.create(set.then(get))
                .verifyComplete();
        //extend
        sleep(5000);
        Mono<Boolean> mono = bucket.expire(Duration.ofSeconds(60));
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();
        // access expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(IO::println)
                .then();
        StepVerifier.create(ttl)
                .verifyComplete();
    }

}