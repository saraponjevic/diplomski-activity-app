package com.example.activityapp.ui.screens.freetime

fun getCategoryDisplayName(category: String): String {
    return when (category) {
        "ACTIVE" -> "Active"
        "CREATIVE" -> "Creative"
        "RELAX" -> "Relax"
        "SOCIAL" -> "Social"
        "LEARNING" -> "Learning"
        "PRODUCTIVE" -> "Productive"
        "OUTDOOR" -> "Outdoor"
        "MINDFULNESS" -> "Mindfulness"
        else -> category
    }
}

fun getIntensityLabel(intensity: String): String {
    return when (intensity) {
        "LOW" -> "Low intensity"
        "MEDIUM" -> "Medium intensity"
        "HIGH" -> "High intensity"
        else -> intensity
    }
}

fun getFreeTimeCategoryOrder(category: String): Int {
    return when (category) {
        "ACTIVE" -> 0
        "CREATIVE" -> 1
        "RELAX" -> 2
        "SOCIAL" -> 3
        "LEARNING" -> 4
        "PRODUCTIVE" -> 5
        "OUTDOOR" -> 6
        "MINDFULNESS" -> 7
        else -> 99
    }
}