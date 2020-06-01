package mostwanted.domain.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "races")
public class Race extends BaseEntity {

    private Integer laps = 0;
    private District district;


    public Race() {
    }

    @Column(nullable = false)
    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    @ManyToOne
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }


}
