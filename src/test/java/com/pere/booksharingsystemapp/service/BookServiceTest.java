package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.dal.BookDAL;
import com.pere.booksharingsystemapp.dal.UserDAL;
import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.BookStatus;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookDAL bookDAL;

    @Mock
    private UserDAL userDAL;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        books.add(book1);

        Book book2 = new Book();
        book2.setId(1L);
        book2.setTitle("Book 1");
        book2.setAuthor("Author 1");
        books.add(book2);

        when(bookDAL.getAllBooks()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());

        verify(bookDAL, times(1)).getAllBooks();
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");

        when(bookDAL.getBookById(1L)).thenReturn(book);

        Book result = bookService.getBookById(1L);

        assertEquals("Book 1", result.getTitle());
        assertEquals("Author 1", result.getAuthor());

        verify(bookDAL, times(1)).getBookById(1L);
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testMakeRequest() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");

        User user = new User();
        user.setId(1L);
        user.setName("John");

        when(bookDAL.getBookById(1L)).thenReturn(book);
        when(userDAL.getUserById(1L)).thenReturn(user);
        when(bookDAL.addBook(book)).thenReturn(book);

        Book result = bookService.makeRequest(1L, 1L);

        assertEquals(BookStatus.REQUESTED, result.getStatus());
        assertEquals(user, result.getRequester());

        verify(bookDAL, times(1)).getBookById(1L);
        verify(userDAL, times(1)).getUserById(1L);
        verify(bookDAL, times(1)).addBook(book);
        verifyNoMoreInteractions(bookDAL, userDAL);
    }

    @Test
    void testMakeRequestUnavailableBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");

        book.setStatus(BookStatus.OUT);

        when(bookDAL.getBookById(1L)).thenReturn(book);

        assertThrows(RequestDeniedException.class, () -> {
            bookService.makeRequest(1L, 1L);
        });

        verify(bookDAL, times(1)).getBookById(1L);
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");
        when(bookDAL.addBook(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertEquals(book, result);

        verify(bookDAL, times(1)).addBook(book);
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1L);

        verify(bookDAL, times(1)).deleteBook(1L);
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("");

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        when(bookDAL.getBookById(1L)).thenReturn(book);
        when(bookDAL.addBook(book)).thenReturn(book);

        Book result = bookService.updateBook(1L, updatedBook);

        assertEquals(updatedBook.getTitle(), result.getTitle());
        assertEquals(updatedBook.getAuthor(), result.getAuthor());

        verify(bookDAL, times(1)).getBookById(1L);
        verify(bookDAL, times(1)).addBook(book);
        verifyNoMoreInteractions(bookDAL);
    }

    @Test
    void testValidateBookWithTitleEmpty() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("");

        assertThrows(RequestDeniedException.class, () -> {
            bookService.addBook(book);
        });
    }

    @Test
    void testValidateBookWithAuthorEmpty() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("");
        assertThrows(RequestDeniedException.class, () -> {
            bookService.addBook(book);
        });
    }
}
