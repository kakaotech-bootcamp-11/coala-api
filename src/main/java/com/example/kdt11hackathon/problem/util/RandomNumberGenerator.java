package com.example.kdt11hackathon.problem.util;

import java.util.Random;

public class RandomNumberGenerator {
    static int size = 40;
    public static int generate() {
        Random random = new Random();
        return random.nextInt(size);
    }

}
