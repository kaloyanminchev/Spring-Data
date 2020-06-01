package com.softuni.springdataintroex.services;

import com.softuni.springdataintroex.entities.AgeRestriction;
import com.softuni.springdataintroex.entities.Author;
import com.softuni.springdataintroex.entities.Book;
import com.softuni.springdataintroex.entities.EditionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> getAllBooksAfterYear2000();

    List<Author> getAllAuthorsWithBooksBeforeYear1990();

    List<Book> findAllBooksBySpecificAuthor();

    List<Book> getAllBooksByAgeRestriction(String ageRestriction);

    List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies);

    List<Book> getAllBooksByPrice(BigDecimal lowerPrice, BigDecimal higherPrice);

    List<Book> getAllBooksNotReleasedInGivenYear(int year);

    List<Book> getAllBooksBeforeGivenReleaseDate(String date);

    List<Book> getAllBooksByTitleContainsString(String containLetters);

    int getCountBooksByLengthOfTitle(int length);

    int getTotalCopiesByAuthor(String fullName);

    int updateBooksCopiesAfterDate(String date, int copies);

    Book getBookByTitle(String title);

    int removeBooksByCopiesLessThanGivenNumber(int copies);

//    int getBooksCountByAuthor(String fullName);
}
