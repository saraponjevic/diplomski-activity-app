from fastapi import FastAPI
from pydantic import BaseModel
from typing import Literal, List
from enum import Enum
import joblib

app = FastAPI()

model = joblib.load("model/recommendation_model.pkl")


class RecommendationRequest(BaseModel):
    stepsToday: int
    avgLast7Days: float
    daysGoalReachedLast7: int
    goalSteps: int


class MealType(str, Enum):
    BREAKFAST = "BREAKFAST"
    LUNCH = "LUNCH"
    DINNER = "DINNER"
    SNACK = "SNACK"


class MealSuggestion(BaseModel):
    mealType: MealType
    title: str
    description: str
    imageKey: str
    sortOrder: int
    calories: int
    recipe: List[str]


class NutritionRecommendation(BaseModel):
    nutritionStatus: str
    waterIntakeTip: str
    nutritionTip: str
    meals: List[MealSuggestion]


class FreeTimeCategory(str, Enum):
    ACTIVE = "ACTIVE"
    CREATIVE = "CREATIVE"
    RELAX = "RELAX"
    SOCIAL = "SOCIAL"
    LEARNING = "LEARNING"
    PRODUCTIVE = "PRODUCTIVE"
    OUTDOOR = "OUTDOOR"
    MINDFULNESS = "MINDFULNESS"


class FreeTimeActivity(BaseModel):
    title: str
    description: str
    category: FreeTimeCategory
    durationMinutes: int
    intensity: Literal["LOW", "MEDIUM", "HIGH"]
    imageKey: str
    sortOrder: int


class FreeTimeCategoryGroup(BaseModel):
    category: FreeTimeCategory
    activities: List[FreeTimeActivity]


class FreeTimeRecommendation(BaseModel):
    mainSuggestion: str
    headline: str
    categoryGroups: List[FreeTimeCategoryGroup]

def free_time_activity(
    title: str,
    description: str,
    category: FreeTimeCategory,
    duration_minutes: int,
    intensity: str,
    image_key: str,
    sort_order: int
) -> FreeTimeActivity:
    return FreeTimeActivity(
        title=title,
        description=description,
        category=category,
        durationMinutes=duration_minutes,
        intensity=intensity,
        imageKey=image_key,
        sortOrder=sort_order
    )


def free_time_group(
    category: FreeTimeCategory,
    activities: List[FreeTimeActivity]
) -> FreeTimeCategoryGroup:
    return FreeTimeCategoryGroup(
        category=category,
        activities=activities
    )


class WellnessRecommendation(BaseModel):
    wellnessTip: str
    restTip: str

class WellnessActionCard(BaseModel):
    imageKey: str
    title: str
    description: str
    sortOrder: int


class WellnessDetailsResponse(BaseModel):
    mood: str
    headline: str
    wellnessTip: str
    restTip: str
    actionCards: List[WellnessActionCard]


class MotivationRecommendation(BaseModel):
    message: str


class RecommendationResponse(BaseModel):
    dailyState: str
    recommendationType: Literal["LIGHT_WALK", "MODERATE_WALK", "HOME_WORKOUT", "RECOVERY_DAY"]
    intensity: Literal["LOW", "MEDIUM", "HIGH"]
    durationMinutes: int
    message: str
    notification: str
    nutrition: NutritionRecommendation
    freeTime: FreeTimeRecommendation
    wellness: WellnessRecommendation
    motivation: MotivationRecommendation


@app.get("/")
def root():
    return {"message": "AI service is running"}


def determine_daily_state(
    steps_today: int,
    avg_last_7_days: float,
    goal_steps: int,
    days_goal_reached_last_7: int
) -> str:
    goal_progress = steps_today / goal_steps if goal_steps > 0 else 0
    consistency_ratio = steps_today / avg_last_7_days if avg_last_7_days > 0 else 0

    if avg_last_7_days >= 9000 and days_goal_reached_last_7 >= 5 and goal_progress >= 1.0:
        return "RECOVERY_DAY"

    if goal_progress >= 1.0:
        return "GOAL_ACHIEVED"

    if goal_progress >= 0.8:
        return "NEAR_GOAL"

    if consistency_ratio >= 1.2 and goal_progress >= 0.6:
        return "ABOVE_USUAL_ACTIVITY"

    if goal_progress >= 0.5 and consistency_ratio >= 0.8:
        return "ON_TRACK"

    if goal_progress >= 0.25:
        return "LOW_ACTIVITY"

    return "VERY_LOW_ACTIVITY"


def meal(
    meal_type: MealType,
    title: str,
    description: str,
    image_key: str,
    sort_order: int,
    calories: int,
    recipe: List[str]
) -> MealSuggestion:
    return MealSuggestion(
        mealType=meal_type,
        title=title,
        description=description,
        imageKey=image_key,
        sortOrder=sort_order,
        calories=calories,
        recipe=recipe
    )


