package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.entity.BookRequest;
import com.pere.booksharingsystemapp.handler.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface BookRequestServiceIntf {

     BookRequest getRequestById(Long requestId);

     BookRequest makeRequest(Long bookId, Long userId);

     void updateRequestStatus(Long requestId, RequestStatus newStatus);

     List<BookRequest> getRequestsByBookId(Long bookId);

     List<BookRequest> getRequestsByUserId(Long userId);
}
