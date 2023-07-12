package com.pere.booksharingsystemapp.controller;

import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.service.BookRequestServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/book-requests")
public class BookRequestController {

    private static final Logger logger = LogManager.getLogger(BookRequestController.class);
    private BookRequestServiceIntf bookRequestService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public BookRequestController(BookRequestServiceIntf bookRequestService, JmsTemplate jmsTemplate) {
        this.bookRequestService = bookRequestService;
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping("/book-request/{requestId}")
    public ResponseEntity<BookRequest> getRequestById(@PathVariable Long requestId) {
        logger.info("Fetching request by ID: {}", requestId);
        BookRequest request = bookRequestService.getRequestById(requestId);
        //sendNotification("Fetching request by ID: {}" + request.getBookRequestId());
        return ResponseEntity.ok(request);
    }

    @PostMapping("/make-request")
    public ResponseEntity<BookRequest> makeRequest(@RequestParam Long bookId, @RequestParam Long userId) {
        logger.info("Making a new request - Book ID: {}, User ID: {}", bookId, userId);
        BookRequest request = bookRequestService.makeRequest(bookId, userId);
        sendNotification("New book request: " + request.getBookRequestId());
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{requestId}/update-status")
    public ResponseEntity<Void> updateRequestStatus(@PathVariable Long requestId, @RequestParam RequestStatus newStatus) {
        logger.info("Updating request status - Request ID: {}, New Status: {}", requestId, newStatus);
        bookRequestService.updateRequestStatus(requestId, newStatus);
        sendNotification("Request ID " + requestId + " status updated to " + newStatus);
        return ResponseEntity.ok().build();
    }

    private void sendNotification(String message) {
        jmsTemplate.convertAndSend("notification-topic", message);
    }

/*    @GetMapping("/by-book/{bookId}")
    public ResponseEntity<List<BookRequest>> getRequestsByBookId(@PathVariable Long bookId) {
        List<BookRequest> requests = bookRequestService.getRequestsByBookId(bookId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<BookRequest>> getRequestsByUserId(@PathVariable Long userId) {
        List<BookRequest> requests = bookRequestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(requests);
    }*/
}
