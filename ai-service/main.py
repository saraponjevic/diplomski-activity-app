from fastapi import FastAPI
from pydantic import BaseModel
from typing import Literal
import joblib

app = FastAPI()

model = joblib.load("model/recommendation_model.pkl")


class RecommendationRequest(BaseModel):
    stepsToday: int
    avgLast7Days: float
    daysGoalReachedLast7: int
    goalSteps: int


class NutritionRecommendation(BaseModel):
    mealSuggestion: str
    waterIntakeTip: str
    nutritionTip: str


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


def build_response(recommendation_type: str, daily_state: str) -> RecommendationResponse:
    if recommendation_type == "LIGHT_WALK":
        return RecommendationResponse(
            dailyState=daily_state,
            recommendationType="LIGHT_WALK",
            intensity="LOW",
            durationMinutes=30,
            message="A light 30-minute walk is recommended today.",
            notification="Try a short walk today to increase your activity.",
            nutrition=NutritionRecommendation(
                mealSuggestion="Chicken salad with whole grain bread",
                waterIntakeTip="Try to drink 6-8 glasses of water today.",
                nutritionTip="Choose lighter meals and include more protein today."
            ),
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
            nutrition=NutritionRecommendation(
                mealSuggestion="Omelette with vegetables and yogurt",
                waterIntakeTip="Keep a steady water intake during the day.",
                nutritionTip="A balanced meal with protein and vegetables would be a good choice today."
            ),
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
            nutrition=NutritionRecommendation(
                mealSuggestion="Grilled fish or chicken with rice and vegetables",
                waterIntakeTip="Increase your water intake after recent activity.",
                nutritionTip="Focus on protein, hydration and recovery-friendly meals today."
            ),
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
        nutrition=NutritionRecommendation(
            mealSuggestion="Eggs, oats and fruit or a protein-rich lunch",
            waterIntakeTip="Drink water before and after your workout.",
            nutritionTip="Support your energy with a meal rich in protein and complex carbs."
        ),
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