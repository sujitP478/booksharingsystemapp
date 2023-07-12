package com.pere.booksharingsystemapp.dal;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.repository.BookRequestRepository;
import com.pere.booksharingsystemapp.repository.BookRepository;
import com.pere.booksharingsystemapp.repository.UserRepository;
import com.pere.booksharingsystemapp.service.BookRequestServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookRequestDAL implements BookRequestServiceIntf {

    private static final Logger logger = LogManager.getLogger(BookRequestDAL.class);

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BookRequest getRequestById(Long requestId) {
        logger.info("Fetching book request by ID: {}", requestId);
        return bookRequestRepository.findById(requestId).orElse(null);
    }

    @Override
    public BookRequest makeRequest(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RequestDeniedException("Book does not exist with ID: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User does not exist with ID: " + userId));

        BookRequest bookRequest = new BookRequest();
        bookRequest.setBook(book);
        bookRequest.setUser(user);
        bookRequest.setStatus(RequestStatus.PENDING);
        bookRequest.setRequestedDate(LocalDateTime.now());

        logger.info("Creating book request: {}", bookRequest);

        return bookRequestRepository.save(bookRequest);
    }

    @Override
    public void updateRequestStatus(Long requestId, RequestStatus newStatus) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestDeniedException("Book request does not exist with ID: " + requestId));

        logger.info("Updating request status. Request ID: {}, New status: {}", requestId, newStatus);

        bookRequest.setStatus(newStatus);
        bookRequestRepository.save(bookRequest);
    }

    @Override
    public List<BookRequest> getRequestsByBookId(Long bookId) {
        logger.info("Fetching book requests by book ID: {}", bookId);

        return bookRequestRepository.findByBookId(bookId);
    }

    @Override
    public List<BookRequest> getRequestsByUserId(Long userId) {
        logger.info("Fetching book requests by user ID: {}", userId);

        return bookRequestRepository.findByUserId(userId);
    }
}
