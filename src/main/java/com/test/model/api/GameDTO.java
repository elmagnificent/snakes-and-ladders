package com.test.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Michael on 12.04.2021.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Long id;
    private int playerPosition;
}
