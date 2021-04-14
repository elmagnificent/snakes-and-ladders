package com.test.services;

import com.test.entities.Game;
import com.test.model.api.GameDTO;
import com.test.repositories.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Michael on 14.04.2021.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private GameDTO newDto = new GameDTO(null, 1);
    private Game existingEntity1 = new Game(1L, 97);
    private Game existingEntity2 = new Game(2L, 10);
    private GameDTO existingDto1 = new GameDTO(1L, 97);
    private GameDTO existingDto2 = new GameDTO(2L, 11);
    private GameDTO gameWonDto = new GameDTO(1L, 100);

    @Before
    public void init() {
        Mockito.when(gameRepository.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.of(existingEntity1));
        Mockito.when(gameRepository.findById(2L)).thenReturn(Optional.of(existingEntity2));
    }

    @Test
    public void getNull_shouldCreateNew() {
        assertEquals(newDto, gameService.get(null));
    }

    @Test
    public void getExisting_shouldReturn() {
        assertEquals(existingDto1, gameService.get(1L));
    }

    @Test
    public void moveToken_shouldBeMoved() {
        assertEquals(existingDto2, gameService.moveToken(2L, 1));
    }

    @Test
    public void moveIllegal_shouldReturn() {
        assertEquals(existingDto1, gameService.moveToken(1L, 4));
    }

    @Test
    public void shouldWinOnlyIfMatchExactly() {
        assertEquals(gameWonDto, gameService.moveToken(1L, 3));
    }
}
