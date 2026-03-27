import random
import os
import pandas as pd
import joblib

from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, accuracy_score


def generate_recommendation(steps_today, avg_last_7_days, days_goal_reached_last_7, trend):
    if steps_today < 3000:
        return "LIGHT_WALK"
    elif steps_today < 7000:
        if trend < -1500:
            return "LIGHT_WALK"
        return "MODERATE_WALK"
    elif avg_last_7_days > 9000 and days_goal_reached_last_7 >= 5:
        return "RECOVERY_DAY"
    else:
        return "HOME_WORKOUT"


def generate_dataset(n=2000):
    data = []

    for _ in range(n):
        steps_today = random.randint(500, 15000)
        avg_last_7_days = random.randint(1000, 14000)
        days_goal_reached_last_7 = random.randint(0, 7)
        trend = steps_today - avg_last_7_days

        recommendation_type = generate_recommendation(
            steps_today,
            avg_last_7_days,
            days_goal_reached_last_7,
            trend
        )

        data.append({
            "steps_today": steps_today,
            "avg_last_7_days": avg_last_7_days,
            "days_goal_reached_last_7": days_goal_reached_last_7,
            "trend": trend,
            "recommendation_type": recommendation_type
        })

    return pd.DataFrame(data)


def main():
    os.makedirs("data", exist_ok=True)
    os.makedirs("model", exist_ok=True)

    df = generate_dataset(3000)
    df.to_csv("data/synthetic_activity_data.csv", index=False)

    X = df[["steps_today", "avg_last_7_days", "days_goal_reached_last_7", "trend"]]
    y = df["recommendation_type"]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )

    model = RandomForestClassifier(
        n_estimators=100,
        random_state=42
    )

    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)

    print("Accuracy:", accuracy_score(y_test, y_pred))
    print("\nClassification report:\n")
    print(classification_report(y_test, y_pred))

    joblib.dump(model, "model/recommendation_model.pkl")
    print("\nModel saved to model/recommendation_model.pkl")


if __name__ == "__main__":
    main()