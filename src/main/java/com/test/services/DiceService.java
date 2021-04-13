package com.test.services;

import com.test.controllers.ApiController;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by Michael on 12.04.2021.
 */
@Service
public class DiceService {
    private final Random random = new Random();

    public int getRoll() {
        return random.nextInt(ApiController.MAX_DICE_ROLL) + 1;
    }
}
