package com.softuni.springdataintroex.services;

import com.softuni.springdataintroex.entities.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(Long id);

    List<Author> findAllAuthorsByCountOfBooks();

    Author getRandomAuthor();

    List<Author> getAuthorsByFirstNameEndsWith(String endsWith);

    List<Author> getAuthorsByLastNameStartsWith(String startsWith);
}
