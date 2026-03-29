package com.example.activityapp.ui.screens.nutrition

fun getMealImageRes(imageKey: String?): Int {
    return when (imageKey) {
        "breakfast_oatmeal_banana" -> com.example.activityapp.R.drawable.breakfast_oatmeal_banana
        "breakfast_yogurt_berries" -> com.example.activityapp.R.drawable.breakfast_yogurt_berries
        "breakfast_toast_peanut" -> com.example.activityapp.R.drawable.breakfast_toast_peanut

        "breakfast_eggs_toast" -> com.example.activityapp.R.drawable.breakfast_eggs_toast
        "breakfast_oats_apple" -> com.example.activityapp.R.drawable.breakfast_oats_apple
        "breakfast_cottage_fruit" -> com.example.activityapp.R.drawable.breakfast_cottage_fruit

        "breakfast_protein_oats_banana" -> com.example.activityapp.R.drawable.breakfast_protein_oats_banana
        "breakfast_eggs_avocado_toast" -> com.example.activityapp.R.drawable.breakfast_eggs_avocado_toast
        "breakfast_greek_yogurt_bowl" -> com.example.activityapp.R.drawable.breakfast_greek_yogurt_bowl

        "breakfast_oatmeal_berries" -> com.example.activityapp.R.drawable.breakfast_oatmeal_berries
        "breakfast_yogurt_banana" -> com.example.activityapp.R.drawable.breakfast_yogurt_banana
        "breakfast_eggs_toast_recovery" -> com.example.activityapp.R.drawable.breakfast_eggs_toast_recovery

        "lunch_chicken_salad" -> com.example.activityapp.R.drawable.lunch_chicken_salad
        "lunch_turkey_sandwich" -> com.example.activityapp.R.drawable.lunch_turkey_sandwich
        "lunch_rice_vegetables" -> com.example.activityapp.R.drawable.lunch_rice_vegetables

        "lunch_chicken_rice" -> com.example.activityapp.R.drawable.lunch_chicken_rice
        "lunch_pasta_tuna" -> com.example.activityapp.R.drawable.lunch_pasta_tuna
        "lunch_beef_stirfry" -> com.example.activityapp.R.drawable.lunch_beef_stirfry

        "lunch_chicken_rice_bowl" -> com.example.activityapp.R.drawable.lunch_chicken_rice_bowl
        "lunch_turkey_pasta" -> com.example.activityapp.R.drawable.lunch_turkey_pasta
        "lunch_quinoa_chicken" -> com.example.activityapp.R.drawable.lunch_quinoa_chicken

        "lunch_fish_rice" -> com.example.activityapp.R.drawable.lunch_fish_rice
        "lunch_chicken_soup" -> com.example.activityapp.R.drawable.lunch_chicken_soup
        "lunch_turkey_mashed_potato" -> com.example.activityapp.R.drawable.lunch_turkey_mashed_potato

        "dinner_vegetable_omelette" -> com.example.activityapp.R.drawable.dinner_vegetable_omelette
        "dinner_soup_bread" -> com.example.activityapp.R.drawable.dinner_soup_bread
        "dinner_chicken_broccoli" -> com.example.activityapp.R.drawable.dinner_chicken_broccoli

        "dinner_salmon_potatoes" -> com.example.activityapp.R.drawable.dinner_salmon_potatoes
        "dinner_chicken_wrap" -> com.example.activityapp.R.drawable.dinner_chicken_wrap
        "dinner_turkey_rice_bowl" -> com.example.activityapp.R.drawable.dinner_turkey_rice_bowl

        "dinner_salmon_rice_broccoli" -> com.example.activityapp.R.drawable.dinner_salmon_rice_broccoli
        "dinner_beef_sweet_potato" -> com.example.activityapp.R.drawable.dinner_beef_sweet_potato
        "dinner_chicken_stirfry" -> com.example.activityapp.R.drawable.dinner_chicken_stirfry

        "dinner_salmon_vegetables" -> com.example.activityapp.R.drawable.dinner_salmon_vegetables
        "dinner_omelette_spinach" -> com.example.activityapp.R.drawable.dinner_omelette_spinach
        "dinner_chicken_quinoa" -> com.example.activityapp.R.drawable.dinner_chicken_quinoa

        "snack_apple_almonds" -> com.example.activityapp.R.drawable.snack_apple_almonds
        "snack_protein_yogurt" -> com.example.activityapp.R.drawable.snack_protein_yogurt
        "snack_carrot_hummus" -> com.example.activityapp.R.drawable.snack_carrot_hummus

        "snack_banana_peanut" -> com.example.activityapp.R.drawable.snack_banana_peanut
        "snack_yogurt_granola" -> com.example.activityapp.R.drawable.snack_yogurt_granola
        "snack_nuts_driedfruit" -> com.example.activityapp.R.drawable.snack_nuts_driedfruit

        "snack_protein_shake_banana" -> com.example.activityapp.R.drawable.snack_protein_shake_banana
        "snack_ricecakes_peanut" -> com.example.activityapp.R.drawable.snack_ricecakes_peanut
        "snack_yogurt_berries" -> com.example.activityapp.R.drawable.snack_yogurt_berries

        "snack_banana_walnuts" -> com.example.activityapp.R.drawable.snack_banana_walnuts
        "snack_cottage_fruit" -> com.example.activityapp.R.drawable.snack_cottage_fruit

        else -> com.example.activityapp.R.drawable.placeholder_food
    }
}