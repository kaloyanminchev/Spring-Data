package com.jsonexdemo.models.dtos.queryDtos.query4;

import com.google.gson.annotations.Expose;

public class UserViewDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int age;
    @Expose
    private SoldProductsViewDto soldProducts;

    public UserViewDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public SoldProductsViewDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(SoldProductsViewDto soldProducts) {
        this.soldProducts = soldProducts;
    }
}
