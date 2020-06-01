package softuni.xmllab.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.xmllab.domain.dtos.PhoneDto;
import softuni.xmllab.domain.dtos.UserDto;
import softuni.xmllab.domain.dtos.UserRootDto;
import softuni.xmllab.domain.dtos.UserSeedDto;
import softuni.xmllab.domain.entities.Phone;
import softuni.xmllab.domain.entities.User;
import softuni.xmllab.repositories.PhoneRepository;
import softuni.xmllab.repositories.UserRepository;
import softuni.xmllab.services.UserService;
import softuni.xmllab.utils.XMLParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    public static final String USERS_FILE_PATH = "src/main/resources/xmls/users";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final PhoneRepository phoneRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           XMLParser xmlParser,
                           PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public <T> void save(T userObject) {
        this.userRepository.saveAndFlush(this.modelMapper.map(userObject, User.class));
    }

    @Override
    public List<UserDto> findAll() {
        return this.userRepository
                .findAll()
                .stream()
                .map(user -> {
                    return this.modelMapper.map(user, UserDto.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        User user = this.userRepository.findById(id).orElse(null);
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public void seedUsers() throws JAXBException, FileNotFoundException {
        UserRootDto userRootDto = this.xmlParser.importFromXML(UserRootDto.class, USERS_FILE_PATH);

        for (UserDto userDto : userRootDto.getUsers()) {

            User user = this.modelMapper.map(userDto, User.class);
            this.userRepository.saveAndFlush(user);

            Set<Phone> phones = new LinkedHashSet<>();
            for (PhoneDto phoneDto : userDto.getPhoneRootDto().getPhoneDtos()) {
                if (this.phoneRepository.findByPhoneNumber(phoneDto.getPhoneNumber()) != null) {
                    System.out.println(phoneDto.getPhoneNumber() + " already exists in database!");
                    return;
                }

                Phone phone =
                        this.phoneRepository.saveAndFlush(this.modelMapper.map(phoneDto, Phone.class));

                phone.setUser(user);
                phones.add(phone);
            }

            user.setPhones(phones);

            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public void exportUsers() throws JAXBException {
        List<UserDto> userDtos = this.findAll();

        UserRootDto userRootDto = new UserRootDto();
        userRootDto.setUsers(userDtos);

        this.xmlParser.exportToXML(userRootDto, "src/main/resources/xmls/export_users");
    }
}