def build_light_walk_nutrition() -> NutritionRecommendation:
    return NutritionRecommendation(
        nutritionStatus="LIGHT_SUPPORT",
        waterIntakeTip="Try to drink 6-8 glasses of water today.",
        nutritionTip="Choose lighter meals with protein, vegetables and steady energy during the day.",
        meals=[
            meal(MealType.BREAKFAST, "Oatmeal with banana", "A light breakfast with fiber and natural energy.", "breakfast_oatmeal_banana", 1, 320, [
                "Cook oats with milk or water for 5 minutes.",
                "Slice one banana.",
                "Add banana on top of the oats.",
                "Serve warm."
            ]),
            meal(MealType.BREAKFAST, "Greek yogurt with berries", "Protein-rich and easy to digest.", "breakfast_yogurt_berries", 2, 250, [
                "Add Greek yogurt to a bowl.",
                "Top with mixed berries.",
                "Optionally add a little honey.",
                "Serve chilled."
            ]),
            meal(MealType.BREAKFAST, "Whole grain toast with peanut butter", "A quick breakfast with healthy fats.", "breakfast_toast_peanut", 3, 290, [
                "Toast two slices of whole grain bread.",
                "Spread peanut butter on top.",
                "Optionally add banana slices.",
                "Serve immediately."
            ]),

            meal(MealType.LUNCH, "Chicken salad bowl", "Lean protein with fresh vegetables.", "lunch_chicken_salad", 4, 430, [
                "Grill or pan-cook chicken breast.",
                "Prepare lettuce, tomato, cucumber and carrots.",
                "Slice the chicken and place over the salad.",
                "Add a light dressing and serve."
            ]),
            meal(MealType.LUNCH, "Turkey sandwich and salad", "Balanced and not too heavy.", "lunch_turkey_sandwich", 5, 410, [
                "Toast whole grain bread.",
                "Add turkey slices, lettuce and tomato.",
                "Prepare a small side salad.",
                "Serve sandwich with salad."
            ]),
            meal(MealType.LUNCH, "Rice with grilled vegetables", "Light and filling lunch option.", "lunch_rice_vegetables", 6, 390, [
                "Cook rice until soft.",
                "Grill zucchini, peppers and mushrooms.",
                "Mix rice with vegetables.",
                "Season lightly and serve."
            ]),

            meal(MealType.DINNER, "Vegetable omelette", "Simple evening meal with protein.", "dinner_vegetable_omelette", 7, 340, [
                "Whisk eggs in a bowl.",
                "Chop vegetables like spinach, tomato and peppers.",
                "Cook vegetables briefly in a pan.",
                "Add eggs and cook until set."
            ]),
            meal(MealType.DINNER, "Soup with whole grain bread", "Comforting and lighter for the evening.", "dinner_soup_bread", 8, 300, [
                "Heat vegetable or chicken soup.",
                "Toast whole grain bread.",
                "Serve soup hot with bread on the side."
            ]),
            meal(MealType.DINNER, "Grilled chicken with broccoli", "High protein and light dinner.", "dinner_chicken_broccoli", 9, 420, [
                "Season chicken breast and grill it.",
                "Steam or boil broccoli.",
                "Plate chicken with broccoli.",
                "Serve warm."
            ]),

            meal(MealType.SNACK, "Apple and almonds", "A practical snack with fiber and healthy fats.", "snack_apple_almonds", 10, 180, [
                "Wash and slice one apple.",
                "Add a small handful of almonds.",
                "Serve as a quick snack."
            ]),
            meal(MealType.SNACK, "Protein yogurt", "Helps keep you full between meals.", "snack_protein_yogurt", 11, 160, [
                "Open one serving of protein yogurt.",
                "Optional: add cinnamon or berries.",
                "Serve chilled."
            ]),
            meal(MealType.SNACK, "Carrot sticks with hummus", "Fresh and light snack option.", "snack_carrot_hummus", 12, 170, [
                "Peel and cut carrots into sticks.",
                "Serve with hummus on the side."
            ]),
        ]
    )


