package com.example.activityapp.ui.screens.freetime

import com.example.activityapp.R

fun getFreeTimeImageRes(category: String, sortOrder: Int): Int {
    return when (category) {
        "ACTIVE" -> {
            when (sortOrder) {
                1 -> R.drawable.free_active_1
                2 -> R.drawable.free_active_2
                3 -> R.drawable.free_active_1
                else -> R.drawable.free_active_1
            }
        }

        "CREATIVE" -> {
            when (sortOrder) {
                1 -> R.drawable.free_creative_1
                2 -> R.drawable.free_creative_2
                3 -> R.drawable.free_creative_1
                else -> R.drawable.free_creative_1
            }
        }

        "RELAX" -> {
            when (sortOrder) {
                1 -> R.drawable.free_relax_1
                2 -> R.drawable.free_relax_2
                3 -> R.drawable.free_relax_1
                else -> R.drawable.free_relax_1
            }
        }

        "SOCIAL" -> {
            when (sortOrder) {
                1 -> R.drawable.free_social_1
                2 -> R.drawable.free_social_2
                3 -> R.drawable.free_social_1
                else -> R.drawable.free_social_1
            }
        }

        "LEARNING" -> {
            when (sortOrder) {
                1 -> R.drawable.free_learning_1
                2 -> R.drawable.free_learning_2
                3 -> R.drawable.free_learning_1
                else -> R.drawable.free_learning_1
            }
        }

        "PRODUCTIVE" -> {
            when (sortOrder) {
                1 -> R.drawable.free_productive_1
                2 -> R.drawable.free_productive_2
                3 -> R.drawable.free_productive_1
                else -> R.drawable.free_productive_1
            }
        }

        "OUTDOOR" -> {
            when (sortOrder) {
                1 -> R.drawable.free_outdoor_1
                2 -> R.drawable.free_outdoor_2
                3 -> R.drawable.free_outdoor_1
                else -> R.drawable.free_outdoor_1
            }
        }

        "MINDFULNESS" -> {
            when (sortOrder) {
                1 -> R.drawable.free_mindfulness_1
                2 -> R.drawable.free_mindfulness_2
                3 -> R.drawable.free_mindfulness_1
                else -> R.drawable.free_mindfulness_1
            }
        }

        else -> R.drawable.placeholder_activity
    }
}