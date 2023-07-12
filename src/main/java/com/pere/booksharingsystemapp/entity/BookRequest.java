package com.pere.booksharingsystemapp.entity;

import com.pere.booksharingsystemapp.handler.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_requests")
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false)
    private LocalDateTime requestedDate;

    public Long getBookRequestId() {
        return bookRequestId;
    }

    public void setBookRequestId(Long bookRequestId) {
        this.bookRequestId = bookRequestId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public BookRequest(Long bookRequestId, Book book, User user, RequestStatus status, LocalDateTime requestedDate) {
        this.bookRequestId = bookRequestId;
        this.book = book;
        this.user = user;
        this.status = status;
        this.requestedDate = requestedDate;
    }

    public BookRequest() {
    }

}
