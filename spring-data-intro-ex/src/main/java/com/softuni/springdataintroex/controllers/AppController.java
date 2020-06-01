package com.softuni.springdataintroex.controllers;

import com.softuni.springdataintroex.entities.Author;
import com.softuni.springdataintroex.entities.Book;
import com.softuni.springdataintroex.services.AuthorService;
import com.softuni.springdataintroex.services.BookService;
import com.softuni.springdataintroex.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.math.BigDecimal;

@Controller
public class AppController implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader reader;

    @Autowired
    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader reader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.reader = reader;
    }

    @Override
    public void run(String... args) throws Exception {
        // seed data
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        System.out.println("ENTER TASK NUMBER:");
        int taskNumber = Integer.parseInt(reader.readLine());

        /*
        ============================EXERCISE: SPRING DATA INTRO=======================================
        switch (taskNumber) {
            case 1:
                this.bookService.getAllBooksAfterYear2000()
                        .forEach(b -> System.out.println(b.getTitle()));
                break;
            case 2:
                this.bookService.getAllAuthorsWithBooksBeforeYear1990()
                        .forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
                break;
            case 3:
                this.authorService.findAllAuthorsByCountOfBooks()
                        .forEach(a -> System.out.printf("%s %s - %d%n",
                                a.getFirstName(),
                                a.getLastName(),
                                a.getBooks().size())
                        );
                break;
            case 4:
                this.bookService.findAllBooksBySpecificAuthor()
                        .forEach(b -> System.out.printf("%s %s %d%n",
                                b.getTitle(),
                                b.getReleaseDate(),
                                b.getCopies())
                        );
                break;
        }
        ==============================================================================================
         */

//      ========================EXERCISE: SPRING DATA ADVANCED QUERYING===============================

        switch (taskNumber) {
            case 1:
                System.out.println("Enter age restriction:");
                String ageRestriction = this.reader.readLine().toUpperCase();

                this.bookService
                        .getAllBooksByAgeRestriction(ageRestriction)
                        .forEach(book -> System.out.println(book.getTitle()));
                break;
            case 2:
                this.bookService.getAllBooksByEditionTypeAndCopies("gold", 5000)
                        .stream()
                        .map(Book::getTitle)
                        .forEach(System.out::println);
                break;
            case 3:
                this.bookService.getAllBooksByPrice(new BigDecimal("5"), new BigDecimal("40"))
                        .forEach(b -> System.out.printf("%s - $%.2f%n", b.getTitle(), b.getPrice()));
                break;
            case 4:
                System.out.println("Enter release year:");
                this.bookService
                        .getAllBooksNotReleasedInGivenYear(Integer.parseInt(this.reader.readLine()))
                        .forEach(b -> System.out.println(b.getTitle()));
                break;
            case 5:
                System.out.println("Enter release date:");
                this.bookService.getAllBooksBeforeGivenReleaseDate(this.reader.readLine())
                        .forEach(b -> System.out.printf("%s %s %.2f%n",
                                b.getTitle(),
                                b.getEditionType(),
                                b.getPrice())
                        );
                break;
            case 6:
                System.out.println("Enter ending letters:");
                this.authorService.getAuthorsByFirstNameEndsWith(this.reader.readLine())
                        .forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
                break;
            case 7:
                System.out.println("Enter contain letters:");
                this.bookService.getAllBooksByTitleContainsString(this.reader.readLine())
                        .forEach(b -> System.out.println(b.getTitle()));
                break;
            case 8:
                System.out.println("Enter starting letters of author's last name:");
                this.authorService.getAuthorsByLastNameStartsWith(this.reader.readLine())
                        .forEach(author -> author.getBooks()
                                .forEach(b -> System.out.printf("%s (%s %s)%n",
                                        b.getTitle(),
                                        author.getFirstName(),
                                        author.getLastName())
                                ));
                break;
            case 9:
                System.out.println("Enter length of title:");
                int length = Integer.parseInt(this.reader.readLine());
                System.out.println(String.format("There are %d books with longer title than %d symbols",
                        this.bookService.getCountBooksByLengthOfTitle(length), length));
                break;
            case 10:
                System.out.println("Enter author's full name:");
                System.out.println(this.bookService.getTotalCopiesByAuthor(this.reader.readLine()));
                break;
            case 11:
                System.out.println("Enter title:");
                Book book = this.bookService.getBookByTitle(this.reader.readLine());
                System.out.printf("%s %s %s %.2f%n",
                        book.getTitle(),
                        book.getEditionType(),
                        book.getAgeRestriction(),
                        book.getPrice()
                );
                break;
            case 12:
                System.out.println("Enter date and copies:");
                String date = this.reader.readLine();
                int copies = Integer.parseInt(this.reader.readLine());
                int totalCopies = this.bookService.updateBooksCopiesAfterDate(date, copies) * copies;

                System.out.println(totalCopies);
                break;
            case 13:
                System.out.println("Enter number of copies:");
                int removedBooks = this.bookService
                        .removeBooksByCopiesLessThanGivenNumber(Integer.parseInt(this.reader.readLine()));
                System.out.println(removedBooks);
                break;
//            case 14:
//                System.out.println("Enter author's full name:");
//                String fullName = this.reader.readLine();
//                int booksCountByAuthor = this.bookService.getBooksCountByAuthor(fullName);
//                System.out.printf("%s has written %d books", fullName, booksCountByAuthor);
//                break;
        }

//      ==============================================================================================
    }
}
