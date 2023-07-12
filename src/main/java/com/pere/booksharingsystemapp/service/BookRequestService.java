package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.entity.Book;
import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.BookStatus;
import com.pere.booksharingsystemapp.handler.NotFoundException;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import com.pere.booksharingsystemapp.handler.RequestStatus;
import com.pere.booksharingsystemapp.repository.BookRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookRequestService implements BookRequestServiceIntf{

    @Autowired
    BookRequestRepository bookRequestRepository;

    @Autowired
    BookServiceIntf bookService;

    @Autowired
    UserServiceIntf userService;

    public BookRequest getRequestById(Long requestId) {
        return bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Book request not found with ID: " + requestId));
    }

    public BookRequest makeRequest(Long bookId, Long userId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        if (book.getStatus() == BookStatus.OUT) {
            throw new RequestDeniedException("The book is currently unavailable.");
        }

        if (bookRequestRepository.findByBookAndUser(book, user) != null) {
            throw new RequestDeniedException("The user has already requested this book.");
        }

        BookRequest request = new BookRequest();
        request.setBook(book);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        request.setRequestedDate(LocalDateTime.now());

        return bookRequestRepository.save(request);
    }


    public void updateRequestStatus(Long requestId, RequestStatus newStatus) {
        BookRequest request = getRequestById(requestId);
        request.setStatus(newStatus);
        bookRequestRepository.save(request);
    }

    public List<BookRequest> getRequestsByBookId(Long bookId) {
        return bookRequestRepository.findByBookId(bookId);
    }

    public List<BookRequest> getRequestsByUserId(Long userId) {
        return bookRequestRepository.findByUserId(userId);
    }
}
