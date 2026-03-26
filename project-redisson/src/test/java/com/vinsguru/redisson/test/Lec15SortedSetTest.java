package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;

import java.util.Map;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void sortedSet() {
        RScoredSortedSet<String> sortedSet = this.client.getScoredSortedSet("student:score", StringCodec.INSTANCE);
        sortedSet.addScore("sam", 12.25);
        sortedSet.add(23.25, "mike");
        sortedSet.addScore("jake", 7);
        sortedSet.entryRange(0, 1)
                .forEach(se ->
                        IO.println(se.getScore() + " : " + se.getValue())
                );
    }

    @Test
    public void test(){
        Map<String, Long> a = Map.of(
                "a", 10L,
                "b", 15L
        );
        RBatch batch = this.client.createBatch(BatchOptions.defaults());
        RScoredSortedSetAsync<String> set = batch.getScoredSortedSet("prod:score", StringCodec.INSTANCE);
        a.forEach(set::addScoreAsync);
        BatchResult<?> result = batch.execute();
        IO.println(result.getResponses());
    }

}