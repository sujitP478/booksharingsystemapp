package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.BookStatus;
import com.pere.booksharingsystemapp.handler.NotFoundException;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.repository.BookRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookRequestServiceTest {
    @Mock
    private BookRequestRepository bookRequestRepository;

    @Mock
    private BookServiceIntf bookService;

    @Mock
    private UserServiceIntf userService;

    @InjectMocks
    private BookRequestService bookRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRequestById() {
        Long requestId = 1L;
        BookRequest request = new BookRequest();
        request.setBookRequestId(requestId);

        when(bookRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        BookRequest result = bookRequestService.getRequestById(requestId);

        assertEquals(request, result);

        verify(bookRequestRepository, times(1)).findById(requestId);
    }

    @Test
    void testGetRequestByIdNotFound() {
        Long requestId = 1L;

        when(bookRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookRequestService.getRequestById(requestId));

        verify(bookRequestRepository, times(1)).findById(requestId);
    }

    @Test
    void testMakeRequest() {
        Long bookId = 1L;
        Long userId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.AVAILABLE);
        User user = new User();
        user.setId(userId);
        BookRequest request = new BookRequest();
        request.setBook(book);
        request.setUser(user);

        when(bookService.getBookById(bookId)).thenReturn(book);
        when(userService.getUserById(userId)).thenReturn(user);
        when(bookRequestRepository.findByBookAndUser(book, user)).thenReturn(null);
        when(bookRequestRepository.save(any(BookRequest.class))).thenReturn(request);

        BookRequest result = bookRequestService.makeRequest(bookId, userId);

        assertEquals(request, result);

        verify(bookService, times(1)).getBookById(bookId);
        verify(userService, times(1)).getUserById(userId);
        verify(bookRequestRepository, times(1)).findByBookAndUser(book, user);
        verify(bookRequestRepository, times(1)).save(any(BookRequest.class));
    }

    @Test
    void testMakeRequestBookUnavailable() {
        Long bookId = 1L;
        Long userId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.OUT);
        User user = new User();
        user.setId(userId);

        when(bookService.getBookById(bookId)).thenReturn(book);

        assertThrows(RequestDeniedException.class, () -> bookRequestService.makeRequest(bookId, userId));

        verify(bookService, times(1)).getBookById(bookId);
        verifyNoMoreInteractions(userService, bookRequestRepository);
    }

    @Test
    void testMakeRequestUserAlreadyRequested() {
        Long bookId = 1L;
        Long userId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.AVAILABLE);
        User user = new User();
        user.setId(userId);
        BookRequest existingRequest = new BookRequest();

        when(bookService.getBookById(bookId)).thenReturn(book);
        when(userService.getUserById(userId)).thenReturn(user);
        when(bookRequestRepository.findByBookAndUser(book, user)).thenReturn(existingRequest);

        assertThrows(RequestDeniedException.class, () -> bookRequestService.makeRequest(bookId, userId));

        verify(bookService, times(1)).getBookById(bookId);
        verify(userService, times(1)).getUserById(userId);
        verify(bookRequestRepository, times(1)).findByBookAndUser(book, user);
        verifyNoMoreInteractions(bookRequestRepository);
    }

    @Test
    void testUpdateRequestStatus() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;
        BookRequest request = new BookRequest();
        request.setBookRequestId(requestId);

        when(bookRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(bookRequestRepository.save(any(BookRequest.class))).thenReturn(request);

        bookRequestService.updateRequestStatus(requestId, newStatus);

        assertEquals(newStatus, request.getStatus());

        verify(bookRequestRepository, times(1)).findById(requestId);
        verify(bookRequestRepository, times(1)).save(any(BookRequest.class));
    }

    @Test
    void testUpdateRequestStatusNotFound() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;

        when(bookRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookRequestService.updateRequestStatus(requestId, newStatus));

        verify(bookRequestRepository, times(1)).findById(requestId);
        verifyNoMoreInteractions(bookRequestRepository);
    }

    @Test
    void testGetRequestsByBookId() {
        Long bookId = 1L;
        List<BookRequest> requests = new ArrayList<>();
        requests.add(new BookRequest());
        requests.add(new BookRequest());

        when(bookRequestRepository.findByBookId(bookId)).thenReturn(requests);

        List<BookRequest> result = bookRequestService.getRequestsByBookId(bookId);

        assertEquals(requests, result);

        verify(bookRequestRepository, times(1)).findByBookId(bookId);
    }

    @Test
    void testGetRequestsByUserId() {
        Long userId = 1L;
        List<BookRequest> requests = new ArrayList<>();
        requests.add(new BookRequest());
        requests.add(new BookRequest());

        when(bookRequestRepository.findByUserId(userId)).thenReturn(requests);

        List<BookRequest> result = bookRequestService.getRequestsByUserId(userId);

        assertEquals(requests, result);

        verify(bookRequestRepository, times(1)).findByUserId(userId);
    }
}
