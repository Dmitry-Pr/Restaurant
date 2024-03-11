package presentation.input

import presentation.model.OutputModel

interface MealCommandsHandler {
    fun addMeal(): OutputModel
    fun changeMealName(): OutputModel
    fun changeMealPrice(): OutputModel
    fun changeMealDuration(): OutputModel
    fun changeMealAmount(): OutputModel
    fun showAllMeals(): OutputModel
}