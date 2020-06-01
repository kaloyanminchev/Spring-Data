package app.domain.dto;

import app.domain.model.Address;
import com.google.gson.annotations.Expose;

public class AddressDto {
    @Expose
    private String city;
    @Expose
    private String country;
    private String street;

    public AddressDto(Address address) {
        this.city = address.getCity();
        this.country = address.getCountry();
        this.street = address.getStreet();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
