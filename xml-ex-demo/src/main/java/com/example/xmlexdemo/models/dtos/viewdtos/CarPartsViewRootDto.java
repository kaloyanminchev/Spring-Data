package com.example.xmlexdemo.models.dtos.viewdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarPartsViewRootDto {

    @XmlElement(name = "car")
    private List<CarPartsViewDto> cars;

    public CarPartsViewRootDto() {
    }

    public List<CarPartsViewDto> getCars() {
        return cars;
    }

    public void setCars(List<CarPartsViewDto> cars) {
        this.cars = cars;
    }
}
