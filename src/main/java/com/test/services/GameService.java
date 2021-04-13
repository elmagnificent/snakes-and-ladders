package com.test.services;

import com.test.entities.Game;
import com.test.model.api.GameDTO;
import com.test.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private static final int MAX_POSITION = 100;

    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public GameDTO get(Long id) {
        Game entity;
        if (id == null) {
            entity = gameRepository.save(new Game());
        } else {
            entity = gameRepository.findById(id).orElseGet(() -> {
                Game game = new Game();
                return gameRepository.save(game);
            });
        }
        return new GameDTO(entity.getId(), entity.getTokenPosition());
    }

    @Transactional
    public GameDTO moveToken(Long gameId, int tokenMoved) {
        Game entity = gameRepository.findById(gameId).orElse(null);
        if (entity == null) {
            entity = new Game();
            entity.setTokenPosition(tokenMoved);
        } else {
            int nextPosition = entity.getTokenPosition() + tokenMoved;
            if (nextPosition > MAX_POSITION) {
                return new GameDTO(entity.getId(), entity.getTokenPosition());
            }
            entity.setTokenPosition(nextPosition);
        }
        entity = gameRepository.save(entity);
        return new GameDTO(entity.getId(), entity.getTokenPosition());
    }
}
