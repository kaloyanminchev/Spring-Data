package mostwanted.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "race_entries")
public class RaceEntry extends BaseEntity {

    private Boolean hasFinished;
    private Double finishTime;
    private Car car;
    private Racer racer;

    public RaceEntry() {
    }

    @Column(name = "has_finished")
    public Boolean isHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(Boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    @Column(name = "finish_time")
    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    @OneToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @OneToOne
    public Racer getRacer() {
        return racer;
    }

    public void setRacer(Racer racer) {
        this.racer = racer;
    }

}
