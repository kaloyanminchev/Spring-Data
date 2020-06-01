package softuni.xmllab.services;

import softuni.xmllab.domain.dtos.PhoneDto;
import softuni.xmllab.domain.entities.Phone;

public interface PhoneService {
    void save(PhoneDto phoneDto);

    Phone getByPhoneNumber(String phoneNumber);
}