def build_moderate_walk_nutrition() -> NutritionRecommendation:
    return NutritionRecommendation(
        nutritionStatus="BALANCED_ENERGY",
        waterIntakeTip="Keep a steady water intake during the day, especially before and after activity.",
        nutritionTip="Focus on balanced meals with protein, vegetables and complex carbohydrates.",
        meals=[
            meal(MealType.BREAKFAST, "Eggs with whole grain toast", "Balanced breakfast with protein and carbs.", "breakfast_eggs_toast", 1, 360, [
                "Cook eggs to your preference.",
                "Toast whole grain bread.",
                "Serve eggs with toast."
            ]),
            meal(MealType.BREAKFAST, "Oats with apple and cinnamon", "A steady-energy breakfast option.", "breakfast_oats_apple", 2, 310, [
                "Cook oats with milk or water.",
                "Dice one apple.",
                "Add apple and cinnamon on top.",
                "Serve warm."
            ]),
            meal(MealType.BREAKFAST, "Cottage cheese and fruit", "Protein-rich and refreshing morning meal.", "breakfast_cottage_fruit", 3, 280, [
                "Add cottage cheese to a bowl.",
                "Top with sliced fruit.",
                "Serve chilled."
            ]),

            meal(MealType.LUNCH, "Grilled chicken with rice", "A strong balanced lunch for active days.", "lunch_chicken_rice", 4, 520, [
                "Cook rice until tender.",
                "Grill seasoned chicken breast.",
                "Serve chicken over rice with a side of vegetables."
            ]),
            meal(MealType.LUNCH, "Pasta with tuna and vegetables", "Good combination of carbs and protein.", "lunch_pasta_tuna", 5, 500, [
                "Boil pasta.",
                "Drain tuna and prepare chopped vegetables.",
                "Mix everything together with light seasoning.",
                "Serve warm."
            ]),
            meal(MealType.LUNCH, "Beef stir-fry with vegetables", "A filling lunch with protein and micronutrients.", "lunch_beef_stirfry", 6, 540, [
                "Slice beef thinly.",
                "Stir-fry beef in a pan.",
                "Add vegetables like peppers and broccoli.",
                "Cook until tender and serve."
            ]),

            meal(MealType.DINNER, "Salmon with potatoes", "Protein and healthy fats for recovery.", "dinner_salmon_potatoes", 7, 530, [
                "Bake or pan-cook salmon.",
                "Boil or roast potatoes.",
                "Serve salmon with potatoes."
            ]),
            meal(MealType.DINNER, "Chicken wrap with vegetables", "A lighter but satisfying dinner.", "dinner_chicken_wrap", 8, 450, [
                "Cook chicken strips.",
                "Fill a wrap with chicken and vegetables.",
                "Roll the wrap and serve."
            ]),
            meal(MealType.DINNER, "Rice bowl with turkey and veggies", "Balanced dinner after a more active day.", "dinner_turkey_rice_bowl", 9, 490, [
                "Cook rice.",
                "Cook ground turkey or turkey strips.",
                "Add mixed vegetables.",
                "Assemble in a bowl and serve."
            ]),

            meal(MealType.SNACK, "Banana and peanut butter", "Great for quick energy and satiety.", "snack_banana_peanut", 10, 220, [
                "Slice one banana.",
                "Serve with one spoon of peanut butter."
            ]),
            meal(MealType.SNACK, "Yogurt with granola", "Easy snack with carbs and protein.", "snack_yogurt_granola", 11, 210, [
                "Add yogurt to a bowl.",
                "Top with granola.",
                "Serve chilled."
            ]),
            meal(MealType.SNACK, "Mixed nuts and dried fruit", "Portable snack for sustained energy.", "snack_nuts_driedfruit", 12, 230, [
                "Mix nuts and dried fruit in a small bowl or container.",
                "Serve as a portable snack."
            ]),
        ]
    )


def build_home_workout_nutrition() -> NutritionRecommendation:
    return NutritionRecommendation(
        nutritionStatus="WORKOUT_SUPPORT",
        waterIntakeTip="Drink water before and after your workout to stay hydrated.",
        nutritionTip="Support your energy with meals rich in protein and complex carbs.",
        meals=[
            meal(MealType.BREAKFAST, "Protein oats with banana", "A strong breakfast before an active day.", "breakfast_protein_oats_banana", 1, 380, [
                "Cook oats.",
                "Add protein powder and stir well.",
                "Top with banana slices.",
                "Serve warm."
            ]),
            meal(MealType.BREAKFAST, "Scrambled eggs with avocado toast", "Protein and healthy fats for energy.", "breakfast_eggs_avocado_toast", 2, 420, [
                "Scramble eggs in a pan.",
                "Toast bread.",
                "Mash avocado onto toast.",
                "Serve eggs with avocado toast."
            ]),
            meal(MealType.BREAKFAST, "Greek yogurt bowl with fruit", "Simple high-protein breakfast.", "breakfast_greek_yogurt_bowl", 3, 300, [
                "Add Greek yogurt to a bowl.",
                "Top with fruit.",
                "Optionally add seeds or honey."
            ]),

            meal(MealType.LUNCH, "Chicken rice power bowl", "Great combination of protein and carbs.", "lunch_chicken_rice_bowl", 4, 560, [
                "Cook rice.",
                "Grill chicken breast.",
                "Prepare vegetables.",
                "Combine in a bowl and serve."
            ]),
            meal(MealType.LUNCH, "Turkey pasta with vegetables", "Supports energy for movement and recovery.", "lunch_turkey_pasta", 5, 540, [
                "Boil pasta.",
                "Cook turkey pieces.",
                "Add vegetables and combine with pasta.",
                "Serve warm."
            ]),
            meal(MealType.LUNCH, "Quinoa bowl with grilled chicken", "Balanced meal with protein and fiber.", "lunch_quinoa_chicken", 6, 510, [
                "Cook quinoa.",
                "Grill chicken.",
                "Add vegetables and assemble the bowl.",
                "Serve warm."
            ]),

            meal(MealType.DINNER, "Salmon with rice and broccoli", "Excellent dinner after a workout day.", "dinner_salmon_rice_broccoli", 7, 580, [
                "Cook rice.",
                "Bake or grill salmon.",
                "Steam broccoli.",
                "Serve together."
            ]),
            meal(MealType.DINNER, "Lean beef with sweet potato", "Recovery-focused dinner with protein.", "dinner_beef_sweet_potato", 8, 560, [
                "Cook lean beef in a pan.",
                "Roast or boil sweet potato.",
                "Serve together with seasoning."
            ]),
            meal(MealType.DINNER, "Chicken and vegetable stir-fry", "A lighter but still recovery-friendly dinner.", "dinner_chicken_stirfry", 9, 470, [
                "Cook chicken strips in a pan.",
                "Add mixed vegetables.",
                "Stir-fry until cooked through.",
                "Serve warm."
            ]),

            meal(MealType.SNACK, "Protein shake with banana", "Good post-workout snack option.", "snack_protein_shake_banana", 10, 240, [
                "Blend milk or water with protein powder.",
                "Add banana and blend again.",
                "Serve chilled."
            ]),
            meal(MealType.SNACK, "Rice cakes with peanut butter", "Quick source of energy.", "snack_ricecakes_peanut", 11, 210, [
                "Spread peanut butter on rice cakes.",
                "Serve immediately."
            ]),
            meal(MealType.SNACK, "Yogurt and berries", "Simple recovery snack.", "snack_yogurt_berries", 12, 180, [
                "Add yogurt to a bowl.",
                "Top with berries.",
                "Serve chilled."
            ]),
        ]
    )


