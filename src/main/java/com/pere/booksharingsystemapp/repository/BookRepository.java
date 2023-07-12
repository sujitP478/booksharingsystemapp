package com.pere.booksharingsystemapp.repository;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.handler.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatus(BookStatus status);

}
