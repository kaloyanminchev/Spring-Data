package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import softuni.exam.domain.entities.Position;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PlayerSeedDto {

    @Expose
    @NotNull
    private String firstName;

    @Expose
    @NotNull
    @Length(min = 3, max = 15)
    private String lastName;

    @Expose
    @NotNull
    @Range(min = 1, max = 99)
    private Integer number;

    @Expose
    @NotNull
    @DecimalMin("0")
    private BigDecimal salary;

    @Expose
    @NotNull
    private Position position;

    @Expose
    @NotNull
    private PictureSeedDto picture;

    @Expose
    @NotNull
    private TeamSeedDto team;

    public PlayerSeedDto() {
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PictureSeedDto getPicture() {
        return picture;
    }

    public void setPicture(PictureSeedDto picture) {
        this.picture = picture;
    }

    public TeamSeedDto getTeam() {
        return team;
    }

    public void setTeam(TeamSeedDto team) {
        this.team = team;
    }
}
