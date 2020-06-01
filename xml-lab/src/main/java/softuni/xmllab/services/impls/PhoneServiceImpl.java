package softuni.xmllab.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.xmllab.domain.dtos.PhoneDto;
import softuni.xmllab.domain.entities.Phone;
import softuni.xmllab.repositories.PhoneRepository;
import softuni.xmllab.services.PhoneService;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            ModelMapper modelMapper) {
        this.phoneRepository = phoneRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(PhoneDto phoneDto) {
        this.phoneRepository.saveAndFlush(this.modelMapper.map(phoneDto, Phone.class));
    }

    @Override
    public Phone getByPhoneNumber(String phoneNumber) {
        return this.phoneRepository.findByPhoneNumber(phoneNumber);
    }
}
