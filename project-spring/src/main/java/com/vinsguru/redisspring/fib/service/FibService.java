package com.vinsguru.redisspring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    // have a strategy for cache evict
    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index){
        IO.println("calculating fib for " + index);
        return this.fib(index);
    }

    // PUT / POST / PATCH / DELETE
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index){
        IO.println("clearing hash key");
    }

  //  @Scheduled(fixedRate = 10_000)
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache(){
        IO.println("clearing all fib keys");
    }

    //intentional 2^N
    private int fib(int index){
        if(index < 2)
            return index;
        return fib(index - 1) + fib(index - 2);
    }

}