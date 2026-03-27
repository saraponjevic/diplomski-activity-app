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


class RecommendationResponse(BaseModel):
    recommendationType: Literal["LIGHT_WALK", "MODERATE_WALK", "HOME_WORKOUT", "RECOVERY_DAY"]
    intensity: Literal["LOW", "MEDIUM", "HIGH"]
    durationMinutes: int
    message: str
    notification: str


@app.get("/")
def root():
    return {"message": "AI service is running"}


def build_response(recommendation_type: str) -> RecommendationResponse:
    if recommendation_type == "LIGHT_WALK":
        return RecommendationResponse(
            recommendationType="LIGHT_WALK",
            intensity="LOW",
            durationMinutes=30,
            message="A light 30-minute walk is recommended today.",
            notification="Try a short walk today to increase your activity."
        )

    if recommendation_type == "MODERATE_WALK":
        return RecommendationResponse(
            recommendationType="MODERATE_WALK",
            intensity="MEDIUM",
            durationMinutes=40,
            message="A moderate 40-minute walk is recommended today.",
            notification="You are doing well. Keep moving with a moderate walk."
        )

    if recommendation_type == "RECOVERY_DAY":
        return RecommendationResponse(
            recommendationType="RECOVERY_DAY",
            intensity="LOW",
            durationMinutes=20,
            message="A recovery day is recommended due to your recent activity level.",
            notification="Take it easier today and focus on recovery."
        )

    return RecommendationResponse(
        recommendationType="HOME_WORKOUT",
        intensity="MEDIUM",
        durationMinutes=25,
        message="A short home workout is recommended today.",
        notification="Stay active today with a quick home workout."
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

    return build_response(prediction)