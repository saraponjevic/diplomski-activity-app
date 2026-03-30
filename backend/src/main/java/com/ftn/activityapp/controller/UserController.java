package com.ftn.activityapp.controller;

import com.ftn.activityapp.dto.user.LoginUserRequest;
import com.ftn.activityapp.dto.user.RegisterUserRequest;
import com.ftn.activityapp.dto.user.UserResponse;
import com.ftn.activityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody LoginUserRequest request) {
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
