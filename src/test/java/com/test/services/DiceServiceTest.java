package com.test.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Michael on 14.04.2021.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiceServiceTest {
    @Autowired
    private DiceService diceService;

    @Test
    public void getRoll_shouldReturnFrom1To6() {
        int result = diceService.getRoll();
        assertTrue(result > 0);
        assertTrue(result < 7);
    }
}
