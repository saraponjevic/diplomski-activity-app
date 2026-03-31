package com.example.activityapp.data.repository

import com.example.activityapp.data.remote.RetrofitInstance
import com.example.activityapp.data.remote.RetrofitInstance.api
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.CreateActivityRequest
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.remote.dto.user.ChangePasswordRequest
import com.example.activityapp.data.remote.dto.user.RegisterUserRequest
import com.example.activityapp.data.remote.dto.user.UserResponse
import com.example.activityapp.data.remote.dto.user.LoginUserRequest
import com.example.activityapp.data.remote.dto.user.UpdateUserRequest
import okhttp3.MultipartBody

class ActivityRepository {

    suspend fun registerUser(request: RegisterUserRequest): UserResponse {
        return RetrofitInstance.api.registerUser(request)
    }

    suspend fun getUserById(id: Long): UserResponse {
        return RetrofitInstance.api.getUserById(id)
    }

    suspend fun createActivity(request: CreateActivityRequest): ActivityResponse {
        return RetrofitInstance.api.createActivity(request)
    }

    suspend fun getActivitiesByUser(userId: Long): List<ActivityResponse> {
        return RetrofitInstance.api.getActivitiesByUser(userId)
    }

    suspend fun generateRecommendation(userId: Long): RecommendationResponse {
        return RetrofitInstance.api.generateRecommendation(userId)
    }

    suspend fun getLatestRecommendation(userId: Long): RecommendationResponse {
        return RetrofitInstance.api.getLatestRecommendation(userId)
    }

    suspend fun loginUser(request: LoginUserRequest): UserResponse {
        return RetrofitInstance.api.loginUser(request)
    }

    suspend fun getLatestActivity(userId: Long): ActivityResponse {
        return RetrofitInstance.api.getLatestActivity(userId)
    }
    suspend fun uploadProfileImage(userId: Long, imagePart: MultipartBody.Part): UserResponse {
        return api.uploadProfileImage(userId, imagePart)
    }

    suspend fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse {
        return api.updateUser(userId, request)
    }

    suspend fun changePassword(userId: Long, request: ChangePasswordRequest) {
        api.changePassword(userId, request)
    }

}