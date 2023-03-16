package com.example.tracker_domain.use_case

import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.Gender
import com.example.core.domain.model.GoalType
import com.example.core.domain.model.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random


@OptIn(ExperimentalCoroutinesApi::class)
class CalculateMealNutrientsTest {

    private var preferences = mockk<Preferences>(relaxed = true)
    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setup(){
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 10,
            weight = 10f,
            height = 80,
            activityLevel = ActivityLevel.High,
            goalType = GoalType.LoseWeight,
            carbRatio = 10f,
            proteinRatio = 40f ,
            fatRatio = 10f
        )
        calculateMealNutrients = CalculateMealNutrients(preferences)
    }
    @Test
    fun `Carbs for Dinner properly calculated `()= runTest {
        val trackedFood = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                proteins = Random.nextInt(100),
                fats = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }
        val result = calculateMealNutrients(trackedFood)
        val dinnerCarbs = result.mealNutrients.values
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carbs }

        val expectedDinner =trackedFood
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carbs }

        Truth.assertThat(dinnerCarbs).isEqualTo(expectedDinner)
    }
    @Test
    fun `Calories for breakfast properly calculated `()= runTest {
        val trackedFood = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                proteins = Random.nextInt(100),
                fats = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }
        val result = calculateMealNutrients(trackedFood)
        val breakFastCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        val expectedCalories =trackedFood
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        Truth.assertThat(breakFastCalories).isEqualTo(expectedCalories)
    }
}