package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.entity.Book;

import java.util.List;

public interface BookServiceIntf {
     List<Book> getAllBooks();

     Book getBookById(Long id);

     Book addBook(Book book);

     Book makeRequest(Long bookId, Long userId);

     void deleteBook(Long id);

     Book updateBook(Long bookId, Book updatedBook);
}
