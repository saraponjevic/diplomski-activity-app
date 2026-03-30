package com.example.activityapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activityapp.data.remote.RetrofitInstance.api
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessDetailsResponse
import com.example.activityapp.data.remote.dto.wellness.WellnessMoodRequest
import com.example.activityapp.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = ActivityRepository()

    private val _latestActivity = MutableStateFlow<ActivityResponse?>(null)
    val latestActivity: StateFlow<ActivityResponse?> = _latestActivity

    private val _latestRecommendation = MutableStateFlow<RecommendationResponse?>(null)
    val latestRecommendation: StateFlow<RecommendationResponse?> = _latestRecommendation

    private val _wellnessDetails = MutableStateFlow<WellnessDetailsResponse?>(null)
    val wellnessDetails: StateFlow<WellnessDetailsResponse?> = _wellnessDetails

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _hasSelectedMood = MutableStateFlow(false)
    val hasSelectedMood: StateFlow<Boolean> = _hasSelectedMood

    fun loadDashboardData(userId: Long) {
        viewModelScope.launch {
            try {
                _latestActivity.value = repository.getLatestActivity(userId)
                _latestRecommendation.value = repository.getLatestRecommendation(userId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun loadTodayWellness(userId: Long) {
        viewModelScope.launch {
            try {
                val response = api.getTodayWellness(userId)
                _wellnessDetails.value = response

                _hasSelectedMood.value = true
            } catch (e: Exception) {
                _wellnessDetails.value = null
                _hasSelectedMood.value = false
                _errorMessage.value = null
            }
        }
    }

    fun selectMood(userId: Long, mood: String) {
        viewModelScope.launch {
            try {
                val response = api.saveMoodAndGetWellness(
                    userId,
                    WellnessMoodRequest(mood)
                )
                _wellnessDetails.value = response
                _hasSelectedMood.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}