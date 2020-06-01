package com.jsonexdemo.services;

import com.jsonexdemo.models.dtos.queryDtos.query2.UserWithSuccessfullySoldProductsDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.UsersAndProductsViewDto;
import com.jsonexdemo.models.dtos.seedDtos.UserSeedDto;
import com.jsonexdemo.models.entities.User;

import java.util.List;

public interface UserService {
    void seedUsers(UserSeedDto[] userSeedDtos);

    User getRandomUser();

    List<UserWithSuccessfullySoldProductsDto> getUsersByProducts(int number);

    UsersAndProductsViewDto getUsersAndProducts();
}
