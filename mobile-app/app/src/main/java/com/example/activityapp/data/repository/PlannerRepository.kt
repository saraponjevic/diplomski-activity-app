package com.example.activityapp.data.repository

import android.content.Context
import com.example.activityapp.data.remote.RetrofitInstance
import com.example.activityapp.data.remote.dto.planner.PlannerTaskCreateRequestDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskResponseDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskUpdateRequestDto

class PlannerRepository(private val context: Context) {

    suspend fun getTodayTasks(userId: Long): List<PlannerTaskResponseDto> {
        return RetrofitInstance.api(context).getTodayPlannerTasks(userId)
    }

    suspend fun addTask(userId: Long, request: PlannerTaskCreateRequestDto): PlannerTaskResponseDto {
        return RetrofitInstance.api(context).createPlannerTask(userId, request)
    }

    suspend fun updateTask(taskId: Long, request: PlannerTaskUpdateRequestDto): PlannerTaskResponseDto {
        return RetrofitInstance.api(context).updatePlannerTask(taskId, request)
    }

    suspend fun markCompleted(taskId: Long, completed: Boolean): PlannerTaskResponseDto {
        return RetrofitInstance.api(context).updatePlannerTaskCompleted(taskId, completed)
    }

    suspend fun deleteTask(taskId: Long) {
        RetrofitInstance.api(context).deletePlannerTask(taskId)
    }
}