def build_recovery_day_nutrition() -> NutritionRecommendation:
    return NutritionRecommendation(
        nutritionStatus="RECOVERY_FOCUS",
        waterIntakeTip="Increase your water intake after recent activity and prioritize hydration today.",
        nutritionTip="Focus on hydration, protein, gentle recovery meals and foods that are easy to digest.",
        meals=[
            meal(MealType.BREAKFAST, "Oatmeal with berries", "Gentle breakfast with fiber and antioxidants.", "breakfast_oatmeal_berries", 1, 300, [
                "Cook oats until soft.",
                "Top with berries.",
                "Serve warm."
            ]),
            meal(MealType.BREAKFAST, "Yogurt with banana", "Easy to digest and protein-rich.", "breakfast_yogurt_banana", 2, 240, [
                "Add yogurt to a bowl.",
                "Slice banana on top.",
                "Serve chilled."
            ]),
            meal(MealType.BREAKFAST, "Eggs and toast", "Simple breakfast with protein.", "breakfast_eggs_toast_recovery", 3, 330, [
                "Cook eggs.",
                "Toast bread.",
                "Serve together."
            ]),

            meal(MealType.LUNCH, "Grilled fish with rice", "Recovery-friendly meal with protein and carbs.", "lunch_fish_rice", 4, 500, [
                "Grill fish fillet.",
                "Cook rice.",
                "Serve fish with rice."
            ]),
            meal(MealType.LUNCH, "Chicken soup with vegetables", "Comforting meal for lighter recovery days.", "lunch_chicken_soup", 5, 360, [
                "Cook chicken in broth.",
                "Add chopped vegetables.",
                "Simmer until everything is tender.",
                "Serve hot."
            ]),
            meal(MealType.LUNCH, "Turkey with mashed potatoes", "Gentle and filling recovery lunch.", "lunch_turkey_mashed_potato", 6, 480, [
                "Cook turkey.",
                "Boil and mash potatoes.",
                "Serve together."
            ]),

            meal(MealType.DINNER, "Salmon with steamed vegetables", "Light dinner with healthy fats and protein.", "dinner_salmon_vegetables", 7, 460, [
                "Cook salmon.",
                "Steam vegetables.",
                "Serve together."
            ]),
            meal(MealType.DINNER, "Omelette with spinach", "Simple and light recovery dinner.", "dinner_omelette_spinach", 8, 320, [
                "Whisk eggs.",
                "Cook spinach briefly.",
                "Add eggs and cook until set.",
                "Serve warm."
            ]),
            meal(MealType.DINNER, "Chicken with quinoa", "Protein-rich evening meal for recovery.", "dinner_chicken_quinoa", 9, 470, [
                "Cook quinoa.",
                "Grill or pan-cook chicken.",
                "Serve chicken over quinoa."
            ]),

            meal(MealType.SNACK, "Protein yogurt", "Supports recovery between meals.", "snack_protein_yogurt", 10, 160, [
                "Open one serving of protein yogurt.",
                "Serve chilled."
            ]),
            meal(MealType.SNACK, "Banana and walnuts", "Good mix of carbs and healthy fats.", "snack_banana_walnuts", 11, 210, [
                "Slice banana.",
                "Add a small handful of walnuts.",
                "Serve as a quick snack."
            ]),
            meal(MealType.SNACK, "Cottage cheese with fruit", "Light snack with protein.", "snack_cottage_fruit", 12, 190, [
                "Add cottage cheese to a bowl.",
                "Top with fruit.",
                "Serve chilled."
            ]),
        ]
    )


