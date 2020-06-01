package com.softuni.springdataintroex.services.impl;

import com.softuni.springdataintroex.entities.Author;
import com.softuni.springdataintroex.repositories.AuthorRepository;
import com.softuni.springdataintroex.services.AuthorService;
import com.softuni.springdataintroex.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.softuni.springdataintroex.constants.GlobalConstants.*;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0) {
            return;
        }

        this.fileUtil.readFileContent(AUTHORS_FILE_PATH)
                .forEach(r -> {
                    String[] params = r.split("\\s+");
                    Author author = new Author(params[0], params[1]);

                    this.authorRepository.saveAndFlush(author);
                });
    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(Long id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public List<Author> findAllAuthorsByCountOfBooks() {
        return this.authorRepository.getAllAuthorsByCountOfBooks();
    }

    @Override
    public Author getRandomAuthor() {
        Random random = new Random();
        int randomId = random.nextInt(this.getAllAuthorsCount()) + 1;

        return this.findAuthorById((long) randomId);
    }

    @Override
    public List<Author> getAuthorsByFirstNameEndsWith(String endsWith) {
        return this.authorRepository.findAllByFirstNameEndsWith(endsWith);
    }

    @Override
    public List<Author> getAuthorsByLastNameStartsWith(String startsWith) {
        return this.authorRepository.findAllByLastNameStartsWith(startsWith);
    }
}
