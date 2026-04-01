package com.example.activityapp.data.remote

import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.CreateActivityRequest
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.remote.dto.planner.PlannerTaskCreateRequestDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskResponseDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskUpdateRequestDto
import com.example.activityapp.data.remote.dto.user.ChangePasswordRequest
import com.example.activityapp.data.remote.dto.user.RegisterUserRequest
import com.example.activityapp.data.remote.dto.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.activityapp.data.remote.dto.user.LoginUserRequest
import com.example.activityapp.data.remote.dto.user.UpdateUserRequest
import com.example.activityapp.data.remote.dto.wellness.WellnessDetailsResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessMoodRequest
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

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

    @Multipart
    @POST("api/users/{userId}/profile-image")
    suspend fun uploadProfileImage(
        @Path("userId") userId: Long,
        @Part file: MultipartBody.Part
    ): UserResponse

    @PUT("api/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Long,
        @Body request: UpdateUserRequest
    ): UserResponse

    @PUT("api/users/{userId}/change-password")
    suspend fun changePassword(
        @Path("userId") userId: Long,
        @Body request: ChangePasswordRequest
    )

    @POST("api/planner/users/{userId}/tasks")
    suspend fun createPlannerTask(
        @Path("userId") userId: Long,
        @Body request: PlannerTaskCreateRequestDto
    ): PlannerTaskResponseDto

    @GET("api/planner/users/{userId}/tasks/today")
    suspend fun getTodayPlannerTasks(
        @Path("userId") userId: Long
    ): List<PlannerTaskResponseDto>

    @GET("api/planner/users/{userId}/tasks/by-date")
    suspend fun getPlannerTasksForDate(
        @Path("userId") userId: Long,
        @Query("date") date: String
    ): List<PlannerTaskResponseDto>

    @GET("api/planner/users/{userId}/tasks/week")
    suspend fun getPlannerTasksForWeek(
        @Path("userId") userId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<PlannerTaskResponseDto>

    @PUT("api/planner/tasks/{taskId}")
    suspend fun updatePlannerTask(
        @Path("taskId") taskId: Long,
        @Body request: PlannerTaskUpdateRequestDto
    ): PlannerTaskResponseDto

    @PATCH("api/planner/tasks/{taskId}/completed")
    suspend fun updatePlannerTaskCompleted(
        @Path("taskId") taskId: Long,
        @Query("completed") completed: Boolean
    ): PlannerTaskResponseDto

    @DELETE("api/planner/tasks/{taskId}")
    suspend fun deletePlannerTask(
        @Path("taskId") taskId: Long
    )

}