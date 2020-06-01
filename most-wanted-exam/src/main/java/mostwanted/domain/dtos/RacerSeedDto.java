package mostwanted.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RacerSeedDto {

    @Expose
    @NotNull
    private String name;

    @Expose
    @NotNull
    private Integer age;

    @Expose
    @NotNull
    private BigDecimal bounty;

    @Expose
    @NotNull
    private String homeTown;

    public RacerSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getBounty() {
        return bounty;
    }

    public void setBounty(BigDecimal bounty) {
        this.bounty = bounty;
    }

    public String getTownName() {
        return homeTown;
    }

    public void setTownName(String homeTown) {
        this.homeTown = homeTown;
    }
}
