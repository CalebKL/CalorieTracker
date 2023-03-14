package com.example.tracker_domain.use_case

import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.Gender
import com.example.core.domain.model.GoalType
import com.example.core.domain.model.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt


class CalculateMealNutrients(
    private val preferences: Preferences
) {
    operator fun invoke(trackedFood: List<TrackedFood>):Result{
        val allNutrients = trackedFood.groupBy {
            it.mealType
        }
            .mapValues {entry ->
                val type = entry.key
                val food = entry.value
                MealNutrients(
                    carbs = food.sumOf { it.carbs },
                    proteins = food.sumOf { it.proteins },
                    fat = food.sumOf { it.fats },
                    calories = food.sumOf { it.calories },
                    mealType = type
                )
            }
        val totalCarbs = allNutrients.values.sumOf { it.carbs }
        val totalProteins = allNutrients.values.sumOf { it.proteins }
        val totalFat = allNutrients.values.sumOf { it.fat }
        val totalCalories = allNutrients.values.sumOf { it.calories }
        val userInfo = preferences.loadUserInfo()
        val calorieGoal = dailyCaloryRequirement(userInfo)
        val carbsGoal = (calorieGoal * userInfo.carbRatio / 4f).roundToInt()
        val proteinGoal = (calorieGoal * userInfo.proteinRatio / 4f).roundToInt()
        val fatGoal = (calorieGoal * userInfo.fatRatio / 9f).roundToInt()
        return Result(
            carbsGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatGoal = fatGoal,
            caloriesGoal = calorieGoal,
            totalCarbs = totalCarbs,
            totalProteins = totalProteins,
            totalFat = totalFat,
            totalCalories = totalCalories,
            mealNutrients = allNutrients
        )
    }
    private fun bmr(userInfo: UserInfo): Int {
        return when(userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }
            is Gender.Female ->  {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    private fun dailyCaloryRequirement(userInfo: UserInfo): Int {
        val activityFactor = when(userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }
        val caloryExtra = when(userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userInfo) * activityFactor + caloryExtra).roundToInt()
    }

    data class MealNutrients(
        val carbs:Int,
        val proteins:Int,
        val fat:Int,
        val calories:Int,
        val mealType: MealType
    )
    data class Result(
        val carbsGoal:Int,
        val proteinGoal:Int,
        val fatGoal:Int,
        val caloriesGoal:Int,
        val totalCarbs:Int,
        val totalProteins:Int,
        val totalFat:Int,
        val totalCalories:Int,
        val mealNutrients:Map<MealType, MealNutrients>
    )
}
