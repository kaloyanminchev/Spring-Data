package softuni.xmllab.services;

import softuni.xmllab.domain.dtos.UserDto;
import softuni.xmllab.domain.dtos.UserSeedDto;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {
    <T> void save(T userObject);

    List<UserDto> findAll();

    UserDto findById(long id);

    void seedUsers() throws JAXBException, FileNotFoundException;

    void exportUsers() throws JAXBException;
}
