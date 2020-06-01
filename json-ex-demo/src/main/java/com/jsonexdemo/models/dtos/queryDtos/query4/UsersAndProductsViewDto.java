package com.jsonexdemo.models.dtos.queryDtos.query4;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UsersAndProductsViewDto {

    @Expose
    private Integer usersCount;
    @Expose
    private List<UserViewDto> users;

    public UsersAndProductsViewDto() {
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserViewDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserViewDto> users) {
        this.users = users;
    }
}
