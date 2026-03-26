package com.vinsguru.redisson.test.dto;

import java.util.List;

public record Student(
        String name,
        int age,
        String city,
        List<Integer> marks) {
}