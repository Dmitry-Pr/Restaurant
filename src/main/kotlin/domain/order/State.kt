package domain.order

import presentation.model.OutputModel

abstract class State(
    protected val orderController: OrderController
) {
    abstract fun getOrder(id: Int): OutputModel
    abstract fun addMeal(id: Int, mealId: Int): OutputModel
    abstract fun removeMeal(id: Int, mealId: Int): OutputModel
    abstract fun getDuration(id: Int): OutputModel
    abstract fun removeOrder(id: Int): OutputModel
    abstract fun isPaid(id: Int): Boolean
    abstract fun pay(id: Int): OutputModel
    abstract fun startCooking(id: Int): OutputModel
    abstract fun isReady(id: Int): Boolean
    abstract fun isCooking(id: Int): Boolean
}

