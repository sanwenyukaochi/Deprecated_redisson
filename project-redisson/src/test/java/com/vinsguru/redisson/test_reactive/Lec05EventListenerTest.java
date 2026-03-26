package com.vinsguru.redisson.test_reactive;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec05EventListenerTest extends BaseTest {

    /* Setting up in Redis
    * config set notify-keyspace-events AKE
    * docs/keyspace-notifications.md
    * https://redis.io/docs/latest/develop/pubsub/keyspace-notifications/
    * */
    @Test
    public void expiredEventTest() {
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        Mono<Void> event = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String s) {
                IO.println("Expired : " + s);
            }
        }).then();

        StepVerifier.create(set.then(get).then(event))
                .verifyComplete();
        //extend
        sleep(11000);
    }

    @Test
    public void deletedEventTest() {
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(IO::println)
                .then();
        Mono<Void> event = bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String s) {
                IO.println("Deleted : " + s);
            }
        }).then();

        StepVerifier.create(set.then(get).then(event))
                .verifyComplete();
        //extend
        sleep(60000);
    }

}