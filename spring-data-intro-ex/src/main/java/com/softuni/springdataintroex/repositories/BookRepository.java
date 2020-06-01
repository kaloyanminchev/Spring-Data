package com.softuni.springdataintroex.repositories;

import com.softuni.springdataintroex.entities.AgeRestriction;
import com.softuni.springdataintroex.entities.Book;
import com.softuni.springdataintroex.entities.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByReleaseDateAfter(LocalDate localDate);

    List<Book> findAllByReleaseDateBefore(LocalDate localDate);

    @Query("SELECT b FROM Book AS b " +
            "WHERE CONCAT(b.author.firstName, ' ', b.author.lastName) = 'George Powell' " +
            "ORDER BY b.releaseDate DESC, b.title ASC")
    List<Book> findAllByAuthor();

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lowerPrice, BigDecimal higherPrice);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    List<Book> findAllByTitleContains(String contain);

    @Query("SELECT COUNT(b) FROM Book AS b WHERE LENGTH(b.title) > ?1")
    int findCountBooksWithTitleLongerThanGivenLength(int length);

    @Query("SELECT SUM(b.copies) FROM Book AS b " +
            "WHERE CONCAT(b.author.firstName, ' ', b.author.lastName) = :fullName")
    int findAllCopiesByAuthor(@Param("fullName") String fullName);

    @Modifying
    @Query("UPDATE Book AS b SET b.copies = b.copies + :copies WHERE b.releaseDate > :date")
    int updateAllBooksAfterGivenDate(@Param("date") LocalDate date, @Param("copies") int copies);

    Book findByTitle(String title);

    @Modifying
    int removeAllByCopiesLessThan(int copies);

//    @Procedure(procedureName = "udp_get_count_books_by_author")
//    int callProcedureGetCountBooksByAuthor(String fullName);
}
