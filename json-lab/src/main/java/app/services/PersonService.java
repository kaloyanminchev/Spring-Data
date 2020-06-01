package app.services;

import app.domain.model.Person;

public interface PersonService {
    Person getById(Long id);

    void save(Person person);
}
