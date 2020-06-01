package com.jsonexdemo.models.dtos.queryDtos.query2;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserWithSuccessfullySoldProductsDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private List<SoldProductDto> soldProducts;

    public UserWithSuccessfullySoldProductsDto() {
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


    public List<SoldProductDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<SoldProductDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
