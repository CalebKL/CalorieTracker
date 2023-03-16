package com.example.tracker_data.mapper

import com.example.tracker_data.local.entity.TrackedFoodEntity
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import java.time.LocalDate

fun TrackedFoodEntity.toTrackedFood(): TrackedFood{
    return TrackedFood(
        id = id,
        name = name,
        carbs = carbs,
        proteins = protein,
        fats = fat,
        imageUrl = imageUrl,
        mealType = MealType.fromString(type),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories
    )
}

fun TrackedFood.toTrackedFoodEntity():TrackedFoodEntity{
    return TrackedFoodEntity(
        name = name,
        carbs = carbs,
        protein = proteins,
        fat = fats,
        amount = amount,
        imageUrl = imageUrl,
        type = mealType.name,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        calories = calories,
        id = id
    )
}