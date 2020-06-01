package com.softuni.springdataintroex.services.impl;

import com.softuni.springdataintroex.entities.*;
import com.softuni.springdataintroex.repositories.BookRepository;
import com.softuni.springdataintroex.services.AuthorService;
import com.softuni.springdataintroex.services.BookService;
import com.softuni.springdataintroex.services.CategoryService;
import com.softuni.springdataintroex.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.softuni.springdataintroex.constants.GlobalConstants.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final FileUtil fileUtil;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, FileUtil fileUtil, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.fileUtil = fileUtil;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        this.fileUtil.readFileContent(BOOKS_FILE_PATH)
                .forEach(r -> {
                    String[] params = r.split("\\s+");

                    Author author = this.authorService.getRandomAuthor();
                    EditionType editionType = EditionType.values()[Integer.parseInt(params[0])];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    LocalDate releaseDate = LocalDate.parse(params[1], formatter);

                    int copies = Integer.parseInt(params[2]);
                    BigDecimal price = new BigDecimal(params[3]);
                    AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(params[4])];
                    String title = getTitle(params);
                    Set<Category> categories = this.categoryService.getRandomCategories();

                    Book book = new Book();
                    book.setAuthor(author);
                    book.setEditionType(editionType);
                    book.setReleaseDate(releaseDate);
                    book.setCopies(copies);
                    book.setPrice(price);
                    book.setAgeRestriction(ageRestriction);
                    book.setTitle(title);
                    book.setCategories(categories);

                    this.bookRepository.saveAndFlush(book);
                });

    }

    private String getTitle(String[] params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 5; i < params.length; i++) {
            sb.append(params[i]).append(" ");
        }

        return sb.toString().trim();
    }

    @Override
    public List<Book> getAllBooksAfterYear2000() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate releaseDate = LocalDate.parse("31/12/2000", formatter);

        return this.bookRepository.findAllByReleaseDateAfter(releaseDate);
    }

    @Override
    public List<Author> getAllAuthorsWithBooksBeforeYear1990() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate releaseDate = LocalDate.parse("01/01/1990", formatter);

        return this.bookRepository.findAllByReleaseDateBefore(releaseDate)
                .stream()
                .map(Book::getAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAllBooksBySpecificAuthor() {
        return this.bookRepository.findAllByAuthor();
    }

    @Override
    public List<Book> getAllBooksByAgeRestriction(String ageRestriction) {
        return this.bookRepository.findAllByAgeRestriction(AgeRestriction.valueOf(ageRestriction));
    }

    @Override
    public List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies) {
        return this.bookRepository.findAllByEditionTypeAndCopiesLessThan(
                EditionType.valueOf(editionType.toUpperCase()),
                copies);
    }

    @Override
    public List<Book> getAllBooksByPrice(BigDecimal lowerPrice, BigDecimal higherPrice) {
        return this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(lowerPrice, higherPrice);
    }

    @Override
    public List<Book> getAllBooksNotReleasedInGivenYear(int year) {
        LocalDate before = LocalDate.of(year, 1, 1);
        LocalDate after = LocalDate.of(year, 12, 31);
        return this.bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(before, after);
    }

    @Override
    public List<Book> getAllBooksBeforeGivenReleaseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate releaseDate = LocalDate.parse(date, formatter);

        return this.bookRepository.findAllByReleaseDateBefore(releaseDate);
    }

    @Override
    public List<Book> getAllBooksByTitleContainsString(String containLetters) {
        return this.bookRepository.findAllByTitleContains(containLetters);
    }

    @Override
    public int getCountBooksByLengthOfTitle(int length) {
        return this.bookRepository.findCountBooksWithTitleLongerThanGivenLength(length);
    }

    @Override
    public int getTotalCopiesByAuthor(String fullName) {
        return this.bookRepository.findAllCopiesByAuthor(fullName);
    }

    @Override
    public int updateBooksCopiesAfterDate(String date, int copies) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));

        return this.bookRepository.updateAllBooksAfterGivenDate(localDate, copies);
    }

    @Override
    public Book getBookByTitle(String title) {
        return this.bookRepository.findByTitle(title);
    }

    @Override
    public int removeBooksByCopiesLessThanGivenNumber(int copies) {
        return this.bookRepository.removeAllByCopiesLessThan(copies);
    }

//    @Override
//    public int getBooksCountByAuthor(String fullName) {
//        return this.bookRepository.callProcedureGetCountBooksByAuthor(fullName);
//    }
}
