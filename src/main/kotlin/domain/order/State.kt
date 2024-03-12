package domain.order

import domain.Result
import presentation.model.OutputModel

abstract class State(
    protected val orderController: OrderController
) {
    abstract fun getOrder(id: Int): OutputModel
    abstract fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel
    abstract fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel
    abstract fun getDuration(id: Int): OutputModel
    abstract fun removeOrder(id: Int): OutputModel
    abstract fun isPaid(id: Int): Boolean
    abstract fun pay(id: Int): Result
    abstract fun startCooking(id: Int): OutputModel
    abstract fun stopCooking(id: Int): OutputModel
    abstract fun isReady(id: Int): Boolean
    abstract fun isCooking(id: Int): Boolean
}

