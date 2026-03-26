package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.client.codec.LongCodec;

public class Lec13BatchTest extends BaseTest {

    @Test // 7.6
    public void batchTestBlocking() {
        RBatch batch = client.createBatch(BatchOptions.defaults());

        RListAsync<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetAsync<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 0; i < 1_000; i++) {
            list.addAsync(i);
            set.addAsync(i);
        }

        batch.execute();
    }


    @Test
    public void regularTest_blocking() {

        RList<Long> list = client.getList("numbers-list", LongCodec.INSTANCE);
        RSet<Long> set = client.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 1; i <= 500_000; i++) {
            list.add(i);
            set.add(i);
        }
    }

}