def build_light_walk_free_time() -> FreeTimeRecommendation:
    return FreeTimeRecommendation(
        mainSuggestion="A light evening walk or gentle movement would be a great way to spend part of your free time today.",
        headline="You have been less active today, so here are some balanced ideas to help you move a little, refresh your mind, and use your free time well.",
        categoryGroups=[
            free_time_group(
                FreeTimeCategory.ACTIVE,
                [
                    free_time_activity("Short walk", "Take a light 15-minute walk to gently increase your movement today.", FreeTimeCategory.ACTIVE, 15, "LOW", "free_active_short_walk", 1),
                    free_time_activity("Stretching session", "Do simple stretches for your back, legs, and shoulders.", FreeTimeCategory.ACTIVE, 10, "LOW", "free_active_stretching", 2),
                    free_time_activity("Light home workout", "Try a few squats, arm circles, and gentle movement at home.", FreeTimeCategory.ACTIVE, 20, "MEDIUM", "free_active_light_workout", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.CREATIVE,
                [
                    free_time_activity("Sketch something simple", "Draw something around you without overthinking it.", FreeTimeCategory.CREATIVE, 15, "LOW", "free_creative_sketch", 1),
                    free_time_activity("Journal your thoughts", "Write a few lines about your day, ideas, or plans.", FreeTimeCategory.CREATIVE, 15, "LOW", "free_creative_journal", 2),
                    free_time_activity("Make a photo collage idea", "Collect a few photos or ideas you find visually interesting.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_photo_collage", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.PRODUCTIVE,
                [
                    free_time_activity("Tidy your desk", "Clean and organize your workspace for a fresh start.", FreeTimeCategory.PRODUCTIVE, 15, "LOW", "free_productive_tidy_desk", 1),
                    free_time_activity("Organize your room", "Do a quick reset of your room or one small area.", FreeTimeCategory.PRODUCTIVE, 20, "MEDIUM", "free_productive_organize_room", 2),
                    free_time_activity("Plan tomorrow", "Write down your main tasks and priorities for tomorrow.", FreeTimeCategory.PRODUCTIVE, 10, "LOW", "free_productive_plan_tomorrow", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.OUTDOOR,
                [
                    free_time_activity("Park walk", "Go outside for fresh air and a calm walk through a nearby park.", FreeTimeCategory.OUTDOOR, 20, "LOW", "free_outdoor_park_walk", 1),
                    free_time_activity("Easy bike ride", "Take a short and easy ride if you want light outdoor activity.", FreeTimeCategory.OUTDOOR, 25, "MEDIUM", "free_outdoor_bike_ride", 2),
                    free_time_activity("Photography walk", "Walk outside and take photos of things that catch your attention.", FreeTimeCategory.OUTDOOR, 20, "LOW", "free_outdoor_photo_walk", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.MINDFULNESS,
                [
                    free_time_activity("Breathing break", "Take a few calm breaths and slow down for a moment.", FreeTimeCategory.MINDFULNESS, 5, "LOW", "free_mindfulness_breathing", 1),
                    free_time_activity("Quiet reflection", "Sit without distractions and let your mind settle a little.", FreeTimeCategory.MINDFULNESS, 10, "LOW", "free_mindfulness_reflection", 2),
                    free_time_activity("Gratitude notes", "Write down three small things you are grateful for today.", FreeTimeCategory.MINDFULNESS, 10, "LOW", "free_mindfulness_gratitude", 3),
                ]
            ),
        ]
    )

def build_moderate_walk_free_time() -> FreeTimeRecommendation:
    return FreeTimeRecommendation(
        mainSuggestion="You are doing well today, so a balanced mix of movement, relaxation, and meaningful free time would fit you nicely.",
        headline="Your activity level looks balanced today, so here are some ideas across different categories to keep your rhythm going.",
        categoryGroups=[
            free_time_group(
                FreeTimeCategory.ACTIVE,
                [
                    free_time_activity("Brisk walk", "Keep your momentum with a stronger evening walk.", FreeTimeCategory.ACTIVE, 25, "MEDIUM", "free_active_brisk_walk", 1),
                    free_time_activity("Bodyweight session", "Do a short bodyweight routine to stay active.", FreeTimeCategory.ACTIVE, 20, "MEDIUM", "free_active_bodyweight", 2),
                    free_time_activity("Dance break", "Put on music and move for a short energizing session.", FreeTimeCategory.ACTIVE, 15, "MEDIUM", "free_active_dance", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.CREATIVE,
                [
                    free_time_activity("Drawing time", "Spend a little time drawing or doodling.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_drawing", 1),
                    free_time_activity("Creative writing", "Write a short story idea, note, or reflection.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_writing", 2),
                    free_time_activity("Design inspiration board", "Collect images and ideas that inspire you.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_inspiration_board", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.RELAX,
                [
                    free_time_activity("Read a chapter", "Unwind with a calm reading break.", FreeTimeCategory.RELAX, 20, "LOW", "free_relax_reading", 1),
                    free_time_activity("Tea and quiet time", "Pause the day and enjoy a simple calm moment.", FreeTimeCategory.RELAX, 15, "LOW", "free_relax_tea", 2),
                    free_time_activity("Watch something light", "Choose a short and relaxing video or episode.", FreeTimeCategory.RELAX, 30, "LOW", "free_relax_watch_light", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.SOCIAL,
                [
                    free_time_activity("Coffee with a friend", "Spend relaxed time talking with someone you enjoy.", FreeTimeCategory.SOCIAL, 45, "LOW", "free_social_coffee", 1),
                    free_time_activity("Call someone close", "A short warm conversation can lift your mood.", FreeTimeCategory.SOCIAL, 15, "LOW", "free_social_call_friend", 2),
                    free_time_activity("Share progress", "Talk to someone about your day or goals.", FreeTimeCategory.SOCIAL, 10, "LOW", "free_social_share_progress", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.LEARNING,
                [
                    free_time_activity("Watch a short course", "Learn something interesting in a focused short session.", FreeTimeCategory.LEARNING, 20, "LOW", "free_learning_course", 1),
                    free_time_activity("Read an article", "Choose one useful article or topic you want to explore.", FreeTimeCategory.LEARNING, 15, "LOW", "free_learning_article", 2),
                    free_time_activity("Practice a skill", "Spend some time improving a skill you care about.", FreeTimeCategory.LEARNING, 25, "MEDIUM", "free_learning_skill", 3),
                ]
            ),
        ]
    )

def build_home_workout_free_time() -> FreeTimeRecommendation:
    return FreeTimeRecommendation(
        mainSuggestion="You have enough energy for something active today, so your free time can include movement plus a few meaningful lighter options.",
        headline="You seem ready for a stronger free time routine today, so here are ideas that combine activity, focus, and balance.",
        categoryGroups=[
            free_time_group(
                FreeTimeCategory.ACTIVE,
                [
                    free_time_activity("Quick home workout", "Use 20 minutes for a stronger movement session.", FreeTimeCategory.ACTIVE, 20, "MEDIUM", "free_active_home_workout", 1),
                    free_time_activity("Core training", "Do a short session focused on posture and core strength.", FreeTimeCategory.ACTIVE, 15, "MEDIUM", "free_active_core", 2),
                    free_time_activity("HIIT session", "Try a short high-energy session if you feel motivated.", FreeTimeCategory.ACTIVE, 15, "HIGH", "free_active_hiit", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.PRODUCTIVE,
                [
                    free_time_activity("Plan your week", "Use your momentum to organize upcoming responsibilities.", FreeTimeCategory.PRODUCTIVE, 20, "LOW", "free_productive_plan_week", 1),
                    free_time_activity("Finish one small task", "Complete something that has been waiting.", FreeTimeCategory.PRODUCTIVE, 25, "MEDIUM", "free_productive_small_task", 2),
                    free_time_activity("Clean your digital space", "Organize files, notes, or your desktop.", FreeTimeCategory.PRODUCTIVE, 20, "LOW", "free_productive_digital_cleanup", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.OUTDOOR,
                [
                    free_time_activity("Longer outdoor walk", "Stay active and clear your head outside.", FreeTimeCategory.OUTDOOR, 30, "MEDIUM", "free_outdoor_long_walk", 1),
                    free_time_activity("Bike ride", "Use your energy for some fun movement outdoors.", FreeTimeCategory.OUTDOOR, 35, "MEDIUM", "free_outdoor_bike", 2),
                    free_time_activity("Outdoor stretch break", "Go outside and do a light movement reset.", FreeTimeCategory.OUTDOOR, 10, "LOW", "free_outdoor_stretch", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.SOCIAL,
                [
                    free_time_activity("Workout with a friend", "Invite someone to move with you or keep each other motivated.", FreeTimeCategory.SOCIAL, 30, "MEDIUM", "free_social_workout_friend", 1),
                    free_time_activity("Share your progress", "Tell someone about your active day and goals.", FreeTimeCategory.SOCIAL, 10, "LOW", "free_social_progress", 2),
                    free_time_activity("Plan a meetup", "Use your good energy to arrange something pleasant.", FreeTimeCategory.SOCIAL, 15, "LOW", "free_social_meetup_plan", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.RELAX,
                [
                    free_time_activity("Post-workout stretching", "Let your body calm down with gentle stretching.", FreeTimeCategory.RELAX, 10, "LOW", "free_relax_postworkout_stretch", 1),
                    free_time_activity("Warm shower and reset", "Recover with a calm and refreshing break.", FreeTimeCategory.RELAX, 15, "LOW", "free_relax_shower", 2),
                    free_time_activity("Music break", "Rest with music after being productive or active.", FreeTimeCategory.RELAX, 15, "LOW", "free_relax_music", 3),
                ]
            ),
        ]
    )


def build_recovery_day_free_time() -> FreeTimeRecommendation:
    return FreeTimeRecommendation(
        mainSuggestion="You have already been quite active, so your free time is better used for recovery, calm habits, and lighter enjoyable activities.",
        headline="You have done a lot recently, so today’s free time suggestions focus more on rest, mindfulness, and gentle recovery.",
        categoryGroups=[
            free_time_group(
                FreeTimeCategory.RELAX,
                [
                    free_time_activity("Gentle stretching", "Release tension with calm full-body stretching.", FreeTimeCategory.RELAX, 10, "LOW", "free_relax_gentle_stretch", 1),
                    free_time_activity("Reading break", "Choose something light and enjoyable to unwind.", FreeTimeCategory.RELAX, 20, "LOW", "free_relax_read_book", 2),
                    free_time_activity("Movie night", "Give yourself a calm evening with something easy to watch.", FreeTimeCategory.RELAX, 60, "LOW", "free_relax_movie", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.MINDFULNESS,
                [
                    free_time_activity("Breathing session", "Slow your breathing and let your body settle.", FreeTimeCategory.MINDFULNESS, 5, "LOW", "free_mindfulness_breathing_session", 1),
                    free_time_activity("Mindful walking", "Take a slow walk and focus on each step, your breathing and surroundings.", FreeTimeCategory.MINDFULNESS, 10, "LOW", "free_mindfulness_walk", 2),
                    free_time_activity("Body scan", "Notice tension and relax each part of your body.", FreeTimeCategory.MINDFULNESS, 10, "LOW", "free_mindfulness_body_scan", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.CREATIVE,
                [
                    free_time_activity("Sketching", "Do something creative without pressure.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_sketching", 1),
                    free_time_activity("Play music", "Listen or gently play something you enjoy.", FreeTimeCategory.CREATIVE, 20, "LOW", "free_creative_music", 2),
                    free_time_activity("Journaling", "Write down your thoughts and reset mentally.", FreeTimeCategory.CREATIVE, 15, "LOW", "free_creative_recovery_journal", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.SOCIAL,
                [
                    free_time_activity("Light conversation", "Talk to someone in a calm and easy way.", FreeTimeCategory.SOCIAL, 15, "LOW", "free_social_light_conversation", 1),
                    free_time_activity("Coffee break", "Enjoy a simple relaxed moment with someone.", FreeTimeCategory.SOCIAL, 30, "LOW", "free_social_coffee_break", 2),
                    free_time_activity("Board game or casual time", "Spend time with others in a low-pressure way.", FreeTimeCategory.SOCIAL, 40, "LOW", "free_social_board_game", 3),
                ]
            ),
            free_time_group(
                FreeTimeCategory.LEARNING,
                [
                    free_time_activity("Watch something inspiring", "Choose a calm and interesting short topic.", FreeTimeCategory.LEARNING, 20, "LOW", "free_learning_inspiring_video", 1),
                    free_time_activity("Read a few pages", "Keep your mind engaged without tiring yourself.", FreeTimeCategory.LEARNING, 15, "LOW", "free_learning_read_pages", 2),
                    free_time_activity("Explore a hobby topic", "Read or watch content about something you enjoy.", FreeTimeCategory.LEARNING, 20, "LOW", "free_learning_hobby_topic", 3),
                ]
            ),
        ]
    )


def build_response(recommendation_type: str, daily_state: str) -> RecommendationResponse:
    if recommendation_type == "LIGHT_WALK":
        return RecommendationResponse(
            dailyState=daily_state,
            recommendationType="LIGHT_WALK",
            intensity="LOW",
            durationMinutes=30,
            message="A light 30-minute walk is recommended today.",
            notification="Try a short walk today to increase your activity.",
            nutrition=build_light_walk_nutrition(),
            freeTime=build_light_walk_free_time(),
            wellness=WellnessRecommendation(
                wellnessTip="Do 5 minutes of light stretching for your back and legs.",
                restTip="Try to go to bed a little earlier tonight."
            ),
            motivation=MotivationRecommendation(
                message="Every step counts. Start small and keep going."
            )
        )

    if recommendation_type == "MODERATE_WALK":
        return RecommendationResponse(
            dailyState=daily_state,
            recommendationType="MODERATE_WALK",
            intensity="MEDIUM",
            durationMinutes=40,
            message="A moderate 40-minute walk is recommended today.",
            notification="You are doing well. Keep moving with a moderate walk.",
            nutrition=build_moderate_walk_nutrition(),
            freeTime=build_moderate_walk_free_time(),
            wellness=WellnessRecommendation(
                wellnessTip="Take a short stretching break after sitting for a long time.",
                restTip="Give yourself a short screen-free break this evening."
            ),
            motivation=MotivationRecommendation(
                message="Nice progress today. Keep up the good work."
            )
        )

    if recommendation_type == "RECOVERY_DAY":
        return RecommendationResponse(
            dailyState=daily_state,
            recommendationType="RECOVERY_DAY",
            intensity="LOW",
            durationMinutes=20,
            message="A recovery day is recommended due to your recent activity level.",
            notification="Take it easier today and focus on recovery.",
            nutrition=build_recovery_day_nutrition(),
            freeTime=build_recovery_day_free_time(),
            wellness=WellnessRecommendation(
                wellnessTip="Do gentle stretching and avoid overexertion today.",
                restTip="Prioritize sleep and recovery tonight."
            ),
            motivation=MotivationRecommendation(
                message="You have been active lately. Recovery is also part of progress."
            )
        )

    return RecommendationResponse(
        dailyState=daily_state,
        recommendationType="HOME_WORKOUT",
        intensity="MEDIUM",
        durationMinutes=25,
        message="A short home workout is recommended today.",
        notification="Stay active today with a quick home workout.",
        nutrition=build_home_workout_nutrition(),
        freeTime=build_home_workout_free_time(),
        wellness=WellnessRecommendation(
            wellnessTip="Warm up before activity and stretch after exercise.",
            restTip="After your workout, allow some time for relaxation."
        ),
        motivation=MotivationRecommendation(
            message="You can do this. A short workout today is a great investment in yourself."
        )
    )





@app.post("/recommend", response_model=RecommendationResponse)
def recommend(request: RecommendationRequest):
    trend = request.stepsToday - request.avgLast7Days

    features = [[
        request.stepsToday,
        request.avgLast7Days,
        request.daysGoalReachedLast7,
        trend
    ]]

    prediction = model.predict(features)[0]

    daily_state = determine_daily_state(
        steps_today=request.stepsToday,
        avg_last_7_days=request.avgLast7Days,
        goal_steps=request.goalSteps,
        days_goal_reached_last_7=request.daysGoalReachedLast7
    )

    return build_response(prediction, daily_state)




def build_wellness_response(mood: str) -> WellnessDetailsResponse:
    if mood == "STRESSED":
        return WellnessDetailsResponse(
            mood="STRESSED",
            headline="It is okay to slow down today. Give yourself space to breathe and reset.",
            wellnessTip="Choose calm moments, less pressure and gentle activities today.",
            restTip="Try to reduce screen time tonight and rest a little earlier.",
            actionCards=[
                WellnessActionCard(imageKey="wellness_go_outside", title="Go outside", description="Spend 10 minutes in fresh air", sortOrder=1),
                WellnessActionCard(imageKey="wellness_breathing_exercise", title="Breathing exercise", description="Take 5 slow deep breaths", sortOrder=2),
                WellnessActionCard(imageKey="wellness_take_break", title="Take a break", description="Step away from screens for a while", sortOrder=3),
                WellnessActionCard(imageKey="wellness_relaxing_music", title="Relaxing music", description="Listen to something calming", sortOrder=4),
            ]
        )

    if mood == "TIRED":
        return WellnessDetailsResponse(
            mood="TIRED",
            headline="You seem tired today, so focus on recovery and lighter choices.",
            wellnessTip="Keep your day gentler and save your energy where it matters most.",
            restTip="Try to rest earlier tonight and avoid overloading your schedule.",
            actionCards=[
                WellnessActionCard(imageKey="wellness_rest", title="Rest", description="Take a short nap or relax", sortOrder=1),
                WellnessActionCard(imageKey="wellness_hydrate", title="Hydrate", description="Drink water to boost energy", sortOrder=2),
                WellnessActionCard(imageKey="wellness_light_walk", title="Light walk", description="Gentle movement can help", sortOrder=3),
                WellnessActionCard(imageKey="wellness_light_snack", title="Light snack", description="Eat something light and healthy", sortOrder=4),
            ]
        )

    if mood == "HAPPY":
        return WellnessDetailsResponse(
            mood="HAPPY",
            headline="It is great that you feel happy today. Keep that positive energy going.",
            wellnessTip="Use your good mood to stay active and do something meaningful today.",
            restTip="End the day with something enjoyable and keep your balance.",
            actionCards=[
                WellnessActionCard(imageKey="wellness_workout", title="Workout", description="Use your energy for activity", sortOrder=1),
                WellnessActionCard(imageKey="wellness_set_goal", title="Set a goal", description="Try reaching your step goal today", sortOrder=2),
                WellnessActionCard(imageKey="wellness_socialize", title="Socialize", description="Share your good mood with others", sortOrder=3),
                WellnessActionCard(imageKey="wellness_go_outside_happy", title="Go outside", description="Enjoy the day", sortOrder=4),
            ]
        )

    return WellnessDetailsResponse(
        mood="OKAY",
        headline="You seem okay today. A few small actions can make your day even better.",
        wellnessTip="Try light movement, small pauses and simple habits that refresh you.",
        restTip="Keep a calm evening routine and give yourself enough rest.",
        actionCards=[
            WellnessActionCard(imageKey="wellness_short_walk", title="Short walk", description="Take a short walk", sortOrder=1),
            WellnessActionCard(imageKey="wellness_stretch", title="Stretch", description="Do a few gentle stretches", sortOrder=2),
            WellnessActionCard(imageKey="wellness_small_break", title="Take a small break", description="Pause for a calm moment", sortOrder=3),
            WellnessActionCard(imageKey="wellness_relax_moment", title="Relax moment", description="Enjoy a quiet and relaxing moment", sortOrder=4),
        ]
    )



@app.get("/wellness/{mood}", response_model=WellnessDetailsResponse)
def get_wellness_by_mood(mood: str):
    return build_wellness_response(mood.upper())