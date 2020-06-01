package com.softuni.springautomappingdemo.controllers;

import com.softuni.springautomappingdemo.domain.dtos.GameAddDto;
import com.softuni.springautomappingdemo.domain.dtos.UserLoginDto;
import com.softuni.springautomappingdemo.domain.dtos.UserRegisterDto;
import com.softuni.springautomappingdemo.services.GameService;
import com.softuni.springautomappingdemo.services.UserService;
import com.softuni.springautomappingdemo.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AppController implements CommandLineRunner {
    private final BufferedReader bufferedReader;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public AppController(BufferedReader bufferedReader, ValidationUtil validationUtil, UserService userService, GameService gameService) {
        this.bufferedReader = bufferedReader;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("Enter command:");
            String[] input = this.bufferedReader.readLine().split("\\|");

            switch (input[0]) {
                case "RegisterUser":
                    if (!input[2].equals(input[3])) {
                        System.out.println("Passwords don't match!");
                        break;
                    }

                    UserRegisterDto userRegisterDto =
                            new UserRegisterDto(input[1], input[2], input[4]);

                    if (this.validationUtil.isValid(userRegisterDto)) {
                        this.userService.registerUser(userRegisterDto);
                        System.out.printf("%s was registered%n", input[4]);
                    } else {
                        this.validationUtil
                                .getViolations(userRegisterDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }

                    break;
                case "LoginUser":
                    UserLoginDto userLoginDto = new UserLoginDto(input[1], input[2]);
                    this.userService.loginUser(userLoginDto);
                    break;
                case "Logout":
                    this.userService.logout();
                    return;
                case "AddGame":
                    GameAddDto gameAddDto = new GameAddDto(
                            input[1],
                            new BigDecimal(input[2]),
                            Double.parseDouble(input[3]),
                            input[4],
                            input[5],
                            input[6],
                            LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    );

                    if (this.validationUtil.isValid(gameAddDto)) {
                        this.gameService.addGame(gameAddDto);
                        System.out.println("Added " + gameAddDto.getTitle());
                    } else {
                        this.validationUtil
                                .getViolations(gameAddDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                    break;
            }
        }
    }
}
