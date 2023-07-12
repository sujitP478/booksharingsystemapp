package com.pere.booksharingsystemapp.dal;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.repository.BookRepository;
import com.pere.booksharingsystemapp.repository.BookRequestRepository;
import com.pere.booksharingsystemapp.repository.UserRepository;
import com.pere.booksharingsystemapp.service.BookServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookDAL implements BookServiceIntf {

    private static final Logger logger = LogManager.getLogger(BookDAL.class);

    private BookRepository bookRepository;
    private BookRequestRepository bookRequestRepository;
    private UserRepository userRepository;

    @Autowired
    public BookDAL(BookRepository bookRepository, BookRequestRepository bookRequestRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        logger.info("Fetching book by ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new RequestDeniedException("Book does not exist with ID: " + id));
    }

    @Override
    public Book addBook(Book book) {
        logger.info("Adding a new book: {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Book makeRequest(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RequestDeniedException("Book does not exist with ID: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User does not exist with ID: " + userId));

        // Create a book request object and set its properties
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBook(book);
        bookRequest.setUser(user);
        bookRequest.setStatus(RequestStatus.PENDING); // Set the initial status as PENDING
        bookRequest.setRequestedDate(LocalDateTime.now()); // Set the current date and time

        // Save the book request object using the appropriate repository or service
        bookRequestRepository.save(bookRequest);

        return bookRequest.getBook();
    }

    @Override
    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Long bookId, Book updatedBook) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RequestDeniedException("Book does not exist with ID: " + bookId));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCopies_available(updatedBook.getCopies_available());

        return bookRepository.save(existingBook);
    }
}
