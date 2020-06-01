package com.example.xmlexdemo.models.dtos.viewdtos;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class    CarPartsViewDto {

    @XmlAttribute
    private String make;

    @XmlAttribute
    private String model;

    @XmlAttribute(name = "travelled-distance")
    private Integer travelledDistance;

    @XmlElement(name = "parts")
    private PartViewRootDto partViewRootDto;

    public CarPartsViewDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Integer travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public PartViewRootDto getPartViewRootDto() {
        return partViewRootDto;
    }

    public void setPartViewRootDto(PartViewRootDto partViewRootDto) {
        this.partViewRootDto = partViewRootDto;
    }
}
