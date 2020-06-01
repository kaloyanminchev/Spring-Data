package com.example.xmlexdemo.models.dtos.seeddtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarSeedDto {

    @XmlElement
    private String make;

    @XmlElement
    private String model;

    @XmlElement(name = "travelled-distance")
    private Integer travelledDistance;

    public CarSeedDto() {
    }

    @NotNull(message = "Make cannot be null!")
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @NotNull(message = "Model cannot be null!")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Min(value = 0, message = "Travelled distance cannot be negative number!")
    public Integer getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Integer travelledDistance) {
        this.travelledDistance = travelledDistance;
    }
}
