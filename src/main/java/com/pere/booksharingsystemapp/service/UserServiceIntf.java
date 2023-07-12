package com.pere.booksharingsystemapp.service;

import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.NotFoundException;
import com.pere.booksharingsystemapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface UserServiceIntf {
     User getUserById(Long userId);

     List<User> getAllUsers();

     User addUser(User user);

     User updateUser(Long userId, User updatedUser);

     void deleteUser(Long userId);
}
