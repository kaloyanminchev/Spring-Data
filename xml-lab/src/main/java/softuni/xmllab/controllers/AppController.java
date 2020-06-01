package softuni.xmllab.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.xmllab.domain.dtos.UserDto;
import softuni.xmllab.domain.entities.User;
import softuni.xmllab.services.UserService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class AppController implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public AppController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {


//        this.userService.seedUsers();
        this.userService.exportUsers();

        /*
        **export multiple objects to XML**
        List<UserDto> users = this.userService.findAll();

        UserRootDto userDto = new UserRootDto();
        userDto.setUsers(users);

        JAXBContext context = JAXBContext.newInstance(userDto.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(userDto, new File(USERS_FILE_PATH));
=================================================================================================

        **import single object from XML**
        JAXBContext context = JAXBContext.newInstance(UserDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        UserDto userDto =
                (UserDto) unmarshaller.unmarshal(new File("src/main/resources/xmls/export_users"));

        this.userService.save(userDto);
         */

//        UserSeedDto userSeedDto1 = new UserSeedDto("Koko", "Minchev", "Dobrich");
//        UserSeedDto userSeedDto2 = new UserSeedDto("Kamen", "Jelev", "Varna");
//        UserSeedDto userSeedDto3 = new UserSeedDto("Ivan", "Georgiev", "Munich");
//
//        this.userService.save(userSeedDto1);
//        this.userService.save(userSeedDto2);
//        this.userService.save(userSeedDto3);
    }
}
