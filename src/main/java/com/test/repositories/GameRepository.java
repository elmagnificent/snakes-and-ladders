package com.test.repositories;

import com.test.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Michael on 12.04.2021.
 */
public interface GameRepository extends JpaRepository<Game, Long> {
}
