package com.example.activityapp.data.remote

import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.CreateActivityRequest
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.remote.dto.RegisterUserRequest
import com.example.activityapp.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.activityapp.data.remote.dto.LoginUserRequest
import com.example.activityapp.data.remote.dto.wellness.WellnessDetailsResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessMoodRequest

interface ApiService {

    @POST("api/users/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): UserResponse

    @GET("api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): UserResponse

    @POST("api/activities")
    suspend fun createActivity(
        @Body request: CreateActivityRequest
    ): ActivityResponse

    @GET("api/activities/user/{userId}")
    suspend fun getActivitiesByUser(
        @Path("userId") userId: Long
    ): List<ActivityResponse>

    @POST("api/recommendations/generate/{userId}")
    suspend fun generateRecommendation(
        @Path("userId") userId: Long
    ): RecommendationResponse

    @GET("api/recommendations/user/{userId}/latest")
    suspend fun getLatestRecommendation(
        @Path("userId") userId: Long
    ): RecommendationResponse

    @POST("api/users/login")
    suspend fun loginUser(
        @Body request: LoginUserRequest
    ): UserResponse

    @GET("api/activities/user/{userId}/latest")
    suspend fun getLatestActivity(
        @Path("userId") userId: Long
    ): ActivityResponse


    @POST("api/wellness/{userId}/mood")
    suspend fun saveMoodAndGetWellness(
        @Path("userId") userId: Long,
        @Body request: WellnessMoodRequest
    ): WellnessDetailsResponse

    @GET("api/wellness/{userId}/today")
    suspend fun getTodayWellness(
        @Path("userId") userId: Long
    ): WellnessDetailsResponse
}