package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.dal.BookDAL;
import com.pere.booksharingsystemapp.dal.UserDAL;
import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.BookStatus;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService implements BookServiceIntf {


    private BookDAL bookDAL;
    private UserDAL userDAL;

    @Autowired
    public BookService(BookDAL bookDAL, UserDAL userDAL) {
        this.bookDAL = bookDAL;
        this.userDAL = userDAL;
    }

  /*  @Autowired
    public BookService(BookDAL bookRepository, BookRequestDAL bookRequestRepository, UserDAL userService) {
        this.bookRepository = bookRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.userService = userService;
    }*/

    public List<Book> getAllBooks() {
        return bookDAL.getAllBooks();
    }

    public Book getBookById(Long id) {
        return bookDAL.getBookById(id);
    }

    public Book makeRequest(Long bookId, Long userId) {
        Book book = getBookById(bookId);
        User requester = userDAL.getUserById(userId);

        if (book.getStatus() == BookStatus.OUT) {
            throw new RequestDeniedException("The book is currently unavailable.");
        }

        book.setStatus(BookStatus.REQUESTED);
        book.setRequester(requester);
        bookDAL.addBook(book);

        return book;
    }

    public Book addBook(Book book) {
        validateBook(book);
        return bookDAL.addBook(book);
    }

    public void deleteBook(Long id) {
        bookDAL.deleteBook(id);
    }

    public Book updateBook(Long bookId, Book updatedBook) {
        Book book = getBookById(bookId);
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        return bookDAL.addBook(book);
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new RequestDeniedException("Book title is required.");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new RequestDeniedException("Book author is required.");
        }
    }
}
