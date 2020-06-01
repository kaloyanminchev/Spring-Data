package app.services.impls;

import app.domain.model.Person;
import app.repository.PersonRepository;
import app.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person getById(Long id) {
        return this.personRepository.getOne(id);
    }

    @Override
    public void save(Person person) {
        this.personRepository.save(person);
    }
}
