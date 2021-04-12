package com.test.entities;

import javax.persistence.*;

/**
 * Created by Michael on 12.04.2021.
 */
@Table(name = "GAME")
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN_POSITION")
    private Integer tokenPosition = 1;
}
