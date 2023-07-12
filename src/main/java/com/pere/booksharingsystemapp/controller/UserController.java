package com.pere.booksharingsystemapp.controller;

import com.pere.booksharingsystemapp.entity.User;
import com.pere.booksharingsystemapp.service.UserServiceIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private UserServiceIntf userService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public UserController(UserServiceIntf userService, JmsTemplate jmsTemplate) {
        this.userService = userService;
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        logger.info("Fetching user by ID: {}", userId);
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.info("Adding a new user: {}", user);
        User addedUser = userService.addUser(user);
        sendNotification("New user added: " + addedUser.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        logger.info("Updating user with ID: {}, New user details: {}", userId, user);
        User updatedUser = userService.updateUser(userId, user);
        sendNotification("User updated: " + updatedUser.getName());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        sendNotification("User deleted with ID: " + userId);
        return ResponseEntity.noContent().build();
    }

    private void sendNotification(String message) {
        jmsTemplate.convertAndSend("notification-topic", message);
    }
}
