package com.ftn.activityapp.service;

import com.ftn.activityapp.dto.user.*;
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

    private final FileStorageService fileStorageService;


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
                .goalSteps(request.getGoalSteps())
                .profileImageUrl(null)
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
                .goalSteps(user.getGoalSteps())
                .profileImageUrl(user.getProfileImageUrl())
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
                .goalSteps(user.getGoalSteps())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }


    public UserResponse uploadProfileImage(Long userId, org.springframework.web.multipart.MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String imageUrl = fileStorageService.saveProfileImage(file);
        user.setProfileImageUrl(imageUrl);

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
                .goalSteps(savedUser.getGoalSteps())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .build();
    }


    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BadRequestException("User with this email already exists.");
            }
            user.setEmail(request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setActivityLevel(request.getActivityLevel());
        user.setGoal(request.getGoal());
        user.setGoalSteps(request.getGoalSteps());

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
                .goalSteps(savedUser.getGoalSteps())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .build();
    }


    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new BadRequestException("Current password is required.");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new BadRequestException("New password is required.");
        }

        if (!user.getPassword().equals(request.getCurrentPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }

        if (request.getNewPassword().length() < 6) {
            throw new BadRequestException("New password must have at least 6 characters.");
        }

        if (request.getNewPassword().equals(user.getPassword())) {
            throw new BadRequestException("New password must be different from the current password.");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}