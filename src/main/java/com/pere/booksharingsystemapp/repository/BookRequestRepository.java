package com.pere.booksharingsystemapp.repository;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    List<BookRequest> findByBookId(Long bookId);

    List<BookRequest> findByUserId(Long userId);

    BookRequest findByBookAndUser(Book book, User user);

}
