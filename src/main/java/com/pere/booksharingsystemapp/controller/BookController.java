package com.pere.booksharingsystemapp.controller;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.service.BookServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LogManager.getLogger(BookController.class);

    private BookServiceIntf bookService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public BookController(BookServiceIntf bookService, JmsTemplate jmsTemplate) {
        this.bookService = bookService;
        this.jmsTemplate = jmsTemplate;
    }

   /* @Autowired
    public BookController(BookServiceIntf bookService) {
        this.bookService = bookService;
    }*/

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        logger.info("Fetching book by ID: {}", bookId);
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Fetching all books");
        System.out.println("**********IN CONTROLLER*****************");
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.info("Adding a new book: {}", book);
        Book addedBook = bookService.addBook(book);
        sendNotification("New book added: " + addedBook.getTitle());
        // kafkaTemplate.send("notification-topic", "New book added: " + addedBook.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book book) {
        logger.info("Updating book with ID: {}, New book details: {}", bookId, book);
        Book updatedBook = bookService.updateBook(bookId, book);
        sendNotification("Book updated: " + updatedBook.getTitle());
        //kafkaTemplate.send("notification-topic", "New book added: " + updatedBook.getTitle());
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        logger.info("Deleting book with ID: {}", bookId);
        bookService.deleteBook(bookId);
        sendNotification("Book deleted with ID: " + bookId);
        return ResponseEntity.noContent().build();
    }

    private void sendNotification(String message) {
        jmsTemplate.convertAndSend("notification-topic", message);
    }
}
