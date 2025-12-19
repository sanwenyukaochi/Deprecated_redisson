package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.assignment.Category;
import com.vinsguru.redisson.test.assignment.PriorityQueue;
import com.vinsguru.redisson.test.assignment.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.codec.TypedJsonJacksonCodec;

public class Lec16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupQueue() {
        RScoredSortedSet<UserOrder> sortedSet = this.client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(sortedSet);
    }

    @Test
    public void producer() {
        for (int round = 0; round < 60; round++) {
            int base = round * 5;
            UserOrder u1 = new UserOrder(base + 1, Category.GUEST);
            UserOrder u2 = new UserOrder(base + 2, Category.STD);
            UserOrder u3 = new UserOrder(base + 3, Category.PRIME);
            UserOrder u4 = new UserOrder(base + 4, Category.STD);
            UserOrder u5 = new UserOrder(base + 5, Category.GUEST);
            this.priorityQueue.add(u1);
            this.priorityQueue.add(u2);
            this.priorityQueue.add(u3);
            this.priorityQueue.add(u4);
            this.priorityQueue.add(u5);
            sleep(1000);
        }
        sleep(60_000);
    }

    @Test
    public void consumer() {
        while (true) {
            UserOrder orders = this.priorityQueue.takeItems();
            if (orders != null) IO.println(orders); else break;
            sleep(500);
        }
        sleep(600_000);
    }

}