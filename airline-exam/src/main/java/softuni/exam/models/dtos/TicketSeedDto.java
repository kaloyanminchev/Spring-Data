package softuni.exam.models.dtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.adapters.LocalDateTimeAdapter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;

    @XmlElement
    private BigDecimal price;

    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeoff;

    @XmlElement(name = "from-town")
    private FromTownSeedDto fromTown;

    @XmlElement(name = "to-town")
    private ToTownSeedDto toTown;

    @XmlElement(name = "passenger")
    private PassengerEmailSeedDto passenger;

    @XmlElement(name = "plane")
    private PlaneRegisterNumberSeedDto plane;

    public TicketSeedDto() {
    }

    @Length(min = 2)
    @NotNull
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @DecimalMin("0")
    @NotNull
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    @NotNull
    public FromTownSeedDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownSeedDto fromTown) {
        this.fromTown = fromTown;
    }

    @NotNull
    public ToTownSeedDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownSeedDto toTown) {
        this.toTown = toTown;
    }

    @NotNull
    public PassengerEmailSeedDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailSeedDto passenger) {
        this.passenger = passenger;
    }

    @NotNull
    public PlaneRegisterNumberSeedDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumberSeedDto plane) {
        this.plane = plane;
    }
}
