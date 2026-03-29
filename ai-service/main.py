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


class FreeTimeRecommendation(BaseModel):
    activitySuggestion: str


class WellnessRecommendation(BaseModel):
    wellnessTip: str
    restTip: str


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
            freeTime=FreeTimeRecommendation(
                activitySuggestion="Take a relaxing 20-minute walk or read a book."
            ),
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
            freeTime=FreeTimeRecommendation(
                activitySuggestion="Spend part of your free time outdoors with a walk or light activity."
            ),
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
            freeTime=FreeTimeRecommendation(
                activitySuggestion="Choose a calm activity such as reading, a short walk or relaxing music."
            ),
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
        freeTime=FreeTimeRecommendation(
            activitySuggestion="Use 20-30 minutes of free time for a quick home workout."
        ),
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