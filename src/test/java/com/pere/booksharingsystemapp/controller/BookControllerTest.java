package com.pere.booksharingsystemapp.controller;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.service.BookServiceIntf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookServiceIntf bookService;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookById() {
        long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Sample Book");

        when(bookService.getBookById(bookId)).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());

        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("New Book");

        Book addedBook = new Book();
        addedBook.setId(1L);
        addedBook.setTitle("New Book");

        when(bookService.addBook(book)).thenReturn(addedBook);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(addedBook, response.getBody());

        verify(bookService, times(1)).addBook(book);
        verify(jmsTemplate, times(1)).convertAndSend("notification-topic", "New book added: " + addedBook.getTitle());
    }

    @Test
    void testUpdateBook() {
        long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Book");

        when(bookService.updateBook(bookId, updatedBook)).thenReturn(updatedBook);

        ResponseEntity<Book> response = bookController.updateBook(bookId, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());

        verify(bookService, times(1)).updateBook(bookId, updatedBook);
        verify(jmsTemplate, times(1)).convertAndSend("notification-topic", "Book updated: " + updatedBook.getTitle());
    }

    @Test
    void testDeleteBook() {
        long bookId = 1L;

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookService, times(1)).deleteBook(bookId);
        verify(jmsTemplate, times(1)).convertAndSend("notification-topic", "Book deleted with ID: " + bookId);
    }
}
