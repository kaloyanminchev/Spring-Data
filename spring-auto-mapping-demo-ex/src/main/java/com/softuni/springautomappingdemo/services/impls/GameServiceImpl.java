package com.softuni.springautomappingdemo.services.impls;

import com.softuni.springautomappingdemo.domain.dtos.GameAddDto;
import com.softuni.springautomappingdemo.domain.entities.Game;
import com.softuni.springautomappingdemo.repositories.GameRepository;
import com.softuni.springautomappingdemo.services.GameService;
import com.softuni.springautomappingdemo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserService userService, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {
        if (!this.userService.isLoggedUserAdmin()) {
            System.out.println("Logged user is not admin");
            return;
        }

        Game game = this.modelMapper.map(gameAddDto, Game.class);
        this.gameRepository.saveAndFlush(game);
        System.out.println("Added " + game.getTitle());
    }

    @Override
    public Game getGameById(Long id) {
        return this.gameRepository.findOneById(id);
    }
}
