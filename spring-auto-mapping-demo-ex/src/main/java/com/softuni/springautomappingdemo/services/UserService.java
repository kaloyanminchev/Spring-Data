package com.softuni.springautomappingdemo.services;

import com.softuni.springautomappingdemo.domain.dtos.UserLoginDto;
import com.softuni.springautomappingdemo.domain.dtos.UserRegisterDto;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logout();

    boolean isLoggedUserAdmin();
}
