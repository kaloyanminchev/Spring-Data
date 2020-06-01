package mostwanted.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "race-entries")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceEntrySeedRootDto {

    @XmlElement(name = "race-entry")
    private List<RaceEntrySeedDto> entries;

    public RaceEntrySeedRootDto() {
    }

    public List<RaceEntrySeedDto> getEntries() {
        return entries;
    }

    public void setEntries(List<RaceEntrySeedDto> entries) {
        this.entries = entries;
    }
}
