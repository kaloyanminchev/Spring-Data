package com.example.xmlexdemo.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {
    private String make;
    private String model;
    private Integer travelledDistance;
    private Set<Part> parts;
    private Sale sale;
    private BigDecimal price;

    public Car() {
    }

    @Column(name = "make")
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "travelled_distance")
    public Integer getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Integer travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "cars_parts",
            joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

    @OneToOne(mappedBy = "car", targetEntity = Sale.class)
    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public BigDecimal getPrice() {
        BigDecimal price = new BigDecimal(0);
        for (Part part : this.getParts()) {
            price = price.add(part.getPrice());
        }

        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
