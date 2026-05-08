
package com.example.activityapp.data.repository
import android.content.Context
import com.example.activityapp.data.remote.RetrofitInstance
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.CreateActivityRequest
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.remote.dto.user.AuthResponse
import com.example.activityapp.data.remote.dto.user.ChangePasswordRequest
import com.example.activityapp.data.remote.dto.user.LoginUserRequest
import com.example.activityapp.data.remote.dto.user.RegisterUserRequest
import com.example.activityapp.data.remote.dto.user.UpdateUserRequest
import com.example.activityapp.data.remote.dto.user.UserResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessDetailsResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessMoodRequest
import okhttp3.MultipartBody

class ActivityRepository(private val context: Context) {

    private fun api() = RetrofitInstance.api(context)

    suspend fun registerUser(request: RegisterUserRequest): UserResponse {
        return api().registerUser(request)
    }

    suspend fun getUserById(id: Long): UserResponse {
        return api().getUserById(id)
    }

    suspend fun getCurrentUser(): UserResponse {
        return api().getCurrentUser()
    }

    suspend fun createActivity(request: CreateActivityRequest): ActivityResponse {
        return api().createActivity(request)
    }

    suspend fun getActivitiesByUser(userId: Long): List<ActivityResponse> {
        return api().getActivitiesByUser(userId)
    }

    suspend fun generateRecommendation(userId: Long): RecommendationResponse {
        return api().generateRecommendation(userId)
    }

    suspend fun getLatestRecommendation(userId: Long): RecommendationResponse {
        return api().getLatestRecommendation(userId)
    }

    suspend fun loginUser(request: LoginUserRequest): AuthResponse {
        return api().loginUser(request)
    }

    suspend fun getLatestActivity(userId: Long): ActivityResponse {
        return api().getLatestActivity(userId)
    }

    suspend fun uploadProfileImage(userId: Long, imagePart: MultipartBody.Part): UserResponse {
        return api().uploadProfileImage(userId, imagePart)
    }

    suspend fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse {
        return api().updateUser(userId, request)
    }

    suspend fun changePassword(userId: Long, request: ChangePasswordRequest) {
        api().changePassword(userId, request)
    }

    suspend fun getTodayWellness(userId: Long): WellnessDetailsResponse {
        return api().getTodayWellness(userId)
    }

    suspend fun saveMoodAndGetWellness(userId: Long, request: WellnessMoodRequest): WellnessDetailsResponse {
        return api().saveMoodAndGetWellness(userId, request)
    }
}