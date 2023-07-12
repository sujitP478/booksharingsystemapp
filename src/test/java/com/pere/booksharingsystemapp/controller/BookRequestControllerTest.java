package com.pere.booksharingsystemapp.controller;

import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.service.BookRequestServiceIntf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookRequestControllerTest {
    @Mock
    private BookRequestServiceIntf bookRequestService;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private BookRequestController bookRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRequestById() {
        Long requestId = 1L;
        BookRequest request = new BookRequest();
        request.setBookRequestId(requestId);
        request.setStatus(RequestStatus.PENDING);

        when(bookRequestService.getRequestById(requestId)).thenReturn(request);

        ResponseEntity<BookRequest> response = bookRequestController.getRequestById(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());

        verify(bookRequestService, times(1)).getRequestById(requestId);
    }

    @Test
    void testMakeRequest() {
        Long bookId = 1L;
        Long userId = 1L;
        BookRequest request = new BookRequest();
        request.setBookRequestId(1L);
        request.setBookRequestId(bookId);
        request.setBookRequestId(userId);
        request.setStatus(RequestStatus.PENDING);

        when(bookRequestService.makeRequest(eq(bookId), eq(userId))).thenReturn(request);

        ResponseEntity<BookRequest> response = bookRequestController.makeRequest(bookId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());

        verify(bookRequestService, times(1)).makeRequest(eq(bookId), eq(userId));
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), anyString());
    }

    @Test
    void testUpdateRequestStatus() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;

        ResponseEntity<Void> response = bookRequestController.updateRequestStatus(requestId, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookRequestService, times(1)).updateRequestStatus(eq(requestId), eq(newStatus));
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), anyString());
    }
}
