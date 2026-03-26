package com.vinsguru.redisson.test_reactive.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {

    public Mono<Void> add(UserOrder userOrder) {
        return this.queue.add(
                getScore(userOrder.getCategory()),
                userOrder
        ).then();
    }

    public Flux<UserOrder> takeItems() {
        return this.queue.takeFirstElements()
                .limitRate(1);
    }

    private double getScore(Category category) {
        return category.ordinal() + Double.parseDouble("0." + System.nanoTime());
    }

}