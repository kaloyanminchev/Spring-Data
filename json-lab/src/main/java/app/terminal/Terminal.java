package app.terminal;

import app.domain.dto.PersonDto;
import app.domain.model.Person;
import app.services.PersonService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class Terminal implements CommandLineRunner {
    private static final String PERSON_JSON =
            "{\n" +
                    "  \"name\": \"Ivan1\",\n" +
                    "  \"phoneNumbers\": [\n" +
                    "    \"4444\",\n" +
                    "    \"5555\",\n" +
                    "    \"6666\"\n" +
                    "  ],\n" +
                    "  \"addressDto\": {\n" +
                    "    \"city\": \"Plovdiv\",\n" +
                    "    \"country\": \"Bulgaria\"\n" +
                    "  }\n" +
                    "}";

    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public Terminal(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
//        gsonToJson();
        gsonFromJson();
    }

    private void gsonToJson() {
        Person p = this.personService.getById(1L);
        PersonDto personDto = new PersonDto(p);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        System.out.println(gson.toJson(personDto));
    }

    private void gsonFromJson() throws IOException {
        Gson gson = new GsonBuilder().create();
        PersonDto personDto = gson.fromJson(PERSON_JSON, PersonDto.class);

        Person person = this.modelMapper.map(personDto, Person.class);
        this.personService.save(person);
    }
}
