package softuni.exam.models.dtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.adapters.LocalDateTimeAdapter;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @XmlElement
    private String description;

    @XmlElement
    private BigDecimal price;

    @XmlElement(name = "added-on")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime addedOn;

    @XmlElement(name = "has-gold-status")
    private Boolean hasGoldStatus;

    @XmlElement
    private CarDto car;

    @XmlElement
    private SellerDto seller;

    public OfferSeedDto() {
    }

    @Length(min = 5)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DecimalMin(value = "0")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public Boolean getHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(Boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public SellerDto getSeller() {
        return seller;
    }

    public void setSeller(SellerDto seller) {
        this.seller = seller;
    }
}
