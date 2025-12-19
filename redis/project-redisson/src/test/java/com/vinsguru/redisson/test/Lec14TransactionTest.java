package com.vinsguru.redisson.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RTransaction;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;

public class Lec14TransactionTest extends BaseTest {

    private RBucket<Long> user1Balance;
    private RBucket<Long> user2Balance;

    @BeforeAll
    public void accountSetup() {
        this.user1Balance = this.client.getBucket("user:1:balance", LongCodec.INSTANCE);
        this.user2Balance = this.client.getBucket("user:2:balance", LongCodec.INSTANCE);

        user1Balance.set(100L);
        user2Balance.set(0L);
    }


    @AfterAll
    public void accountBalanceStatus() {
        IO.println(user1Balance.get());
        IO.println(user2Balance.get());
    }

    @Test
    public void nonTransactionTest() {
        try {
            this.transfer(user1Balance, user2Balance, 50);
            int i = 0;
            int r = 5 / i;
        } catch (Exception e) {
            IO.println(e);
        }
        sleep(1000);
    }

    @Test
    public void transactionTest() {
        RTransaction transaction = this.client.createTransaction(TransactionOptions.defaults());
        RBucket<Long> user1Balance = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucket<Long> user2Balance = transaction.getBucket("user:2:balance", LongCodec.INSTANCE);
        try {
            this.transfer(user1Balance, user2Balance, 50);
            int i = 0;
            int r = 5 / i;
            transaction.commit();
        } catch (Exception e) {
            IO.println(e);
            transaction.rollback();
        }
        sleep(1000);
    }

    private void transfer(RBucket<Long> fromAccount,
                          RBucket<Long> toAccount,
                          int amount) {
        Long fromBalance = fromAccount.get();
        Long toBalance = toAccount.get();
        if (fromBalance < amount) {
            return; // 余额不足，直接结束
        }
        fromAccount.set(fromBalance - amount);
        toAccount.set(toBalance + amount);
    }

}