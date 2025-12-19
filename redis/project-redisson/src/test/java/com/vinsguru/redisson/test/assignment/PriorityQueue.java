package com.vinsguru.redisson.test.assignment;

import org.redisson.api.RScoredSortedSet;

public record PriorityQueue(RScoredSortedSet<UserOrder> queue) {

    public void add(UserOrder userOrder) {
        this.queue.add(
                getScore(userOrder.getCategory()),
                userOrder
        );
    }

    public UserOrder takeItems() {
        return queue.pollFirst();
    }

    private double getScore(Category category) {
        return category.ordinal() + Double.parseDouble("0." + System.nanoTime());
    }

}