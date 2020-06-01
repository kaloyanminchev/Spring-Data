package softuni.xmllab.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto {

    @XmlElement(name = "first_name")
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private String city;
    @XmlElement(name = "phones")
    private PhoneRootDto phoneRootDto;

    public UserDto() {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PhoneRootDto getPhoneRootDto() {
        return phoneRootDto;
    }

    public void setPhoneRootDto(PhoneRootDto phoneRootDto) {
        this.phoneRootDto = phoneRootDto;
    }
}
