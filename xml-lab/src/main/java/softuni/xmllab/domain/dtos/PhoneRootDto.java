package softuni.xmllab.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "phones")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneRootDto {

    @XmlElement(name = "phone")
    private List<PhoneDto> phoneDtos;

    public PhoneRootDto() {
    }

    public List<PhoneDto> getPhoneDtos() {
        return phoneDtos;
    }

    public void setPhoneDtos(List<PhoneDto> phoneDtos) {
        this.phoneDtos = phoneDtos;
    }
}
