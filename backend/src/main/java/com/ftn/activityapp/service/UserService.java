package com.ftn.activityapp.service;

import com.ftn.activityapp.dto.user.LoginUserRequest;
import com.ftn.activityapp.dto.user.RegisterUserRequest;
import com.ftn.activityapp.dto.user.UserResponse;
import com.ftn.activityapp.exception.BadRequestException;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    // proverava da li email vec postoji
    // ako postoji baca gresku
    // pravi User objekat iz request-a
    // cuva ga u bazi
    // vraca UserResponse
    public UserResponse registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User with this email already exists.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .height(request.getHeight())
                .weight(request.getWeight())
                .activityLevel(request.getActivityLevel())
                .goal(request.getGoal())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .age(savedUser.getAge())
                .height(savedUser.getHeight())
                .weight(savedUser.getWeight())
                .activityLevel(savedUser.getActivityLevel())
                .goal(savedUser.getGoal())
                .build();
    }


    // trazi korisnika pod IDu
    // ako ne postoji baca gresku
    // ako postoji, mapira ga u response DTO
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .activityLevel(user.getActivityLevel())
                .goal(user.getGoal())
                .build();
    }

    public UserResponse loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid password.");
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .activityLevel(user.getActivityLevel())
                .goal(user.getGoal())
                .build();
    }
}