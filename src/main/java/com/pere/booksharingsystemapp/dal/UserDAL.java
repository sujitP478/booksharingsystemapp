package com.pere.booksharingsystemapp.dal;

import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.handler.RequestDeniedException;
import com.pere.booksharingsystemapp.repository.UserRepository;
import com.pere.booksharingsystemapp.service.UserServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDAL implements UserServiceIntf {

    private static final Logger logger = LogManager.getLogger(UserDAL.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User not found with ID: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");

        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        logger.info("Adding a new user: {}", user);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        logger.info("Updating user with ID: {}, New user details: {}", userId, updatedUser);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User not found with ID: " + userId));

        // Update the properties of the existing user with the values from updatedUser
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User not found with ID: " + userId));

        userRepository.delete(user);
    }
}

