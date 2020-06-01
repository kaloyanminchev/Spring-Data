package com.softuni.springautomappingdemo.services.impls;

import com.softuni.springautomappingdemo.domain.dtos.UserDto;
import com.softuni.springautomappingdemo.domain.dtos.UserLoginDto;
import com.softuni.springautomappingdemo.domain.dtos.UserRegisterDto;
import com.softuni.springautomappingdemo.domain.entities.Role;
import com.softuni.springautomappingdemo.domain.entities.User;
import com.softuni.springautomappingdemo.repositories.UserRepository;
import com.softuni.springautomappingdemo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private UserDto userDto;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        User user = this.modelMapper.map(userRegisterDto, User.class);

        user.setRole(this.userRepository.count() == 0 ? Role.ADMIN : Role.USER);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {
        User user = this.userRepository.findByEmail(userLoginDto.getEmail());

        if (user == null) {
            System.out.println("Incorrect username / password");
        } else {
            this.userDto = this.modelMapper.map(user, UserDto.class);
            System.out.println("Successfully logged in " + user.getFullName());
        }
    }

    @Override
    public void logout() {
        String result;

        if (this.userDto == null) {
            System.out.println("Cannot log out. No user was logged in.");
        } else {
            String name = this.userDto.getFullName();
            this.userDto = null;
            System.out.printf("User %s successfully logged out%n", name);
        }
    }

    @Override
    public boolean isLoggedUserAdmin() {
        return this.userDto.getRole().equals(Role.ADMIN);
    }
}
