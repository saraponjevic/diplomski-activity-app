package com.ftn.activityapp.service;

import com.ftn.activityapp.dto.ActivityResponse;
import com.ftn.activityapp.dto.CreateActivityRequest;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.model.Activity;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.repository.ActivityRepository;
import com.ftn.activityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    /*
    nađe korisnika po userId
    ako ne postoji → greška
    napravi Activity
    sačuva je u bazu
    vrati ActivityResponse
    * */
    public ActivityResponse createActivity(CreateActivityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Activity activity = Activity.builder()
                .user(user)
                .date(request.getDate())
                .steps(request.getSteps())
                .goalSteps(request.getGoalSteps())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return ActivityResponse.builder()
                .id(savedActivity.getId())
                .userId(savedActivity.getUser().getId())
                .date(savedActivity.getDate())
                .steps(savedActivity.getSteps())
                .goalSteps(savedActivity.getGoalSteps())
                .build();
    }


    //Uzima sve aktivnosti tog korisnika i mapira ih u DTO listu.
    public List<ActivityResponse> getActivitiesByUser(Long userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        return activities.stream()
                .map(activity -> ActivityResponse.builder()
                        .id(activity.getId())
                        .userId(activity.getUser().getId())
                        .date(activity.getDate())
                        .steps(activity.getSteps())
                        .goalSteps(activity.getGoalSteps())
                        .build())
                .toList();
    }
}
