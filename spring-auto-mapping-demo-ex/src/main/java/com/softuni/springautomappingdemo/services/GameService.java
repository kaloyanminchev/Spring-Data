package com.softuni.springautomappingdemo.services;

import com.softuni.springautomappingdemo.domain.dtos.GameAddDto;
import com.softuni.springautomappingdemo.domain.entities.Game;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    Game getGameById(Long id);
}
