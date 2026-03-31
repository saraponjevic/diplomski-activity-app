package com.example.activityapp.ui.screens.wellnesss

import com.example.activityapp.R

fun getWellnessImageRes(imageKey: String?): Int {
    return when (imageKey) {
        "wellness_go_outside" -> R.drawable.wellness_go_outside
        "wellness_breathing_exercise" -> R.drawable.wellness_breathing_exercise
        "wellness_take_break" -> R.drawable.wellness_take_break
        "wellness_relaxing_music" -> R.drawable.wellness_relaxing_music

        "wellness_rest" -> R.drawable.wellness_rest
        "wellness_hydrate" -> R.drawable.wellness_hydrate
        "wellness_light_walk" -> R.drawable.wellness_light_walk
        "wellness_light_snack" -> R.drawable.wellness_light_snack

        "wellness_workout" -> R.drawable.wellness_workout
        "wellness_set_goal" -> R.drawable.wellness_set_goal
        "wellness_socialize" -> R.drawable.wellness_socialize
        "wellness_go_outside_happy" -> R.drawable.wellness_go_outside_happy

        "wellness_short_walk" -> R.drawable.wellness_short_walk
        "wellness_stretch" -> R.drawable.wellness_stretch
        "wellness_small_break" -> R.drawable.wellness_small_break
        "wellness_relax_moment" -> R.drawable.wellness_relax_moment

        else -> R.drawable.wellness_placeholder
    }
}